import javax.annotation.Resource;
import javax.servlet.ServletException;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String expirationMMDD = request.getParameter("cc-mmdd-expiration");
        String month = expirationMMDD.substring(0,2);
        String day = expirationMMDD.substring(2,4);

        int expirationYear = Integer.parseInt((request.getParameter("cc-year-expiration")));
        String lastName = request.getParameter("cc-last-name");
        String firstName = request.getParameter("cc-first-name");
        String creditCardNo = request.getParameter("cc-number");
        LocalDate exp = LocalDate.of(expirationYear, Integer.parseInt(month), Integer.parseInt(day));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formated_expiration = exp.format(formatter);

        System.out.println(formated_expiration);


//
//        PrintWriter out = response.getWriter();

//        try{
//
//
//
//
//
//        }





    }

}
