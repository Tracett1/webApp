let emp_login_form = $("#emp_login_form");

function handleLoginResult(resultData){
    let resultDataJson = JSON.parse(resultData);
    if (resultDataJson["status"] == "success"){
        console.log("admin login success!");
        window.location.replace("_dashboard.html");
    }
    else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
    }
}


function handleSubmit(formSubmit){
    formSubmit.preventDefault();
    $.ajax(
        "api/dblogin", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            // serialize will convert the values in the form into a byte array
            data: emp_login_form.serialize(),
            success: handleLoginResult
        }
    );
}



emp_login_form.submit(handleSubmit);