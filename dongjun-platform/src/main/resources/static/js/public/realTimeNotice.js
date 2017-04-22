// 暴露到全局，万一哪天要订阅别的推送呢= =
var stompClient = null
// load socket
dj.jsLoad('../../js/websocket/sockjs.min.js', function() {
	dj.jsLoad('../../js/websocket/storm.js', function() {
		// load notification.js
		dj.jsLoad('../../js/public/notification.js', function() {
			// load css
			var link = document.createElement('link')
			link.type = 'text/css'
			link.rel = 'stylesheet'
			link.href = '../../css/notification/notification_full.css'
			document.getElementsByTagName('head')[0].appendChild(link)

			// set the notification
			notification.start({
				// 配置简易通知的显示内容，title——标题， description——内容
				simple: {
					title: 'name',
					description: 'hitchReason'
				},
				// 配置详情通知的显示内容
				detail: {
					name: '设备名称',
					type: '设备类型',
					hitchReason: '报警原因',
					tag: {
						label: '传感器',
						render: function(data) {
							return data + ' 号'
						}
					},
					value: '报警值',
					maxHitchValue: '温度上限',
					minHitchValue: '温度下限'
				},
				// 设置限制条数10，超过则跳转
				limit: {
					num: 10,
					action: function() {
						location.pathname = 'templates/event/index.html'
					}
				}
			})

			// start socket
			var socket = new SockJS('/portfolio')
			stompClient = Stomp.over(socket)
			stompClient.connect({}, function(frame) {
				// 订阅报警，每次返回一个object
				stompClient.subscribe('/queue/user-' + roles.currentId + '/hitch', function(message) {
					var data = JSON.parse(message.body)
					// 通知
					notification.full_alert({
						data: data,
						type: 'danger'
					})
				})
			})
		})
	})
})
