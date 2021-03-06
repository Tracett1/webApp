// INDEX.JS --> MOVIESERVLET
// Index.js
let limit = $("#limit_per_page");
function updateURLParameter(url, param, paramVal){
    var newAdditionalURL = "";
    var tempArray = url.split("?");
    var baseURL = tempArray[0];
    var additionalURL = tempArray[1];
    var temp = "";
    if (additionalURL) {
        tempArray = additionalURL.split("&");
        for (var i=0; i<tempArray.length; i++){
            if(tempArray[i].split('=')[0] != param){
                newAdditionalURL += temp + tempArray[i];
                temp = "&";
            }
        }
    }

    var rows_txt = temp + "" + param + "=" + paramVal;
    return baseURL + "?" + newAdditionalURL + rows_txt;
}



function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return '';
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
// FUNCTION FOR DETERMINING PAGE # FOR PAGINATIONG
// TAKES IN PAGE # and WHETHER "PREV" OR "NEXT" CHOSEN, RETURNS NEXT PAGE IN STRING FORM
function getPrevNextIndex(pageParam, value){
    var p = parseInt(pageParam);
    if (value == "next"){
        p = p+1;
        return (String(p));
    }
    if (value == "prev"){
        p = p-1;
        return  String(p);
    }
}

//handles the buttons
function handleAddCart(value){
    if (sessionStorage.getItem("cart") == null){
        sessionStorage.setItem("cart", "{}");
        sessionStorage.setItem("movieids", "{}");
    }
    var val = value.substring(9);
    var newval = val.split(",,,").join(" ");
    var movieId = value.substring(0, 9);

    var updateCart = JSON.parse(sessionStorage.getItem("cart"));
    var updateMovieID = JSON.parse(sessionStorage.getItem("movieids"));
    if (updateCart.hasOwnProperty(newval)){
        updateCart[newval] +=1;
        updateMovieID[newval] = movieId;
    }
    else{
        updateCart[newval] = 1;
        updateMovieID[newval] = movieId;
    }
    var cartSize = Object.keys(updateCart).length;
    sessionStorage.setItem("cart", JSON.stringify(updateCart));
    sessionStorage.setItem("movieids", JSON.stringify(updateMovieID));
    alert("'"+ newval + "'" + " added to cart!. You have " + cartSize.toString() + " movies in your cart.");


}
function handleCheckout(){
    window.location.replace("shopping-cart.html");
}
function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");
    // Populates the movie list!
    document.getElementById("limit_per_page").value = numRecords;
    // Displays current page #
    document.getElementById("currPage").innerHTML = ("Current page #: " + pageNum);
    let starTableBodyElement = jQuery("#star_table_body");
    for (let i = 0; i < resultData.length; i++) {
        var resultStar = JSON.parse(resultData[i]["movie_stars"]);
        var resultGenre = resultData[i]["movie_genre"].split(",");
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +
            '</a>' +
            "</th>";
        rowHTML +="<td>" + resultData[i]["movie_year"] + "</td>";
        rowHTML +="<td>" + resultData[i]["movie_director"] + "</td>";
        rowHTML +="<td>" + resultData[i]["movie_rating"] + "</td>";
        rowHTML+= "<td>"
        for (let j =0; j < resultGenre.length; j++){
            rowHTML+= '<a href="index.html?sortBy=tite&order=ASC&browse=YES&alphanum=&genreId=' + resultGenre[j] + '">' +
                resultGenre[j] + '</a>' + ", ";
        }
        rowHTML+="</td>";

        rowHTML +=
            "<th>" +

            '<a href="single-star.html?id=' + resultStar[0]['starid'] + '">'
            + resultStar[0]["starname"] +
            '</a>' + ", " + '<a href="single-star.html?id=' + resultStar[1]['starid'] + '">'
            + resultStar[1]["starname"] +
            '</a>' + ", " + '<a href="single-star.html?id=' + resultStar[2]['starid'] + '">'
            + resultStar[2]["starname"] +
            "</th>";
        rowHTML+= "<td>";
        rowHTML+= "<button type='button' onclick='handleAddCart(this.value)' value=" + resultData[i]["movie_id"] + resultData[i]["movie_title"].split(" ").join(",,,")  +">" + "Add to Cart </button>";
        rowHTML+="</td>";
        rowHTML += "</tr>";

        starTableBodyElement.append(rowHTML);
    }


    //POPULATING HYPERLINKS FOR SORTING
    console.log("Populating hyper-links for sorting by title or rating");
    let sortingElement = jQuery("#sorting_nav_body");
    let listHTML = "";
    let url = window.location.href;
    var newURL = updateURLParameter(url, "sortBy","tite");
    newURL = updateURLParameter(newURL, "order", "ASC");
    listHTML+= "<li class='nav-item' style='text-decoration: underline;'>";
    listHTML+= '<a class="nav-link" href=' + newURL +">" + "Title Increasing" + "</a>";
    listHTML+= "</li>";
    newURL = updateURLParameter(newURL, "order", "DESC");
    listHTML+= "<li class='nav-item' style='text-decoration: underline;'>";
    listHTML+= '<a class="nav-link" href=' + newURL +'>' + "Title Dec" + "</a>";
    listHTML+= "</li>";

    newURL = updateURLParameter(url, "sortBy", "rating");
    newURL = updateURLParameter(newURL, "order", "ASC");
    listHTML+= "<li class='nav-item' style='text-decoration: underline;'>";
    listHTML+= '<a class="nav-link" href=' + newURL +'>' + "Rating Inc" + "</a>";
    listHTML+= "</li>";
    newURL = updateURLParameter(newURL, "order","DESC");
    listHTML+= "<li class='nav-item' style='text-decoration: underline;'>";
    listHTML+= '<a class="nav-link" href=' + newURL +'>' + "Rating Dec" + "</a>";
    listHTML+= "</li>";
    sortingElement.append(listHTML);

    // FILLING NEXT/PREV LINKS
    var ul = document.getElementById("page_control");
    var li = document.createElement("li");
    var a = document.createElement("a");
    var newPrev = getPrevNextIndex(pageNum,"prev");
    var changeURL = updateURLParameter(window.location.href, "pageNum", newPrev);
    a.setAttribute("href", changeURL);
    a.setAttribute('class', 'page-link');
    a.innerHTML = " << prev ";
    if (pageNum == "1"){
        li.setAttribute('class', 'page-item disabled');
    }
    else {
        li.setAttribute('class', 'page-item');
    }
    li.appendChild(a);
    ul.appendChild(li);

    var li2 = document.createElement("li");
    var a2 = document.createElement("a");
    var newNext = getPrevNextIndex(pageNum, "next");

    changeURL = updateURLParameter(window.location.href, "pageNum", newNext);

    a2.setAttribute('href', changeURL);
    a2.setAttribute('class', 'page-link');
    a2.innerHTML = " next >> ";
    if (numRecords < resultData.length){
        li2.setAttribute('class', 'page-item disabled');
    }
    else{
        li2.setAttribute('class', 'page-item');
    }
    li2.appendChild(a2);
    ul.appendChild(li2);





}

