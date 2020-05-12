const express = require("express");
const hbs = require("hbs");
const app = express();
const publicResourcesPath = __dirname + "/resources/public";

app.set("view engine", "hbs");
app.set("views", publicResourcesPath + "/views");
app.use(express.static(publicResourcesPath));
hbs.registerPartials(publicResourcesPath + "/views/partials");

app.get("/", (request, response) => {
   response.redirect("/home");
});

app.get("/home", (request, response) => {
   response.render("home")
});

app.get("/products", (request, response) => {
   response.render("products")
});

app.get("/products/*", (request, response) => {
   response.render("product")
});

app.get("/cart", (request, response) => {
   response.render("cart")
});

app.get("/checkout", (request, response) => {
   response.render("checkout")
});

app.get("/sign-in", (request, response) => {
   response.render("sign-in")
});

app.get("/sign-up", (request, response) => {
   response.render("sign-up")
});

app.get("/admin/request", (request, response) => {
   response.render("admin-request")
});

app.get("/admin/products", (request, response) => {
   response.render("admin-products")
});

app.get("/admin/add-product", (request, response) => {
   response.render("admin-add-product")
});

app.listen(4815);