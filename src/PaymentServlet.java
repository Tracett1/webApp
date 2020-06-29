import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
        String expirationMMDD = request.getParameter("cc-mmdd-expiration");
        String month = expirationMMDD.substring(0,2);
        String day = expirationMMDD.substring(2,4);
        int expirationYear = Integer.parseInt((request.getParameter("cc-year-expiration")));
        String lastName = request.getParameter("cc-last-name");
        String firstName = request.getParameter("cc-first-name");
        String creditCardNo = request.getParameter("cc-number");
        LocalDate exp = LocalDate.of(expirationYear, Integer.parseInt(month), Integer.parseInt(day));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatted_expiration = exp.format(formatter);
        LocalDate object = LocalDate.now();
        String today = object.format(formatter);


        try{
            Connection dbcon = dataSource.getConnection();
            String query = "SELECT cc.*, cust.id as customerId from creditcards as cc, customers as cust where cc.id = cust.ccId and cc.firstName like ? and cc.lastName like ?";
            PreparedStatement statement = dbcon.prepareStatement(query);

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            ResultSet rs = statement.executeQuery();
            JsonObject jsonObj= new JsonObject();
            if (rs.next()){
                String ccDraft = rs.getString("id");
                String cc = ccDraft.replaceAll(" ","");
                String expir = rs.getString("expiration");
                String customer_id = rs.getString("customerId");
                if (cc.equals(creditCardNo)){
                    if (expir.equals(formatted_expiration)){
                        jsonObj.addProperty("status", "success");
                        jsonObj.addProperty("message", "correct payment");
                        jsonObj.addProperty("saleDate", today);
                        jsonObj.addProperty("customer_id", customer_id);
                        HttpSession session = request.getSession();
                        session.setAttribute("saleDate", today);
                        session.setAttribute("customer_id", customer_id);
                    }
                    else{
                        jsonObj.addProperty("status", "fail");
                        jsonObj.addProperty("message", "ERROR: Incorrect expiration info");

                    }
                }
                else {
                    jsonObj.addProperty("status", "fail");
                    jsonObj.addProperty("message", "ERROR: Incorrect credit card no.");
                }
            }
            else {
                jsonObj.addProperty("status", "fail");
                jsonObj.addProperty("message", "ERROR: Name does not match credit card holder");

            }
            // write JSON string to output
            out.write(jsonObj.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
            rs.close();
            statement.close();
            dbcon.close();

        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();

    }

}