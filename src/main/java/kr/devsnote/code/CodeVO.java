package kr.devsnote.code;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class CodeVO {

    String cd_nm; // 코드명
    String cd; // 코드
    String grp_cd; // 그룹코드
    String grp_cd_nm; // 그룹코드명
    String sort_no; // 정렬순서
    String search_word;;// 검색어
}
