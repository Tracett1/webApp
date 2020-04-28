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

function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");
    console.log(url_to_pass);
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#star_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(20, resultData.length); i++) {
        var resultStar = JSON.parse(resultData[i]["movie_stars"]);
        var resultGenre = resultData[i]["movie_genre"].split(",");
        console.log(resultGenre);
        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        // rowHTML +="<th>" + resultData[i]["movie_title"] + "</th>";
        rowHTML +=
            "<th>" +
            // Add a link to single-movie.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +
            '</a>' +
            "</th>";
        rowHTML +="<td>" + resultData[i]["movie_year"] + "</td>";
        rowHTML +="<td>" + resultData[i]["movie_director"] + "</td>";
        rowHTML +="<td>" + resultData[i]["movie_rating"] + "</td>";
        // rowHTML +="<td>" + resultData[i]["movie_genre"] + "</td>";
        rowHTML+= "<td>"
        for (let j =0; j < resultGenre.length; j++){
            rowHTML+= '<a href="index.html?browse=YES&alphanum=&genreId=' + resultGenre[j] + '">' +
                resultGenre[j] + '</a>' + ", ";
        }
        rowHTML+="</td>";

        rowHTML +=
            "<th>" +
            // Add a link to single-movie.html with id passed with GET url parameter
            '<a href="single-star.html?id=' + resultStar[0]['starid'] + '">'
            + resultStar[0]["starname"] +
            '</a>' + ", " + '<a href="single-star.html?id=' + resultStar[1]['starid'] + '">'
            + resultStar[1]["starname"] +
            '</a>' + ", " + '<a href="single-star.html?id=' + resultStar[2]['starid'] + '">'
            + resultStar[2]["starname"] +
            "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
let browse_check = getParameterByName('browse'); //may be YES or NO
let browse_genre = getParameterByName('genreId'); //may be not a parameter (empty)
let browse_alphanum = getParameterByName('alphanum'); //may be not a parameter (empty)
let url_to_pass = "";
if (browse_check == "YES"){
    url_to_pass = "?browse=YES"+ "&genreId=" + browse_genre + "&alphanum=" + browse_alphanum;
}

else{
    let title = getParameterByName('title');
    let year = getParameterByName('year');
    let director = getParameterByName('director');
    let stars = getParameterByName('stars');
    url_to_pass = "?browse=NO" +"&title=" + title + "&year=" + year + "&director=" + director + "&stars=" + stars;

}

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/stars" + url_to_pass, // REMEMBER TO CHANGE BACK
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});