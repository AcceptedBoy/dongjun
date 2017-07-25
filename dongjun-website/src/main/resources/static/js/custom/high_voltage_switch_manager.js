$(document).ready(function() {

	/**
	 * 初始化列表
	 */
	window.afterLoadLine = function(lineData) {
		reloadDataTable(lineData[0].id)
	}
	
	initial_table("switch_list");
	loadSubstationSet();
	//reloadDataTable(localStorage.getItem('defaultId'))
	$("#add_switch_btn").click(addSwitch);
	$(".edit_switch_btn").click(editSwitch);
	$(".del_switch_btn").click(delSwitch);
	$(".enter_map").click(enterMap);
	$(".location_switch_btn").click(locateSwitch);

	// $(".chat-btn").click(handleClick)

	/**
	 * 编辑提交
	 */
	$("#edit_switch_confirm_btn").click(function() {

		$.ajax({
			type : "post",
			url : "edit_high_voltage_switch",
			async : false,
			data : {
				"id" : $("#editId").val(),
				"name" : $("#editName").val(),
				"showName" : $("#editShowName").val(),
				"lineId" : $("#editLineId").val(),
				"address" : $("#editAddress").val(),
				"longitude" : $("#editLongitude").val(),
				"latitude" : $("#editLatitude").val(),
				"simNumber" : $("#editSim").val(),
				"inlineIndex" : $("#editinlineIndex").val(),
				"deviceNumber":$("#editDeviceNumber").val(),
				'onlineTime': $('#inLineTime').val(),
				"current_ratio": $('#editCurrentRatio').val()
			},
			success : function(data) {

				if(data!=null){
					alert('修改成功')
					reloadDataTable(data);
				}
			}
		})
	});

	$("#add_switch_confirm_btn").click(function() {

		$.ajax({
			type : "post",
			url : "edit_high_voltage_switch",
			async : false,
			data : {
				"id" : $("#inputId").val(),
				"name" : $("#inputName").val(),
				"showName" : $("#inputShowName").val(),
				"lineId" : $("#inputLineId").val(),
				"address" : $("#inputAddress").val(),
				"longitude" : $("#inputLongitude").val(),
				"latitude" : $("#inputLatitude").val(),
				"simNumber" : $("#inputSim").val(),
				"inlineIndex" : $("#inlineIndex").val(),
				"deviceNumber":$("#inputDeviceNumber").val(),
				"current_ratio": $('#addCurrentRatio').val()
			},
			success : function(data) {

				if(data!=null){
					alert('添加成功')
					reloadDataTable(data);
				}
			}
		})
	});



	$(".lines").change(function(){
		reloadDataTable(this.value);
	});

});

var myState = 'high'

/**
 *
* @Title: reloadDataTable
* @Description: TODO
* @param
* @return void
* @throws
 */
function reloadDataTable(lineId){
	$('#switch_list').DataTable( {
		"destroy": true,// destroy之后才能重新加载
		"ajax": "high_voltage_switch_list_by_line_id.action?lineId="+lineId,
        "columns": [
            { "data": "deviceNumber" },
            { "data": "name", "maxWidth": "100px" },
            { "data": "showName"},
            { 	"data": "id",
            	"sClass": "dpass"
            },
            { 	"data": "lineId",
            	"sClass": "dpass"
            },
	        { "data": "address" },
	        { "data": "longitude" },
	        { "data": "latitude" },
	        { "data": "simNumber" },
	        { "data": "inlineIndex" },
	        { "data": "currentRatio" },
	        { "data": "onlineTime"},
	        { "data": null},
            { "data": null},// 设置默认值 null，表示列不会获得数据源对象的信息,否则默认值会被覆盖掉
            { "data": null},// 设置默认值 null，表示列不会获得数据源对象的信息,否则默认值会被覆盖掉
            { "data": null},
            { "data": null}
        ],

        // 为下面的列设置默认值
        "columnDefs": [ {
            "targets": -5,
            "data": null,
            "defaultContent": '<button class="btn btn enter_map">进入地图</button>'
        },{
            "targets": -4,
            "data": null,
            "defaultContent": '<a href="#edit_switch_modal" role="button" class="edit_switch_btn btn" data-toggle="modal">修改</a>'
        },
        {
            "targets": -3,
            "data": null,
            "defaultContent": '<a href="#del_switch_modal" class="del_switch_btn btn btn-danger" data-toggle="modal" data-backdrop="static">删除 </a>'
        },
        {
        	"targets": -2,
            "data": null,
            "defaultContent": '<a href="#location_switch_modal" role="button" class="location_switch_btn btn btn-primary" data-toggle="modal">设为定位中心</a>'
        },
        {
        	"targets": -1,
            "data": null,
            "defaultContent": '<a class="chat-btn btn btn-primary">实时操控</a>'
        }
        ],
        'language': {
            'paginate': {
              'next': '下一页',
              'previous': '上一页'
            },
            'emptyTable': '找不到相关数据',
            'zeroRecords': '找不到相关数据'
          },
        "fnInitComplete": function(oSettings, json) {
	        //alert('123')
        	$(".edit_switch_btn").unbind().click(editSwitch);
        	$(".del_switch_btn").unbind().click(delSwitch);
        	$(".enter_map").unbind().click(enterMap);
        	$(".location_switch_btn").unbind().click(locateSwitch);
        	$(".chat-btn").click(handleClick)
          }
    } );

	$.ajax({
			url: '/dongjun/online_order',
			method: 'POST',
			data: {
				'lineId': lineId
			}
		}).success(function(data) {

		})

}


