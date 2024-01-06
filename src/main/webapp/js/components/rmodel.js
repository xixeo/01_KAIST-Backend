import { c_get_api, c_post_api } from './common.js';

//로봇모델 자동완성
$(function(){
	$("#rmodelBtn").click(function(){
		rmodel_api();
	})		
});
function rmodel_api(){
	var q = $('#rRmodel').val();
//	var baseUrl = '10.100.79.62/api/autocomplete/rmodel';
	var baseUrl = '/api/rmodel';
	var get_param = "?q="+ q;
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