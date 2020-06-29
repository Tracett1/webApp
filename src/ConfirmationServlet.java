import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "ConfirmationServlet", urlPatterns = "/api/conf")
public class ConfirmationServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
        Map m = request.getParameterMap();
        Map<String,String> movie_map = new HashMap<String,String>();
        for ( Object key : m.keySet()){
            Gson gson = new Gson();
            String ok = key.toString();
            movie_map = gson.fromJson(ok, Map.class);

        }
//        for (String entry : movie_map.keySet()){
//            String value = movie_map.get(entry);
//            System.out.println(value);
//        }

        try{
            Connection dbcon = dataSource.getConnection();

            String query = "INSERT INTO sales VALUES('0',?,?,?,?)";
            PreparedStatement statement = dbcon.prepareStatement(query);
            HttpSession session = request.getSession();
            String saleDate = session.getAttribute("saleDate").toString();
            String customer_id = session.getAttribute("customer_id").toString();

            dbcon.setAutoCommit(false);

            statement.setString(1,customer_id);
            statement.setString(3, saleDate);
            for (String entry : movie_map.keySet()){
                statement.setString(2,entry);
                String check = movie_map.get(entry);
                statement.setString(4, check);
                statement.addBatch();
            }
            int [] count = statement.executeBatch();
            dbcon.commit();




            JsonObject json = new JsonObject();
            out.write(json.toString());
            // write JSON string to output
//            System.out.println(jsonObj.toString());
//            out.write(jsonObj.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
            //rs.close();
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