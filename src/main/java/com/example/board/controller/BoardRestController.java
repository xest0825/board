package com.example.board.controller;

import com.example.board.vo.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class BoardRestController {

    @GetMapping("/boards")
    public ResponseEntity<List<HashMap<String, Object>>> getBoardItems() {
        List list = new ArrayList<HashMap<String, Object>>();
        HashMap map = new HashMap<String, Object>();

        map.put("title", "향수");
        map.put("contents", "넒은동쪽 끝으로");
        map.put("author", "choiys");

        list.add(map);

        HashMap map2 = new HashMap<String, Object>();

        map2.put("title", "자바공부");
        map2.put("contents", "열심히 해봅시다");
        map2.put("author", "shinwj");

        list.add(map2);

        ResponseEntity<List<HashMap<String, Object>>> ret = new ResponseEntity<>(list, HttpStatus.OK);
        return ret;
    };


    @PostMapping("/boards")
    public ResponseEntity<HashMap<String, Object>> insertItems(@RequestBody Board model) {
        String title = model.getTitle();
        String contents = model.getContents();
        String author = model.getAuthor();
        log.info(title + ", " + contents + ", " + author);

        HashMap map = new HashMap<String, Object>();
        map.put("result", "OK");
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

}
