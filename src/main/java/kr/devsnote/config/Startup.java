package kr.devsnote.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import kr.devsnote.config.Constants.LOGGING;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템 초기화 클래스, 서버 실행시 로깅/업로드경로 설정 등의 초기화 작업을 처리
 */
@Slf4j
public class Startup {

	@Value("${Globals.LoggingYN}")
	private String loggingYN;

	@Value("${Globals.Stage}")
	private String stage;

	private static String OS = System.getProperty("os.name").toLowerCase();

	@PostConstruct
	public void init() {
		log.info("######################################");
		log.info("-kr.devsnote Board App -");
		log.info("######################################");
		log.info("-LOGGING = {}-", loggingYN); // globals.properties에서 읽어옴.
		log.info("-Stage = {}-", stage);
		LoadInit();
		log.info("######################################");
		log.info("-kr.devsnote Board App Started -");
		log.info("######################################");
	}

	private void LoadInit() {
		try {
			log.info("-----LoadInitData...-----");

			String[] wrPathArr = this.getClass().getResource("/").getPath().split("/");
			String wrPath = org.apache.commons.lang3.StringUtils.join(wrPathArr, "/", 0, wrPathArr.length - 2);
			// log.info("local path : " + wrPath);
			// String homePath = "";
			// localhost 는 현재 경로를 webroot로 사용하고 
			// 스테이징, 운영서버는 하드코딩으로 webroot를 설정
			log.info("os : " + OS);

			if ("LOCAL".equals(stage)) {
				log.info("stage : " + stage);
				wrPath = "/Users/yoonsik/IdeaProjects/board/";
			} else {
				wrPath = "/home/devsnote/board";
			}

			log.info("Web Root Path = {}", wrPath);
			Constants.setConstants(wrPath);
			if (loggingYN.equals("N")) {
				Constants.QueryLogging = LOGGING.NOLOGGING;
			} else {
				Constants.QueryLogging = LOGGING.LOGGING;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("######################################");
			log.warn("-kr.devsnote Board App Start Fail -");
			log.warn("######################################");
		}
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

}
