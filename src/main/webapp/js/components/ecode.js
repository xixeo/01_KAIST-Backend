import { c_get_api, c_get_api2, c_post_api } from './common.js';

$(function(){	
	// 아코디언 ∨ , ∧ 변경
	var acc = document.getElementsByClassName("accordion");
	var i; 
	for (i = 0; i < acc.length; i++) { 
		acc[i].addEventListener("click", function() {
			// Add fa-angle-up icon for collapse element which is open by default
			$(".collapse.show").each(function(){
		    	$(this).prev(".card-header").find(".fas").addClass("fa-angle-up").removeClass("fa-angle-down");
		    });
		    
		    // Toggle fa-angle-down / fa-angle-up icon on show hide of collapse element
		    $(".collapse").on('show.bs.collapse', function(){
		    	$(this).prev(".card-header").find(".fas").removeClass("fa-angle-down").addClass("fa-angle-up");
		    }).on('hide.bs.collapse', function(){
		    	$(this).prev(".card-header").find(".fas").removeClass("fa-angle-up").addClass("fa-angle-down");
		    });
		});
	};
	
	// 조회버튼 클릭 시 조치 가이드 조회
	$("#ecodeBtn").click(function(){
		guide_api();
	});
	
	// 에러코드 타입 변경 시
	$("#basic-addon1").click(function(){ 
		if($('#basic-addon1').text() == "E"){
			$('#basic-addon1').html("W");
		}else if($('#basic-addon1').text() == "W"){
			$('#basic-addon1').html("A");
		}else if($('#basic-addon1').text() == "A"){
			$('#basic-addon1').html("E");
		}else{
			$('#basic-addon1').html("E");
		}
		guide_api();
	});
	
	// 제어기모델 타입 변경시
	$("input[name='eCmodel']").change(function(){
		guide_api();
	});
	
	// 에러코드 자동완성	
	$("#eEcode").autocomplete({
		// source 는 자동 완성 대상
        source : function( request, response ) {
//        	var cmodel = "";
//        	var radios = document.getElementsByName('eCmodel');
//        	for (var i = 0, length = radios.length; i < length; i++) {
//        	  if (radios[i].checked) {
//        		cmodel = radios[i].value;
//        	    break;
//        	  }
//        	};
        	var cmodel = $("#cmodelSelect").val();
        	var q = $('#basic-addon1').text() + $('#eEcode').val();
        	var params = "?q="+ q + "&cmodel=" + cmodel;

        	var baseUrl = '/api/rest/ecode';
        	var url = baseUrl + params;
        	var getData = c_get_api2(url);
//        	console.log("getDataAuto ::: " + JSON.stringify(getData.ecodes));
        	//json[i] 번째 에 있는게 item 임.
        	response($.map(getData.ecodes, function(item) {    
        		return {
            		label: item.ecode,							// UI 에서 보여지는 글자, 실제 검색어랑 비교 대상
                    value: item.ecode.replace(/[^0-9]/g,"")		// value 값은 숫자로만 가공
                }
        	}));
        },   
        // 아이템 선택시
        select : function(event, ui) {
        	//사용자가 오토컴플릿이 만들어준 목록에서 선택을 하면 반환되는 객체
//            console.log(ui);
//            console.log(ui.item.label);
//            console.log(ui.item.value);
            
        },
        // 포커스 가면
        focus : function(event, ui) {    
        	//한글 에러 잡기용도로 사용됨
            return false;
        },
        // 최소 글자수
        minLength: 1,
        // 첫번째 항목 자동 포커스 기본값 false
        autoFocus: true, 
        classes: {
            "ui-autocomplete": "highlight"
        },
        // 검색창에 글자 써지고 나서 autocomplete 창 뜰 때 까지 딜레이 시간(ms)
//        delay: 500,    
        // 자동완성 기능 끄기
//        disabled: true, 
        position: { my : "right top", at: "right bottom" },
        // 자동완성창 닫아질때 호출
        close : function(event){    
//            console.log(event);
        }
    });
	

	// 로봇모델 자동완성	
	$("#rModel").autocomplete({
		// source 는 자동 완성 대상
        source : function( request, response ) {
//        	var cmodel = "";
//        	var radios = document.getElementsByName('eCmodel');
//        	for (var i = 0, length = radios.length; i < length; i++) {
//        	  if (radios[i].checked) {
//        		cmodel = radios[i].value;
//        	    break;
//        	  }
//        	};
        	var q = $('#rModel').val();
        	var params = "?q="+ q;

        	var baseUrl = '/api/rest/rmodel';
        	var url = baseUrl + params;
        	var getData = c_get_api2(url);
//        	console.log("getDataAuto ::: " + JSON.stringify(getData.rmodel_lst));
        	//json[i] 번째 에 있는게 item 임.
        	response($.map(getData.rmodel_lst, function(item) {    
        		return {
            		label: item.robot_model,							// UI 에서 보여지는 글자, 실제 검색어랑 비교 대상
                    value: item.robot_model
                }
        	}));
        },   
        // 아이템 선택시
        select : function(event, ui) {
        	//사용자가 오토컴플릿이 만들어준 목록에서 선택을 하면 반환되는 객체
//            console.log(ui);
//            console.log(ui.item.label);
//            console.log(ui.item.value);
            
        },
        // 포커스 가면
        focus : function(event, ui) {    
        	//한글 에러 잡기용도로 사용됨
            return false;
        },
        // 최소 글자수
        minLength: 1,
        // 첫번째 항목 자동 포커스 기본값 false
        autoFocus: true, 
        classes: {
            "ui-autocomplete": "highlight"
        },
        // 검색창에 글자 써지고 나서 autocomplete 창 뜰 때 까지 딜레이 시간(ms)
//        delay: 500,    
        // 자동완성 기능 끄기
//        disabled: true, 
        position: { my : "right top", at: "right bottom" },
        // 자동완성창 닫아질때 호출
        close : function(event){    
//            console.log(event);
        }
    });
});

