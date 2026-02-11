var utils = {
	consoleLog : function(...arg){
		console.log(arg);
	}
	//parent의mousemove 이벤트를 iframe에게 전달
	//UUID생성 1밀리초 이상 떨어져 생성된 경우 100%고유
	, uuid: function(){
        return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
            (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
        );	
	}
	//브라우저 버전 체크
	, getBowserVersion: function(){ 
		var agt = navigator.userAgent.toLowerCase(); 

		if (agt.indexOf("chrome") != -1) return 'Chrome'; 
		if (agt.indexOf("opera") != -1) return 'Opera'; 
		if (agt.indexOf("staroffice") != -1) return 'Star Office'; 
		if (agt.indexOf("webtv") != -1) return 'WebTV'; 
		if (agt.indexOf("beonex") != -1) return 'Beonex'; 
		if (agt.indexOf("chimera") != -1) return 'Chimera'; 
		if (agt.indexOf("netpositive") != -1) return 'NetPositive'; 
		if (agt.indexOf("phoenix") != -1) return 'Phoenix'; 
		if (agt.indexOf("firefox") != -1) return 'Firefox'; 
		if (agt.indexOf("safari") != -1) return 'Safari'; 
		if (agt.indexOf("skipstone") != -1) return 'SkipStone'; 
		if (agt.indexOf("netscape") != -1) return 'Netscape'; 
		if (agt.indexOf("mozilla/5.0") != -1) return 'Mozilla'; 
		if (agt.indexOf("msie") != -1) {  
			var rv = -1;
			if (navigator.appName == 'Microsoft Internet Explorer') { 
				var ua = navigator.userAgent; 
				var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})"); 
				if (re.exec(ua) != null) rv = parseFloat(RegExp.$1); 
			}
			return 'IE '+rv; 
		} 
	} 
	//화면별 제목과 네비게이션 구성
	, setPage: function(_id){
		var menu = parent.tree.data.listObj
			, title = menu[_id].text
			;
		$("title").text(title);
	}
	//String Format----------------------------------------------------------
	, format : {
		//카드번호 : 1111-2222-3333-4444-55
		cardno : function(_val){
			if(!_val) return _val;
			var v = _val.toString().trim();
		    while((/(-?[0-9x]+)([0-9x]{4})/).test(v)) {
		        v = v.replace((/(-?[0-9x]+)([0-9x]{4})/), "$1-$2");
		    }
		    v = v.substr(0,7)+"XX-XXXX-"+v.substr(15);
		    return v;
		}
		//사업자번호 123-12-12345
		, bizno : function(_val){	
			if(!_val) return _val;
			var v = _val.trim();
			if(v.length != 10) return v;
			v = v.substr(0,3)+'-'+v.substr(3,2)+'-'+v.substr(5,5);
			return v;
		}
		//돈표시 3자리 콤마 추가
		, money : function(_val){
			if(!_val) return _val;
			var v = _val.trim();
		    while((/(-?[0-9]+)([0-9]{3})/).test(v)) {
		        v = v.replace((/(-?[0-9]+)([0-9]{3})/), "$1,$2");
		    }
		    return v;
		}
		//거래일자 년/월/일 : 2015/07/30
		, transdate : function(_val){	
			if(!_val) return _val;
			var v = _val.trim();
			if(v.length != 8) return v;
			v = v.substr(0,4)+'/'+v.substr(4,2)+'/'+v.substr(6,2);
			return v;
		}
		//거래시간 시분초 : 13:24:31
		, transtime : function(_val){
			if(!_val) return _val;
			var v = _val.trim();
		    while((/(-?[0-9]+)([0-9]{2})/).test(v)) {
		        v = v.replace((/(-?[0-9]+)([0-9]{2})/), "$1:$2");
		    }
		    return v;
		}
		//초단위 정수를 시:분:초 로 변경(3601-->1:00:01)
		, secondsToTime : function(_val){
			if(!_val) return _val;
			var v = parseInt(_val, 10);
			
			var hh = parseInt(v/3600, 10);
			v = v-(hh*3600);
			var mi = parseInt(v/60, 10);
			v = v-(mi*60);
			var ss = v;
			
			return this.hmsToTime(hh, mi, ss);
		}
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
		//매입방법 출력
		, ddcflagname : function(_ddcflag, _acquier){
			if(_ddcflag =="D"){
				if(_acquier=="BC") return "EDC";
				return "DDC";
			}else if(_ddcflag=="S"){
				if(_acquier=="BC") return "ESC";
				return "DESC";
			}else if(_ddcflag=="C"){
				return "ESC";
			}else if(_ddcflag=="I"){
				return "EDI";
			}else if(_ddcflag=="X"){
				return "전표매입";
			}else if(_ddcflag==""){
				return "DDCFLAG 데이터 없음!";
			}else{
				return _ddcflag;
			}	
		}
	}
	//시간관련 함수 모음
	, time : {
		getTime: function(_date, _format){
			var now = _date || new Date(); // 현재 일자 시간값을 얻어 now 변수에 할당한다.
			var yyyy=now.getFullYear(); //년도
			var yy = (yyyy+"").substr(2);
			var mm=now.getMonth()+1;  //월(1을 더해야 한다.)
			var dd=now.getDate();        //일
			var hh24 = now.getHours(); //시간
			var hh = hh24%12;
			var mi = now.getMinutes(); //분
			var ss = now.getSeconds(); //초

			mm = (mm < 10)?"0"+mm : ""+mm;
			dd = (dd < 10)?"0"+dd : ""+dd;
			hh24 = (hh24 < 10)?"0"+hh24 : ""+hh24;
			var hh = (hh < 10)?"0"+hh : ""+hh;
			mi = (mi < 10)?"0"+mi : ""+mi ;
			ss = (ss < 10)?"0"+ss : ""+ss ;


			var mmdd = mm +""+ dd;
			var yyyymm = yyyy +""+ mm;
			var yyyymmdd = yyyy +""+ mm +""+ dd;
			var yyyymmddhh24miss = yyyy +""+ mm +""+ dd +""+ hh24 +""+ mi +""+ ss;		 

			var weekArray=new Array('일','월','화','수','목','금','토'); // 배열변수 개체를 생성하며 값을 할당한다.
			var weekday=weekArray[now.getDay()];
			
			var result = _format.replace(/yyyy/g, yyyy)
										.replace(/yy/g, yy)
										.replace(/mm/g, mm)
										.replace(/dd/g, dd)
										.replace(/hh24/g, hh24)
										.replace(/hh/g, hh)
										.replace(/mi/g, mi)
										.replace(/ss/g, ss)
										.replace(/weekday/g, weekday)
										.replace(/wd/g, weekday)
										;
			try{
				return result||"";
			}catch(e){
				return "";
			}
		}
		, getTimeGap: function(_d1, _d2){
			_d1 = _d1.replace(/[^0-9]/g,'');
			_d2 = _d2.replace(/[^0-9]/g,'');
			if(!(/^([1-9])\d{3}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$/.test(_d1))) return "";
			if(!(/^([1-9])\d{3}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$/.test(_d2))) return "";
			var a1=new Date(_d1.substr(0,4),_d1.substr(4,2)-1,_d1.substr(6,2)).getTime();  
			var a2=new Date(_d2.substr(0,4),_d2.substr(4,2)-1,_d2.substr(6,2)).getTime();  
			b=(a2-a1)/(1000*60*60*24) + 1;		 
			return parseInt(b,10);
		}
	}
	/*
	* @Description: 자연수를 한글/중국어/위조방지 문자로 변환
	* @Param : Number(변환할숫자), String(언어선택:korean, chinese, bank)
	* @Return : String
	*/
	, getNumOriental: function (_num, _type){
		var type = (_type == null) ? "korean" : _type
			, num = _num.toString().replace(/[^0-9.-]/g,"").split(".")
			, num1 = num[0] //자연수
			, isMinus = num1.indexOf("-")==0
			, num1 = num1.replace(/[^0-9]/g,"")
			, num2 = num[1] //소수
			, isFloat = num2&&num2!=""
			, result = ""
			, make = function(_num, _type, _isFloat){
				//한글
				if(_type == "korean"){
					var arrLv1 = ["","일","이","삼","사","오","육","칠","팔","구"];
					var arrLv2 = ["","십","백","천"];
					var arrLv3 = ["","만","억","조","경","해","시","양","구","간","정","재","극","항하사","아승지","나유타","불가사의","무량대수"];
				}
				//한자
				else if(_type == "chinese"){
					var arrLv1 = ["","一","二","三","四","五","六","七","八","九"];
					var arrLv2 = ["","十","百","千"];
					var arrLv3 = ["","萬","億","兆","京","垓","示","穰","溝","澗","正","載","極","恒河沙","阿僧祗","那由他","不可思議","無量大數"];
				}
				//위조방지
				else if(_type =="bank"){
					var arrLv1 = ["","壹","貳","蔘","肆","伍","陸","漆","捌","玖"];
					var arrLv2 = ["","拾","伯","阡"];
					var arrLv3 = ["","萬","億","兆","京","垓","示","穰","溝","澗","正","載","極","恒河沙","阿僧祗","那由他","不可思議","無量大數"];
				}
	
				var len = _num.length;
				var num = "";
				for(var i=len; i>=1; i--){
					if(!_isFloat){
						//큰단위(만억조경해...)를 구분짓는 것 4의 배수자리마다
						if((len-i) >= 4 && ((len-i)%4) == 0){    
							num = arrLv3[(len-i)/4] + " " +num;
						}  
						//작은단위(일십백천)을 구분짓는 것 4단어 주기
						if(_num.substr(i-1,1) != 0){
							num = (len==1) ? "" : arrLv2[(len-i)%4] + num;  
						}
					}
					//통상 일 자리수 숫자를 제외한 작은단위 앞의 1은 생략
					//(처음 시작하는 1은 제외)  
					if((len-i) > 0){ 
						if(_num.substr(i-1,1) != 1 ||  i==1){
							num = arrLv1[_num.substr(i-1,1)] + num; 
						}  
					}else{
						num = arrLv1[_num.substr(i-1,1)] + num; 
					}
				}
				return num;
			}
		;
		result = (isMinus?"-" : "")
				+ make(num1, type)
				+ (isFloat?"."+make(num2, type, isFloat) : "")
				;
		
		return result;
	}
	, nvl: function nvl(_item, _txt){
		/*
		* @Description: undefined > "" 처리
		* @Param : String(체크 대상 텍스트), String(undefined 일경우 변경할 텍스트)
		* @Return : String
		*/
		_txt = _txt && _txt!=undefined ? _txt : ""
		return _item && _item!=undefined ? _item : _txt;
	}
};



