/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Hibernate.HibernateUtile;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.City;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author dinus
 */
@WebServlet(name = "CityData", urlPatterns = {"/CityData"})
public class CityData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionFactory sf = HibernateUtile.getSessionFactory();
        Session s = sf.openSession();
        
        Criteria c = s.createCriteria(City.class);
        List<City> cityList = c.list();
        
//        for (City city : cityList) {
//            System.out.println(city.getId());
//            System.out.println(city.getName());
//        }
        
        Gson gson = new Gson();
        String toJson = gson.toJson(cityList);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        s.close();
    }

   

}
