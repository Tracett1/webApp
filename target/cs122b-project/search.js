let search_form = $("#search_form");


function submitSearchForm(formSubmitEvent){
    formSubmitEvent.preventDefault();
    window.location.replace("index.html?pageNum=1&sortBy=tite&order=ASC&browse=NO&" + search_form.serialize());
}


search_form.submit(submitSearchForm);