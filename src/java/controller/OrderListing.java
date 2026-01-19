/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Hibernate.Order;
import Hibernate.OrderItem;
import Hibernate.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dinus
 */
@WebServlet(name = "OrderListing", urlPatterns = {"/OrderListing"})
public class OrderListing extends HttpServlet {
    
  @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    JsonObject responseObject = new JsonObject();
    responseObject.addProperty("status", false);

    SessionFactory sf = Hibernate.HibernateUtile.getSessionFactory();
    Session s = sf.openSession();
    Transaction tx = null;

    try {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            responseObject.addProperty("message", "Session Expired or not logged in.");
            writeJsonResponse(response, responseObject);
            return;
        }

        tx = s.beginTransaction();

        Criteria c1 = s.createCriteria(Order.class);
        c1.add(Restrictions.eq("user", user));
        List<Order> orderList = c1.list();

        List<OrderItem> orderItems = new ArrayList<>();

        if (orderList != null && !orderList.isEmpty()) { // ✅ FIXED condition
            Criteria c2 = s.createCriteria(OrderItem.class);
            c2.add(Restrictions.in("order", orderList));
            orderItems = c2.list();
        }

        responseObject.addProperty("status", true);
        responseObject.add("orderItems", new Gson().toJsonTree(orderItems));

        tx.commit();
    } catch (Exception e) {
        if (tx != null) {
            tx.rollback();
        }
        e.printStackTrace();
        responseObject.addProperty("message", "Error occurred while retrieving Orders.");
    } finally {
        s.close();
    }

    writeJsonResponse(response, responseObject);
}

private void writeJsonResponse(HttpServletResponse response, JsonObject json) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json.toString());
}

}
