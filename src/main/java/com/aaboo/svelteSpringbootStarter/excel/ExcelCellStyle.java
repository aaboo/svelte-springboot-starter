package com.aaboo.svelteSpringbootStarter.excel;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelCellStyle {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelCellStyle.class);
	
	private SXSSFWorkbook workbook;
	
	private short defaultFontSize;
	private Font fontDefault;
	private Font fontWhite;
	private Font fontBold;
	private Font fontTitle;
	private Font fontBigBold;
	
	//스타일 종류 선언
	private CellStyle csPageName;
	private CellStyle csPageNameBorder;
	
	private CellStyle csPlain;
	private CellStyle csPlainLeft;
	private CellStyle csPlainRight;
	private CellStyle csPlainCenter;
	private CellStyle csPlainLeftMoney;
	private CellStyle csPlainRightMoney;
	private CellStyle csPlainCenterMoney;
	private CellStyle csPlainLeftPercent;
	private CellStyle csPlainRightPercent;
	private CellStyle csPlainCenterPercent;
	private CellStyle csPlainLeftFloat;
	private CellStyle csPlainRightFloat;
	private CellStyle csPlainCenterFloat;
	private CellStyle csPlainLeftFloat1;
	private CellStyle csPlainRightFloat1;
	private CellStyle csPlainCenterFloat1;
	private CellStyle csPlainLeftBorderLeftSolid1pxRed;
	
	private CellStyle csPlainBg;
	
	private CellStyle csPlainBigBold;
	private CellStyle csPlainBigBoldLeft;
	private CellStyle csPlainBigBoldRight;
	private CellStyle csPlainBigBoldCenter;
	private CellStyle csPlainBigBoldLeftMoney;
	private CellStyle csPlainBigBoldRightMoney;
	private CellStyle csPlainBigBoldCenterMoney;
	private CellStyle csPlainBigBoldLeftPercent;
	private CellStyle csPlainBigBoldRightPercent;
	private CellStyle csPlainBigBoldCenterPercent;
	private CellStyle csPlainBigBoldLeftFloat;
	private CellStyle csPlainBigBoldRightFloat;
	private CellStyle csPlainBigBoldCenterFloat;
	private CellStyle csPlainBigBoldLeftFloat1;
	private CellStyle csPlainBigBoldRightFloat1;
	private CellStyle csPlainBigBoldCenterFloat1;
	
	private CellStyle csPlainBold;
	private CellStyle csPlainBoldLeft;
	private CellStyle csPlainBoldRight;
	private CellStyle csPlainBoldCenter;
	private CellStyle csPlainBoldLeftMoney;
	private CellStyle csPlainBoldRightMoney;
	private CellStyle csPlainBoldCenterMoney;
	private CellStyle csPlainBoldLeftPercent;
	private CellStyle csPlainBoldRightPercent;
	private CellStyle csPlainBoldCenterPercent;
	private CellStyle csPlainBoldLeftFloat;
	private CellStyle csPlainBoldRightFloat;
	private CellStyle csPlainBoldCenterFloat;
	private CellStyle csPlainBoldLeftFloat1;
	private CellStyle csPlainBoldRightFloat1;
	private CellStyle csPlainBoldCenterFloat1;
	
	private CellStyle csBorder;
	private CellStyle csBorderLeft;
	private CellStyle csBorderRight;
	private CellStyle csBorderCenter;
	private CellStyle csBorderLeftMoney;
	private CellStyle csBorderRightMoney;
	private CellStyle csBorderCenterMoney;
	private CellStyle csBorderLeftPercent;
	private CellStyle csBorderRightPercent;
	private CellStyle csBorderCenterPercent;
	private CellStyle csBorderLeftFloat;
	private CellStyle csBorderRightFloat;
	private CellStyle csBorderCenterFloat;
	private CellStyle csBorderLeftFloat1;
	private CellStyle csBorderRightFloat1;
	private CellStyle csBorderCenterFloat1;
	
	private XSSFCellStyle csGridHeader1;
	private CellStyle csGridHeader1Left;
	private CellStyle csGridHeader1Right;
	private CellStyle csGridHeader1Center;
	private CellStyle csGridHeader1LeftMoney;
	private CellStyle csGridHeader1RightMoney;
	private CellStyle csGridHeader1CenterMoney;
	private CellStyle csGridHeader1LeftPercent;
	private CellStyle csGridHeader1RightPercent;
	private CellStyle csGridHeader1CenterPercent;
	private CellStyle csGridHeader1LeftFloat;
	private CellStyle csGridHeader1RightFloat;
	private CellStyle csGridHeader1CenterFloat;
	private CellStyle csGridHeader1LeftFloat1;
	private CellStyle csGridHeader1RightFloat1;
	private CellStyle csGridHeader1CenterFloat1;
	
	private XSSFCellStyle csGridHeader2;
	private CellStyle csGridHeader2Left;
	private CellStyle csGridHeader2Right;
	private CellStyle csGridHeader2Center;
	private CellStyle csGridHeader2LeftMoney;
	private CellStyle csGridHeader2RightMoney;
	private CellStyle csGridHeader2CenterMoney;
	private CellStyle csGridHeader2LeftPercent;
	private CellStyle csGridHeader2RightPercent;
	private CellStyle csGridHeader2CenterPercent;
	private CellStyle csGridHeader2LeftFloat;
	private CellStyle csGridHeader2RightFloat;
	private CellStyle csGridHeader2CenterFloat;
	private CellStyle csGridHeader2LeftFloat1;
	private CellStyle csGridHeader2RightFloat1;
	private CellStyle csGridHeader2CenterFloat1;
	
	private XSSFCellStyle csGridHeader3;
	private CellStyle csGridHeader3Left;
	private CellStyle csGridHeader3Right;
	private CellStyle csGridHeader3Center;
	private CellStyle csGridHeader3LeftMoney;
	private CellStyle csGridHeader3RightMoney;
	private CellStyle csGridHeader3CenterMoney;
	private CellStyle csGridHeader3LeftPercent;
	private CellStyle csGridHeader3RightPercent;
	private CellStyle csGridHeader3CenterPercent;
	private CellStyle csGridHeader3LeftFloat;
	private CellStyle csGridHeader3RightFloat;
	private CellStyle csGridHeader3CenterFloat;
	private CellStyle csGridHeader3LeftFloat1;
	private CellStyle csGridHeader3RightFloat1;
	private CellStyle csGridHeader3CenterFloat1;
	
	private XSSFCellStyle csGridHeader4;
	private CellStyle csGridHeader4Left;
	private CellStyle csGridHeader4Right;
	private CellStyle csGridHeader4Center;
	private CellStyle csGridHeader4LeftMoney;
	private CellStyle csGridHeader4RightMoney;
	private CellStyle csGridHeader4CenterMoney;
	private CellStyle csGridHeader4LeftPercent;
	private CellStyle csGridHeader4RightPercent;
	private CellStyle csGridHeader4CenterPercent;
	private CellStyle csGridHeader4LeftFloat;
	private CellStyle csGridHeader4RightFloat;
	private CellStyle csGridHeader4CenterFloat;
	private CellStyle csGridHeader4LeftFloat1;
	private CellStyle csGridHeader4RightFloat1;
	private CellStyle csGridHeader4CenterFloat1;
	
	private CellStyle csGrid;
	private CellStyle csGridLeft;
	private CellStyle csGridRight;
	private CellStyle csGridCenter;
	private CellStyle csGridLeftMoney;
	private CellStyle csGridRightMoney;
	private CellStyle csGridCenterMoney;
	private CellStyle csGridLeftPercent;
	private CellStyle csGridRightPercent;
	private CellStyle csGridCenterPercent;
	private CellStyle csGridLeftFloat;
	private CellStyle csGridRightFloat;
	private CellStyle csGridCenterFloat;
	private CellStyle csGridLeftFloat1;
	private CellStyle csGridRightFloat1;
	private CellStyle csGridCenterFloat1;
	
	private XSSFCellStyle csGridBg1;
	private CellStyle csGridBg1Left;
	private CellStyle csGridBg1Right;
	private CellStyle csGridBg1Center;
	private CellStyle csGridBg1LeftMoney;
	private CellStyle csGridBg1RightMoney;
	private CellStyle csGridBg1CenterMoney;
	private CellStyle csGridBg1LeftPercent;
	private CellStyle csGridBg1RightPercent;
	private CellStyle csGridBg1CenterPercent;
	private CellStyle csGridBg1LeftFloat;
	private CellStyle csGridBg1RightFloat;
	private CellStyle csGridBg1CenterFloat;
	private CellStyle csGridBg1LeftFloat1;
	private CellStyle csGridBg1RightFloat1;
	private CellStyle csGridBg1CenterFloat1;
	
	private XSSFCellStyle csGridBg2;
	private CellStyle csGridBg2Left;
	private CellStyle csGridBg2Right;
	private CellStyle csGridBg2Center;
	private CellStyle csGridBg2LeftMoney;
	private CellStyle csGridBg2RightMoney;
	private CellStyle csGridBg2CenterMoney;
	private CellStyle csGridBg2LeftPercent;
	private CellStyle csGridBg2RightPercent;
	private CellStyle csGridBg2CenterPercent;
	private CellStyle csGridBg2LeftFloat;
	private CellStyle csGridBg2RightFloat;
	private CellStyle csGridBg2CenterFloat;
	private CellStyle csGridBg2LeftFloat1;
	private CellStyle csGridBg2RightFloat1;
	private CellStyle csGridBg2CenterFloat1;
	
	private XSSFCellStyle csGridBg3;
	private CellStyle csGridBg3Left;
	private CellStyle csGridBg3Right;
	private CellStyle csGridBg3Center;
	private CellStyle csGridBg3LeftMoney;
	private CellStyle csGridBg3RightMoney;
	private CellStyle csGridBg3CenterMoney;
	private CellStyle csGridBg3LeftPercent;
	private CellStyle csGridBg3RightPercent;
	private CellStyle csGridBg3CenterPercent;
	private CellStyle csGridBg3LeftFloat;
	private CellStyle csGridBg3RightFloat;
	private CellStyle csGridBg3CenterFloat;
	private CellStyle csGridBg3LeftFloat1;
	private CellStyle csGridBg3RightFloat1;
	private CellStyle csGridBg3CenterFloat1;
	
	private XSSFCellStyle csGridBg4;
	private CellStyle csGridBg4Left;
	private CellStyle csGridBg4Right;
	private CellStyle csGridBg4Center;
	private CellStyle csGridBg4LeftMoney;
	private CellStyle csGridBg4RightMoney;
	private CellStyle csGridBg4CenterMoney;
	private CellStyle csGridBg4LeftPercent;
	private CellStyle csGridBg4RightPercent;
	private CellStyle csGridBg4CenterPercent;
	private CellStyle csGridBg4LeftFloat;
	private CellStyle csGridBg4RightFloat;
	private CellStyle csGridBg4CenterFloat;
	private CellStyle csGridBg4LeftFloat1;
	private CellStyle csGridBg4RightFloat1;
	private CellStyle csGridBg4CenterFloat1;
	
	private XSSFCellStyle csGridBg5;
	private CellStyle csGridBg5Left;
	private CellStyle csGridBg5Right;
	private CellStyle csGridBg5Center;
	private CellStyle csGridBg5LeftMoney;
	private CellStyle csGridBg5RightMoney;
	private CellStyle csGridBg5CenterMoney;
	private CellStyle csGridBg5LeftPercent;
	private CellStyle csGridBg5RightPercent;
	private CellStyle csGridBg5CenterPercent;
	private CellStyle csGridBg5LeftFloat;
	private CellStyle csGridBg5RightFloat;
	private CellStyle csGridBg5CenterFloat;
	private CellStyle csGridBg5LeftFloat1;
	private CellStyle csGridBg5RightFloat1;
	private CellStyle csGridBg5CenterFloat1;
	
	private XSSFCellStyle csGridBg6;
	private CellStyle csGridBg6Left;
	private CellStyle csGridBg6Right;
	private CellStyle csGridBg6Center;
	private CellStyle csGridBg6LeftMoney;
	private CellStyle csGridBg6RightMoney;
	private CellStyle csGridBg6CenterMoney;
	private CellStyle csGridBg6LeftPercent;
	private CellStyle csGridBg6RightPercent;
	private CellStyle csGridBg6CenterPercent;
	private CellStyle csGridBg6LeftFloat;
	private CellStyle csGridBg6RightFloat;
	private CellStyle csGridBg6CenterFloat;
	private CellStyle csGridBg6LeftFloat1;
	private CellStyle csGridBg6RightFloat1;
	private CellStyle csGridBg6CenterFloat1;
	
	private CellStyle csGridFooter;
	private CellStyle csGridFooterLeft;
	private CellStyle csGridFooterRight;
	private CellStyle csGridFooterCenter;
	private CellStyle csGridFooterLeftMoney;
	private CellStyle csGridFooterRightMoney;
	private CellStyle csGridFooterCenterMoney;
	private CellStyle csGridFooterLeftPercent;
	private CellStyle csGridFooterRightPercent;
	private CellStyle csGridFooterCenterPercent;
	private CellStyle csGridFooterLeftFloat;
	private CellStyle csGridFooterRightFloat;
	private CellStyle csGridFooterCenterFloat;
	private CellStyle csGridFooterLeftFloat1;
	private CellStyle csGridFooterRightFloat1;
	private CellStyle csGridFooterCenterFloat1;
	
	public ExcelCellStyle(){}
	public ExcelCellStyle(SXSSFWorkbook workbook){
		this.workbook = workbook;
		this.setCellStyles();
	}
	
	public void setDefaultFontSize(short fontSize){
		this.defaultFontSize = fontSize;
		this.fontDefault.setFontHeightInPoints(fontSize);
		this.fontWhite.setFontHeightInPoints(fontSize);	
		this.fontBold.setFontHeightInPoints(fontSize);
	}
	
	//스타일 생성하기
	public void setCellStyles(){
		csPageName = workbook.createCellStyle();
		csPageNameBorder = workbook.createCellStyle();
		
		csPlain = workbook.createCellStyle();
		csPlainLeft = workbook.createCellStyle();
		csPlainRight = workbook.createCellStyle();
		csPlainCenter = workbook.createCellStyle();
		csPlainLeftMoney = workbook.createCellStyle();
		csPlainRightMoney = workbook.createCellStyle();
		csPlainCenterMoney = workbook.createCellStyle();
		csPlainLeftPercent = workbook.createCellStyle();
		csPlainRightPercent = workbook.createCellStyle();
		csPlainCenterPercent = workbook.createCellStyle();		
		csPlainLeftFloat = workbook.createCellStyle();
		csPlainRightFloat = workbook.createCellStyle();
		csPlainCenterFloat = workbook.createCellStyle();
		csPlainLeftFloat1 = workbook.createCellStyle();
		csPlainRightFloat1 = workbook.createCellStyle();
		csPlainCenterFloat1 = workbook.createCellStyle();
		csPlainLeftBorderLeftSolid1pxRed = workbook.createCellStyle();
		
		csPlainBg = workbook.createCellStyle();
		
		csPlainBigBold = workbook.createCellStyle();
		csPlainBigBoldLeft = workbook.createCellStyle();
		csPlainBigBoldRight = workbook.createCellStyle();
		csPlainBigBoldCenter = workbook.createCellStyle();
		csPlainBigBoldLeftMoney = workbook.createCellStyle();
		csPlainBigBoldRightMoney = workbook.createCellStyle();
		csPlainBigBoldCenterMoney = workbook.createCellStyle();
		csPlainBigBoldLeftPercent = workbook.createCellStyle();
		csPlainBigBoldRightPercent = workbook.createCellStyle();
		csPlainBigBoldCenterPercent = workbook.createCellStyle();
		csPlainBigBoldLeftFloat = workbook.createCellStyle();
		csPlainBigBoldRightFloat = workbook.createCellStyle();
		csPlainBigBoldCenterFloat = workbook.createCellStyle();
		csPlainBigBoldLeftFloat1 = workbook.createCellStyle();
		csPlainBigBoldRightFloat1 = workbook.createCellStyle();
		csPlainBigBoldCenterFloat1 = workbook.createCellStyle();
		
		csPlainBold = workbook.createCellStyle();
		csPlainBoldLeft = workbook.createCellStyle();
		csPlainBoldRight = workbook.createCellStyle();
		csPlainBoldCenter = workbook.createCellStyle();
		csPlainBoldLeftMoney = workbook.createCellStyle();
		csPlainBoldRightMoney = workbook.createCellStyle();
		csPlainBoldCenterMoney = workbook.createCellStyle();
		csPlainBoldLeftPercent = workbook.createCellStyle();
		csPlainBoldRightPercent = workbook.createCellStyle();
		csPlainBoldCenterPercent = workbook.createCellStyle();		
		csPlainBoldLeftFloat = workbook.createCellStyle();
		csPlainBoldRightFloat = workbook.createCellStyle();
		csPlainBoldCenterFloat = workbook.createCellStyle();
		csPlainBoldLeftFloat1 = workbook.createCellStyle();
		csPlainBoldRightFloat1 = workbook.createCellStyle();
		csPlainBoldCenterFloat1 = workbook.createCellStyle();
		
		csBorder = workbook.createCellStyle();
		csBorderLeft = workbook.createCellStyle();
		csBorderRight = workbook.createCellStyle();
		csBorderCenter = workbook.createCellStyle();
		csBorderLeftMoney = workbook.createCellStyle();
		csBorderRightMoney = workbook.createCellStyle();
		csBorderCenterMoney = workbook.createCellStyle();
		csBorderLeftPercent = workbook.createCellStyle();
		csBorderRightPercent = workbook.createCellStyle();
		csBorderCenterPercent = workbook.createCellStyle();		
		csBorderLeftFloat = workbook.createCellStyle();
		csBorderRightFloat = workbook.createCellStyle();
		csBorderCenterFloat = workbook.createCellStyle();
		csBorderLeftFloat1 = workbook.createCellStyle();
		csBorderRightFloat1 = workbook.createCellStyle();
		csBorderCenterFloat1 = workbook.createCellStyle();
		
		csGridHeader1 = (XSSFCellStyle) workbook.createCellStyle();
		csGridHeader1Left = workbook.createCellStyle();
		csGridHeader1Right = workbook.createCellStyle();
		csGridHeader1Center = workbook.createCellStyle();
		csGridHeader1LeftMoney = workbook.createCellStyle();
		csGridHeader1RightMoney = workbook.createCellStyle();
		csGridHeader1CenterMoney = workbook.createCellStyle();
		csGridHeader1LeftPercent = workbook.createCellStyle();
		csGridHeader1RightPercent = workbook.createCellStyle();
		csGridHeader1CenterPercent = workbook.createCellStyle();		
		csGridHeader1LeftFloat = workbook.createCellStyle();
		csGridHeader1RightFloat = workbook.createCellStyle();
		csGridHeader1CenterFloat = workbook.createCellStyle();
		csGridHeader1LeftFloat1 = workbook.createCellStyle();
		csGridHeader1RightFloat1 = workbook.createCellStyle();
		csGridHeader1CenterFloat1 = workbook.createCellStyle();
		
		csGridHeader2 = (XSSFCellStyle) workbook.createCellStyle();
		csGridHeader2Left = workbook.createCellStyle();
		csGridHeader2Right = workbook.createCellStyle();
		csGridHeader2Center = workbook.createCellStyle();
		csGridHeader2LeftMoney = workbook.createCellStyle();
		csGridHeader2RightMoney = workbook.createCellStyle();
		csGridHeader2CenterMoney = workbook.createCellStyle();
		csGridHeader2LeftPercent = workbook.createCellStyle();
		csGridHeader2RightPercent = workbook.createCellStyle();
		csGridHeader2CenterPercent = workbook.createCellStyle();		
		csGridHeader2LeftFloat = workbook.createCellStyle();
		csGridHeader2RightFloat = workbook.createCellStyle();
		csGridHeader2CenterFloat = workbook.createCellStyle();
		csGridHeader2LeftFloat1 = workbook.createCellStyle();
		csGridHeader2RightFloat1 = workbook.createCellStyle();
		csGridHeader2CenterFloat1 = workbook.createCellStyle();
		
		csGridHeader3 = (XSSFCellStyle) workbook.createCellStyle();
		csGridHeader3Left = workbook.createCellStyle();
		csGridHeader3Right = workbook.createCellStyle();
		csGridHeader3Center = workbook.createCellStyle();
		csGridHeader3LeftMoney = workbook.createCellStyle();
		csGridHeader3RightMoney = workbook.createCellStyle();
		csGridHeader3CenterMoney = workbook.createCellStyle();
		csGridHeader3LeftPercent = workbook.createCellStyle();
		csGridHeader3RightPercent = workbook.createCellStyle();
		csGridHeader3CenterPercent = workbook.createCellStyle();		
		csGridHeader3LeftFloat = workbook.createCellStyle();
		csGridHeader3RightFloat = workbook.createCellStyle();
		csGridHeader3CenterFloat = workbook.createCellStyle();
		csGridHeader3LeftFloat1 = workbook.createCellStyle();
		csGridHeader3RightFloat1 = workbook.createCellStyle();
		csGridHeader3CenterFloat1 = workbook.createCellStyle();
		
		csGridHeader4 = (XSSFCellStyle) workbook.createCellStyle();
		csGridHeader4Left = workbook.createCellStyle();
		csGridHeader4Right = workbook.createCellStyle();
		csGridHeader4Center = workbook.createCellStyle();
		csGridHeader4LeftMoney = workbook.createCellStyle();
		csGridHeader4RightMoney = workbook.createCellStyle();
		csGridHeader4CenterMoney = workbook.createCellStyle();
		csGridHeader4LeftPercent = workbook.createCellStyle();
		csGridHeader4RightPercent = workbook.createCellStyle();
		csGridHeader4CenterPercent = workbook.createCellStyle();		
		csGridHeader4LeftFloat = workbook.createCellStyle();
		csGridHeader4RightFloat = workbook.createCellStyle();
		csGridHeader4CenterFloat = workbook.createCellStyle();
		csGridHeader4LeftFloat1 = workbook.createCellStyle();
		csGridHeader4RightFloat1 = workbook.createCellStyle();
		csGridHeader4CenterFloat1 = workbook.createCellStyle();
		
		csGrid = workbook.createCellStyle();
		csGridLeft = workbook.createCellStyle();
		csGridRight = workbook.createCellStyle();
		csGridCenter = workbook.createCellStyle();
		csGridLeftMoney = workbook.createCellStyle();
		csGridRightMoney = workbook.createCellStyle();
		csGridCenterMoney = workbook.createCellStyle();
		csGridLeftPercent = workbook.createCellStyle();
		csGridRightPercent = workbook.createCellStyle();
		csGridCenterPercent = workbook.createCellStyle();		
		csGridLeftFloat = workbook.createCellStyle();
		csGridRightFloat = workbook.createCellStyle();
		csGridCenterFloat = workbook.createCellStyle();
		csGridLeftFloat1 = workbook.createCellStyle();
		csGridRightFloat1 = workbook.createCellStyle();
		csGridCenterFloat1 = workbook.createCellStyle();
		
		csGridBg1 = (XSSFCellStyle) workbook.createCellStyle();
		csGridBg1Left = workbook.createCellStyle();
		csGridBg1Right = workbook.createCellStyle();
		csGridBg1Center = workbook.createCellStyle();
		csGridBg1LeftMoney = workbook.createCellStyle();
		csGridBg1RightMoney = workbook.createCellStyle();
		csGridBg1CenterMoney = workbook.createCellStyle();
		csGridBg1LeftPercent = workbook.createCellStyle();
		csGridBg1RightPercent = workbook.createCellStyle();
		csGridBg1CenterPercent = workbook.createCellStyle();		
		csGridBg1LeftFloat = workbook.createCellStyle();
		csGridBg1RightFloat = workbook.createCellStyle();
		csGridBg1CenterFloat = workbook.createCellStyle();
		csGridBg1LeftFloat1 = workbook.createCellStyle();
		csGridBg1RightFloat1 = workbook.createCellStyle();
		csGridBg1CenterFloat1 = workbook.createCellStyle();
		
		csGridBg2 = (XSSFCellStyle) workbook.createCellStyle();
		csGridBg2Left = workbook.createCellStyle();
		csGridBg2Right = workbook.createCellStyle();
		csGridBg2Center = workbook.createCellStyle();
		csGridBg2LeftMoney = workbook.createCellStyle();
		csGridBg2RightMoney = workbook.createCellStyle();
		csGridBg2CenterMoney = workbook.createCellStyle();
		csGridBg2LeftPercent = workbook.createCellStyle();
		csGridBg2RightPercent = workbook.createCellStyle();
		csGridBg2CenterPercent = workbook.createCellStyle();		
		csGridBg2LeftFloat = workbook.createCellStyle();
		csGridBg2RightFloat = workbook.createCellStyle();
		csGridBg2CenterFloat = workbook.createCellStyle();
		csGridBg2LeftFloat1 = workbook.createCellStyle();
		csGridBg2RightFloat1 = workbook.createCellStyle();
		csGridBg2CenterFloat1 = workbook.createCellStyle();
		
		csGridBg3 = (XSSFCellStyle) workbook.createCellStyle();
		csGridBg3Left = workbook.createCellStyle();
		csGridBg3Right = workbook.createCellStyle();
		csGridBg3Center = workbook.createCellStyle();
		csGridBg3LeftMoney = workbook.createCellStyle();
		csGridBg3RightMoney = workbook.createCellStyle();
		csGridBg3CenterMoney = workbook.createCellStyle();
		csGridBg3LeftPercent = workbook.createCellStyle();
		csGridBg3RightPercent = workbook.createCellStyle();
		csGridBg3CenterPercent = workbook.createCellStyle();		
		csGridBg3LeftFloat = workbook.createCellStyle();
		csGridBg3RightFloat = workbook.createCellStyle();
		csGridBg3CenterFloat = workbook.createCellStyle();
		csGridBg3LeftFloat1 = workbook.createCellStyle();
		csGridBg3RightFloat1 = workbook.createCellStyle();
		csGridBg3CenterFloat1 = workbook.createCellStyle();
		
		csGridBg4 = (XSSFCellStyle) workbook.createCellStyle();
		csGridBg4Left = workbook.createCellStyle();
		csGridBg4Right = workbook.createCellStyle();
		csGridBg4Center = workbook.createCellStyle();
		csGridBg4LeftMoney = workbook.createCellStyle();
		csGridBg4RightMoney = workbook.createCellStyle();
		csGridBg4CenterMoney = workbook.createCellStyle();
		csGridBg4LeftPercent = workbook.createCellStyle();
		csGridBg4RightPercent = workbook.createCellStyle();
		csGridBg4CenterPercent = workbook.createCellStyle();		
		csGridBg4LeftFloat = workbook.createCellStyle();
		csGridBg4RightFloat = workbook.createCellStyle();
		csGridBg4CenterFloat = workbook.createCellStyle();
		csGridBg4LeftFloat1 = workbook.createCellStyle();
		csGridBg4RightFloat1 = workbook.createCellStyle();
		csGridBg4CenterFloat1 = workbook.createCellStyle();
		
		csGridBg5 = (XSSFCellStyle) workbook.createCellStyle();
		csGridBg5Left = workbook.createCellStyle();
		csGridBg5Right = workbook.createCellStyle();
		csGridBg5Center = workbook.createCellStyle();
		csGridBg5LeftMoney = workbook.createCellStyle();
		csGridBg5RightMoney = workbook.createCellStyle();
		csGridBg5CenterMoney = workbook.createCellStyle();
		csGridBg5LeftPercent = workbook.createCellStyle();
		csGridBg5RightPercent = workbook.createCellStyle();
		csGridBg5CenterPercent = workbook.createCellStyle();		
		csGridBg5LeftFloat = workbook.createCellStyle();
		csGridBg5RightFloat = workbook.createCellStyle();
		csGridBg5CenterFloat = workbook.createCellStyle();
		csGridBg5LeftFloat1 = workbook.createCellStyle();
		csGridBg5RightFloat1 = workbook.createCellStyle();
		csGridBg5CenterFloat1 = workbook.createCellStyle();
		
		csGridBg6 = (XSSFCellStyle) workbook.createCellStyle();
		csGridBg6Left = workbook.createCellStyle();
		csGridBg6Right = workbook.createCellStyle();
		csGridBg6Center = workbook.createCellStyle();
		csGridBg6LeftMoney = workbook.createCellStyle();
		csGridBg6RightMoney = workbook.createCellStyle();
		csGridBg6CenterMoney = workbook.createCellStyle();
		csGridBg6LeftPercent = workbook.createCellStyle();
		csGridBg6RightPercent = workbook.createCellStyle();
		csGridBg6CenterPercent = workbook.createCellStyle();		
		csGridBg6LeftFloat = workbook.createCellStyle();
		csGridBg6RightFloat = workbook.createCellStyle();
		csGridBg6CenterFloat = workbook.createCellStyle();
		csGridBg6LeftFloat1 = workbook.createCellStyle();
		csGridBg6RightFloat1 = workbook.createCellStyle();
		csGridBg6CenterFloat1 = workbook.createCellStyle();
		
		csGridFooter = workbook.createCellStyle();
		csGridFooterLeft = workbook.createCellStyle();
		csGridFooterRight = workbook.createCellStyle();
		csGridFooterCenter = workbook.createCellStyle();
		csGridFooterLeftMoney = workbook.createCellStyle();
		csGridFooterRightMoney = workbook.createCellStyle();
		csGridFooterCenterMoney = workbook.createCellStyle();
		csGridFooterLeftPercent = workbook.createCellStyle();
		csGridFooterRightPercent = workbook.createCellStyle();
		csGridFooterCenterPercent = workbook.createCellStyle();		
		csGridFooterLeftFloat = workbook.createCellStyle();
		csGridFooterRightFloat = workbook.createCellStyle();
		csGridFooterCenterFloat = workbook.createCellStyle();
		csGridFooterLeftFloat1 = workbook.createCellStyle();
		csGridFooterRightFloat1 = workbook.createCellStyle();
		csGridFooterCenterFloat1 = workbook.createCellStyle();
		
		//폰트
		short fontSize = 9;
		if(defaultFontSize>0){
			fontSize = defaultFontSize;
		}
		fontDefault = workbook.createFont();
			fontDefault.setFontName("맑은 고딕");
			fontDefault.setFontHeightInPoints(fontSize);
		fontWhite = workbook.createFont();
			fontWhite.setFontName("맑은 고딕");
			fontWhite.setFontHeightInPoints(fontSize);			
			fontWhite.setColor(HSSFColor.WHITE.index);
		fontBold = workbook.createFont();
			fontBold.setFontName("맑은 고딕");
			fontBold.setFontHeightInPoints(fontSize);
			fontBold.setBold(true);
		fontTitle = workbook.createFont();
			fontTitle.setFontName("맑은 고딕");
			fontTitle.setFontHeightInPoints((short)16);
		fontBigBold = workbook.createFont();
			fontBigBold.setFontName("맑은 고딕");
			fontBigBold.setFontHeightInPoints((short)20);
			fontBigBold.setBold(true);
			//fontTitle.setBold(true);
		
		//유형
		//fontBold.setColor(HSSFColor.WHITE.index);
		short formatMoney = HSSFDataFormat.getBuiltinFormat("#,##0"); //돈콤마(,)
		short formatFloat = HSSFDataFormat.getBuiltinFormat("#,##0.00"); //돈콤마(,) + 소수2자리
		short formatFloat1 = HSSFDataFormat.getBuiltinFormat("#,##0.0"); //소수1자리 >> 돈콤마(,) 안됨
		short formatPercent = HSSFDataFormat.getBuiltinFormat("#,##0.00%"); //돈콤마(,) + 소수2자리 + %

		//-------------------------------------------------------------------------------PAGENAME: 제목 유형
		{
			//csPageName
			csPageName.setFont(fontTitle);
			csPageName.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csPageName.setAlignment(CellStyle.ALIGN_CENTER);
			//csPageNameBorder
			csPageNameBorder.cloneStyleFrom(csPageName);
			csPageNameBorder.setBorderTop(XSSFCellStyle.BORDER_THICK); 
			csPageNameBorder.setBorderRight(XSSFCellStyle.BORDER_THICK);              
			csPageNameBorder.setBorderBottom(XSSFCellStyle.BORDER_THICK); 
			csPageNameBorder.setBorderLeft(XSSFCellStyle.BORDER_THICK);  
		}
		//-----------------------------------------------------------------------PALIN: csPlain 유형(태두리 없음)
		{
			//csPlain
			csPlain.setFont(fontDefault);
			csPlain.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			//csPlainLeft
			csPlainLeft.cloneStyleFrom(csPlain);
			csPlainLeft.setAlignment(CellStyle.ALIGN_LEFT);
			csPlainLeftMoney.cloneStyleFrom(csPlainLeft);
			csPlainLeftMoney.setDataFormat(formatMoney);
			csPlainLeftPercent.cloneStyleFrom(csPlainLeft);
			csPlainLeftPercent.setDataFormat(formatPercent);
			csPlainLeftFloat.cloneStyleFrom(csPlainLeft);
			csPlainLeftFloat.setDataFormat(formatFloat);
			csPlainLeftFloat1.cloneStyleFrom(csPlainLeft);
			csPlainLeftFloat1.setDataFormat(formatFloat1);
			//csPlainRight
			csPlainRight.cloneStyleFrom(csPlain);
			csPlainRight.setAlignment(CellStyle.ALIGN_RIGHT);
			csPlainRightMoney.cloneStyleFrom(csPlainRight);
			csPlainRightMoney.setDataFormat(formatMoney);
			csPlainRightPercent.cloneStyleFrom(csPlainRight);
			csPlainRightPercent.setDataFormat(formatPercent);
			csPlainRightFloat.cloneStyleFrom(csPlainRight);
			csPlainRightFloat.setDataFormat(formatFloat);
			csPlainRightFloat1.cloneStyleFrom(csPlainRight);
			csPlainRightFloat1.setDataFormat(formatFloat1);
			//csPlainCenter
			csPlainCenter.cloneStyleFrom(csPlain);
			csPlainCenter.setAlignment(CellStyle.ALIGN_CENTER);
			csPlainCenterMoney.cloneStyleFrom(csPlainCenter);
			csPlainCenterMoney.setDataFormat(formatMoney);
			csPlainCenterPercent.cloneStyleFrom(csPlainCenter);
			csPlainCenterPercent.setDataFormat(formatPercent);
			csPlainCenterFloat.cloneStyleFrom(csPlainCenter);
			csPlainCenterFloat.setDataFormat(formatFloat);
			csPlainCenterFloat1.cloneStyleFrom(csPlainCenter);
			csPlainCenterFloat1.setDataFormat(formatFloat1);
			//csPlainLeftBorderLeftSolid1pxRed
			csPlainLeftBorderLeftSolid1pxRed.cloneStyleFrom(csPlainLeft);
			csPlainLeftBorderLeftSolid1pxRed.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			csPlainLeftBorderLeftSolid1pxRed.setLeftBorderColor(HSSFColor.RED.index);
		}	
		//-------------------------------------------------------------PALINBG: csPlainBg 유형(태두리 없음)
		{
			//csPlainBg
			//색상 : https://aaboo.home.blog/2019/10/30/poi-배경색상-표/ 참조
			csPlainBg.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			csPlainBg.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}	
		//-----------------------------------------------------PALINBIGBOLD: csPlainBigBold 유형(태두리 없음)
		{
			//csPlainBigBold
			csPlainBigBold.setFont(fontBigBold);
			csPlainBigBold.setWrapText(true);
			csPlainBigBold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			//csPlainBigBoldLeft
			csPlainBigBoldLeft.cloneStyleFrom(csPlainBigBold);
			csPlainBigBoldLeft.setAlignment(CellStyle.ALIGN_LEFT);
			csPlainBigBoldLeftMoney.cloneStyleFrom(csPlainBigBoldLeft);
			csPlainBigBoldLeftMoney.setDataFormat(formatMoney);
			csPlainBigBoldLeftPercent.cloneStyleFrom(csPlainBigBoldLeft);
			csPlainBigBoldLeftPercent.setDataFormat(formatPercent);
			csPlainBigBoldLeftFloat.cloneStyleFrom(csPlainBigBoldLeft);
			csPlainBigBoldLeftFloat.setDataFormat(formatFloat);
			csPlainBigBoldLeftFloat1.cloneStyleFrom(csPlainBigBoldLeft);
			csPlainBigBoldLeftFloat1.setDataFormat(formatFloat1);
			//csPlainBigBoldRight
			csPlainBigBoldRight.cloneStyleFrom(csPlainBigBold);
			csPlainBigBoldRight.setAlignment(CellStyle.ALIGN_RIGHT);
			csPlainBigBoldRightMoney.cloneStyleFrom(csPlainBigBoldRight);
			csPlainBigBoldRightMoney.setDataFormat(formatMoney);
			csPlainBigBoldRightPercent.cloneStyleFrom(csPlainBigBoldRight);
			csPlainBigBoldRightPercent.setDataFormat(formatPercent);
			csPlainBigBoldRightFloat.cloneStyleFrom(csPlainBigBoldRight);
			csPlainBigBoldRightFloat.setDataFormat(formatFloat);
			csPlainBigBoldRightFloat1.cloneStyleFrom(csPlainBigBoldRight);
			csPlainBigBoldRightFloat1.setDataFormat(formatFloat1);
			//csPlainBigBoldCenter
			csPlainBigBoldCenter.cloneStyleFrom(csPlainBigBold);
			csPlainBigBoldCenter.setAlignment(CellStyle.ALIGN_CENTER);
			csPlainBigBoldCenterMoney.cloneStyleFrom(csPlainBigBoldCenter);
			csPlainBigBoldCenterMoney.setDataFormat(formatMoney);
			csPlainBigBoldCenterPercent.cloneStyleFrom(csPlainBigBoldCenter);
			csPlainBigBoldCenterPercent.setDataFormat(formatPercent);
			csPlainBigBoldCenterFloat.cloneStyleFrom(csPlainBigBoldCenter);
			csPlainBigBoldCenterFloat.setDataFormat(formatFloat);
			csPlainBigBoldCenterFloat1.cloneStyleFrom(csPlainBigBoldCenter);
			csPlainBigBoldCenterFloat1.setDataFormat(formatFloat1);
		}	
		//-------------------------------------------------------------PALINBOLD: csPlainBold 유형(태두리 없음)
		{
			//csPlainBold
			csPlainBold.setFont(fontBold);
			csPlainBold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			//csPlainBoldLeft
			csPlainBoldLeft.cloneStyleFrom(csPlainBold);
			csPlainBoldLeft.setAlignment(CellStyle.ALIGN_LEFT);
			csPlainBoldLeftMoney.cloneStyleFrom(csPlainBoldLeft);
			csPlainBoldLeftMoney.setDataFormat(formatMoney);
			csPlainBoldLeftPercent.cloneStyleFrom(csPlainBoldLeft);
			csPlainBoldLeftPercent.setDataFormat(formatPercent);
			csPlainBoldLeftFloat.cloneStyleFrom(csPlainBoldLeft);
			csPlainBoldLeftFloat.setDataFormat(formatFloat);
			csPlainBoldLeftFloat1.cloneStyleFrom(csPlainBoldLeft);
			csPlainBoldLeftFloat1.setDataFormat(formatFloat1);
			//csPlainBoldRight
			csPlainBoldRight.cloneStyleFrom(csPlainBold);
			csPlainBoldRight.setAlignment(CellStyle.ALIGN_RIGHT);
			csPlainBoldRightMoney.cloneStyleFrom(csPlainBoldRight);
			csPlainBoldRightMoney.setDataFormat(formatMoney);
			csPlainBoldRightPercent.cloneStyleFrom(csPlainBoldRight);
			csPlainBoldRightPercent.setDataFormat(formatPercent);
			csPlainBoldRightFloat.cloneStyleFrom(csPlainBoldRight);
			csPlainBoldRightFloat.setDataFormat(formatFloat);
			csPlainBoldRightFloat1.cloneStyleFrom(csPlainBoldRight);
			csPlainBoldRightFloat1.setDataFormat(formatFloat1);
			//csPlainBoldCenter
			csPlainBoldCenter.cloneStyleFrom(csPlainBold);
			csPlainBoldCenter.setAlignment(CellStyle.ALIGN_CENTER);
			csPlainBoldCenterMoney.cloneStyleFrom(csPlainBoldCenter);
			csPlainBoldCenterMoney.setDataFormat(formatMoney);
			csPlainBoldCenterPercent.cloneStyleFrom(csPlainBoldCenter);
			csPlainBoldCenterPercent.setDataFormat(formatPercent);
			csPlainBoldCenterFloat.cloneStyleFrom(csPlainBoldCenter);
			csPlainBoldCenterFloat.setDataFormat(formatFloat);
			csPlainBoldCenterFloat1.cloneStyleFrom(csPlainBoldCenter);
			csPlainBoldCenterFloat1.setDataFormat(formatFloat1);
		}	
		//----------------------------------------------------------------BORDER: csBorder 유형(태두리 있음)
		{
			//csBorder
			csBorder.setFont(fontDefault);
			csBorder.setWrapText(true);
			csBorder.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csBorder.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csBorder.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csBorder.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			//csBorderLeft
			csBorderLeft.cloneStyleFrom(csBorder);
			csBorderLeft.setAlignment(CellStyle.ALIGN_LEFT);
			csBorderLeftMoney.cloneStyleFrom(csBorderLeft);
			csBorderLeftMoney.setDataFormat(formatMoney);
			csBorderLeftPercent.cloneStyleFrom(csBorderLeft);
			csBorderLeftPercent.setDataFormat(formatPercent);
			csBorderLeftFloat.cloneStyleFrom(csBorderLeft);
			csBorderLeftFloat.setDataFormat(formatFloat);
			csBorderLeftFloat1.cloneStyleFrom(csBorderLeft);
			csBorderLeftFloat1.setDataFormat(formatFloat1);
			//csBorderRight
			csBorderRight.cloneStyleFrom(csBorder);
			csBorderRight.setAlignment(CellStyle.ALIGN_RIGHT);
			csBorderRightMoney.cloneStyleFrom(csBorderRight);
			csBorderRightMoney.setDataFormat(formatMoney);
			csBorderRightPercent.cloneStyleFrom(csBorderRight);
			csBorderRightPercent.setDataFormat(formatPercent);
			csBorderRightFloat.cloneStyleFrom(csBorderRight);
			csBorderRightFloat.setDataFormat(formatFloat);
			csBorderRightFloat1.cloneStyleFrom(csBorderRight);
			csBorderRightFloat1.setDataFormat(formatFloat1);
			//csBorderCenter
			csBorderCenter.cloneStyleFrom(csBorder);
			csBorderCenter.setAlignment(CellStyle.ALIGN_CENTER);
			csBorderCenterMoney.cloneStyleFrom(csBorderCenter);
			csBorderCenterMoney.setDataFormat(formatMoney);
			csBorderCenterPercent.cloneStyleFrom(csBorderCenter);
			csBorderCenterPercent.setDataFormat(formatPercent);
			csBorderCenterFloat.cloneStyleFrom(csBorderCenter);
			csBorderCenterFloat.setDataFormat(formatFloat);
			csBorderCenterFloat1.cloneStyleFrom(csBorderCenter);
			csBorderCenterFloat1.setDataFormat(formatFloat1);
		}	
		//--------------------------------------------------------------HEADER: csGridHeader1 유형(태두리 있음)
		{
			//csGridHeader1
			csGridHeader1.setFont(fontBold);
			csGridHeader1.setWrapText(true);
			csGridHeader1.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridHeader1.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridHeader1.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridHeader1.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridHeader1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridHeader1.setFillForegroundColor(new XSSFColor(new byte[] {(byte)208,(byte)225,(byte)244}));
			csGridHeader1.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridHeader1Left
			csGridHeader1Left.cloneStyleFrom(csGridHeader1);
			csGridHeader1Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridHeader1LeftMoney.cloneStyleFrom(csGridHeader1Left);
			csGridHeader1LeftMoney.setDataFormat(formatMoney);
			csGridHeader1LeftPercent.cloneStyleFrom(csGridHeader1Left);
			csGridHeader1LeftPercent.setDataFormat(formatPercent);
			csGridHeader1LeftFloat.cloneStyleFrom(csGridHeader1Left);
			csGridHeader1LeftFloat.setDataFormat(formatFloat);
			csGridHeader1LeftFloat1.cloneStyleFrom(csGridHeader1Left);
			csGridHeader1LeftFloat1.setDataFormat(formatFloat1);
			//csGridHeader1Right
			csGridHeader1Right.cloneStyleFrom(csGridHeader1);
			csGridHeader1Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridHeader1RightMoney.cloneStyleFrom(csGridHeader1Right);
			csGridHeader1RightMoney.setDataFormat(formatMoney);
			csGridHeader1RightPercent.cloneStyleFrom(csGridHeader1Right);
			csGridHeader1RightPercent.setDataFormat(formatPercent);
			csGridHeader1RightFloat.cloneStyleFrom(csGridHeader1Right);
			csGridHeader1RightFloat.setDataFormat(formatFloat);
			csGridHeader1RightFloat1.cloneStyleFrom(csGridHeader1Right);
			csGridHeader1RightFloat1.setDataFormat(formatFloat1);
			//csGridHeader1Center
			csGridHeader1Center.cloneStyleFrom(csGridHeader1);
			csGridHeader1Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridHeader1CenterMoney.cloneStyleFrom(csGridHeader1Center);
			csGridHeader1CenterMoney.setDataFormat(formatMoney);
			csGridHeader1CenterPercent.cloneStyleFrom(csGridHeader1Center);
			csGridHeader1CenterPercent.setDataFormat(formatPercent);
			csGridHeader1CenterFloat.cloneStyleFrom(csGridHeader1Center);
			csGridHeader1CenterFloat.setDataFormat(formatFloat);
			csGridHeader1CenterFloat1.cloneStyleFrom(csGridHeader1Center);
			csGridHeader1CenterFloat1.setDataFormat(formatFloat1);
		}		
		//--------------------------------------------------------------HEADER: csGridHeader2 유형(태두리 있음)
		{
			//csGridHeader2
			csGridHeader2.setFont(fontBold);
			csGridHeader2.setWrapText(true);
			csGridHeader2.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridHeader2.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridHeader2.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridHeader2.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridHeader2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridHeader2.setFillForegroundColor(new XSSFColor(new byte[] {(byte)184,(byte)204,(byte)226}));
			csGridHeader2.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridHeader2Left
			csGridHeader2Left.cloneStyleFrom(csGridHeader2);
			csGridHeader2Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridHeader2LeftMoney.cloneStyleFrom(csGridHeader2Left);
			csGridHeader2LeftMoney.setDataFormat(formatMoney);
			csGridHeader2LeftPercent.cloneStyleFrom(csGridHeader2Left);
			csGridHeader2LeftPercent.setDataFormat(formatPercent);
			csGridHeader2LeftFloat.cloneStyleFrom(csGridHeader2Left);
			csGridHeader2LeftFloat.setDataFormat(formatFloat);
			csGridHeader2LeftFloat1.cloneStyleFrom(csGridHeader2Left);
			csGridHeader2LeftFloat1.setDataFormat(formatFloat1);
			//csGridHeader2Right
			csGridHeader2Right.cloneStyleFrom(csGridHeader2);
			csGridHeader2Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridHeader2RightMoney.cloneStyleFrom(csGridHeader2Right);
			csGridHeader2RightMoney.setDataFormat(formatMoney);
			csGridHeader2RightPercent.cloneStyleFrom(csGridHeader2Right);
			csGridHeader2RightPercent.setDataFormat(formatPercent);
			csGridHeader2RightFloat.cloneStyleFrom(csGridHeader2Right);
			csGridHeader2RightFloat.setDataFormat(formatFloat);
			csGridHeader2RightFloat1.cloneStyleFrom(csGridHeader2Right);
			csGridHeader2RightFloat1.setDataFormat(formatFloat1);
			//csGridHeader2Center
			csGridHeader2Center.cloneStyleFrom(csGridHeader2);
			csGridHeader2Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridHeader2CenterMoney.cloneStyleFrom(csGridHeader2Center);
			csGridHeader2CenterMoney.setDataFormat(formatMoney);
			csGridHeader2CenterPercent.cloneStyleFrom(csGridHeader2Center);
			csGridHeader2CenterPercent.setDataFormat(formatPercent);
			csGridHeader2CenterFloat.cloneStyleFrom(csGridHeader2Center);
			csGridHeader2CenterFloat.setDataFormat(formatFloat);
			csGridHeader2CenterFloat1.cloneStyleFrom(csGridHeader2Center);
			csGridHeader2CenterFloat1.setDataFormat(formatFloat1);
		}		
		//--------------------------------------------------------------HEADER: csGridHeader3 유형(태두리 있음)
		{
			//csGridHeader3
			csGridHeader3.setFont(fontBold);
			csGridHeader3.setWrapText(true);
			csGridHeader3.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridHeader3.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridHeader3.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridHeader3.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridHeader3.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridHeader3.setFillForegroundColor(new XSSFColor(new byte[] {(byte)162,(byte)187,(byte)215}));
			csGridHeader3.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridHeader3Left
			csGridHeader3Left.cloneStyleFrom(csGridHeader3);
			csGridHeader3Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridHeader3LeftMoney.cloneStyleFrom(csGridHeader3Left);
			csGridHeader3LeftMoney.setDataFormat(formatMoney);
			csGridHeader3LeftPercent.cloneStyleFrom(csGridHeader3Left);
			csGridHeader3LeftPercent.setDataFormat(formatPercent);
			csGridHeader3LeftFloat.cloneStyleFrom(csGridHeader3Left);
			csGridHeader3LeftFloat.setDataFormat(formatFloat);
			csGridHeader3LeftFloat1.cloneStyleFrom(csGridHeader3Left);
			csGridHeader3LeftFloat1.setDataFormat(formatFloat1);
			//csGridHeader3Right
			csGridHeader3Right.cloneStyleFrom(csGridHeader3);
			csGridHeader3Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridHeader3RightMoney.cloneStyleFrom(csGridHeader3Right);
			csGridHeader3RightMoney.setDataFormat(formatMoney);
			csGridHeader3RightPercent.cloneStyleFrom(csGridHeader3Right);
			csGridHeader3RightPercent.setDataFormat(formatPercent);
			csGridHeader3RightFloat.cloneStyleFrom(csGridHeader3Right);
			csGridHeader3RightFloat.setDataFormat(formatFloat);
			csGridHeader3RightFloat1.cloneStyleFrom(csGridHeader3Right);
			csGridHeader3RightFloat1.setDataFormat(formatFloat1);
			//csGridHeader3Center
			csGridHeader3Center.cloneStyleFrom(csGridHeader3);
			csGridHeader3Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridHeader3CenterMoney.cloneStyleFrom(csGridHeader3Center);
			csGridHeader3CenterMoney.setDataFormat(formatMoney);
			csGridHeader3CenterPercent.cloneStyleFrom(csGridHeader3Center);
			csGridHeader3CenterPercent.setDataFormat(formatPercent);
			csGridHeader3CenterFloat.cloneStyleFrom(csGridHeader3Center);
			csGridHeader3CenterFloat.setDataFormat(formatFloat);
			csGridHeader3CenterFloat1.cloneStyleFrom(csGridHeader3Center);
			csGridHeader3CenterFloat1.setDataFormat(formatFloat1);
		}			
		//--------------------------------------------------------------HEADER: csGridHeader4 유형(태두리 있음)
		{
			//csGridHeader4
			csGridHeader4.setFont(fontBold);
			csGridHeader4.setWrapText(true);
			csGridHeader4.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridHeader4.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridHeader4.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridHeader4.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridHeader4.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridHeader4.setFillForegroundColor(new XSSFColor(new byte[] {(byte)145,(byte)170,(byte)198}));
			csGridHeader4.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridHeader4Left
			csGridHeader4Left.cloneStyleFrom(csGridHeader4);
			csGridHeader4Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridHeader4LeftMoney.cloneStyleFrom(csGridHeader4Left);
			csGridHeader4LeftMoney.setDataFormat(formatMoney);
			csGridHeader4LeftPercent.cloneStyleFrom(csGridHeader4Left);
			csGridHeader4LeftPercent.setDataFormat(formatPercent);
			csGridHeader4LeftFloat.cloneStyleFrom(csGridHeader4Left);
			csGridHeader4LeftFloat.setDataFormat(formatFloat);
			csGridHeader4LeftFloat1.cloneStyleFrom(csGridHeader4Left);
			csGridHeader4LeftFloat1.setDataFormat(formatFloat1);
			//csGridHeader4Right
			csGridHeader4Right.cloneStyleFrom(csGridHeader4);
			csGridHeader4Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridHeader4RightMoney.cloneStyleFrom(csGridHeader4Right);
			csGridHeader4RightMoney.setDataFormat(formatMoney);
			csGridHeader4RightPercent.cloneStyleFrom(csGridHeader4Right);
			csGridHeader4RightPercent.setDataFormat(formatPercent);
			csGridHeader4RightFloat.cloneStyleFrom(csGridHeader4Right);
			csGridHeader4RightFloat.setDataFormat(formatFloat);
			csGridHeader4RightFloat1.cloneStyleFrom(csGridHeader4Right);
			csGridHeader4RightFloat1.setDataFormat(formatFloat1);
			//csGridHeader4Center
			csGridHeader4Center.cloneStyleFrom(csGridHeader4);
			csGridHeader4Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridHeader4CenterMoney.cloneStyleFrom(csGridHeader4Center);
			csGridHeader4CenterMoney.setDataFormat(formatMoney);
			csGridHeader4CenterPercent.cloneStyleFrom(csGridHeader4Center);
			csGridHeader4CenterPercent.setDataFormat(formatPercent);
			csGridHeader4CenterFloat.cloneStyleFrom(csGridHeader4Center);
			csGridHeader4CenterFloat.setDataFormat(formatFloat);
			csGridHeader4CenterFloat1.cloneStyleFrom(csGridHeader4Center);
			csGridHeader4CenterFloat1.setDataFormat(formatFloat1);
		}	
		//----------------------------------------------------------------------BODY: csGrid 유형(태두리 있음)
		{
			//csGrid
			csGrid.setFont(fontDefault);
			csGrid.setWrapText(true);
			csGrid.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGrid.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGrid.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGrid.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGrid.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			//csGridLeft
			csGridLeft.cloneStyleFrom(csGrid);
			csGridLeft.setAlignment(CellStyle.ALIGN_LEFT);
			csGridLeftMoney.cloneStyleFrom(csGridLeft);
			csGridLeftMoney.setDataFormat(formatMoney);
			csGridLeftPercent.cloneStyleFrom(csGridLeft);
			csGridLeftPercent.setDataFormat(formatPercent);
			csGridLeftFloat.cloneStyleFrom(csGridLeft);
			csGridLeftFloat.setDataFormat(formatFloat);
			csGridLeftFloat1.cloneStyleFrom(csGridLeft);
			csGridLeftFloat1.setDataFormat(formatFloat1);
			//csGridRight
			csGridRight.cloneStyleFrom(csGrid);
			csGridRight.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridRightMoney.cloneStyleFrom(csGridRight);
			csGridRightMoney.setDataFormat(formatMoney);
			csGridRightPercent.cloneStyleFrom(csGridRight);
			csGridRightPercent.setDataFormat(formatPercent);
			csGridRightFloat.cloneStyleFrom(csGridRight);
			csGridRightFloat.setDataFormat(formatFloat);
			csGridRightFloat1.cloneStyleFrom(csGridRight);
			csGridRightFloat1.setDataFormat(formatFloat1);
			//csGridCenter
			csGridCenter.cloneStyleFrom(csGrid);
			csGridCenter.setAlignment(CellStyle.ALIGN_CENTER);
			csGridCenterMoney.cloneStyleFrom(csGridCenter);
			csGridCenterMoney.setDataFormat(formatMoney);
			csGridCenterPercent.cloneStyleFrom(csGridCenter);
			csGridCenterPercent.setDataFormat(formatPercent);
			csGridCenterFloat.cloneStyleFrom(csGridCenter);
			csGridCenterFloat.setDataFormat(formatFloat);
			csGridCenterFloat1.cloneStyleFrom(csGridCenter);
			csGridCenterFloat1.setDataFormat(formatFloat1);
		}	
		//--------------------------------------------------------------------BG1: csGridBg1 유형(태두리 있음)
		{
			//csGridBg1
			csGridBg1.setFont(fontDefault);
			csGridBg1.setWrapText(true); //개행 허용
			csGridBg1.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridBg1.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridBg1.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridBg1.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridBg1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridBg1.setFillForegroundColor(new XSSFColor(new byte[] {(byte)238,(byte)238,(byte)238}));
			csGridBg1.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridBg1Left
			csGridBg1Left.cloneStyleFrom(csGridBg1);
			csGridBg1Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridBg1LeftMoney.cloneStyleFrom(csGridBg1Left);
			csGridBg1LeftMoney.setDataFormat(formatMoney);
			csGridBg1LeftPercent.cloneStyleFrom(csGridBg1Left);
			csGridBg1LeftPercent.setDataFormat(formatPercent);
			csGridBg1LeftFloat.cloneStyleFrom(csGridBg1Left);
			csGridBg1LeftFloat.setDataFormat(formatFloat);
			csGridBg1LeftFloat1.cloneStyleFrom(csGridBg1Left);
			csGridBg1LeftFloat1.setDataFormat(formatFloat1);
			//csGridBg1Right
			csGridBg1Right.cloneStyleFrom(csGridBg1);
			csGridBg1Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridBg1RightMoney.cloneStyleFrom(csGridBg1Right);
			csGridBg1RightMoney.setDataFormat(formatMoney);
			csGridBg1RightPercent.cloneStyleFrom(csGridBg1Right);
			csGridBg1RightPercent.setDataFormat(formatPercent);
			csGridBg1RightFloat.cloneStyleFrom(csGridBg1Right);
			csGridBg1RightFloat.setDataFormat(formatFloat);
			csGridBg1RightFloat1.cloneStyleFrom(csGridBg1Right);
			csGridBg1RightFloat1.setDataFormat(formatFloat1);
			//csGridBg1Center
			csGridBg1Center.cloneStyleFrom(csGridBg1);
			csGridBg1Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridBg1CenterMoney.cloneStyleFrom(csGridBg1Center);
			csGridBg1CenterMoney.setDataFormat(formatMoney);
			csGridBg1CenterPercent.cloneStyleFrom(csGridBg1Center);
			csGridBg1CenterPercent.setDataFormat(formatPercent);
			csGridBg1CenterFloat.cloneStyleFrom(csGridBg1Center);
			csGridBg1CenterFloat.setDataFormat(formatFloat);
			csGridBg1CenterFloat1.cloneStyleFrom(csGridBg1Center);
			csGridBg1CenterFloat1.setDataFormat(formatFloat1);
		}	
		//-------------------------------------------------------------------BG2: csGridBg2 유형(태두리 있음)
		{
			//csGridBg2
			csGridBg2.setFont(fontDefault);
			csGridBg2.setWrapText(true);
			csGridBg2.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridBg2.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridBg2.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridBg2.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridBg2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridBg2.setFillForegroundColor(new XSSFColor(new byte[] {(byte)221,(byte)221,(byte)221}));
			csGridBg2.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridBg2Left
			csGridBg2Left.cloneStyleFrom(csGridBg2);
			csGridBg2Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridBg2LeftMoney.cloneStyleFrom(csGridBg2Left);
			csGridBg2LeftMoney.setDataFormat(formatMoney);
			csGridBg2LeftPercent.cloneStyleFrom(csGridBg2Left);
			csGridBg2LeftPercent.setDataFormat(formatPercent);
			csGridBg2LeftFloat.cloneStyleFrom(csGridBg2Left);
			csGridBg2LeftFloat.setDataFormat(formatFloat);
			csGridBg2LeftFloat1.cloneStyleFrom(csGridBg2Left);
			csGridBg2LeftFloat1.setDataFormat(formatFloat1);
			//csGridBg2Right
			csGridBg2Right.cloneStyleFrom(csGridBg2);
			csGridBg2Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridBg2RightMoney.cloneStyleFrom(csGridBg2Right);
			csGridBg2RightMoney.setDataFormat(formatMoney);
			csGridBg2RightPercent.cloneStyleFrom(csGridBg2Right);
			csGridBg2RightPercent.setDataFormat(formatPercent);
			csGridBg2RightFloat.cloneStyleFrom(csGridBg2Right);
			csGridBg2RightFloat.setDataFormat(formatFloat);
			csGridBg2RightFloat1.cloneStyleFrom(csGridBg2Right);
			csGridBg2RightFloat1.setDataFormat(formatFloat1);
			//csGridBg2Center
			csGridBg2Center.cloneStyleFrom(csGridBg2);
			csGridBg2Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridBg2CenterMoney.cloneStyleFrom(csGridBg2Center);
			csGridBg2CenterMoney.setDataFormat(formatMoney);
			csGridBg2CenterPercent.cloneStyleFrom(csGridBg2Center);
			csGridBg2CenterPercent.setDataFormat(formatPercent);
			csGridBg2CenterFloat.cloneStyleFrom(csGridBg2Center);
			csGridBg2CenterFloat.setDataFormat(formatFloat);
			csGridBg2CenterFloat1.cloneStyleFrom(csGridBg2Center);
			csGridBg2CenterFloat1.setDataFormat(formatFloat1);
		}	
		//-------------------------------------------------------------------BG3: csGridBg3 유형(태두리 있음)
		{
			//csGridBg3
			csGridBg3.setFont(fontDefault);
			csGridBg3.setWrapText(true);
			csGridBg3.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridBg3.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridBg3.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridBg3.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridBg3.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridBg3.setFillForegroundColor(new XSSFColor(new byte[] {(byte)204,(byte)204,(byte)204}));
			csGridBg3.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridBg3Left
			csGridBg3Left.cloneStyleFrom(csGridBg3);
			csGridBg3Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridBg3LeftMoney.cloneStyleFrom(csGridBg3Left);
			csGridBg3LeftMoney.setDataFormat(formatMoney);
			csGridBg3LeftPercent.cloneStyleFrom(csGridBg3Left);
			csGridBg3LeftPercent.setDataFormat(formatPercent);
			csGridBg3LeftFloat.cloneStyleFrom(csGridBg3Left);
			csGridBg3LeftFloat.setDataFormat(formatFloat);
			csGridBg3LeftFloat1.cloneStyleFrom(csGridBg3Left);
			csGridBg3LeftFloat1.setDataFormat(formatFloat1);
			//csGridBg3Right
			csGridBg3Right.cloneStyleFrom(csGridBg3);
			csGridBg3Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridBg3RightMoney.cloneStyleFrom(csGridBg3Right);
			csGridBg3RightMoney.setDataFormat(formatMoney);
			csGridBg3RightPercent.cloneStyleFrom(csGridBg3Right);
			csGridBg3RightPercent.setDataFormat(formatPercent);
			csGridBg3RightFloat.cloneStyleFrom(csGridBg3Right);
			csGridBg3RightFloat.setDataFormat(formatFloat);
			csGridBg3RightFloat1.cloneStyleFrom(csGridBg3Right);
			csGridBg3RightFloat1.setDataFormat(formatFloat1);
			//csGridBg3Center
			csGridBg3Center.cloneStyleFrom(csGridBg3);
			csGridBg3Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridBg3CenterMoney.cloneStyleFrom(csGridBg3Center);
			csGridBg3CenterMoney.setDataFormat(formatMoney);
			csGridBg3CenterPercent.cloneStyleFrom(csGridBg3Center);
			csGridBg3CenterPercent.setDataFormat(formatPercent);
			csGridBg3CenterFloat.cloneStyleFrom(csGridBg3Center);
			csGridBg3CenterFloat.setDataFormat(formatFloat);
			csGridBg3CenterFloat1.cloneStyleFrom(csGridBg3Center);
			csGridBg3CenterFloat1.setDataFormat(formatFloat1);
		}
		//-------------------------------------------------------------------BG4: csGridBg4 유형(태두리 있음)
		{
			//csGridBg4
			csGridBg4.setFont(fontDefault);
			csGridBg4.setWrapText(true);
			csGridBg4.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridBg4.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridBg4.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridBg4.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridBg4.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridBg4.setFillForegroundColor(new XSSFColor(new byte[] {(byte)187,(byte)187,(byte)187}));
			csGridBg4.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridBg4Left
			csGridBg4Left.cloneStyleFrom(csGridBg4);
			csGridBg4Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridBg4LeftMoney.cloneStyleFrom(csGridBg4Left);
			csGridBg4LeftMoney.setDataFormat(formatMoney);
			csGridBg4LeftPercent.cloneStyleFrom(csGridBg4Left);
			csGridBg4LeftPercent.setDataFormat(formatPercent);
			csGridBg4LeftFloat.cloneStyleFrom(csGridBg4Left);
			csGridBg4LeftFloat.setDataFormat(formatFloat);
			csGridBg4LeftFloat1.cloneStyleFrom(csGridBg4Left);
			csGridBg4LeftFloat1.setDataFormat(formatFloat1);
			//csGridBg4Right
			csGridBg4Right.cloneStyleFrom(csGridBg4);
			csGridBg4Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridBg4RightMoney.cloneStyleFrom(csGridBg4Right);
			csGridBg4RightMoney.setDataFormat(formatMoney);
			csGridBg4RightPercent.cloneStyleFrom(csGridBg4Right);
			csGridBg4RightPercent.setDataFormat(formatPercent);
			csGridBg4RightFloat.cloneStyleFrom(csGridBg4Right);
			csGridBg4RightFloat.setDataFormat(formatFloat);
			csGridBg4RightFloat1.cloneStyleFrom(csGridBg4Right);
			csGridBg4RightFloat1.setDataFormat(formatFloat1);
			//csGridBg4Center
			csGridBg4Center.cloneStyleFrom(csGridBg4);
			csGridBg4Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridBg4CenterMoney.cloneStyleFrom(csGridBg4Center);
			csGridBg4CenterMoney.setDataFormat(formatMoney);
			csGridBg4CenterPercent.cloneStyleFrom(csGridBg4Center);
			csGridBg4CenterPercent.setDataFormat(formatPercent);
			csGridBg4CenterFloat.cloneStyleFrom(csGridBg4Center);
			csGridBg4CenterFloat.setDataFormat(formatFloat);
			csGridBg4CenterFloat1.cloneStyleFrom(csGridBg4Center);
			csGridBg4CenterFloat1.setDataFormat(formatFloat1);
		}
		//-------------------------------------------------------------------BG5: csGridBg5 유형(태두리 있음)
		{
			//csGridBg5
			csGridBg5.setFont(fontDefault);
			csGridBg5.setWrapText(true);
			csGridBg5.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridBg5.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridBg5.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridBg5.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridBg5.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridBg5.setFillForegroundColor(new XSSFColor(new byte[] {(byte)170,(byte)170,(byte)170}));
			csGridBg5.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridBg5Left
			csGridBg5Left.cloneStyleFrom(csGridBg5);
			csGridBg5Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridBg5LeftMoney.cloneStyleFrom(csGridBg5Left);
			csGridBg5LeftMoney.setDataFormat(formatMoney);
			csGridBg5LeftPercent.cloneStyleFrom(csGridBg5Left);
			csGridBg5LeftPercent.setDataFormat(formatPercent);
			csGridBg5LeftFloat.cloneStyleFrom(csGridBg5Left);
			csGridBg5LeftFloat.setDataFormat(formatFloat);
			csGridBg5LeftFloat1.cloneStyleFrom(csGridBg5Left);
			csGridBg5LeftFloat1.setDataFormat(formatFloat1);
			//csGridBg5Right
			csGridBg5Right.cloneStyleFrom(csGridBg5);
			csGridBg5Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridBg5RightMoney.cloneStyleFrom(csGridBg5Right);
			csGridBg5RightMoney.setDataFormat(formatMoney);
			csGridBg5RightPercent.cloneStyleFrom(csGridBg5Right);
			csGridBg5RightPercent.setDataFormat(formatPercent);
			csGridBg5RightFloat.cloneStyleFrom(csGridBg5Right);
			csGridBg5RightFloat.setDataFormat(formatFloat);
			csGridBg5RightFloat1.cloneStyleFrom(csGridBg5Right);
			csGridBg5RightFloat1.setDataFormat(formatFloat1);
			//csGridBg5Center
			csGridBg5Center.cloneStyleFrom(csGridBg5);
			csGridBg5Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridBg5CenterMoney.cloneStyleFrom(csGridBg5Center);
			csGridBg5CenterMoney.setDataFormat(formatMoney);
			csGridBg5CenterPercent.cloneStyleFrom(csGridBg5Center);
			csGridBg5CenterPercent.setDataFormat(formatPercent);
			csGridBg5CenterFloat.cloneStyleFrom(csGridBg5Center);
			csGridBg5CenterFloat.setDataFormat(formatFloat);
			csGridBg5CenterFloat1.cloneStyleFrom(csGridBg5Center);
			csGridBg5CenterFloat1.setDataFormat(formatFloat1);
		}
		//-------------------------------------------------------------------BG5: csGridBg6 유형(태두리 있음)
		{
			//csGridBg6
			csGridBg6.setFont(fontDefault);
			csGridBg6.setWrapText(true);
			csGridBg6.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridBg6.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridBg6.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridBg6.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridBg6.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridBg6.setFillForegroundColor(new XSSFColor(new byte[] {(byte)153,(byte)153,(byte)153}));
			csGridBg6.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridBg6Left
			csGridBg6Left.cloneStyleFrom(csGridBg6);
			csGridBg6Left.setAlignment(CellStyle.ALIGN_LEFT);
			csGridBg6LeftMoney.cloneStyleFrom(csGridBg6Left);
			csGridBg6LeftMoney.setDataFormat(formatMoney);
			csGridBg6LeftPercent.cloneStyleFrom(csGridBg6Left);
			csGridBg6LeftPercent.setDataFormat(formatPercent);
			csGridBg6LeftFloat.cloneStyleFrom(csGridBg6Left);
			csGridBg6LeftFloat.setDataFormat(formatFloat);
			csGridBg6LeftFloat1.cloneStyleFrom(csGridBg6Left);
			csGridBg6LeftFloat1.setDataFormat(formatFloat1);
			//csGridBg6Right
			csGridBg6Right.cloneStyleFrom(csGridBg6);
			csGridBg6Right.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridBg6RightMoney.cloneStyleFrom(csGridBg6Right);
			csGridBg6RightMoney.setDataFormat(formatMoney);
			csGridBg6RightPercent.cloneStyleFrom(csGridBg6Right);
			csGridBg6RightPercent.setDataFormat(formatPercent);
			csGridBg6RightFloat.cloneStyleFrom(csGridBg6Right);
			csGridBg6RightFloat.setDataFormat(formatFloat);
			csGridBg6RightFloat1.cloneStyleFrom(csGridBg6Right);
			csGridBg6RightFloat1.setDataFormat(formatFloat1);
			//csGridBg6Center
			csGridBg6Center.cloneStyleFrom(csGridBg6);
			csGridBg6Center.setAlignment(CellStyle.ALIGN_CENTER);
			csGridBg6CenterMoney.cloneStyleFrom(csGridBg6Center);
			csGridBg6CenterMoney.setDataFormat(formatMoney);
			csGridBg6CenterPercent.cloneStyleFrom(csGridBg6Center);
			csGridBg6CenterPercent.setDataFormat(formatPercent);
			csGridBg6CenterFloat.cloneStyleFrom(csGridBg6Center);
			csGridBg6CenterFloat.setDataFormat(formatFloat);
			csGridBg6CenterFloat1.cloneStyleFrom(csGridBg6Center);
			csGridBg6CenterFloat1.setDataFormat(formatFloat1);
		}
		//------------------------------------------------------------FOOTER: csGridFooter 유형(태두리 있음)
		{
			//csGridFooter
			csGridFooter.setFont(fontDefault);
			csGridFooter.setWrapText(true);
			csGridFooter.setBorderTop(XSSFCellStyle.BORDER_THIN); 
			csGridFooter.setBorderRight(XSSFCellStyle.BORDER_THIN);              
			csGridFooter.setBorderBottom(XSSFCellStyle.BORDER_THIN); 
			csGridFooter.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
			csGridFooter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csGridBg3.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
			csGridBg3.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//csGridFooterLeft
			csGridFooterLeft.cloneStyleFrom(csGridFooter);
			csGridFooterLeft.setAlignment(CellStyle.ALIGN_LEFT);
			csGridFooterLeftMoney.cloneStyleFrom(csGridFooterLeft);
			csGridFooterLeftMoney.setDataFormat(formatMoney);
			csGridFooterLeftPercent.cloneStyleFrom(csGridFooterLeft);
			csGridFooterLeftPercent.setDataFormat(formatPercent);
			csGridFooterLeftFloat.cloneStyleFrom(csGridFooterLeft);
			csGridFooterLeftFloat.setDataFormat(formatFloat);
			csGridFooterLeftFloat1.cloneStyleFrom(csGridFooterLeft);
			csGridFooterLeftFloat1.setDataFormat(formatFloat1);
			//csGridFooterRight
			csGridFooterRight.cloneStyleFrom(csGridFooter);
			csGridFooterRight.setAlignment(CellStyle.ALIGN_RIGHT);
			csGridFooterRightMoney.cloneStyleFrom(csGridFooterRight);
			csGridFooterRightMoney.setDataFormat(formatMoney);
			csGridFooterRightPercent.cloneStyleFrom(csGridFooterRight);
			csGridFooterRightPercent.setDataFormat(formatPercent);
			csGridFooterRightFloat.cloneStyleFrom(csGridFooterRight);
			csGridFooterRightFloat.setDataFormat(formatFloat);
			csGridFooterRightFloat1.cloneStyleFrom(csGridFooterRight);
			csGridFooterRightFloat1.setDataFormat(formatFloat1);
			//csGridFooterCenter
			csGridFooterCenter.cloneStyleFrom(csGridFooter);
			csGridFooterCenter.setAlignment(CellStyle.ALIGN_CENTER);
			csGridFooterCenterMoney.cloneStyleFrom(csGridFooterCenter);
			csGridFooterCenterMoney.setDataFormat(formatMoney);
			csGridFooterCenterPercent.cloneStyleFrom(csGridFooterCenter);
			csGridFooterCenterPercent.setDataFormat(formatPercent);
			csGridFooterCenterFloat.cloneStyleFrom(csGridFooterCenter);
			csGridFooterCenterFloat.setDataFormat(formatFloat);
			csGridFooterCenterFloat1.cloneStyleFrom(csGridFooterCenter);
			csGridFooterCenterFloat1.setDataFormat(formatFloat1);
		}
	} 

	//스타일 반환하기
	public CellStyle getCellStyles(String part, Map<String, String> cfg){
		String type = cfg.get("type");
		String align = cfg.get("align");
		//logger.info("part: "+ part +", type: "+ type +", align: "+ align);
		if(part.equalsIgnoreCase("PAGENAME")){
			return this.csPageName;
		}else if(part.equalsIgnoreCase("PAGENAMEBORDER")){
			return this.csPageNameBorder;			
		}else if(part.equalsIgnoreCase("PLAINBG")){
			return this.csPlainBg;			
		}else if(part.equalsIgnoreCase("PLAINLEFTBORDERLEFTSOLID1PXRED")){
			return this.csPlainLeftBorderLeftSolid1pxRed;			
		}else if(part.equalsIgnoreCase("PLAINBIGBOLD")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainBigBoldLeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainBigBoldLeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainBigBoldLeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainBigBoldLeftFloat1;
				else return this.csPlainBigBoldLeft;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainBigBoldRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainBigBoldRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainBigBoldRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainBigBoldRightFloat1;
				else return this.csPlainBigBoldRight;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainBigBoldCenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainBigBoldCenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainBigBoldCenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainBigBoldCenterFloat1;
				else return this.csPlainBigBoldCenter;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainBigBoldLeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainBigBoldLeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainBigBoldLeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainBigBoldLeftFloat1;
				else return this.csPlainBigBoldLeft;
			}
		}else if(part.equalsIgnoreCase("PLAIN")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainLeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainLeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainLeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainLeftFloat1;
				else return this.csPlainLeft;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainRightFloat1;
				else return this.csPlainRight;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainCenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainCenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainCenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainCenterFloat1;
				else return this.csPlainCenter;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainRightFloat1;
				else return this.csPlainLeft;
			}
		}else if(part.equalsIgnoreCase("PLAINBOLD")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainBoldLeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainBoldLeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainBoldLeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainBoldLeftFloat1;
				else return this.csPlainBoldLeft;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainBoldRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainBoldRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainBoldRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainBoldRightFloat1;
				else return this.csPlainBoldRight;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainBoldCenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainBoldCenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainBoldCenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainBoldCenterFloat1;
				else return this.csPlainBoldCenter;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csPlainBoldRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csPlainBoldRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csPlainBoldRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csPlainBoldRightFloat1;
				else return this.csPlainBoldLeft;
			}
		}else if(part.equalsIgnoreCase("BORDER")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csBorderLeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csBorderLeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csBorderLeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csBorderLeftFloat1;
				else return this.csBorderLeft;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csBorderRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csBorderRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csBorderRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csBorderRightFloat1;
				else return this.csBorderRight;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csBorderCenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csBorderCenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csBorderCenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csBorderCenterFloat1;
				else return this.csBorderCenter;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csBorderRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csBorderRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csBorderRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csBorderRightFloat1;
				else return this.csBorderLeft;
			}
		}else if(part.equalsIgnoreCase("HEADER") || part.equalsIgnoreCase("HEADER1")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader1LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader1LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader1LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader1LeftFloat1;
				else return this.csGridHeader1Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader1RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader1RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader1RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader1RightFloat1;
				else return this.csGridHeader1Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader1CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader1CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader1CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader1CenterFloat1;
				else return this.csGridHeader1Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader1CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader1CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader1CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader1CenterFloat1;
				else return this.csGridHeader1Center;
			}
		}else if(part.equalsIgnoreCase("HEADER2")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader2LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader2LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader2LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader2LeftFloat1;
				else return this.csGridHeader2Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader2RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader2RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader2RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader2RightFloat1;
				else return this.csGridHeader2Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader2CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader2CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader2CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader2CenterFloat1;
				else return this.csGridHeader2Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader2CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader2CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader2CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader2CenterFloat1;
				else return this.csGridHeader2Center;
			}
		}else if(part.equalsIgnoreCase("HEADER3")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader3LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader3LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader3LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader3LeftFloat1;
				else return this.csGridHeader3Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader3RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader3RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader3RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader3RightFloat1;
				else return this.csGridHeader3Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader3CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader3CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader3CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader3CenterFloat1;
				else return this.csGridHeader3Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader3CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader3CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader3CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader3CenterFloat1;
				else return this.csGridHeader3Center;
			}
		}else if(part.equalsIgnoreCase("HEADER4")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader4LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader4LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader4LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader4LeftFloat1;
				else return this.csGridHeader4Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader4RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader4RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader4RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader4RightFloat1;
				else return this.csGridHeader4Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader4CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader4CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader4CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader4CenterFloat1;
				else return this.csGridHeader4Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridHeader4CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridHeader4CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridHeader4CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridHeader4CenterFloat1;
				else return this.csGridHeader4Center;
			}
		}else if(part.equalsIgnoreCase("BODY")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridLeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridLeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridLeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridLeftFloat1;
				else return this.csGridLeft;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridRightFloat1;
				else return this.csGridRight;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridCenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridCenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridCenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridCenterFloat1;
				else return this.csGridCenter;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridRightFloat1;
				else return this.csGridLeft;
			}
		}else if(part.equalsIgnoreCase("BG1")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg1LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg1LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg1LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg1LeftFloat1;
				else return this.csGridBg1Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg1RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg1RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg1RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg1RightFloat1;
				else return this.csGridBg1Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg1CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg1CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg1CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg1CenterFloat1;
				else return this.csGridBg1Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg1RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg1RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg1RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg1RightFloat1;
				else return this.csGridBg1Left;
			}
		}else if(part.equalsIgnoreCase("BG2")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg2LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg2LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg2LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg2LeftFloat1;
				else return this.csGridBg2Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg2RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg2RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg2RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg2RightFloat1;
				else return this.csGridBg2Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg2CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg2CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg2CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg2CenterFloat1;
				else return this.csGridBg2Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg2RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg2RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg2RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg2RightFloat1;
				else return this.csGridBg2Left;
			}
		}else if(part.equalsIgnoreCase("BG3")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg3LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg3LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg3LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg3LeftFloat1;
				else return this.csGridBg3Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg3RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg3RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg3RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg3RightFloat1;
				else return this.csGridBg3Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg3CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg3CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg3CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg3CenterFloat1;
				else return this.csGridBg3Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg3RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg3RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg3RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg3RightFloat1;
				else return this.csGridBg3Left;
			}
		}else if(part.equalsIgnoreCase("BG4")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg4LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg4LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg4LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg4LeftFloat1;
				else return this.csGridBg4Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg4RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg4RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg4RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg4RightFloat1;
				else return this.csGridBg4Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg4CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg4CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg4CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg4CenterFloat1;
				else return this.csGridBg4Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg4RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg4RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg4RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg4RightFloat1;
				else return this.csGridBg4Left;
			}
		}else if(part.equalsIgnoreCase("BG5")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg5LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg5LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg5LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg5LeftFloat1;
				else return this.csGridBg5Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg5RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg5RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg5RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg5RightFloat1;
				else return this.csGridBg5Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg5CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg5CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg5CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg5CenterFloat1;
				else return this.csGridBg5Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg5RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg5RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg5RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg5RightFloat1;
				else return this.csGridBg5Left;
			}
		}else if(part.equalsIgnoreCase("BG6")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg6LeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg6LeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg6LeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg6LeftFloat1;
				else return this.csGridBg6Left;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg6RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg6RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg6RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg6RightFloat1;
				else return this.csGridBg6Right;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg6CenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg6CenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg6CenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg6CenterFloat1;
				else return this.csGridBg6Center;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridBg6RightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridBg6RightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridBg6RightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridBg6RightFloat1;
				else return this.csGridBg6Left;
			}
		}else if(part.equalsIgnoreCase("FOOTER")){
			if(align!=null && align.equalsIgnoreCase("LEFT")){//default
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridFooterLeftMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridFooterLeftPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridFooterLeftFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridFooterLeftFloat1;
				else return this.csGridFooterLeft;
			}else if(align!=null && align.equalsIgnoreCase("RIGHT")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridFooterRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridFooterRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridFooterRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridFooterRightFloat1;
				else return this.csGridFooterRight;
			}else if(align!=null && align.equalsIgnoreCase("CENTER")){
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridFooterCenterMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridFooterCenterPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridFooterCenterFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridFooterCenterFloat1;
				else return this.csGridFooterCenter;
			}else{
				if(type!=null && type.equalsIgnoreCase("MONEY")) return this.csGridFooterRightMoney;
				else if(type!=null && type.equalsIgnoreCase("PERCENT")) return this.csGridFooterRightPercent;
				else if(type!=null && type.equalsIgnoreCase("FLOAT")) return this.csGridFooterRightFloat;
				else if(type!=null && type.equalsIgnoreCase("FLOAT1")) return this.csGridFooterRightFloat1;
				else return this.csGridFooterLeft;
			}
		}
		return null;
	}
}

