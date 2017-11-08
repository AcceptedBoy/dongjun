;(function() {
	var Msg = function () {
		var snackbarContainer = document.querySelector('#notification')
		return {
			notify: function (text) {
				snackbarContainer.MaterialSnackbar.showSnackbar({
					message: text,
					timeout: 1000
				})
			}
		}
	}()


	function login() {
		$.ajax({
			type : 'POST',
			url : '/dongjun/login_form',
			data : {
				'name' : $('#login-name').val(),
				'password' : $('#login-pw').val()
			},
			success : function(data) {
				switch (data) {
					case '0':
						location.href = '/dongjun/companyManager/'
						break
					case '1':
						Msg.notify('用户名不存在')
						break
					case '2':
						Msg.notify('密码错误')
						break
					default:
						break
				}
			},
			error: function() {
				Msg.notify('网络错误')
			}
		})
	}

	$('#login-btn').click(login)
	document.onkeydown = function (e) {
		var ev = e || window.event
		if (ev.keyCode === 13) {
			login()
		}
	}
})()