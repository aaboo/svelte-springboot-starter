package com.aaboo.svelteSpringbootStarter.user;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aaboo.svelteSpringbootStarter.common.SessionUtils;
import com.aaboo.svelteSpringbootStarter.common.Utils;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired private Gson gson;
	@Autowired private UserMapper userMapper;
	
	//메뉴접속 로그저장
	@SuppressWarnings("unchecked")
	public void hitMenu(HttpServletRequest req) throws Exception {
		Map<String,Object> reqParam = Utils.getRequestData(req);
		
		Map<String,String> session = gson.fromJson(gson.toJson(reqParam.get("session")), HashMap.class);
		logger.info("reqParam : {}", gson.toJson(reqParam));
		Map<String, Object> param = new HashMap<String, Object>();
		
		String userid = session.get("userid");
		String userip = session.get("userip");
		
		String pathname = reqParam.get("pathname").toString();
		String[] arrPathname = pathname.split("/");
		
		//serviceId, serviceOperation, menuType 은 필수값이다.
		String serviceId = "-";
		String serviceOperation = "-";
		String menuType = "-";		
		if(arrPathname.length==2) {
			 serviceId = arrPathname[1];
		}else if(arrPathname.length>2) {
			serviceId = arrPathname[1];
			serviceOperation = arrPathname[2];
		}
		if(arrPathname.length>=4) {
			for(int i=3, il=arrPathname.length; i<il; i++) {
				if(i==3) menuType = "";
				menuType += "/"+arrPathname[i];
			}
		}		
		
		param.put("userid", userid);
		param.put("serviceId", serviceId);
		param.put("serviceOperation", serviceOperation);
		param.put("menuType", menuType);
		param.put("logNotice", "{}");
		param.put("userip", userip);
		param.put("gubun", "MENU");
		
		userMapper.insertUserSvcHistory(param);
	}
	
	
	//APX접속 로그 저장(front-end/src/routes/test/+layout.svelte)
	public void log(HttpServletRequest req) throws Exception {
		Map<String,Object> reqParam = Utils.getRequestData(req);
		Map<String, Object> param = new HashMap<String, Object>();

		String userid = SessionUtils.getUserid(req);
		String userip = Utils.getUserIP(req);
		String logNotice = gson.toJson(reqParam);

		//logger.info("req.getRequestURI() = {}", req.getRequestURI());
		String pathname = req.getRequestURI(); //"/cleantopia/tidTrans"
		String[] arrPathname = pathname.split("/");

		String serviceId = "-";
		String serviceOperation = "-";
		String menuType = "-";		
		if(arrPathname.length==2) {
			serviceId = arrPathname[1];
		}else if(arrPathname.length>2) {
			serviceId = arrPathname[1];
			serviceOperation = arrPathname[2];
		}
		if(arrPathname.length>=4) {
			for(int i=3, il=arrPathname.length; i<il; i++) {
				if(i==3) menuType = "";
				menuType += "/"+arrPathname[i];
			}
		}

		param.put("userid", userid);
		param.put("serviceId", serviceId);
		param.put("serviceOperation", serviceOperation);
		param.put("menuType", menuType);
		param.put("logNotice", logNotice);
		param.put("userip", userip);
		param.put("gubun", "DO");

		userMapper.insertUserSvcHistory(param);
	}
	 
}