(function(){
    //렌덤 UUID
    if(!crypto.randomUUID){
        Crypto.prototype.randomUUID = function(){
            return utils.uuid();
        }
    }

    //select 처리
    HTMLElement.prototype.makeSelect = function(_obj){
        /*
        * @Description: SELECT에 OPTION 추가
        * @Param : {
        * 	, list: [{code:"code", codename:"codename"}...]
        * 	, event: {click: function(ev){}}
        * }
        * @Return : Param.target
        * @사용예시
            $("#object").makeSelect({
                , list: data.POLTYPE.list
                , event: {
                    change: function(ev){
                        ds.makeSelect({
                            target: dom.poldetail
                            , list: data.POLTYPE.obj[$(this).val()].cn
                        });
                    }
                }
            }).change();
        */
        var target = this
            , list = _obj.list
            , evt = _obj.event
            , options = ""
            ;
        for(var i=0, il=list.length; i<il; i++){
            var item = list[i];
            options +=
                '<option value="'+ item.code +'">'
                +(item.code?'['+ item.code +'] ':'')
                +item.codename
                +'</option>';
        }
        target.innerHTML=options;		
        //if(evt) target.unbind().bind(evt);
		if(evt){
			for(let k in evt){
				console.log("evt", k, evt[k], evt);
				target.removeEventListener(k, {});
				target.addEventListener(k, evt[k]);
			}
		}
        return target;
    }
    String.prototype.jtNumber = function(){
        var inValue = this.toString()
            , tp = typeof(inValue)
            , regexp = /[^0-9.%\-]/g
            , result = ""
            ;
        if(tp=="string"){
            result = inValue.replace(regexp,"");
        }else if(tp=="number"){
            result = (inValue+"").replace(regexp,"");
        }
        return result;
    }; 	
    Number.prototype.jtNumber = function(){
        var inValue = this.toString()
            , tp = typeof(inValue)
            , regexp = /[^0-9.%\-]/g
            , result = ""
            ;
        if(tp=="string"){
            result = inValue.replace(regexp,"");
        }else if(tp=="number"){
            result = (inValue+"").replace(regexp,"");
        }
        return result+"";
    };
    String.prototype.money = function(){
        var txtNumber = '' + this;
        if (isNaN(txtNumber) || txtNumber == "") { return ""; }
        else {
            var rxSplit = new RegExp('([0-9])([0-9][0-9][0-9][,.])');
            var arrNumber = txtNumber.split('.');
            arrNumber[0] += '.';
            do {
                arrNumber[0] = arrNumber[0].replace(rxSplit, '$1,$2');
            } while (rxSplit.test(arrNumber[0]));
            if (arrNumber.length > 1) {
                return arrNumber.join('');
            } else {
                return arrNumber[0].split('.')[0];
            }
        }
    }
    Number.prototype.money = function(){
        var txtNumber = '' + this;
        if (isNaN(txtNumber) || txtNumber == "") { return ""; }
        else {
            var rxSplit = new RegExp('([0-9])([0-9][0-9][0-9][,.])');
            var arrNumber = txtNumber.split('.');
            arrNumber[0] += '.';
            do {
                arrNumber[0] = arrNumber[0].replace(rxSplit, '$1,$2');
            } while (rxSplit.test(arrNumber[0]));
            if (arrNumber.length > 1) {
                return arrNumber.join('');
            } else {
                return arrNumber[0].split('.')[0];
            }
        }
    }
	String.prototype.number = function(){
		var pair = this.replace(/,/g, "").split(".")
          , isMinus = !1;
        parseFloat(pair[0]) < 0 && (isMinus = !0),
        "-0" == pair[0] && (isMinus = !0);
        var returnValue = 0;
        return pair[0] = pair[0].replace(/[-|+]?[\D]/gi, ""),
        pair[1] ? (pair[1] = pair[1].replace(/\D/gi, ""),
        returnValue = parseFloat(pair[0] + "." + pair[1]) || 0) : returnValue = parseFloat(pair[0]) || 0,
        isMinus ? -returnValue : returnValue    
	}


	//AXSJ 도구
	Date.prototype.print = function(format) {
		if (void 0 == format) {
			var sSeper = "-";
			return this.getFullYear() + sSeper + (this.getMonth() + 1).setDigit(2) + sSeper + this.getDate().setDigit(2)
		}
		var nY, nM, nD, nH, nMM, nS, nDW, fStr = format;
		nY = this.getFullYear(),
		nM = (this.getMonth() + 1).setDigit(2),
		nD = this.getDate().setDigit(2),
		nH = this.getHours().setDigit(2),
		nMM = this.getMinutes().setDigit(2),
		nS = this.getSeconds().setDigit(2),
		nDW = this.getDay();
		var yre = /[^y]*(yyyy)[^y]*/gi;
		yre.exec(fStr);
		var regY = RegExp.$1
			, mre = /[^m]*(mm)[^m]*/gi;
		mre.exec(fStr);
		var regM = RegExp.$1
			, dre = /[^d]*(dd)[^d]*/gi;
		dre.exec(fStr);
		var regD = RegExp.$1
			, hre = /[^h]*(hh)[^h]*/gi;
		hre.exec(fStr);
		var regH = RegExp.$1
			, mire = /[^m]*(mi)[^i]*/gi;
		mire.exec(fStr);
		var regMI = RegExp.$1
			, sre = /[^s]*(ss)[^s]*/gi;
		sre.exec(fStr);
		var regS = RegExp.$1
			, dwre = /[^d]*(dw)[^w]*/gi;
		dwre.exec(fStr);
		var regDW = RegExp.$1;
		return "yyyy" === regY && (fStr = fStr.replace(regY, nY.right(regY.length))),
		"mm" === regM && (1 == regM.length && (nM = this.getMonth() + 1),
		fStr = fStr.replace(regM, nM)),
		"dd" === regD && (1 == regD.length && (nD = this.getDate()),
		fStr = fStr.replace(regD, nD)),
		"hh" === regH && (fStr = fStr.replace(regH, nH)),
		"mi" === regMI && (fStr = fStr.replace(regMI, nMM)),
		"ss" === regS && (fStr = fStr.replace(regS, nS)),
		"dw" == regDW && (fStr = fStr.replace(regDW, AXConfig.weekDays[nDW].label)),
		fStr
	}
	Date.prototype.date = function () {
		return this
	}
	Date.prototype.diff = function(edDate, tp) {
		var DyMilli = 864e5
			, y1 = this.getFullYear()
			, m1 = this.getMonth()
			, d1 = this.getDate()
			, hh1 = this.getHours()
			, mm1 = this.getMinutes()
			, dd1 = new Date(y1,m1,d1,hh1,mm1,this.getSeconds())
			, day2 = edDate.date()
			, y2 = day2.getFullYear()
			, m2 = day2.getMonth()
			, d2 = day2.getDate()
			, hh2 = day2.getHours()
			, mm2 = day2.getMinutes()
			, dd2 = new Date(y2,m2,d2,hh2,mm2,this.getSeconds());
		return void 0 != tp && ("D" == tp ? (DyMilli = 864e5,
		dd2 = new Date(y2,m2,d2,hh1,mm1,this.getSeconds())) : "H" == tp ? DyMilli = 36e5 : "mm" == tp ? DyMilli = 6e4 : (DyMilli = 864e5,
		dd2 = new Date(y2,m2,d2,hh1,mm1,this.getSeconds()))),
		((dd2.getTime() - dd1.getTime()) / DyMilli).floor()
	}
	String.prototype.date = function(){
        function local_date(yy, mm, dd, hh, mi, ss) {
            var utc_d, local_d;
            return local_d = new Date,
            "undefined" == typeof hh && (hh = 23),
            "undefined" == typeof mi && (mi = 59),
            utc_d = new Date(Date.UTC(yy, mm, dd || 1, hh, mi, ss || 0)),
            0 == mm && 1 == dd && utc_d.getUTCHours() + utc_d.getTimezoneOffset() / 60 < 0 ? utc_d.setUTCHours(0) : utc_d.setUTCHours(utc_d.getUTCHours() + utc_d.getTimezoneOffset() / 60),
            utc_d
        }
        if (0 == this.length)
            return defaultDate || new Date;
        if (this.length > 15) {
            var yy, mm, dd, hh, mi, aTimes, aTime, aDateTime = this.split(/ /g), aDate = aDateTime[0].split(separator || "-");
            return yy = aDate[0],
            mm = parseFloat(aDate[1]),
            dd = parseFloat(aDate[2]),
            aTime = aDateTime[1] || "09:00",
            aTimes = aTime.left(5).split(":"),
            hh = parseFloat(aTimes[0]),
            mi = parseFloat(aTimes[1]),
            "AM" !== aTime.right(2) && "PM" !== aTime.right(2) || (hh += 12),
            local_date(yy, mm - 1, dd, hh, mi)
        }
        if (14 == this.length) {
            var va = this.replace(/\D/g, "");
            return local_date(va.substr(0, 4), va.substr(4, 2).number() - 1, va.substr(6, 2).number(), va.substr(8, 2).number(), va.substr(10, 2).number(), va.substr(12, 2).number())
        }
        if (this.length > 7) {
            var va = this.replace(/\D/g, "")+"";
            return local_date(va.substr(0, 4), va.substr(4, 2).number() - 1, va.substr(6, 2).number())
        }
        if (this.length > 4) {
            var va = this.replace(/\D/g, "");
            return local_date(va.substr(0, 4), va.substr(4, 2).number() - 1, 1)
        }
        if (this.length > 2) {
            var va = this.replace(/\D/g, "");
            return local_date(va.substr(0, 4), va.substr(4, 2).number() - 1, 1)
        }
        return defaultDate || new Date
    }
	Date.prototype.add = function(daynum, interval) {
		function dayLen(y, m) {
			return [3, 5, 8, 10].indexOf(m)>-1 ? 30 : 1 == m ? y % 4 == 0 && y % 100 != 0 || y % 400 == 0 ? 29 : 28 : 31
		}
		interval = interval || "d";
		var interval = interval.toLowerCase()
			, DyMilli = 864e5
			, aDate = new Date(this.getFullYear(),this.getMonth(),this.getDate(),12);
		if ("d" == interval)
			aDate.setTime(aDate.getTime() + daynum * DyMilli);
		else if ("m" == interval) {
			var yy = aDate.getFullYear()
				, mm = aDate.getMonth()
				, dd = aDate.getDate();
			yy += parseInt(daynum / 12),
			mm += daynum % 12;
			var mxdd = dayLen(yy, mm);
			mxdd < dd && (dd = mxdd),
			aDate = new Date(yy,mm,dd,12)
		} else
			"y" == interval ? aDate.setTime(aDate.getTime() + 365 * daynum * DyMilli) : aDate.setTime(aDate.getTime() + daynum * DyMilli);
		return aDate
	}

	Number.prototype.right = function(strLen) {
        return this.toString().substring(this.toString().length - strLen, this.toString().length)
    }
	String.prototype.left = function(strLen) {
        return this.toString().substr(0, strLen)
    }
	String.prototype.times = function(count) {
        return count < 1 ? "" : new Array(count + 1).join(this)
    }
	Number.prototype.setDigit = function(length, padder, radix) {
        var string = this.toString(radix || 10);
        return (padder || "0").times(length - string.length) + string
    };

})();

