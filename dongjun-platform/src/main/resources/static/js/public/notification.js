var notification = (function(){
	// 储存每个通知标签
	var store = {
		notify: [],
		maxIndex: 0, // 当前notify数组中最大的下标
		length: 0,
		blanks: [],
		ignore: [],
		done: [],
		hasLoadDetail: []
	}
	// 相关dom
	var doms = {
		cover: null,
		wrapper: null
	}
	var className = {
		detail: 'notification_full_item detail',
		normal: 'notification_full_item'
	}
	var icon = {
		danger: '!',
		success: 'O',
		normal: '·'
	}
	// 用于匹配显示详情时的label，由初始时传入
	var detailLabel = {}
	// 用于选择显示在简易通知的标题和描述
	var simpleLabel = {
		title: 'title',
		description: 'description'
	}

	// 限制显示通知数，超过限制显示btn
	var limit = {
		__hasFlow: false,
		num: 100,
		text: '查看更多',
		href: '#',
		action: null
	}
	var isHiding = true 
	return {
		getStore: function() {
			return store
		},
		start: function(setting) {
			// 匹配显示详情时的label
			detailLabel = setting.detail
			// 显示在简易通知的标题和描述
			if(setting.simple) {
				simpleLabel = setting.simple
			}
			if(setting.limit) {
				for(var k in setting.limit) {
					limit[k] = setting.limit[k]
				}
				limit.__hasFlow = false
			}
			var nFull = document.createElement('div')
			nFull.id = 'notification_full'
			nFull.className = 'hide'
			var containerDomStr = '<div class="notification_full_container">'+
										            '<div class="notification_full_wrapper" id="notification_full_wrapper">'+
										            '</div>'+
										        '</div>'+
										        '<div class="notification_full_close" data-type="close_btn">×</div>';
			nFull.innerHTML = containerDomStr
			doms.wrapper = nFull.querySelector('#notification_full_wrapper')
			doms.cover = nFull
			var body = document.getElementsByTagName('body')[0]
			body.appendChild(nFull)

			this.listen()
		},
		listen: function() {
			doms.cover.addEventListener('click', function(e) {
				var ev = e || window.event
				var target = ev.target
				var fncType = target.dataset.type
				if(typeof fncType == 'undefined') {
					return
				}
				if(fncType.match(/^item/)) {
					while(!target.dataset.type.match(/^item$/)) {
						target = target.parentNode
					}
					if(fncType == 'item-ignore') {
						this.ignoreNotify(target)
					} else {
						this.notifyDetail(target)
					}
					// this.removeNotify(target)
				} else if(fncType == 'close_btn'){
					this.clearNotify()
				}
			}.bind(this))
		},
		showCover: function() {
			isHiding = false
			doms.cover.className = 'show'
		},
		hideCover: function() {
			isHiding = true
			doms.cover.className = 'hide'
		},
		createNotify: function(data, type) {
			var index
			if(store.blanks.length > 0) {
				index = store.blanks.shift()
				store.notify.splice(index, 1, data)
			} else {
				index = store.maxIndex++ 
				store.notify.push(data)
			}
			store.length++
			// store.notify.push(data)	// 暂存
			var currentTime = this.getCurrentTime()
			var item = document.createElement('div')
			item.className = 'notification_full_item ' + type
			item.dataset.type = 'item'
			item.dataset.index = index
			var domStr = 	'<div class="notification_full_item_icon" data-type="item-icon"> ' + icon[type] + '</div>' +
                    '<div class="notification_full_item_content" data-type="item-content">' +
                        '<div class="content_title" data-type="item-content"> '+ data[simpleLabel.title] +' </div>' +
                        '<div class="content_time" data-type="item-content"> '+ currentTime +'</div>' +
                        '<div class="content_description" data-type="item-content"> '+ data[simpleLabel.description] +' </div>' +
                        '<div class="content_enter" data-type="item-content">点击查看详情</div>' +
                    '</div>' +
                    '<div class="notification_full_item_close" data-type="item-ignore" style="font-size: 1.4em">×</div>'
      item.innerHTML = domStr
      return item
		},
		notifyDetail: function(item) {
			if(item.className.search(/\sdetail$/) != -1) {
				item.className = item.className.replace(/\sdetail/, '')
				return
			}
			var index = parseInt(item.dataset.index)
			if(store.hasLoadDetail.indexOf(index) == -1) {
				var data = store.notify[index]
				var detailDom = this.createDetail(data)
				item.appendChild(detailDom)
				store.hasLoadDetail.push(index)
			}
			item.className += ' detail'
		},
		createDetail: function(data) {
			var detailContainer = document.createElement('div')
			detailContainer.className = 'notification_full_item_detailContainer'
			detailContainer.dataset.type = 'item-detailContainer'
			var domStr = ''
			var label, content
			for(var d in detailLabel) {
				if(typeof data[d] === 'undefined') continue
				label = detailLabel[d].label || detailLabel[d]
				content = detailLabel[d].render ? detailLabel[d].render(data[d]) : data[d]
				domStr += '<div class="notification_full_item_detailRow" data-type="item-detail">'+
                    '<div class="detailRow_label" data-type="item-detailLabel">' + label + '</div>'+
                    '<div class="detailRow_content" data-type="item-detailContent">' + content + '</div>'+
              	  '</div>'
			}
			detailContainer.innerHTML = domStr
			return detailContainer
		},
		createLimitBtn: function() {
			var docfrag = document.createDocumentFragment()
			var btn = document.createElement('a')
			btn.className = className.normal + ' more'
			btn.innerHTML = limit.text
			if(limit.href && limit.href != '#') {
				btn.href = limit.href	
			}
			if(limit.action) {
				btn.href = 'javascript:void(0)'
				btn.onclick = limit.action
			}
      docfrag.appendChild(btn)

      var placehold = document.createElement('div')
      placehold.className = className.normal + ' placehold'
      docfrag.appendChild(placehold)
      return docfrag
		},
		ignoreNotify: function(item) {
			// 当前页面只剩下一个通知，则直接退出
			if(store.length == 1) {
				this.clearNotify()
				return
			}
			this.removeNotify(item)
			store.ignore.push(item)
		},
		removeNotify: function(item) {
			var index = parseInt(item.dataset.index)
			// 将已删除的用null代替原来坐标，并将此坐标放入blanks数组中，供下次使用
			store.notify.splice(index, 1, null)
			store.blanks.push(index)
			store.hasLoadDetail.splice(store.hasLoadDetail.indexOf(index), 1)
			store.length--
			item.className += ' remove'
			setTimeout(function(){
				item.parentNode.removeChild(item)
			}, 600)
		},
		clearNotify: function() {
			this.hideCover()
			setTimeout(function(){
				doms.wrapper.innerHTML = ''
				this.resetStore(true)
			}.bind(this), 500)
		},
		resetStore: function(resetIgnore) {
			store.maxIndex = 0
			store.length = 0
			store.notify = []
			store.blanks = []
			store.hasLoadDetail = []
			limit.__hasFlow = false
			if(resetIgnore) {
				store.ignore = []
			}
		},
		full_alert: function(dataObj) {
			var data = dataObj.data,
					type = dataObj.type ? dataObj.type : 'normal'
			// console.log(type, dataObj.type)

			var dataType = Object.prototype.toString.call(data)
			var docfrag = document.createDocumentFragment()
			console.log(dataType)
			
			// 检查是否超过限制条数，超过则添加“显示更多”按钮
			if(store.length >= limit.num) {
				if(limit.__hasFlow) {
					return
				}
				limit.__hasFlow = true
				docfrag.appendChild(this.createLimitBtn())	

			} else if(dataType === '[object Array]') {
				// 数组形式
				var len = limit.num - store.length
				if(len < data.length) {			// 超过限制条数
					data = data.slice(0, len)	// 去除超过限制的通知
					limit.__hasFlow = true		// 已经超出
				}
				data.forEach(function(item) {
					docfrag.appendChild(this.createNotify(item, type))
				}.bind(this))

				if(limit.__hasFlow){
					docfrag.appendChild(this.createLimitBtn())
				}
			} else if(dataType === '[object Object]'){
				// 单个对象形式
				// 没超过限制，添加通知
				docfrag.appendChild(this.createNotify(data, type))
			} else {
				console.warn('please send the right type')
				return
			}
			doms.wrapper.appendChild(docfrag)
			if(isHiding) {
				this.showCover()
			}
		},
		getCurrentTime: function() {
			var date = new Date()
			var h = date.getHours()
	    h = h < 10 ? ('0' + h) : h
	    var m = date.getMinutes()
	    m = m < 10 ? ('0' + m) : m
	    return h + ':' + m
		}
	}
})()