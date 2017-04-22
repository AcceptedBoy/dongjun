var clickManagers = function(isLine) {
	var comFunc = null;
	var line = isLine;
	var delCol = null;
	var clickId;				// new: 移至分组时需要用到的id（当前点击对象的id）
	// allUrl = {};
	return {
		setOption: function(setting) {
			comFunc = setting.fn;
			
			// 给外加的url一个机会
			if(setting.url) {
				console.log('url');
			}
		},
		addSwitch: function() {
			// 表单置值
			$("#inputId").val("");
			$("#inputName").val("");
			if(line) {
				$("#inputLineId").val(
						$(".edit_switch_btn").parent("td").prevAll()[0].innerHTML);
			} else {
					$("#inputLineId").val("");
			}

			// 发请求修改刷新
		},
		editSwitch: function(self) {
			var column = $(self).parent("td").prevAll();
			console.log(column);
			if(line) {
				$("#editName").val(column[2].innerHTML);
				$("#editId").val(column[1].innerHTML);
				$("#editLineId").val(column[0].innerHTML);
			}
			else {
				$("#editName").val(column[1].innerHTML);
				$("#editId").val(column[0].innerHTML);
			}
		},
		delSwitch: function(self) {
			delCol = $(self).parent("td").prevAll();
		},
		moveSwitch: function(self) {	// new: 移至分组
			var column = $(self).parent("td").prevAll();
			clickId = column[2].innerHTML;
		},
		// expanded.
		editConfirm: function() {
			var editUrl = line?"/dongjun/platform_group/update":"/dongjun/group/update";
			var editParams = line?{
				"id" : $("#editId").val(),
				"name" : $("#editName").val(),
				"groupId" : $("#editLineId").val(),
				"type": 1,
				"companyId": '001'
			}:{
				"id" : $("#editId").val(),
				"name" : $("#editName").val(),
				// "companyId" : $("#editLineId").val()
			};
			console.log(editParams);
			$.ajax({
				type : "post",
				url : editUrl,
				async : false,
				data : editParams,
				success : function(data) {
					if(data!=null){
						try {
							comFunc();
						}
						catch(e) {
							console.warn('请传入完成函数');
						}
					}
				}
			});
		},
		delConfirm: function() {
			var delUrl = line?"/dongjun/platform_group/delete":"/dongjun/group/delete";
			var column = delCol || null;
			console.log(column);
			var id = line?column[3].innerHTML:column[1].innerHTML;
			$.ajax({
				type : "post",
				url : delUrl,
				async : false,
				data : {
					"id" : id,
					"type": 1,
				},
				success : function(data) {
					if(data!=null){
						try {
							comFunc();
						}
						catch(e) {
							console.warn('请传入完成函数');
						}
					}
				}
			});
		},
		addConfirm: function() {
				console.log(line);
				var addUrl = line?"/dongjun/platform_group/add":"/dongjun/group/add";
				// addUrl = 'http://127.0.0.1:8080/templates/manager/addSubstation';
				var addParams = line?{
					"name" : $("#inputName").val(),
					"groupId" : $("#inputLineId").val(),
					"type": 1
				}:{
					"name" : $("#inputName").val(),
				};
				console.log(addParams);

				$.ajax({
					type : "post",
					url : addUrl,
					async : false,
					data : addParams,
					success : function(data) {
						if(data!=null){
							try {
								comFunc();
							}
							catch(e) {
								console.warn('请传入完成函数');
							}
						}
					},
					error: function(err) {
						console.log('失败');
					}
				});
		},
		moveConfirm: function() {		// new: 移至分组
			var groupId = $('#move_switch_modal').find('.groups').val();
			$.ajax({
				url: '/dongjun/platform_group/move_new_group',
				type: 'POST',
				data: {
					id: clickId,
					groupId: groupId
				},
				success: function(res) {
					if(res) {
						comFunc($('.substations').val());
						alert('修改成功');
					}
				}
			})
		}
	};
};