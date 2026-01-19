package controller;

import Hibernate.HibernateUtile;
import Hibernate.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Address;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dinus
 */
@WebServlet(name = "UserAccount", urlPatterns = {"/UserAccount"})
public class UserAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession ses = request.getSession(false);
        if (ses != null && ses.getAttribute("user") != null) {

            User u = (User) ses.getAttribute("user");
            JsonObject responseObject = new JsonObject();

            String createdAt = new SimpleDateFormat("MMM yyyy").format(u.getCreated_At());

            responseObject.addProperty("fristname", u.getFname());
            responseObject.addProperty("lastname", u.getLname());
            responseObject.addProperty("password", u.getPassword());
            responseObject.addProperty("createdAt", createdAt);

            SessionFactory sf = HibernateUtile.getSessionFactory();
            Session s = sf.openSession();
            
            Gson gson = new Gson();
            Criteria c = s.createCriteria(Address.class);
            c.add(Restrictions.eq("user", u));

            List<Address> addressList = c.list();
            responseObject.add("addressList", gson.toJsonTree(addressList));

            String toJson = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(toJson);
        } else {

        }

    }

}
