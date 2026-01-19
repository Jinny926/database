package com.meta.memo.controller;

import com.meta.memo.domain.Memo;
import com.meta.memo.dto.MemoRequestDto;
import com.meta.memo.dto.MemoResponseDto;
import com.meta.memo.service.MemoService;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;



@RestController
@RequestMapping("api/memos")
public class MemoController {
    // JDBC를 통한 MySQL 연결
    private final JdbcTemplate jdbcTemplate;

    public MemoController(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @PostMapping
    public MemoResponseDto creatMemo(@RequestBody MemoRequestDto memoRequestDto){
        MemoService memoService = new MemoService(jdbcTemplate);

        return memoService.creatMemo(memoRequestDto);
    }

    @GetMapping()
    public List<MemoResponseDto> getMemos() {
        MemoService memoService= new MemoService(jdbcTemplate);
        return memoService.getMemos();
    }

    @PutMapping("{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto memoRequestDto){
        MemoService memoService= new MemoService(jdbcTemplate);
        return memoService.updateMemo(id, memoRequestDto);
    }

    @DeleteMapping("{id}")
    public Long deleteMemo(@PathVariable Long id){
        MemoService memoService= new MemoService(jdbcTemplate);
        return memoService.deleteMemo(id);
    }
}
