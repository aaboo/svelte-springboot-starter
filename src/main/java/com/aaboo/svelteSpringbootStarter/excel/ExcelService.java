/**
 * @author aaboo
 * @Description 웹에서 JSON을 요청하여 엑셀을 만들 수 있음
 * Fixed형 : 일반적인 텍스트를 출력(출력 영역이 고정이다)
 * Grid형 : DB자료를 출력(출력 영역이 변형될 수 있다-)
 */
package com.aaboo.svelteSpringbootStarter.excel;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import com.aaboo.svelteSpringbootStarter.common.ModifiableHttpServletRequest;
import com.aaboo.svelteSpringbootStarter.common.Utils;
import com.aaboo.svelteSpringbootStarter.common.XssUtils;
import com.google.gson.Gson;

@Service
@SuppressWarnings("deprecation")
public class ExcelService {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

	@Autowired private SqlSession sqlSession;
	@Autowired private Gson gson;

	//생성자
	public ExcelService(){}
	public ExcelService(SqlSession sqlSession, Gson gson, DataSourceTransactionManager txManager){
		if(this.sqlSession==null) this.sqlSession = sqlSession;
		if(this.gson==null) this.gson = gson;
	}
	
	//properties 정보 불러오기
	private static String fileImgPath;//file.img.path
	
	//static의 properties값 가져오기는 아래와 같이 
	@Value("${file.img.path}")
	private void setFileImgPath(String fileImgPath){
		ExcelService.fileImgPath = fileImgPath;
	}
	
	//javascript 엔진 불러오기
	private static ScriptEngineManager scriptEngineManager = new ScriptEngineManager(); 
	private static ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");
	private static Pattern pattern1 = Pattern.compile("\\{(.*?)\\}");
	
	//공유정보
	private ExcelCellStyle cs;
	private Map<String, String> jtBasicInfo = null; //본사기본정보
	private List<Map<String, String>> dataList;
	private Map<String, String> dataListObj;
	List<String> rowspanCheckList = new ArrayList<String>(); //rowspan, coslpan 중복여부 체크리스트
	Map<String, String> mergeCheck = null; //tdConfig.merge 중복여부 체크
	private int x = 0;
	private int y = 0;
	
	/***********************************************************************************************************************
	엑셀 다운로드 CORE 기능 관련
	***********************************************************************************************************************/
	//엑셀 다운로드
	@SuppressWarnings("unchecked")
	public void toExcelComplete(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//엑셀다운로드 세션
		HttpSession ses = req.getSession();
		ses.setAttribute("excelDownloadStatus","");
		
		//파라미터 받아옴
		String excelList = XssUtils.filter(req.getParameter("excel"));
		//logger.info(gson.toJson(excelList));
		
		//excelList (workbook과 같은 레벨)
		Map<String, Object> excelListJson = gson.fromJson(excelList, Map.class);
		
		//엑셀파일명
		String fileName = excelListJson.get("fileName").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String filename = fileName.replaceAll(" ","_") +"_"+ sdf.format(new Date())+ ".xlsx";
    	String filenameEnc = URLEncoder.encode(filename, "UTF-8");
    	filenameEnc = filenameEnc.replace("%28", "(");
    	filenameEnc = filenameEnc.replace("%29", ")");
    	filenameEnc = filenameEnc.replace("%2F", "-");
		
		//엑셀객체 생성(최상위 workbook > sheet > row > cell)
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);		
				
		//Param: sheetList (다중 시트가 들어갈 수 있음)
		List<Object> sheetList = (List<Object>) excelListJson.get("sheetList");
		//logger.info("sheetList="+ gson.toJson(sheetList));

