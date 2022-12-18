package kr.devsnote.excel;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import kr.devsnote.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import kr.devsnote.util.CommonUtil;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;


@Slf4j
public class ExcelHandler implements ResultHandler {
	
	
	private String EXCEL_FILENAME = "";
	private String EXCEL_PATH = "";;
	private ArrayList<String> TITLE = null;
	private ArrayList<String> FIELDS = null;
	
	private Workbook wb = null;
	private XSSFWorkbook temp_wb = null;
	private Sheet dataSheet = null;
	private int rowindex = 0;
	
	private int hidden = 0;
	private int BLANK_ROW = 0;
	private String TILTE_YN  = "Y";
	
	private DataFormat dataFormat = null;
	private CellStyle numStyle = null;
	private CellStyle rateStyle = null;
	
	private Font monthFont = null;
	private CellStyle  style = null;
	
    public int getRowindex() {
		return rowindex;
	}


	/**
     * 
     * Description  : 엑셀 핸들러 
     * 최초 생성일  : 2014. 5. 20. : 오전 10:16:20
     * file         : FileService.java 
     * 페키지       : genexon.comm.excel;
     * RETURN       : excelHandler 
     * @param excelfilename
     * @throws Exception

     */
	public ExcelHandler(String excelfilename){

		EXCEL_FILENAME = excelfilename;
		EXCEL_PATH = Constants.getPATH(Constants.UPLOADS.TEMPLATE_EXCEL) +  EXCEL_FILENAME;
		
		try 
		{	
			temp_wb = new XSSFWorkbook(new FileInputStream(EXCEL_PATH));
			wb =  new SXSSFWorkbook(temp_wb, 1000); 
			wb.removeSheetAt(wb.getSheetIndex("Data"));
			
			dataSheet = wb.createSheet("Data");
			
			monthFont = wb.createFont();
			monthFont.setFontHeightInPoints((short)12);
		    monthFont.setColor(IndexedColors.WHITE.getIndex());
			
		    style = wb.createCellStyle();
		    style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    style.setFont(monthFont);
		    
		    dataFormat = wb.createDataFormat();
			numStyle = wb.createCellStyle();
			numStyle.setDataFormat(dataFormat.getFormat("#,##0"));
			
			rateStyle = wb.createCellStyle();
			rateStyle.setDataFormat(dataFormat.getFormat("0.00%"));
			
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	
	public ExcelHandler(String excelfilename , int  blankrow , String titleyn){

		EXCEL_FILENAME = excelfilename;
		EXCEL_PATH = Constants.getPATH(Constants.UPLOADS.TEMPLATE_EXCEL) +  EXCEL_FILENAME;
		BLANK_ROW = blankrow;	
		TILTE_YN = titleyn;
		
		try 
		{	
			temp_wb = new XSSFWorkbook(new FileInputStream(EXCEL_PATH));
			wb =  temp_wb; 
			wb.removeSheetAt(wb.getSheetIndex("Data"));
			dataSheet = wb.createSheet("Data");
			
			monthFont = wb.createFont();
			monthFont.setFontHeightInPoints((short)12);
		    monthFont.setColor(IndexedColors.WHITE.getIndex());
			
		    style = wb.createCellStyle();
		    style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    style.setFont(monthFont);
		    
		    dataFormat = wb.createDataFormat();
			numStyle = wb.createCellStyle();
			numStyle.setDataFormat(dataFormat.getFormat("#,##0"));
			
			rateStyle = wb.createCellStyle();
			rateStyle.setDataFormat(dataFormat.getFormat("0.00%"));
			
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	

	/**
     * @return the dataSheet
     */
    public Sheet getDataSheet() {
        return dataSheet;
    }


    /**
     * 
     * Description  : 엑셀 핸들러 맵형태
     * 최초 생성일  : 2014. 5. 20. : 오전 10:16:20
     * file         : FileService.java 
     * 페키지       : genexon.comm.excel;
     * RETURN       : excelHandler 
     * @param excelfilename , titles

     */
	
	public ExcelHandler(String excelfilename, ArrayList<String> titles){

		EXCEL_FILENAME = excelfilename;
		EXCEL_PATH = Constants.getPATH(Constants.UPLOADS.TEMPLATE_EXCEL) +  EXCEL_FILENAME;
		TITLE =titles;
		TILTE_YN = "Y";
		
		try 
		{	
			temp_wb = new XSSFWorkbook(new FileInputStream(EXCEL_PATH));
			
			wb =  temp_wb; 
			wb.removeSheetAt(wb.getSheetIndex("Data"));
			
			dataSheet = wb.createSheet("Data");
			
			monthFont = wb.createFont();
			monthFont.setFontHeightInPoints((short)12);
		    monthFont.setColor(IndexedColors.WHITE.getIndex());
			
		    style = wb.createCellStyle();
		    style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    style.setFont(monthFont);
		    
		    dataFormat = wb.createDataFormat();
			numStyle = wb.createCellStyle();
			numStyle.setDataFormat(dataFormat.getFormat("#,##0"));
			
			rateStyle = wb.createCellStyle();
			rateStyle.setDataFormat(dataFormat.getFormat("0.00%"));
			
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	
	
	public ExcelHandler(String excelfilename, ArrayList<String> titles, ArrayList<String> fields){

		EXCEL_FILENAME = CommonUtil.isEmpty(excelfilename) ? "DEFAULT.xlsx" : excelfilename;
		//EXCEL_PATH = Constants.getPATH(Constants.UPLOADS.TEMPLATE_EXCEL) +  EXCEL_FILENAME;
		//EXCEL_PATH = "";
		log.info("EXCEL_PATH : " + EXCEL_PATH);
		TITLE =titles;
		FIELDS = fields;
		TILTE_YN = "Y";
		
		try 
		{	
			final DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
			Resource resource = defaultResourceLoader.getResource("classpath:static/excel/" + EXCEL_FILENAME);
			log.debug("resource_name : " + resource.getFile().getAbsolutePath());
			temp_wb = new XSSFWorkbook(resource.getInputStream()); //XSSFWorkbook 파일 읽어 들이기 FileInputStream 해당 경로의 파일을 읽어 들인다.
			
			wb =  new SXSSFWorkbook(temp_wb, 1000); // SXSSFWorkbook 파일과 엑셀 생성해 다운로드 1000은 행의 개수
			wb.removeSheetAt(wb.getSheetIndex("Data")); // 해당 인덱스의 sheet를 삭제한다. getSheetIndex 해당 sheet 이름의 (data) index값을 return
			
			dataSheet = wb.createSheet("Data"); // 생성
			
			monthFont = wb.createFont(); // font 생성
			monthFont.setFontHeightInPoints((short)12);
		    monthFont.setColor(IndexedColors.WHITE.getIndex());
			
		    style = wb.createCellStyle();
		    style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    style.setFont(monthFont);
		    
		    dataFormat = wb.createDataFormat();

			numStyle = wb.createCellStyle();
			numStyle.setDataFormat(dataFormat.getFormat("#,##0")); // 1000단위로 콤마
			
			rateStyle = wb.createCellStyle();
			rateStyle.setDataFormat(dataFormat.getFormat("0.00%")); // %
			
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	@Override
	public void handleResult(ResultContext context) {
		Object obj = context.getResultObject();
		this.handleResultObj(obj);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.ResultHandler#handleResult(org.apache.ibatis.session.ResultContext)
	 */
	public void handleResultObj(Object obj) {
       Sheet sh = dataSheet;
       
       try {
			
			//맵형태(mybatis alias > lhmap, hmap)
			if(obj instanceof LinkedHashMap || obj instanceof java.util.HashMap) {
				
				hidden = 1;
				  
				@SuppressWarnings("unchecked")
				Map<String, Object> resultMap = (Map<String, Object>) obj;
				
				Iterator<String> itvalue = resultMap.keySet().iterator();
				Iterator<String> itvaluevalue = resultMap.keySet().iterator();
				
				Object keyvalue = null;
				Row headerRow = sh.createRow(rowindex);
				
				if (rowindex == 0) {
					//빈로우 생성
				    for(int j =0  ; j < BLANK_ROW; j++){
				        rowindex++;
						headerRow = sh.createRow(rowindex);
				    }
				    
				    //타이틀 작성여부
				    if(TILTE_YN.equals("Y")){
				        //타이틀 파라메터가 있는경우
				        if(TITLE != null){
				        
				        	for(int  i= 0 ; i < this.TITLE.size(); i++ ){
		
								  Cell cell = headerRow.createCell(i);
								  cell.setCellValue(TITLE.get(i));
								  cell.setCellStyle(style);
								  
							}
				        //타이틀 파라메터가 없는경우map keyvlaue 를 타이틀로 	
				        }else{
				        	int i = 0;
							while(itvalue.hasNext())
							{	
							  keyvalue = itvalue.next();
							  String keystr = keyvalue.toString();
							  
							  Cell cell = headerRow.createCell(i);
							  cell.setCellValue(keystr);
							  cell.setCellStyle(style);
							  
							  i++;
							}
				        }
				        rowindex++;
						headerRow = sh.createRow(rowindex);
				    }
				}
				
				//FIELDS 값 정의 했을 때 > FIELDS에 정의해 놓은 결과값 필드명으로 맵에있는 데이터 가져와서 셀 데이터 생성
				if(FIELDS != null) {
					String cellValue = "";
					
					for(int i=0; i < FIELDS.size(); i++) {
						Cell cell = headerRow.createCell(i);
						
						cellValue = String.valueOf(resultMap.get(FIELDS.get(i)));

						if(cellTypeStringField(FIELDS.get(i))) {
								cell.setCellType(CellType.STRING);
								cell.setCellValue(cellValue.replace("null", ""));
						}else{
							if(CommonUtil.isStringDouble(cellValue)) {
								//금액, 성적 컬럼 셀은 #,### 형식으로 나오도록
								if(cellTypeNumberField(FIELDS.get(i)) && !cellTypeRateField(FIELDS.get(i))) {
									cell.setCellType(CellType.NUMERIC);
									cell.setCellStyle(numStyle);
									cell.setCellValue(Double.parseDouble(cellValue.replace("null", "")));
								}else if(cellTypeRateField(FIELDS.get(i)) && !cellTypeNumberField(FIELDS.get(i))) {
									cell.setCellType(CellType.NUMERIC);
									cell.setCellStyle(rateStyle);
									cell.setCellValue(Double.parseDouble(cellValue.replace("null", "")) / 100 );
								}else {
									cell.setCellValue(Double.parseDouble(cellValue.replace("null", "")));
								}
			                }else {
			                    cell.setCellValue(cellValue.replace("null", ""));
			                }
						}
					}
				}else {	//FIELDS 값 정의 안했을 때 > 맵의 데이터 순차적으로 insert
					int i = 0;
					
					while(itvaluevalue.hasNext()) {
						keyvalue = itvaluevalue.next();
						String keystr = keyvalue.toString();
						String value = String.valueOf(resultMap.get(keystr));
						
						Cell cell = headerRow.createCell(i);
						  
						  if (resultMap.get(keystr) instanceof String) {
							  cell.setCellValue( value.replace("null", ""));
						  } else {
							  if(CommonUtil.isStringDouble(value)){
								  cell.setCellValue(Double.parseDouble(value.replace("null", "")));
							  }else{
								  cell.setCellValue( value.replace("null", ""));
							  }
						  }
					 
					  
					  i++;
					}
				}

				rowindex++;
			
			//VO 형태	
			}
			else
			{
				Field[] fields = obj.getClass().getDeclaredFields();
				
				if (TITLE != null && FIELDS !=null ) {

					hidden = 1;
					Row headerRow = sh.createRow(rowindex);
					
					if (rowindex == 0) {
				        //타이틀 파라메터가 있는경우
			        	for(int  i= 0 ; i < TITLE.size(); i++ ){
			        		  Cell cell = headerRow.createCell(i);
			        		  
			        		  if(i==0) {
			        			  cell.setCellValue("serialVersionUID");
			        		  }
							  cell.setCellValue(TITLE.get(i));				        		  					 
							  cell.setCellStyle(style);
							  
						}
				        
				        rowindex++;
						headerRow = sh.createRow(rowindex);
					}
					
					Map<String, Object> map = new LinkedHashMap<String, Object>();
						
					for (int i = 0; i <= fields.length - 1; i++) {
						if (fields[i] != null) {
							fields[i].setAccessible(true);
							map.put(fields[i].getName().toString(), String.valueOf(fields[i].get(obj)));
						}
					}
					
					if(map != null) {
						for (int i = 0; i < FIELDS.size(); i++) {                                                                            
							
							Cell cell = headerRow.createCell(i);
							String cellValue = "";
							
							if(FIELDS.get(i) != null ) {
								cellValue = String.valueOf(map.get(FIELDS.get(i)));
								
								if(cellTypeStringField(FIELDS.get(i))) {
									cell.setCellType(CellType.STRING);
									cell.setCellValue(cellValue.replace("null", ""));
								}else{
									if(CommonUtil.isStringDouble(cellValue)) {
										//금액, 성적 컬럼 셀은 #,### 형식으로 나오도록
										if(cellTypeNumberField(FIELDS.get(i)) && !cellTypeRateField(FIELDS.get(i))) {
											cell.setCellType(CellType.NUMERIC);
											cell.setCellStyle(numStyle);
											
											cell.setCellValue(Double.parseDouble(cellValue.replace("null", "")));
										}else if(cellTypeRateField(FIELDS.get(i)) && !cellTypeNumberField(FIELDS.get(i))) {
											cell.setCellType(CellType.NUMERIC);
											cell.setCellStyle(rateStyle);
											
											cell.setCellValue(Double.parseDouble(cellValue.replace("null", "")) / 100 );
										}else {
											cell.setCellValue(Double.parseDouble(cellValue.replace("null", "")));
										}
					                }else {
					                    cell.setCellValue(cellValue.replace("null", ""));
					                }
								} 
							}			                
						}
					}
					
					
				} else {
//					Field[] fields = obj.getClass().getDeclaredFields();
					hidden = 0;
					Row headerRow = sh.createRow(rowindex);
					
					if (rowindex == 0) 
					{
						
						for (int i = 0; i <= fields.length - 1; i++)
						{
							fields[i].setAccessible(true);
							Cell cell = headerRow.createCell(i);
							cell.setCellValue(fields[i].getName().toString());
						}
						rowindex++;
						headerRow = sh.createRow(rowindex);
						
					}
	
					for (int i = 0; i <= fields.length - 1; i++)
					{
						
						Cell cell = headerRow.createCell(i);
						String cellvalue = "";
						
						if(fields[i] != null )
						{
							fields[i].setAccessible(true);
							cellvalue = String.valueOf(fields[i].get(obj));
						}
						
		                if(CommonUtil.isStringDouble(cellvalue))
		                {
		                    cell.setCellValue(Double.parseDouble(cellvalue.replace("null", "")));
		                }
		                else
		                {
		                    cell.setCellValue(cellvalue.replace("null", ""));
		                }
		                
					}
				}
				rowindex++;
			}
			
		}
		catch (Exception e)
        {
			log.error(e.getMessage());
        }
	}

	/**
	 * 
	 * Description  : 생성된 엑셀 파일 전송 
	 * 최초 생성일  : 2014. 5. 20. : 오전 10:18:54
	 * file         : excelHandler.java 
	 * 페키지       : genexon.comm.excel
	 * RETURN       : void 
	 * @param response
	 * @param filename
	 * @throws Exception

	 */
	public void sendResponse(HttpServletResponse response, String filename){

		OutputStream out = null;
		
		try {
			out = response.getOutputStream();
			
			filename = URLEncoder.encode(filename, "UTF-8");
			filename = filename.replace("+", "%20");	//파일명에 공백이 있으면 +가 되고 그 +를 공백으로 다시 변환
			
		    response.setHeader("Content-Disposition", "attachment;filename=" + filename + ";");
		    response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setDateHeader("Last-Modified", new Date().getTime());
			
			if(hidden == 0) {
				wb.setSheetOrder("Data", 1);
				wb.setSheetHidden(1, true);
	
			}else{
				wb.setSheetOrder("Data", 0);
				wb.setSheetHidden(0, false);
			}
			
		    
			wb.write(out);
			wb.close();

		} catch (Exception e) {
			log.error(e.getMessage());
			//throw new ServletException(e);
		} finally {
			try {
				if(null != out && !(wb instanceof SXSSFWorkbook)){
					out.close();
				}
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * Description  : 엑셀파일 암호 적용하여 생성된 파일 전송
	 * 최초 생성일  : 2020. 02. 07.
	 * file         : excelHandler2.java 
	 * RETURN       : void
	 * @param HttpServletResponse response, String filename, String excelPwd
	 */
	public void sendEncryptExcelResponse(HttpServletResponse response, String filename, String excelPwd) {

		OutputStream out = null;
		OutputStream encDataStream = null;
		POIFSFileSystem fs = null;
		
		try {
			fs = new POIFSFileSystem();
			EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);

			Encryptor enc = info.getEncryptor();
			enc.confirmPassword(excelPwd);
			encDataStream = enc.getDataStream(fs);
			
			if(hidden == 0) {
				wb.setSheetOrder("Data", 1);
				wb.setSheetHidden(1, true);
	
			}else{
				wb.setSheetOrder("Data", 0);
				wb.setSheetHidden(0, false);
			}
			
			wb.write(encDataStream);
			wb.close();
			encDataStream.close();
			
			filename = URLEncoder.encode(filename, "UTF-8");
			filename = filename.replace("+", "%20");	//파일명에 공백이 있으면 +가 되고 그 +를 공백으로 다시 변환
			
		    response.setHeader("Content-Disposition", "attachment;filename=" + filename + ";");
		    response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setDateHeader("Last-Modified", new Date().getTime());
						
			out = response.getOutputStream();
			fs.writeFilesystem(out);

		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if(null != out && !(wb instanceof SXSSFWorkbook)){
					out.close();
				}
				
				if(fs != null) {
					fs.close();
				}
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}
	}

	
	/**
	 * 
	 * Description  : 파일삭제 
	 * 최초 생성일  : 2014. 5. 20. : 오전 10:30:55
	 * file         : excelHandler.java 
	 * 페키지       : genexon.comm.excel
	 * RETURN       : void 
	 * @throws Exception

	 */
	public void closeExcel(){
		try {
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 
	 * Description  : 워크북 리턴  
	 *
	 * 최초 생성일  : 2014. 5. 20. : 오전 10:30:58
	 * file         : excelHandler.java 
	 * 페키지       : genexon.comm.excel
	 * RETURN       : XSSFWorkbook 
	 * @return

	 */
	public Workbook getWb() {
		
		
	    if (wb instanceof SXSSFWorkbook)
	    {
	       	return wb;
	    }
	    else if (wb instanceof XSSFWorkbook ){
	    	return wb;
	    }
	    else
	    {
	    	return wb;
	    }
		
	}

	
	public  boolean isNumeric(String str)  
	 {  
	    try  
	    {  
	    @SuppressWarnings("unused")
	    double d = Double.parseDouble(str);  
	    
	    }  
	    catch(NumberFormatException nfe)  
	    {  
	      return false;  
	    }  
	    return true;  
	  }
	
	/**
	 * @desc 데이터가 숫자일 수도 있으나 셀 타입이 String 타입이어야 하는 필드
	 * @param field
	 * @return
	 */
	public boolean cellTypeStringField(String field) {
		boolean yn = "pscd".equals(field.toLowerCase()) || "scd".equals(field.toLowerCase()) || field.toLowerCase().indexOf("inspol_no") != -1 || field.toLowerCase().indexOf("emp_cd") != -1 
					|| field.indexOf("hpno") != -1 || field.indexOf("telno") != -1 || field.indexOf("zipcd") != -1 || "insco_emp_cd".equals(field)
					|| field.indexOf("bk_id") != -1 || field.indexOf("maid") != -1 || field.indexOf("carcode") != -1
					|| field.indexOf("mo_cd") != -1 || "bank".equals(field) || "scdpath3".equals(field) || "org_channel_gbn".equals(field)
					|| field.indexOf("jijum_code") != -1 || field.indexOf("vreg_no") != -1 || field.indexOf("areg_no") != -1
					|| field.indexOf("mobPhn") != -1
		;
		
		return yn;
	}
	
	/**
	 * @desc 숫자형 데이터 중 #,###으로 나와야 하는 컬럼
	 * @param field
	 * @return
	 */
	public boolean cellTypeNumberField(String field) {
		boolean yn = (field.toLowerCase().indexOf("amt") != -1 || field.toLowerCase().indexOf("money") != -1 || field.toLowerCase().indexOf("hwan") != -1 ||
					  field.indexOf("life_prod_kind2") != -1 || field.indexOf("mojibgo") != -1 || field.indexOf("usigo") != -1 ||
					  field.indexOf("sugum_resource") != -1 || field.indexOf("sugum_result") != -1 || field.equals("payment") ||
					  field.indexOf("income_tax") != -1 || field.equals("total_tax") || field.equals("dept_payment")
					  
					 );
		
		return yn;
	}
	
	/**
	 * @desc 백분율(#.##%)로 나와야 하는 컬럼
	 * @param field
	 * @return
	 */
	public boolean cellTypeRateField(String field) {
		boolean yn = (field.toLowerCase().indexOf("usi_rate") != -1 || field.toLowerCase().indexOf("sugum_rate") != -1 || field.toLowerCase().endsWith("rate") ||
					  field.toLowerCase().equals("tariff") || field.toLowerCase().indexOf("corporation_rate") != -1 || field.toLowerCase().indexOf("planner_rate") != -1 ||
					  field.toLowerCase().indexOf("rate_l") != -1 || field.toLowerCase().indexOf("rate_n") != -1 ||
					  field.toLowerCase().indexOf("suip_rate") != -1 || field.toLowerCase().indexOf("jigub_rate") != -1
					 );
		
		return yn;
	}
	
}
