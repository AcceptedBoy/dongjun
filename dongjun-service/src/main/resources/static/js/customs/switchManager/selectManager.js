var selectManager = function() {
	var companyList = [];						// 公司list
	var substationList = [];				// 配电站
	var lineList = []
	var urls = {};
	var node = {};
	var nowCmpId = -1;					// 当前组别Id
	var nowSubId = -1;					// 当前公司的id
	var nowLineId = -1;
	var completeFnc = function () {
		console.log('没有完成回调函数')
	}
	return {
		setOption: function(o) {
			if(o.url) {
				var url = o.url
				urls = {
					company: url.company,
					substation: url.substation,
					line: url.line
				}
			}
			if(o.node) {
				var nodes = o.node;
				node = {
					company: nodes.company,
					substation: nodes.substation,
					line: nodes.line
				};
			}
			completeFnc = o.completeFnc
			this.autoListen()
			return this
		},
		getOption: function() {
			return {
				url: urls,
				node: node,
				completeFnc: completeFnc
			}
		},
		loadCompany: function() {
			console.log('开始请求 company')
			var _self = this;
			$.ajax({
				url: urls.company,
				type: 'POST',
				async: false,
				success: function(data) {
					console.log('company data')
					// console.log(data)
					if(data != null){
						data = typeof data === 'string' ? JSON.parse(data).text : data.text
						// default_id = data[0].id;
						nowCmpId = data[0].id
						var options = '';
						companyList = [];
						for (var i = 0; i < data.length; i++) {
							companyList.push(data[i]);
							options += '<option value="' + data[i].id + '">' + data[i].name
									+ '</option>';
						}
						$(node.company).html(options);
						_self.loadSubstation(nowCmpId);
						// localStorage.setItem('defaultId', default_id);
					}
				}
			})
		},
		loadSubstation: function(companyId, fn) {
			console.log('开始请求 substation')
			var _self = this;
			$.ajax({
				url: urls.substation,
				type: 'POST',
				async: false,
				data: {
					'companyId': companyId
				},
				success: function(data) {
					if(data) {
						data = typeof data === 'string' ? JSON.parse(data).text : data.text
						var opts = ''
						if(data.length == 0) {
							opts = '<option>当前没有数据</option>'
							$(node.substation).html(opts)
							_self.loadLines()
							return
						}
						substationList = []
						nowSubId = data[0].id
						data.forEach(function(item, index) {
							substationList.push(item)
							opts += '<option value="' + item.id + '">' + item.name + '</option>'
						})
						$(node.substation).html(opts)
						_self.loadLines(nowSubId)
						if (fn) {
							fn(data[0].id)
						}
					}
				}
			})
		},
		loadLines: function (substationId, fn) {
			console.log('开始请求line')
			if (!substationId) {
				opts = '<option>当前没有数据</option>'
				$(node.line).html(opts)
				completeFnc(-1)
				return
			}
			var _self = this;
			$.ajax({
				url: urls.line,
				type: 'POST',
				async: false,
				data: {
					'substationId': substationId
				},
				success: function(data) {
					if(data) {
						data = typeof data === 'string' ? JSON.parse(data).text : data.text
						var opts = ''
						if(data.length == 0) {
							opts = '<option>当前没有数据</option>'
							$(node.line).html(opts)
							completeFnc(-1)
							return
						}
						lineList = []
						nowLineId = data[0].id
						data.forEach(function(item, index) {
							lineList.push(item)
							opts += '<option value="' + item.id + '">' + item.name + '</option>'
						})
						$(node.line).html(opts)
						completeFnc(nowLineId)
						if(fn) {
							fn(data[0].id)
						}
					}
				}
			})
		},
		companyChange: function(eOrId, fn) {
			var sId
			if(eOrId) {
				if(Object.prototype.toString.apply(eOrId) != '[object object]') {
					sId = eOrId
				} else {
					var ev = e || window.event
					sId = ev.target.value
				}
			} else {
				sId = $(node.company).val()
			}
			nowCmpId = sId
			this.loadSubstation(sId)
			if(fn) {
				fn(sId)
			}
		},
		substationChange: function(eOrId, fn) {
			var substationId
			if(eOrId) {
				if(Object.prototype.toString.apply(eOrId) != '[object object]') {
					substationId = eOrId
				} else {
					var ev = e || window.event
					substationId = ev.target.value
				}
			} else {
				substationId = $(node.substation).val()
			}
			
			nowSubId = substationId
			this.loadLines(substationId)
			// completeFnc(substationId)

			if(fn) {
				fn(substationId)
			}
		},
		lineChange: function(eOrId, fn) {
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
			nowlineId = lineId
			// this.loadLines(lineId)
			completeFnc(lineId)
			if(fn) {
				fn(lineId)
			}
		},
		autoListen: function() {
			var _self = this
			$(node.company).change(function() {
				_self.companyChange(this.value)
			})

			$(node.substation).change(function() {
				_self.substationChange(this.value)
			})

			$(node.line).change(function() {
				_self.lineChange(this.value)
			})
		},
		getGroup: function() {
			if(nowCmpId != -1) {
				return nowCmpId
			} else {
				return $(node.group)[0].value
			}
		},
		getSubstation: function() {
			if(nowSubId != -1){
				return nowSubId
			} else {
				return $(node.substation)[0].value
			}
		},
		getLine: function() {
			if(nowLineId != -1){
				return nowLineId
			} else {
				return $(node.line)[0].value
			}
		}
	}
}();

