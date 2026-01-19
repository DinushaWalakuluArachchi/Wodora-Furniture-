/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Hibernate.HibernateUtile;
import Hibernate.SellersProduct;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dinus
 */
@WebServlet(name = "BasicSearch", urlPatterns = {"/BasicSearch"})
public class BasicSearch extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Gson gson = new Gson();
        JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);
        String searchText = requestObject.get("query").getAsString();

        JsonObject responseObject = new JsonObject();

        SessionFactory sf = HibernateUtile.getSessionFactory();
        Session session = sf.openSession();

        try {
            Criteria criteria = session.createCriteria(SellersProduct.class);
            criteria.add(Restrictions.ilike("title", searchText, MatchMode.ANYWHERE)); // case-insensitive LIKE

            List<SellersProduct> sellerproductList = criteria.list();

            responseObject.addProperty("status", true);
            responseObject.addProperty("allProductCount", criteria.list().size());
            responseObject.add("sellerproductList", gson.toJsonTree(sellerproductList));
            
        } catch (Exception e) {
            e.printStackTrace();
            responseObject.addProperty("status", false);
        } finally {
            session.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(responseObject.toString());
    }
}
