/*
 * 로그인
 */
$(function(){
	$("#login-button").click(function(){
		login();
	})		
})


function login(){
	// 로그인 폼 데이터 가져오기
	var loginData = document.loginForm;
	var obj = new Object();
	obj.userId = loginData.userEmail.value;
	obj.password = loginData.userPw.value;
	const json_data = JSON.stringify(obj);
	// 통신	
//	$.ajax({
//		url : '/signin',
//		type : 'POST',
//		contentType: 'application/json',
//		dataType : 'json',		
//		data : json_data,
//		cache : true,
//		async : false,
//		success : function(response){
//			if(response.success){
//				sessionStorage.setItem('token', response.data);
//				nextStep();
//			}else{
//				alert(response.msg);
//			}
//		},
//		error: function(xhr, status, error){
//			var getMsg = JSON.parse(xhr.responseText);
//			alert(getMsg.msg);
//		}
//	});
	nextStep();	
}

//GET contextPath
function getContextPath(){
	var hostIndex = location.href.indexOf( location.host ) + location.host.length;
	return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
}

function nextStep(){
	var path = getContextPath();
	location.href= path + "/ecode";
}