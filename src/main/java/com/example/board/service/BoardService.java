package com.example.board.service;

import com.example.board.dao.BoardDAO;
import com.example.board.vo.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardDAO dao;

    public List<HashMap<String, Object>> getItemList(Board vo){
        return dao.getItemList(vo);
    }

    public HashMap<String, Object> getItemCount(Board vo){
        return dao.getItemCount(vo);
    }

    public int insertItem(Board vo) {
        return dao.insertItem(vo);
    }

    public int updateItem(Board vo) {
        return dao.updateItem(vo);
    }

    public int deleteItem(Board vo) {
        return dao.deleteItem(vo);
    }
}
