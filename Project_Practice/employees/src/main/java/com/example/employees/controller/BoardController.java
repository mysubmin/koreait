package com.example.employees.controller;


import com.example.employees.dto.BoardDto;
import com.example.employees.mappers.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/board")
public class BoardController {

    @Value("${spring.servlet.multipart.location}")
    String UPLOAD_LOCATION;

    @Autowired
    private BoardMapper boardMapper;

    @GetMapping("")
    public String getBoardList(Model model) {
        model.addAttribute("board", boardMapper.getBoard());
        return "board/list";
    }

    @GetMapping("/write")
    public String getBoardWrite() {
        return "board/write";
    }

    @PostMapping("/write")
    @ResponseBody
    public Map<String, Object> setBoardWrite(
            @ModelAttribute BoardDto boardDto,
            MultipartFile uploadFile) {

            Map<String, Object> map = new HashMap<>();

            try {

                if( uploadFile != null ) {
                    String saveDir =
                            new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

                    File f = new File(UPLOAD_LOCATION + "\\" + saveDir);

                    if( !f.exists() ) {
                        f.mkdir();
                    }

                    String oName = uploadFile.getOriginalFilename();
                    String ext  = oName.substring(oName.lastIndexOf("."));
                    String tName = System.currentTimeMillis() + ext;

                    Path p = Paths.get(String.valueOf(f), tName);
                    Files.write(p, uploadFile.getBytes());


                   boardDto.setKorBoardUploadName(uploadFile.getOriginalFilename());
                   boardDto.setKorBoardUploadSize(uploadFile.getSize());
                   boardDto.setKorBoardUploadTrans(tName);
                }
                boardDto.setKorBoardReplyGrp(boardMapper.getMaxCnt());
                boardMapper.setBoardWrite(boardDto);

                map.put("msg", "success");

            }catch(Exception e){
                e.printStackTrace();
            }


        return map;
    }

    @GetMapping("/delete")
    public String deleteBoard(@RequestParam int korBoardId) {
        if( korBoardId > 0 ) {
            boardMapper.deleteBoard(korBoardId);
        }
        return "redirect:/admin/board?page=1";
    }

    @GetMapping("/view")
    public String viewBoard(@RequestParam int korBoardId, Model model) {
        if( korBoardId > 0 ) {
            model.addAttribute("board", boardMapper.viewBoard(korBoardId));
        }
        return "board/view";
    }


    @GetMapping("/modify")
    public String modifyBoard(@RequestParam int korBoardId, Model model) {
        if( korBoardId > 0 ) {
            model.addAttribute("board", boardMapper.viewBoard(korBoardId));
        }
        return "board/modify";
    }

    @PostMapping("/modify")
    @ResponseBody
    public Map<String, Object> setBoardUpdate(
            @ModelAttribute BoardDto boardDto,
            MultipartFile uploadFile) {

        Map<String, Object> map = new HashMap<>();

        System.out.println(boardDto);
        System.out.println(uploadFile.getOriginalFilename());

        return map;
    }

    @GetMapping("/reply")
    public String setReply(@ModelAttribute BoardDto boardDto, Model model) {
        model.addAttribute("board", boardMapper.viewBoard(boardDto.getKorBoardId()));
        return "board/reply";
    }

    @PostMapping("/reply")
    @ResponseBody
    public Map<String, Object> setReply(@ModelAttribute BoardDto boardDto,
                           MultipartFile uploadFile) {
        Map<String, Object> map = new HashMap<>();

        try {

            if( uploadFile != null ) {
                String saveDir =
                        new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

                File f = new File(UPLOAD_LOCATION + "\\" + saveDir);

                if( !f.exists() ) {
                    f.mkdir();
                }

                String oName = uploadFile.getOriginalFilename();
                String ext  = oName.substring(oName.lastIndexOf("."));
                String tName = System.currentTimeMillis() + ext;

                Path p = Paths.get(String.valueOf(f), tName);
                Files.write(p, uploadFile.getBytes());


                boardDto.setKorBoardUploadName(uploadFile.getOriginalFilename());
                boardDto.setKorBoardUploadSize(uploadFile.getSize());
                boardDto.setKorBoardUploadTrans(tName);
            }

            /* reply에 필요한 값 설정 */
            boardDto.setKorBoardReplyGrp(boardDto.getKorBoardReplyGrp());
            boardDto.setKorBoardReplyGrpSeq(boardDto.getKorBoardReplyGrpSeq() + 1);
            boardDto.setKorBoardReplyGrpSeqIndent(boardDto.getKorBoardReplyGrpSeqIndent()+ 1);


            boardMapper.replyBoard(boardDto);

            map.put("msg", "success");

        }catch(Exception e){
            e.printStackTrace();
        }

        return map;
    }

}
