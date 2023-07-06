package com.example.employees.mappers;


import com.example.employees.dto.EmployeeDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Select("SELECT * FROM kor_employees ORDER BY kor_emp_id DESC")
    List<EmployeeDto> getEmpList();

    @Delete("DELETE FROM kor_employees WHERE kor_emp_id = #{korEmpId}")
    void deleteEmp(int korEmpId);

    @Select("SELECT * FROM kor_employees WHERE kor_emp_id = #{korEmpId}")
    EmployeeDto getEmpView(int korEmpId);
}







