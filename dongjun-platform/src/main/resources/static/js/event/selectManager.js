var selectManager = function() {
	var substationList = [];	// 单位（组别）list
	var lineList = [];				// 配电室
	var switchList = [];			// 回路
	var urls = {};
	var node = {};
	var completeFnc = null;
	return {
		setOption: function(o) {
			var url = o.url;
			urls = {
				substation: url.substation ? url.substation : '/dongjun/group/gruop_list',
				line: url.line ? url.line : '/dongjun/platform_group/platform_group_list_by_group_id',
				switch: url.switch ? url.switch : 'high_voltage_switch_list_by_line_id',
				lineChange: url.lineChange ? url.lineChange : url.switch
			};
			var nodes = o.node;
			node = {
				substation: nodes.substation ? nodes.substation : '.substations',
				line: nodes.line ? nodes.line : '.lines',
				switch: nodes.switch ? nodes.switch : '#switchs',
				searchType: nodes.searchType ? nodes.searchType : '#searchType',
				searchContent: nodes.searchContent ? nodes.searchContent : '#searchContent',
				searchList: nodes.searchList ? nodes.searchList : '#searchlist'
			};
			completeFnc = o.completeFnc
		},
		getOption: function() {
			return {
				url: urls,
				node: node,
				completeFnc: completeFnc
			}
		},
		loadSubstations: function(loadLines) {
			loadLines = loadLines || true;
			var _self = this;
			$.ajax({
				url: urls.substation,
				type: 'POST',
				async: false,
				success: function(data) {
					if(data != null){
						//data = typeof data == 'string' ? JSON.parse(data): data; 
						data = data.data
						default_id = data[0].id;
						var options = '';
						substationList = [];
						for (var i = 0; i < data.length; i++) {
							substationList.push(data[i]);
							options += '<option value="' + data[i].id + '">' + data[i].name
									+ '</option>';
						}
						_self.loadLines(default_id, loadLines);
						$(node.substation).html(options);
						$(node.searchList).html(options);	// 初始化searchlist
						localStorage.setItem('defaultId', default_id);
					}
				}
			})
		},
		loadLines: function(substationId, loadSwitch) {
			loadSwitch = loadSwitch || true;
			var _self = this;
			$.ajax({
				url: urls.line,
				type: 'POST',
				async: false,
				data: {
					'groupId': substationId
				},
				success: function(data) {
					if(data) {
						data = typeof data == 'string' ? JSON.parse(data): data; 
						data = data.data;
						var opts = '';
						if(data.length == 0) {
							opts += '<option> 该组暂无相关数据 </option>'
							$(node.line).html(opts)
							lineList = []
							_self.loadSwitch(-1)
							return
						}
						lineList = [];
						data.forEach(function(item, index) {
							lineList.push(item);
							opts += '<option value="' + item.id + '">' + item.name + '</option>';
						})
						$(node.line).html(opts);
						_self.loadSwitch(data[0].id, loadSwitch);
					}
				}
			})
		},
		loadSwitch: function(lineId) {
			if(lineId == -1) {
				$(node.switch).html('<option> 该组暂无相关数据 </option>')
				completeFnc(-1)
				return
			}
			$.ajax({
				type: 'POST',
				url: urls.switch,
				data: {
					'platformId': lineId
				},
				async: false,
				success: function(data) {
					if(data) {
						data = typeof data == 'string' ? JSON.parse(data): data; 
						data = data.data;
						if(data.length == 0) {
							$(node.switch).html('<option> 该组暂无相关数据 </option>')
							switchList = []
							completeFnc(-1)
							return
						}
						var opts = '';
						switchList = [];
						data.forEach(function(item, index) {
							switchList.push(item);
							opts += '<option value="' + item.id + '">' + item.name + '</option>';
						})
						$(node.switch).html(opts);
					}
				}
			})
		},
		loadSwitchListWithLineId: function(lineId) {
			$(node.searchType).val('组别');
			var _self = this;
			$.ajax({
				type: 'POST',
				url: urls.lineChange,
				async: false,
				data: {
					lineId: lineId
				},
				success: function(data) {
					if(typeof data == 'string'){
						data = JSON.parse(data).data;
					} else {
						data = data.data;
					}
					var opts = '';
					switchList = [];
					data.forEach(function(item) {
						switchList.push(item);
						opts += '<option value="'+item.id+'">'+item.name+'</option>';
					})
					$(node.switch).html(opts);
				}
			})
		},
		loadSearchList: function(type) {
			var list, opts;
			switch(type) {
				case 'substation': list = substationList; break;
				case 'line': list = lineList; break;
				case 'switch': list = switchList; break;
			}
			list.forEach(function(item) {
				opts += '<option value="' + item.id + '">'+ item.name +'</option>';
			})
			$(node.searchList).html(opts);
		},
		substationChange: function(eOrId) {
			var sId
			if(eOrId) {
				if(Object.prototype.toString.apply(eOrId) != '[object object]') {
					sId = eOrId
				} else {
					var ev = e || window.event
					sId = ev.target.value
				}
			} else {
				sId = $(node.substation).val()
			}
			this.loadLines(sId)
			$(node.searchType).val('组别')
			if(switchList.length != 0) {
				completeFnc(switchList[0].id)
			}
	/*		var sId
			if(substation) {
				sId = substationId;
			} else if(e) {
				var ev = e || window.event
				sId = ev.target.value
			} else {
				sId = $(node.substation).val()
			}
			this.loadLines(sId)
			$(node.searchType).val('组别')*/
		},
		linesChange: function(eOrId) {
			// console.log(`linesChange param: ${e.target.value}`)
			// var ev = e || window.event;
			var lineId
			if(eOrId) {
				if(Object.prototype.toString.apply(eOrId) != '[object object]') {
					lineId = eOrId
				} else {
					var ev = e || window.event
					lineId = ev.target.value
				}
			} else {
				lineId = $(node.line).val()
			}
			// this.loadSwitchListWithLineId(lineId) // 更新switch
			this.loadSwitch(lineId)
			if(switchList.length != 0) {
				completeFnc(switchList[0].id)
			}
		},
		switchsChange: function(eOrId) {
			// console.log(`switchsChange param: ${e.target.value}`)
			// console.log('switch changer!!')
			var switchId
			if(eOrId) {
				if(Object.prototype.toString.apply(eOrId) != '[object object]') {
					switchId = eOrId
				} else {
					var ev = e || window.event
					switchId = ev.target.value
				}
			} else {
				switchId = $(node.switch).val()
			}
			// var switchId = $(node.switch).val()
			completeFnc(switchId)
		},
		searchTypeChange: function(e) {
			var type = $(node.searchType).val();
			switch(type) {
				case '组别': this.loadSearchList('substation'); break;
				case '公司' : this.loadSearchList('line'); break;
				case '开关' : this.loadSearchList('switch'); break;
			}
			$(node.searchContent).val('');
		},
		search: function(e) {
			var contentNode = $(node.searchContent)
			var content = contentNode.val()
			if(!$.trim(content)) {
				alert('请输入搜索内容')
				contentNode.focus()
				return
			}
			var type = $(node.searchType).val()
			switch (type) {
				case '组别': this.substationSearch(content); break;
				case '公司': this.lineSearch(content); break;
				case '开关': this.switchSearch(content); break;				
			}
			contentNode.val('')
		},
		searchByKey: function(e) {
			var ev = e || window.event
			if(ev.keyCode == 13) {
				this.search()
			}
		},
		substationSearch: function(content) {
			if(this.searchOnList(content, 1)){
				alert('找不到此组别')
				return
			}
			$(node.substation).val(content)
			this.loadLines(content)
			completeFnc(switchList[0].id)
			$(node.searchType).val('公司')
		},
		lineSearch(content) {
			if(this.searchOnList(content, 2)){
				alert('当前组别找不到此公司')
				return
			}
			$(node.line).val(content)
			this.loadSwitch(content)
			completeFnc(switchList[0].id)			
		},
		switchSearch(content) {
			if(this.searchOnList(content, 3)){
				alert('当前公司找不到此开关')
				return
			}
			$(node.switch).val(content)
			completeFnc(content)
			$(node.searchType).val('开关')
		},
		searchOnList: function(content, type) {
			var list = type == 1 ? substationList : type == 2 ? lineList : switchList
			console.log(list)
			return true
		},
		autoListen: function() {
			var _self = this
			$(node.substation).change(function() {
				_self.substationChange(this.value);				
			})

			$(node.line).change(function() {
				_self.linesChange(this.value);
			})

			$(node.switch).change(function() {
				_self.switchsChange(this.value)
			})
		},
		getList: function() {
			return {
				substation: substationList,
				line: lineList,
				switch: switchList
			}
		}
	}
}();

