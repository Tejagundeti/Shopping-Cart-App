package com.demo.Servlet;

import com.demo.Servlet.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewCartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        List<String> cart = (List<String>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Retrieve items from the database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT item_name FROM cart";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    cart.add(resultSet.getString("item_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Display the cart items
        out.println("<html><head><title>Your Shopping Cart</title></head><body>");
        out.println("<h1>Your Shopping Cart</h1>");

        if (cart.isEmpty()) {
            out.println("<p>Your cart is empty.</p>");
        } else {
            out.println("<ul>");
            for (String item : cart) {
                out.println("<li>" + item + "</li>");
            }
            out.println("</ul>");
        }

        out.println("<a href='shop.html'>Continue Shopping</a><br>");
        out.println("<a href='clearCart'>Clear Cart</a>");
        out.println("</body></html>");
    }
}
