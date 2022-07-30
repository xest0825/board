package com.example.board.dao;

import com.example.board.vo.Board;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class BoardDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String SQL_PREFIX = "Board.";

    public List<HashMap<String, Object>> getItemList(Board vo) {
        return sqlSession.selectList(SQL_PREFIX + "getItemList", vo);
    }

    public int insertItem(Board vo) {
        return sqlSession.insert(SQL_PREFIX + "insertItem", vo);
    }

    public int updateItem(Board vo) {
        return sqlSession.update(SQL_PREFIX + "updateItem", vo);
    }

    public int deleteItem(Board vo) {
        return sqlSession.update(SQL_PREFIX + "deleteItem", vo);
    }


}
