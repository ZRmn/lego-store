let profile = document.getElementById("profile");
let cart = document.getElementById("cart");

if(getAuthToken())
{
    profile.textContent = "Выйти";
    profile.onclick = signOut;
    cart.href = "/cart";
}
else
{
    profile.textContent = "Войти";
    profile.href = "/sign-in";
    cart.onclick = ev => {
        window.location.replace("/sign-in");
    }
}