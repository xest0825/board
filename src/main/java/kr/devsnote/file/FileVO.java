package kr.devsnote.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class FileVO {

    private String file_id;
    private String file_name;
    private String org_file_name;
    private String file_path;
    private String file_size;
    private String file_ext;
    private String in_id;
    private String up_id;
    private String search_word;
    private String search_file_id;
    private String search_org_file_name;
    private String search_file_path;
    private String search_file_size;
    private String search_file_ext;
    private String excel_path;
    private String system_type;

    private String use_yn;
    private String display_yn;
    private String seq;
    private String start_dtm;
    private String end_dtm;
    private String status;
    private String srch_start_dtm;
    private String srch_end_dtm;
    private int diff_min;
    private String in_ip;
    private String msg;
    private String category;

}
