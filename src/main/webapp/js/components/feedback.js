import { c_get_api, c_post_api } from './common.js';

//평가 피드백
$(function(){
	$("#feedbackBtn").click(function(){
		feedback_api();
	})		
});
function feedback_api(){
	var bool = false;
	var jsonData = $('#fdbkJson').val();
	try{
		var json_obj = JSON.parse(jsonData);
		bool = true;
	}catch (e) {
		bool = false;
		swal("올바른 JSON 형식이 아닙니다(JSON Validation Error)", {
			icon : "error",
			buttons: {
				confirm: {
					className : 'btn btn-danger'
				}
			},
		});
	}
	if(bool){
//		var baseUrl = '10.100.79.62/api/autocomplete/rmodel';
		var url = '/api/feedback';
		var getData = c_post_api(url, JSON.parse(jsonData));
		swal(getData, {
			icon : "success",
			buttons: {
				confirm: {
					className : 'btn btn-success'
				}
			},
		});
	}
}