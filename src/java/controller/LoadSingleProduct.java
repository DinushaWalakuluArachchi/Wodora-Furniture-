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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author dinus
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responJsonObject = new JsonObject();
        responJsonObject.addProperty("status", false);

        String productId = request.getParameter("id");
        System.out.println(productId);

        if (Util.isInteger(productId)) {

            SessionFactory sf = HibernateUtile.getSessionFactory();
            Session s = sf.openSession();
            try {
                SellersProduct sellerproduct = (SellersProduct) s.get(SellersProduct.class, Integer.valueOf(productId));
                if (sellerproduct.getStatus().getValue().equals("Active")) {

                    sellerproduct.getUser().setEmail(null);
                    sellerproduct.getUser().setPassword(null);
                    sellerproduct.getUser().setvCode(null);
                    sellerproduct.getUser().setId(-1);
                    sellerproduct.getUser().setCreated_At(null);

                    responJsonObject.add("sellerproduct", gson.toJsonTree(sellerproduct));
                    responJsonObject.addProperty("status", true);
                    //System.out.println(product);

                } else {
                    System.out.println("product not found!");
                }

            } catch (Exception e) {
                responJsonObject.addProperty("message", "Product Not Found!");
            }

            response.setContentType("application/json");
            String toJson = gson.toJson(responJsonObject);
            response.getWriter().write(toJson);

        }

    }

}
