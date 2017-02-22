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
				location.href = "/templates/manager/index.html#substation_manager";
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

$('#register_worker_modal').on('keydown', function(e){
	e.stopPropagation()
})

$('#register_company_modal').on('keydown', function(e){
	e.stopPropagation()
})

function throttle(fn, delay) {
	var timer = null;
 	return function(){
 		var context = this, args = arguments;
 		clearTimeout(timer);
 		timer = setTimeout(function(){
 			fn.apply(context, args);
 		}, delay);
 	};
}

var registerObj = function() {
	return {
		companyId: '',
		companyList: [],
		registerWorker: function(name, realName, password, controlCode, self) {
			var that = this;
			if(name && password && controlCode && realName) {
				if(this.testContent([name, password, controlCode], [realName], [])) {
					if($('#registerPassword').val() == $('#registerPasswordConfirm').val()) {
						if(this.companyId) {
							$(self).attr('data-dismiss', 'modal');
							$.ajax({
								type: 'POST',
								url: '/dongjun/elecon/user_registy',
								data: {
									name: name,
									realName: realName,
									password: password,
									controlCode: controlCode,
									companyId: that.companyId      // '001'
								},
								success: function(res) {
									alert('注册成功！');
								},
								error: function(err) {
									alert('失败！');
								}
							});
						} else {
							alert('该公司还没有注册');
						}
					} else {
						$(self).attr('data-dismiss', '');
						alert('确认密码和密码不一致');
						$('#registerPasswordConfirm').val('')
					}
				} else {
					$(self).attr('data-dismiss', '');
					$('#warnMsg').show();
					setTimeout(function(){
						$('#warnMsg').hide();
					}, 3000);
//					alert('请输入正确的格式');
				}
			} else {
				$(self).attr('data-dismiss', '');
				alert('注册时请务必填写所有信息');
			}
		},
		registerCompany: function(companyName, description, address, mainStaff, phone, email, staffName, staffPassword, staffControlCode, self) {
			var that = this;
			if(companyName && description && address && mainStaff && phone && email && staffName && staffPassword && staffControlCode) {
				// testContent
				if(this.testContent([],[mainStaff],[phone])) {
					$(self).attr('data-dismiss', 'modal');
					// ajax
					$.ajax({
						type: 'POST',
						url: '/dongjun/elecon/company_registry',
						data: {
							name: companyName,
							description: description,
							address: address,
							mainStaff: mainStaff,
							phone: phone,
							email: email
						},
						success: function(res){
							if(res.success) {
								that.companyId = res.text
								$.ajax({
									type: 'POST',
									url: '/dongjun/elecon/user_registy',	//TODO
									data: {
										name: staffName,
										realName: mainStaff,
										password: staffPassword,
										controlCode: staffControlCode,
										companyId: res.text,
										mainStaff: 'yes'
									},
									success: function(res) {
										if(res.success) {
											alert('注册成功！');
										} else {
											alert('注册用户时出现错误');
										}
									}
								})
							} else {
								alert('注册公司时出现错误')
							}
						}
					})
				} else {
					$(self).attr('data-dismiss', '');
					alert('请填入对应格式的信息(真实姓名需要为中文)')
				}
			} else {
				$(self).attr('data-dismiss', '');
				alert('注册时请务必填写所有信息');
			}
		},
		testContent: function(data, letter, num) {
			for(var i = 0; i<data.length; i++) {
				if(data[i].replace(/[\w\d]+/, '')) {
					return false;
				}
			}
			for(var i = 0; i<letter.length; i++) {
				if(letter[i].replace(/[\u4e00-\u9fa5]+/, '')) {
					return false;
				}
			}
			for(var i = 0; i<num.length; i++) {
				if(num[i].replace(/\d+/, '')) {
					return false;
				}
			}
			return true;
		},
		clearText: function(companyName, name, realName, password, passConfirm, controlCode) {
			$(companyName).val('');
			$(name).val('');
			$(realName).val('');
			$(password).val('');
			$(passConfirm).val('');
			$(controlCode).val('');
		},
		clearCompanyText: function() {
			$('#companyName').val('');
			$('#description').val('');
			$('#address').val('');
			$('#mainStaff').val('');
			$('#phone').val('');
			$('#email').val('');
			$('#staffName').val('');
			$('#staffPassword').val('');
			$('#staffControlCode').val('');
		},
		showPassWord: function(self) {
			$(self).attr('type', 'text');
		},
		hidePassWord: function(self) {
			$(self).attr('type', 'password');
		},
		search: function(name) {
			$.ajax({
				type: 'POST',
				url: '/dongjun/elecon/fuzzy_search',
				data: {
					name: name
				},
				success: function(res) {
					that.companyList = res.text;
					if(res.success) {
						$('#companyList').html(registerObj.joinOp(res.text))
					} else {
						//
					}
				}
			})
		},
		joinOp: function(opArr) {
			var html = '';
			for(var i = 0; i<opArr.length; i++) {
				html += '<option value="' + opArr[i].name + '" />'
			}
			return html
		},
		fuzzySearch: throttle(function(name) {
//			$('#companyList').html(registerObj.joinOp([{name:'哈哈'},{name:'嘿嘿'}]))
			var that = this;
			$.ajax({
				type: 'POST',
				url: '/dongjun/elecon/fuzzy_search',
				data: {
					name: name
				},
				success: function(res) {
					if(res.success && res.text) {
						that.companyList = res.text;
						$('#companyList').html(registerObj.joinOp(res.text))
					} else {
						console.log('没有找到对应的公司所以搜索不出来')
					}
				}
			})
		}, 1500),
		editCompanyId(value) {
			var that = this;
			if(this.companyList.length) {
				this.companyList.forEach(function(company) {
					if(company.name == value) {
						that.companyId = company.id
					}
				})
			}
		},
	};
}();