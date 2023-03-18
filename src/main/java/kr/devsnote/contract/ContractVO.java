package kr.devsnote.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContractVO {

    String emp_cd      ; /* varchar(20) NN  '사번' */
    String insco_cd    ; /* varchar(3)  NN  '보험사코드' */
    String inspol_no   ; /* varchar(40) NN  '증권번호' */
    String cont_date   ; /* varchar(10) NN  '계약일자' */
    String first_prem  ; /* int(11) '최초보험료' */
    String usi_prem    ; /* int(11) '유지보험료' */
    String first_hwan  ; /* int(11) '최초보험사성적' */
    String usi_hwan    ; /* int(11) '유지보험사성적' */
    String rate        ; /* float '조정계수' */
    String status      ; /* varchar(10) NN  '상태 : 정상(정상, 연체), 미유지(실효, 해약, 취소)' */
    String cnt         ; /* int(11) NN  '회차' */
    String in_emp_cd   ; /* varchar(20) '입력자 사번' */
    String in_dtm      ; /* datetime '입력일시' */
    String up_emp_cd   ; /* varchar(20) '수정자 사번' */
    String up_dtm      ; /* datetime '수정일시' */

    int row_cnt;
    int offset;
}
