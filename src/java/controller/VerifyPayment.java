/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.PayHear;

/**
 *
 * @author dinus
 */
@WebServlet(name = "VerifyPayment", urlPatterns = {"/VerifyPayment"})
public class VerifyPayment extends HttpServlet {

    private static final int PAYHEAR_SUCCESS = 2;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String merchant_id = request.getParameter("merchant_id");
        String order_id = request.getParameter("order_id");
        String payhere_amount = request.getParameter("payhere_amount");
        String payhere_currency = request.getParameter("payhere_currency");
        String status_code = request.getParameter("status_code");
        String md5sig = request.getParameter("md5sig");

        String merchantSecret = "MzgwMDAyODAzNTIwMjc5NDg5MDQ0MTczMzY2Mjc2MTAyMDEwMzg1Nw==";
        String merchantSecretMD5 = PayHear.generateMD5(merchantSecret);
        String hash = PayHear.generateMD5(merchant_id + order_id + payhere_amount + payhere_currency + merchantSecretMD5);

        if (md5sig.equals(hash) && Integer.parseInt(status_code) == PAYHEAR_SUCCESS) {
            System.out.println("Payment completed. order_Id" + order_id);
            String orderId = order_id.substring(3);
            System.out.println(orderId);
        }

    }
    
    

}
