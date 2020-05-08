
let host = "http://localhost:8080";
let headers = {
    "Accept": "application/json",
    "Content-Type": "application/json"
};

function testRequest()
{
    let method = document.forms.testForm.method.value;
    let url = document.forms.testForm.url.value;
    let body = JSON.parse(document.forms.testForm.requestBody.value);

    addHeader(headers);

    let requestConfig = {
        "method": method,
        "url":  host + url,
        "body": body,
        "headers": headers
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value => {
            document.getElementById("response").innerText = value;
        }, reason => {
            document.getElementById("response").innerText = reason;
        });
}

function signIn()
{
    let login = document.forms.signInForm.elements.login.value;
    let password = document.forms.signInForm.elements.password.value;

    let signInForm = {login, password};

    let requestConfig = {
        "method": "POST",
        "url":  host + "/sign-in",
        "body": signInForm,
        "headers": headers
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value => {
            let json = JSON.parse(value);
            saveAuthToken(json.value);
            window.location.replace("/");
        }, reason => {
            alert("error");
        });
}

function signOut()
{
    // let requestConfig = {
    //     "method": "POST",
    //     "url":  host + "/sign-out",
    //     "headers": headers
    // };
    // let ajax = new Ajax(requestConfig);
    //
    // ajax.makeRequest()
    //     .then(value => {
    //         deleteAuthToken();
    //         window.location.replace("/");
    //     }, reason => {
    //         alert("error");
    //     });
}

function getProducts()
{
    let requestConfig = {
        "method": "GET",
        "url":  host + "/products",
        "headers": headers
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value => {
            alert(value);
        }, reason => {
            alert("error");
        });
}

function addProduct()
{
    let title = document.forms.productForm.elements.title;
    let article = document.forms.productForm.elements.article;
    let category = document.forms.productForm.elements.category;
    let description = document.forms.productForm.elements.description;
    let releaseDate = document.forms.productForm.elements.releaseDate;
    let price = document.forms.productForm.elements.price;
    let pieces = document.forms.productForm.elements.pieces;
    let images = document.forms.productForm.elements.images.files;

    let productForm = {title, article, category, description, releaseDate, price, pieces, images};

    let requestConfig = {
        "method": "POST",
        "url": host + "/admin/products",
        "body": productForm,
        "headers": headers
    };
    let ajaxRequest = new Ajax(requestConfig);

    ajaxRequest.makeRequest()
        .then(value => {
            alert(value);
        }, reason => {
            alert(reason);
        });
}