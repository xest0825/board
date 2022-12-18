package kr.devsnote.board;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class BoardRestController {

    @Autowired
    private BoardService service;

    @GetMapping("/items")
    public ResponseEntity<List<HashMap<String, Object>>> getBoardItems(BoardVO vo) {
        log.info("/items");
        List list = new ArrayList<HashMap<String, Object>>();
        list = service.getItemList(vo);

        ResponseEntity<List<HashMap<String, Object>>> ret = new ResponseEntity<>(list, HttpStatus.OK);
        return ret;
    };

    @GetMapping("/items-count")
    public ResponseEntity<HashMap<String, Object>> getBoardItemsCount(BoardVO vo) {
        log.info("/items-count");
        HashMap<String, Object> retmap = service.getItemCount(vo);

        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(retmap, HttpStatus.OK);
        return ret;
    };


    @PostMapping("/items")
    public ResponseEntity<HashMap<String, Object>> insertItems(@RequestBody BoardVO model) {
        String title = model.getTitle();
        String contents = model.getContents();
        String author = model.getAuthor();
        log.info(title + ", " + contents + ", " + author);

        HashMap map = new HashMap<String, Object>();
        if (service.insertItem(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }

        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @PutMapping("/items")
    public ResponseEntity<HashMap<String, Object>> updateItems(@RequestBody BoardVO model) {
        String seq = model.getSeq();
        String title = model.getTitle();
        String contents = model.getContents();
        String author = model.getAuthor();
        log.info(seq + "," + title + ", " + contents + ", " + author);

        HashMap map = new HashMap<String, Object>();
        if (service.updateItem(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @DeleteMapping("/items")
    public ResponseEntity<HashMap<String, Object>> deleteItems(@RequestBody BoardVO model) {
        String seq = model.getSeq();
        log.info(seq);

        HashMap map = new HashMap<String, Object>();
        if (service.deleteItem(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

}
