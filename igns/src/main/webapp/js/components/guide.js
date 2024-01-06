import { c_get_api, c_post_api } from './common.js';

//조치 가이드
$(function(){
	$("#guideBtn").click(function(){
		guide_api();
	})		
});
function guide_api(){
	var cmodel = ""
	var radios = document.getElementsByName('gCmodel');
	for (var i = 0, length = radios.length; i < length; i++) {
	  if (radios[i].checked) {
		cmodel = radios[i].value;
	    break;
	  }
	}
	var ecode = $('#gEcode').val();
	var rmodel = $('#gRmodel').val();
	var axis = $('#axis').val();
	var sym1 = $('#sym1').val();
	var sym2 = $('#sym2').val();
	
//	var baseUrl = '10.100.79.62/api/search/guide';
	var baseUrl = '/api/guide';
	var get_param = "?cmodel="+ cmodel + "&ecode=" + ecode + "&rmodel=" + rmodel + "&axis=" + axis + "&sym1=" + sym1 + "&sym2=" + sym2;
	var url = baseUrl + get_param;	
	var getData = c_get_api(url);
	swal(getData, {
		icon : "success",
		buttons: {
			confirm: {
				className : 'btn btn-success'
			}
		},
	});
}