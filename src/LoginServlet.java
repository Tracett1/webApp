import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
        JsonObject responseJsonObject = new JsonObject();

        // Verify reCAPTCHA
        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
        } catch (Exception e) {
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", e.getMessage());
            out.write(responseJsonObject.toString());
            out.close();
            response.setStatus(500);
            return;
        }


        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            Connection dbcon = dataSource.getConnection();
            String query = "SELECT id, email, password from customers where email = ?";

            PreparedStatement statement = dbcon.prepareStatement(query);


            statement.setString(1,username);
//            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            int count = 0;
            boolean success = false;
            StrongPasswordEncryptor enc = new StrongPasswordEncryptor();
            while (rs.next()){
                count++;
                String encryptedPassword = rs.getString("password");
                success = enc.checkPassword(password,encryptedPassword);

            }
            if (count < 1 || success == false){
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Username or Password Incorrect");
            }
            else { //if login successful
                // Create a user for the session
                request.getSession().setAttribute("user", new User(username));
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");
            }

            out.write(responseJsonObject.toString());
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();

        }
        catch (Exception e) {
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", e.getMessage());
            out.write(responseJsonObject.toString());
            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();

    }
}
