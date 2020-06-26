var jsonCart = JSON.parse(sessionStorage.getItem("cart"));
function handleQuantity(quantity, index){
    console.log("this is index");
    console.log(index);
    jsonCart[index] = parseInt(quantity);
    sessionStorage.setItem("cart", JSON.stringify(jsonCart));
    console.log(JSON.parse(sessionStorage.getItem("cart")));
    displayTotal();
}
function handleCheckout(){
    window.location.replace("payment-page.html");
}

let table_body = document.getElementById("cart_table_body");
for (i in jsonCart) {

    var row = document.createElement("tr");
    let cell = document.createElement("td");
    let text = document.createTextNode(i);
    cell.appendChild(text);
    let cell2 = document.createElement("td");
    let text2 = document.createTextNode("$10.00");
    cell2.appendChild(text2);

    let inputSelector = document.createElement("INPUT");
    inputSelector.setAttribute("type", "number");
    let quantity = jsonCart[i].toString();
    inputSelector.setAttribute("value",quantity);
    inputSelector.setAttribute("min","0");
    inputSelector.setAttribute("max","100");
    inputSelector.setAttribute("step","1");
    inputSelector.setAttribute("id", i);
    //inputSelector.addEventListener("change", handleQuantity(i));
    inputSelector.onchange = function(){
        handleQuantity(inputSelector.value,inputSelector.id);
    }
    let cell3 = document.createElement("td");
    cell3.appendChild(inputSelector);

    row.appendChild(cell);
    row.appendChild(cell2);
    row.appendChild(cell3);

    table_body.appendChild(row);

}

function displayTotal(){
    var total = JSON.parse(sessionStorage.getItem("cart"));
    var count = 0;
    for (each in total){
        count += 10 * total[each];
    }
    var totalElement = document.getElementById("total");
    totalElement.innerHTML = "Total:\n$" + count.toString() + ".00";
}

displayTotal();