package com.aaboo.svelteSpringbootStarter.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aaboo.svelteSpringbootStarter.common.Response;
import com.aaboo.svelteSpringbootStarter.common.ResponseCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/purchase")
public class PurchaseController {
	
	@Autowired PurchaseService purchaseService;

	@RequestMapping(value="/{reqPath}", method= {RequestMethod.POST})
	public ResponseEntity<String> PurchaseControllerEngine(HttpServletRequest req, HttpServletResponse res, @PathVariable("reqPath") String reqPath) throws Exception {
		//logger.info(reqPath+"={}", body);		
		switch(reqPath){
			case "selectPurchase": return purchaseService.selectPurchase(req); //매입처리 조회			
			default: return Response.error(ResponseCode.ERR_NONE_URL);
		}
	}
	
}
