package kr.devsnote.insco;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class InscoVO {

    String insco_cd;
    String insco_nm;
    String sort_no;
    String in_emp_cd;
    String up_emp_cd;
    String search_word;
}
