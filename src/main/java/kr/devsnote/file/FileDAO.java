package kr.devsnote.file;

import kr.devsnote.excel.ExcelHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class FileDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String SQL_PREFIX = "File.";

    /**
     * 파일정보 목록 조회
     * @param vo
     * @return List<HashMap<String, Object>
     */
    public List<HashMap<String, Object>> getFileInfoList(FileVO vo) {
        return sqlSession.selectList(SQL_PREFIX + "getFileInfoList", vo);
    }

    /**
     * 파일정보 조회
     * @param vo
     * @return HashMap<String, Object>
     */
    public HashMap<String, Object> getFileInfo(FileVO vo) {
        return sqlSession.selectOne(SQL_PREFIX + "getFileInfo", vo);
    }

    /**
     * 파일정보 조회(엑셀다운로드)
     * @param vo, eh
     * @return void
     */
    public void getFileInfoListExcel(FileVO vo, ExcelHandler eh){
        sqlSession.select(SQL_PREFIX + "getFileInfoList", vo, eh);
    }

    /**
     * 파일정보 입력
     * @param vo
     * @return int
     */
    public int insertFileInfo(FileVO vo) {
        return sqlSession.insert(SQL_PREFIX + "insertFileInfo", vo);
    }

    /**
     * 파일정보 수정
     * @param vo
     * @return int
     */
    public int updateFileInfo(FileVO vo) {
        return sqlSession.update(SQL_PREFIX + "updateFileInfo", vo);
    }

    /**
     * 파일정보 삭제
     * @param vo
     * @return int
     */
    public int deleteFileInfo(FileVO vo) {
        return sqlSession.delete(SQL_PREFIX + "deleteFileInfo", vo);
    }

    public List<HashMap<String, Object>> getFileDownloadLogList(FileVO vo) {
        return sqlSession.selectList(SQL_PREFIX + "getFileDownloadLogList", vo);
    }

    public HashMap<String, Object> getMinDownloadingFileInfo(FileVO vo) {
        return sqlSession.selectOne(SQL_PREFIX + "getMinDownloadingFileInfo", vo);
    }

    public int insertFileDownloadLog(FileVO vo) {
        return sqlSession.insert(SQL_PREFIX + "insertFileDownloadLog", vo);
    }
    public int updateFileDownloadLog(FileVO vo) {
        return sqlSession.update(SQL_PREFIX + "updateFileDownloadLog", vo);
    }
    public int deleteFileDownloadLog(FileVO vo) {
        return sqlSession.delete(SQL_PREFIX + "deleteFileDownloadLog", vo);
    }

    public int updateExpirationFileDownloadLog(FileVO vo) {
        return sqlSession.update(SQL_PREFIX + "updateExpirationFileDownloadLog", vo);
    }
}
