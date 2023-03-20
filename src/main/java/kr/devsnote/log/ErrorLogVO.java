package kr.devsnote.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorLogVO {
	
	private String err_seq;
	private String err_dtm; /* DATETIME '오류 일시' */
	private String err_sts; /* VARCHAR(10) '오류 상태 (코드)' */
	private String err_cls; /* VARCHAR(200) '오류 발생 클래스' */
	private String err_msg; /* VARCHAR(4000) '오류 메시지' */
	private String err_trc; /* VARCHAR(4000) '오류 트레이스' */
	private String err_env;
	private String params; /* TEXT */
	private String conn_id; /* VARCHAR(256) '접속 ID' */
	private String user_id;
	private String srch_dt_from;
	private String srch_dt_to;
	
}
