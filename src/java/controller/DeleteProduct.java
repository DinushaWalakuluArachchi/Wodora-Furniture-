/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Hibernate.Cart;
import Hibernate.HibernateUtile;
import Hibernate.SellersProduct;
import Hibernate.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
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
@WebServlet(name = "DeleteProduct", urlPatterns = {"/DeleteProduct"})
public class DeleteProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);


        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        int productId = requestJsonObject.get("id").getAsInt();

        SessionFactory sf = HibernateUtile.getSessionFactory();
        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();

        User user = (User) request.getSession().getAttribute("user");

        Criteria c1 = s.createCriteria(Cart.class);
        c1.add(Restrictions.eq("product.id", productId));
        c1.add(Restrictions.eq("user", user));
        Cart cart = (Cart) c1.uniqueResult();
        
        if (cart != null) {
            s.delete(cart);
            tr.commit();
            responseObject.addProperty("status", true);
            responseObject.addProperty("message", "Product deleted from cart successfully");
        } else {
            responseObject.addProperty("message", "Product not found or not authorized");
        }

        s.close();
        responseObject.addProperty("status", true);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));

    }

}
