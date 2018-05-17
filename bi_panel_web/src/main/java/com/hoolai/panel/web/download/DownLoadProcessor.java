package com.hoolai.panel.web.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hoolai.bi.report.model.entity.AnalysisGDT;
import com.hoolai.bi.report.model.entity.ClientDailyReport;
import com.hoolai.bi.report.model.entity.DailyReport;
import com.hoolai.bi.report.model.entity.GameLtv;
import com.hoolai.bi.report.model.entity.Games;
import com.hoolai.bi.report.model.entity.SourceDailyReport;
import com.hoolai.bi.report.model.entity.SummaryGDT;
import com.hoolai.bi.report.vo.ClientDailyReportVO;
import com.hoolai.bi.report.vo.UserRetentionLtvVO;

public class DownLoadProcessor {

	private static DecimalFormat df = new DecimalFormat("#0.00");
	
	private static final String templeteFileName = "templates/report_template.xlsx";
	
	private static int dailyReportCurrencyHeader[] = {4,11,12,14,16,17};
	
	private static int sourceDailyReportCurrencyHearder[] = {5,9,8};
	
	public enum WorkbookType{
		XLSX("xlsx"),XLS("xls");
		
		private String type;
		private WorkbookType(String type){
			this.type = type;
		}
		
		private String value(){
			return type;
		}
		
		public static WorkbookType convertToWorkbookType(String type){
			for(WorkbookType workbookType:WorkbookType.values()){
				if(workbookType.value().equals(type)){
					return workbookType;
				}
			}
			throw new RuntimeException();
		}
	}
	
	private Workbook wb;
	private  Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
	
	private WorkbookType workbookType;
	
	private Games games;
	private List<DailyReport> dailyReports;
	private List<SourceDailyReport> sourceDailyReports;
	private List<ClientDailyReport> clientDailyReports;
	private List<ClientDailyReportVO> clientAllInstalls;
	
	private List<UserRetentionLtvVO> userRetentions_install;
	private List<UserRetentionLtvVO> userLtvs_install;
	private List<UserRetentionLtvVO> userRetentions_role;
	private List<UserRetentionLtvVO> userLtvs_role;
	private List<UserRetentionLtvVO> userRetentionsLtvInstall;
	private List<UserRetentionLtvVO> userRetentionsLtvInstallUserCount;
	
	private List<SummaryGDT> summaryGDTs;
	private List<AnalysisGDT> analysisGDTs;
	
	public DownLoadProcessor(WorkbookType workbookType){
		this.workbookType = workbookType;
		readTemplete();
		createStyles();
	}
	
	public void initData(Games games,List<DailyReport> dailyReports,
                         List<SourceDailyReport> sourceDailyReports,
                         List<ClientDailyReport> clientDailyReports,
                         List<ClientDailyReportVO> clientAllInstalls,
			             List<UserRetentionLtvVO> userRetentions_install,
			             List<UserRetentionLtvVO> userLtvs_install,
			             List<UserRetentionLtvVO> userRetentions_role,
			             List<UserRetentionLtvVO> userLtvs_role,
			             List<UserRetentionLtvVO> userRetentionsLtvInstall, 
			             List<UserRetentionLtvVO> userRetentionsLtvInstallUserCount, 
			             List<SummaryGDT> summaryGDTs,
			             List<AnalysisGDT> analysisGDTs){
		this.games = games;
		this.dailyReports = dailyReports;
		this.sourceDailyReports = sourceDailyReports;
		this.clientDailyReports = clientDailyReports;
		this.userRetentions_install = userRetentions_install;
		this.userLtvs_install = userLtvs_install;
		this.userRetentions_role = userRetentions_role;
		this.userLtvs_role = userLtvs_role;
		this.userRetentionsLtvInstall = userRetentionsLtvInstall;
		this.userRetentionsLtvInstallUserCount = userRetentionsLtvInstallUserCount;
		this.summaryGDTs = summaryGDTs;
		this.analysisGDTs = analysisGDTs;
		this.clientAllInstalls = clientAllInstalls;
	}
	
	public void readyDownload(){
		createDailyReportsSheet();
		createSourceDailyReportsSheet();
		createInstallLtvSheet();
		createInstallUserRetentionSheet();
		createRoleLtvSheet();
		createRoleUserRetentionSheet();
		createClientDailyReportsSheet();
		createClientUserRetentionLtvSheet();
		createSummaryGDTsSheet();
		createAnalysisGDTsSheet();
	}
	
