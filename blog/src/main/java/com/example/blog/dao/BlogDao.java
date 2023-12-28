package com.example.blog.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.io.File;
@Repository
public class BlogDao {
    @Autowired
    JdbcTemplate jt;

    public List<Map<String, Object>> listSelect(){
        String sqlStmt = "select seq, title, writer, search_count from blog"; 
        return jt.queryForList(sqlStmt);
    }

     public List<Map<String, Object>> detailSelect(String seq){
        String sqlStmt = String.format("select seq, title, content, search_count from blog where seq = '%s'",seq);
        return jt.queryForList(sqlStmt);
    }

    public void insert(String title, String content, String writer){
        String sqlStmt = String.format("insert into blog(title, content, writer) values('%s','%s','%s')",title,content,writer);
        jt.execute(sqlStmt);
    } 
    public void delete(String seq){
        String sqlStmtMain = String.format("delete from blog where seq = '%s'",seq);
        String sqlStmtDetail = String.format("delete from blog_answer where seq = '%s'",seq);
        jt.execute(sqlStmtMain);
        jt.execute(sqlStmtDetail);
    }

    public void update(String title, String content, String seq){
        String sqlStmt = String.format("update blog set title='%s', content='%s' where seq='%s'",title,content,seq);
        jt.execute(sqlStmt);
    }

    public void updateSearchCount(String seq, String searchCount){
        String sqlStmt = String.format("update blog set search_count='%s' where seq='%s'",searchCount,seq);
        jt.execute(sqlStmt);
    }

    public void insertAnswer(String seq, String answer){
        String sqlStmt = String.format("insert into blog_answer(seq, answer) values('%s', '%s')",seq,answer);
        jt.execute(sqlStmt);
    }

    public List<Map<String, Object>> getCountAnswer(String seq){
        String sqlStmt = String.format("select answer from blog_answer where seq='%s'",seq);
        return jt.queryForList(sqlStmt);
    }

    public List<Map<String, Object>> login(String memberId, String memberPw){
        String sqlStmt = String.format("select * from blogmember where member_id = '%s' AND member_pw = '%s'",memberId,memberPw);
        return jt.queryForList(sqlStmt, memberId, memberPw);
    }
    
    public void register(String memberId, String memberPw, String memberName){
        String sqlStmt = String.format("insert into blogmember(member_id,member_pw,member_name) values('%s','%s','%s')",memberId,memberPw,memberName);
        jt.execute(sqlStmt);
    }

    public List<Map<String,Object>> checkId(String memberId){
        String sqlStmt = String.format("select count(*) from blogmember where member_id='%s'",memberId);
        return jt.queryForList(sqlStmt,memberId);
    }

    public void uploadFile(MultipartFile mFile, String originFileName, String uuid, String memberId) {
        String fileName = mFile.getOriginalFilename();
        String memberFileName = memberId + "_" + fileName;
        String saveFolder = "C:/work/blog/src/main/resources/static/image/"; // Update the path accordingly
    
        try {
            mFile.transferTo(new File(saveFolder + memberFileName));
    
            String sqlStmt = String.format("insert into blog_upload(origin_file_name, uuid, member_id) values ( '%s', '%s', '%s')",
                    originFileName, uuid, memberId);
            jt.update(sqlStmt);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }
}
