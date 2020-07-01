import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "DashboardServlet", urlPatterns = "/api/dashboard")
public class DashboardServlet extends HttpServlet {

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        JsonArray responseJsonArr = new JsonArray();

        try {
            Connection dbcon = dataSource.getConnection();
            DatabaseMetaData meta = dbcon.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rsTables = meta.getTables(null, null, null, types);

            while (rsTables.next()){
                JsonObject table_data = new JsonObject();
                String tableName = rsTables.getString(3);
                table_data.addProperty("table_name", tableName);
                JsonArray column_vars = new JsonArray();

                ResultSet rsColumns = meta.getColumns(null, null, tableName, null);
                while(rsColumns.next()){
                    JsonObject columns_data = new JsonObject();
                    String columnName = rsColumns.getString("COLUMN_NAME");
                    int dataType = Integer.parseInt(rsColumns.getString("DATA_TYPE"));
                    String stringType = "";
                    switch(dataType){
                        case 91:
                            stringType = "Date";
                            break;
                        case 4:
                            stringType = "Integer";
                            break;
                        case 12:
                            stringType = "Varchar";
                            break;
                    }
                    columns_data.addProperty("col_name", columnName);
                    columns_data.addProperty("col_type", stringType);
                    column_vars.add(columns_data);
                }
                table_data.add("column_vars", column_vars);
                responseJsonArr.add(table_data);

            }
            System.out.println(responseJsonArr.toString());
            out.write(responseJsonArr.toString());

            dbcon.close();

        }
        catch (Exception e) {
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", e.getMessage());
            out.write(responseJsonObject.toString());
            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        String starName = request.getParameter("starname");
        String birthYear = request.getParameter("year");
        JsonObject Jsonresponse = new JsonObject();
        try {
            Connection dbcon = dataSource.getConnection();

            String query = "CALL add_star(?,?)";
            PreparedStatement statement = dbcon.prepareStatement(query);

            statement.setString(1, starName);
            statement.setString(2,birthYear);
            int rs = statement.executeUpdate();
            if (rs != 0){
                Jsonresponse.addProperty("status","success");
            }
            out.write(Jsonresponse.toString());



            dbcon.close();
            response.setStatus(200);


        } catch (Exception e) {
            Jsonresponse.addProperty("status","fail");
            Jsonresponse.addProperty("message", e.getMessage());
            response.setStatus(500);

        }

        out.close();


    }
}

