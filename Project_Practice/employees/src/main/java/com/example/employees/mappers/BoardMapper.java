package com.example.employees.mappers;

import com.example.employees.dto.BoardDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    @Insert("INSERT INTO kor_board VALUES(NULL, #{korBoardNotice}, #{korBoardWriter}, #{korBoardSubject}, #{korBoardContent}, " +
                                            "#{korBoardUploadName}, #{korBoardUploadSize}, #{korBoardUploadTrans}, 0, 1, 1, 1, NOW())")
    void setBoardWrite(BoardDto boardDto);
}
