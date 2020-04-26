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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "MainServlet", urlPatterns = "/api/single-movie")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Construct a query with parameter represented by "?"
            String query = "SELECT g.*, ratings.rating FROM" +
                    "( SELECT movies.*, group_concat(DISTINCT genres.name) as genrename, group_concat(DISTINCT stars.name) as starsname, group_concat( DISTINCT stars.id) as starsid FROM movies " +
                    "JOIN genres_in_movies ON genres_in_movies.movieId = movies.id " +
                    "JOIN genres ON genres_in_movies.genreId = genres.id " +
                    "JOIN stars_in_movies ON stars_in_movies.movieId = movies.id " +
                    "JOIN stars ON stars.id = stars_in_movies.starId\n" +
                    "GROUP BY(movies.id) ) AS g " +
                    "JOIN ratings ON ratings.movieId = g.id " +
                    "WHERE g.id = ?";
            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {

                String movieId = rs.getString("id");
                String movieTitle = rs.getString("tite");
                String movieYear = rs.getString("year");
                String movieDirector = rs.getString("director");
                String movieRating = rs.getString("rating");
                String movieGenre = rs.getString("genrename");
                // Create a JsonObject based on the data we retrieve from rs
                String[] movie_stars = rs.getString("starsname").split(",");
                String[] movie_starsid = rs.getString("starsid").split(",");

                List<starsObj> starsObjList = new ArrayList<>();
                for (int i = 0; i < movie_stars.length ; i++) {
                    starsObjList.add(new starsObj(movie_stars[i], movie_starsid[i]));
                }

                Gson gson = new Gson();
                String star_json = gson.toJson(starsObjList);

                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("movie_id", movieId);
                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
                jsonObject.addProperty("movie_director", movieDirector);
                jsonObject.addProperty("movie_rating", movieRating);
                jsonObject.addProperty("movie_genres", movieGenre);
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