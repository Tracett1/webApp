import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "StarsServlet", urlPatterns = "/api/stars")
public class StarsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();

            String query = "SELECT g.*, ratings.rating FROM" +
                    "( SELECT movies.*, group_concat(DISTINCT genres.name) as genrename, group_concat(DISTINCT stars.name) as starsname, group_concat(DISTINCT stars.id) as starsid FROM movies " +
                    "JOIN genres_in_movies ON genres_in_movies.movieId = movies.id " +
                    "JOIN genres ON genres_in_movies.genreId = genres.id " +
                    "JOIN stars_in_movies ON stars_in_movies.movieId = movies.id " +
                    "JOIN stars ON stars.id = stars_in_movies.starId\n" +
                    "GROUP BY(movies.id) ) AS g " +
                    "JOIN ratings ON ratings.movieId = g.id " +
                    "ORDER BY rating DESC " +
                    "LIMIT 20";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs

            while (rs.next()) {
                String movie_id = rs.getString("id");
                String movie_title = rs.getString("tite");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String movie_rating = rs.getString("rating");
                String movie_genre = rs.getString("genrename");
                String[] movie_stars = rs.getString("starsname").split(",");
                String[] movie_starsid = rs.getString("starsid").split(",");

                List<starsObj> starsObjList = new ArrayList<>();
                for (int i = 0; i < 3 ; i++) {
                    starsObjList.add(new starsObj(movie_stars[i], movie_starsid[i]));
                }

                Gson gson = new Gson();
                String star_json = gson.toJson(starsObjList);

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("movie_genre", movie_genre);
                jsonObject.addProperty("movie_stars", star_json);
                jsonArray.add(jsonObject);
            }
            
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
        	
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
        out.close();

    }
}
