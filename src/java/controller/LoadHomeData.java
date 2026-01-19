package controller;

import Hibernate.Category;
import Hibernate.HibernateUtile;
import Hibernate.SellersProduct;
import Hibernate.Status;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dinus
 */
@WebServlet(name = "LoadHomeData", urlPatterns = {"/LoadHomeData"})
public class LoadHomeData extends HttpServlet {

    private static final int ACTIVE_STATUS_ID = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", true);

        SessionFactory sf = HibernateUtile.getSessionFactory();
        Session s = sf.openSession();

        Criteria c1 = s.createCriteria(Category.class);;
        List<Category> categoryList = c1.list();
        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        
        Criteria c2 = s.createCriteria(SellersProduct.class);
        c2.addOrder(Order.desc("id"));
        
        Status status =  (Status) s.get(Status.class, LoadHomeData.ACTIVE_STATUS_ID);
        c2.add(Restrictions.eq("status", status));
        
        c2.setFirstResult(0);
        c2.setMaxResults(8);
        
        List<SellersProduct> sellerProductList =c2.list();
        for (SellersProduct sellersProduct : sellerProductList) {
            sellersProduct.setUser(null);
        }
         responseObject.add("sellerProductList", gson.toJsonTree(sellerProductList));
        
        
        
        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

}
