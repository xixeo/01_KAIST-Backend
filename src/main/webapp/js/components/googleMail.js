import { c_get_api, c_post_api } from './common.js';

//메일 전송
$(function(){
	$("#mailSendBtn").click(function(){
		$('#loading').show();
		setTimeout(function(){
			mailSend_api();
		}, 100);		
	});
	$("#mailSendBtnTest").click(function(){
		test();
	})	
});

function test(){
	
	console.log('"addrList" : ' + jsonData);
}

function mailSend_api(){		
	var bool = false;
	var mailTo = $('#mailMultiTo').val();
	var mailTitle = $('#mailTitle').val();
	var arrData = mailTo.split(",");
	var newArray = [];
	for(var num in arrData) {
		// trim( ) 함수를 사용하여 공백을 제거한다.
		var p = arrData[num].trim();
		var obj = new Object();
		obj["addr"] = p
		newArray.push(obj);
		
	}
	var jsonData = JSON.stringify(newArray);
	
	var mailMessage = editorMail.getData();
	if(mailTo == "" || mailTo == null){
		bool = false;
	}else{		
		var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
		if(mailTo.match(regExp) != null){
			bool = true;
		}else{
			bool = false;
			swal("정확한 이메일을 적어주세요", {
				icon : "error",
				buttons: {
					confirm: {
						className : 'btn btn-danger'
					}
				},
			});
			return;
		}
	}
	if(mailTitle == "" || mailTitle == null){
		bool = false;
	}else{
		bool = true;
	}
	if(mailMessage == "" || mailMessage == null){
		bool = false;
	}else{
		bool = true;
	}
	if(bool){
		var obj = new Object();
		obj.addrList = mailTo;
		obj.mailTitle = mailTitle;
		obj.mailMessage = mailMessage;
		var url = '/api/google/mailSend';
		var getData = c_post_api(url, obj);
		$('#loading').hide();
		swal("메일 전송에 성공하였습니다.", {
			icon : "success",
			buttons: {
				confirm: {
					className : 'btn btn-success'
				}
			},
		});
	}else{
		$('#loading').hide();
		swal("받는사람, 제목, 내용 모두 필수입력입니다", {
			icon : "warning",
			buttons: {
				confirm: {
					className : 'btn btn-warning'
				}
			},
		});
	}	
}

var editorMail;
ClassicEditor
.create(document.querySelector('#ckeditorMail'))
.then(newEditor => {
	editorMail = newEditor;
})
.catch( error => {
 console.error( error );
});

// 워드 테스트
$(function(){
	$("#docxTest").click(function(){
		docx_open();
	})
});

function docx_open(){
	alert("openDocx");
	var input = document.createElement("input");
	input.type = "file";
	input.accept = ".docx";
	input.click();
	input.onchange = function(event){
		processFile(event.target.files[0]);
	};
	
//	var reader = new FileReader();
//	reader.readAsText("C:\\Users\\sjun0916\\Desktop\\testDocx.docx", "UTF-8");
//	$("#wordArea").html(reader.result);
	
//	var url = '/api/openDocx';
//	var getData = c_get_api(url);
}

function processFile(file){
	var reader = new FileReader();
	reader.readAsText(file, "EUC-KR");
	reader.onload = function(){
		$("#wordArea").html(reader.result);
	};	
}