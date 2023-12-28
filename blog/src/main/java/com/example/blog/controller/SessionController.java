package com.example.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blog.dao.BlogDao;
import java.util.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class SessionController {
    @Autowired
    BlogDao blogDao;

    @GetMapping("/")
    public String main(HttpSession session) {
        // Object memberId = session.getAttribute("memberId");
        // if (memberId != null) {
        //     return "redirect:/blog/list";
        // }
        return "html/home";
    }

    @PostMapping("/login")
    public String loginPost(
            @RequestParam("memberId") String memberId,
            @RequestParam("memberPw") String memberPw,
            HttpSession session) {

        List<Map<String, Object>> memberList = blogDao.login(memberId, memberPw);
        int count = memberList.size();
        if (count < 1) {
            return "html/loginfail";
        }

        Map<String, Object> member = memberList.get(0);

        session.setAttribute("memberId", member.get("member_id"));
        return "redirect:/blog/list";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("blog/register")
    public String register(HttpSession session){
        return "html/register";
    }

    @PostMapping("/register")
    public String registerPost(    
        @RequestParam("memberId") String memberId,
        @RequestParam("memberPw") String memberPw,
        @RequestParam("memberName") String memberName,
        HttpSession session
    ){
        int count = blogDao.checkId(memberId).size();
        String result = "";
        
        
        blogDao.register(memberId, memberPw, memberName);
        session.setAttribute("memberId", memberId );
         
        if(count > 0){
            result = "html/registerFail";
        }
        
        result = "redirect:/";
        
        return result;
    }

}
