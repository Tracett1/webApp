function createDataToSend(){
    var movieToQuantity = JSON.parse(sessionStorage.getItem("cart"));
    var movieToId = JSON.parse(sessionStorage.getItem("movieids"));
    var obj = '{}';
    var jsonObj = JSON.parse(obj);

    for (each in movieToQuantity){
        var movieId = movieToId[each].toString();
        var quantity = movieToQuantity[each].toString();
        jsonObj[movieId] = quantity;
    }
    return jsonObj
}

function handleSaleSub(){
    console.log("heyyyyyy it worked");
}
var obj = createDataToSend();
var param = JSON.parse('{}');
param["param"] = obj;
console.log(param);

$.ajax(
    "api/conf",{
        method: "POST",
        data: JSON.stringify(obj),
        success: handleSaleSub
    }
);