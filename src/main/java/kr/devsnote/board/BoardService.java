package kr.devsnote.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardDAO dao;

    public List<HashMap<String, Object>> getItemList(BoardVO vo){
        return dao.getItemList(vo);
    }

    public HashMap<String, Object> getItemCount(BoardVO vo){
        return dao.getItemCount(vo);
    }

    public int insertItem(BoardVO vo) {
        return dao.insertItem(vo);
    }

    public int updateItem(BoardVO vo) {
        return dao.updateItem(vo);
    }

    public int deleteItem(BoardVO vo) {
        return dao.deleteItem(vo);
    }
}
