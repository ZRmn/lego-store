function saveAuthToken(token)
{
    localStorage.setItem("authToken", token);
}

function deleteAuthToken()
{
    localStorage.removeItem("authToken");
}

function getAuthToken()
{
    return localStorage.getItem("authToken");
}

function addHeader(headers)
{
    let authToken = getAuthToken();
    if (authToken !== null)
    {
        headers["Auth-Token"] = authToken;
    }
}