package com.example.employees.controller;


import com.example.employees.dto.BoardDto;
import com.example.employees.mappers.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/admin/board")
public class BoardController {

    @Value("${spring.servlet.multipart.location}")
    String UPLOAD_LOCATION;

    @Autowired
    private BoardMapper boardMapper;

    @GetMapping("")
    public String getBoardList() {
        return "board/list";
    }

    @GetMapping("/write")
    public String getBoardWrite() {
        return "board/write";
    }

    @PostMapping("/write")
    @ResponseBody
    public String setBoardWrite(
            @ModelAttribute BoardDto boardDto, MultipartFile uploadFile) {
        //원본파일 이름, 원본파일 용량, 원본파일 이름 바꾸기(날짜로 )
//        System.out.println("원본파일명 : " + uploadFile.getOriginalFilename());
//        System.out.println("원본파일용량 : " + uploadFile.getSize() + "bytes");
//
//        String oName = uploadFile.getOriginalFilename();
//        String ext  = oName.substring(oName.lastIndexOf("."));
//        System.out.println("원본파일확장자 : " + ext);
//
//        String tName = System.currentTimeMillis() + ext;
//        System.out.println("변환된파일명 : " + tName);

        try {
            if(uploadFile != null) {
                // 날짜별 폴더 생성 만들기
                String saveDir = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                // 폴더생성 : File 객체 사용
                File f = new File(UPLOAD_LOCATION + "\\" + saveDir);

                if(!f.exists()) {
                    f.mkdir();
                }

                boardDto.setKorBoardUploadName(uploadFile.getOriginalFilename());
                boardDto.setKorBoardUploadSize(uploadFile.getSize());

                String oName = uploadFile.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String tName = System.currentTimeMillis() + ext;

                Path p = Paths.get(String.valueOf(f), tName);
                Files.write(p, uploadFile.getBytes());

                boardDto.setKorBoardUploadTrans(tName);
            }
            boardMapper.setBoardWrite(boardDto);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
