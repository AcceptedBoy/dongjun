var djMap = {
	Model: {},
	View: {},
	Control: {},
	getMap: function() {
		var m = this.Model.getMap()
		if(m) {
			return m
		} else {
			console.warn('not found any BMap')
		}
	}
}

djMap.Model = function(){
	var map					// 当前BMap.Map的实例对象

	var urls = {
		all_low: 'get_all_low_voltage_switch', // get_all_low_voltage_switch
		all_high: 'get_all_high_voltage_switch', // 'get_all_high_voltage_switch'
		all_ctrl: 'get_all_control_measure_switch', // 'get_all_control_measure_switch'
		active: 'get_active_switch_status', // get_active_switch_status
		set_center_high: '/dongjun/edit_location',
		alarmVoice: 'get_voice_of_event', // get_voice_of_event
	}

	var icons = {		// 图标s
		warning: '../../ico/tuDing.gif',													// 报警图标，为动图
		close_high: '../../ico/highvoltage-close.jpg',						// 高 合闸图标
		open_high: '../../ico/highvoltage-open.jpg',							// 高 开闸图标
		close_low: '../../ico/lowvoltage-close.jpg',							// 低 合闸图标
		open_low: '../../ico/lowvoltage-open.jpg',								// 低 开闸图标
		close_switch: '../../ico/voltage-close.jpg',							// ???
		low_voltage: '../../ico/voltage-outLine_low.jpg',					// 低压开关的图标
		high_voltage: '../../ico/voltage-outLine_high.jpg',				// 高压开关的图标
		control_measure: '../../ico/control_measure_switch.jpg',	// 低压开关的图标
		old_icon: null																											// BMap.icon 对象，储存上一个图标
	}

	// polyline的颜色，待用
	var lineColor = {
		all: ['#2B0BB4', '#389510', '#F2611D', '#0E8CB3', '#A34545', '#CA0CBE', '#DFE21A'],
		used: []
	}

	var history = {
		activeMarkers: {},
		alarmMarkers: [],
		polyline: {}
	}

	var location = {
		switchId: -1,
		scale: 12,
		lng: 0,
		lat: 0
	}
	// var oldMarkerList = []
	// var PreHandlerNode
	return {
		setMap: function(_map) {
			map = _map
		},
		getMap: function() {
			if(map) {
				return map
			} else {
				return false
			}
		},
		getIcon: function(iconName) {
			return icons[iconName]
		},
		setOldIcon: function(icon) {
			icons.old_icon = icon
		},
		lineColor: function(color) {
			if(color) {
				var pos = lineColor.used.indexOf(color)
				lineColor.all.push(lineColor.used.splice(pos, 1))
				return
			} else {
				var color = lineColor.all.shift()
				lineColor.used.push(color)
				return color
			}
		},
		location: function(nameSet) {
			// 获取
			if(!nameSet) return location
			if((typeof nameSet) == 'string') {
				return location[nameSet]
			}
			if(nameSet instanceof Array) {
				var res = {}
				nameSet.forEach(function(item) {
					res[item] = location[item]
				})
				return res
			}
			// 设置
			for(var k in nameSet) {
				location[k] = nameSet[k]
			}
		},
		getSwitchNodes: function(type) {
			var nodes;
			var Surl = '';
			var Stype = 0;
			switch(type) {
				case 'low':
					Surl = urls.all_low;	//
					Stype = 0;
					break;
				case 'high':
					Surl = urls.all_high;
					Stype = 1;
					break;
				case 'measure':
					Surl = urls.all_ctrl;
					Stype = 2;
					break;
			}
			if(Surl) {
				$.ajax({
					type: 'GET',
					url: Surl,
					async: false,
					data: {},
					success: function(res) {
						nodes = res;
						for(var i = 0; i<nodes.length; i++) {
							nodes[i].type = Stype
							nodes[i].status = '-1'
						}
					}
				});
			}
			return nodes
		},
		getActiveSwitch: function() {
			var nodes = []
			$.ajax({
				type: 'GET',
				url: urls.active,
				async: false,
				success: function(data) {
					nodes = data
				}
			})
			return nodes
		},
		history: function(name, item) {
			if(arguments.length == 1) {
				if(name == 'ALL') {
					return history
				} else {
					return history[name]
				}
			} else {
				if(name == 'polyline') {
					// 是否在此数量判断？
					history.polyline[item.id] = item
				} else if(name == 'activeMarkers') {
					try{
						for(let k in item) {
							history.activeMarkers[k] = item[k]
						}
					} catch(e) {
						for(var k in item) {
							history.activeMarkers[k] = item[k]
						}
					}
				} else {
					history.alarmMarkers = history.alarmMarkers.concat(item)
				}
			}
		},
		delHistory: function(name, item) {
			switch(name) {
				case 'polyline':
					if(item) {
						var pL = history.polyline[item]
						delete history.polyline[item]
						return pL
					} else {
						var pLs = []
						for(var k in history.polyline) {
							pLs.push(history.polyline[k])
						}
						history.polyline = {}
						return pLs
					}
				case 'activeMarkers':
					var delItem = []
				  if(item instanceof Array) {
				  	item.forEach(function(markerId) {
				  		delItem.push(history.activeMarkers[markerId])
				  		delete history.activeMarkers[markerId]
				  	})
				  	return delItem
				  } else {
				  	delItem.push(history.activeMarkers[item])
				  	delete history.activeMarkers[item]
				  	return delItem[0]
				  }
				case 'alarmMarkers':
					var delMarkers = []
					var alarmList = history.alarmMarkers
					if(alarmList.length > 1) {
						if(item) {
							var pos = -1
							var markerId = item.id ? item.id : item
							for (var i = alarmList.length - 1; i >= 0; i--) {
								if(alarmList[i].id == markerId){
									pos = i
									break
								}
							}
							return history.alarmMarkers.splice(pos, 1)
						}
					}
					delMarkers = history.alarmMarkers
					history.alarmMarkers = []
					return delMarkers
			}
		},
		setCenter: function(switchId, type) {
			var _self = this
			$.ajax({
				type: 'POST',
				url: urls.set_center_high,
				data: {
					switchId: switchId,
					type: type,
					scale: map.getZoom()
				},
				success: function(res) {
					if(res) {
						var zTree = $.fn.zTree.getZTreeObj('treeDemo')
						var node = zTree.getNodesByParamFuzzy('id', switchId)[0]
						_self.location({
							lng: node.longitude,
							lat: node.latitude
						})
						alert('设置中心点成功')	
					}
				}
			})
		},
		getVoice: function(name) {
			var _self = this
			var result = false
			// 使用循环请求原因： 这个API的毛病，有可能请求一次不会成功，有可能请求很多也不会成功
			// 所以，限制请求5次，若在5次内请求不到音源，则不做报警音频
			for(var count = 5; count > 0; count--) {
				$.ajax({
					type : "GET",
					url: urls.alarmVoice,
					async : false,
					data : {
						"name" : name
					},
					success : function(data) {
						if(data.errNum == 0) {
							result = data.retData
						}
					},
					error: function() {
						result = 'error'
					}
				})
				if(result) {
					break
				}
			}

			return result == 'error' ? false : result
		}
	}
}()

