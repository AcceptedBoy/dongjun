var infoWindow = function() {
/*	var opts = {
		width : 580, // 信息窗口宽度
		enableCloseOnClick: false,
		enableMessage: false
	}*/
	var win = new BMap.InfoWindow('asdasdasd', {
		width : 580, // 信息窗口宽度
		enableCloseOnClick: false,
		enableMessage: false
	})
	
	var now = {
		id: '',
		type: -1,
		status: '-1',
		marker: null
	}

	var winStore = {
		msg: {
			cur: false,
			vol: false,
			status: false
		},
		winDOM: null,
		cloneWin: null,
	}

	win.addEventListener('close', function(){
		$('#mapInfoWin').off('click')
		winStore.winDOM = null
		winStore.cloneWin = null
		monitorSet.stopRead()
	})
	return {
		closeWin: function(){
			now.marker.closeInfoWindow()
		},
		setCenter: function() {
			djMap.Model.setCenter(now.id, now.type)
		},
		setNowData: function(marker) {
			now.marker = marker
			now.id = marker.id
			now.type = marker.type
			now.status = marker.status
		},
		initWinStore: function() {
			var winDOM = $('#mapInfoWin')
			winStore.status = now.status
			winStore.winDOM = winDOM
			winStore.cloneWin = winDOM.clone()
		},
		updateWinStore: function(type, cloneWin) {
			winStore.msg[type] = true
			// console.log('获取信息：' + type)
			winStore.cloneWin = cloneWin
			this.updateWinDOM()
		},
		updateWinDOM: function() {
			var status = now.status
			var type = now.type
			if(type == 0) {	// 如果是低压，但没有读到电流
				if(!winStore.msg.cur || !winStore.msg.vol){
					return
				}
			} else if(type == 1){
				if(status == '-1' && !winStore.msg.status) {	// 如果是离线 但没有读到状态
					return
				} else if(status != '-1') {	// 高压在线
					for(var k in winStore.msg) {
						if(!winStore.msg[k]){							// 如果没有全读到（cur、 vol、 status）
							return
						}
					}
				}	
			}
			// console.log('更新窗口信息')
			winStore.winDOM.html(winStore.cloneWin.html())	// cur、 vol、 status数据更新完， 再一起更新进去
			for(var k in winStore.msg) {										// 重置读取信息状态
				winStore.msg[k] = false
			}
		},
		click_low_voltage_switch: function(marker) {
			this.setNowData(marker)
			var content = '<div class="BDM_custom_popup" id="mapInfoWin">'
										+ '<h4>'
										+ marker.name
										+ '<button type="button" class="close" data-dismiss="modal" aria-label="Close" data-fnc="close">'
										+ '<span aria-hidden="true" data-fnc="close">' + '&times;'
										+ '</span>' + '</button>'
										+ '</h4>'
										+ '<table class="table table-bordered table-condensed">'
										+ '<thead><tr><th>开关控制</th><th>状态</th><th id="status"></th></tr></thead>'
										+ '<tbody>'
										+ '<tr><td></td><td>电压</td><td>电流</td></tr>'
										+ '<tr><td>A相</td><td id="a_phase_voltage" class="red"></td><td id="a_phase_current" class="red"></td></tr>'
										+ '<tr><td>B相</td><td id="b_phase_voltage" class="red"></td><td id="b_phase_current" class="red"></td></tr>'
										+ '<tr><td>C相</td><td id="c_phase_voltage" class="red"></td><td id="c_phase_current" class="red"></td></tr>'
										+ '</tbody></table>'
										+ '<button data-fnc="close_switch" class="btn btn-primary">合闸</a>'
										+ '<button data-fnc="open_switch" class="btn btn-primary">分闸</a>'
										+ "</div>"
			win.setContent(content)
			marker.openInfoWindow(win)
			this.eventHandler()
			this.initWinStore()
			// monitorSet.readlvSwitchStatus(marker.id)
			monitorSet.readCurrentVoltage(marker.id, marker.type)
		},
		// 离线
		click_high_voltage_switch_out: function(marker) {
			// curMarker = marker
			this.setNowData(marker)
			var content = '<div class="BDM_custom_popup" id="mapInfoWin">'+'<h4>'
									+ marker.name + '&nbsp;&nbsp;'
									+ '<button class="btn btn-info btn-mini" data-fnc="set_center">设为中心点</button>'
									+ '<button type="button" class="close" data-fnc="close">'
									+ '<span aria-hidden="true" data-fnc="close">' + '&times;'
									+ '</span>' + '</button>'
									+ "</h4>"
									+ '<table class="table table-bordered table-condensed">'
									+ '<tbody>'
									+ '<tr>'
									+ '<td>断路器位置</td>'
									+ '<td id="status"></td>'
									+ '</table>'
									+ '</div>'
			win.setContent(content)
			marker.openInfoWindow(win)
			this.eventHandler()
			this.initWinStore()
			monitorSet.hvSwitchStatusSpy(now.id)
		},
		// 以下是临时加需求 盲改代码 晚上2点
		// pending 
		click_high_voltage_switch_pending: function(marker) {
			// curMarker = marker
			this.setNowData(marker)
			var msg = '设备已连接，暂时无法读取状态'
			var content = '<div class="BDM_custom_popup" id="mapInfoWin">'+'<h4>'
									+ marker.name + '&nbsp;&nbsp;'
									+ '<button class="btn btn-info btn-mini" data-fnc="set_center">设为中心点</button>'
									+ '<button type="button" class="close" data-fnc="close">'
									+ '<span aria-hidden="true" data-fnc="close">' + '&times;'
									+ '</span>' + '</button>'
									+ "</h4>"
									+ '<table class="table table-bordered table-condensed">'
									+ '<tbody>'
									+ '<tr>'
									+ '<td>断路器位置</td>'
									+ '<td id="status"> ' + msg + '</td>'
									+ '</table>'
									+ '</div>'
			win.setContent(content)
			marker.openInfoWindow(win)
			this.eventHandler()
			// this.initWinStore()
			// monitorSet.hvSwitchStatusSpy(now.id)
		},
		// 合闸和分闸是可以合并起来= =
		// 合闸
		click_high_voltage_switch_close: function(marker) {
			// curMarker = marker
			this.setNowData(marker)
			var content = '<div class="BDM_custom_popup" id="mapInfoWin">' + '<h4>'
										+ marker.name + '&nbsp;&nbsp;'
										+ '<button class="btn btn-info btn-mini" data-fnc="set_center">设为中心点</button>'
										+ '<button type="button" class="close" data-dismiss="modal" aria-label="Close" data-fnc="close">'
										+ '<span aria-hidden="true" data-fnc="close">' + '&times;'
										+ '</span>' + '</button>'
										+ '</h4>'
										+ '<table class="table table-bordered table-condensed">'
										+ '<tbody>'
										+ '<tr>'
										+ '<td></td><td>电压</td><td>电流</td>'
										+ '<td>过流I段保护</td>'
										+ '<td id="guo_liu_yi_duan"></td>'
										+ '</tr>'
										+ '<tr>'
										+ '<td>A相</td><td id="a_phase_voltage" class="red"></td><td id="a_phase_current" class="red"></td>'
										+ '<td>过流II段保护</td>'
										+ '<td id="guo_liu_er_duan"></td>'
										+ '</tr>'
										+ '<tr>'
										+ '<td>B相</td><td id="b_phase_voltage" class="red"></td><td id="b_phase_current" class="red"></td>'
										+ '<td>过流III段保护</td>'
										+ '<td id="guo_liu_san_duan"></td>'
										+ '</tr>'
										+ '<tr>'
										+ '<td>C相</td><td id="c_phase_voltage" class="red"></td><td id="c_phase_current" class="red"></td>'
										+ '<td>零序过流保护</td>'
										+ '<td id="ling_xu_guo_liu_"></td>'
										+ '</tr>'
										+ '</tbody></table>'
										+ '<table class="table table-bordered table-condensed">'
										+ '<tbody>'
										+ '<tr>'
										+ '<td>断路器位置</td>'
										+ '<td id="status"></td>'
										+ '<td>PT2过压告警</td>'
										+ '<td id="pt2_guo_ya"></td>'
										+ '<td>重合闸动作</td>'
										+ '<td id="chong_he_zha"></td>'
										+ '</tr>'
										+ '<tr>'
										+ '<td>PT1有压</td>'
										+ '<td id="pt1_you_ya"></td>'
										+ '<td>交流失电告警</td>'
										+ '<td id="jiao_liu_shi_dian"></td>'
										+ '<td>遥控复归</td>'
										+ '<td id="yao_kong_fu_gui"></td>'
										+ '</tr>'
										+ '<tr>'
										+ '<td>PT2有压</td>'
										+ '<td id="pt2_you_ya"></td>'
										+ '<td>手动合闸动作</td>'
										+ '<td id="shou_dong_he_zha"></td>'
										+ '<td>遥控器合闸</td>'
										+ '<td id="yao_kong_he_zha"></td>'
										+ '</tr>'
										+ '<tr>'
										+ '<td>PT1过压告警</td>'
										+ '<td id="pt1_guo_ya"></td>'
										+ '<td>手动分闸动作</td>'
										+ '<td id="shou_dong_fen_zha"></td>'
										+ '<td>遥控器分闸</td>'
										+ '<td id="yao_kong_fen_zha"></td>'
										+ '</tr>'
										+ '</tbody></table>'
										// + '<button data-fnc="close_switch" class="btn btn-primary" data-loading-text="合闸中...">合闸</button>'
										+ '<button data-fnc="open_switch" class="btn btn-primary">分闸</button>'
										+ '</div>'
			win.setContent(content)
			marker.openInfoWindow(win)
			this.eventHandler()
			this.initWinStore()
			monitorSet.hvSwitchStatusSpy(marker.id)
			monitorSet.readCurrentVoltage(marker.id, marker.type)
		},
		// 分闸
		click_high_voltage_switch_open: function(marker) {
			// curMarker = marker
			// console.log(marker)
			var ignoreBtn = marker.open? '<button data-fnc="ignore_switch" class="btn btn-primary" data-loading-text="忽略...">忽略</button>' : ''
			this.setNowData(marker)
			var content = '<div class="BDM_custom_popup" id="mapInfoWin">' + '<h4>'
									+ marker.name + '&nbsp;&nbsp;'
									+ '<button class="btn btn-info btn-mini" data-fnc="set_center">设为中心点</button>'
									+ '<button type="button" class="close" data-dismiss="modal" aria-label="Close" data-fnc="close">'
									+ '<span aria-hidden="true" data-fnc="close">' + '&times;'
									+ '</span>' + '</button>'
									+ "</h4>"
									+ "<table class='table table-bordered table-condensed'>"
									+ "<tbody>"
									+ "<tr>"
									+ "<td></td><td>电压</td><td>电流</td>"
									+ "<td>过流I段保护</td>"
									+ "<td id='guo_liu_yi_duan'></td>"
									+ "</tr>"
									+ "<tr>"
									+ "<td>A相</td><td id='a_phase_voltage' class='red'></td><td id='a_phase_current' class='red'></td>"
									+ "<td>过流II段保护</td>"
									+ "<td id='guo_liu_er_duan'></td>"
									+ "</tr>"
									+ "<tr>"
									+ "<td>B相</td><td id='b_phase_voltage' class='red'></td><td id='b_phase_current' class='red'></td>"
									+ "<td>过流III段保护</td>"
									+ "<td id='guo_liu_san_duan'></td>"
									+ "</tr>"
									+ "<tr>"
									+ "<td>C相</td><td id='c_phase_voltage' class='red'></td><td id='c_phase_current' class='red'></td>"
									+ "<td>零序过流保护</td>"
									+ "<td id='ling_xu_guo_liu_'></td>"
									+ "</tr>"
									+ "</tbody></table>"
									+ "<table class='table table-bordered table-condensed'>"
									+ "<tbody>"
									+ "<tr>"
									+ "<td>断路器位置</td>"
									+ "<td id='status'></td>"
									+ "<td>PT2过压告警</td>"
									+ "<td id='pt2_guo_ya'></td>"
									+ "<td>重合闸动作</td>"
									+ "<td id='chong_he_zha'></td>"
									+ "</tr>"
									+ "<tr>"
									+ "<td>PT1有压</td>"
									+ "<td id='pt1_you_ya'></td>"
									+ "<td>交流失电告警</td>"
									+ "<td id='jiao_liu_shi_dian'></td>"
									+ "<td>遥控复归</td>"
									+ "<td id='yao_kong_fu_gui'></td>"
									+ "</tr>"
									+ "<tr>"
									+ "<td>PT2有压</td>"
									+ "<td id='pt2_you_ya'></td>"
									+ "<td>手动合闸动作</td>"
									+ "<td id='shou_dong_he_zha'></td>"
									+ "<td>遥控器合闸</td>"
									+ "<td id='yao_kong_he_zha'></td>"
									+ "</tr>"
									+ "<tr>"
									+ "<td>PT1过压告警</td>"
									+ "<td id='pt1_guo_ya'></td>"
									+ "<td>手动分闸动作</td>"
									+ "<td id='shou_dong_fen_zha'></td>"
									+ "<td>遥控器分闸</td>"
									+ "<td id='yao_kong_fen_zha'></td>"
									+ "</tr>"
									+ "</tbody></table>"
									+ '<button data-fnc="close_switch" class="btn btn-primary" data-loading-text="合闸中...">合闸</button>'
									+ ignoreBtn
									+ "</div>"
			win.setContent(content)
			marker.openInfoWindow(win)
			this.eventHandler()
			this.initWinStore()
			monitorSet.hvSwitchStatusSpy(marker.id)
			monitorSet.readCurrentVoltage(marker.id, marker.type)
		},
		// 忽略报警
		ignoreAlarm: function(marker){
			marker.open = false
			winStore.winDOM.find('button[data-fnc=ignore_switch]').remove()
			winStore.cloneWin.find('button[data-fnc=ignore_switch]').remove()
			djMap.View.removeAlarm(marker.id)
		},
		eventHandler: function() {
			var _self = this
			$('#mapInfoWin').on('click', function(e){
				var ev = e || window.event
				var fnc = $(ev.target).data('fnc')
				switch(fnc) {
					case 'close':
						_self.closeWin()
						break
					case 'close_switch':
						_self.security_modal(0)	// 不是1、3为合闸 = =。
						break
					case 'open_switch':
						_self.security_modal(1)	// 1、分闸
						break
					case 'ignore_switch':
						_self.security_modal(3) // 3、忽略
						break
					case 'set_center':
						_self.setCenter()
						break
				}
			})
		},
		removeHandler: function(){
			$('#mapInfoWin').off('click')
		},
		// 版本二
		// 实时更新高压状态
		actualUpdateHvStatus: function(message) {

			var cloneWin = winStore.cloneWin
			if(!cloneWin) { // infoWin 已经被关闭 或 没开启
				return
			}
			var green_or_red = function(sign) {
				if(sign == '00') {
					return 'green_point';
				}
				else if(sign == '01') {
					return 'red_point';
				}
			}

			var data = JSON.parse(message.body)
			if (data == null || data == "") {
				cloneWin.find("#status").text("离线");
			} else {
				cloneWin.find("#guo_liu_yi_duan").attr('class', green_or_red(data.guo_liu_yi_duan));
				cloneWin.find("#guo_liu_er_duan").attr('class', green_or_red(data.guo_liu_er_duan));
				cloneWin.find("#guo_liu_san_duan").attr('class', green_or_red(data.guo_liu_san_duan));

				cloneWin.find("#pt1_you_ya").attr('class', green_or_red(data.pt1_you_ya));
				cloneWin.find("#pt2_you_ya").attr('class', green_or_red(data.pt2_you_ya));
				cloneWin.find("#pt1_guo_ya").attr('class', green_or_red(data.pt1_guo_ya));
				cloneWin.find("#pt2_guo_ya").attr('class', green_or_red(data.pt2_guo_ya));

				cloneWin.find("#shou_dong_he_zha").attr('class', green_or_red(data.shou_dong_he_zha));
				cloneWin.find("#shou_dong_fen_zha").attr('class', green_or_red(data.shou_dong_fen_zha));

				cloneWin.find("#yao_kong_fu_gui").attr('class', green_or_red(data.yao_kong_fu_gui));
				cloneWin.find("#yao_kong_fen_zha").attr('class', green_or_red(data.yao_kong_fen_zha));
				cloneWin.find("#yao_kong_he_zha").attr('class', green_or_red(data.yao_kong_he_zha));

				cloneWin.find("#jiao_liu_shi_dian").attr('class', green_or_red(data.jiao_liu_shi_dian));
				cloneWin.find("#chong_he_zha").attr('class', green_or_red(data.chong_he_zha));
				cloneWin.find("#ling_xu_guo_liu_").attr('class', green_or_red(data.ling_xu_guo_liu_));
				if (data.status == '00') {
					cloneWin.find("#status").text("分");
				} else if (data.status == "01") {
					cloneWin.find("#status").text("合");
				}
			}
			this.updateWinStore('status', cloneWin)
		},
		// 实时更新电流信息
		actualUpdateCur: function(message) {
			var cloneWin = winStore.cloneWin
			if(!cloneWin) {
				return
			}
			var data = JSON.parse(message.body)
			var curacc = dealSwitch.getCuracc(now.type)
			cloneWin.find("#a_phase_current").text(data[0] / curacc)
			cloneWin.find("#b_phase_current").text(data[1] / curacc)
			cloneWin.find("#c_phase_current").text(data[2] / curacc)
			// win.html(cloneWin.html())
			this.updateWinStore('cur', cloneWin)
		},
		// 实时更新电压信息
		actualUpdateVol: function(message) {
			var cloneWin = winStore.cloneWin
			if(!cloneWin) {
				return
			}
			var data = JSON.parse(message.body)
			var volacc = dealSwitch.getVolacc(now.type)
			cloneWin.find("#a_phase_voltage").text(data[0] / volacc)

			cloneWin.find("#b_phase_voltage").text(data[1] / volacc)

			cloneWin.find("#c_phase_voltage").text(data[2] / volacc)
			this.updateWinStore('vol', cloneWin)
		},
		// 更新低压状态
		actualLvStatus: function(status) {
			var winDOM = winStore.winDOM
			if(!winDOM) {
				return
			}
			if (data == null || data == "") {
				winDOM.find("#status").text("离线");
			} else if (data.isOpen) {
				winDOM.find("#status").text("分");
			} else {
				winDOM.find("#status").text("合");
			}
		},
		security_modal: function(type) {
			$('#notice_msg').text('将在 ' + ' ' + ' 秒内执行！')
			$("#security_modal").modal('show');
			var timer;
			$("#secu_confirm_btn").unbind().click(function() {
				if(timer) {
					clearTimeout(timer);
				}
				var wait = 6;
				timer = setInterval(function() {
					if (wait === 0) {
						$.ajax({
							type : "POST",
							url : "security_confirm",
							async : false,
							data : {
								"controlCode" : $("#controlCode").val()
							},
							success: function(res) {
								if(res) {
									$('#notice_msg').text("正在操作，请等待。。。。")
									if(type == 1) {
										dealSwitch.openSwitch(now.id, now.type)
									} else if(type == 3) {
										dealSwitch.ignoreSwitch(now.id, now.marker.node, now.marker)
									} else {
										dealSwitch.closeSwitch(now.id, now.type, now.marker)
									}
									// setTimeout(djMap.Control.refresh.bind(djMap.Control), 3000); // 为什么在3秒后执行？？
								} else {
									alert('安全密码错误!');
									clearTimeout(timer);
									$('#notice_msg').text("将在 " + ' ' + " 秒内执行！");
								}
								$("#controlCode").val('');
							}
						});
						clearTimeout(timer);
					} else {
						wait--;
						$('#notice_msg').text("将在 " + wait + " 秒内执行！");
					}
				}, 1000);
			});
			$('#cancel_control').unbind().click(function() {
				clearInterval(timer);
			});
		},
		alarm_handle_modal: function(marker) {
			// curMarker = marker
			// var node = marker.node ??
			this.setNowData(marker)
			var alarm_modal = $('#alarm_modal')
			alarm_modal.modal('show');
			var now = new Date();
			var month = (now.getMonth() + 1);               
			var day = now.getDate();
			var hour = now.getHours();
			var minute = now.getMinutes();
			var second = now.getSeconds();
			if(month < 10) 
				month = "0" + month;
			if(day < 10) 
				day = "0" + day;
			if(hour < 10)
				hour = "0" + hour;
			if(minute < 10)
				minute = "0" + minute;
			if(second < 10)
				second = "0" + second;
			var today = now.getFullYear() + '-' + month + '-' + day + ' ' 
								+ hour + ':' + minute + ':' + second;
			alarm_modal.find('#handleTime').val(today)

			alarm_modal.find('#alarm_confirm_btn').unbind().click(this.submitAlarmEvent)
		},
		submitAlarmEvent: function() {
			var solvePeople = $('#handleName').val()
			if(!$.trim(solvePeople)) {
				alert('请填写解决人员')
				return
			}
			var solveTime = $('#handleTime').val()
			if(!$.trim(solveTime)) {
				alert('请填写解决时间')
				return
			}
			var switchId = now.id
			$.ajax({
				type: 'POST',
				url: 'update_hitchEvent',
				async: false,
				data: {
					switchId: switchId,
					solveTime: solveTime,
					solvePeople: solvePeople
				},
				success: function (data) {
					if(data.success != 'false' && data.success != false) {
						$('#alarm_modal').modal('hide')
						djMap.View.removeAlarm(now.marker)
					} else {
						alert('操作失败')
					}
				},
				error: function(){
					alert('出错')
				}
			});
		}
	};
}();