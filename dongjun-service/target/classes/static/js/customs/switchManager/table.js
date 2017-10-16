// ECalender 写法
// $("#ECalendar_date").ECalendar({
// 	 type:"time",   //模式，time: 带时间选择; date: 不带时间选择;
// 	 stamp : false,   //是否转成时间戳，默认true;
// 	 offset:[0,2],   //弹框手动偏移量;
// 	 format:"yyyy年mm月dd日",   //时间格式 默认 yyyy-mm-dd hh:ii;
// 	 skin:3,   //皮肤颜色，默认随机，可选值：0-8,或者直接标注颜色值;
// 	 step:10,   //选择时间分钟的精确度;
// 	 callback:function(v,e){} //回调函数
// });
// 
(function (){
	// Polyfill Object.assign
	if (typeof Object.assign != 'function') {
	  Object.assign = function(target) {
	    'use strict';
	    if (target == null) {
	      throw new TypeError('Cannot convert undefined or null to object');
	    }

	    target = Object(target);
	    for (var index = 1; index < arguments.length; index++) {
	      var source = arguments[index];
	      if (source != null) {
	        for (var key in source) {
	          if (Object.prototype.hasOwnProperty.call(source, key)) {
	            target[key] = source[key];
	          }
	        }
	      }
	    }
	    return target;
	  };
	}
	var api = {
		company: '/dongjun/company/list',
		substation: '/dongjun/substation/list',
		line: '/dongjun/line/list',
		switch: '/dongjun/switch/list',
		editSwitch: '/dongjun/switch/edit_date',
		disSwitch: '/dongjun/switch/disabled'
	}

	var switchTable = function() {
	  var table = null;
	  var defUrl = api.switch
	  var store = {
	  	id: ''
	  }
	  var isInit = false
	  return {
	    getTable: function(){
	      return table;
	    },
	    init: function(o) {
	      var _self = this;
	      table = $('#switch_list').DataTable({
					'ajax': {
			      'url': defUrl,
			      'type': 'POST',
			      'data': o.data,
			      'dataSrc': function(data) {
			        return typeof data === 'string' ? JSON.parse(data).text : data.text
		        }
			     },
			    'columns': [
			      { 'data': 'name' },
			      { 'data': 'address' },
			      { 'data': 'expireTime' },
			      {
			        data: 'id',
			        orderable: false,
			        width: '80px',
			        render: function(data, type, row) {
			        	var renderMsg = row.available ? '修改期限' : '启用'
			        	var extraClass = row.available ? '' : 'mdl-button--primary'
			          return '<button class="mdl-button mdl-js-button '+extraClass+'" data-fnc="editDate" data-id="'+data+'">'+
			          				renderMsg +'</button>'
			        }
			      },
			      {
			        data: 'available',
			        orderable: false,
			        width: '80px',
			        render: function(available, type, row) {
			        	var renderMsg
			        	if (available) {
			        		renderMsg = '<button class="mdl-button mdl-js-button mdl-button--accent" data-fnc="delete" data-id="'+row.id+'">' +
			        								'点击停用</button>'
			        	} else {
			        		renderMsg = '<button class="mdl-button mdl-js-button" disabled>已停用</button>'
			        	}
			          return renderMsg
			        }
			      },
			    ],
			    'language': {
			      'paginate': {
			        'next': '下一页',
			        'previous': '上一页'
			      },
			      'emptyTable': '找不到相关数据',
			      'zeroRecords': '找不到相关数据',
			      'loadingRecords': '正在加载数据...'
			    },
			    drawCallback: function () {
			    	componentHandler.upgradeDom()
			    },
			    'initComplete': function() {
			    	isInit = true
			    	$(this[0]).click(function(event) {
	            var target = $(event.target)
	            var fnc = target.data('fnc')
	            switch(fnc) {
	              // 编辑功能
	              case 'editDate':
	                var id = target.data('id')
	                var data = Array.prototype.find.call(table.data(), function(item) {
	                  return item.id == id
	                })
	              	switchModal.setId(id).setModalForm('editTime', data).show()
	                return
	              case 'delete':
	              	switchModal.setId(target.data('id')).show('disableSwitch')
	            }
	          })
			    }
			  })
	    },
	    redraw: function(id) {
	    	console.log('redraw: ', id)
	 	  	if(id == -1) {
	       	table.clear().draw()
	       	return
	      }
	      // post
	      table.ajax.url(defUrl)
	      var param = {
	        lineId: id
	      }
	      table.settings()[0].ajax.data = param;
	      table.ajax.reload()
	    },
	    toggleRender: function (lineId) {
	    	if(!isInit) {
	    		this.init({
	    			lineId: lineId
	    		})
	    	} else {
	    		this.redraw(lineId)
	    	}
	    }
	  }
	}()

	var switchModal = function () {
		var url = {
			editDate: '',
			disableSwitch: ''
		}
		var store = {
			curId: '',
			curModal: null,
			editDate: {
				endDate: ''
			}
		}
		var modal = {
			editTime: $('#editTime'),
			disableSwitch: $('#disableSwitch')
		}
		return {
			init: function (opts) {
				if (opts && opts.dateInput) {
					this.__initCalender(opts.dateInput).__listen()
				}
			},
			store: function (storeId, data) {
				if (arguments.length === 0) {
					return store
				}
				Object.assign(store[storeId], data)
			},
			setId: function (id) {
				store.curId = id
				return this
			},
			__initCalender: function (opts) {
				for (var opt in opts) {
					$('#' + opt).ECalendar(opts[opt])
				}
				return this
	    },
	    setModalForm: function (formName, data) {
	    	store.curModal = modal[formName]
	    	var inputs = store.curModal.find('input')
	    	console.log(data)
	      Array.prototype.forEach.call(inputs, function(item) {
	      	console.log(item.name)
	        item.value = data[item.name]
	      })
	      return this
	    },
	    saveEndDate: function () {
	    	var date = $('#expireTimeInput').val()
	    	if (!$.trim(date)) {
	    		Msg.notify('请选择日期')
	    		return
	    	}
	    	// get data
	    	var data = {
	    		endDate: date,
	    		switchId: store.curId
	    	}
	    	console.log(data)
	    	$.ajax({
	    		url: api.editSwitch,
	    		type: 'POST',
	    		data: data,
	    		success: function (res) {
	    			Msg.notify('修改成功')
	    			switchTable.redraw(store.curId)
	    		},
	    		error: function () {
	    			Msg.notify('修改失败')
	    		}
	    	})
	    },
	    __listen: function () {
	    	var self = this
	    	$('#saveEditTime').click(function() {
	    		self.saveEndDate()
	    	})
	    	$('#saveDisableSwitch').click(function() {
	    		self.disSwitch()
	    	})
	    },
	    show: function (name, cb) {
	    	if (!name) {
	    		if (store.curModal) {
	    			store.curModal.modal()
	    		}
	    	} else {
		    	modal[name].modal()
		    	if (cb) cb()
	    	}
	    },
	    disSwitch: function () {
	    	var id = store.curId
	    	$.ajax({
	    		type: 'POST',
	    		url: api.disSwitch,
	    		data: {
	    			switchId: id
	    		},
	    		success: function () {
	    			Msg.notify('停用成功，正在刷新列表')
	    			switchTable.redraw(store.curId)
	    		},
	    		error: function () {
	    			Msg.notify('停用失败')
	    		}
	    	})
	    }
		}
	}()

	var Msg = function () {
		var snackbarContainer = document.querySelector('#notification')
		return {
			notify: function (text) {
				snackbarContainer.MaterialSnackbar.showSnackbar({
					message: text,
					timeout: 10000
				})
			}
		}
	}()

	selectManager.setOption({
		url: {
			company: api.company,
			substation: api.substation,
			line: api.line
		},
		node: {
			company: $('#companySelect'),
			substation: $('#substationSelect'),
			line: $('#lineSelect')
		},
		completeFnc: switchTable.toggleRender.bind(switchTable)
	}).loadCompany()

	// switchTable.init()

	switchModal.init({
		dateInput: {
			'expireTimeInput': {
				type:"date",
				skin:"#2196f3",
				offset:[-55,2],
				callback(v, e) {
					console.log(v, e)
				}
			}
		}
	})
	
})()