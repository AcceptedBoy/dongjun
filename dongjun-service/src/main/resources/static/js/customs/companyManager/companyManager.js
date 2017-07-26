var table = $('#company_list').DataTable({
		'ajax': {
		  'url': `${localhost}/company/list`,
		  // 'url': '/dongjun/company/list',
		  'type': 'POST',
		  'dataSrc': function(data) {
		    return typeof data === 'string' ? JSON.parse(data).text : data.text
			}
		 },
		'columns': [
		  { 'data': 'name' },
		  { 'data': 'description' },
		  { 'data': 'chief' },
		  { 'data': 'address' },
		  { 'data': 'phone' },
		  { 'data': 'ipAddr' },
		  {
		    data: 'id',
		    orderable: false,
		    width: '80px',
		    render: function(data, type, row) {
					return `<button data-fnc="edit" data-id=${data} class="mdl-button mdl-js-button mdl-js-ripple-effect">修改</button>`
		    }
		  },
		  {
		  	data: 'id',
		  	orderable: false,
		  	width: '80px',
		  	render: function(data, type, row) {
		  		return `<button data-fnc="del" data-id=${data} class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">删除</button>`
		  	}
		  }
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
		initComplete: function() {
			$(this[0]).click(function(e) {
				var target = $(e.target.parentNode)
				var type = target.data('fnc')
				switch(type) {
					case 'edit':
						var id = target.data('id')
						var row = Array.prototype.find.call(table.data(), function(item) {
							return item.id === id
						})
						util.showEdit(row)
						break
					case 'del':
						var id = target.data('id')
						util.handleDel(id)
						break
				}
			})
			$('.add-company').click(function(e) {
				util.showAdd()
			})
			// 提交事件
			$('.submit').click(function(e) {
				var data = {}
				var canSubmit = true
				var itemArr = $('.form-item')
				for(var i = 0; i < itemArr.length; i++) {
					// 验证各个表单是否通过
					if(!$(itemArr[i]).find('input').val() || $(itemArr[i]).hasClass('is-invalid')) {
						$(itemArr[i]).find('input').addClass('warn-tip')
						canSubmit = false
					} else {
						var node = $(itemArr[i]).find('input')
						data[$(node).data('type')] = $(node).val()
					}
				}
				data.description = $('#description').val() || null
				if(canSubmit) {
					// submit
					console.log(data)
					cAjax.save(data)
				} else {
					var snackbarContainer = document.querySelector('#snackbar')
					var tips = {
					  message: '必填表单必须填写并填入合适的值',
					  timeout: 2000
					}
					snackbarContainer.MaterialSnackbar.showSnackbar(tips)
				}
			})
			$('.form-item').find('input').focus(function(e) {
				$(this).removeClass('warn-tip')
			})
		}
})

/**
 * [util 用来设置modal一些属性的单例]
 * @type {Object}
 */
var util = {
	showAdd: function () {
		$('.modal-title').text('增加公司')
		$('#company-modal').modal('show')
		// 清空表单数据和错误信息
		$('.form-item').find('input').removeClass('warn-tip')
		$('.form-item').removeClass('is-focused is-dirty is-invalid')
		$('.area-item').removeClass('is-focused is-dirty')
		$('.form-item').find('input').val('')
		$('#description').val('')
		// 将modal模式切位增加
		cAjax.setType('add')
	},
	showEdit: function (row) {
		$('.modal-title').text('修改公司信息')
		// 展示窗口
		$('#company-modal').modal('show')
		// 除掉错误提示，加入使title展开的Class
		$('.form-item').find('input').removeClass('warn-tip')
		$('.form-item').removeClass('is-invalid')
		$('.form-item').addClass('is-focused is-dirty')
		$('.area-item').addClass('is-focused is-dirty')
		// 用row的data给里面的表单赋值
		var inputArr = $('.form-item').find('input')
		var value = ''
		for(var i = 0; i < inputArr.length; i++) {
			value = $(inputArr[i]).data('type')
			$(inputArr[i]).val(row[value])
		}
		// 给textarea赋值
		$('#description').val(row.description)
		// 将modal模式切位编辑
		cAjax.setType('edit')
		cAjax.setId(row.id)
	},
	handleDel: function(id) {
		console.log(id)
		alert('删除公司，同时会删除该公司旗下的配电站、线路、设备，确定要删除吗？')
		cAjax.delCompany(id)
	}
}

/**
 * [cAjax 用来保存对接相关信息的一个单例]
 * @return {[type]} [description]
 */
var cAjax = function() {
	var type = ''
	var id = ''
	var url = {
		addCompany: '/dongjun/company/edit',
		delCompany: '/dongjun/company/del',
		editCompany: '/dongjun/company/edit'
	}
	var redraw = function() {
    table.ajax.reload()
  }
	return {
		setType: function(choose) {
			type = choose
		},
		setId: function(thisId) {
			id = thisId
		},
		save: function(data) {
			if(!type == 'add') {
				data.id = id
			}
			$.ajax({
				url: url.addCompany,
				data: data,
				success: function(res) {
					redraw()
				}
			})
		},
		delCompany: function(id) {
			$.ajax({
				url: url.delCompany,
				data: {
					companyId: id
				},
				success: function(res) {
					redraw()
				}
			})
		}
	}
}()

// var validate = function() {
// 	var type = ''
// 	var reg = ''
// 	return {
// 		setType: function(type) {
// 			type = type
// 		},
// 		setReg: function(reg) {
// 			reg = reg
// 		},
// 		isEmpty: function(val) {
// 			if(val && val.trim()) {
// 				return true
// 			} else {
// 				return false
// 			}
// 		},
// 		requireTest: function(val) {
// 			if(this.isEmpty(val)) {
// 				if(reg.test(val)) {
// 					return true
// 				} else {
// 					return false
// 				}
// 			} else {
// 				return false
// 			}
// 		},
// 		chooseTest: function(val) {
// 			if(this.isEmpty) {
// 				return true
// 			} else {
// 				if(reg.test(val)) {
// 					return true
// 				} else {
// 					return false
// 				}
// 			}
// 		}
// 	}
// }()