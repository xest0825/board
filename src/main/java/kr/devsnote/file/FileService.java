package kr.devsnote.file;

import kr.devsnote.excel.ExcelHandler;
import kr.devsnote.excel.ExcelVO;
import kr.devsnote.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private FileDAO fileDAO;

    /**
     * 파일저장
     * @param file
     * @return HashMap<String, Object>
     */
    public HashMap<String, Object> saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        FileVO fileInfo = new FileVO();

        // 원래 파일 이름 추출
        String origName = file.getOriginalFilename();
        fileInfo.setOrg_file_name(origName);
        log.info("origName : " + fileInfo.getOrg_file_name());

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();
        //log.info("file_id : " + uuid);
        fileInfo.setFile_id(uuid);
        log.info("file_id : " + fileInfo.getFile_id());

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));
        String extension_only = origName.substring(origName.lastIndexOf(".")).replace(".", "");
        log.info("extension : " + extension);
        log.info("extension_only : " + extension_only);
        fileInfo.setFile_ext(extension_only);

        // uuid와 확장자 결합
        String savedName = uuid + extension;
        log.info("savedName : " + savedName);
        fileInfo.setFile_name(savedName);

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = "C:\\weowefsodf\\" + savedName;
        //String savedPath = fileDir + savedName;
        log.info("savedPath : " + savedPath);
        fileInfo.setFile_path(savedPath);

        String fileSize = String.valueOf(file.getSize());
        log.info("fileSize : " + fileSize);
        fileInfo.setFile_size(fileSize);

        // 실제로 로컬에 uuid를 파일명으로 저장
        file.transferTo(new File(savedPath));

        fileInfo.setIn_id("admin");
        fileInfo.setUp_id("admin");
        fileDAO.insertFileInfo(fileInfo);

        retMap.put("file_id", uuid);
        return retMap;
    }

    /**
     * 파일정보 목록 조회
     * @param vo
     * @return List<HashMap<String, Object>>
     */
    public List<HashMap<String, Object>> getFileInfoList(FileVO vo) {
        return fileDAO.getFileInfoList(vo);
    }

    /**
     * 파일정보 조회
     * @param vo
     * @return HashMap<String, Object>
     */
    public HashMap<String, Object> getFileInfo(FileVO vo) {
        return fileDAO.getFileInfo(vo);
    }

    public ExcelHandler getFileInfoListExcel(FileVO vo){
        ExcelHandler eh = null;

        String[] tiltestemp = {};

        String[] fieldstemp = {};

        tiltestemp = new String [] {"파일ID", "원 파일명", "파일경로", "확장자", "파일크기", "입력자", "입력일시", "수정자", "수정일시", "시스템구분"};
        fieldstemp = new String [] {"file_id", "org_file_name", "file_path", "file_ext", "file_size", "in_id" , "in_dtm", "up_id", "up_dtm", "system_type"};

        ArrayList<String> titleList = new ArrayList<String>();
        ArrayList<String> fieldsList = new ArrayList<String>();

        Collections.addAll(titleList, tiltestemp);
        Collections.addAll(fieldsList,fieldstemp);

        eh = new ExcelHandler(vo.getExcel_path() ,titleList, fieldsList);
        fileDAO.getFileInfoListExcel(vo, eh);

        //컬럼 사이즈 설정
        if(eh.getRowindex() != 0) {
            for(int i=0; i<tiltestemp.length; i++) {
                eh.getDataSheet().setColumnWidth(i, (eh.getDataSheet().getColumnWidth(i)) + 2048); //(int)1 : 약 0.03픽셀
            }
        }

        return eh;
    }

    /**
     * 파일정보 입력
     * @param vo
     * @return
     */
    public int insertFileInfo(FileVO vo) {
        return fileDAO.insertFileInfo(vo);
    }

    /**
     * 파일정보 수정
     * @param vo
     * @return int
     */
    public int updateFileInfo(FileVO vo) {
        return fileDAO.updateFileInfo(vo);
    }

    /**
     * 파일정보 삭제
     * @param vo
     * @return int
     */
    public int deleteFileAndInfo(FileVO vo) {
        HashMap<String, Object> fileMap = new HashMap<String, Object>();
        fileMap = getFileInfo(vo);
        int ret = 0;
        if (CommonUtil.isNotEmpty(fileMap) && CommonUtil.isNotEmpty((String)fileMap.get("file_path"))) {
            log.debug("파일삭제:{}", String.valueOf(fileMap.get("file_path")));
            File file = new File(String.valueOf(fileMap.get("file_path")));

            file.delete();
            ret = fileDAO.deleteFileInfo(vo);
        }
        return ret;
    }



}
