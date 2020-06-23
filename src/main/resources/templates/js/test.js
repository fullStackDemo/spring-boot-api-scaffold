//封装HTTP请求方法
const http = {
    // 请求方法
    request: (params = {}) => {
        // 默认参数和参数合并
        const requestParams = Object.assign({
            type: 'GET',
            url: '',
            dateType: 'json',//请求返回结果类型
            data: {
                // 请求参数
            },
            headers: {
                // 请求头
            },
            callback0: () => {
                // 报文成功返回后回调
            }
        }, params);
        // 请求
        const xhr = new XMLHttpRequest();
        // 请求方法
        const {type, data, url, headers} = requestParams;
        const requestMethod = ('' + type).toLocaleLowerCase();
        // url 是否携带参数
        const urlHasParams = requestMethod === 'get';
        const requestUrl = urlHasParams ? http.getUrlString(url, data) : url;

        // 参数类型
        const dataType = Object.prototype.toString.apply(data);

        xhr.open(type, requestUrl);
        if (dataType === '[object Array]') {
            xhr.setRequestHeader("Content-type", "application/json; charset=utf-8");
        }

        // 自定义表头
        Object.keys(headers).forEach(m => {
            xhr.setRequestHeader(m, headers[m]);
        });

        xhr.addEventListener("load", e => {
            const status = xhr.status;
            if (status === 200) {
                requestParams.callback0(xhr.response);
            } else {
                console.log(e);
            }
        });
        xhr.send(urlHasParams ? null : http.getFormData(data));
    },
    // Object 转化为 String
    getQueryString: (data = {}) => {
        let _r = [];
        Object.keys(data).forEach(m => {
            _r.push(`${m}=${JSON.stringify(data[m])}`);
        });
        _r = _r.join('&');
        return _r;
    },
    // GET 请求，参数合并到URL上
    getUrlString: (url, data) => {
        return url.includes('?') ? url + '&' + http.getQueryString(data) : url + '?' + http.getQueryString(data);
    },
    // formData
    getFormData: data => {
        const dataType = Object.prototype.toString.apply(data);
        // Object
        if (dataType === '[object Object]') {
            const formData = new FormData();
            Object.keys(data).forEach(m => {
                formData.append(m, data[m]);
            });
            return formData;
        } else {
            // Array
            return JSON.stringify(data);
        }
    }

};


// GET 参数类型为 Object
const test1 = () => {
    http.request({
        url: '/test/get',
        type: 'get',
        data: {
            name: 'get',
            age: 22
        },
        callback0: data => {
            document.querySelector('.test1Result').innerHTML = data;
        }
    })
};

// Get 参数存在于路径中
const testPath = () => {
    http.request({
        url: '/test/path/6666',
        type: 'get',
        data: {
            name: '111',
        },
        callback0: data => {
            document.querySelector('.testPathResult').innerHTML = data;
        }
    })
};

// post 参数类型为 Object
const test2 = () => {
    http.request({
        url: '/test/post2',
        type: 'post',
        data: {
            name: 'post',
            age: 22
        },
        callback0: data => {
            document.querySelector('.test2Result').innerHTML = data;
        }
    })
};

// POST 参数类型为 list
const test3 = () => {
    http.request({
        url: '/test/post3',
        type: 'post',
        data: [
            {
                name: 'post2',
                age: 22
            },
            {
                name: 'post3',
                age: 23
            }
        ],
        callback0: data => {
            document.querySelector('.test3Result').innerHTML = data;
        }
    })
};

// GET 获取数组ID
const test4 = () => {
    http.request({
        url: '/test/getList',
        type: 'get',
        data: {
            productId: '1,2,3'
        },
        callback0: data => {
            document.querySelector('.test4Result').innerHTML = data;
        }
    })
};

// 获取请求头数据
const testHeader = () => {
    http.request({
        url: '/test/header',
        type: 'post',
        headers: {
            privateHeader: "test88888",
            privateHeader2: "test888882",
        },
        callback0: data => {
            document.querySelector('.testHeaderResult').innerHTML = data;
        }
    })
};

// 获取所有请求头数据
const testHeaders = () => {
    http.request({
        url: '/test/headers',
        type: 'get',
        headers: {
            privateHeader: "test888881",
            privateHeader2: "test888882",
            privateHeader3: "test888883",
        },
        callback0: data => {
            document.querySelector('.testHeadersResult').innerHTML = data;
        }
    })
};

// 测试方法5
const test5 = () => {
    http.request({
        url: '/test/post5',
        type: 'post',
        data: [
            {
                name: 'post2',
                age: 22,
                list: [
                    {
                        name: 'post22',
                        age: 2233,
                    }
                ]
            },
            {
                name: 'post3',
                age: 23,
                list: [
                    {
                        name: 'post33',
                        age: 2233,
                    }
                ]
            }
        ],
        callback0: data => {
            document.querySelector('.test5Result').innerHTML = data;
        }
    })
};