function handleOnChange(formChangeEvent){
    formChangeEvent.preventDefault();
    console.log(document.getElementById("limit_per_page").value);
    newerURL = updateURLParameter(window.location.href, "numRecords", limit.val());
    window.location.replace(newerURL);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
let browse_check = getParameterByName('browse'); //may be YES or NO
let browse_genre = getParameterByName('genreId'); //may be not a parameter (empty)
let browse_alphanum = getParameterByName('alphanum'); //may be not a parameter (empty)
let sortBy = getParameterByName('sortBy');
let order = getParameterByName('order');
let numRecords = getParameterByName('numRecords');
let pageNum = getParameterByName('pageNum');
if (numRecords == ""){
    numRecords = '10';
}
if (numRecords == null){
    numRecords = '10';
}
let url_to_pass = "";
if (browse_check == "YES"){
    url_to_pass = "?sortBy=" + sortBy + "&order="+ order + "&browse=YES"+ "&genreId=" + browse_genre + "&alphanum="
        + browse_alphanum + "&numRecords=" + numRecords + "&pageNum=" + pageNum;
}

else{
    let title = getParameterByName('title');
    let year = getParameterByName('year');
    let director = getParameterByName('director');
    let stars = getParameterByName('stars');
    let sortBy = getParameterByName('sortBy');
    let order = getParameterByName('order');
    url_to_pass = "?sortBy=" + sortBy + "&order="+ order +"&browse=NO" +"&title=" + title
        + "&year=" + year + "&director=" + director + "&stars=" + stars + "&numRecords=" + numRecords +
        "&pageNum=" + pageNum;

}

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/stars" + url_to_pass,
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

limit.change(handleOnChange);