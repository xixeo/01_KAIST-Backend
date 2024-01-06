import { c_get_api, c_post_api } from './common.js';

//사용자목록
$(function(){
	$("#userBtn").click(function(){
		user_api();
	})		
});
function user_api(){
	var msrl = $('#msrl').val();
	var baseUrl = '/api/users';
	var get_param = "?msrl="+ msrl;
	var url = baseUrl + get_param;
	var getData = c_get_api(url);
		
	var str = "";
	$.each(JSON.parse(getData), function(i, v){
		str += "<tr>";
		str += "<td class='text-center'>" + v.msrl + "</td>";
		str += "<td>" + v.name + "</td>";
		str += "<td>" + v.userId + "</td>";
		str += "<td>" + v.roles + "</td>";
		str += "</tr>";
	});	
	$("#userList").html(str);
}