package controller;

import Hibernate.Category;
import Hibernate.Color;
import Hibernate.HibernateUtile;
import Hibernate.Material;
import Hibernate.SellersProduct;
import Hibernate.Status;
import Hibernate.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.crypto.Data;
import model.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author dinus
 */
@MultipartConfig
@WebServlet(name = "SaveProduct", urlPatterns = {"/SaveProduct"})
public class SaveProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String categoryId = request.getParameter("categoryId");
        String materialId = request.getParameter("materialId");
        String color = request.getParameter("color");
        String productQty = request.getParameter("productQty");
        String productPrice = request.getParameter("productPrice");

        //image uploading
        Part part1 = request.getPart("img1");
        Part part2 = request.getPart("img2");
        Part part3 = request.getPart("img3");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtile.getSessionFactory();
        Session s = sf.openSession();

        if (request.getSession().getAttribute("user") == null) {
            responseObject.addProperty("message", "please Sign in!");
        } else if (title.isEmpty()) {
            responseObject.addProperty("message", "Please Enterd Title!");
        } else if (description.isEmpty()) {
            responseObject.addProperty("message", "please Enter the Description");
        } else if (!Util.isInteger(materialId)) {
            responseObject.addProperty("message", "Invalid Material");
        } else if (Integer.parseInt(materialId) == 0) {
            responseObject.addProperty("message", "Please select meterial!");
        } else if (!Util.isInteger(color)) {
            responseObject.addProperty("message", "Invalid Color");
        } else if (Integer.parseInt(color) == 0) {
            responseObject.addProperty("message", "Please select Color");
        } else if (!Util.isInteger(productQty)) {
            responseObject.addProperty("message", "Invalid quantity");
        } else if (Integer.parseInt(productQty) <= 0) {
            responseObject.addProperty("message", "Quantity must be greater than 0");
        } else if (!Util.isDouble(productPrice)) {
            responseObject.addProperty("message", "Invalid price");
        } else if (Double.parseDouble(productPrice) <= 0) {
            responseObject.addProperty("message", "Price must be greater than 0");
        } else if (part1.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Product image one is required");
        } else if (part2.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Product image two is required");
        } else if (part3.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Product image three is required");
        } else {

            Category category1 = (Category) s.load(Category.class, Integer.parseInt(categoryId));
            if (category1 == null) {
                responseObject.addProperty("message", "Please select a valid Category Name!");
            } else {
                Material material1 = (Material) s.load(Material.class, Integer.parseInt(materialId));
                if (material1 == null) {
                    responseObject.addProperty("message", "Please select a valid Material Name!");
                } else {
                    Color color1 = (Color) s.load(Color.class, Integer.parseInt(color));
                    if (color1 == null) {
                        responseObject.addProperty("message", "Please select a valid Color Name!");
                    } else {

                        Status status = (Status) s.load(Status.class, 1); // Pending
                        User user = (User) request.getSession().getAttribute("user");
                        SellersProduct p = new SellersProduct();
                        p.setTitle(title);
                        p.setDescription(description);
                        p.setColor(color1);
                        p.setCategory(category1);
                        p.setMaterial(material1);
                        p.setQty(Integer.parseInt(productQty));
                        p.setPrice(Double.parseDouble(productPrice));
                        p.setStatus(status);
                        p.setUser(user);
                        p.setCreatedAt(new Date());

                        int id = (int) s.save(p);
                        s.beginTransaction().commit();
                        s.close();

                        String appPath = getServletContext().getRealPath("");// full path of the web pages folder

                        String newPath = appPath.replace("build\\web", "web\\product-images");

                        File productFolder = new File(newPath, String.valueOf(id));
                        productFolder.mkdir();

                        File file1 = new File(productFolder, "image1.png");
                        Files.copy(part1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        File file2 = new File(productFolder, "image2.png");
                        Files.copy(part2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        File file3 = new File(productFolder, "image3.png");
                        Files.copy(part3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        responseObject.addProperty("status", true);

                    }
                }
            }

        }

        Gson gson = new Gson();
        response.setContentType("application/Json");
        response.getWriter().write(gson.toJson(responseObject));

    }

}
