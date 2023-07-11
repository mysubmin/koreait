package com.example.employees.controller;


import com.example.employees.dto.EmployeeDto;
import com.example.employees.mappers.EmployeeMapper;
import com.example.employees.mappers.LevelMapper;
import com.example.employees.mappers.ReigsterMapper;
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
import java.util.HashMap;
import java.util.Map;

@Controller
public class EmployeesController {

    private String UPLOAD_LOCATION = "D:\\koreait\\Java\\spring\\employees\\src\\main\\resources\\static\\upload";

    private EmployeeMapper employeeMapper;

    @Autowired
    private LevelMapper levelMapper;

    @Autowired
    private ReigsterMapper reigsterMapper;

    @Autowired
    public EmployeesController(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @GetMapping("/admin/employees")
    public String getEmpList(Model model) {
        model.addAttribute("dept", reigsterMapper.getDept());
        model.addAttribute("emp", employeeMapper.getEmpList());
        model.addAttribute("level", employeeMapper.getLevel());
        return "admin/employees";
    }

    @GetMapping("/admin/employees/delete")
    @ResponseBody
    public Map<String, Object> deleteEmp(@RequestParam int korEmpId) {
        Map<String, Object> map = new HashMap<>();
        if( korEmpId > 0 ) {
            EmployeeDto edto = employeeMapper.getImageName(korEmpId); //image 파일

            System.out.println(edto.getKorEmpImageName());
            File file = new File(UPLOAD_LOCATION + "\\" + edto.getKorEmpImageName());
            boolean b = file.delete();
            if( b ) {
                System.out.println("이미지 삭제 성공");
            }

            employeeMapper.deleteEmp(korEmpId); //db
            map.put("msg", "success");
        }
        return map;
    }

    @GetMapping("/admin/employees/view")
    public String getEmpView(@RequestParam int korEmpId, Model model) {
        if( korEmpId > 0 ) {
            model.addAttribute("emp", employeeMapper.getEmpView(korEmpId));
            model.addAttribute("level", levelMapper.getLevel());
        }
        return "admin/empView";
    }

    @GetMapping("/admin/employees/popup")
    public String getPopup() {
        return "admin/popup";
    }

    @PostMapping("/admin/employees/upload")
    @ResponseBody
    public Map<String, Object> fileUpload(MultipartFile uploadFile, int korEmpId) {
        Map<String, Object> map = new HashMap<>();

        try {
            if( uploadFile != null ) {

                EmployeeDto employeeDto = new EmployeeDto();
                employeeDto.setKorEmpImageName(uploadFile.getOriginalFilename());
                employeeDto.setKorEmpImageSize(uploadFile.getSize());
                employeeDto.setKorEmpId(korEmpId);

                employeeMapper.fileUpload(employeeDto);

                Path path = Paths.get(UPLOAD_LOCATION, uploadFile.getOriginalFilename());
                Files.write(path, uploadFile.getBytes());

                map.put("msg", "success");
            }

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @GetMapping("/admin/employees/updateLevel")
    @ResponseBody
    public Map<String, Object> updateLevel(@ModelAttribute EmployeeDto employeeDto) {
        Map<String, Object> map = new HashMap<>();

        if(employeeDto.getKorEmpId() > 0 && employeeDto.getKorEmpLevel() > 0) {
            //update query
            employeeMapper.updateLevel(employeeDto);
            map.put("msg", "success");
        }
        return map;
    }

}
