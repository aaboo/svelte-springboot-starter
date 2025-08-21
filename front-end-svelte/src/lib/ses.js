import { loginTimerStore } from '$lib/stores';

const SessionCheck = function(target){
    this.target = target
    this.SessionLimitSeconds = 60*60*1; //로그인 제한 시간(초) : 1시간
    //this.SessionLimitSeconds = 5*1*1; //로그인 제한 시간(초) : 5초
    this.loginRetryBody = "";	
    this.pwChangeBody = "";	
    this.timer = null;	//로그아웃 버튼 왼쪽 타이머
    this.multiSesTimer = null;	//멀티세션 타이머
    this.init();
}

SessionCheck.prototype = {
	init : function(target){
		var ds = this;
	}
	//LPAD
	, lpad : function(str, len){
		str = str + "";
		while (str.length < len){
			str = "0" + str;
		}
		return str;
	}
	//패스워드 변경
	// , pwChange : function(){
	// 	var ds = this;
	// 	jt.ajax({
	// 		url: "/getTokn.do"
	// 		, type: "post"
	// 		, data: {
	// 			target: $("input[name=dkdlelwjwkd]").val()
	// 		}
	// 		, success: function(tokn){
	// 			ctrler.ajax({
	// 				url : "/sources/login/pwChange.html"
	// 				, method : "post"
	// 				, error : function(){
	// 					ds.init(); //재시도
	// 				}
	// 				, success : function(result){
	// 					$("body").append(_.template(result,{mode:"", userpw:"", tokn:tokn}));
	// 					$(window).resize();
	// 				}
	// 			});
	// 		}
	// 	});
	// }
	//세션연장 및 시간 초기화
	, resetTimer : function(){
		var ds = this;
		//$.post("/getSession.do", {}, function(){
			var now = new Date(); // 현재 일자 시간값을 얻어 now 변수에 할당한다.
			var yyyy = now.getFullYear(); //년도
			var mm = now.getMonth()+1;  //월(1을 더해야 한다.)
			var dd = now.getDate();        //일
			var hh24 = now.getHours(); //시간
			var mi = now.getMinutes(); //분
			var ss = now.getSeconds(); //초		
			ds.timeLimit = new Date(yyyy, mm-1, dd, hh24, mi, ss+ds.SessionLimitSeconds+1);
			clearTimeout(ds.timer);
			ds.clock();
			//ds.chkMultiSession();
		//}); 
	}
	//시간 재생 단위(초)
	, clock : function(target){
		var ds = this;		
		var now = new Date();
		var timeGapCal = 0
		timeGapCal = ds.timeLimit-now;
		var timeGap = new Date(0, 0, 0, 0, 0, 0, timeGapCal); 
		var rHour  = timeGap.getHours();      // 시
		var rMinute  = timeGap.getMinutes();      // 분
		var rSecond  = timeGap.getSeconds();      // 초
		var timeLog = ds.hmsToTime(rHour, rMinute, rSecond);
		//console.log(timeGapCal, ds.timeLimit-now, now-ds.timeLimit,  rHour, rMinute, rSecond, timeLog);
		if(timeGapCal > 0){
			//$("span[id=timeLeft]").html(timeLog||""); //header.jsp		
            ds.timeLog = timeLog;
			ds.timer = setTimeout(function(){ds.clock();}, 1000);
		}else{
			//$("span[id=timeLeft]").html("0"); //header.jsp
            ds.timeLog = timeLog = "0";
			clearTimeout(ds.timer);
			clearTimeout(ds.multiSesTimer);
			// if($("#loginRetry").length==0){
			// 	$.post("/sessionDestroy.do");
			// 	//sessionBook = null;
			// 	$("body").append(ds.loginRetryBody1); //로그인 시간이 경과
			// 	$(window).resize();
			// }
		}
		loginTimerStore.update(ds.timeLog);
        //ses.timeLog = ds.timeLog
	}
	//멀티세션 체크
	// , chkMultiSession : function(_callback){
	// 	var ds = this;
	// 	$.post("/getNowUserAuthkey.do", {}, function(result){
	// 		//console.log("비교결과: " + result);			
	// 		if(result=="SUCCESS"){
	// 		}else{
	// 			ctrler.modalClose();
	// 			//sessionBook = null;
	// 			if($("#loginRetry").length==0){
	// 				$.post("/sessionDestroy.do");
	// 				$("body").append(ds.loginRetryBody2); //다른 기기에서 사용
	// 				$(window).resize();
	// 			}
	// 		}
	// 		if(_callback) _callback.call(this, result);
	// 	});
	// }
    //시간, 분, 초를 받아 시:분:초로 변경(1,5,59-->1:05:59)
    , hmsToTime : function(hh, mi, ss){
        var result = "";
        if(hh>0){
            result += hh+":";
        }
        if(hh>0 || mi>0){
            result += (result==""?mi:(mi<10?"0"+mi:mi))+":";
        }
        result += (result==""?ss:(ss<10?"0"+ss:ss));
        return result;
    }
};

const ses = new SessionCheck();

export default ses;