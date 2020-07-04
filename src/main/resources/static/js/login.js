// 获取相关用户信息
const userNameInput = document.getElementById("js_input——user");
const passwordInput = document.getElementById("js_input——pwd");
const codeInput = document.getElementById("js_input——code");
const submitBtn = document.getElementById("submit");

// submit
submitBtn.onclick = () => {

	const userName = userNameInput.value;
	const password = passwordInput.value;
	const code = codeInput.value;

	// verify
	if (!userName) {
		weui.topTips('用户姓名不能为空');
		return;
	} else if (!password) {
		weui.topTips('用户密码不能为空');
		return;
	} else if (!code) {
		weui.topTips('验证码不能为空');
		return;
	}

	// 加密密码
	const newPassword = utils.generateMd5(userName, password);

	// 注册
	dataService.login({
		userName,
		password: newPassword,
		code,
	}).then(res => {
		const { code, data, message} = res;

		if (code != 200) {
			weui.topTips(message);
		} else {
			weui.topTips(`登录成功，欢迎`);
			utils.setCookie('token', data.token);
			location.href = location.origin + '/home.html';
		}
	})
};

// 刷新code
const refreshCode = ()=>{
	let codeNode = document.querySelector(".captchaCode");
	codeInput.value = '';
	codeNode.src = '';
	codeNode.src = '/api/v1/user/getCaptchaImage';
};