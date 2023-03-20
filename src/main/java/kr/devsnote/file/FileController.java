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
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
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
        //log.info("original file name : " + file.getOriginalFilename());
        map = fileService.saveFile(file);

        if (map.get("file_id") != null) {
        //if (file.getOriginalFilename() != null) {
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

    @GetMapping("/download/{file_id}")
    public void downloadByFileId(HttpServletResponse res, HttpServletRequest req, @PathVariable(value = "file_id", required = true) String file_id, FileVO vo) {
        //String file_id = vo.getFile_id();
        log.info("file_id : " + file_id);
        vo.setFile_id(file_id);
        HashMap<String, Object> fileMap = new HashMap<>();
        HashMap<String, Object> fileMap2 = fileService.getFileInfo(vo);
        FileVO dnvo = new FileVO();

        /*
        if (CommonUtil.isNotEmpty(fileMap2)) {
            if ("connector".equals(fileMap2.get("category").toString())){
                fileMap = fileMap2;
            } else {
                // 재조회 필요
                vo.setCategory(fileMap2.get("category").toString());
                fileMap = fileService.getMinDownloadingFileInfo(vo);
            }
        }
        */

        String path = String.valueOf(fileMap.get("file_path"));
        log.info("path : " + path);
        String onlyFileName = String.valueOf(fileMap.get("file_name"));

        FileInputStream fis  = null;
        BufferedInputStream bis = null;
        //FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        String log_seq = "";

        try {
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

            File file = new File(path);

            //HttpHeaders headers = new HttpHeaders();
            //headers.setContentDisposition(ContentDisposition.builder("attachment;filename=").filename(file.getName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더

            String agent = req.getHeader("User-Agent");

            //브라우저별 한글파일 명 처리
            if(agent.contains("Trident")){ //Internet Explore
                onlyFileName = URLEncoder.encode(onlyFileName, "UTF-8").replaceAll("\\+", " ");
            } else if(agent.contains("Edge")) {//Micro Edge
                onlyFileName = URLEncoder.encode(onlyFileName, "UTF-8");
            } else {//Chrome
                onlyFileName = new String(onlyFileName.getBytes("UTF-8"), "ISO-8859-1");
            }//브라우저별 한글파일 명 처리

            res.setHeader("Content-Disposition", "attachment; filename=\"" + onlyFileName + "\";");
            res.setHeader("Content-Transfer-Encoding", "binary");
            res.setHeader("Content-Type", "application/octet-stream");
            res.setHeader("Content-Length", "" + file.length());
            res.setHeader("Pragma", "no-cache;");
            res.setHeader("Expires", "-1;");

            fis = new FileInputStream(file);
            OutputStream out  = res.getOutputStream();

            Date d = null;
            bis = new BufferedInputStream(fis);

            // FileOutputStream 으로 출력할 파일 ("response.getOutputStream()") 객체 생성 후 BufferedOutputStream 객체
            //fos = new FileOutputStream(response.getOutputStream());
            bos = new BufferedOutputStream(res.getOutputStream());

            int i = 0;
            d = new Date();
            long start = d.getTime();
            // 다운로드 시작 기록
            /*
            dnvo.setFile_id(fileMap.get("file_id").toString());
            dnvo.setStart_dtm(CommonUtil.getCurrentDateTimeFormatted());
            dnvo.setIn_ip(InetUtil.getClientIP(req));
            dnvo.setStatus("S");
            fileService.insertFileDownloadLog(dnvo);
            String seq = dnvo.getSeq();
            log_seq = seq;
            log.info("last_insert_id seq : " + seq);
            */

            // 1바이트씩 읽어서 버퍼에 담는다.
            while((i=bis.read())!=-1){
                bos.write(i);
            }

            d = new Date();
            long end = d.getTime();
            log.info("File Download - Time : {} sec", (end-start)/1000);
            // 종료 기록
            /*
            dnvo.setEnd_dtm(CommonUtil.getCurrentDateTimeFormatted());
            dnvo.setSeq(seq);
            dnvo.setStatus("E");
            fileService.updateFileDownloadLog(dnvo);
            */


            //bos.flush();
        } catch (Exception e){
            e.printStackTrace();
            // 에러시 다운로드 실패 기록
            /*
            dnvo.setEnd_dtm(CommonUtil.getCurrentDateTimeFormatted());
            dnvo.setSeq(log_seq);
            dnvo.setStatus("F");
            dnvo.setMsg(e.getMessage());
            fileService.updateFileDownloadLog(dnvo);
            */

        } finally {
            // 마지막에 FileInputStream / FileOutputStream을 닫아준다.
            // BufferedInputStream / BufferedOutputStream 도 닫아준다.
            if(bis != null) try{bis.close();}catch(IOException e){}
            if(fis != null) try{fis.close();}catch(IOException e){}

            // BufferedOutputStream 이 close() 되면서 버퍼의 내용을 출력한다.
            if(bos != null) try{bos.close();}catch(IOException e){}
            //if(fos != null) try{fos.close();}catch(IOException e){}
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
        if (CommonUtil.isNotEmpty(map) && map.get("file_id") != null) {
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
        vo.setFile_id(file_id);
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (fileService.updateFileInfo(vo) > 0) {
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
        vo.setFile_id(file_id);
        HashMap<String, Object> map = new HashMap<>();
        if (fileService.deleteFileAndInfo(vo) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    }
}
