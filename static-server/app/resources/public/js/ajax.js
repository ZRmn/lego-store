class Ajax
{
    constructor(requestConfig)
    {
        this.requestConfig = requestConfig;
    }

    makeRequest()
    {
        return new Promise((onSuccess, onFail) =>
        {
            let request = new XMLHttpRequest();
            let resultRequestConfig = Object.assign(Ajax.getDefaultRequestConfig(), this.requestConfig);
            let {method, url, headers, body, async, responseType} = resultRequestConfig;

            method = method.toUpperCase();

            if ( method === "GET" || method === "HEAD")
            {
                url += "/?" + Ajax.paramsFromObject(body);
                body = null;
            }

            request.responseType = responseType;
            request.open(method, url, async);

            for (let key in headers)
            {
                if (headers.hasOwnProperty(key))
                {
                    request.setRequestHeader(key, headers[key]);
                }
            }

            request.onload = () =>
            {
                if (request.readyState === 4 && request.status === 200)
                {
                    onSuccess(request.response);
                }
                else
                {
                    onFail(request.status);
                }
            };

            request.send(JSON.stringify(body));
        });
    }

    static paramsFromObject(object)
    {
        return Object.keys(object)
            .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(object[key]))
            .join('&');
    }

    static getDefaultRequestConfig()
    {
        return {
            headers: {},
            body: {},
            async: true,
            responseType: "text"
        };
    }
}

