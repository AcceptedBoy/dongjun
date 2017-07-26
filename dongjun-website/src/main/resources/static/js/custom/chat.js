(function() {
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

  // 给监控窗口加入一条聊天
	function addItem(title, content) {
		$('.chat-board').append(`<div class="chat-item"><b>${title}：</b>${content}</div>`)
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
          }
        }
      })
    }
  })

  $('.send-content').keyup(function (e) {
    var event = e || window.e
    event.preventDefault()
    var msg = $('.send-content').val()
    if(event.keyCode == 13) {
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
            }
          }
        })
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
	    			console.log('warn')
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