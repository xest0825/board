package kr.devsnote.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ActionLogVO {

	private String action_seq;
	private String action_dtm; 	/* DATETIME 	'액션 일시' */
	private String action_url; 	/* VARCHAR(200) '액션 URL'  */
	private String action_ctg;	/* VARCHAR(10)  액션 분류   */
	private String user_id;		
	private String user_ip; 	/* VARCHAR(30) '사용자 IP'  */
	private String action_env; 	/* TEXT 		로그인환경  */
	private String params; 		/* TEXT 		'파라미터'  */

}
