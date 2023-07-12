package com.example.employees.service;


import com.example.employees.dto.EmployeeDto;
import com.example.employees.dto.PagingDto;
import com.example.employees.mappers.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PagingSrv {

    @Autowired
    private EmployeeMapper employeeMapper;

    public PagingDto pageCalc(int page) {
        int totalCount = employeeMapper.getTotalCount();
        PagingDto pDto = new PagingDto();

        int totalPage = (int) Math.ceil(((double) totalCount / pDto.getPageCount()));
        int startPage = (((int) Math.ceil((double) page / pDto.getBlockCount())) - 1)  * pDto.getBlockCount() + 1;

        int endPage = startPage + pDto.getBlockCount() - 1;

        if( endPage > totalPage ) {
            endPage = totalPage;
        }

        pDto.setPage(page);
        pDto.setTotalPage(totalPage);
        pDto.setStartPage(startPage);
        pDto.setEndPage(endPage);

        return pDto;
    }

    public List<EmployeeDto> getPagingEmp(int page) {
        Map<String, Object> map = new HashMap<>();
        PagingDto pagingDto = new PagingDto();

        int pageStartNum = (page - 1) * pagingDto.getPageCount();

        map.put("start", pageStartNum);
        map.put("limit", pagingDto.getPageCount());

        return employeeMapper.getEmpList(map);
    }

}






