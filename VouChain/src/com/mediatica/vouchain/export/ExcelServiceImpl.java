package com.mediatica.vouchain.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.TransactionDTO;
import com.mediatica.vouchain.entities.User;

@Service
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Transactional
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class ExcelServiceImpl implements ExcelServiceInterface {
	
	@Value("${font_name}")
	private String fontName;
	
	@Value("${font_height}")
	private String fontHeight;
	
	@Value("${color_excel_row_1}")
	private short colorExcelRow1;

	@Value("${color_excel_row_2}")
	private short colorExcelRow2;
	
	@Autowired
	UserDaoImpl userDaoImpl;

	@Override
	public byte[] exportTransaction(List<TransactionDTO> transactionList, String usrId, String startDate, String endDate) {

		try {
			String pathTemplate = Constants.VOUCHAIN_HOME+Constants.EXCEL_TEMPLATE_SUBDIR_PATH+Constants.EXCEL_TEMPLATE_FILE_NAME;
			FileInputStream inputStream = new FileInputStream(new File(pathTemplate));

			
			//create workbook		
		    Workbook wb = new XSSFWorkbook(inputStream);
		    
						
			//Create sheet
			Sheet sheet1 = wb.getSheet("transaction");
			
			//parameters for modify table position
			int rowIndex =Constants.INITIAL_ROW_POSTION_FOR_TABLE;
			int initialcellIndex =Constants.INITIAL_CELL_POSTION_FOR_TABLE;
			

			//sum the all trcValue
			double trcValueTot = manageTrcValue(transactionList);			
			
			//create note head with applicant data, date from and date to
			createNoteHead(usrId, startDate, endDate, wb, sheet1);				
			
			//create header (columns titles)
			createTableHeader(sheet1, rowIndex, initialcellIndex, wb);
			
			
//			CellStyle styleCenterAlign = wb.getCellStyle();
//			styleCenterAlign.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			
			//for each transaction add a row
			int lastRowIndex = addTransactionsRows(transactionList, sheet1, rowIndex, initialcellIndex, null,wb);
			
			//add total transaction value
			addTotalTransactionValue(wb, sheet1, lastRowIndex, initialcellIndex, trcValueTot);		

			
			//autosize for all colums
			autoSizeColumn(sheet1, initialcellIndex);

			//generate excel bytes array
			byte[] excelBytes = generateBytesArray(wb);
			
			//TEST: generate xlsx output
			ByteArrayInputStream bis = new ByteArrayInputStream(excelBytes);
			XSSFWorkbook workbook = new XSSFWorkbook(bis);
			FileOutputStream fileOut;
			String path = Constants.VOUCHAIN_HOME+Constants.PDF_EXPORT_SUBDIR_PATH+(new Date()).getTime()+".xlsx";
			System.out.println("path where to write: "+path);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
						
			return excelBytes;
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}



	private void createNoteHead(String usrId, String startDate, String endDate, Workbook wb, Sheet sheet1) {
		User user = new User();
		String reportApplicant = "";
		
		if(usrId!=null) {				
			user = userDaoImpl.findByPrimaryKey(user, Integer.valueOf(usrId));
		}
		
		if(user!=null && user.getUsrId()!=null) {
			if(user.getCompany()!=null && user.getCompany().getCpyRagioneSociale()!=null) {
				reportApplicant = "Company: " + user.getCompany().getCpyRagioneSociale();
			}
			else if(user.getMerchant()!=null && user.getMerchant().getMrcRagioneSociale()!=null) {
				reportApplicant = "Merchant: " + user.getMerchant().getMrcRagioneSociale();
			}
			else if(user.getEmployee()!=null && user.getEmployee().getEmpLastName()!=null) {
				reportApplicant = "Employee: " + user.getEmployee().getEmpFirstName() + " " + user.getEmployee().getEmpLastName();
			}
		}


		
		//Create a new font and alter it.
//		XSSFFont font= (XSSFFont) wb.createFont();
//		font.setFontHeightInPoints(Short.valueOf(fontHeight));
//		font.setFontName(fontName);
//		font.setBold(true);
//		font.setItalic(false);
//		//fix
//		XSSFFont whiteFont= (XSSFFont) wb.createFont();
//		whiteFont.setFontHeightInPoints(Short.valueOf(fontHeight));
//		whiteFont.setFontName(fontName);
//		whiteFont.setBold(true);
//		whiteFont.setItalic(false);
//		whiteFont.setColor(HSSFColor.WHITE.index);

		
		
		
		
		//Create style and set font into style
//		CellStyle styleForColumn1 = wb.getCellStyle();
//		styleForColumn1.setFont(whiteFont);	
//		styleForColumn1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//
//
//		styleForColumn1.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
//		styleForColumn1.setFillPattern(XSSFCellStyle.LESS_DOTS);
//
//
//		
//		CellStyle styleForColumn2 = wb.getCellStyle();
//		styleForColumn2.setAlignment(XSSFCellStyle.ALIGN_CENTER);	

		Row row1 = sheet1.getRow(0);				
		row1.setHeightInPoints(Constants.ROW_HEIGHT_IN_POINT);	
		Cell cell1 = row1.getCell(1);								
		cell1.setCellValue("Report Applicant");
//		cell1.setCellStyle(styleForColumn1);
		Cell cell2 = row1.getCell(2);
		cell2.setCellValue(reportApplicant);	
//		cell2.setCellStyle(styleForColumn2);

		
		Row row2 = sheet1.getRow(1);				
		row2.setHeightInPoints(Constants.ROW_HEIGHT_IN_POINT);			
		Cell cell3 = row2.getCell(1);								
		cell3.setCellValue("Date From");
//		cell3.setCellStyle(styleForColumn1);
		Cell cell4 = row2.getCell(2);
		cell4.setCellValue(startDate);
//		cell4.setCellStyle(styleForColumn2);

		
		Row row3 = sheet1.getRow(2);				
		row3.setHeightInPoints(Constants.ROW_HEIGHT_IN_POINT);			
		Cell cell5 = row3.getCell(1);								
		cell5.setCellValue("Date To");
//		cell5.setCellStyle(styleForColumn1);
		Cell cell6 = row3.getCell(2);
		cell6.setCellValue(endDate);
//		cell6.setCellStyle(styleForColumn2);

		
		sheet1.autoSizeColumn(0);
		sheet1.autoSizeColumn(1);
	}
	

	
	private void createTableHeader(Sheet sheet1, int i, int initialcellIndex, Workbook wb) {		
		
		
		//Create a new font and alter it.
	    XSSFFont font= (XSSFFont) wb.createFont();
	    font.setFontHeightInPoints(Short.valueOf(fontHeight));
	    font.setFontName(fontName);
	    font.setBold(true);
	    font.setItalic(false);
	    
		XSSFFont whiteFont= (XSSFFont) wb.createFont();
		whiteFont.setFontHeightInPoints(Short.valueOf(fontHeight));
		whiteFont.setFontName(fontName);
		whiteFont.setBold(true);
		whiteFont.setItalic(false);
		whiteFont.setColor(HSSFColor.WHITE.index);

		//Create style and set font into style
//		CellStyle style = wb.getCellStyle();
//		style.setFont(font);	
//		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(XSSFCellStyle.ALIGN_CENTER);

//		CellStyle style = wb.getCellStyle();
//		style.setFont(whiteFont);	
//		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(XSSFCellStyle.ALIGN_CENTER);
//		style.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
//		style.setFillPattern(XSSFCellStyle.LESS_DOTS);
		
		int cellIndex = initialcellIndex;
		
		Row rowHeader = sheet1.getRow(i-1);	
		
		rowHeader.setHeightInPoints(Constants.ROW_HEIGHT_IN_POINT);	
		
		Cell cell2 = rowHeader.getCell(cellIndex);								
		cell2.setCellValue("Transaction ID");
//		cell2.setCellStyle(style);

		Cell cell4 = rowHeader.getCell(++cellIndex);
		cell4.setCellValue("User (From)");				
//		cell4.setCellStyle(style);

		Cell cell5 = rowHeader.getCell(++cellIndex);
		cell5.setCellValue("User (To)");				
//		cell5.setCellStyle(style);

		Cell cell6 = rowHeader.getCell(++cellIndex);
		cell6.setCellValue("Transaction type");	
//		cell6.setCellStyle(style);
		
		Cell cell7 = rowHeader.getCell(++cellIndex);
		cell7.setCellValue("Transaction date");	
//		cell7.setCellStyle(style);
		
		Cell cell9 = rowHeader.getCell(++cellIndex);
		cell9.setCellValue("Transaction IBAN");		
//		cell9.setCellStyle(style);
		
		Cell cell10 = rowHeader.getCell(++cellIndex);
		cell10.setCellValue("Transaction payed");	
//		cell10.setCellStyle(style);
		
		Cell cell12 = rowHeader.getCell(++cellIndex);
		cell12.setCellValue("Transaction value "  + Constants.EURO_SYMBOL + ":");
//		cell12.setCellStyle(style);
		
//		Cell cell13 = rowHeader.getCell(++cellIndex);
//		cell13.setCellValue("QR Code causal");
//		cell13.setCellStyle(style);
		
//		Cell cell14 = rowHeader.getCell(++cellIndex);
//		cell14.setCellValue("QR Code value");
//		cell14.setCellStyle(style);
	
	}



	private int addTransactionsRows(List<TransactionDTO> transactionList, Sheet sheet1, int rowIndex,
			int initialcellIndex, CellStyle styleCenterAlign2,Workbook wb) {
		CellStyle styleEuro = null;
		styleEuro = wb.createCellStyle();
		styleEuro.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styleEuro.setFillPattern(XSSFCellStyle.LESS_DOTS);

		
		for(TransactionDTO transaction : transactionList) {	
			
			int cellIndex = initialcellIndex;
			CellStyle styleCenterAlign = null;
			styleCenterAlign = wb.createCellStyle();
			styleCenterAlign.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			styleCenterAlign.setFillPattern(XSSFCellStyle.LESS_DOTS);
			if((rowIndex %2)==0){
				styleCenterAlign.setFillBackgroundColor(colorExcelRow2);
				styleEuro.setFillBackgroundColor(colorExcelRow2);
			}else {
  			    styleCenterAlign.setFillBackgroundColor(colorExcelRow1);
  			    styleEuro.setFillBackgroundColor(colorExcelRow1);
			}

			Row row = sheet1.createRow(rowIndex);					
		    //set row height in points
		    row.setHeightInPoints(Constants.ROW_HEIGHT_IN_POINT);			
		    
			Cell cell2 = row.createCell(cellIndex);								
			cell2.setCellValue(transaction.getTrcId());	
			cell2.setCellStyle(styleCenterAlign);

			
			Cell cell4 = row.createCell(++cellIndex);
			cell4.setCellValue(transaction.getUsrIdDa());	
			cell4.setCellStyle(styleCenterAlign);

			
			Cell cell5 = row.createCell(++cellIndex);
			cell5.setCellValue(transaction.getUsrIdA());	
			cell5.setCellStyle(styleCenterAlign);

			
			Cell cell6 = row.createCell(++cellIndex);
			cell6.setCellValue(transaction.getTrcType());	
			cell6.setCellStyle(styleCenterAlign);

			
			Cell cell7 = row.createCell(++cellIndex);
			cell7.setCellValue(transaction.getTrcDate());	
			cell7.setCellStyle(styleCenterAlign);

			
			Cell cell9 = row.createCell(++cellIndex);
			cell9.setCellValue(transaction.getTrcIban());	
			cell9.setCellStyle(styleCenterAlign);

			
			Cell cell10 = row.createCell(++cellIndex);
			cell10.setCellValue(transaction.getTrcPayed());	
			cell10.setCellStyle(styleCenterAlign);
			
			DataFormat datafrmt = wb.createDataFormat();
		    styleCenterAlign.setDataFormat(datafrmt.getFormat("#,##0.00")); 
			//styleEuro.setDataFormat(datafrmt.getFormat("#.##0,00")); 
			Cell cell12 = row.createCell(++cellIndex);
			cell12.setCellValue(Double.parseDouble(transaction.getTrcValue()));
			cell12.setCellStyle(styleCenterAlign);
			/*
			if(transaction.getTrcQrCausale()!=null) {
				Cell cell13 = row.createCell(++cellIndex);
				cell13.setCellValue(transaction.getTrcQrCausale());	
				cell13.setCellStyle(styleCenterAlign);
				
				Cell cell14 = row.createCell(++cellIndex);
				cell14.setCellValue(transaction.getTrcQrValue());	
				cell14.setCellStyle(styleCenterAlign);
			}
			else {
				Cell cell13 = row.createCell(++cellIndex);
				cell13.setCellStyle(styleCenterAlign);
				
				Cell cell14 = row.createCell(++cellIndex);
				cell14.setCellStyle(styleCenterAlign);
			}
			*/
			rowIndex++;
		}
		return rowIndex;
	}



	private void addTotalTransactionValue(Workbook wb, Sheet sheet1, int rowIndex, int initialcellIndex,
			double trcValueTot) {
	
		Row row = sheet1.createRow(rowIndex+3);
		
		//Create a new font and alter it.
		XSSFFont font= (XSSFFont) wb.createFont();
		font.setFontHeightInPoints(Short.valueOf(fontHeight));
		font.setFontName(fontName);
		font.setBold(true);
		font.setItalic(false);
		font.setColor(HSSFColor.WHITE.index);

		//Create style and set font into style
		CellStyle style = wb.createCellStyle();
		style.setFont(font);	
		style.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
		style.setFillPattern(XSSFCellStyle.LESS_DOTS);
		
		
		CellStyle styleValue = wb.createCellStyle();
		//CellStyle styleCenterAlign = wb.createCellStyle();
		styleValue.setBorderBottom(HSSFBorderFormatting.BORDER_DOUBLE);
		styleValue.setBorderLeft(HSSFBorderFormatting.BORDER_DOUBLE);
		styleValue.setBorderRight(HSSFBorderFormatting.BORDER_DOUBLE);
		styleValue.setBorderTop(HSSFBorderFormatting.BORDER_DOUBLE);
		
		
		Cell cell1 = row.createCell(initialcellIndex);								
		cell1.setCellValue("Total transactions value "  + Constants.EURO_SYMBOL + ":"  );
		cell1.setCellStyle(style);
		
		DataFormat datafrmt = wb.createDataFormat();
		styleValue.setDataFormat(datafrmt.getFormat("#,##0.00")); 
		
		Cell cell2 = row.createCell(initialcellIndex+1);								
		cell2.setCellValue(trcValueTot);
		cell2.setCellStyle(styleValue);
 
		
	}
	



 


	private double manageTrcValue(List<TransactionDTO> transactionList) {
		double trcValueTot = 0;
		for(TransactionDTO transaction : transactionList) {	
			if(transaction.getTrcValue()!=null) {
				trcValueTot+=Double.parseDouble(transaction.getTrcValue());
			}
		}
		return trcValueTot;
	}



	private byte[] generateBytesArray(Workbook wb) throws IOException {	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		wb.write(baos);
		byte[] xls = baos.toByteArray();
		baos.close();
		return xls;
	}

	private void autoSizeColumn(Sheet sheet1, int initialcellIndex) {		
		int columnIndex = initialcellIndex;		
		sheet1.autoSizeColumn(columnIndex);
		sheet1.autoSizeColumn(++columnIndex);
		sheet1.autoSizeColumn(++columnIndex);
		sheet1.autoSizeColumn(++columnIndex);
		sheet1.autoSizeColumn(++columnIndex);
		sheet1.autoSizeColumn(++columnIndex);
		sheet1.autoSizeColumn(++columnIndex);
		sheet1.autoSizeColumn(++columnIndex);
	
	}



}
