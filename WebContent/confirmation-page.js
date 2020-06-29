function createDataToSend(){
    var movieToQuantity = JSON.parse(sessionStorage.getItem("cart"));
    var movieToId = JSON.parse(sessionStorage.getItem("movieids"));
    var obj = '{}';
    var jsonObj = JSON.parse(obj);
    /// sending a dictionary of {movieId : quantity} over to server
    for (each in movieToQuantity){
        var movieId = movieToId[each].toString();
        var quantity = movieToQuantity[each].toString();
        jsonObj[movieId] = quantity;
    }
    return jsonObj
}

function handleSaleSub(){
    sessionStorage.clear();
    console.log("TRANSACTION PROCESSED");
}
var obj = createDataToSend();

$.ajax(
    "api/conf",{
        method: "POST",
        data: JSON.stringify(obj),
        success: handleSaleSub
    }
);