package com.example.upload2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadController {
    @Autowired
    private UploadMapper uploadMapper;

    @GetMapping("/main")
    public String getMain() { return "main"; }
    @GetMapping("/list")
    public String getList() {
        return "list";
    }

    @GetMapping("/upload")
    public String getUpload() {
        return "upload";
    }
    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> fileUpload(MultipartFile uploadFile) {
        Map<String, Object> map = new HashMap<>();
        try {
            String UPLOAD_DIR = "C:\\temp2\\upload";
            String oName = uploadFile.getOriginalFilename();
            Long fileSize = uploadFile.getSize();
            
            // 폴더이름은 2023-06-27
            String folderName = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
            File f = new File(UPLOAD_DIR + "\\" + folderName);

            if(!f.exists()) {
                f.mkdir();
                System.out.println("폴더가 생성 되었습니다.");
            }

            // 이름.jpg => .을 기준으로 뒷에만 추출(lastIndex) => 시간.jpg
            String ext = oName.substring(oName.lastIndexOf("."));
            String changedName = System.currentTimeMillis() + ext;

            Path path = Paths.get(f.toString(), changedName);
            Files.write(path, uploadFile.getBytes());


            map.put("msg","success");
            map.put("oName",oName);
            map.put("size",((double)fileSize/1000));

        }catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> checkLogin(@ModelAttribute UploadDto uploadDto, HttpServletRequest req) {
        UploadDto emp = uploadMapper.getEmp(uploadDto);

        Map<String, Object> map = new HashMap<>();
        if(emp != null) {
            HttpSession hs = req.getSession(); // 세션 객체 만든 후
            hs.setAttribute("sessInfo", emp); // 세션 객체에 개인 정보 삽입
            hs.setMaxInactiveInterval(60 * 30); // 세션 유효 시간
            map.put("msg","success");
        }else {
            map.put("msg", "failure");
        }
        return map;
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession hs) {
        hs.invalidate();
        return "redirect:/login";
    }

}
