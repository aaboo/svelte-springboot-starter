package com.aaboo.svelteSpringbootStarter.common;

public class XssUtils {
	
	public static final String[] ignoreKeyword = new String[]{
//			"<" 
//			,">"
//			,"\""
//			,"%22"
			"'"
			,"%27"
//			,"%"
//			,"&"
			,"%00"
//			,"\\("
//			,"\\)"
//			,"="
			,"\\/\\*"
			,"\\*\\/"
			,"\";\\\\u"
			,"&lt;"
			,"&gt;"
			,"alert" 
			,"append" 
			,"applet" 
			,"base" 
			,"binding" 
			,"blink" 
			,"bgsound"
			,"charset"
			,"confirm" 
			,"cookie" 
			,"create"
			,"document" 
			,"embed" 
			,"eval\\("
			,"expression"
			,"frame"
			,"frameset"
			,"Href" 
			,"iframe"
			,"ilayer"
			,"innerHTML"
			,"javascript"
			,"layer" 
			,"<link" 
			,"<meta" 
			,"msgbox" 
			,"object" 
			,"refresh"
			,"script"
			,"string"
			,"<style" 
			,"<svg" 
			,"title"
			,"vbscript" 
			,"void"
			,"xml" 
			,"onabort"
			,"onactive"
			,"onafteripudate" 
			,"onafterprint"
			,"onbefore"
			,"onbeforeactivate"
			,"onbeforecopy" 
			,"onbeforecut"
			,"onbeforedeactivate" 
			,"onbeforeeditfocus"
			,"onbeforepaste" 
			,"onbeforeprint"
			,"onbeforeunload"
			,"onbeforeupdate"
			,"onbegin"
			,"onblur"
			,"onbounce"
			,"oncellchange" 
			,"onchange" 
			,"onclick" 
			,"oncontentready"
			,"oncontentsave"
			,"oncontrolselect"
			,"oncontrolselected" 
			,"oncontextmenu"
			,"oncopy"
			,"oncut" 
			,"ondataavailable"
			,"ondatasetcomplete" 
			,"ondatasetchanged" 
			,"ondblclick"
			,"ondeactivate"
			,"ondetach"
			,"ondocumentready"
			,"ondrag"
			,"ondragdrop"
			,"ondragend"
			,"ondragenter"
			,"ondragleave"
			,"ondragover"
			,"ondragstart" 
			,"ondrop"
			,"onend"
			,"onerror"
			,"onerrorupdate"
			,"onevent"
			,"onfilterchange"
			,"onfinish" 
			,"onfocus" 
			,"onfocusin" 
			,"onfocusout"
			,"onhelp"
			,"onhide"
			,"onkeydown" 
			,"onkeypress"
			,"onkeyup"
			,"onlayoutcomplete"
			,"onload"
			,"onlosecapture"
			,"onmediacomplete"
			,"onmediaerror"
			,"onmedialoadfailed"
			,"onmousedown" 
			,"onmouseend"
			,"onmouseenter"
			,"onmouseleave"
			,"onmousemove"
			,"onmouseout"
			,"onmouseover"
			,"onmouseup" 
			,"onmousewheel"
			,"onmove"
			,"onmoveend"
			,"onmovestart"
			,"onopenstatechange"
			,"onoutofsync"
			,"onpaste" 
			,"onpause"
			,"onplaystatechange"
			,"onpropertychange"
			,"onreadystatechange" 
			,"onrepeat"
			,"onreset"
			,"onresize"
			,"onresizestart"
			,"onresizeend"
			,"onresume"
			,"onreverse"
			,"onrowclick"
			,"onrowenter"
			,"onrowexit"
			,"onrowout"
			,"onrowover"
			,"onrowsdelete"
			,"onrowsinserted" 
			,"onsave"
			,"onscroll"
			,"onseek"
			,"onselect" 
			,"onselectionchange"
			,"onselectstart"
			,"onshow"
			,"onstart" 
			,"onstop"
			,"onsubmit"
			,"onsyncrestored"
			,"ontimeerror"
			,"ontrackchange"
			,"onunload"
			,"onurlflip"
			,"onwheel"
    };
	
	public static String filter(String value){
		String convertString = "";
		if(value != null && !"".equals(value)){
			for(int i=0;i<ignoreKeyword.length;i++){
				//sql DB Error, Injection
				if(",',%27,".indexOf(","+ignoreKeyword[i]+",")>-1){
					convertString = "`";
				}else if(",\\/\\*,".indexOf(","+ignoreKeyword[i]+",")>-1){
					convertString = "/＊";
				}else if(",\\*\\/,".indexOf(","+ignoreKeyword[i]+",")>-1){
					convertString = "＊/";
				}
				//그 외
				else{					
					if(ignoreKeyword[i].length()>2){
						convertString = ignoreKeyword[i].substring(0,1)+"_"+ignoreKeyword[i].substring(2);
					}else if(ignoreKeyword[i].length()==2){
						convertString = ignoreKeyword[i].substring(0,1)+"_";
					}else{
						convertString = ignoreKeyword[i];
					}
				}				
				value = value.replaceAll("(?i)"+ignoreKeyword[i], convertString).trim();
			}
		}
		return value;
	}
	
	//사용할 수 없는 키워드
	public static String getIgnoreKeyword(String value) {
		String result = null;		
		if(value!=null && !"".equals(value)) {
			value = value.toLowerCase();
			for(int i=0, il=ignoreKeyword.length; i<il; i++) {
				if(value.indexOf(ignoreKeyword[i]) > -1) {
					return ignoreKeyword[i];
				}
			}
		}
		return result;
	}
	//사용할 수 없는 키워드가 포함여부
	public static Boolean hasIgnoreKeyword(String value) {
		if(value!=null && !"".equals(value)) {
			value = value.toLowerCase();
			for(int i=0, il=ignoreKeyword.length; i<il; i++) {
				if(value.indexOf(ignoreKeyword[i]) > -1) {
					return true;
				}
			}
		}
		return false;
	}
	
}
