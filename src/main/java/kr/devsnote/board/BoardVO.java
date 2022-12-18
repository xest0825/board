package kr.devsnote.board;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class BoardVO {

    String seq;
    String title;
    String contents;
    String author;
    String category;
    String reg_dtm;

    boolean paging;
    int total_cnt;
    int row_cnt;
    int current_page;
    int total_page;
    int offset = 0;

}