    	try{
			
			//다중 시트별로 작업 시작
			for(int i=0, il=sheetList.size(); i<il; i++){
				//logger.info("sheetList["+i+"]");
				
				//sheetList Parse
				Map<String, String> sheetListParse = gson.fromJson(gson.toJson(sheetList.get(i)), Map.class);
				//logger.info("printList["+ i +"]="+ gson.toJson(sheetListObj));
				
				//sheetName
				String sheetName = sheetListParse.get("sheetName").toString();
				//logger.info("sheetName="+ sheetName);
				
				//시트객체 생성(이제부터는 셀을 그릴 수 있음)
				Sheet sheet = workbook.createSheet(sheetName);
				cs = new ExcelCellStyle(workbook);

				//시트 인쇄범위설정
				sheet.setAutobreaks(true);
				XSSFPrintSetup print = (XSSFPrintSetup) sheet.getPrintSetup();
				print.setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);
				print.setFitHeight((short)1);
				print.setFitWidth((short)1);
					//사용자 정의
					Short scale = sheetListParse.get("scale")!=null?((Double)((Object) sheetListParse.get("scale"))).shortValue():100;
					Boolean landscape = sheetListParse.get("landscape")!=null?String.valueOf(sheetListParse.get("landscape")).equalsIgnoreCase("TRUE")?true:false:false;					
					print.setLandscape(landscape);
					print.setScale(scale);
					
				//시트 설정값 초기화
				rowspanCheckList.clear();
				x = 0;
				y = 0;
				mergeCheck = new HashMap<String, String>(); //tdConfig.merge 중복여부 체크
				
				//----------------------------------------------------------------------
				
				//printList : 출력양식(출력순서, mode 별로 구분된다.)
				List<Object> printList = (List<Object>) gson.fromJson(gson.toJson(sheetListParse.get("printList")), List.class);
				//logger.info("printList="+ gson.toJson(printList));
		    		
				for(int j=0, jl=printList.size(); j<jl; j++){
					//printList Parse
					Map<String, String> printObjParse = gson.fromJson(gson.toJson(printList.get(j)), Map.class);
					//logger.info("printObjParse["+j+"]="+ gson.toJson(printObjParse));
					
					//mode 추출(fixed: 일반적인 글을 출력 /grid: 그리드형 DB 자료를 출력함)
					String type = null;
					if(printObjParse.get("type")==null){
						type = "FIXED";
					}else{
						type = printObjParse.get("type").toString();
					}
					//logger.info("printObjParse["+j+"].mode="+ mode);
					
					
					//본사기본정보 불러오기
					//청구공문상에서 feecode를 가지고 올 경우가 발생하여, 페이지마다 검색하기로 했다.
					//if(jtBasicInfo==null){
						//클래스 찾기
			    		String klassPackage = "com.aaboo.svelteSpringbootStarter";
			    		String className = "bank.BankService";
			    		String methodName = "selectCfBankJtinfo";
			    		//logger.info(klassPackage+"."+className);
			    		
			        	Class<?> klass = Class.forName(klassPackage+"."+className);
			        	Constructor<?> constructor = klass.getConstructor(new Class[]{SqlSession.class, Gson.class});//생성자 파라미터 추가
		        		Object klassInstance = constructor.newInstance(new Object[]{sqlSession, gson});   
			        	//logger.info(klassPackage+"."+className);
			        	
			        	//클래스에서 메써드 찾기
			        	for(int k=0, kl=klass.getMethods().length; k<kl; k++){
			        		//메써드를 찾았다!
			        		if(klass.getMethods()[k].getName().equals(methodName)){
			        			
			        			//req.setParameter
			        			ModifiableHttpServletRequest mReq= new ModifiableHttpServletRequest(req);
			        			Map<String, String> param = gson.fromJson(gson.toJson(printObjParse.get("param")),Map.class);
			        			if(param!=null){
			        				for(Map.Entry<String, String> entry : param.entrySet()){
			        					String key = entry.getKey();
			        					String value = null;
			        					Object sValue = entry.getValue();
			    						if(sValue.getClass().getName().equals("java.lang.Double")){
			    							value = String.valueOf(((Double) sValue).intValue());
			    						}else if(sValue.getClass().getName().equals("java.lang.Boolean")){
			    							value = String.valueOf(((Boolean) sValue));
			    						}else{
			    							value = entry.getValue();
			    						}
			        					mReq.setParameter(key, value);
			        				}
			        				req = (HttpServletRequest) mReq;
			        				
			        				//서비스의 쿼리 실행
			        				ResponseEntity<String> result = (ResponseEntity<String>) klass.getMethods()[k].invoke(klassInstance, new Object[]{req});
			        				Map<String, String> result1 = gson.fromJson(result.getBody(), Map.class);
			        				Map<String, Object> result2 = gson.fromJson(result1.get("result"), Map.class);
			        				jtBasicInfo = gson.fromJson(gson.toJson(result2.get("data")), Map.class);			        				
			        			}
					    		//logger.info("jtBasicInfo : "+ gson.toJson(jtBasicInfo));
			        			break;
			        		}
			        	}
					//}
					
					//공통 폰트사이즈 조정
					Object oFontSize = printObjParse.get("defaultFontSize");
					short sFontSize = 0;					
					if(oFontSize!=null){
						if(oFontSize.getClass().getName().equals("java.lang.Double")){
							sFontSize = ((Double) oFontSize).shortValue();
						}else{
							sFontSize = Short.parseShort(((String) oFontSize).replaceAll("[^0-9]", ""));
						}
					}else{
						sFontSize = 9;
					}
					cs.setDefaultFontSize(sFontSize);
					
					//fixed와 grid의 분기 처리
					if(type.equalsIgnoreCase("FIXED")){
						this.doFixed(req, sheet, printObjParse);
					}else if(type.equalsIgnoreCase("GRID")){
						this.doGrid(req, sheet, printObjParse);
					}
					
				}//for(int j=0, jl=printList.size(); j<jl; j++){
				
			}//for(int i=0, il=sheetList.size(); i<il; i++){

    	}catch(Exception e){
    		System.out.println("=================================================================");
    		logger.info("");
    		System.out.println("엑셀 변환 오류");
    		e.printStackTrace();
    	}finally{
    		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	    	workbook.write(outByteStream);
	    	byte[] outArray = outByteStream.toByteArray();       
	    	res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    	res.setHeader("Expires", "0");
	    	res.setHeader("Content-Disposition",  "attachment; filename=\""+ filenameEnc +"\""); 	    	
	    	res.setContentLength(outArray.length);
	    	OutputStream outStream = res.getOutputStream();
	    	
	    	outStream.write(outArray);
	    	outStream.flush();
	    	workbook.dispose();
	    	workbook.close();
	    	outStream.close();
	    	outByteStream.close();		
	    	
    		//공유정보
    		rowspanCheckList.clear();
    		mergeCheck = null;
    		//EXCEL 기본정보
    		cs = null;
    		workbook = null;
    		dataList = null;
    		dataListObj = null;
    		x = 0;
    		y = 0;
    	}

		ses.setAttribute("excelDownloadStatus","FINISHED"); //파일 다운로드 세션 완료 처리
	}
	/***********************************************************************************************************************
	MODE FIXED 엑셀 출력
	***********************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void doFixed(HttpServletRequest req, Sheet sheet, Map<String, String> printObj) throws Exception{

		List<Map<String, String>> configList = (List<Map<String, String>>) gson.fromJson(gson.toJson(printObj.get("config")), List.class);
		String pk = printObj.get("pk"); 
		Map<String, String> data  = null; 
		
		//기본자료 또는 사용자정의 자료 추출
		if(printObj.get("klass")!=null && printObj.get("method")!=null){
			if(
					printObj.get("klass").equalsIgnoreCase("bank.BankService") 
					&& printObj.get("method").equalsIgnoreCase("selectCfBankJtinfo")
					&& jtBasicInfo!=null
					&& printObj.get("feecode")==null
			){
				//기본정보
				data = jtBasicInfo;
			}else{
				//거래명세서 자료 추출
	    		String klassPackage = "com.jtnet.jcf";
	    		String className = printObj.get("klass").toString();
	    		String methodName = printObj.get("method").toString();
	    		
	        	Class<?> klass = Class.forName(klassPackage+"."+className);
	        	Constructor<?> constructor = klass.getConstructor(new Class[]{SqlSession.class, Gson.class});//생성자 파라미터 추가
	    		Object klassInstance = constructor.newInstance(new Object[]{sqlSession, gson}); 
	    		
	    		//클래스에서 메써드 찾기
	        	for(int k=0, kl=klass.getMethods().length; k<kl; k++){
	        		//메써드를 찾았다!
	        		if(klass.getMethods()[k].getName().equals(methodName)){
	                	
	            		//req.setParameter
	        			ModifiableHttpServletRequest mReq= new ModifiableHttpServletRequest(req);
	        			Map<String, String> param = gson.fromJson(gson.toJson(printObj.get("param")),Map.class);
	        			for(Map.Entry<String, String> entry : param.entrySet()){
	        				String key = entry.getKey();
        					String value = null;
        					Object sValue = entry.getValue();
    						if(sValue.getClass().getName().equals("java.lang.Double")){
    							value = String.valueOf(((Double) sValue).intValue());			    							
    						}else if(sValue.getClass().getName().equals("java.lang.Boolean")){
    								value = String.valueOf(((Boolean) sValue));			    							
    						}else{
    							value = entry.getValue();
    						}
	        				mReq.setParameter(key, value);
	        			}
	        			req = (HttpServletRequest) mReq;
	        			
	        			//서비스의 쿼리 실행
	        			ResponseEntity<String> result = (ResponseEntity<String>) klass.getMethods()[k].invoke(klassInstance, new Object[]{req});
	        	    	Map<String, String> result1 = gson.fromJson(result.getBody(), Map.class);
	    	    		Map<String, Object> result2 = gson.fromJson(result1.get("result"), HashMap.class);

	    	    		//실사용 자료(dataList: 레코드셋, dataSum: 합계)
	    	    		this.dataList = gson.fromJson(gson.toJson(result2.get("list")), List.class);
//	        	    	dataSum = gson.fromJson(gson.toJson(result2.get("sum")), HashMap.class);
//	        	    	dataSubHeader = gson.fromJson(gson.toJson(result2.get("subheader")), HashMap.class);
	    	    		//logger.info(pk+":"+ gson.toJson(dataList));
	    	    		//pk설정이 있을 경우, List자료를 pk기준으로 Map으로 만듦 
	    	    		if(pk!=null && !pk.equalsIgnoreCase("") && this.dataList!=null){
	    	    			this.dataListObj = new HashMap<String,String>();
		    	    		for(int i=0, il=this.dataList.size(); i<il; i++){
		    	    			if(this.dataList.get(i)!=null && this.dataList.get(i).get(pk)!=null){
		    	    				this.dataListObj.put(this.dataList.get(i).get(pk).toString(), gson.toJson(this.dataList.get(i)));
		    	    			}
		    	    		}
	    	    		}else{
	    	    			data = this.dataList.get(0); 
	    	    		}
	        			break;
	        		}
	        	}
			}
		}
		for(int i=0, il=configList.size(); i<il; i++){
			Row row = (sheet.getRow(y)==null?sheet.createRow(y):sheet.getRow(y));
			Map<String, String> rowConfig = gson.fromJson(gson.toJson(configList.get(i).get("row")),Map.class);
			List<Map<String, String>> colConfig = gson.fromJson(gson.toJson(configList.get(i).get("col")), List.class);
			this.toExcelRenderRow(
					sheet
					, row
					, rowConfig //Map<String, String>
					, colConfig //List<Map<String, String>>
					, data
					, 0
				);
			y++;
		}
		
		//넓이 조정
		this.setWidth(sheet, printObj);
	}
	/***********************************************************************************************************************
	MODE GRID 엑셀 출력
	***********************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void doGrid(HttpServletRequest req, Sheet sheet, Map<String, String> printObj) throws Exception{

		//DB 데이터 추출
		Map<String, String> dataSum = null;
		Map<String, String> dataSubHeader = null;
		if(printObj.get("klass")!=null && printObj.get("method")!=null){
			//클래스 찾기
    		String klassPackage = "com.jtnet.jcf";
    		String className = printObj.get("klass").toString();
    		String methodName = printObj.get("method").toString();
    		//logger.info(klassPackage+"."+className);
    		
        	Class<?> klass = Class.forName(klassPackage+"."+className); //"com.jtnet.webddc."+className
        	Constructor<?> constructor = klass.getConstructor(new Class[]{SqlSession.class, Gson.class});//생성자 파라미터 추가
    		Object klassInstance = constructor.newInstance(new Object[]{sqlSession, gson});   
        	//logger.info(klassPackage+"."+className);
        	
        	//클래스에서 메써드 찾기
        	for(int k=0, kl=klass.getMethods().length; k<kl; k++){
        		//메써드를 찾았다!
        		if(klass.getMethods()[k].getName().equals(methodName)){
                	
            		//req.setParameter
        			ModifiableHttpServletRequest mReq= new ModifiableHttpServletRequest(req);
        			Map<String, String> param = gson.fromJson(gson.toJson(printObj.get("param")),Map.class);
        			for(Map.Entry<String, String> entry : param.entrySet()){
        				String key = entry.getKey();
    					String value = null;
    					Object sValue = entry.getValue();
						if(sValue.getClass().getName().equals("java.lang.Double")){
							value = String.valueOf(((Double) sValue).intValue());			    							
						}else{
							value = entry.getValue();
						}
        				mReq.setParameter(key, value);
        			}
        			req = (HttpServletRequest) mReq;
        			
        			//서비스의 쿼리 실행
        			ResponseEntity<String> result = (ResponseEntity<String>) klass.getMethods()[k].invoke(klassInstance, new Object[]{req});
        	    	Map<String, String> result1 = gson.fromJson(result.getBody(), Map.class);
    	    		Map<String, Object> result2 = gson.fromJson(result1.get("result"), HashMap.class);
    	    		
    	    		//실사용 자료(dataList: 레코드셋, dataSum: 합계)
    	    		this.dataList = gson.fromJson(gson.toJson(result2.get("list")), List.class);
    	    		//logger.info("dataList : "+ gson.toJson(dataList));
        	    	dataSum = gson.fromJson(gson.toJson(result2.get("sum")), HashMap.class);
        	    	dataSubHeader = gson.fromJson(gson.toJson(result2.get("subheader")), Map.class);
					//logger.info("grid.dataList: "+ gson.toJson(dataList));
        			break;
        		}
        	}
		}
		
		//사용자 정의 시작
		List<Map<String, String>> header = gson.fromJson(gson.toJson(printObj.get("header")), List.class);
		String displayHeader = String.valueOf(printObj.get("displayHeader")); //헤더를 렌더링하지 않는 옵션
		List<Map<String, String>> config = gson.fromJson(gson.toJson(printObj.get("config")), List.class);
		List<Map<String, String>> footer = gson.fromJson(gson.toJson(printObj.get("footer")), List.class);
		List<Map<String, String>> group = gson.fromJson(gson.toJson(printObj.get("group")), List.class);
		
		//그룹설정(group) 정리
		/* 그룹은 JSON 형식으로 아래와 같은 구조로 되어 있다. 
		 * 사용자가 정의한 group 코드를, JAVA로 파싱하여 groupList에 담는다.
			group :  [
				{
					keys : ["key1", "key2",...]
					, marker : [
						{
							row:{}
							, col: [
								{key: "key1", label:"label1"...}
								, {...}, ...
							]
						}
						, {...},  ...
					]
				}
				, {...}, ...
			]
			
		 * JAVA 파싱 후 샘플코드
		 * [{"keys":["mname","transdate"],"marker":[[{"label":"{transdate}소계","align":"center","type":"label","key":"mname"},{"label":"건수","type":"money","key":"totcnt"},{"label":"금액","type":"money","key":"totamt"},{"label":"건수","type":"money","key":"usncnt"},{"label":"금액","type":"money","key":"usnamt"},{"label":"건수","type":"money","key":"uvncnt"},{"label":"금액","type":"money","key":"uvnamt"}]]},{"keys":["mname"],"marker":[[{"label":"{mname}소계","align":"center","type":"label","key":"mname"},{"label":"건수","type":"money","key":"totcnt"},{"label":"금액","type":"money","key":"totamt"},{"label":"건수","type":"money","key":"usncnt"},{"label":"금액","type":"money","key":"usnamt"},{"label":"건수","type":"money","key":"uvncnt"},{"label":"금액","type":"money","key":"uvnamt"}]]}]
		 */
		List<Map<String, Object>> groupList = null;
		if(group!=null){
			groupList = new ArrayList<Map<String, Object>>(); //사용자 정의 담을 것
			//logger.info(gson.toJson(group));			
			for(int i=0, il=group.size(); i<il; i++){
				HashMap<String, String> groupItem = gson.fromJson(gson.toJson(group.get(i)), HashMap.class); //사용자 정의 파싱
				HashMap<String, Object> groupListItem = new HashMap<String, Object>(); //사용자 정의 담을 것			
				
				//keys 구하기
				groupListItem.put("keys", gson.fromJson(gson.toJson(groupItem.get("keys")), List.class));
				
				//marker 구하기
				List<List<HashMap<String,String>>> groupMarker = gson.fromJson(gson.toJson(groupItem.get("marker")), List.class); //사용자 정의 파싱
				List<List<HashMap<String, String>>> groupListMarker = new ArrayList<List<HashMap<String, String>>>(); //사용자 정의 담을 것
				for(int j=0, jl=groupMarker.size(); j<jl; j++){
					List<HashMap<String,String>> groupMarkerParsed = gson.fromJson(gson.toJson(groupMarker.get(j)), List.class); //사용자 정의 파싱
					List<HashMap<String, String>> groupListMarkerParsed  = new ArrayList<HashMap<String, String>>(); //사용자 정의 담을 것
					for(int jj=0, jjl=groupMarkerParsed.size(); jj<jjl; jj++){
						HashMap<String,String> groupMarkerParsedList = gson.fromJson(gson.toJson(groupMarkerParsed.get(jj)), HashMap.class); //사용자 정의 파싱
						groupListMarkerParsed.add(gson.fromJson(gson.toJson(groupMarkerParsedList), HashMap.class));
					}
					groupListMarker.add(groupListMarkerParsed);
				}
				groupListItem.put("marker", groupListMarker);
				
				//groupList 추가
				groupList.add(groupListItem);
			}
		}

		//최초 row설정
		Row row = (sheet.getRow(y)==null?sheet.createRow(y):sheet.getRow(y));
		
		/***************************************************************
		 * 테이블 헤더
		 ***************************************************************/
		//header설정이 있을 경우
		if(header!=null){
			//sub 헤더 값 구하기
			Map<String, String> subData = null;
			if(dataSubHeader != null){
				subData = dataSubHeader;
			}else {
				subData = null;
			}
			for(int h=0, hl=header.size(); h<hl; h++){
				Map<String, String> cfgHeader = gson.fromJson(gson.toJson(header.get(h)), Map.class);
				Map<String, String> rowConfig = gson.fromJson(gson.toJson(cfgHeader.get("row")), Map.class);
				List<Map<String, String>> colConfig = gson.fromJson(gson.toJson(cfgHeader.get("col")), List.class);
//				logger.info("cfgHeader: "+ gson.toJson(cfgHeader));
//				logger.info("tr: "+ gson.toJson(tr));
//				logger.info("td: "+ gson.toJson(td));
//				logger.info("trPart: "+ trPart);
				this.toExcelRenderRow(
					sheet
					, row
					, rowConfig
					, colConfig //header/config/footer/group[{}...]
					, subData //Object dataList
					, 0
				);
				y++;
			}
		}else{
			if(displayHeader!=null && !displayHeader.equalsIgnoreCase("false")){
				for(int h=0, hl=config.size(); h<hl; h++){
					Map<String, String> cfgHeader = gson.fromJson(gson.toJson(config.get(h)), Map.class);
					Map<String, String> rowConfig = gson.fromJson(gson.toJson(cfgHeader.get("row")), Map.class);
					List<Map<String, String>> colConfig = gson.fromJson(gson.toJson(cfgHeader.get("col")), List.class);
	//				logger.info("cfgHeader: "+ gson.toJson(cfgHeader));
	//				logger.info("tr: "+ gson.toJson(tr));
	//				logger.info("td: "+ gson.toJson(td));
	//				logger.info("trPart: "+ trPart);
					
					if(rowConfig==null) rowConfig = new HashMap<String,String>();
					rowConfig.put("part", "header");
					
					this.toExcelRenderRow(
						sheet
						, row
						, rowConfig
						, colConfig //header/config/footer/group[{}...]
						, null //Object dataList
						, 0
					);
					y++;
				}
			}
		}
		/***************************************************************
		 * 테이블 바디
		 ***************************************************************/
		logger.info("dataList={}", gson.toJson(dataList));
		//rowspanCheckList.clear(); //rowspan 테스트
		List<String> groupCheckList = new ArrayList<String>(); //group이 사용될 경우 group.keys[key, key...]를 기반으로 marker[[],[]...]를 생성.
		List<Integer> groupCheckStartIndex = new ArrayList<Integer>(); //group이 사용될 경우 소계/합계를 만들기 위한 인덱스 범위 저장				
		for(int i=0, il=dataList.size(); i<il; i++){
			Map<String, String> data = gson.fromJson(gson.toJson(dataList.get(i)), Map.class);
			
			//그룹
			if(groupList != null){
				if(i==0){
					for(int b=0, bl=groupList.size(); b<bl; b++){
						List<String> keys = (List<String>) groupList.get(b).get("keys");
						StringBuilder groupCheckListItem = new StringBuilder();
						groupCheckListItem.append(",");
						for(int bb=0, bbl=keys.size(); bb<bbl; bb++){
							groupCheckListItem.append(data.get(keys.get(bb))).append(",");
						}
						groupCheckList.add(groupCheckListItem.toString());
						groupCheckStartIndex.add(i); //그룹 소계 시작 인덱스
					}
				}
				
				for(int b=0, bl=groupList.size(); b<bl; b++){
					//HashMap<String, Object> group = (HashMap<String, Object>) groupList.get(c);
					List<String> keys = (List<String>) groupList.get(b).get("keys");
					List<Map<String, String>> marker = (List<Map<String, String>>) groupList.get(b).get("marker");
					StringBuilder groupCheckListItem = new StringBuilder();
					groupCheckListItem.append(",");
					for(int bb=0, bbl=keys.size(); bb<bbl; bb++){
						groupCheckListItem.append(data.get(keys.get(bb))).append(",");
					}
//					logger.info(groupCheckList.get(b) +" == "+ groupCheckListItem.toString());
					if(!groupCheckList.get(b).equals(groupCheckListItem.toString())){
						//이 부분에서 그룹 추가됨
						//SUM구하기
						Map<String, String> sumData = this.getSum(dataList,groupCheckStartIndex.get(b), i, marker);
						groupCheckStartIndex.set(b, i);//그룹 소계 시작 인덱스
						
						//그룹 렌더링
						for(int f=0, fl=marker.size(); f<fl; f++){
							Map<String, String> markerBody = gson.fromJson(gson.toJson(marker.get(f)), Map.class);
							Map<String, String> rowConfig = gson.fromJson(gson.toJson(markerBody.get("row")), Map.class);
							List<Map<String, String>> colConfig = gson.fromJson(gson.toJson(markerBody.get("col")), List.class);
							row = (sheet.getRow(y)==null?sheet.createRow(y):sheet.getRow(y));
							x = 0;
							this.toExcelRenderRow(
									sheet //Sheet sheet
									, row //Row row
									, rowConfig
									, colConfig
									, sumData
									, i
									);
							y++;
						}
						data = gson.fromJson(gson.toJson(dataList.get(i)), Map.class);
						groupCheckList.set(b, groupCheckListItem.toString());
					}
				}
			}
			
			//그룹 외
			for(int j=0, jl=config.size(); j<jl; j++){
				Map<String, String> cfgBody = gson.fromJson(gson.toJson(config.get(j)), Map.class);
				Map<String, String> rowConfig = gson.fromJson(gson.toJson(cfgBody.get("row")), Map.class);
				List<Map<String, String>> colConfig = gson.fromJson(gson.toJson(cfgBody.get("col")), List.class);
				
				if(rowConfig==null) rowConfig = new HashMap<String,String>();
				//rowConfig.put("part",  "body");

				row = (sheet.getRow(y)==null?sheet.createRow(y):sheet.getRow(y));
				this.toExcelRenderRow(
						sheet //Sheet sheet
						, row //Row row
						, rowConfig
						, colConfig
						, data //
						, i
					);
				y++;
			}
			
			//그룹 마무리
			if(groupList != null && i==dataList.size()-1){
				for(int b=0, bl=groupList.size(); b<bl; b++){
					List<Map<String, String>> marker = (List<Map<String, String>>) groupList.get(b).get("marker");
					Map<String, String> sumData = this.getSum(dataList, groupCheckStartIndex.get(b), i+1, marker);
//					logger.info(groupCheckStartIndex.toString());
//					logger.info(groupCheckStartIndex.get(b).toString() +"~"+ i);
//					logger.info(sumData.toString());
					groupCheckStartIndex.set(b, i);//그룹 소계 시작 인덱스
					for(int f=0, fl=marker.size(); f<fl; f++){
						Map<String, String> markerBody = gson.fromJson(gson.toJson(marker.get(f)), Map.class);
						Map<String, String> rowConfig = gson.fromJson(gson.toJson(markerBody.get("row")), Map.class);
						List<Map<String, String>> colConfig = gson.fromJson(gson.toJson(markerBody.get("col")), List.class);
						row = (sheet.getRow(y)==null?sheet.createRow(y):sheet.getRow(y));
						x = 0;
						this.toExcelRenderRow(
								sheet //Sheet sheet
								, row //Row row
								, rowConfig
								, colConfig
								, sumData
								, i
							);
					}
					y++;
				}
			}
		}
		/***************************************************************
		 * Footer 그리드 하단
		 ***************************************************************/        	    	
		if(footer!=null){
			//SUM구하기
			Map<String, String> sumData = null;
			if(dataSum!=null){
				sumData = dataSum;
			}else{
				sumData = this.getSum(dataList, 0, dataList.size(), footer);
			}
			
			//Footer 렌더링
			rowspanCheckList.clear();
			for(int f=0, fl=footer.size(); f<fl; f++){
				Map<String, String> footerBody = gson.fromJson(gson.toJson(footer.get(f)), Map.class);
				Map<String, String> rowConfig = gson.fromJson(gson.toJson(footerBody.get("row")), Map.class);
				List<Map<String, String>> colConfig = gson.fromJson(gson.toJson(footerBody.get("col")), List.class);
				row = (sheet.getRow(y)==null?sheet.createRow(y):sheet.getRow(y));
				x = 0;
				this.toExcelRenderRow(
						sheet //Sheet sheet
						, row //Row row
						, rowConfig
						, colConfig
						, sumData
						, dataList.size() //int i //resultBody 인덱스
					);
				y++;
			}
		}
		//셀넓이 조정
		this.setWidth(sheet, printObj);
	}
	/***********************************************************************************************************************
	//toExcelComplex 엑셀 Row 렌더링 처리 공통기능
	***********************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void toExcelRenderRow(
		Sheet sheet
		, Row row
		, Map<String, String> rowConfig
		, List<Map<String, String>> colConfigList
		, Map<String, String> data
		, int dataSeq
	){
		
		//row(y)와 cell(x) 좌표 초기화 및 설정
		x = 0;
		StringBuilder sb = new StringBuilder();

		//데이터 변형: List, Map 두가지 유형이 있다.
		
//		logger.info("cfgList: "+ gson.toJson(cfgList));
//		logger.info("trPart: "+ trPart);
//		logger.info("colConfigList: "+ colConfigList);
		for(int c=0, cl=colConfigList.size(); c<cl; c++){
			
			Map<String, String> colConfig = gson.fromJson(gson.toJson(colConfigList.get(c)), Map.class);
			
			//거래명세서에서 사용되는 row.key 가 있으면 자료 추출방식이 아래와 같다.
			if(this.dataListObj!=null && rowConfig!=null && rowConfig.get("key")!=null){
				data = gson.fromJson(this.dataListObj.get(rowConfig.get("key")), Map.class);
				//logger.info(rowConfig.get("key")+":"+c+": "+gson.toJson(data));
			}
			
			//Offset 있을 경우 x, y 재설정
//			logger.info(gson.toJson(colConfig));
			if(colConfig.get("offsetY")!=null){
				y = ((Double) ((Object) colConfig.get("offsetY"))).intValue();
			}
			if(colConfig.get("offsetYAdd")!=null){
				y += ((Double) ((Object) colConfig.get("offsetYAdd"))).intValue();
			}			
			if(colConfig.get("offsetX")!=null) x = ((Double) ((Object) colConfig.get("offsetX"))).intValue();
			
			//Row 체크 및 재설정
			if(row.getRowNum()!=y) row = (sheet.getRow(y)==null?sheet.createRow(y):sheet.getRow(y));

			//part 조정 : 기본적으로 row에 설정을 하지만, col에 있는 설정을 우선한다.
			String part = colConfig.get("part");
			if(part==null) part = rowConfig.get("part");
			if(part==null) part = "body";
			
			//align 조정 - row설정을 먼저 적용하고, col설정을 나중에 
			//if(colConfig.get("align")==null && align!=null) colConfig.put("align", align);
			if(rowConfig!=null) {
				String align = rowConfig.get("align");		
				if(colConfig.get("align")==null && align!=null){
					if(data!=null && align.indexOf("{")>-1){
						colConfig.put("align", calcFunc(align, data));			
					}else{
						colConfig.put("align", align);
					}
				}
				align = colConfig.get("align");
				if(align!=null){
					if(data!=null && align.indexOf("{")>-1){
						colConfig.put("align", calcFunc(align, data));			
					}else{
						colConfig.put("align", align);
					}
				}
			}
			
			//셀 높이 조정
			if(part!=null && part.indexOf("pageName")>-1){
				//제목은 높이 상향
				row.setHeight((short)720); 
			}else if(rowConfig!=null && rowConfig.get("height")!=null){
				//tr 높이 조정
				Object trHeight = rowConfig.get("height");
				short trHeightValue = 0;
				if(trHeight.getClass().getName().equals("java.lang.Double")){
					trHeightValue = ((Double) trHeight).shortValue();
				}else{
					trHeightValue = Short.parseShort((String) trHeight);
				}
				row.setHeight((short)(trHeightValue * 20));
			}else{
				//일반높이
				row.setHeight((short)360); 
			}

			//td.merge 구하기 - 같은 값은 rowpan 처리한다------------------------------------------
			Boolean display = true;
			//Display
			if(colConfig.get("display")!=null){
				if(data!=null && String.valueOf(colConfig.get("display")).indexOf("{")>-1){
					if(calcFunc(colConfig.get("display"), data).equalsIgnoreCase("FALSE")){						
						continue;//display = false;
					}
				}else{
					if(String.valueOf(colConfig.get("display")).equalsIgnoreCase("FALSE")){						
						continue;//display = false;
					}
				}
			}			
			//display체크 : true/null 보임, false:안보임(x만 -1 이동)
			//colConfig.put("display", display.toString());
			
			//병합여부			
			Boolean isMerged = false; 
			int mergeCount = 0;
			if(
					colConfig!=null && String.valueOf(colConfig.get("merge")).equalsIgnoreCase("TRUE") 
					&& dataList!=null && dataList.getClass().getName().equalsIgnoreCase("java.util.ArrayList")
					&& display == true
			){
				sb.setLength(0);
				//logger.info(colConfig.get("key") +" >> "+ colConfig.toString());
//				if(data.get(colConfig.get("key"))==null){
//					data.put(colConfig.get("key"), "");
//				}
				if(
						colConfig.get("key")!=null 
						&& data.get(colConfig.get("key"))!=null
						//&& !data.get(colConfig.get("key")).equalsIgnoreCase("")
						&& !data.get(colConfig.get("key")).equals(mergeCheck.get(colConfig.get("key")))
				){
					mergeCheck.put(colConfig.get("key"), data.get(colConfig.get("key")));
					List<Map<String, String>> tmpList = (List<Map<String,String>>)dataList;
					for(int i=dataSeq, il=tmpList.size(); i<il; i++){
						Map<String, String> tmpObj = tmpList.get(i);
						//logger.info(gson.toJson(tmpObj));
						String checker = tmpObj.get(colConfig.get("key"));
						if( checker==null || (checker!=null && !checker.equals(mergeCheck.get(colConfig.get("key")))) ) break;
						mergeCount++;				
					}
					//logger.info(colConfig.get("key") +" : "+ data.get(colConfig.get("key"))+ " mergeCount : "+ mergeCount);
					if(mergeCount>1){
						colConfig.put("rowspan", String.valueOf(mergeCount));
					}
				}else{
					isMerged = true;
				}
			}
			
			int colspan = 0;
			if(colConfig.get("colspan")!=null){
				Object cfgColspan = colConfig.get("colspan");
				if(cfgColspan.getClass().getName().equals("java.lang.Double")){
					colspan = ((Double) cfgColspan).intValue();
				}else{
					if(cfgColspan!=null && cfgColspan.toString().indexOf("{")>-1){
						colspan = Integer.parseInt(calcFunc(cfgColspan.toString(), data));
					}else{
						colspan = Integer.parseInt((String) cfgColspan);
					}
				}
			}
			int rowspan = 0;
			if(colConfig.get("rowspan")!=null){
				Object cfgRowspan = colConfig.get("rowspan");
				if(cfgRowspan.getClass().getName().equals("java.lang.Double")){
					rowspan = ((Double) cfgRowspan).intValue();
				}else{
					if(cfgRowspan!=null && cfgRowspan.toString().indexOf("{")>-1){
						rowspan = Integer.parseInt(calcFunc(cfgRowspan.toString(), data));
					}else{
						rowspan = Integer.parseInt((String) cfgRowspan);
					}
				}
			}
			/*
			if(data!=null && (colspan>1 || rowspan>1)){
				logger.info(
					"dataSeq:value:mergeCount / x:y~y+rowspan: / colConfig = "
					+ dataSeq +":"+ data.get(colConfig.get("key")) +":"+ mergeCount
					+" / "+ x +":"+ y +"~"+ (y+mergeCount-1)
					+" / "+ colConfig
				);
				logger.info("colspan: "+ colspan +", rowspan:"+ rowspan);
			}
			*/
			//ROWSPAN 만 있는 것 처리------------------------------------------------------------
			if(rowspan>1 && colspan<=1){
				//셀중복체크
				sb.setLength(0);
				while(rowspanCheckList.indexOf(sb.append(y).append("-").append(x).toString())>-1){
					//셀테두리만 칠하기
					this.toExcelRenderCell(
						row.getCell(x)==null?row.createCell(x):row.getCell(x) //cell
	    				, part
	    				, colConfig
	    				, null //data
	    				, 0 //dataSeq
		    		);
					x++;
					sb.setLength(0);
				}

				//RowSpan적용 
				sheet.addMergedRegion(new CellRangeAddress(y, y+rowspan-1, x, x)); //RowStart, RowEnd, ColStart, ColEnd
				//logger.info("y:"+ y +"~"+ (y+rowspan-1) +", x:"+ x +"~"+ x +" >> 병합됨1-"+ gson.toJson(rowspanCheckList));
								
				//SPAN 체크 리스트 추가
				for(int s=y, sl=y+rowspan; s<sl; s++){
					sb.setLength(0);    	    							
					if(rowspanCheckList.indexOf(sb.append(s).append("-").append(x).toString())<0){
						rowspanCheckList.add(sb.toString());
						//셀테두리만 칠하기
						//logger.info("part:x:y = "+ part +":"+ x +":"+ s);
						Row tmpRow = (sheet.getRow(s)==null?sheet.createRow(s):sheet.getRow(s));
						Cell tmpCell = tmpRow.getCell(x)==null?tmpRow.createCell(x):tmpRow.getCell(x);
						this.toExcelRenderCell(
							tmpCell
		    				, part
		    				, colConfig
		    				, null
		    				, 0
			    		);
					}
				}
				
				//값주입
				if(!isMerged){
					this.toExcelRenderCell(
						row.getCell(x)==null?row.createCell(x):row.getCell(x) //cell
	    				, part
	    				, colConfig
	    				, data
	    				, dataSeq
		    		);
					x++;
				}				
				
			}
			//COLSPAN 만 있는 것 처리------------------------------------------------------------
			else if(rowspan<=1 && colspan>1){	
				//셀중복체크
				sb.setLength(0);
				while(rowspanCheckList.indexOf(sb.append(y).append("-").append(x).toString())>-1){
					//셀테두리 칠하기
					this.toExcelRenderCell(
						row.getCell(x)==null?row.createCell(x):row.getCell(x) //cell
						, part
						, colConfig
	    				, null //data
	    				, 0 //dataSeq
		    		);
					x++;
					sb.setLength(0);
				}
				
				//ColSpan적용 
	    		sheet.addMergedRegion(new CellRangeAddress(y, y, x, x+colspan-1)); //RowStart, RowEnd, ColStart, ColEnd
				//logger.info("y:"+y +"~"+ y +", x:"+ x +"~"+ (x+colspan-1) +" >> 병합됨2-"+ gson.toJson(rowspanCheckList));

	    		//값주입
				if(!isMerged){
					this.toExcelRenderCell(
						row.getCell(x)==null?row.createCell(x):row.getCell(x) //cell
	    				, part
	    				, colConfig
	    				, data
	    				, dataSeq
		    		);
					x += colspan;
	    		
					//셀테두리 칠하기
					for(int s=x-colspan+1, sl=x; s<sl; s++){
			    		this.toExcelRenderCell(
		    				row.getCell(s)==null?row.createCell(s):row.getCell(s) //cell
		    				, part
		    				, colConfig
		    				, null //data
		    				, 0 //dataSeq
			    		);
						//this.toExcelRenderCell(workbook, cell, cfg, null, 0, part);	 
					}
				}
				
			}
			//ROWSPAN과 COLSPAN이 함께 있는 것 처리------------------------------------------------------------
			else if(rowspan>1 && colspan>1){
				//셀중복체크
				sb.setLength(0);
				while(rowspanCheckList.indexOf(sb.append(y).append("-").append(x).toString())>-1){
					//셀테두리 칠하기
					this.toExcelRenderCell(
						row.getCell(x)==null?row.createCell(x):row.getCell(x) //cell
	    				, part
	    				, colConfig
	    				, null //data
	    				, 0 //dataSeq
		    		);
					//this.toExcelRenderCell(workbook, cell, cfg, data, 0, part);
					x++;
					sb.setLength(0);
				}

				//RowSpan, ColSpan적용 
				sheet.addMergedRegion(new CellRangeAddress(y, y+rowspan-1, x, x+colspan-1)); //RowStart, RowEnd, ColStart, ColEnd
				//logger.info("y:"+ y +"~"+ (y+rowspan-1) +", x:"+ x +"~"+ (x+colspan-1) +" >> 병합됨3-"+ gson.toJson(rowspanCheckList));
				
				//SPAN 체크 리스트 추가
				for(int s=y, sl=y+rowspan; s<sl; s++){
					row = (sheet.getRow(s)==null?sheet.createRow(s):sheet.getRow(s));
					for(int ss=x, ssl=x+colspan; ss<ssl; ss++){
						sb.setLength(0);
						sb.append(s).append("-").append(ss);
						if(rowspanCheckList.indexOf(sb.toString())<0){
							rowspanCheckList.add(sb.toString());
						}
						this.toExcelRenderCell(
							row.getCell(ss)==null?row.createCell(ss):row.getCell(ss) //cell
		    				, part
		    				, colConfig
		    				, null //data
		    				, 0 //dataSeq
			    		);
					}    	    							
				}

				if(!isMerged){
					//값주입
					row = (sheet.getRow(y)==null?sheet.createRow(y):sheet.getRow(y));
					this.toExcelRenderCell(
						row.getCell(x)==null?row.createCell(x):row.getCell(x) //cell
	    				, part
	    				, colConfig
	    				, data
	    				, dataSeq
		    		);
					x += colspan;
				}
				
			}
			//그외------------------------------------------------------------
			else{

				//셀중복체크
				sb.setLength(0);
				while(rowspanCheckList.indexOf(sb.append(y).append("-").append(x).toString())>-1){
					//logger.info("y:"+ y +"~"+ y +", x:"+ x +"~"+ x +" >> 병합됨4-"+ gson.toJson(rowspanCheckList));
					//셀테두리 칠하기
					this.toExcelRenderCell(
						row.getCell(x)==null?row.createCell(x):row.getCell(x) //cell
	    				, part
	    				, colConfig
	    				, null //data
	    				, 0 //dataSeq
		    		);
					//this.toExcelRenderCell(workbook, cell, cfg, data, 0, part);
					x++;
					sb.setLength(0);
				}

				if(!isMerged){
					//값주입
					this.toExcelRenderCell(
						row.getCell(x)==null?row.createCell(x):row.getCell(x) //cell
	    				, part
	    				, colConfig //HashMap<String, String> cfg //USER Config
	    				, data
	    				, dataSeq
		    		);
					x++;
				}
			}
			
		}//for(int c=0, cl=cfgList.size(); c<cl; c++){
	}
	
	/***********************************************************************************************************************
	//toExcelComplex 엑셀 Cell 렌더링 처리 공통기능
	***********************************************************************************************************************/
	public void toExcelRenderCell(
			Cell cell //Cell 객체
			, String part
			, Map<String, String> colConfig //USER Config
			, Map<String, String> data //레코드셋의 해당 ROW
			, int dataSeq
	){
//		logger.info(String.valueOf(rowSeq));
//		logger.info("	"+gson.toJson(cfg));
//		logger.info("	"+gson.toJson(dataList));
//		logger.info(gson.toJson(cfg));		
//		logger.info(cell.getRowIndex()+"-"+cell.getColumnIndex()+" = "+ cfg.get("part") + ":"+ cfg.get("type")+":"+cfg.get("key")+":"+koConvert(cfg.get("label"))+":"+cfg.get("align"));
		//logger.info(cell.getRowIndex()+"-"+cell.getColumnIndex()+" = "+ tablePart);	

		//display체크 : true/null 보임, false:안보임(x만 -1 이동)
		if(colConfig.get("display")!=null && data!=null){
			if(colConfig.get("display").equalsIgnoreCase("FALSE")){
				x--;
				return;
			}
		}
		
		//계산식 part 구하기
		if(",pageName,pageNameBorder,plain,border,header,body,bg1,bg2,bg3,bg4,footer,".indexOf(","+part+",")<0){
			//logger.info("calcFunc(part) : "+ ",pageName,pageNameBorder,plain,border,header,body,bg1,bg2,bg3,bg4,footer,".indexOf(","+part+",") +" : "+ part);
			part = calcFunc(part, data);
		}

		//셀스타일
		cell.setCellStyle(cs.getCellStyles(part, colConfig));
		
		//셀 값
		Object cellValue = "";

		//테이블 Header, 제목
		if(part.equalsIgnoreCase("HEADER")){
			//헤더는 묻지도 따지지도 않고 라벨 주입
			if(colConfig.get("label")!=null && colConfig.get("label").toString().indexOf("{")>-1){
				cellValue = calcFunc(colConfig.get("label"), data);
			}else{
				cellValue = colConfig.get("label");
			}
			if(cellValue!=null) cellValue = prePostFix(cellValue.toString(), colConfig);
		}
		//NO출력
		else if(colConfig.get("key")!=null && colConfig.get("key").equalsIgnoreCase("NO")){			
			cellValue = prePostFix(String.valueOf(dataSeq+1), colConfig);
		}
		//라벨출력
		else if(colConfig.get("type")!=null && colConfig.get("type").equalsIgnoreCase("LABEL")){			
			if(colConfig.get("print")!=null){
				if(colConfig.get("print").toString().indexOf("{")>-1){
					cellValue = calcFunc(colConfig.get("print"), data);
				}else{
					cellValue = colConfig.get("print").toString();
				}
			}else if(colConfig.get("label")!=null){
				//거래명세서 금액 한글표현 식: Utils.convertNum2Str 참조
				//logger.info("colConfig.get(label).toString().indexOf(convertNum2Str) : "+ colConfig.get("label").toString().indexOf("convertNum2Str"));
				if((colConfig.get("label").toString().indexOf(".korean()")>-1 || colConfig.get("label").toString().indexOf(".money()")>-1) && data!=null){
					//String tmpValue = calcFunc(colConfig.get("label"), data);
					String tmpValue = colConfig.get("label").toString();
					//convertNum2Str({pay},korean)
					//logger.info("data : "+ data);
					try {
						Matcher matcher = pattern1.matcher(tmpValue);								
						while(matcher.find()){
							String replaceString = matcher.group(1);
							//logger.info("replaceString: "+replaceString);
							//logger.info("Utils.convertNum2Str(data.get(replaceString).toString(),korean): "+ Utils.convertNum2Str(data.get(replaceString).toString(),"korean"));
							tmpValue = tmpValue.replace("{" + replaceString + "}.korean()", data==null?"": Utils.convertNum2Str(data.get(replaceString).toString(),"korean"));							
							tmpValue = tmpValue.replace("{" + replaceString + "}.money()", data==null?"": Utils.convertNum2Str(data.get(replaceString).toString(),"money"));					
							tmpValue = tmpValue.replace("{" + replaceString + "}", data==null?"": data.get(replaceString).toString());					
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					//logger.info("tmpValue: "+tmpValue);
					cellValue = tmpValue;
					
				}else if(colConfig.get("label").toString().indexOf("{")>-1){
					cellValue = calcFunc(colConfig.get("label"), data);
				}else{
					cellValue = colConfig.get("label").toString();
				}
			}
			cellValue = prePostFix((String) cellValue, colConfig);
		}
		//금액표기
		else if(
			colConfig.get("type")!=null && colConfig.get("type").equalsIgnoreCase("MONEY") 
			&& data!=null && data.get(colConfig.get("key"))!=null	&& !colConfig.get("key").equalsIgnoreCase("")
		){ 
			Object cfgData = data.get(colConfig.get("key"));
			if(cfgData.getClass().getName().equals("java.lang.Double")){
				cell.setCellValue((Double)cfgData);
			}else{
				try{
					//금액표기 하면서 prePostFix 처리
					cell.setCellValue(Double.parseDouble(prePostFix(String.valueOf(cfgData), colConfig)));					
				}catch(Exception e){
					cell.setCellValue(prePostFix(String.valueOf(cfgData), colConfig));
				}
			}
		}
		//소숫점 표기
		else if(
			colConfig.get("type")!=null 
			&& (colConfig.get("type").equalsIgnoreCase("FLOAT") || colConfig.get("type").equalsIgnoreCase("FLOAT1")) 
			&& data!=null && data.get(colConfig.get("key"))!=null
			&& !colConfig.get("key").equalsIgnoreCase("")
		){ 
			Object cfgData = data.get(colConfig.get("key"));			
			if(cfgData.getClass().getName().equals("java.lang.Double")){
				cell.setCellValue(Double.parseDouble(prePostFix(String.valueOf(cfgData), colConfig)));
			}else{
				try{
					//logger.info(cfgData.toString() +">>"+cfgData.getClass().getName()+">>"+Double.parseDouble(String.valueOf(cfgData)));
					//금액표기 하면서 prePostFix 처리
					cell.setCellValue(Double.parseDouble(prePostFix(String.valueOf(cfgData), colConfig)));					
				}catch(Exception e){
					cell.setCellValue(prePostFix(String.valueOf(cfgData), colConfig));
				}
			}
		}
		//계산식 출력
		else if(colConfig.get("type")!=null && colConfig.get("type").equalsIgnoreCase("CALCULATOR")){
			cellValue = calcFunc(koConvert(colConfig.get("label")), data);
			if(String.valueOf(cellValue).equalsIgnoreCase("INFINITY")){
				cellValue = "-";
			}else{
				cellValue = prePostFix(String.valueOf(cellValue), colConfig);
			}
		}
		//이미지 삽입
		else if(colConfig.get("type")!=null && colConfig.get("type").equalsIgnoreCase("IMG")){
			String filePath = colConfig.get("src"); ///img/JTNet-CI_80px.png
			try {
	            // 이미지 파일 로드
	            InputStream inputStream = new FileInputStream(fileImgPath+filePath);
	            byte[] bytes = IOUtils.toByteArray(inputStream);
	            SXSSFWorkbook workbook = (SXSSFWorkbook) cell.getRow().getSheet().getWorkbook();
	            Sheet sheet = cell.getRow().getSheet();
	            int pictureIdx = 0;
	            if(filePath.toUpperCase().endsWith(".PNG")){
	            	pictureIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
	            }else if(filePath.toUpperCase().endsWith(".JPG") || filePath.toUpperCase().endsWith(".JPEG")){
	            	pictureIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_JPEG);
	            }else if(filePath.toUpperCase().endsWith(".GIF")){
	            	pictureIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_GIF);
	            }else if(filePath.toUpperCase().endsWith(".BMP")){
	            	pictureIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_BMP);
	            }
	            inputStream.close();
	            
	            XSSFCreationHelper helper = (XSSFCreationHelper) workbook.getCreationHelper();
	            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
	            XSSFClientAnchor anchor = helper.createClientAnchor();
	            
	            //사용자 정의 가져오기
	            int col1 = colConfig.get("col1")!=null?((Double) ((Object)colConfig.get("col1"))).intValue() : 0;//시작 칼럼 가로 위치
	            int row1 = colConfig.get("row1")!=null?((Double) ((Object)colConfig.get("row1"))).intValue() : 0;//시작 칼럼 세로 위치
	            int col2 = colConfig.get("col2")!=null?((Double) ((Object)colConfig.get("col2"))).intValue() : 0;//종료 칼럼 가로 위치
	            int row2 = colConfig.get("row2")!=null?((Double) ((Object)colConfig.get("row2"))).intValue() : 0;//종료 칼럼 세로 위치
	            int dx1 = colConfig.get("dx1")!=null?((Double) ((Object)colConfig.get("dx1"))).intValue() : 0;//시작 padding 가로 위치
	            int dy1 = colConfig.get("dy1")!=null?((Double) ((Object)colConfig.get("dy1"))).intValue() : 0;//시작 padding 세로 위치
	            int dx2 = colConfig.get("dx2")!=null?((Double) ((Object)colConfig.get("dx2"))).intValue() : 0;//종료 padding 가로 위치
	            int dy2 = colConfig.get("dy2")!=null?((Double) ((Object)colConfig.get("dy2"))).intValue() : 0;//종료 padding 세로 위치
	            int resize = colConfig.get("resize")!=null?((Double) ((Object)colConfig.get("resize"))).intValue() : 1;//시작 종료 칼럼 안에서의 이미지 비율
	            
	            // 이미지를 출력할 CELL 위치 및 크기 범위	            
	            anchor.setCol1(col1); //좌상단 셀 가로 위치
	            anchor.setRow1(row1); //좌상단 셀 세로 위치
	            anchor.setCol2(col2); //우하단 셀 가로 위치
	            anchor.setRow2(row2); //우하단 셀 세로 위치
	            
	            //이미지 margin 설정
	            anchor.setDx1(dx1 * XSSFShape.EMU_PER_PIXEL); //좌상단 셀 가로 margin
	            anchor.setDy1(dy1 * XSSFShape.EMU_PER_PIXEL); //좌상단 셀 세로 margin
	            anchor.setDx2(dx2 * XSSFShape.EMU_PER_PIXEL); //우하단 셀 가로 margin
	            anchor.setDy2(dy2 * XSSFShape.EMU_PER_PIXEL); //우하단 셀 세로 margin
	            
	            // 이미지 그리기
	            XSSFPicture pict = drawing.createPicture(anchor, pictureIdx);
	            
	            // 이미지 사이즈 비율 설정
	            pict.resize(resize); //col1, row1, col2, row2 범위 안에 맞춤 상태의 크기 ==> 1, 2는 그 상태의 2배

	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
		//일반
		else if(
				colConfig.get("key")!=null 
				&& data!=null 
				&& data.get(colConfig.get("key"))!=null 
				&& !colConfig.get("key").equalsIgnoreCase("")
		){ 
			//cell.setCellValue(data.get(cfg.get("key")));
			cellValue = data.get(colConfig.get("key"));
			cellValue = prePostFix((String) cellValue, colConfig);
		}
		// 라벨출력
		else if(
				colConfig.get("label")!=null 
				&& data!=null 
				&& data.get(colConfig.get("label"))!=null 
				&& !colConfig.get("label").equalsIgnoreCase("")
		){ 
			//cell.setCellValue(data.get(koConvert(cfg.get("label"))));
			cellValue = data.get(koConvert(colConfig.get("label")));
			cellValue = prePostFix((String) cellValue, colConfig);
		}
		else{ 
			//cell.setCellValue("");
			cellValue = "";
		}
		
		//셀에 값 삽입(MONEY, FLOAT, FLOAT1은 위에서 먼저 set 한다)
		if(
				colConfig.get("type")!=null 
			&& colConfig.get("type").equalsIgnoreCase("MONEY") 
			&& data!=null 
			&& data.get(colConfig.get("key"))!=null 
			&& !colConfig.get("key").equalsIgnoreCase("")
		){
			//Nothing Happen
		}else if(
			colConfig.get("type")!=null 
			&& (colConfig.get("type").equalsIgnoreCase("FLOAT") || colConfig.get("type").equalsIgnoreCase("FLOAT1")) 
			&& data!=null 
			&& data.get(colConfig.get("key"))!=null 
			&& !colConfig.get("key").equalsIgnoreCase("")
		){
			//Nothing Happen
		}else{
			cell.setCellValue(cellValue==null?"":(String)cellValue);
		}
		

		//logger.info("toExcelRenderCell >> part = "+ part +" / x:y = "+ cell.getColumnIndex() +":"+ cell.getRowIndex() +" = "+  cell.getStringCellValue() +" / "+ gson.toJson(cfg));
	} 

	/***********************************************************************************************************************
	//셀넓이 조정 : width설정이 있으면 우선 반영
	***********************************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void setWidth(Sheet sheet, Map<String, String> printObj){
		String width = null;
		if(printObj.get("width")!=null) width = gson.toJson(printObj.get("width"));
		List<Map<String, String>> config = gson.fromJson(gson.toJson(printObj.get("config")), List.class);

		if(width!=null){
			int sWidth = 70;
			List<Integer> wdl = null;
			if(width.startsWith("[") && width.endsWith("]")){
				wdl = gson.fromJson(width, List.class);
			}else if(width.startsWith("{") && width.endsWith("}")){
				Map<String, String> wdo = gson.fromJson(width, Map.class);
				Object wdoColCount = wdo.get("colCount");
				int colCount = 0; 
				if(wdoColCount.getClass().getName().equals("java.lang.Double")){
					colCount = ((Double) wdoColCount).intValue();
				}else{
					colCount = Integer.parseInt(((String) wdoColCount).replaceAll("[^0-9]", ""));
				}
				Object all = wdo.get("all");
				if(all.getClass().getName().equals("java.lang.Double")){
					sWidth = ((Double) all).intValue();
				}else{
					sWidth = Integer.parseInt(((String) all).replaceAll("[^0-9]", ""));
				}
				wdl = new ArrayList<Integer>();
				for(int i=0, il=colCount; i<il; i++){
					wdl.add(sWidth);
				}
			}
			for(int i=0, il=wdl.size(); i<il; i++){
				Object ow = wdl.get(i);
				if(ow.getClass().getName().equals("java.lang.Double")){
					sWidth = ((Double) ow).intValue();
				}else if(ow.getClass().getName().equals("java.lang.String")){
					sWidth = Integer.parseInt(((String) ow).replaceAll("[^0-9]", ""));
				}else if(ow.getClass().getName().equals("java.lang.Integer")){
					sWidth = (int)ow;
				}
				sheet.setColumnWidth(i, Math.min(256*256, sWidth*30) ); //최대 256*256을 넘을 수 없음, 픽셀넓이와 비슷하게 35 곱해야함
			}
		}else{
			for(int i=0, il=config.size(); i<il; i++){
				Map<String, String> cfg = config.get(i);
				List<Map<String, String>> colConfigList = gson.fromJson(gson.toJson(cfg.get("col")), List.class);
				for(int j=0, jl=colConfigList.size(); j<jl; j++){
					int sWidth = 70;//기존 넓이
					//logger.info("configList : " + configList.toString());
					Map<String, String> colConfig = colConfigList.get(j);//gson.fromJson(gson.toJson(, Map.class);
					Object tdWidth = colConfig.get("width");
					if(tdWidth!=null){
	    				if(tdWidth.getClass().getName().equals("java.lang.Double")){
	    					sWidth = ((Double) tdWidth).intValue();
	    				}else{
	    					sWidth = Integer.parseInt(((String) tdWidth).replaceAll("[^0-9]", ""));
	    				}
					}
					if(sWidth!=0){
						sheet.setColumnWidth(j, Math.min(256*256, sWidth*30) ); //최대 256*256을 넘을 수 없음, 픽셀넓이와 비슷하게 35 곱해야함
					}
				}
			}
		}
		
	}
	
	/***********************************************************************************************************************
	부가 기능
	***********************************************************************************************************************/
	//한글깨짐 처리
	private String koConvert(String _txt){
		/*
	 	try {
			System.out.println("utf-8 -> euc-kr        : " + new String(_txt.getBytes("utf-8"), "euc-kr"));
			System.out.println("utf-8 -> ksc5601       : " + new String(_txt.getBytes("utf-8"), "ksc5601"));
			System.out.println("utf-8 -> x-windows-949 : " + new String(_txt.getBytes("utf-8"), "x-windows-949"));
			System.out.println("utf-8 -> iso-8859-1    : " + new String(_txt.getBytes("utf-8"), "iso-8859-1"));
			System.out.println("iso-8859-1 -> euc-kr        : " + new String(_txt.getBytes("iso-8859-1"), "euc-kr"));
			System.out.println("iso-8859-1 -> ksc5601       : " + new String(_txt.getBytes("iso-8859-1"), "ksc5601"));
			System.out.println("iso-8859-1 -> x-windows-949 : " + new String(_txt.getBytes("iso-8859-1"), "x-windows-949"));
			System.out.println("iso-8859-1 -> utf-8         : " + new String(_txt.getBytes("iso-8859-1"), "utf-8")); //이게 맞네!!!
			System.out.println("euc-kr -> utf-8         : " + new String(_txt.getBytes("euc-kr"), "utf-8"));
			System.out.println("euc-kr -> ksc5601       : " + new String(_txt.getBytes("euc-kr"), "ksc5601"));
			System.out.println("euc-kr -> x-windows-949 : " + new String(_txt.getBytes("euc-kr"), "x-windows-949"));
			System.out.println("euc-kr -> iso-8859-1    : " + new String(_txt.getBytes("euc-kr"), "iso-8859-1"));
			System.out.println("ksc5601 -> euc-kr        : " + new String(_txt.getBytes("ksc5601"), "euc-kr"));
			System.out.println("ksc5601 -> utf-8         : " + new String(_txt.getBytes("ksc5601"), "utf-8"));
			System.out.println("ksc5601 -> x-windows-949 : " + new String(_txt.getBytes("ksc5601"), "x-windows-949"));
			System.out.println("ksc5601 -> iso-8859-1    : " + new String(_txt.getBytes("ksc5601"), "iso-8859-1"));
			System.out.println("x-windows-949 -> euc-kr     : " + new String(_txt.getBytes("x-windows-949"), "euc-kr"));
			System.out.println("x-windows-949 -> utf-8      : " + new String(_txt.getBytes("x-windows-949"), "utf-8"));
			System.out.println("x-windows-949 -> ksc5601    : " + new String(_txt.getBytes("x-windows-949"), "ksc5601"));
			System.out.println("x-windows-949 -> iso-8859-1 : " + new String(_txt.getBytes("x-windows-949"), "iso-8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		try {
			return new String(_txt.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		*/
		
		try {
			return URLDecoder.decode(_txt, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return _txt;
	}
	
	//prefix, postfix 처리
	private String prePostFix(String cellValue, Map<String, String> cfg){
		if(cfg.get("prefix")!=null && !cfg.get("prefix").equalsIgnoreCase("")) cellValue = koConvert(cfg.get("prefix")) + "" + cellValue;
		if(cfg.get("postfix")!=null && !cfg.get("postfix").equalsIgnoreCase("")) cellValue = cellValue + "" + koConvert(cfg.get("postfix"));
		return cellValue;
	};
	
	//엑셀다운로드 진행 중 체크위한 세션체크(jt.js jt.toExcelComplete() 참조)
	public String getSessionExcelDown(HttpSession ses){
		String result = (String)ses.getAttribute("excelDownloadStatus");
		return result;
	}
	
	//엑셀다운로드 완료 상태 결과 전송(jt.js  jt.toExcelComplete() 참조)
	public void setSessionExcelDownFinished(HttpSession ses){
		ses.removeAttribute("excelDownloadStatus");
	}
	//toExcelComplex, toPrintComplex 엑셀/프린트 렌더링 합계/소계 구하기
	@SuppressWarnings("unchecked")
	public Map<String, String> getSum(
		List<Map<String, String>> resultBody
		, int startIndex
		, int endIndex
		, List<Map<String, String>> config
	){
		HashMap<String, String> sumData = new HashMap<String, String>();
		for(int i=startIndex, il=endIndex; i<il; i++){
    		String dataSet = gson.toJson(resultBody.get(i));
			HashMap<String, String> data = gson.fromJson(dataSet, HashMap.class);
			for(int f=0, fl=config.size(); f<fl; f++){
    			for(int c=0, cl=config.get(f).size(); c<cl; c++){
    				String configSet = gson.toJson(config.get(f).get(c));
    	    		HashMap<String, String> cfg = gson.fromJson(configSet, HashMap.class);
    				if(data.get(cfg.get("key"))!=null){
    					//logger.info(cfg.get("key")+":"+data.get(cfg.get("key")));
    					if(sumData.get(cfg.get("key"))!=null && cfg.get("type").equalsIgnoreCase("money")){
    						BigDecimal value = new BigDecimal(0.0);
    						Object cfgData = data.get(cfg.get("key"));
    						if(cfgData.getClass().getName().equals("java.lang.Double")){
//	    							BigDecimal cfgDataParsed = (BigDecimal) cfgData;
    							BigDecimal cfgDataParsed = new BigDecimal((Double) cfgData);
    							value = (new BigDecimal(sumData.get(cfg.get("key")))).add(cfgDataParsed);
    						}else{
    							value = (new BigDecimal(sumData.get(cfg.get("key")))).add(new BigDecimal(data.get(cfg.get("key"))));
    						}
    						sumData.put(cfg.get("key"), String.valueOf(value));
    						//logger.info(i+"-"+c+" "+cfg.get("key")+": "+ String.valueOf(value));    						
    						
    					}
    					else if(cfg.get("type")!=null && cfg.get("type").equalsIgnoreCase("label")){ //라벨
    						Matcher matcher = pattern1.matcher(cfg.get("label"));
    						while(matcher.find()){
    							String replaceString = matcher.group(1);
    							String replaceKey = replaceString.replaceAll("\\{", "").replaceAll("\\}", "");
     							sumData.put(replaceKey, data.get(replaceKey));
    						}
    					}else{
							sumData.put(cfg.get("key"), String.valueOf(data.get(cfg.get("key"))));
    					}
//							logger.info(i+"-"+c+" "+cfg.get("key"));  
    				}
    				else{
    					 if(cfg.get("type")!=null && cfg.get("type").equalsIgnoreCase("label")){ //라벨
     						Matcher matcher = pattern1.matcher(cfg.get("label"));
     						while(matcher.find()){
     							String replaceString = matcher.group(1);
     							String replaceKey = replaceString.replaceAll("\\{", "").replaceAll("\\}", "");
     							sumData.put(replaceKey, data.get(replaceKey));
     						}
     					}else{
 							sumData.put(cfg.get("key"), String.valueOf(data.get(cfg.get("key"))));
     					}
    				}
    			}
    		}
		}
//			logger.info(sumData.toString());
		return sumData;
	}
	
	public String calcFunc(String _calcTxt, Map<String, String> _data){
		try {
			Matcher matcher = pattern1.matcher(_calcTxt);								
			while(matcher.find()){
				String replaceString = matcher.group(1);
				_calcTxt = _calcTxt.replace("{" + replaceString + "}", _data==null?"":(_data.get(replaceString)==null?"":_data.get(replaceString).toString()));					
			}
			//사업자번호(107-81-58543)가 -58517로 나오면 {type:"label", label:'"{bizno1}"'}와 같이 "로 감싸준다.
			_calcTxt = String.valueOf(scriptEngine.eval(_calcTxt));
			//logger.info(_calcTxt + " = "+ result);
		} catch (Exception e1) {
			//e1.printStackTrace();
		}
		return _calcTxt;
	}
}