/**
 *
 * @Title: addSwitch
 * @Description: TODO
 * @param
 * @return void
 * @throws
 */
function addSwitch() {

	$("#inputId").val("");

	if($(".lines").val() == null){

		$("#inputLineId").val(
				$(".edit_switch_btn").parent("td").prevAll()[8].innerHTML);
	}else{
		$("#inputLineId").val($(".lines").val());
	}
	$("#inputAddress").val("");
	$("#inputLongitude").val("");
	$("#inputLatitude").val("");
	$("#inputDeviceNumber").val("");
	$("#inputName").val("");
	$("#inputShowName").val("");
	$("#inputSim").val("");
	$("#inlineIndex").val("");
	$('#addCurrentRatio').val("")
}

/**
 *
 * @Title: editSwitch
 * @Description: TODO
 * @param
 * @return void
 * @throws
 */
function editSwitch() {

	var column = $(this).parent("td").prevAll();
	console.log(column);
	$("#editDeviceNumber").val(column[12].innerHTML);
	$("#editName").val(column[11].innerHTML);
	$("#editShowName").val(column[10].innerHTML);
	$("#editId").val(column[9].innerHTML);
	$("#editLineId").val(column[8].innerHTML);
	$("#editAddress").val(column[7].innerHTML);
	$("#editLongitude").val(column[6].innerHTML);
	$("#editLatitude").val(column[5].innerHTML);
	$('#inLineTime').val(column[1].innerHTML);
	$("#editSim").val(column[4].innerHTML);
	$("#editinlineIndex").val(column[3].innerHTML);
	$("#editCurrentRatio").val(column[2].innerHTML);
}


/**
 *
 * @Title: delSwitch
 * @Description: TODO
 * @param
 * @return void
 * @throws
 */
function delSwitch() {

	var column = $(this).parent("td").prevAll();
	$("#del_confirm_btn").click(function() {
		$.ajax({
			type : "post",
			url : "del_high_voltage_switch",
			async : false,
			data : {
				"switchId" : column[10].innerHTML,
			},
			success : function(data) {

				if(data!=null){

					reloadDataTable(data);
				}
			}
		})
	});
}

/**
 *
 * @Title: enterMap
 * @Description: TODO
 * @param
 * @return void
 * @throws
 */
function enterMap() {

	var longitude = $(this).parent("td").prevAll()[5].innerHTML;
	var latitude = $(this).parent("td").prevAll()[4].innerHTML;
	localStorage.setItem('longitude', longitude);
	localStorage.setItem('latitude', latitude);
	location.href = "index";
}

function locateSwitch() {

	var column = $(this).parent("td").prevAll();
	$.ajax({
		type : "get",
		url : "edit_location",
		async : false,
		data : {
			"switchId" : column[11].innerHTML,
			"type" : 1,
			"scale": 12
		},
		success : function(data) {

			if(data != null){

				alert("设置成功");
			}
		}
	})
}


function handleClick(e) {
	var column = $(this).parent("td").prevAll()
	if(localStorage) {
		var switchConfig = {
			switchId: column[12].innerHTML,
			showName: column[13].innerHTML,
			switchName: column[14].innerHTML,
			switchNum: column[15].innerHTML
		}
		var localData = JSON.stringify(switchConfig)
		localStorage.setItem('switchConfig', localData)
		location.href = '/dongjun/chat'
	}
}