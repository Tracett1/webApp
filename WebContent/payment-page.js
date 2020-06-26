let cart_form = $("#cc-form");
function getCarttotal(){
    console.log("hey");
    var count = JSON.parse(sessionStorage.getItem("cart"));
    var placeHolder = 0;
    for (each in count){
        placeHolder+= count[each];
    }
    return placeHolder;

}
var total = getCarttotal();

function getTotalPrice(){
    var it = JSON.parse(sessionStorage.getItem("cart"));
    var count = 0;
    for (each in it){
        count += 10 * it[each];
    }
    return count;

}
function handlePaymentSub(resultData){
    console.log(resultData["errorMessage"]);
    console.log("TO SERVER SUCCESSFUL");
}
function submitCCForm(formSubmit){
    formSubmit.preventDefault();
    // go to the next page if it is processed correctly!
    console.log(cart_form.serialize());
    $.ajax(
        "api/payment",{
            method: "POST",
            data: cart_form.serialize(),
            success: handlePaymentSub
        }
    );



}
document.getElementById("cart_total").innerHTML = total;
document.getElementById("total_count").innerHTML= "$" + getTotalPrice().toString() + ".00";


cart_form.submit(submitCCForm);