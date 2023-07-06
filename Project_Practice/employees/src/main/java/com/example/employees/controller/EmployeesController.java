package com.example.employees.controller;


import com.example.employees.mappers.EmployeeMapper;
import com.example.employees.mappers.LevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EmployeesController {

    private EmployeeMapper employeeMapper;

    @Autowired
    private LevelMapper levelMapper;

    @Autowired
    public EmployeesController(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @GetMapping("/admin/employees")
    public String getEmpList(Model model) {
        model.addAttribute("emp", employeeMapper.getEmpList());
        return "admin/employees";
    }

    @GetMapping("/admin/employees/delete")
    @ResponseBody
    public Map<String, Object> deleteEmp(@RequestParam int korEmpId) {
        Map<String, Object> map = new HashMap<>();
        if( korEmpId > 0 ) {
            employeeMapper.deleteEmp(korEmpId);
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

}
