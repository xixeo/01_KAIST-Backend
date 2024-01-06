var sessionLang;

// 세션 스토리지에 언어셋을 저장
$(function(){
	sessionLang = sessionStorage.getItem('lang');
	if(sessionStorage.getItem('lang') == null){
		changeLang("ko");
	}else{
		changeLang(sessionLang);
	}
});

// 언어 변경시 세션 스토리지에 삭제 후 재 등록 후 언어 렌더링 변경
function changeLang(lang){
	sessionStorage.removeItem('lang');
	sessionStorage.setItem('lang', lang);
	
	render(lang);
}

//언어 셋
const lang = {
		en: {
			login: 'Login',
			en: 'English',
			ko: 'Korean',
			cn: 'Chinese',
            my_page: "My Info.",
            my_logout: "Logout",
            menu_error: "Error",
            menu_all_list: "All List",
            menu_video: "Maintenance Video",
            error_msg: "Error Message",
            menu_locale: "Language",
            ecode_subtitle: "Error & Warning Help",
            video_subtitle: "Maintenance Video",
            all_subtitle: "All List",
            search: "Search",
            no_data: "No Result",
            manual: "Guidebook",
            history: "Maintenance History",
            error_code: "Error Code",
            version: "version",
            cause: "Cause",
            action: "Action",
            outline: "Summary",
            inspection_method: "Cause & Maintenance Method",
            part: "Breakdown Parts",
            add_home_icon: "Add Home Icon",
            prepare: "Preparing Page..",
            kakaoMsg: "Kakao Message Templete API"
        },
        ko: {
        	login: "로그인",
        	en: '영어',
        	ko: '한국어',
        	cn: '중국어',
            my_page: "나의정보",
            my_logout: "로그아웃",
            menu_error: "에러 도움말",
            menu_all_list: "전체목록",
            menu_video: "보수동영상",
            error_msg: "에러 메세지",
            menu_locale: "언어",
            ecode_subtitle: "에러 및 경고 도움말",
            video_subtitle: "보수동영상",
            all_subtitle: "전체목록",
            search: "조회",
            no_data: "조회된 데이터가 없습니다",
            manual: "매뉴얼",
            history: "보수이력",
            error_code: "에러번호",
            version: "버전",
            cause: "원인",
            action: "조치",
            outline: "개요",
            inspection_method: "원인 및 점검방법",
            part: "고장부위",
            add_home_icon: "홈 바로가기 추가",
            prepare: "준비중입니다..",
            kakaoMsg: "카카오 메세지 API"
        },
        cn: {
        	login: "登录",
        	en: '英语',
        	ko: '韩国语',
        	cn: '汉语',
            my_page: "我的信息",
            my_logout: "注销",
            menu_error: "错误提示语",
            menu_all_list: "总目录",
            menu_video: "保守视频",
            error_msg: "错误信息",
            menu_locale: "语言",
            ecode_subtitle: "错误及警告提示",
            video_subtitle: "保守视频",
            all_subtitle: "总目录",
            search: "查询",
            no_data: "已查询的数据不存在。",
            manual: "手册",
            history: "维修履历",
            error_code: "错误编号",
            version: "版本",
            cause: "原因",
            action: "措施",
            outline: "纲要",
            inspection_method: "原因及检验方法",
            part: "故障部位",
            add_home_icon: "追加直接进入本垒",
            prepare: "正在准备中。",
            kakaoMsg: "카카오 메세지 API"
        }
}

// 언어별 랜더링
function render(locale) {
    const $lang = document.querySelectorAll("[data-lang]")
    $lang.forEach(el => {
        const code = el.dataset.lang
        el.textContent = lang[locale][code]
    })
}