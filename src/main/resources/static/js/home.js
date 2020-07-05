// 获取相关用户信息

const titleDom = document.querySelector(".weui-form__title");

// getUserInfo
function getUserInfo() {
    dataService.getUserInfo({
        // token: utils.getCookie('token')
    }).then(res => {
        const {data} = res;
        if (data) {
            titleDom.innerHTML = 'Hello ' + data.userName + ', 欢迎登录追梦空间';
            createSocket(data.userId)
        }

    })
}


// websocket 连接

let socket;

const createSocket = (userId) => {
    if (typeof WebSocket == 'undefined') {
        console.log("浏览器不支持websocket");
    } else {
        let socketUrl = location.origin + "/websocket/" + userId;
        socketUrl = socketUrl.replace(/http|https/g, 'ws');
		console.log(socketUrl)
        if (socket != null) {
            socket.close();
            socket = null;
        }
        socket = new WebSocket(socketUrl);
        debugger
        // 建立连接
        socket.onopen = () => {
            console.log("建立连接");
        };
        // 获取消息
        socket.onmessage = message => {
            console.log(message);
        };

    }
};


getUserInfo();