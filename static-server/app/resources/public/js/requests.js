
let host = "http://localhost:8080";
let headersSample = {
    "Accept": "application/json",
    "Content-Type": "application/json"
};

function headers()
{
    let headers = {};
    Object.assign(headers, headersSample);
    addAuthenticationHeader(headersSample);
}

function testRequest()
{
    let method = document.forms.testForm.method.value;
    let url = document.forms.testForm.url.value;
    let params = JSON.parse(document.forms.testForm.requestBody.value);

    let requestConfig = {
        "method": method,
        "url":  host + url,
        "params": params,
        "body": JSON.stringify(params),
        "headers": headers()
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

    let params = {login, password};

    addAuthenticationHeader(headersSample);

    let requestConfig = {
        "method": "POST",
        "url":  host + "/sign-in",
        "body": JSON.stringify(params),
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value => {
            let json = JSON.parse(value);
            saveAuthToken(json.value);
            window.location.replace("/");
        }, reason => {
            alert(reason);
        });
}

function signOut()
{
    let requestConfig = {
        "method": "POST",
        "url":  host + "/sign-out",
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value => {
            deleteAuthToken();
            window.location.replace("/");
        }, reason => {
            alert(reason);
        });
}

function getProducts()
{
    let requestConfig = {
        "method": "GET",
        "url":  host + "/products",
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value => {
            alert(value);
        }, reason => {
            alert(reason);
        });
}

function addProduct()
{
    let files = document.forms.productForm.elements.images.files;

    let formData = new FormData();
    formData.append("title", document.forms.productForm.elements.title.value);
    formData.append("article", document.forms.productForm.elements.article.value);
    formData.append("category", document.forms.productForm.elements.category.value);
    formData.append("description", document.forms.productForm.elements.description.value);
    formData.append("releaseDate", document.forms.productForm.elements.releaseDate.value);
    formData.append("price", document.forms.productForm.elements.price.value);
    formData.append("pieces", document.forms.productForm.elements.pieces.value);

    for(let file in files)
    {
        if(files.hasOwnProperty(file))
        {
            formData.append("images", files[file]);
        }
    }

    let headers = {};
    addAuthenticationHeader(headers);

    let requestConfig = {
        "method": "POST",
        "url": host + "/admin/products",
        "body": formData,
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

function setImage(imgId, imageUrl)
{
    let requestConfig = {
        "method": "GET",
        "url":  host + "/resources" + imageUrl,
        "responseType": "blob"
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value => {
            let urlCreator = window.URL;
            document.getElementById(imgId).src = urlCreator.createObjectURL(value);
        }, reason => {
            alert(reason);
        });
}