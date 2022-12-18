package kr.devsnote.file;

import kr.devsnote.excel.ExcelHandler;
import kr.devsnote.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping({"/upload", "/files"})
    public ResponseEntity<HashMap<String, Object>> uploadFile(@RequestParam(value="file", required = false) MultipartFile file) throws IOException {
        log.info("[POST] /upload");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = fileService.saveFile(file);

        /*
        for (MultipartFile multipartFile : files) {
            map = fileService.saveFile(multipartFile);
        }
         */

        if (map.get("file_id") != null) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    }

    @GetMapping("/download")
    public ResponseEntity<Object> download(FileVO vo) {
        String file_id = vo.getFile_id();
        log.info("file_id : " + file_id);
        HashMap<String, Object> fileMap = fileService.getFileInfo(vo);

        String path = String.valueOf(fileMap.get("file_path"));
        log.info("path : " + path);

        try {
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

            File file = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더

            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/files-info-excel")
    public void downloadFilesInfoExcel(HttpServletRequest request, HttpServletResponse response, FileVO vo) {
        log.info("[POST] /files-info-excel");
        ExcelHandler eh = fileService.getFileInfoListExcel(vo);

        if (eh.getRowindex() == 0) {
            CommonUtil.sendAlertToView(response, "info", "상품관리 - 엑셀다운로드", "조회 데이타가 없습니다.");
        } else {
            String filename = "fileInfo_" + CommonUtil.getCurrentDateTime() + ".xlsx";
            eh.sendResponse(response, filename);
        }
    }

    @GetMapping("/files-info")
    public ResponseEntity<List<HashMap<String, Object>>> getFileInfoList(FileVO vo) throws IOException {
        log.info("[GET] /files-info");
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list = fileService.getFileInfoList(vo);
        ResponseEntity<List<HashMap<String, Object>>> ret = new ResponseEntity<>(list, HttpStatus.OK);
        return ret;
    }

    @GetMapping("/files-info/{file_id}")
    public ResponseEntity<HashMap<String, Object>> getFileInfoList(FileVO vo, @PathVariable String file_id) throws IOException {
        log.info("[GET] /file-info/" + file_id);
        vo.setFile_id(file_id);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = fileService.getFileInfo(vo);
        if (map.get("file_id") != null) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    }

    @PutMapping("/files-info/{file_id}")
    public ResponseEntity<HashMap<String, Object>> updateFileInfo(@RequestBody FileVO vo, @PathVariable String file_id) throws IOException {
        log.info("[PUT] /file-info/" + file_id);
        int retint = 0;
        vo.setFile_id(file_id);
        HashMap<String, Object> map = new HashMap<String, Object>();
        retint = fileService.updateFileInfo(vo);
        if (retint > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    }

    @DeleteMapping("/files-info/{file_id}")
    public ResponseEntity<HashMap<String, Object>> deleteFileInfo(@RequestBody FileVO vo, @PathVariable String file_id) throws IOException {
        log.info("[DELETE] /file-info/" + file_id);
        int retint = 0;
        vo.setFile_id(file_id);
        HashMap<String, Object> map = new HashMap<String, Object>();
        retint = fileService.deleteFileAndInfo(vo);
        if (retint > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    }
}
