function handleMainResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    let genreList = jQuery("#genre_list_body");
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toUpperCase().split("");
    for (let i = 0; i < Math.min(30, resultData.length); i++){
        let rowHTML = "";
         rowHTML += "<li>" +
             '<a href="index.html?browse=YES&alphanum=&genreId=' + resultData[i]["genre_name"] + '">' +
             resultData[i]["genre_name"] + '</a>' + '</li>';
/*        var tem = document.createElement('li');
        item.appendChild(document.createTextNode(resultData[i]["genre_name"]));
        list.appendChild(item);*/
        genreList.append(rowHTML);
    }

    let alpha = jQuery("#alphanum_body");
    for(let i =0; i < chars.length ; i++){
        let rowHTML = "";
        rowHTML += "<li>" +
            '<a href="index.html?browse=YES&genreId=&alphanum=' + chars[i] + '">' +
            chars[i]+ '</a>' + '</li>';
        /*        var tem = document.createElement('li');
                item.appendChild(document.createTextNode(resultData[i]["genre_name"]));
                list.appendChild(item);*/
        alpha.append(rowHTML);
    }

    //document.getElementById("genre_list_body").appendChild(list);

}






jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/main",
    success: (resultData) => handleMainResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});