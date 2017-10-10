var monitorSet = function() {
	var stompClient
	return {
		getClient: function(){
			return stompClient
		},
		initSock: function() {
			var socket = new SockJS("/portfolio");
			stompClient = Stomp.over(socket);
			stompClient.connect({}, function(frame) {
				stompClient.subscribe("/topic/get_active_switch_status", function(message) {
//					console.log('/topic/get_active_switch_status', JSON.parse(message.body))
					djMap.Control.freshActiveSwitch(JSON.parse(message.body))
				});
				stompClient.subscribe('/user/queue/read_voltage', function(message) {
//					console.log('/user/queue/read_voltage', JSON.parse(message.body))
					infoWindow.actualUpdateVol(message)
				});
				stompClient.subscribe('/user/queue/read_current', function(message) {
//					console.log('/user/queue/read_current', JSON.parse(message.body))
					infoWindow.actualUpdateCur(message)
				});
				stompClient.subscribe('/user/queue/read_hv_status', function(message) {
//					 console.log('/user/queue/read_hv_status', JSON.parse(message.body))
					infoWindow.actualUpdateHvStatus(message)
				});
				stompClient.subscribe('/topic/switch_event', function (message) {
					console.log('/topic/switch_event', message)
					var voice = 'data:audio/mp3;base64,' + message.body
					new Audio(voice).play()
				});
			});
		},
		loadTree: function() {
			$.ajax({
		    type: 'POST',
		    url: 'get_location_switch',
		    async: false,
		    dataType: 'json',
		    
		    success: function (data) {
		    
			      if (data != null) {
			    	  djMap.Model.location({
			    	  	switchId: data.switchId,
			    	  	scale: data.scale
			    	  })
			    	  treeSet.set($.fn.zTree.init($("#treeDemo"), treeSet.setTree(data.type)));
			    	  document.getElementById("zTree_node_type").options[data.type].selected = true;
			      } else {
			    	 
			    	  treeSet.set($.fn.zTree.init($("#treeDemo"), treeSet.setTree(1)));
			    	  document.getElementById("zTree_node_type").options[1].selected = true;
			      }

		    },
		    error: function() {
		    	$.fn.zTree.init($("#treeDemo"), treeSet.setTree(1));
		    	  document.getElementById("zTree_node_type").options[1].selected = true;
		    }
			});
			// 给初次加载页面时与设备树相关的DOM标签添加事件，本页面只执行一次
			$("#searchNode").click(function() {
				treeSet.zTreeSearch($("#search_node_key").val());
			})
			$('#search_node_key').keydown(function (e) {
				e.stopPropagation()
				if(e.keyCode == 13) {
					treeSet.zTreeSearch($("#search_node_key").val())
				}
			})
			$('#back').click(function() {
				treeSet.zTreeSearchRes(null, true)
			})
			// 切换ZTree显示的开关种类
			$("#zTree_node_type").change(function() {
				$.fn.zTree.getZTreeObj("treeDemo").destroy();
				$.fn.zTree.init($("#treeDemo"), treeSet.setTree(this.value));
			})
		},
		getSwitch: function() {
			$.ajax({
		    type: 'POST',
		    url: 'get_active_switch_ignore_change',
		    async: false,
		    dataType: 'json'
			});
		},
		hvSwitchStatusSpy: function(id) {
			$.ajax({
				type: 'POST',
				url: 'read_hvswitch_status',
				async: false,
				data: {
					id: id
				}
			});
		},
		readCurrentVoltage(id, type) {
			this.readAllPhaseParam(id, type, 'vol');
			this.readAllPhaseParam(id, type, 'cur');
		},
		readAllPhaseParam: function(id, type, kind) {
			if(kind == 'vol') {
				var url = 'read_voltage';
			}
			else {
				var url = 'read_current';
			}
			$.ajax({
				type: 'POST',
				url: url,
				async: false,
				data: {
					switchId: id,
					type: type
				},
				success: function(res) {
					console.log('success in readAllPhase', kind);
				}
			});
		},
		readlvSwitchStatus: function(id) {
			$.ajax({
				type: 'POST',
				url: 'read_lvswitch_status',
				async: false,
				data: {
					id: id
				},
				success: function(res) {
					infoWindow.actualLvStatus(res)
				}
			});
		},
		stopRead: function() {
			$.ajax({
				method: 'POST',
				url: '/dongjun/stop_read_param'
			});
		},
		init: function() {
			this.loadTree();
			this.getSwitch();
			this.initSock();
		}
	};
}(); 

monitorSet.init()
// 在页面卸载前必须停止
window.onbeforeunload = function() {
	monitorSet.stopRead()
}