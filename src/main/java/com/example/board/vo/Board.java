package com.example.board.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class Board {

    String seq;
    String title;
    String contents;
    String author;
    String reg_dtm;
}
