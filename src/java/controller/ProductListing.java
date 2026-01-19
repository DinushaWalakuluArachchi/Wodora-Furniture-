/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Hibernate.HibernateUtile;
import Hibernate.SellersProduct;
import Hibernate.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dinus
 */
@WebServlet(name = "ProductListing", urlPatterns = {"/ProductListing"})
public class ProductListing extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtile.getSessionFactory();
        Session s = sf.openSession();

         User user = (User) request.getSession().getAttribute("user");
        
        Criteria c1 = s.createCriteria(SellersProduct.class);
        c1.add(Restrictions.eq("user", user));
        List<SellersProduct> sellerProductList = c1.list();
        

        s.close();

        Gson gson = new Gson();
        responseObject.addProperty("status", true);
        responseObject.add("sellerProductList", gson.toJsonTree(sellerProductList));
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));

    }

}
