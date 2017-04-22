function Table(o) {
	this.table = null;
	this.domId = o.id;
	this.initApi = o.url.init ? o.url.init : o.url;
	this.redrawApi = o.url.redraw ? o.url.redraw : this.initApi;
	this.ajaxType = o.type ? o.type : 'POST';
	this.params = o.param ? o.param : [];
	this.render = o.render ? o.render : {};
	this.columns = o.columns;
}

Table.prototype.init = function(data) {
	var _self = this;
	data = data ? data : {id: 7};
	var columns = _self.render ? _self.getColumns() : _self.columns;
	console.log(data);
	console.log(columns);
	console.log(this)
	_self.table = $('#'+_self.domId).DataTable({
		destroy: true,
		ajax: {
			url: _self.initApi,
			type: _self.ajaxType,
			data: data,
		},
		columns: columns
	})
};

Table.prototype.getColumns = function() {
	var r = this.render;
	if(!r) {
		return this.columns;
	}
	var c = this.columns;
	for (var i = c.length - 1; i >= 0; i--) {
		if(r[c[i].data]){
			c[i].render = r[c[i].data];
		}
	}
	return c;
};

Table.prototype.redraw = function(param) {
	var _self = this;
	var table = _self.table;
	console.log(param)
	table.ajax.url(_self.redrawApi);
  table.settings()[0].ajax.data = param;
  table.ajax.reload();
};

/*
var lowVoltageTable = function() {
  var table = null;
  return {
    getTable: function(){
      return table;
    },
    init: function() {
      var _self = this;
      table = $('#hitch_event_list').DataTable({
        'destroy': true,// destroy之后才能重新加载
        'ajax': {
          'url': 'http://127.0.0.1:8080/templates/event/get_low_voltage_hitch_event_by_switch_id',
          'type': 'POST',
          'data': {
            'switchId': 7
          }
        },
        'columns': [
          {
            'data': 'hitchTime'
          },
          {
            'data': 'hitchPhase'
          },
          {
            'data': 'hitchReason',
            'render': this.redenerReason
          },
          {
            'data': 'behitchAPhaseVoltage'
          },
          {
            'data': 'behitchBPhaseVoltage'
          },
          {
            'data': 'behitchCPhaseVoltage'
          },
          {
            'data': 'behitchAPhaseCurrent'
          },
          {
            'data': 'behitchBPhaseCurrent'
          },
          {
            'data': 'behitchCPhaseCurrent'
          },
        ]
      })
    },
    redraw: function(url, id) {
      // get
      // url += '?switchId=' + id;
      // table.ajax.url('http://127.0.0.1:8080/templates/event/refresh').load();

      // post
      table.ajax.url('http://127.0.0.1:8080/templates/event/refresh');
      var param = {
        switchId: id
      };
      table.settings()[0].ajax.data = param;
      table.ajax.reload();
    },
    redenerReason(data) {
      var reason = '';
      switch(parseInt(data)) {
        case 1: reason = '剩余电流'; break;
        case 2: reason = '缺零'; break;
        case 3: reason = '过载'; break;
        case 4: reason = '短路'; break;
        case 5: reason = '缺相'; break;
        case 6: reason = '欠压'; break;
        case 7: reason = '过压'; break;
        case 8: reason += '接地'; break;
        case 9: reason = '停电'; break;
        case 10: reason = '定时试验'; break;
        case 11: reason = '远程'; break;
        case 12: reason = '闭锁'; break;
        case 13: reason = '手动'; break;
        case 14: reason = '互感器故障'; break;
        case 15: reason = '合闸失败'; break;
        case 16: reason = '设置失败'; break;
      }
      return reason;
    }
  }
}()*/