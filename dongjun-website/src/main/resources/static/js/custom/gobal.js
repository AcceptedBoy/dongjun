$('#log_out').click(function () {
	$.ajax({
		url: '/dongjun/logout',
	}).success(function () {
		alert('退出成功，再见！');
		location.href = 'login';
	})
});
var webPage = $('#module').data("web");
switch (webPage) {
	case 1: $('[href="index"]').removeClass('btn-primary').addClass('btn-success'); break;
	case 2: $('#manage').removeClass('btn-primary').addClass('btn-success'); break;
	case 3: $('[href="current_voltage_chart"]').removeClass('btn-primary').addClass('btn-success'); break;
	case 4: $('#event').removeClass('btn-primary').addClass('btn-success'); break;
}

$.ajax({
	url: '/dongjun/module/get_user_module',
	method: 'GET'
}).success(function(data) {
	/*if(data.text.length != 0) {
		var item = data.text[0].items
		$('#menu').append('<li class="nav-header">权限管理</li>')
		item.forEach(function(item) {
			var tag;
			switch(item.text) {
				case '查看角色': tag = '<li><a href="characterPower">'+ item.text +'</a></li>'; break;
				case '查看权限': tag = '<li><a href="powerControl">'+ item.text +'</a></li>'; break;
				case '开关管理': tag = '<li><a href="switchPower">'+ item.text +'</a></li>'; break;
			}
			$('#menu').append(tag)
		})
	}*/
})