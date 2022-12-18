package kr.devsnote.customer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class CustomerVO {

    String cust_id;
    String cust_name;
    String gender;
    String hobby;
    String region;
    String introduction;
    String file_id;
    String in_id;
    String up_id;
}
