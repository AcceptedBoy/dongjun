$(document).ready(function() {
	document.onkeydown = function(e) {
		var ev = e || window.event
		if(ev.keyCode == 13) {
			login()
		}
	}
	$("#login_form_btn").click(login);
});

/**
 * 
 * @Title: login
 * @Description: TODO
 * @param
 * @return void
 * @throws
 */
function login() {

	$.ajax({
		type : "post",
		url : "elecon/login_form",
		async : false,
		data : {
			"name" : $("#inputEmail").val(),
			"password" : $("#inputPassword").val()
		},
		success : function(data) {

			switch (data) {
			case "0":
				location.href = "/templates/monitor/index.html";
				break;
			case "1":
				$("#error_msg").text("用户名不存在！");
				break;
			case "2":
				$("#error_msg").text("密码错误！");
				break;
			default:
				break;
			}

		}

	})

}