djMap.View = function(_map) {
	var model = _map.Model
	var map = null
	return {
		/**
		 * 初始化地图
		 * @param  {number} lng 经度
		 * @param  {number} lat 纬度
		 * @return {void}
		 */
		initMap: function(lng, lat) {
			map = model.getMap()
			map.centerAndZoom(new BMap.Point(lng, lat), 12);
			map.enableScrollWheelZoom();                				 	//启用滚轮放大缩小

			var mapType1 = new BMap.MapTypeControl({
				mapTypes: [ BMAP_NORMAL_MAP, BMAP_HYBRID_MAP ]
			});
			var mapType2 = new BMap.MapTypeControl({
				anchor: BMAP_ANCHOR_TOP_LEFT
			});

			var overView = new BMap.OverviewMapControl();
			var overViewOpen = new BMap.OverviewMapControl({
				isOpen: true,
				anchor: BMAP_ANCHOR_BOTTOM_RIGHT
			});

			map.addControl(mapType1); 			// 2D图，卫星图
			map.addControl(mapType2); 			// 左上角，默认地图控件
			map.addControl(overView); 			// 添加默认缩略地图控件
			map.addControl(overViewOpen); 	// 右下角，打开
		},

		/**
		 * 绘制线条
		 * @param  {Array} data 对象数组(树节点)
		 *                      [{longitude: number, latitude: number}, ...]
		 * @return {void}
		 */
		drawLine: function(supId, data, locatePoint) {
			map = map || model.getMap()
			var points = []
			data.forEach(function(item) {
				points.push(new BMap.Point(item.longitude, item.latitude))
			})
			var polyline = new BMap.Polyline(points, {
				strokeWeight: 6,
				strokeOpacity: 0.7
			})
			polyline.setStrokeColor(model.lineColor())
			polyline.id = supId
			map.addOverlay(polyline)
			model.history('polyline', polyline)
			this.panTo(locatePoint)
		},
		removeLine: function(polyline) {
			model.lineColor(polyline.getStrokeColor())
			map.removeOverlay(polyline)
		},
		/**
		 * 视图转移
		 * @param  {object} o 转至地点及标注信息
		 *                    {
		 *                    	lng/longitude : number,
		 *                    	lat/latitude : number,
		 *                    	marker: {            // optional
		 *                    	  title: string      // 鼠标移入显示
		 *                    	  label: string      // 显示在标注旁边
		 *                    	  animation: boolean // default: true
		 *                    	  icon: {
		 *                    		 name: Model.icons,
		 *                    		 height: number,
		 *                    		 width: number
		 *                    	  },
		 *                    	}
		 *                    }
		 * @return {object}   point对象
		 */
		panTo: function(o, marker) {
			if(map.getZoom() < 14) {
				map.setZoom(15)
			}
			var point = new BMap.Point(o.lng||o.longitude, o.lat||o.latitude) // 位置点
			map.panTo(point)																									// 视图转移到相应的点
			if(marker || true) {
				this.addMarker(point, o.marker)																		// 添加标注
			}
			return point
		},
		showLineLabel: function(supNodeName, locateNodeName) {
			$('#show_line').text(supNodeName + '-' + locateNodeName)
		},
		/**
		 * 添加标注
		 * @param {Object} point   point对象
		 * @param {object} setting marker的简要配置信息
		 *                         {
		 *                    	    title: string      // 鼠标移入显示
		 *                    	    label: string      // 显示在标注旁边
		 *                    	    icon: {						// 暂且没用
		 *                    		   	name: Model.icons,
		 *                    		   	height: number,
		 *                    		    width: number
		 *                    	     },
		 *                    	   }
		 */
		addMarker: function(point, setting) {
			var marker = null
			// var opts = {}
			// if(setting) {
			// 	if(setting.icon) {					// 自定义图标
			// 		var iconObj = setting.icon
			// 		var size = iconObj.height && iconObj.width ? new BMap.Size(iconObj.height, iconObj.width) : new BMap.Size(30, 30)
			// 		var icon = new BMap.Icon(model.getIcon(iconObj.name), size)	// 根据iconName去model取得相应图标
			// 		opts.icon = icon
			// 	}
			// 	if(setting.title) {
			// 		opts.title = setting.title
			// 	}
			// }

			// marker = !!opts ? new BMap.Marker(point, opts) : new BMap.Marker(point)
			marker = new BMap.Marker(point)
			marker.setZIndex(9)
			map.addOverlay(marker) 	// 添加标注

			// 设置动画，并设置消失
			marker.setAnimation(BMAP_ANIMATION_BOUNCE)
			var timer = setTimeout(function() {
				marker.setAnimation(null)
				map.removeOverlay(marker);
				clearTimeout(timer)
			}, 2500)

			if(setting && setting.label) {	// 文字标注
				var label = new BMap.Label(setting.label, {offset: new BMap.Size(20, -10)})
				marker.setLabel(label)
			}
			return marker
		},
		addMarkerByNode: function(nodeOrList) {
			var _self = this
			var nodeList = []
			var markerList = []
			if(nodeOrList instanceof Array) {
				nodeList = nodeOrList
			} else {
				nodeList.push(nodeOrList)
			}
			nodeList.forEach(function(node) {
				// make a marker without icon
				var pt = new BMap.Point(node.longitude, node.latitude)
				var marker = new BMap.Marker(pt)
				marker.id = node.id
				marker.type = node.type
				marker.name = node.name
				if(node.status) {
					marker.status = node.status
					if(node.status == '00' || node.status == '02') {
						marker.node = node
						marker.open = node.open
					}
				}

				// set icon to marker
				_self.setMarkerIcon(marker)
				markerList.push(marker)
				map.addOverlay(marker)
			})
			return markerList
		},
		setMarkerIcon: function(marker) {
				var iconName = ''
				var icon = null
				if(marker.status == '02') {					// 报警
					iconName = model.getIcon('warning')
					icon = new BMap.Icon(iconName, new BMap.Size(80, 80))
					marker.setZIndex(-1)
				} else {
					switch(marker.type) {
						case 0:
							iconName = 'low'
							break
						case 1:
							iconName = 'high'
							break
					}
					if(iconName != '') {
						iconName = (marker.status == '00') ? model.getIcon('open_'+iconName) :
											(marker.status == '01') ? model.getIcon('close_'+iconName) : model.getIcon(iconName+'_voltage')
					} else {
						iconName = model.getIcon('control_measure')
					}
					icon = new BMap.Icon(iconName, new BMap.Size(20, 20))
					marker.setZIndex(9)
				}

				marker.setIcon(icon)
				return marker
		},
		removeMarker: function(marker) {
			map.removeOverlay(marker)
		},
		alarm: function(node) {
			this.playVoice(node.name)
			treeSet.update(node, 2)
			node.status = '02'
			model.history('alarmMarkers', this.addMarkerByNode(node)[0])
		},
		playVoice: function(name){
			var voiceData = model.getVoice(name)
			if(!voiceData) return
			// var audioUrl = 'data:audio/mp3;base64,' + data
			var audioUrl = voiceData
			new Audio(audioUrl).play()
		},
		removeAlarm: function(markerOrId){
			// model.delHistory('alarmMarkers', marker)
			map.removeOverlay(model.delHistory('alarmMarkers', markerOrId)[0])
		},
		clearAlarm: function(){
			var _self = this
			var alarmList = model.delHistory('alarmMarkers')
			if(alarmList.length == 0){
				return
			}
			alarmList.forEach(function(item) {
				treeSet.update(item.node)
				_self.removeMarker(item)
			})
		},
		backCenter: function() {
			var center = model.location(['lng', 'lat', 'scale'])
			var pt = new BMap.Point(center.lng, center.lat);
			map.centerAndZoom(pt, center.scale);
			this.addMarker(pt, {label: '中心点'})
		}
	}
}(djMap)

