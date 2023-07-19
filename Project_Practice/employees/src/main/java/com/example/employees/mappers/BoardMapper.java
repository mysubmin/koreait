package com.example.employees.mappers;


import com.example.employees.dto.BoardDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert(" INSERT INTO kor_board VALUES(NULL, " +
            "#{korBoardNotice}, #{korBoardWriter}, #{korBoardSubject}, #{korBoardContent}, #{korBoardUploadName}, " +
            "#{korBoardUploadSize}, #{korBoardUploadTrans}, 0, #{korBoardReplyGrp}, 1, 1, NOW() ) ")
    void setBoardWrite(BoardDto boardDto);

    //ifnull(값, 1)
    @Select("SELECT ifnull( MAX(kor_board_reply_grp) + 1, 1 ) as Maxcnt FROM kor_board")
    int getMaxCnt();

    @Select("SELECT * FROM kor_board " +
            "ORDER BY kor_board_notice DESC, " +
            "kor_board_reply_grp DESC")
    List<BoardDto> getBoard();

    @Delete("DELETE FROM kor_board WHERE kor_board_id = #{korBoardId}")
    void deleteBoard(int korBoardId);

    @Select("SELECT * FROM kor_board WHERE kor_board_id = #{korBoardId}")
    BoardDto viewBoard(int korBoardId);

    @Insert(" INSERT INTO kor_board VALUES(NULL, " +
            "#{korBoardNotice}, #{korBoardWriter}, #{korBoardSubject}, #{korBoardContent}, #{korBoardUploadName}, " +
            "#{korBoardUploadSize}, #{korBoardUploadTrans}, 0, #{korBoardReplyGrp}, #{korBoardReplyGrpSeq}, #{korBoardReplyGrpSeqIndent}, NOW() ) ")
    void replyBoard(BoardDto boardDto);
}
