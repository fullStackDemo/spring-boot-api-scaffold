// 获取相关用户信息

const titleDom = document.querySelector(".weui-form__title");
const result = document.getElementById("result");

// getUserInfo
function getUserInfo() {
    dataService.getUserInfo().then(res => {
        const {code, data} = res;
        if (code === 401) {
            location.href = location.origin + "/login.html";
            return;
        } else if (code == 1001) {
            alert("你的账户已经在别的设备登录");
            location.href = location.origin + "/login.html";
            return;
        }
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
        console.log(socketUrl);
        if (socket != null) {
            socket.close();
            socket = null;
        }
        socket = new WebSocket(socketUrl);
        // 建立连接
        socket.onopen = () => {
            console.log("建立连接");

            socket.send(JSON.stringify({
                userId,
                query: 'onLineNumber'
            }))
        };
        // 获取消息
        socket.onmessage = message => {
            console.log(message);
            result.innerText = message.data;
        };

    }
};


getUserInfo();