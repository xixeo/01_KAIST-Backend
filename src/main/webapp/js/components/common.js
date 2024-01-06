// GET contextPath
function getContextPath(){
	var hostIndex = location.href.indexOf( location.host ) + location.host.length;
	return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
}

// 버전정보 가져오기
var str = "igmes";	
$("#version-text").html(str);

// 메뉴 외 다른 영역 클릭시 메뉴 닫기
$('.main-panel').click(function(e){
	var open = 1;
	var sidebar = false;
	var toggle = false;
	sidebarToggle(open, sidebar, toggle);
});

var realUrl = "http://3.34.181.220/";
var baseApiUrl = "http://twx.i-gns.co.kr/igns";

//getXMLHttpRequest() 객체생성
var httpRequest = null; 
function getXMLHttpRequest() { 
	//브라우저가 IE일 경우 XMLHttpRequest 객체 구하기
	if (window.ActiveXObject) {
		//Msxml2.XMLHTTP가 신버전이어서 먼저 시도
		try { 
			return new ActiveXObject("Msxml2.XMLHTTP");
		} catch(e) { 
			try { 
				return new ActiveXObject("Microsoft.XMLHTTP");
			} catch(e1) { 
				return null;
			}
		}
	//IE 외 파이어폭스 오페라 같은 브라우저에서 XMLHttpRequest 객체 구하기
	} else if (window.XMLHttpRequest) { 
		return new XMLHttpRequest();
	} else { 
		return null; 
	}
}

// 응답처리 콜백함수 등록
function callbackFunction(){ 
	// 서버로부터 응답이 왔으므로 알맞은 작업을 수행
	if (httpRequest.readyState == 4) {   // (수신 완료, XMLHttpRequest.DONE : 4)
		if (httpRequest.status == 200) { // (통신 성공)
			alert(httpRequest.responseText); 
		} else { 
			alert("실패: "+httpRequest.status);
		}
	} else { // 통신 완료 전
        console.log('통신중...');
    }
}

// COMMON GET API
function c_get_api(url){ 
	var xhr = getXMLHttpRequest();
	xhr.open('GET', url, true);
//    xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.setRequestHeader("Access-Control-Allow-Headers", "*");
    xhr.setRequestHeader('Access-Control-Allow-Origin','*');
	xhr.setRequestHeader('Access-Control-Allow-Credentials','true');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === xhr.DONE) {
            if (xhr.status === 200 || xhr.status === 201) {
                console.log(JSON.parse(xhr.responseText));
            } else {
            	console.error(xhr.responseText);
            }
        }
    };    
    xhr.send();    
}


// COMMON GET API 2 (사용)
function c_get_api2(url){
	var path = getContextPath();
	var baseUrl = path + url;
	console.log('[[ Request API Url ]] ' + baseUrl)
	var returnRes = "";	
	$.ajax({
		url : baseUrl,
		type : 'GET',
		contentType: 'application/json',
		dataType : 'json',
		cache : true,
		async : false,
//		crossOrigin : true,
//		crossDomain: true,
		xhrFields: {
			withCredentials: true
		},
		beforeSend: function(xhr){
			xhr.setRequestHeader("Access-Control-Allow-Headers", "*");
			xhr.setRequestHeader('Access-Control-Allow-Origin','*');
			xhr.setRequestHeader('Access-Control-Allow-Credentials','true');
		},
		success : function(data, status, xhr){
			returnRes = data;
		},
		error: function(xhr, status, error){
			var getMsg = xhr + ", " + error;
			swal(getMsg, {
				icon : "error",
				buttons: {
					confirm: {
						className : 'btn btn-danger'
					}
				},
			});
		}
	});
	
	return returnRes;
}

//COMMON POST API
function c_post_api(url, obj){
	var path = getContextPath();
	var baseUrl = path + url;
	var returnRes = "";
	const json_data = JSON.stringify(obj);
	$.ajax({
		url : baseUrl,
		type : 'POST',
		contentType: 'application/json',
		dataType : 'json',
		data : json_data,
		cache : true,
		async : false,
		success : function(response){
			returnRes = JSON.stringify(response);
		},
		error: function(xhr, status, error){
//			swal(xhr, {
//				icon : "error",
//				buttons: {
//					confirm: {
//						className : 'btn btn-danger'
//					}
//				},
//			});
		}
	});
	
	return returnRes;
}

export { c_get_api, c_get_api2, c_post_api };