djMap.Control = function(_map) {
	var view = _map.View
	var model = _map.Model
	_map = null
	return {
		init: function(initData) {
			var map = new BMap.Map('baidu_map', {enableMapClick: false})
			model.setMap(map)
			view.initMap(108.386287, 22.82277)
/*			model.location({
				lng: initData.longitude,
				lat: initData.latitude
			})*/
			this.initAllSwitch()
			this.startListen(map)
		},
		treeNodeClick: function(action, supNodeId, siblingNodes, curNode, supNodeName) {
			/*if(action == 'add') {
				// var oldLines = model.delHistory('polyline')
				// if(oldLines.length != 0) {
				// 	view.removeLine(oldLines[0])
				// }
				curNode = curNode ? curNode : siblingNodes[0]
				view.showLineLabel(supNodeName, curNode.name)
				view.drawLine(supNodeId, siblingNodes, curNode)
			} else {
				view.removeLine(model.delHistory('polyline', supNodeId))
			}*/
			if(action == 'add') {
				curNode = curNode ? curNode : siblingNodes[0]
				view.showLineLabel(supNodeName, curNode.name)
				view.drawLine(supNodeId, siblingNodes, curNode)
			} else if(action == 'del'){
				view.removeLine(model.delHistory('polyline', supNodeId))
			} else {
				view.panTo(arguments[1])	// 第二个参数（supNodeId）= =
			}
		},
		removeActiveMarker: function(markerId) {
			view.removeMarker(model.delHistory('activeMarkers', markerId))
		},
		initAllSwitch: function() {
			var allSwitch = []
			allSwitch = allSwitch.concat(model.getSwitchNodes('low'), model.getSwitchNodes('high'), model.getSwitchNodes('measure'))
			view.addMarkerByNode(allSwitch)
		},
		refresh: function() {
			var nodeData = model.getActiveSwitch()
			this.freshActiveSwitch(nodeData)
		},
		freshActiveSwitch: function(nodeData) {
			var markers = model.history('activeMarkers')
			var newMarker = {}
			var alarmNode = null
			// var newNodeList = []
			var oldIds = Object.keys(markers)
			var zTree = $.fn.zTree.getZTreeObj('treeDemo')

			view.clearAlarm()	// 清除所有报警

			if(oldIds.length != 0){	// 原本有活动开关情况
				nodeData.forEach(function(item) {
					var node = zTree.getNodesByParamFuzzy('id', item.id)[0]
					// console.log(node)
					if(node) {
						var pos = oldIds.indexOf(item.id)
						if(pos == -1) {	// add
							// console.log('fresh—add')
							node.status = item.status
							node.open = item.open
							newMarker[item.id] = view.addMarkerByNode(node)[0]
							// newNodeList.push(node)
						} else {
							// console.log('fresh,is update?')
							// console.log(markers[item.id].status, item.status)
							if(markers[item.id].status != item.status) {	// update old marker
								// console.log('fresh-update')
								markers[item.id].status = item.status
								markers[item.id].open = item.open
								view.setMarkerIcon(markers[item.id])
							}
							oldIds.splice(pos, 1)												// 若是更新则弹出旧的ID，到最后则剩下 离线
						}
						if(item.status == '00' && item.open) {				// status为00， open为true时报警
							node.open = item.open
							alarmNode = node
							view.alarm(node)
						}
					}
				})
				// console.log('after', oldIds)
				if(oldIds.length > 0){	// 离线
					oldIds.forEach(function(item){
						var delMarker = model.delHistory('activeMarkers', item)
						view.removeMarker(delMarker)
					})
				}

			} else {	// 原本无活动开关情况
				nodeData.forEach(function(item) {
					var node = zTree.getNodesByParamFuzzy('id', item.id)[0]
					if(!!node) {
						node.status = item.status
						node.open = item.open
						newMarker[item.id] = view.addMarkerByNode(node)[0]
						// newNodeList.push(node)
						if(item.status == '00' && item.open) {
							alarmNode = node
							view.alarm(node)
						}
					}
				})
			}

			model.history('activeMarkers', newMarker)
			if(alarmNode) {	// 定位到最后一个报警地点
				view.panTo({
					lng: alarmNode.longitude,
					lat: alarmNode.latitude
				})
			}
			// view.addMarkerByNode(newNodeList)
		},
		startListen: function(map) {
			// 事件委托， 监听图标
			map.addEventListener('click', function(e) {
				if(e.overlay instanceof BMap.Marker) {
					var marker = e.overlay
					var type = marker.type
					// console.log(marker)
					if(type == 0) {	// 低压
						infoWindow.click_low_voltage_switch(marker)
					} else if(type == 1){								// 高压
						switch(marker.status) {
							case '-1':
								if(marker.type == 1){
									infoWindow.click_high_voltage_switch_out(marker)
								}
								break
							case '00': 	// 分闸
								if(marker.type == 1){
									infoWindow.click_high_voltage_switch_open(marker)
								}
								break
							case '01': 	// 合闸
								if(marker.type == 1){
									infoWindow.click_high_voltage_switch_close(marker)
								}
								break
							case '02': 	// 报警
						  	infoWindow.alarm_handle_modal(marker); 	// pop up a handle window
								break
						}
					}
				}
			})
			// 返回中心点
			$('#Center').click(function () {
				$('#show_line').text('您的中心点');
				view.backCenter()
			});
		}
	}
}(djMap)