(function() {
  Notification.start({
    limit: {
      num: 100
    }
  })
	
  var api = {
    read: '/dongjun/read_text',
    send: '/dongjun/send_text',
    totalCall: '/dongjun/send_total_call',
    stop: '/dongjun/stop_read_text',
    queue: '/user/queue/read_text',
    infor: ''
  }

  var switchId = ''
  var switchName = ''
  var switchNum = 0
  var showName = ''

  if(localStorage) {
    config = JSON.parse(localStorage.getItem('switchConfig'))
    if(config) {
      switchId = config.switchId
      switchName = config.switchName
      switchNum = config.switchNum
      showName = config.showName
    } else {
      console.error('客户端没有缓存到设备的id，请进入设备管理页面点击设备实时操控')
    }
  }

  // 载入监控窗口头部标题
  function showTitle () {
    $('.switchNum').text(switchNum)
    $('.switchName').text(switchName)
    $('.showName').text(showName)
  }

  // 生成时间戳
  function generateTime() {
	var t = new Date()
	return t.toLocaleDateString() + ' - ' + t.toLocaleTimeString()
  }
  
  // 给监控窗口加入一条聊天
	function addItem(title, content) {
		var t = generateTime()
		$('.chat-board').append(`<div style="margin: 0; padding:0 15px; font-size: .9em">${t}</div><div class="chat-item"><b>${title}：</b>${content}</div>`)
	}

  // 在页面载入时通知后台这台设备开始接受推送
  function startRead () {
    $.ajax({
      url: api.read,
      data: {
        switchId: switchId
      }
    })
  }

  // 在页面卸载时通知后台这台设备结束接受推送
  function stopRead () {
    $.ajax({
      url: api.stop,
      data: {
        switchId: switchId
      }
    })
  }
  
  function tip(reason, type) {
	Notification.notify('simple', {
	  simple: {
	    title: 'title',
	    description: 'reason'
	  },
	  data: {
		title: '错误',
		reason: reason,
	  },
	  timeout: 2000,
	  type: type
	})
  }

  // 开始socket的连接
  var socket = new SockJS('/portfolio')
  var stompClient = Stomp.over(socket)
  stompClient.connect({}, function (frame) {
    stompClient.subscribe(api.queue, function(msg) {
    	console.log(msg)
    	if(msg) {
    		addItem('接收', msg.body)
    	}
    })
  })

  // 下面是给按钮添加事件
  $('#sendMsg').click(function (e) {
    // ajax
    var msg = $('.send-content').val()
    if(msg) {
      $.ajax({
        url: api.send,
        data: {
          text: $('.send-content').val(),
          switchId: switchId
        },
        success: function(res) {
          if(res) {
            addItem('发送', msg)
            $('.send-content').val('')
          } else {
        	// 错误处理
        	tip('设备已离线', 'danger')
          }
        }
      })
    } else {
      tip('请输入有意义的值')
    }
  })

  $('.send-content').keydown(function (e) {
    var event = e || window.e
    var msg = $('.send-content').val()
    if(event.keyCode == 13) {
    	event.preventDefault()
    	if(msg) {
        $.ajax({
          url: api.send,
          type: 'POST',
          data: {
            text: $('.send-content').val(),
            switchId: switchId
          },
          success: function(res) {
            if(res) {
              addItem('发送', msg)
              $('.send-content').val('')
            } else {
            	// 错误处理
            	tip('设备已离线', 'danger')
            }
          }
        })
      } else {
    	tip('请输入有意义的值')
      }
    }
  })
  
    $('#sendAll').click(function (e) {
	    $.ajax({
	    	url: api.totalCall,
	    	data: {
	    		switchId: switchId
	    	},
	    	type: 'POST',
	    	success: function(res) {
	    		if(res) {
	    			addItem('发送', '总召')
	    		} else {
	    			// 错误处理
	    			tip('设备已离线', 'danger')
	    		}
	    	}
	    })
	  })

  $('.return').click(function (e) {
    location.href = "/dongjun/high_voltage_switch_manager"
  })

  $('.return-font').click(function (e) {
    location.href = "/dongjun/high_voltage_switch_manager"
  })

  // 加载好页面后告诉后台开始订阅
  $(window).load(function () {
    startRead()
    showTitle()
  })

  window.onbeforeunload = function() {
    stopRead()
  }
})()