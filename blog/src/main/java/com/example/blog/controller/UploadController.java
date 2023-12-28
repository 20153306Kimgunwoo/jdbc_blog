package com.example.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.blog.dao.BlogDao;

@Controller
public class UploadController {
    @Autowired
    BlogDao blogDao;
    
    @GetMapping("/blog/upload")
    public String blogUpload(){
        return "html/uploadtest";
    }

    @PostMapping("/blog/upload")
    public String blogUploadPost(@RequestParam("file") MultipartFile mFile,
                                  @RequestParam("originFileName") String originFileName,
                                  @RequestParam("uuid") String uuid,
                                  @RequestParam("memberId") String memberId) {
        blogDao.uploadFile(mFile, originFileName, uuid, memberId);
        return "redirect:/blog/list";
    }
}
