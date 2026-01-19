package com.meta.memo.service;

import com.meta.memo.domain.Memo;
import com.meta.memo.dto.MemoRequestDto;
import com.meta.memo.dto.MemoResponseDto;
import com.meta.memo.repository.MemoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MemoService {
    // JDBC를 통한 MySQL 연결
    private final JdbcTemplate jdbcTemplate;

    public MemoService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    public MemoResponseDto creatMemo(@RequestBody MemoRequestDto memoRequestDto){
        // RequestDto -> Entity 변환
        Memo newMemo= new Memo(memoRequestDto);

        MemoRepository memoRepository = new MemoRepository(jdbcTemplate);
        Memo saveMemo = memoRepository.save(newMemo);

        // Entity -> ResponseDto 변환
        MemoResponseDto memoResponseDto=new MemoResponseDto(saveMemo);

        return null;
    }


    public List<MemoResponseDto> getMemos() {
       MemoRepository memoRepository = new MemoRepository(jdbcTemplate);
       List<MemoResponseDto> memoResponseDtoList = memoRepository.findAll();
        return memoResponseDtoList;
    }


    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto memoRequestDto){
        // 해당 id의 메모가 존재하는지 확인
        Memo foundMemo=findById(id);

        // 메모 내용 수정
        if(foundMemo != null){
            String sql="UPDATE memo SET username = ?, contents = ? WHERE id = ?";
            jdbcTemplate.update(sql, memoRequestDto.getUsername(), memoRequestDto.getContents(), id);

            return id;
        } else{
            throw new IllegalArgumentException("선택한 id의 메모는 존재하지 않습니다.");
        }
    }


    public Long deleteMemo(@PathVariable Long id){
        // 해당 id의 메모가 데이터베이스에 존재하는지 확인
        Memo foundMemo=findById(id);

        // 메모 내용 삭제
        if(foundMemo != null){
            String sql ="DELETE FROM memo WHERE id = ?";
            jdbcTemplate.update(sql,id);

            return id;
        }else{
            throw new IllegalArgumentException("선택한 id의 메모는 존재하지 않습니다.");
        }

    }

    //특정 id의 메모 존재 여부 확인 공용메서드
    private Memo findById(Long id){
        // DB 조회
        String sql ="SELECT * FROM memo where id = ?";

        return jdbcTemplate.query(sql, resultSet ->{
            if(resultSet.next()){
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            }else{
                return null;
            }
        },id);


    }

}
