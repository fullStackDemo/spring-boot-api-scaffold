//封装HTTP请求方法
const http = {
    // 请求方法
    request: (params = {}) => {
        // 默认参数和参数合并
        const requestParams = Object.assign({
            type: 'GET',
            url: '',
            dateType: 'json',//请求返回结果类型
            data: '', // 请求参数
            callback0: () => {
                // 成功后回调
            }
        }, params);
        // 请求
        const xhr = new XMLHttpRequest();
        // 请求方法
        const {type, data, url} = requestParams;
        const requestMethod = ('' + type).toLocaleLowerCase();
        // url 是否携带参数
        const urlHasParams = requestMethod === 'get';
        const requestUrl = urlHasParams ? http.getUrlString(url, data) : url;
        debugger
        xhr.open(type, requestUrl);
        xhr.addEventListener("load", e => {
            const status = xhr.status;
            if (status === 200) {
                requestParams.callback0(xhr.response);
            } else {
                console.log(e);
            }
        });
        xhr.send(urlHasParams ? null : requestParams.data);
    },
    // Object 转化为 String
    getQueryString: (data = {}) => {
        let _r = [];
        Object.keys(data).forEach(m => {
            _r.push(`${m}=${data[m]}`);
        });
        _r = _r.join('&');
        return _r;
    },
    // GET 请求，参数合并到URL上
    getUrlString: (url, data) => {
        return url.includes('?') ? url + '&' + http.getQueryString(data) : url + '?' + http.getQueryString(data);
    }

};


// 测试方法1
const test1 = () => {
    http.request({
        url: '/test/get',
        type: 'get',
        data: {
            name: 'test',
            age: 22
        },
        callback0: data => {
            console.log(data)
        }
    })
};