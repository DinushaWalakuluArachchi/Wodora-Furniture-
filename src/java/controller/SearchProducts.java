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
@WebServlet(name = "SearchProducts", urlPatterns = {"/SearchProducts"})
public class SearchProducts extends HttpServlet {

    private static final int MAX_RESULT = 6;
    private static final int ACTIVE_ID = 1;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject resposeObject = new JsonObject();
        resposeObject.addProperty("status", false);

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        SessionFactory sf = HibernateUtile.getSessionFactory();
        Session s = sf.openSession();

        Criteria c1 = s.createCriteria(SellersProduct.class);

        if (requestJsonObject.has("categoryName")) {
            String categoryName = requestJsonObject.get("categoryName").getAsString();

            // get category details 
            Criteria c2 = s.createCriteria(Category.class);
            c2.add(Restrictions.eq("type", categoryName));
            Category category = (Category) c2.uniqueResult();

            c1.add(Restrictions.eq("category", category));


        }

        if (requestJsonObject.has("materialName")) {
            String materialName = requestJsonObject.get("materialName").getAsString();

            // get category details 
            Criteria c3 = s.createCriteria(Material.class);
            c3.add(Restrictions.eq("type", materialName));
            Material material = (Material) c3.uniqueResult();

            c1.add(Restrictions.eq("material", material));

        }

        if (requestJsonObject.has("colorName")) {
            String colorName = requestJsonObject.get("colorName").getAsString();
            // get color details
            Criteria c4 = s.createCriteria(Color.class);
            c4.add(Restrictions.eq("name", colorName));
            Color color = (Color) c4.uniqueResult();

            // filter product by using color
            c1.add(Restrictions.eq("color", color));

        }

        if (requestJsonObject.has("priceStart") && requestJsonObject.has("priceEnd")) {
            double priceStart = requestJsonObject.get("priceStart").getAsDouble();
            double priceEnd = requestJsonObject.get("priceEnd").getAsDouble();

            c1.add(Restrictions.ge("price", priceStart));
            c1.add(Restrictions.le("price", priceEnd));
                System.out.println(priceStart); 
                System.out.println(priceEnd);
           


        }

        if (requestJsonObject.has("sortValue")) {
            String sortValue = requestJsonObject.get("sortValue").getAsString();
            if (sortValue.equals("Most Popular")) {
                c1.addOrder(Order.desc("id"));
            } else if (sortValue.equals("Price: Low to High")) {
                c1.addOrder(Order.asc("price"));
            } else if (sortValue.equals("Price: High to Low")) {
                c1.addOrder(Order.desc("price"));
            } 
        }

       

        resposeObject.addProperty("allProductCount", c1.list().size());

        if (requestJsonObject.has("firstResult")) {
            int firstResult = requestJsonObject.get("firstResult").getAsInt();
            c1.setFirstResult(firstResult);
            c1.setMaxResults(SearchProducts.MAX_RESULT);
        }

         Status status = (Status) s.get(Status.class, SearchProducts.ACTIVE_ID); // get Active product [2 = Active]
        c1.add(Restrictions.eq("status", status));
        
        List<SellersProduct> sellerproductList = c1.list();
        for (SellersProduct sellersProduct : sellerproductList) {
            sellersProduct.setUser(null);
        }
        s.close();

        resposeObject.add("sellerproductList", gson.toJsonTree(sellerproductList));
        resposeObject.addProperty("status", true);
        response.setContentType("application/json");
        String toJson = gson.toJson(resposeObject);
        response.getWriter().write(toJson);

    }

}