	public void writeBook(HttpServletResponse response) {
		String fileName = games.getName() + "_运营日报.xls";
		if (wb instanceof XSSFWorkbook)
			fileName += "x";
		OutputStream out;
		try {
			out = response.getOutputStream();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType("application/msexcel;charset=UTF-8");
			wb.write(out);
			wb.close();
			out.close();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readTemplete(){
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		InputStream strem = null;
		strem = loader.getResourceAsStream(templeteFileName);//new FileInputStream(templeteFileName);
		
		switch (this.workbookType) {
		case XLS:
			try {
				POIFSFileSystem fs = new POIFSFileSystem(strem);//
				wb = new HSSFWorkbook(fs);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case XLSX:
		default:
			try {
				wb = new XSSFWorkbook(strem);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			break;
		}
	}
	
	private void createDailyReportsSheet() {
		Sheet zonglanSheet = wb.getSheetAt(0);
		
		Row headerRow = zonglanSheet.getRow(0);
		for(int colNum:dailyReportCurrencyHeader){
			Cell headerCell = headerRow.getCell(colNum);
			headerCell.setCellValue(headerCell.getStringCellValue()+"("+games.getCurrency()+")");
		}
		
		Row dataRow;
		Row templeteRow = zonglanSheet.getRow(1);
		int rownum = 2;
		
		for (DailyReport report : dailyReports) {
			UserRetentionLtvVO userRetention_install = this.getInstallUserRetention(report.getDay());
			UserRetentionLtvVO userRetention_role = this.getRoleUserRetention(report.getDay());
			
			Object data[] = {
					report.getDay(),
					report.getDnu(),
					report.getRoleChoice() == null ? '-' : report.getRoleChoice(),
					report.getDau(),
					Math.round((report.getPaymentAmount() / games.getRate()) * 100d)/100d,
					report.getPu(),
					Math.round(report.getPayRate() * 10000d)/10000d,
				   (userRetention_install == null || userRetention_role.getGameLtv().getInstall() == null)
				   			? "-" : Math.round(userRetention_install.getGameLtv().getD1() * 10000d)/10000d,
				   (userRetention_install == null || userRetention_role.getGameLtv().getInstall() == null)
				   			? "-" : Math.round(userRetention_install.getGameLtv().getD7() * 10000d)/10000d,
				   (userRetention_role == null || userRetention_role.getGameLtv().getInstall() == null || userRetention_role.getGameLtv().getInstall() == 0)
							? "-" : Math.round(userRetention_role.getGameLtv().getD1() * 10000d)/10000d,
				   (userRetention_role == null || userRetention_role.getGameLtv().getInstall() == null || userRetention_role.getGameLtv().getInstall() == 0)
				   			? "-" : Math.round(userRetention_role.getGameLtv().getD7() * 10000d)/10000d,
				   Math.round((report.getArpu() / games.getRate())*100d)/100d,
				   Math.round((report.getArppu() / games.getRate())*100d)/100d,
				   report.getInstallPu(),
				   Math.round((report.getInstallPayAmount() / games.getRate())*100d)/100d,
				   (report.getDnu() != null && report.getDnu() != 0) 
				   			? Math.round((Double.valueOf(report.getInstallPu()) / Double.valueOf(report.getDnu())) * 10000d)/10000d : 0,
				   (report.getDnu() != null && report.getDnu() != 0) 
				   			? Math.round((report.getInstallPayAmount() / Double.valueOf(report.getDnu())/ games.getRate())*100d)/100d : 0,
				   (report.getInstallPu() != null && report.getInstallPu() != 0) 
				   			? Math.round((report.getInstallPayAmount() / Double.valueOf(report.getInstallPu())/ games.getRate())*100d)/100d : 0,
				   report.getDailyOldUser(),
				   (report.getOldPayment() != null && report.getOldPayment() != 0)
				   			? Math.round((Double.valueOf(report.getOldPayment())/games.getRate()) * 100d)/100d : 0,
				   report.getOldPayer(),
				   (report.getOldPayer() != null && report.getOldPayer() != 0)
				   			? Math.round((Double.valueOf(report.getOldPayer()) / Double.valueOf(report.getDailyOldUser())) * 10000d)/10000d : 0,
				   (report.getOldArpu() != null && report.getOldArpu() != 0)
		   					? Math.round((report.getOldArpu() / games.getRate())*100d)/100d : 0,
				   (report.getOldArppu() != null && report.getOldArppu() != 0)
				   			? Math.round((report.getOldArppu() / games.getRate())*100d)/100d : 0,
				   (report.getLtv0() != null && report.getLtv0() != 0)
				   			? Math.round((Double.valueOf(report.getLtv0())/games.getRate()) * 10000d)/10000d : 0,
				   (report.getLtv1() != null && report.getLtv1() != 0)
						   	? Math.round((Double.valueOf(report.getLtv1())/games.getRate()) * 10000d)/10000d : 0,
				   (report.getLtv7() != null && report.getLtv7() != 0)
							? Math.round((Double.valueOf(report.getLtv7())/games.getRate()) * 10000d)/10000d : 0
			};
			
			dataRow = zonglanSheet.createRow(rownum++);
			dataRow.setHeight(templeteRow.getHeight());
			dataRow.setRowStyle(templeteRow.getRowStyle());
			createRow(dataRow, templeteRow, data);
		}
		zonglanSheet.shiftRows(2, zonglanSheet.getLastRowNum()+1, -1);
	}

	private void createRow(Row dataRow, Row templeteRow, Object[] data) {
		Cell dataCell;
		for (int i = 0; i < data.length; i++) {
			dataCell=dataRow.createCell(i);
			dataCell.setCellType(templeteRow.getCell(i).getCellType());
			dataCell.setCellStyle(templeteRow.getCell(i).getCellStyle());
			if(data[i] instanceof String){
				dataCell.setCellValue((String)data[i]);
			}else if(data[i] instanceof Integer){
				dataCell.setCellValue((Integer)data[i]);
			}else if(data[i] instanceof Long){
				dataCell.setCellValue((Long)data[i]);
			}else{
				dataCell.setCellValue(Double.valueOf(String.valueOf(data[i])));
			}
		}
	}
	
	private void createSourceDailyReportsSheet() {
		Sheet sourceSheet = wb.getSheetAt(2);

		Row headerRow = sourceSheet.getRow(0);
		for(int colNum:sourceDailyReportCurrencyHearder){
			Cell headerCell = headerRow.getCell(colNum);
			headerCell.setCellValue(headerCell.getStringCellValue()+"("+games.getCurrency()+")");
		}
		
		Row dataRow;
		Row templeteRow = sourceSheet.getRow(1);
		int rownum = 2;
		for (SourceDailyReport report : sourceDailyReports) {
			
			Object data[] = {
					report.getDay(),
					report.getSource(),
					report.getDnu(),
					report.getRoleChoice(),
					report.getDau(),	
					Double.valueOf(df.format(report.getPaymentAmount() / games.getRate())),	
					report.getPu(),
					Math.floor(report.getPayRate() * 10000) / 10000,
					(Math.floor((report.getArpu() / games.getRate()) * 100))/100,
					Double.valueOf(df.format(report.getArppu() / games.getRate()))
			};
			
			dataRow = sourceSheet.createRow(rownum);
			dataRow.setHeight(templeteRow.getHeight());
			dataRow.setRowStyle(templeteRow.getRowStyle());
			
			createRow(dataRow, templeteRow, data);
			
			rownum++;
		}
		sourceSheet.shiftRows(2, sourceSheet.getLastRowNum()+1, -1);
	}
	
	private UserRetentionLtvVO getInstallUserRetention(String date){
		for(UserRetentionLtvVO userRetention:userRetentions_install){
			if(userRetention.getGameLtv().getInstallDay().equals(date)){
				return userRetention;
			}
		}
		return null;
	}
	
	private UserRetentionLtvVO getRoleUserRetention(String date){
		for(UserRetentionLtvVO userRetention:userRetentions_role){
			if(userRetention.getGameLtv().getInstallDay().equals(date)){
				return userRetention;
			}
		}
		return null;
	}
	
	private void createInstallUserRetentionSheet() {
		Sheet retentionSheet = wb.getSheetAt(6);//"按注册-留存"
		
		Row templeteRow = retentionSheet.getRow(1);
		int rownum = 2;
		for (UserRetentionLtvVO retention : userRetentions_install) {
			createRetention(retentionSheet, templeteRow, rownum, retention);
			rownum ++;
		}
		retentionSheet.shiftRows(2, retentionSheet.getLastRowNum()+1, -1);
	}

	private void createRetention(Sheet retentionSheet, Row templeteRow,
			int rownum, UserRetentionLtvVO retention) {
		Row dataRow;
		GameLtv gameLtv = retention.getGameLtv();
		Cell dataCell;
		
		dataRow = retentionSheet.createRow(rownum);
		dataRow.setHeight(templeteRow.getHeight());
		dataRow.setRowStyle(templeteRow.getRowStyle());
		
		dataCell = dataRow.createCell(0);
		dataCell.setCellType(templeteRow.getCell(0).getCellType());
		dataCell.setCellStyle(templeteRow.getCell(0).getCellStyle());
		dataCell.setCellValue(gameLtv.getInstallDay());
		
		dataCell = dataRow.createCell(1);
		dataCell.setCellType(templeteRow.getCell(1).getCellType());
		dataCell.setCellStyle(templeteRow.getCell(1).getCellStyle());
		if(gameLtv.getInstall() != null){
		dataCell.setCellValue(gameLtv.getInstall());}
		
		if(gameLtv.getInstall() == null || gameLtv.getInstall() == 0){
			for(int i=2;i<templeteRow.getLastCellNum();i++){
				dataCell = dataRow.createCell(i);
				dataCell.setCellType(templeteRow.getCell(i).getCellType());
				dataCell.setCellStyle(templeteRow.getCell(i).getCellStyle());
				dataCell.setCellValue("-");
			}
		}else{
			Double data[] = createRetention(gameLtv);
			for(int i=2;i<templeteRow.getLastCellNum();i++){
				dataCell = dataRow.createCell(i);
				dataCell.setCellType(templeteRow.getCell(i).getCellType());
				dataCell.setCellStyle(templeteRow.getCell(i).getCellStyle());
				if(data[i-2] != null){
					dataCell.setCellValue(data[i-2]);
				}else{
					dataCell.setCellValue("-");
				}
			}
		}
	}
	
	private void createRoleUserRetentionSheet() {
		Sheet retentionSheet = wb.getSheetAt(4);//"按创角-留存"
		
		Row templeteRow = retentionSheet.getRow(1);
		int rownum = 2;
		for (UserRetentionLtvVO retention : userRetentions_role) {
			createRetention(retentionSheet, templeteRow, rownum, retention);
			rownum ++;
		}
		retentionSheet.shiftRows(2, retentionSheet.getLastRowNum()+1, -1);
	}
	
	private Double[] createRetention(GameLtv gameLtv){
		Double data[] = {
				gameLtv.getD1() == null ? null : ((Math.floor(gameLtv.getD1() * 10000))/10000),
				gameLtv.getD2() == null ? null : ((Math.floor(gameLtv.getD2() * 10000))/10000),
				gameLtv.getD3() == null ? null : ((Math.floor(gameLtv.getD3() * 10000))/10000),
				gameLtv.getD4() == null ? null : ((Math.floor(gameLtv.getD4() * 10000))/10000),
				gameLtv.getD5() == null ? null : ((Math.floor(gameLtv.getD5() * 10000))/10000),
				gameLtv.getD6() == null ? null : ((Math.floor(gameLtv.getD6() * 10000))/10000),
				gameLtv.getD7() == null ? null : ((Math.floor(gameLtv.getD7() * 10000))/10000),
				gameLtv.getD8() == null ? null : ((Math.floor(gameLtv.getD8() * 10000))/10000),
				gameLtv.getD9() == null ? null : ((Math.floor(gameLtv.getD9() * 10000))/10000),
				gameLtv.getD10() == null ? null : ((Math.floor(gameLtv.getD10() * 10000))/10000),
				gameLtv.getD11() == null ? null : ((Math.floor(gameLtv.getD11() * 10000))/10000),
				gameLtv.getD12() == null ? null : ((Math.floor(gameLtv.getD12() * 10000))/10000),
				gameLtv.getD13() == null ? null : ((Math.floor(gameLtv.getD13() * 10000))/10000),
				gameLtv.getD14() == null ? null : ((Math.floor(gameLtv.getD14() * 10000))/10000),
				gameLtv.getD15() == null ? null : ((Math.floor(gameLtv.getD15() * 10000))/10000),
				gameLtv.getD16() == null ? null : ((Math.floor(gameLtv.getD16() * 10000))/10000),
				gameLtv.getD17() == null ? null : ((Math.floor(gameLtv.getD17() * 10000))/10000),
				gameLtv.getD18() == null ? null : ((Math.floor(gameLtv.getD18() * 10000))/10000),
				gameLtv.getD19() == null ? null : ((Math.floor(gameLtv.getD19() * 10000))/10000),
				gameLtv.getD20() == null ? null : ((Math.floor(gameLtv.getD20() * 10000))/10000),
				gameLtv.getD21() == null ? null : ((Math.floor(gameLtv.getD21() * 10000))/10000),
				gameLtv.getD22() == null ? null : ((Math.floor(gameLtv.getD22() * 10000))/10000),
				gameLtv.getD23() == null ? null : ((Math.floor(gameLtv.getD23() * 10000))/10000),
				gameLtv.getD24() == null ? null : ((Math.floor(gameLtv.getD24() * 10000))/10000),
				gameLtv.getD25() == null ? null : ((Math.floor(gameLtv.getD25() * 10000))/10000),
				gameLtv.getD26() == null ? null : ((Math.floor(gameLtv.getD26() * 10000))/10000),
				gameLtv.getD27() == null ? null : ((Math.floor(gameLtv.getD27() * 10000))/10000),
				gameLtv.getD28() == null ? null : ((Math.floor(gameLtv.getD28() * 10000))/10000),
				gameLtv.getD29() == null ? null : ((Math.floor(gameLtv.getD29() * 10000))/10000),
				gameLtv.getD30() == null ? null : ((Math.floor(gameLtv.getD30() * 10000))/10000),
				gameLtv.getD31() == null ? null : ((Math.floor(gameLtv.getD31() * 10000))/10000),
				gameLtv.getD32() == null ? null : ((Math.floor(gameLtv.getD32() * 10000))/10000),
				gameLtv.getD33() == null ? null : ((Math.floor(gameLtv.getD33() * 10000))/10000),
				gameLtv.getD34() == null ? null : ((Math.floor(gameLtv.getD34() * 10000))/10000),
				gameLtv.getD35() == null ? null : ((Math.floor(gameLtv.getD35() * 10000))/10000),
				gameLtv.getD36() == null ? null : ((Math.floor(gameLtv.getD36() * 10000))/10000),
				gameLtv.getD37() == null ? null : ((Math.floor(gameLtv.getD37() * 10000))/10000),
				gameLtv.getD38() == null ? null : ((Math.floor(gameLtv.getD38() * 10000))/10000),
				gameLtv.getD39() == null ? null : ((Math.floor(gameLtv.getD39() * 10000))/10000),
				gameLtv.getD40() == null ? null : ((Math.floor(gameLtv.getD40() * 10000))/10000),
				gameLtv.getD41() == null ? null : ((Math.floor(gameLtv.getD41() * 10000))/10000),
				gameLtv.getD42() == null ? null : ((Math.floor(gameLtv.getD42() * 10000))/10000),
				gameLtv.getD43() == null ? null : ((Math.floor(gameLtv.getD43() * 10000))/10000),
				gameLtv.getD44() == null ? null : ((Math.floor(gameLtv.getD44() * 10000))/10000),
				gameLtv.getD45() == null ? null : ((Math.floor(gameLtv.getD45() * 10000))/10000),
				gameLtv.getD46() == null ? null : ((Math.floor(gameLtv.getD46() * 10000))/10000),
				gameLtv.getD47() == null ? null : ((Math.floor(gameLtv.getD47() * 10000))/10000),
				gameLtv.getD48() == null ? null : ((Math.floor(gameLtv.getD48() * 10000))/10000),
				gameLtv.getD49() == null ? null : ((Math.floor(gameLtv.getD49() * 10000))/10000),
				gameLtv.getD50() == null ? null : ((Math.floor(gameLtv.getD50() * 10000))/10000),
				gameLtv.getD51() == null ? null : ((Math.floor(gameLtv.getD51() * 10000))/10000),
				gameLtv.getD52() == null ? null : ((Math.floor(gameLtv.getD52() * 10000))/10000),
				gameLtv.getD53() == null ? null : ((Math.floor(gameLtv.getD53() * 10000))/10000),
				gameLtv.getD54() == null ? null : ((Math.floor(gameLtv.getD54() * 10000))/10000),
				gameLtv.getD55() == null ? null : ((Math.floor(gameLtv.getD55() * 10000))/10000),
				gameLtv.getD56() == null ? null : ((Math.floor(gameLtv.getD56() * 10000))/10000),
				gameLtv.getD57() == null ? null : ((Math.floor(gameLtv.getD57() * 10000))/10000),
				gameLtv.getD58() == null ? null : ((Math.floor(gameLtv.getD58() * 10000))/10000),
				gameLtv.getD59() == null ? null : ((Math.floor(gameLtv.getD59() * 10000))/10000),
				gameLtv.getD60() == null ? null : ((Math.floor(gameLtv.getD60() * 10000))/10000),
				gameLtv.getD61() == null ? null : ((Math.floor(gameLtv.getD61() * 10000))/10000),
				gameLtv.getD62() == null ? null : ((Math.floor(gameLtv.getD62() * 10000))/10000),
				gameLtv.getD63() == null ? null : ((Math.floor(gameLtv.getD63() * 10000))/10000),
				gameLtv.getD64() == null ? null : ((Math.floor(gameLtv.getD64() * 10000))/10000),
				gameLtv.getD65() == null ? null : ((Math.floor(gameLtv.getD65() * 10000))/10000),
				gameLtv.getD66() == null ? null : ((Math.floor(gameLtv.getD66() * 10000))/10000),
				gameLtv.getD67() == null ? null : ((Math.floor(gameLtv.getD67() * 10000))/10000),
				gameLtv.getD68() == null ? null : ((Math.floor(gameLtv.getD68() * 10000))/10000),
				gameLtv.getD69() == null ? null : ((Math.floor(gameLtv.getD69() * 10000))/10000),
				gameLtv.getD70() == null ? null : ((Math.floor(gameLtv.getD70() * 10000))/10000),
				gameLtv.getD71() == null ? null : ((Math.floor(gameLtv.getD71() * 10000))/10000),
				gameLtv.getD72() == null ? null : ((Math.floor(gameLtv.getD72() * 10000))/10000),
				gameLtv.getD73() == null ? null : ((Math.floor(gameLtv.getD73() * 10000))/10000),
				gameLtv.getD74() == null ? null : ((Math.floor(gameLtv.getD74() * 10000))/10000),
				gameLtv.getD75() == null ? null : ((Math.floor(gameLtv.getD75() * 10000))/10000),
				gameLtv.getD76() == null ? null : ((Math.floor(gameLtv.getD76() * 10000))/10000),
				gameLtv.getD77() == null ? null : ((Math.floor(gameLtv.getD77() * 10000))/10000),
				gameLtv.getD78() == null ? null : ((Math.floor(gameLtv.getD78() * 10000))/10000),
				gameLtv.getD79() == null ? null : ((Math.floor(gameLtv.getD79() * 10000))/10000),
				gameLtv.getD80() == null ? null : ((Math.floor(gameLtv.getD80() * 10000))/10000),
				gameLtv.getD81() == null ? null : ((Math.floor(gameLtv.getD81() * 10000))/10000),
				gameLtv.getD82() == null ? null : ((Math.floor(gameLtv.getD82() * 10000))/10000),
				gameLtv.getD83() == null ? null : ((Math.floor(gameLtv.getD83() * 10000))/10000),
				gameLtv.getD84() == null ? null : ((Math.floor(gameLtv.getD84() * 10000))/10000),
				gameLtv.getD85() == null ? null : ((Math.floor(gameLtv.getD85() * 10000))/10000),
				gameLtv.getD86() == null ? null : ((Math.floor(gameLtv.getD86() * 10000))/10000),
				gameLtv.getD87() == null ? null : ((Math.floor(gameLtv.getD87() * 10000))/10000),
				gameLtv.getD88() == null ? null : ((Math.floor(gameLtv.getD88() * 10000))/10000),
				gameLtv.getD89() == null ? null : ((Math.floor(gameLtv.getD89() * 10000))/10000),
				gameLtv.getD90() == null ? null : ((Math.floor(gameLtv.getD90() * 10000))/10000)
		};
		return data;
	}

	private void createInstallLtvSheet() {
		Sheet ltvSheet = wb.getSheetAt(5);//"按注册-注收比"

		Row headerRow = ltvSheet.getRow(0);
		for (int i = 2; i < headerRow.getLastCellNum(); i++) {
			Cell headerCell = headerRow.getCell(i);
			headerCell.setCellValue(headerCell.getStringCellValue() + "("+games.getCurrency()+")");
		}
		
		Row templeteRow = ltvSheet.getRow(1);
		int rownum = 2;
		for (UserRetentionLtvVO ltv : userLtvs_install) {
			createLtv(ltvSheet, templeteRow, rownum, ltv);
			rownum ++;
		}
		ltvSheet.shiftRows(2, ltvSheet.getLastRowNum()+1, -1);
	}

	private void createLtv(Sheet ltvSheet, Row templeteRow, int rownum,
			UserRetentionLtvVO ltv) {
		GameLtv gameLtv = ltv.getGameLtv();
		Cell dataCell;
		
		Row dataRow = ltvSheet.createRow(rownum);
		dataRow.setHeight(templeteRow.getHeight());
		dataRow.setRowStyle(templeteRow.getRowStyle());
		
		dataCell = dataRow.createCell(0);
		dataCell.setCellType(templeteRow.getCell(0).getCellType());
		dataCell.setCellStyle(templeteRow.getCell(0).getCellStyle());
		dataCell.setCellValue(gameLtv.getInstallDay());
		
		dataCell = dataRow.createCell(1);
		dataCell.setCellType(templeteRow.getCell(1).getCellType());
		dataCell.setCellStyle(templeteRow.getCell(1).getCellStyle());
		if(gameLtv.getInstall() != null){
			dataCell.setCellValue(gameLtv.getInstall());
		}
		Integer install = gameLtv.getInstall();
		if(install == null || install.intValue() == 0){
			for(int i=2;i<templeteRow.getLastCellNum();i++){
				dataCell = dataRow.createCell(i);
				dataCell.setCellType(templeteRow.getCell(i).getCellType());
				dataCell.setCellStyle(templeteRow.getCell(i).getCellStyle());
				dataCell.setCellValue("-");
			}
		}else{
			Double[] data = createLtv(gameLtv);
			for(int i=2;i<templeteRow.getLastCellNum();i++){
				dataCell = dataRow.createCell(i);
				dataCell.setCellType(templeteRow.getCell(i).getCellType());
				dataCell.setCellStyle(templeteRow.getCell(i).getCellStyle());
				if(data[i-2] != null){
					dataCell.setCellValue(data[i-2]);
				}else{
					dataCell.setCellValue("-");
				}
			}
		}
	}
	
	private void createRoleLtvSheet() {
		Sheet ltvSheet = wb.getSheetAt(3);//"按创角-注收比"
		
		Row headerRow = ltvSheet.getRow(0);
		for (int i = 2; i < headerRow.getLastCellNum(); i++) {
			Cell headerCell = headerRow.getCell(i);
			headerCell.setCellValue(headerCell.getStringCellValue() + "("+games.getCurrency()+")");
		}
		
		Row templeteRow = ltvSheet.getRow(1);
		int rownum = 2;
		for (UserRetentionLtvVO ltv : userLtvs_role) {
			createLtv(ltvSheet, templeteRow, rownum, ltv);
			rownum ++;
		}
		ltvSheet.shiftRows(2, ltvSheet.getLastRowNum()+1, -1);
	}
	
	private Double[] createLtv(GameLtv gameLtv){
		Double[] data = {
				gameLtv.getD0() == null ? null : ((Math.floor(gameLtv.getD0() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD1() == null ? null : ((Math.floor(gameLtv.getD1() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD2() == null ? null : ((Math.floor(gameLtv.getD2() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD3() == null ? null : ((Math.floor(gameLtv.getD3() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD4() == null ? null : ((Math.floor(gameLtv.getD4() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD5() == null ? null : ((Math.floor(gameLtv.getD5() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD6() == null ? null : ((Math.floor(gameLtv.getD6() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD7() == null ? null : ((Math.floor(gameLtv.getD7() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD8() == null ? null : ((Math.floor(gameLtv.getD8() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD9() == null ? null : ((Math.floor(gameLtv.getD9() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD10() == null ? null : ((Math.floor(gameLtv.getD10() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD11() == null ? null : ((Math.floor(gameLtv.getD11() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD12() == null ? null : ((Math.floor(gameLtv.getD12() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD13() == null ? null : ((Math.floor(gameLtv.getD13() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD14() == null ? null : ((Math.floor(gameLtv.getD14() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD15() == null ? null : ((Math.floor(gameLtv.getD15() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD16() == null ? null : ((Math.floor(gameLtv.getD16() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD17() == null ? null : ((Math.floor(gameLtv.getD17() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD18() == null ? null : ((Math.floor(gameLtv.getD18() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD19() == null ? null : ((Math.floor(gameLtv.getD19() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD20() == null ? null : ((Math.floor(gameLtv.getD20() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD21() == null ? null : ((Math.floor(gameLtv.getD21() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD22() == null ? null : ((Math.floor(gameLtv.getD22() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD23() == null ? null : ((Math.floor(gameLtv.getD23() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD24() == null ? null : ((Math.floor(gameLtv.getD24() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD25() == null ? null : ((Math.floor(gameLtv.getD25() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD26() == null ? null : ((Math.floor(gameLtv.getD26() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD27() == null ? null : ((Math.floor(gameLtv.getD27() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD28() == null ? null : ((Math.floor(gameLtv.getD28() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD29() == null ? null : ((Math.floor(gameLtv.getD29() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD30() == null ? null : ((Math.floor(gameLtv.getD30() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD31() == null ? null : ((Math.floor(gameLtv.getD31() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD32() == null ? null : ((Math.floor(gameLtv.getD32() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD33() == null ? null : ((Math.floor(gameLtv.getD33() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD34() == null ? null : ((Math.floor(gameLtv.getD34() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD35() == null ? null : ((Math.floor(gameLtv.getD35() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD36() == null ? null : ((Math.floor(gameLtv.getD36() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD37() == null ? null : ((Math.floor(gameLtv.getD37() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD38() == null ? null : ((Math.floor(gameLtv.getD38() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD39() == null ? null : ((Math.floor(gameLtv.getD39() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD40() == null ? null : ((Math.floor(gameLtv.getD40() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD41() == null ? null : ((Math.floor(gameLtv.getD41() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD42() == null ? null : ((Math.floor(gameLtv.getD42() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD43() == null ? null : ((Math.floor(gameLtv.getD43() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD44() == null ? null : ((Math.floor(gameLtv.getD44() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD45() == null ? null : ((Math.floor(gameLtv.getD45() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD46() == null ? null : ((Math.floor(gameLtv.getD46() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD47() == null ? null : ((Math.floor(gameLtv.getD47() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD48() == null ? null : ((Math.floor(gameLtv.getD48() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD49() == null ? null : ((Math.floor(gameLtv.getD49() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD50() == null ? null : ((Math.floor(gameLtv.getD50() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD51() == null ? null : ((Math.floor(gameLtv.getD51() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD52() == null ? null : ((Math.floor(gameLtv.getD52() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD53() == null ? null : ((Math.floor(gameLtv.getD53() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD54() == null ? null : ((Math.floor(gameLtv.getD54() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD55() == null ? null : ((Math.floor(gameLtv.getD55() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD56() == null ? null : ((Math.floor(gameLtv.getD56() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD57() == null ? null : ((Math.floor(gameLtv.getD57() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD58() == null ? null : ((Math.floor(gameLtv.getD58() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD59() == null ? null : ((Math.floor(gameLtv.getD59() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD60() == null ? null : ((Math.floor(gameLtv.getD60() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD61() == null ? null : ((Math.floor(gameLtv.getD61() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD62() == null ? null : ((Math.floor(gameLtv.getD62() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD63() == null ? null : ((Math.floor(gameLtv.getD63() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD64() == null ? null : ((Math.floor(gameLtv.getD64() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD65() == null ? null : ((Math.floor(gameLtv.getD65() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD66() == null ? null : ((Math.floor(gameLtv.getD66() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD67() == null ? null : ((Math.floor(gameLtv.getD67() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD68() == null ? null : ((Math.floor(gameLtv.getD68() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD69() == null ? null : ((Math.floor(gameLtv.getD69() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD70() == null ? null : ((Math.floor(gameLtv.getD70() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD71() == null ? null : ((Math.floor(gameLtv.getD71() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD72() == null ? null : ((Math.floor(gameLtv.getD72() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD73() == null ? null : ((Math.floor(gameLtv.getD73() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD74() == null ? null : ((Math.floor(gameLtv.getD74() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD75() == null ? null : ((Math.floor(gameLtv.getD75() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD76() == null ? null : ((Math.floor(gameLtv.getD76() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD77() == null ? null : ((Math.floor(gameLtv.getD77() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD78() == null ? null : ((Math.floor(gameLtv.getD78() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD79() == null ? null : ((Math.floor(gameLtv.getD79() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD80() == null ? null : ((Math.floor(gameLtv.getD80() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD81() == null ? null : ((Math.floor(gameLtv.getD81() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD82() == null ? null : ((Math.floor(gameLtv.getD82() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD83() == null ? null : ((Math.floor(gameLtv.getD83() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD84() == null ? null : ((Math.floor(gameLtv.getD84() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD85() == null ? null : ((Math.floor(gameLtv.getD85() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD86() == null ? null : ((Math.floor(gameLtv.getD86() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD87() == null ? null : ((Math.floor(gameLtv.getD87() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD88() == null ? null : ((Math.floor(gameLtv.getD88() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD89() == null ? null : ((Math.floor(gameLtv.getD89() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100),
				gameLtv.getD90() == null ? null : ((Math.floor(gameLtv.getD90() / Double.valueOf(gameLtv.getInstall())/ games.getRate() * 100))/100)
		};
		return data;
	}
	
	private void createClientUserRetentionLtvSheet(){
		Sheet clientSheet = wb.getSheetAt(9);//"分服ltv"
		
		if(userRetentionsLtvInstall.size() < 1){
			clientSheet.getRow(0).removeCell(clientSheet.getRow(0).getCell(2));
			clientSheet.getRow(1).removeCell(clientSheet.getRow(1).getCell(2));
			clientSheet.getRow(2).removeCell(clientSheet.getRow(2).getCell(0));
			clientSheet.getRow(2).removeCell(clientSheet.getRow(2).getCell(1));
			clientSheet.getRow(2).removeCell(clientSheet.getRow(2).getCell(2));
			return ;
		}
		
		/* 读取模板  */
		Row dataRow;
		Row clientRow = clientSheet.getRow(0);
		Row installRow = clientSheet.getRow(1);
		Row templeteDataRow = clientSheet.getRow(2);
		
		Cell tempCell = clientRow.getCell(2);
		int tempCellType = tempCell.getCellType();
		CellStyle tempCellStyle = tempCell.getCellStyle();
		
		Cell installCell = installRow.getCell(2);
		int installCellType = installCell.getCellType();
		CellStyle installCellStyle = installCell.getCellStyle();
		
		Cell dateDateCell = templeteDataRow.getCell(0);
		int dateCellType = dateDateCell.getCellType();
		CellStyle dateCellStyle = dateDateCell.getCellStyle();
		
		Cell dataUnitCell = templeteDataRow.getCell(1);
		int unitCellType = dataUnitCell.getCellType();
		CellStyle unitCellStyle = dataUnitCell.getCellStyle();
		String unitValue = dataUnitCell.getStringCellValue();
		
		Cell dataLtvCell = templeteDataRow.getCell(2);
		int dataLtvCellType = dataLtvCell.getCellType();
		CellStyle dataLtvCellStyle = dataLtvCell.getCellStyle();
		
		short templeteDataRowHeight = templeteDataRow.getHeight();
		CellStyle templeteDataRowStyle = templeteDataRow.getRowStyle();
		
		Set<String> clientidSet = new HashSet<String>();
		Map<String,HashMap<String,Double>> datas = new HashMap<String, HashMap<String,Double>>();
		HashMap<String,Integer> installUserCounts = new HashMap<String,Integer>();
		
		/* 整理数据 */
		for(UserRetentionLtvVO userRetentionLtvVO : userRetentionsLtvInstall){
//			int clientid = Integer.valueOf(userRetentionLtvVO.getGameLtv().getClientId());
			String clientid = userRetentionLtvVO.getGameLtv().getClientId().toString();
			clientidSet.add(clientid);
			HashMap<String,Double> ltvDatas = datas.get(userRetentionLtvVO.getGameLtv().getInstallDay());
			if(ltvDatas == null){
				ltvDatas = new HashMap<String, Double>();
			}
			ltvDatas.put(clientid, Math.floor(userRetentionLtvVO.getGameLtv().getLtv() / games.getRate() * 100) / 100);
			datas.put(userRetentionLtvVO.getGameLtv().getInstallDay(), ltvDatas);
		}
		for(UserRetentionLtvVO userRetentionLtvVO : userRetentionsLtvInstallUserCount){
//			int clientid = Integer.valueOf(userRetentionLtvVO.getGameLtv().getClientId());
			String clientid = userRetentionLtvVO.getGameLtv().getClientId().toString();
			installUserCounts.put(clientid, userRetentionLtvVO.getGameLtv().getInstall());
		}
		
		/* 服务器ID排序 */
		Object[] clientArr = clientidSet.toArray();
		Arrays.sort(clientArr);
		
		Cell dataCell;
		
		/* 灌装服务器ID */
		int cellNum = 2;
		for(Object str:clientArr){
			String clientid = (String) str;
			
			dataCell = clientRow.createCell(cellNum);
			dataCell.setCellType(tempCellType);
			dataCell.setCellStyle(tempCellStyle);
			dataCell.setCellValue(clientid);
			
			dataCell = installRow.createCell(cellNum);
			dataCell.setCellType(installCellType);
			dataCell.setCellStyle(installCellStyle);
			dataCell.setCellValue(installUserCounts.get(clientid)==null?0:installUserCounts.get(clientid));
			
			cellNum ++;
			if(cellNum==397){
				System.out.println();
			}
		}
		
		Object[] clientDateArr = datas.keySet().toArray();
		Arrays.sort(clientDateArr);
		
		int rownum = 2;
		for(Object obj:clientDateArr){
			String date = (String) obj;
			
			dataRow = clientSheet.createRow(rownum);
			dataRow.setHeight(templeteDataRowHeight);
			dataRow.setRowStyle(templeteDataRowStyle);
			
			dataCell = dataRow.createCell(0);
			dataCell.setCellType(dateCellType);
			dataCell.setCellStyle(dateCellStyle);
			dataCell.setCellValue(date);
			
			HashMap<String,Double> payDatas = datas.get(date);
			cellNum = 2;
			if(payDatas.size() > 0){
				for(Object str:clientArr){
					String clientid = (String) str;
					
					dataCell = dataRow.createCell(cellNum);
					Double pay = payDatas.get(clientid);
					if(pay != null){
						dataCell.setCellValue(pay);
					}
					dataCell.setCellType(dataLtvCellType);
					dataCell.setCellStyle(dataLtvCellStyle);
					
					cellNum ++;
				}
			}
			
			rownum ++;
		}
	}

	private void createClientDailyReportsSheet(){
		Sheet clientSheet = wb.getSheetAt(1);//"分服"

		if(clientDailyReports.size() < 1){
        	clientSheet.getRow(0).removeCell(clientSheet.getRow(0).getCell(2));
        	clientSheet.getRow(1).removeCell(clientSheet.getRow(1).getCell(2));
        	clientSheet.getRow(2).removeCell(clientSheet.getRow(2).getCell(2));
        	clientSheet.getRow(3).removeCell(clientSheet.getRow(3).getCell(0));
        	clientSheet.getRow(3).removeCell(clientSheet.getRow(3).getCell(1));
        	clientSheet.getRow(3).removeCell(clientSheet.getRow(3).getCell(2));
        	return;
		}
		
		/* 读取模板  */
		Row dataRow;
		Row clientRow = clientSheet.getRow(0);
		Row testRow = clientSheet.getRow(1);
		Row installRow = clientSheet.getRow(2);
		Row templeteDataRow = clientSheet.getRow(3);
		
		Cell tempCell = clientRow.getCell(2);
		int tempCellType = tempCell.getCellType();
		CellStyle tempCellStyle = tempCell.getCellStyle();
		
		Cell installCell = installRow.getCell(2);
		int installCellType = installCell.getCellType();
		CellStyle installCellStyle = installCell.getCellStyle();
		
		Cell testCell = testRow.getCell(2);
		int testCellType = testCell.getCellType();
		CellStyle testCellStyle = testCell.getCellStyle();
		
		Cell dateDateCell = templeteDataRow.getCell(0);
		int dateCellType = dateDateCell.getCellType();
		CellStyle dateCellStyle = dateDateCell.getCellStyle();
		
		Cell dataUnitCell = templeteDataRow.getCell(1);
		int unitCellType = dataUnitCell.getCellType();
		CellStyle unitCellStyle = dataUnitCell.getCellStyle();
		String unitValue = dataUnitCell.getStringCellValue();
		
		Cell dataPayCell = templeteDataRow.getCell(2);
		int dataPayCellType = dataPayCell.getCellType();
		CellStyle dataPayCellStyle = dataPayCell.getCellStyle();
		
		short templeteDataRowHeight = templeteDataRow.getHeight();
		CellStyle templeteDataRowStyle = templeteDataRow.getRowStyle();
		
		Set<String> clientidSet = new HashSet<String>();
		Map<String,HashMap<String,Double>> datas = new HashMap<String, HashMap<String,Double>>();
		HashMap<String,Integer> installs = new HashMap<String,Integer>();
		
		/* 整理数据 */
		for(ClientDailyReport report : clientDailyReports){
			String clientid = getClientid(report);
			
			clientidSet.add(clientid);
			
			HashMap<String,Double> payDatas = datas.get(report.getDay());
			if(payDatas == null){
				payDatas = new HashMap<String, Double>();
			}
			payDatas.put(clientid, Math.floor(report.getPaymentAmount() / games.getRate() * 100) / 100);
			datas.put(report.getDay(), payDatas);
		}
		for(ClientDailyReportVO reportVO:clientAllInstalls){
			String clientid = getClientid(reportVO.getEntity());
			installs.put(clientid, reportVO.getInstallSum());
		}
		
		/* 服务器ID排序 */
		Object[] clientArr = clientidSet.toArray();
		Arrays.sort(clientArr);
		
		Cell dataCell;
		
		/* 灌装服务器ID */
		int cellNum = 2;
		for(Object str:clientArr){
			String clientid = (String) str;
			
			dataCell = clientRow.createCell(cellNum);
			dataCell.setCellType(tempCellType);
			dataCell.setCellStyle(tempCellStyle);
			dataCell.setCellValue(clientid);
			
			dataCell = testRow.createCell(cellNum);
			dataCell.setCellType(testCellType);
			dataCell.setCellStyle(testCellStyle);
			dataCell.setCellValue("-");
			
			dataCell = installRow.createCell(cellNum);
			dataCell.setCellType(installCellType);
			dataCell.setCellStyle(installCellStyle);
			dataCell.setCellValue(installs.get(clientid));
			
			cellNum ++;
		}
		
		Object[] clientDateArr = datas.keySet().toArray();
		Arrays.sort(clientDateArr);
		
		int rownum = 3;
		for(Object obj:clientDateArr){
			String date = (String) obj;
			
			dataRow = clientSheet.createRow(rownum);
			dataRow.setHeight(templeteDataRowHeight);
			dataRow.setRowStyle(templeteDataRowStyle);
			
			dataCell = dataRow.createCell(0);
			dataCell.setCellType(dateCellType);
			dataCell.setCellStyle(dateCellStyle);
			dataCell.setCellValue(date);
			
			if(rownum == 3){
				dataCell = dataRow.createCell(1);
				dataCell.setCellType(unitCellType);
				dataCell.setCellStyle(unitCellStyle);
				dataCell.setCellValue(unitValue+games.getCurrency()+")");
			}
			
			HashMap<String,Double> payDatas = datas.get(date);
			cellNum = 2;
			if(payDatas.size() > 0){
				for(Object str:clientArr){
					String clientid = (String) str;
					
					dataCell = dataRow.createCell(cellNum);
					Double pay = payDatas.get(clientid);
					if(pay != null){
						dataCell.setCellValue(pay);
					}
					dataCell.setCellType(dataPayCellType);
					dataCell.setCellStyle(dataPayCellStyle);
					
					cellNum ++;
				}
			}
			
			rownum ++;
		}
	}

	private String getClientid(ClientDailyReport report) {
		String clientid = "";
		try {
			clientid = report.getClientid();
		} catch (Exception e) {
			System.out.println("snid="+report.getSnid()+",gameid="+report.getGameid()+",clientid="+report.getClientid()
					+"下载时排序出错，归为0服务器："+e.getMessage());
		}
		return clientid;
	}

	
	private void createAnalysisGDTsSheet() {
        Sheet sheet = wb.getSheetAt(8);//广点通分析表
		
		Row dataRow;
		Row templeteRow = sheet.getRow(2);
		int rownum = 3;
		for (AnalysisGDT gdt : analysisGDTs) {
			dataRow = sheet.createRow(rownum);
			dataRow.setHeight(templeteRow.getHeight());
			dataRow.setRowStyle(templeteRow.getRowStyle());
			
			
			Object data[] = {
					gdt.getDate(),
					gdt.getAdvInstall(),
					gdt.getAdvCost(),
					gdt.getD0Payuser(),
					gdt.getPayRate(),
					gdt.getD0Payment() == null ? "-" : gdt.getD0Payment(),
					gdt.getD1Payment() == null ? "-" : gdt.getD1Payment(),
					gdt.getD3Payment() == null ? "-" : gdt.getD3Payment(),
					gdt.getD7Payment() == null ? "-" : gdt.getD7Payment(),
					gdt.getD30Payment() == null ? "-" : gdt.getD30Payment(),
					gdt.getD0Roi() == null ? "-" : gdt.getD0Roi(),
					gdt.getD1Roi() == null ? "-" : gdt.getD1Roi(),
					gdt.getD3Roi() == null ? "-" : gdt.getD3Roi(),
					gdt.getD7Roi() == null ? "-" : gdt.getD7Roi(),
					gdt.getD30Roi() == null ? "-" : gdt.getD30Roi()
			};

			createRow(dataRow, templeteRow, data);
			
			rownum ++;
		}
		
		sheet.shiftRows(3, sheet.getLastRowNum()+1, -1);
		
	}

	private void createSummaryGDTsSheet() {
       Sheet sheet = wb.getSheetAt(7);//广点通汇总表
		
		Row dataRow;
		Row templeteRow = sheet.getRow(1);
		Cell dataCell;
		int rownum = 2;
		for (SummaryGDT gdt : summaryGDTs) {
			dataRow = sheet.createRow(rownum);
			dataRow.setHeight(templeteRow.getHeight());
			dataRow.setRowStyle(templeteRow.getRowStyle());
			
			for(int cellNum=0;cellNum<=9;cellNum++){
				dataCell = dataRow.createCell(cellNum);
				dataCell.setCellType(templeteRow.getCell(cellNum).getCellType());
				dataCell.setCellStyle(templeteRow.getCell(cellNum).getCellStyle());
				switch(cellNum){
				case 0:
					dataCell.setCellValue(gdt.getDate());
					break;
				case 1:
					dataCell.setCellValue(gdt.getExposure());
					break;
				case 2:
					dataCell.setCellValue(gdt.getClickCount());
					break;
				case 3:
					dataCell.setCellValue(gdt.getClickRate());
					break;
				case 4:
					dataCell.setCellValue(gdt.getInstall());
					break;
				case 5:
					dataCell.setCellValue(gdt.getConversionRate());
					break;
				case 6:
					dataCell.setCellValue(gdt.getAllCost());
					break;
				case 7:
					dataCell.setCellValue(gdt.getD0Cost());
					break;
				case 8:
					dataCell.setCellValue(gdt.getCpc());
					break;
				case 9:
					dataCell.setCellValue(gdt.getCpa());
					break;
				}
			}
			
			rownum ++;
		}
		
		sheet.shiftRows(2, sheet.getLastRowNum()+1, -1);
	}
	
	/**
     * create a library of cell styles
     */
    private Map<String, CellStyle> createStyles(){
       
        DataFormat df = wb.createDataFormat();

        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put("header", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("header_date", style);

        Font font1 = wb.createFont();
        font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font1);
        styles.put("cell_b", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font1);
        styles.put("cell_b_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_b_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_g", style);

        Font font2 = wb.createFont();
        font2.setColor(IndexedColors.BLUE.getIndex());
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font2);
        styles.put("cell_bb", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_bg", style);

        Font font3 = wb.createFont();
        font3.setFontHeightInPoints((short)14);
        font3.setColor(IndexedColors.DARK_BLUE.getIndex());
        font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font3);
        style.setWrapText(true);
        styles.put("cell_h", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        styles.put("cell_normal", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        styles.put("cell_normal_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setWrapText(true);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_normal_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setIndention((short)1);
        style.setWrapText(true);
        styles.put("cell_indented", style);

        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("cell_blue", style);

        return styles;
    }

    private static CellStyle createBorderedStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }
   
}
