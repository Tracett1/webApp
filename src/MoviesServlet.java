import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "MoviesServlet", urlPatterns = "/api/stars")
public class MoviesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //SAVING SESSION URL
        HttpSession session = request.getSession();
        String Uri = "?" + request.getQueryString();
        System.out.println("this is uri");
        System.out.println(Uri);
        session.setAttribute("savedMoviePage", Uri);



        response.setContentType("application/json"); // Response mime type
        //Check all parameters and filter appropriately
        String add_to_query = "";
        String queryLimit = "";
        String queryOffset = "0";
        String browse = request.getParameter("browse");
        String genrename = request.getParameter("genreId");
        String alphanum = request.getParameter("alphanum");
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        String numRecords = request.getParameter("numRecords");
        String pageNum = request.getParameter("pageNum");
        if (numRecords != null && !numRecords.equals("")){
            queryLimit = numRecords;
        }
        else{
            queryLimit = "10";
            numRecords = "10";
        }
        queryOffset = String.valueOf( (Integer.parseInt(pageNum) * Integer.parseInt(numRecords)) - Integer.parseInt(numRecords));

        String adding_opposite_sort;
        if (sortBy.equals("rating")){ //If sorting by rating, title is descending
            sortBy = "ORDER BY rating";
            adding_opposite_sort = ", g.tite DESC";
        }
        else{ //If sorting by title, rating is descending
            String keep = sortBy;
            sortBy = "ORDER BY g." + keep;
            adding_opposite_sort = ", rating DESC";
        }

        boolean searchForStars = false;
        if ("YES".equals(browse)) {
            if (genrename.equals("")) {
                if (alphanum.equals("*")){
                    add_to_query += "WHERE g.tite NOT REGEXP '^[a-zA-z0-9]'";
                }
                else {
                    add_to_query += "WHERE g.tite LIKE" + "'" + alphanum + "%'";
                }
            }
            else{
                add_to_query = "HAVING FIND_IN_SET('" + genrename + "',genrename)";
            }
        }
        else{ // this means that browse is not a parameter, we are SEARCHING
            //add_to_query = "WHERE g.year=" + year;
            int count = 0;
            add_to_query = "WHERE ";
            if (!title.equals("")){
                count = 1;
                add_to_query += "g.tite LIKE '%" + title + "%'"; // OR g.tite LIKE " +"'%" + title + "%'"
            }

            if (!year.equals("")){ //first one in and its not empty
                if(count == 0) {
                    add_to_query += "g.year=" + year + " ";
                    count = 1;
                }
                else{
                    add_to_query += "AND g.year=" + year + " ";
                }
            }

            if (!director.equals("")){
                if (count ==0){
                    add_to_query += "g.director LIKE '%" + director + "%'";
                    count = 1;

                }
                else{
                    add_to_query += "AND g.director LIKE '%" + director + "%'";
                }
            }

            if (!stars.equals("")){
                searchForStars = true;
            }

        }

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
                    "JOIN ratings ON ratings.movieId = g.id " + add_to_query + sortBy + " " + order +
                    adding_opposite_sort +
                    " LIMIT " + queryLimit + " OFFSET " + queryOffset;
            System.out.println("SQL QUERY BEING SENT: " + query);


            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs

            while (rs.next()) {
                //in case stars is a search
                String[] movie_stars = rs.getString("starsname").split(",");
                String[] movie_starsid = rs.getString("starsid").split(",");
                List<starsObj> starsObjList = new ArrayList<>();

                if (searchForStars){
                    int found = 0;
                    for (int j = 0 ; j < movie_stars.length; j++){
                        if (movie_stars[j].toUpperCase().contains(stars.toUpperCase())){
                            starsObjList.add(new starsObj(movie_stars[j], movie_starsid[j]));
                            found = j;
                            break;
                        }
                    }
                    if (found != 0){ //found a match
                        for (int l = 0 ; l < movie_stars.length ; l++){
                            if (l != found){
                                starsObjList.add(new starsObj(movie_stars[l], movie_starsid[l]));
                            }
                            if (starsObjList.size() == 3){
                                break;
                            }
                        }
                    }
                    else{
                        continue;
                    }
                }
                // IF STARS ISN'T SEARCHED FOR
                else{
                    for (int i = 0; i < 3 ; i++){
                        starsObjList.add(new starsObj(movie_stars[i], movie_starsid[i]));
                    }
                }

                String movie_id = rs.getString("id");
                String movie_title = rs.getString("tite");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String movie_rating = rs.getString("rating");
                String movie_genre = rs.getString("genrename");
                //String[] movie_stars = rs.getString("starsname").split(",");
//                String[] movie_starsid = rs.getString("starsid").split(",");
//
//                List<starsObj> starsObjList = new ArrayList<>();
//                for (int i = 0; i < 3 ; i++) {
//                    starsObjList.add(new starsObj(movie_stars[i], movie_starsid[i]));
//                }

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
			jsonObject.addProperty("message", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
        out.close();

    }





}
