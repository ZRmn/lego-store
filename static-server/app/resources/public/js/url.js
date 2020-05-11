function objectToUrlParams(object)
{
    return Object.keys(object)
        .filter(value => object[value])
        .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(object[key]))
        .join('&');
}

function objectFromUrlParams(params)
{
    let object = {};

    params
        .split("&")
        .forEach(param => {
            let s = param.split("=");
            object[decodeURIComponent(s[0])] = decodeURIComponent(s[1]);
        });

    return object;
}