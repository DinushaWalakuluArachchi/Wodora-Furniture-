/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Hibernate.Category;
import Hibernate.Color;
import Hibernate.HibernateUtile;
import Hibernate.Material;
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

/**
 *
 * @author dinus
 */
@WebServlet(name = "LoadProductData", urlPatterns = {"/LoadProductData"})
public class LoadProductData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("nice");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtile.getSessionFactory();
        Session s = sf.openSession();

        Criteria c1 = s.createCriteria(Category.class);
        List<Category> categoryList = c1.list();

//        for (Category category : categoryList) {
//            System.out.println(category.getId());
//            System.out.println(category.getType());
//        }
        Criteria c2 = s.createCriteria(Material.class);
        List<Material> materialList = c2.list();

//        for (Material material : materialList) {
//            System.out.println(material.getId());
//            System.out.println(material.getType());
//            System.out.println(material.getCategory().getId());
//            System.out.println(material.getCategory().getType());
//            
//            
//        }
        Criteria c3 = s.createCriteria(Color.class);
        List<Color> colorList = c3.list();
        
//        for (Color color : colorList) {
//            System.out.println(color.getId());
//            System.out.println(color.getName());
//        }

        s.close();
        
        Gson gson = new Gson();
       responseObject.addProperty("status", true);
       responseObject.add("categoryList", gson.toJsonTree(categoryList));
       responseObject.add("materialList", gson.toJsonTree(materialList));
       responseObject.add("colorList", gson.toJsonTree(colorList));
       
         response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
       

    }

}
