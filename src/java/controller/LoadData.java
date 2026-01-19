package controller;

import Hibernate.Category;
import Hibernate.Color;
import Hibernate.HibernateUtile;
import Hibernate.Material;
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
@WebServlet(name = "LoadData", urlPatterns = {"/LoadData"})
public class LoadData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

        Criteria c3 = s.createCriteria(Color.class);
        List<Color> colorList = c3.list();
        
//        for (Color color : colorList) {
//            System.out.println(color.getName());
//        }

        Status status = (Status) s.get(Status.class, 1);
        Criteria c4 = s.createCriteria(SellersProduct.class);
        c4.addOrder(Order.desc("id"));
        
        
        
        c4.add(Restrictions.eq("status", status));
        responseObject.addProperty("allProductCount",c4.list().size());
        c4.setFirstResult(0);
        c4.setMaxResults(6);
        
         List<SellersProduct> sellerproductList = c4.list();
         
         for (SellersProduct sellersProduct : sellerproductList) {
            sellersProduct.setUser(null);
        }
         
         Gson gson = new Gson();
         
         responseObject.add("categoryList", gson.toJsonTree(categoryList));
         responseObject.add("materialList", gson.toJsonTree(materialList));
         responseObject.add("colorList", gson.toJsonTree(colorList));
         responseObject.add("sellerproductList", gson.toJsonTree(sellerproductList));
         
         responseObject.addProperty("status", true);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        s.close();
        
    }

}
