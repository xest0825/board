package kr.devsnote.config;

import java.io.File;

import lombok.extern.slf4j.Slf4j;

/**
 * 시스템 전역 상수
 * @author genexon
 *
 */
@Slf4j
public class Constants {

	public enum UPLOADS {
		ROOT, UPLOAD_ROOT, DOWNLOAD_ROOT, TEMPLATE_EXCEL, TEMPLATE_PDF
	};
	
	public enum LOGGING{LOGGING, NOLOGGING};
	
	public static LOGGING QueryLogging = LOGGING.LOGGING;
	
	private static  String WEB_ROOT_PATH = "";

	private static  String UPLOAD_ROOT_PATH = "";

	private static  String DOWNLOAD_ROOT_PATH = "";

	private static  String TEMPLATE_EXCEL_FILE_PATH = "";

	private static  String TEMPLATE_PDF_FILE_PATH = "";

    
	private static final String UPLOAD_ROOT_URL = "/resources/";
	private static final String DOWNLOAD_ROOT_URL = "/resources/";
	private static final String TEMPLATE_EXCEL_FILE_URL =  "excel/";
	private static final String TEMPLATE_PDF_FILE_URL =  "pdf/";

    public static void setConstants(String WebRootPath)
    {

    	log.debug("-----Set Constants...-----");
    	
    	Constants.WEB_ROOT_PATH = WebRootPath;
    	
    	Constants.UPLOAD_ROOT_PATH = WebRootPath + Constants.UPLOAD_ROOT_URL;

		Constants.DOWNLOAD_ROOT_PATH = WebRootPath + Constants.DOWNLOAD_ROOT_URL;
    	
    	/** TEMPLATE_EXCEL_FILE_PATH */
    	Constants.TEMPLATE_EXCEL_FILE_PATH = "classpath:static/" + Constants.TEMPLATE_EXCEL_FILE_URL;
    	mkdir(Constants.TEMPLATE_EXCEL_FILE_PATH);
    	log.debug("-Constants.TEMPLATE_EXCEL_FILE_PATH={}-",Constants.TEMPLATE_EXCEL_FILE_PATH);

		Constants.TEMPLATE_PDF_FILE_PATH = "classpath:static/" + Constants.TEMPLATE_PDF_FILE_URL;
    	mkdir(Constants.TEMPLATE_PDF_FILE_PATH);
    	log.debug("-Constants.TEMPLATE_PDF_FILE_PATH={}-",Constants.TEMPLATE_PDF_FILE_PATH);

    }
    
    // getURL 은 프로젝트내 상대경로 반환
    public static String getURL(UPLOADS u)
    {
    	String rtnURL = "";
    	switch(u)
    	{
    		case ROOT:
    			rtnURL = "/";
    			break;
    			
    		case UPLOAD_ROOT:
    			rtnURL = Constants.UPLOAD_ROOT_URL;
    			break;

			case DOWNLOAD_ROOT:
				rtnURL = Constants.DOWNLOAD_ROOT_URL;
				break;
    			
    		case TEMPLATE_EXCEL:
    			rtnURL = Constants.TEMPLATE_EXCEL_FILE_URL;
    			break;

			case TEMPLATE_PDF:
				rtnURL = Constants.TEMPLATE_PDF_FILE_URL;
				break;
    			
    		default:
    			rtnURL = "/";
    			break;
    	}
    	return rtnURL;
    }
    
    // getPATH은 절대경로 반환
    public static String getPATH(UPLOADS u)
    {
    	String rtnPATH = "";
    	switch(u)
    	{
    		case ROOT:
    			rtnPATH = Constants.WEB_ROOT_PATH;
    			break;
    			
    		case UPLOAD_ROOT:
    			rtnPATH = Constants.UPLOAD_ROOT_PATH;
    			break;

			case DOWNLOAD_ROOT:
				rtnPATH = Constants.DOWNLOAD_ROOT_PATH;
				break;
    			
    		case TEMPLATE_EXCEL:
    			rtnPATH = Constants.TEMPLATE_EXCEL_FILE_PATH;	
    			break;

			case TEMPLATE_PDF:
    			rtnPATH = Constants.TEMPLATE_PDF_FILE_PATH;
    			break;

    		default:
    			rtnPATH = Constants.WEB_ROOT_PATH;
    			break;
    	}
    	
    	return rtnPATH;
    }
    
    private static void mkdir(String path)
    {
    	File upDir = new File(path);
    	if(!upDir.exists())//해당 디렉토리의 존재여부를 확인
    	{
    		upDir.mkdirs();//없다면 생성 
    	}
    }

}///~
