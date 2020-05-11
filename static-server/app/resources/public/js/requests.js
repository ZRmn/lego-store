
let host = "http://localhost:8080";
let headersSample = {
    "Accept": "application/json",
    "Content-Type": "application/json"
};

function headers()
{
    let headers = {};
    Object.assign(headers, headersSample);
    addAuthenticationHeader(headers);
    return headers;
}

function testRequest()
{
    let method = document.forms.testForm.method.value;
    let url = document.forms.testForm.url.value;
    let params = JSON.parse(document.forms.testForm.requestBody.value);

    let requestConfig = {
        "method": method,
        "url":  host + url + method === "GET" ? "?" + objectToUrlParams(params) : "",
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
            alert("Логин или паоль введены неверно");
        });
}

function signUp()
{
    let fullName = document.forms.signUpForm.elements.fullName.value;
    let phoneNumber = document.forms.signUpForm.elements.phoneNumber.value;
    let login = document.forms.signUpForm.elements.email.value;
    let password = document.forms.signUpForm.elements.password.value;

    let params = {fullName, phoneNumber, login, password};

    let requestConfig = {
        "method": "POST",
        "url":  host + "/sign-up",
        "body": JSON.stringify(params),
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value => {
            window.location.replace("/sign-in");
        }, reason => {
            alert("Логин занят");
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

function getObjectUrl(resourceUrl)
{
    let requestConfig = {
        "method": "GET",
        "url":  host + "/resources" + resourceUrl,
        "responseType": "blob",
    };
    let ajax = new Ajax(requestConfig);

    return ajax.makeRequest()
        .then(value => {
            let urlCreator = window.URL;
            return urlCreator.createObjectURL(value);
        }, reason => {
            alert(reason);
        });
}

function getProductHtml(product)
{
    return `
        <a href="/products/${product.id}">
            <div class="product">
                
                    <div class="product_image">
                        <img id="product-${product.id}-image">
                    </div>
                    <div class="product_content">
                        <div class="product_title text-center">
                            <h1 class="text-center lego-name">${product.title}</h1>
                            <h6 class="text-center price">${product.price} р</h6>
                        </div>
                    </div>
            </div>
        </a>
    `
}

function showProducts(productsAsJson)
{
    let products = JSON.parse(productsAsJson);
    let afterNode = document.getElementById("sampleProduct");

    for(let product of products)
    {
        let productNode = document.createElement("a");
        let productHtml = getProductHtml(product);
        afterNode.after(productNode);
        productNode.innerHTML = productHtml;
        setImage(`product-${product.id}-image`, product.imageUrl);
        afterNode = productNode;
    }

}

function gerUrlParams()
{
    let urlParams = window.location.search.replace("?", "");
    return objectFromUrlParams(urlParams);
}

function setUrlParams(params)
{
    let urlParams = objectToUrlParams(params);

    if(urlParams)
    {
        window.location.search = "?" + urlParams;
    }
}

function getFormData()
{
    let query = document.forms.searchForm.elements.query.value;
    let priceFrom = document.forms.filtersForm.elements.priceFrom.value;
    let priceTo = document.forms.filtersForm.elements.priceTo.value;
    let piecesFrom = document.forms.filtersForm.elements.piecesFrom.value;
    let piecesTo = document.forms.filtersForm.elements.piecesTo.value;
    let yearFrom = document.forms.filtersForm.elements.yearFrom.value;
    let yearTo = document.forms.filtersForm.elements.yearTo.value;
    let orderMode = document.forms.orderForm.elements.order.value;
    let order, sortField;

    if(orderMode !== "dont sort")
    {
        [order, sortField] = orderMode.split("/");
    }

    return  {
        "query": query,
        "order": order,
        "sortField": sortField,
        "priceFrom": priceFrom,
        "priceTo": priceTo,
        "piecesFrom": piecesFrom,
        "piecesTo": piecesTo,
        "yearFrom": yearFrom,
        "yearTo": yearTo,
    };
}

function setFormDataFromUrlParams()
{
    let params = gerUrlParams();

    if(params)
    {
        if(params.query) document.forms.searchForm.elements.query.value = params.query;
        if(params.priceFrom) document.forms.filtersForm.elements.priceFrom.value = params.priceFrom;
        if(params.priceTo) document.forms.filtersForm.elements.priceTo.value = params.priceTo;
        if(params.piecesFrom) document.forms.filtersForm.elements.piecesFrom.value = params.piecesFrom;
        if(params.piecesTo) document.forms.filtersForm.elements.piecesTo.value = params.piecesTo;
        if(params.yearFrom) document.forms.filtersForm.elements.yearFrom.value = params.yearFrom;
        if(params.yearTo) document.forms.filtersForm.elements.yearTo.value = params.yearTo;

        if(params.order && params.sortField)
        {
            document.forms.orderForm.elements.order.value = params.order + "/" + params.sortField;
        }
    }
}

function getProducts()
{
    let url = host + "/products";

    if(window.location.search.length > 0)
    {
        url += "?" + window.location.search.replace("?", "");
    }

    let requestConfig = {
        "method": "GET",
        "url": url,
        "headers": headers(),
        "async": false,
        "responseType": ""
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(showProducts,
                reason => {
            alert(reason);
        });
}

async function showProduct(productAsJson)
{
    let product = JSON.parse(productAsJson);

    document.getElementById("title").innerText = product.product.article + " " + product.product.title;
    document.getElementById("price").innerText = product.product.price + " р";
    document.getElementById("availability").innerText = product.availability + " шт";
    document.getElementById("description").innerText = product.product.description;

    for (let i = 0; i < product.product.imageUrls.length && i < 4; i++)
    {
        let objectUrl;
        await getObjectUrl(product.product.imageUrls[i]).then(value =>
        {
            objectUrl = value
        });
        document.getElementById("image-" + (i + 1)).src = objectUrl;
    }

    if (product.product.imageUrls.length > 0)
    {
        let src = document.getElementById("image-1").src;
        document.getElementById("large-image").src = src;
    }
}

function setLargeImage(element)
{
    let id = element.querySelector("img").id;
    document.getElementById("large-image").src = document.getElementById(id).src;
}

function getProductAndShow()
{
    let path = window.location.pathname;
    let splittedPath = path.split("/");
    let id = splittedPath[splittedPath.length - 1];
    getProduct(id);
}

function getProduct(id)
{
    let requestConfig = {
        "method": "GET",
        "url": host + "/products/" + id,
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(showProduct,
            reason => {
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