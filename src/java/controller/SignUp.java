/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Hibernate.HibernateUtile;
import Hibernate.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dinus
 */
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        String fname = user.get("fristName").getAsString();
        String lname = user.get("lastName").getAsString();
        final String email = user.get("email").getAsString();
        String password = user.get("password").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (fname.isEmpty()) {
            responseObject.addProperty("message", "Frist Name Can Not Be Empty");
        } else if (lname.isEmpty()) {
            responseObject.addProperty("message", "Last Name Can Not Be Empty");
        } else if (email.isEmpty()) {
            responseObject.addProperty("message", "Email Can Not Be Empty");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Please Enter Valid Email");
        } else if (password.isEmpty()) {
            responseObject.addProperty("message", "Password Can Not Be Empty");
        } else if (!Util.isPasswordValid(password)) {
            responseObject.addProperty("message", "The password must contains at least Uppercase,Lowecase Number,"
                    + "Special Character and to be minimum 8 Characters Long!");
        } else {
            //hibernate save
            SessionFactory sf = HibernateUtile.getSessionFactory();
            Session s = sf.openSession();

            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("email", email));

            if (!c.list().isEmpty()) {
                responseObject.addProperty("message", "User With the Email Alredy Exsists");
            } else {
                User u = new User();
                u.setFname(fname);
                u.setLname(lname);
                u.setEmail(email);
                u.setPassword(password);

                //verification code
                final String Verificationcode = Util.genarateCode();
                u.setvCode(Verificationcode);
                u.setCreated_At(new Date());

                s.save(u);
                s.beginTransaction().commit();
                //hibernate save

                //send email
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(email , "Woodora Furniture", "<html>\n"
                                + "  <body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;\">\n"
                                + "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n"
                                + "      <tr>\n"
                                + "        <td align=\"center\">\n"
                                + "          <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #ffffff; padding: 20px; border-radius: 8px; margin-top: 30px;\">\n"
                                + "            <tr>\n"
                                + "              <td align=\"center\">\n"
                                + "                <h2 style=\"color: #333333;\">Hello, <strong>"+fname+"</strong> </h2>\n"
                                + "                <p style=\"color: #666666;\">Thank you for signing up on <strong>Woodora Furniture</strong>!</p>\n"
                                + "                <p style=\"color: #666666;\">Please use the code below to verify your email address:</p>\n"
                                + "\n"
                                + "                <!-- ✅ Verification Code -->\n"
                                + "                <div style=\"font-size: 24px; font-weight: bold; color: #4CAF50; margin: 20px 0;\">\n"
                                + "                 "+Verificationcode+"\n"
                                + "                </div>\n"
                                + "\n"
                                            
                                + "\n"
                                + "                <p style=\"font-size: 14px; color: #999999;\">\n"
                                + "                  If you didn’t request this, you can ignore this email.\n"
                                + "                </p>\n"
                                + "\n"
                                + "                <hr style=\"margin-top: 30px; border: none; border-top: 1px solid #dddddd;\" />\n"
                                + "                <p style=\"font-size: 12px; color: #cccccc;\">\n"
                                + "                  &copy; 2025 [Woodorafurniture.com], All rights reserved.<br />\n"
                                + "                  123 Furniture St, Wood City, SL 10000\n"
                                + "                </p>\n"
                                + "              </td>\n"
                                + "            </tr>\n"
                                + "          </table>\n"
                                + "        </td>\n"
                                + "      </tr>\n"
                                + "    </table>\n"
                                + "  </body>\n"
                                + "</html>");
                    }
                }).start();
                
                HttpSession ses = request.getSession();
                ses.setAttribute("email", email);
                
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Registration Success.Please check youer Email for the Verification Code. ");
            }

            s.close();
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

//            
    }

  

}
