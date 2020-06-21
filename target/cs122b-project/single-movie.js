let button = $("#back_to_movies");

function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    console.log("handleResult: populating star info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    let starInfoElement = jQuery("#movie_info");

    // append two html <p> created to the h3 body, which will refresh the page
    starInfoElement.append("<p> Movie Title: " + resultData[0]["movie_title"] + "</p>" +
        "<p>Release Year: " + resultData[0]["movie_year"] + "</p>");

    console.log("handleResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
   // for (let i = 0; i < Math.min(10, resultData.length); i++) {
    var resultStar = JSON.parse(resultData[0]["movie_stars"]);
    var resultGenre = resultData[0]["movie_genres"].split(",");
    let rowHTML = "";
    rowHTML += "<tr>";
    rowHTML +=
        "<th>" +
        // Add a link to single-movie.html with id passed with GET url parameter
        '<a href="single-movie.html?id=' + resultData[0]['movie_id'] + '">'
        + resultData[0]["movie_title"] +
        '</a>' +
        "</th>";
    rowHTML += "<th>" + resultData[0]["movie_year"] + "</th>";
    rowHTML += "<th>" + resultData[0]["movie_director"] + "</th>";
    //rowHTML += "<th>" + resultData[0]["movie_genres"] + "</th>";
    rowHTML+= "<th>"
    for (let j =0; j < resultGenre.length; j++){
        rowHTML+= '<a href="index.html?sortBy=tite&order=ASC&browse=YES&alphanum=&genreId=' + resultGenre[j] + '">' +
            resultGenre[j] + '</a>' + ", ";
    }
    rowHTML+="</th>";
    rowHTML += "<th>"
    for (let i = 0; i < Math.min(10, resultStar.length); i++){
        rowHTML +=
            // Add a link to single-movie.html with id passed with GET url parameter
            '<a href="single-star.html?id=' + resultStar[i]['starid'] + '">'
            + resultStar[i]["starname"] + '</a>' + ", ";
    }
    rowHTML += "</th>";
    rowHTML += "<th>" + resultData[0]["movie_rating"] + "</th>";
    rowHTML += "</tr>";

    // Append the row created to the table body, which will refresh the page
    movieTableBodyElement.append(rowHTML);
    //}
}

function handleRedirect(resultData){
    window.location.replace(resultData);
}


function handleButtonClick(){
    console.log("hitting handle button");
    $.ajax("api/single-movie", {
        method: "POST",
        success: handleRedirect
    });

}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let starId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-movie?id=" + starId, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
