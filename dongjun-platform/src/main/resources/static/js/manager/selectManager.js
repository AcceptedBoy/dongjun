var selectManager = function() {
	var groupList = [];					// 单位（变电站）list
	var companyList = [];				// 配电室
	var urls = {};
	var node = {};
	var completeFnc = null;
	var nowComId = -1;					// 当前公司的id
	var nowGrpId = -1;					// 当前组别Id
	return {
		setOption: function(o) {
			if(o.url) {
				var url = o.url;
				urls = {
					group: url.group ? url.group : '/dongjun/group/gruop_list',
					company: url.company ? url.company : '/dongjun/platform_group/platform_group_list_by_group_id',
				};
				console.log(urls)
			}
			if(o.node) {
				var nodes = o.node;
				node = {
					group: nodes.group ? nodes.group : '.groups',
					company: nodes.company ? nodes.company : '.companys',
				};
			}
			completeFnc = o.completeFnc
		},
		getOption: function() {
			return {
				url: urls,
				node: node,
				completeFnc: completeFnc
			}
		},
		loadGroups: function() {
			var _self = this;
			$.ajax({
				url: urls.group,
				type: 'POST',
				async: false,
				success: function(data) {
					console.log('groups data')
					console.log(data)
					if(data != null){
						data = data.data;
						default_id = data[0].id;
						var options = '';
						groupList = [];
						for (var i = 0; i < data.length; i++) {
							groupList.push(data[i]);
							options += '<option value="' + data[i].id + '">' + data[i].name
									+ '</option>';
						}
						_self.loadCompanys(default_id);
						nowGrpId = data[0].id
						$(node.group).html(options);
						localStorage.setItem('defaultId', default_id);
					}
				}
			})
		},
		loadCompanys: function(groupId, fn) {
			var _self = this;
			$.ajax({
				url: urls.company,
				type: 'POST',
				async: false,
				data: {
					'groupId': groupId
				},
				success: function(data) {
					if(data) {
						data = data.data
						var opts = ''
						if(data.length == 0) {
							opts = '<option>当前没有数据</option>'
							$(node.company).html(opts)
							completeFnc(-1)
							return
						}
						
						companyList = []
						data.forEach(function(item, index) {
							companyList.push(item)
							opts += '<option value="' + item.id + '">' + item.name + '</option>'
						})
						$(node.company).html(opts)
						nowComId = data[0].id
						if(fn) {
							fn(data[0].id)
						}
					}
				}
			})
		},
		groupChange: function(eOrId, fn) {
			var sId
			if(eOrId) {
				if(Object.prototype.toString.apply(eOrId) != '[object object]') {
					sId = eOrId
				} else {
					var ev = e || window.event
					sId = ev.target.value
				}
			} else {
				sId = $(node.group).val()
			}
			nowGrpId = sId
			this.loadCompanys(sId, completeFnc)
			if(fn) {
				fn(sId)
			}
		},
		companysChange: function(eOrId, fn) {
			var companyId
			if(eOrId) {
				if(Object.prototype.toString.apply(eOrId) != '[object object]') {
					companyId = eOrId
				} else {
					var ev = e || window.event
					companyId = ev.target.value
				}
			} else {
				companyId = $(node.company).val()
			}
			
			nowComId = companyId
			completeFnc(companyId)

			if(fn) {
				fn(companyId)
			}
		},
		autoListen: function() {
			var _self = this
			$(node.group).change(function() {
				_self.groupChange(this.value);				
			})

			$(node.company).change(function() {
				_self.companysChange(this.value);
			})
		},
		getGroup: function() {
			if(nowGrpId != -1) {
				return nowGrpId
			} else {
				return $(node.group)[0].value
			}
		},
		getCompany: function() {
			if(nowComId != -1){
				return nowComId
			} else {
				return $(node.company)[0].value
			}
		}
	}
}();

