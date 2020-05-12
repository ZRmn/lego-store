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

function adminRequest()
{
    let method = document.forms.adminForm.method.value;
    let url = document.forms.adminForm.url.value;
    let params = JSON.parse(document.forms.adminForm.requestBody.value);

    let requestConfig = {
        "method": method,
        "url": host + url,
        "body": JSON.stringify(params),
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    let responseElement = document.getElementById("response");

    ajax.makeRequest()
        .then(value =>
        {
            responseElement.innerText = value;
        }, reason =>
        {
            responseElement.innerText = reason;
        });
}

function getAdminProductHtml(product)
{
    return `
            <td> 
               ${product.id}
            </td>
            <td> 
               ${product.category}
            </td>
            <td> 
               ${product.title}
            </td>
            <td> 
               ${product.article}
            </td>
            <td> 
               ${product.price}
            </td>
            <td> 
               ${product.releaseDate}
            </td>
    `
}

function showAdminProducts(productsAsJson)
{
    let products = JSON.parse(productsAsJson);
    let afterNode = document.getElementById("sampleProduct");

    for (let product of products)
    {
        let productNode = document.createElement("tr");
        let productHtml = getAdminProductHtml(product);
        afterNode.after(productNode);
        productNode.innerHTML = productHtml;
        afterNode = productNode;
    }
}

function getAdminProducts()
{
    let requestConfig = {
        "method": "GET",
        "url": host + "/admin/products",
        "headers": headers(),
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(showAdminProducts);
}

function signIn()
{
    let login = document.forms.signInForm.elements.login.value;
    let password = document.forms.signInForm.elements.password.value;

    let params = {
        login,
        password
    };

    let requestConfig = {
        "method": "POST",
        "url": host + "/sign-in",
        "body": JSON.stringify(params),
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value =>
        {
            let json = JSON.parse(value);
            saveAuthToken(json.value);
            window.location.replace("/");
        }, reason =>
        {
            alert("Логин или паоль введены неверно");
        });
}

function signUp()
{
    let fullName = document.forms.signUpForm.elements.fullName.value;
    let phoneNumber = document.forms.signUpForm.elements.phoneNumber.value;
    let login = document.forms.signUpForm.elements.email.value;
    let password = document.forms.signUpForm.elements.password.value;

    let params = {
        fullName,
        phoneNumber,
        login,
        password
    };

    let requestConfig = {
        "method": "POST",
        "url": host + "/sign-up",
        "body": JSON.stringify(params),
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value =>
        {
            window.location.replace("/sign-in");
        }, reason =>
        {
            alert("Логин занят");
        });
}

function signOut()
{
    let requestConfig = {
        "method": "POST",
        "url": host + "/sign-out",
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value =>
        {
            deleteAuthToken();
            window.location.replace("/");
        }, reason =>
        {
            alert(reason);
        });
}

function setImage(imgId, imageUrl)
{
    let requestConfig = {
        "method": "GET",
        "url": host + "/resources" + imageUrl,
        "responseType": "blob"
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value =>
        {
            let urlCreator = window.URL;
            document.getElementById(imgId).src = urlCreator.createObjectURL(value);
        }, reason =>
        {
            alert(reason);
        });
}

function getObjectUrl(resourceUrl)
{
    let requestConfig = {
        "method": "GET",
        "url": host + "/resources" + resourceUrl,
        "responseType": "blob",
    };
    let ajax = new Ajax(requestConfig);

    return ajax.makeRequest()
        .then(value =>
        {
            let urlCreator = window.URL;
            return urlCreator.createObjectURL(value);
        }, reason =>
        {
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

    for (let product of products)
    {
        let productNode = document.createElement("a");
        let productHtml = getProductHtml(product);
        afterNode.after(productNode);
        productNode.innerHTML = productHtml;
        setImage(`product-${product.id}-image`, product.imageUrl);
        afterNode = productNode;
    }
}

function clearFilters()
{
    let category = document.forms.filtersForm.elements.category.value;

    if (category)
    {
        window.location.search = "?category=" + category;
    }
    else
    {
        window.location.search = "";
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

    if (urlParams)
    {
         window.location =  "/products?" + urlParams;
    }
}

function getFormData()
{
    let priceTo;
    let priceFrom;
    let piecesFrom;
    let piecesTo;
    let yearFrom;
    let yearTo;
    let category;
    let order, sortField;

    if(document.forms.filtersForm)
    {
        priceFrom = document.forms.filtersForm.elements.priceFrom.value;
        priceTo = document.forms.filtersForm.elements.priceTo.value;
        piecesFrom = document.forms.filtersForm.elements.piecesFrom.value;
        piecesTo = document.forms.filtersForm.elements.piecesTo.value;
        yearFrom = document.forms.filtersForm.elements.yearFrom.value;
        yearTo = document.forms.filtersForm.elements.yearTo.value;
        category = document.forms.filtersForm.elements.category.value;
        let orderMode = document.forms.orderForm.elements.order.value;

        if (orderMode !== "dont sort")
        {
            [order, sortField] = orderMode.split("/");
        }
    }

    let query = document.forms.searchForm.elements.query.value;

    return {
        "query": query,
        "order": order,
        "sortField": sortField,
        "priceFrom": priceFrom,
        "priceTo": priceTo,
        "piecesFrom": piecesFrom,
        "piecesTo": piecesTo,
        "yearFrom": yearFrom,
        "yearTo": yearTo,
        "category": category
    };
}

function setFormDataFromUrlParams()
{
    let params = gerUrlParams();

    if (params)
    {
        if (params.query)
        {
            document.forms.searchForm.elements.query.value = params.query;
        }
        if (params.priceFrom)
        {
            document.forms.filtersForm.elements.priceFrom.value = params.priceFrom;
        }
        if (params.priceTo)
        {
            document.forms.filtersForm.elements.priceTo.value = params.priceTo;
        }
        if (params.piecesFrom)
        {
            document.forms.filtersForm.elements.piecesFrom.value = params.piecesFrom;
        }
        if (params.piecesTo)
        {
            document.forms.filtersForm.elements.piecesTo.value = params.piecesTo;
        }
        if (params.yearFrom)
        {
            document.forms.filtersForm.elements.yearFrom.value = params.yearFrom;
        }
        if (params.yearTo)
        {
            document.forms.filtersForm.elements.yearTo.value = params.yearTo;
        }
        if (params.category)
        {
            document.forms.filtersForm.elements.category.value = params.category;
        }

        if (params.order && params.sortField)
        {
            document.forms.orderForm.elements.order.value = params.order + "/" + params.sortField;
        }
    }
}

function getProducts()
{
    let url = host + "/products";

    if (window.location.search.length > 0)
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
            reason =>
            {
                alert(reason);
            });
}

async function showProduct(productAsJson)
{
    let product = JSON.parse(productAsJson);

    document.getElementById("productId").value = product.product.id;
    document.getElementById("title").innerText = product.product.article + " " + product.product.title;
    document.getElementById("price").innerText = product.product.price + " р";
    document.getElementById("availability").innerText = product.availability;
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
            reason =>
            {
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

    for (let file in files)
    {
        if (files.hasOwnProperty(file))
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
        .then(value =>
        {
            alert(value);
        }, reason =>
        {
            alert(reason);
        });
}

function addToCart()
{
    let productId = document.getElementById("productId").value;
    let availability = document.getElementById("availability").innerText;
    let quantity = document.getElementById("quantity_input").value;

    if (Number(availability) >= Number(quantity))
    {
        let cartItemForm = {
            "productId": productId,
            "quantity": quantity
        };

        let requestConfig = {
            "method": "POST",
            "url": host + "/cart",
            "body": JSON.stringify(cartItemForm),
            "headers": headers()
        };
        let ajaxRequest = new Ajax(requestConfig);

        ajaxRequest.makeRequest()
            .then(value =>
            {
                window.location.replace("/cart");
            });
    }
}

function getCartItemHtml(cartItem)
{
    return `
                    <div class="cart_item d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-start">
                        <div class="cart_item_product d-flex flex-row align-items-center justify-content-start">
                            <div class="cart_item_image">
                                <div><img id="cart-item-${cartItem.id}-image"></div>
                            </div>
                            <div class="cart_item_name_container">
                                <div class="cart_item_name"><a href="/products/${cartItem.product.id}">${cartItem.product.article + " " + cartItem.product.title}</a>
                                </div>
                            </div>
                        </div>
                        <div class="cart_item_price">${cartItem.product.price} р</div>
                        <div class="cart_item_quantity">
                            <div class="product_quantity_container">
                                <div class="product_quantity clearfix">
                                    <input id="quantity_input" type="text" pattern="[0-9]*" value="${cartItem.quantity}">
                                    <div class="quantity_buttons">
                                        <div id="quantity_inc_button" class="quantity_inc quantity_control"><i
                                                class="fa fa-chevron-up" aria-hidden="true"></i></div>
                                        <div id="quantity_dec_button" class="quantity_dec quantity_control"><i
                                                class="fa fa-chevron-down" aria-hidden="true"></i></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
    `
}

function getCartItems(onSuccess)
{
    let requestConfig = {
        "method": "GET",
        "url": host + "/cart",
        "headers": headers(),
        "async": false,
        "responseType": ""
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(onSuccess,
            reason =>
            {
                window.location.replace("/sign-in");
            });
}

function showCartItems(cartItemsAsJson)
{
    let cartItems = JSON.parse(cartItemsAsJson);
    let afterNode = document.getElementById("sampleCartItem");

    let totalPrice = 0;

    for (let cartItem of cartItems)
    {
        totalPrice += cartItem.product.price * cartItem.quantity;
        let cartItemNode = document.createElement("div");
        let cartItemHtml = getCartItemHtml(cartItem);
        afterNode.after(cartItemNode);
        cartItemNode.innerHTML = cartItemHtml;
        setImage(`cart-item-${cartItem.id}-image`, cartItem.product.imageUrls[0]);
        afterNode = cartItemNode;
    }

    document.getElementById("totalPrice").innerText = totalPrice + " р";
}

function clearCart()
{
    let requestConfig = {
        "method": "DELETE",
        "url": host + "/cart",
        "headers": headers(),
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value =>
        {
            window.location.replace("/");
        });
}

function showOrder(cartItemsAsJson)
{
    let cartItems = JSON.parse(cartItemsAsJson);
    let totalPrice = 0;

    for (let cartItem of cartItems)
    {
        totalPrice += cartItem.product.price * cartItem.quantity;
    }

    document.getElementById("totalPrice").innerText = totalPrice + " р";
}

function placeOrder()
{
    let address = document.getElementById("checkout_address").value;
    let payment = document.querySelector('input[name="radio_payment"]:checked').value;
    let delivery = document.querySelector('input[name="radio_delivery"]:checked').value;

    let orderForm = {
        "address": address,
        "payment": payment,
        "delivery": delivery
    };

    let requestConfig = {
        "method": "POST",
        "url": host + "/orders",
        "body": JSON.stringify(orderForm),
        "headers": headers()
    };
    let ajax = new Ajax(requestConfig);

    ajax.makeRequest()
        .then(value =>
        {
            window.location.replace("/");
        });
}