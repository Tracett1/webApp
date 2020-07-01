let metatable = document.getElementById("meta_table_body");
let starForm = $("#star_form");
function handleMetaData(resultData){
    for (let i = 0; i < resultData.length; i++){
        var row = document.createElement("tr");
        let cell = document.createElement("td");
        let text = document.createTextNode(resultData[i]["table_name"]);
        cell.appendChild(text);
        row.appendChild(cell);
        let cell2 = document.createElement("td");
        let full_string = "";
        for(let j=0; j < resultData[i]["column_vars"].length; j++){
            full_string+= resultData[i]["column_vars"][j]["col_name"];
            full_string+= "(" +resultData[i]["column_vars"][j]["col_type"] + "), ";
        }
        let text2 = document.createTextNode(full_string);
        cell2.appendChild(text2);
        row.appendChild(cell2);

        metatable.appendChild(row);
    }
}
function handleStarServer(resultData){
    console.log("submitted!!!");
}
function handleSubmitStar(formSubmit){
    formSubmit.preventDefault();
    var pass_url = "api/dashboard?" + starForm.serialize();
    $.ajax(
        "api/dashboard", {
            method: "GET",
            url: pass_url,
            success: handleStarServer
        }
    );


}
$.ajax(
    "api/dashboard", {
        method: "POST",
        // Serialize the login form to the data sent by POST request
        // serialize will convert the values in the form into a byte array
        success: handleMetaData
    }
);

starForm.submit(handleSubmitStar());