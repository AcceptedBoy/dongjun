var dealSwitch = function() {
	return {
		getVolacc: function(type) {
			var volacc
			switch (type) {
				case 0:				// 低压？
					volacc = 10
					break
				case 1:				// 高压
					// volacc = 100 	// 原本为100，现改为1000
					volacc = 1000
					break
				case 2:				// 管控
					volacc = 100
					break
			}
			return volacc
		},
		getCuracc: function(type) {
			var curacc
			switch (type) {
				case 1:					// 高压
//					curacc = 100
					curacc = 1
					break
				case 0:
				case 2:
					curacc = 10
					break
			}
			return curacc
		},
		controlSwitch: function(id, type, sign) {
			var _self = this
			$.ajax({
				type: 'POST',
				url: 'control_switch',
				data: {
					switchId: id,
					type: type,
					sign: sign
				},
				success: function(res) {
					if(res == 'success') {
						$('#notice_msg').text('操作成功，正在获取最新开关状态。。。')
						_self.TheBtnStatus(type)
						setTimeout(function(){
							$('#security_modal').modal('hide')
							infoWindow.closeWin()
						}, 8000)
						monitorSet.getSwitch()
					} else {
						$('#notice_msg').text('操作失败')
					}
				},
				error: function(res) {
					$('#notice_msg').text('操作失败')
				}
			})
		},
		closeSwitch: function(id, type, marker) {
			this.controlSwitch(id, type, 1);
		},
		openSwitch: function(id, type) {
			this.controlSwitch(id, type, 0);
		},
		ignoreSwitch: function(id, node, marker) {
			$.ajax({
				url: '/dongjun/ignore_hitch_event',
				data: {
					switchId: id
				},
				success: function(res) {
					$('#security_modal').modal('hide');
					alert('操作成功');
					$('#notice_msg').text("将在 " + ' ' + " 秒内执行！");
					infoWindow.ignoreAlarm(marker)
					var zTree = $.fn.zTree.getZTreeObj("treeDemo");
					node.highlight = 0;
					zTree.updateNode(node);
				},
				error: function(res) {
					alert('操作失败')
					$('#notice_msg').text("将在 " + ' ' + " 秒内执行！")
				}
			});
		},
		TheBtnStatus: function(type) {
			if(type == 0) {
				for(var i = 0; i <= 20; i++) {
					window.setTimeout('dealSwitch.updateOpen(' + i + ')', i*1000);
				}
			} else {
				for(var i = 0; i <= 20; i++) {
					window.setTimeout('dealSwitch.updateOpen(' + i + ')', i*1000);
				}
			}
		},
		updateOpen: function(num) {
			var second = 20;
			if(num == second) {
				$("#open_switch_btn").attr("disabled", false);
				$("#close_switch_btn").attr("disabled", false);
			}
			else {
				$("#open_switch_btn").attr("disabled", true);
				$("#close_switch_btn").attr("disabled", true);
			}
		}
	};
}();