// 조회
function guide_api(){
	// 초기화
	$("#textMsg").html("");
	$("#textCause").html("");
	$("#textPart").html("");
	$("#textAction").html("");
	if($('#eEcode').val().length > 0){
//		var cmodel = "";
//		var radios = document.getElementsByName('eCmodel');
//		for (var i = 0, length = radios.length; i < length; i++) {
//		  if (radios[i].checked) {
//			cmodel = radios[i].value;
//		    break;
//		  }
//		};	
		var cmodel = $("#cmodelSelect").val();
		var ecode = $('#basic-addon1').text() + $('#eEcode').val();
		var baseUrl = '/api/rest/guide';
		var get_param = "?cmodel="+ cmodel + "&ecode=" + ecode;
		var url = baseUrl + get_param;
		var getData = c_get_api2(url);
		console.log('getData' + JSON.stringify(getData))
		
		// 에러코드
		$("#textError").html(cmodel);
		// 메시지
		$("#textMsg").html(ecode);
				
		var rp1 = 0;
		$("#errorMsg").html("");
		$("#causeMsg").html("");
		
		$("#accordionH").html("");		
		$("#accordionM").html("");
		
		var strHtmlH = "<div class='search-msg' data-lang='no_data'>[no_data]</div>";
		var strHtmlM = "<div class='search-msg' data-lang='no_data'>[no_data]</div>";
		
		try{
			/* 보수이력 */
			if(getData.svc_history_lst.length > 0){
				for(var i = 0; i < getData.svc_history_lst.length; i++){
					var strHtml = 
						"<div class='card mb-2'>" +
						"<div class='card-header' id='headingH" + i + "'>" +
						"<h2 class='mb-0 w-100'>" + 
						"<button type='button' class='accodianBtn btn btn-link collapsed' data-toggle='collapse' data-target='#collapseH" + i + "'>" +
						"<div class='d-flex'><div class='hist-title text-left'>" +
						getData.svc_history_lst[i].probcasenm + 
						"</div>" +
						"<div class='hist-per text-right'>" +
						getData.svc_history_lst[i].pct + "%" +
						"</div>" +
						"</div>" +
						"<div class='text-center mt-1'>" +
						"<i class='fas fa-angle-down'></i>" +
						"</div>" +
						"</button></h2></div>" +
						"<div id='collapseH" + i + "' class='collapse' aria-labelledby='headingH" + i + "' data-parent='#accordionH'>" +	
						"<div class='card-body'>" +
						"<div class='b-title text-left'><span class='accordion-subtitle' data-lang='part'>[part]</span></div>" +
						"<div class='b-content text-left'><div class='disabled-box accordion-content'>"+ getData.svc_history_lst[i].failgbnm +"</div></div>" +
						"<div class='b-title text-left'><span class='accordion-subtitle' data-lang='action'>[action]</span></div>" +
						"<div class='b-content text-left'><div class='disabled-box accordion-content'>"+ getData.svc_history_lst[i].actrtdesc +"</div></div>" +
						"</div></div>";				
					$("#accordionH").append(strHtml);
				}
			}else{
				$("#accordionH").append(strHtmlH);
			}
			

			/* 매뉴얼 */
			if(getData.svc_manual_lst.length > 0){
				for(var i = 0; i < getData.svc_manual_lst.length; i++){
					var subTitle1 = "";
					var subTitle2 = "";
					var subContent1 = "";
					var subContent2 = "";
					var htmlST1 = "";
					var htmlST2 = "";
					if(getData.svc_manual_lst[i].doc_type == 1){
						var subTitle1 = "[cause]"; //원인
						var subTitle2 = "[action]";//조치
						var subContent1 = getData.svc_manual_lst[i].desc1;
						var subContent2 = getData.svc_manual_lst[i].desc2;
						htmlST1 = "<span class='accordion-subtitle' data-lang='cause'>"+ subTitle1 +"</span>";
						htmlST2 = "<span class='accordion-subtitle' data-lang='action'>"+ subTitle2 +"</span>";
						if(rp1 == 0){
							// 에러메세지
							$("#errorMsg").html(getData.svc_manual_lst[i].msg);
							$("#causeMsg").html(getData.svc_manual_lst[i].desc1);
						}
					}else{
						var subTitle1 = "[outline]";//개요
						var subTitle2 = "[inspection_method]";//원인 및 점검방법
						var subContent1 = getData.svc_manual_lst[i].desc1;
						var subContent2 = getData.svc_manual_lst[i].section_text;
						htmlST1 = "<span class='accordion-subtitle' data-lang='outline'>"+ subTitle1 +"</span>";
						htmlST2 = "<span class='accordion-subtitle' data-lang='inspection_method'>"+ subTitle2 +"</span>";
					}
					var strHtml = 
						"<div class='card mb-2'>" +
						"<div class='card-header' id='headingM" + i + "'>" +
						"<h2 class='mb-0 w-100'>" + 
						"<button type='button' class='accodianBtn btn btn-link collapsed' data-toggle='collapse' data-target='#collapseM" + i + "'>" +
						"<div class='text-left'>" +
						getData.svc_manual_lst[i].title +
						"</div>" +
						"<div class='text-center mt-1'>" +
						"<i class='fas fa-angle-down'></i>" +
						"</div>" +
						"</button></h2></div>" +
						"<div id='collapseM" + i + "' class='collapse' aria-labelledby='headingM" + i + "' data-parent='#accordionM'>" +	
						"<div class='card-body'>" +
						"<div class='b-title text-left'>"+ htmlST1 +"</div>" +
						"<div class='b-content text-left'><div class='disabled-box accordion-content'>"+ subContent1 +"</div></div>" +
						"<div class='b-title text-left'>"+ htmlST2 +"</div>" +
						"<div class='b-content text-left'><div class='disabled-box accordion-content'>"+ subContent2 +"</div></div>" +
						"</div></div>";
					$("#accordionM").append(strHtml);
				
				}			
			}else{
				$("#accordionM").append(strHtmlM);
			}
		}catch (e) {
			$("#accordionH").append(strHtmlH);
			$("#accordionM").append(strHtmlM);
		}
		render(sessionStorage.getItem('lang'));
	}
}