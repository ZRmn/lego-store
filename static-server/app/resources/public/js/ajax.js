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

            request.send(body);
        });
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

