package com.example.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;
import com.example.blog.dao.BlogDao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class DBController {
    @Autowired
    BlogDao blogDao;

    // @GetMapping("/")
    // public String main(){
    //     return "html/home";
    // }
    @GetMapping("/blog/list")
    public String blogList(Model model, HttpSession session) {
    String loggedInUserId = (String) session.getAttribute("memberId");

    

    List<Map<String, Object>> qrySet = blogDao.listSelect();
    model.addAttribute("qrySet", qrySet);
    model.addAttribute("loggedInUserId", loggedInUserId);
    return "html/list";
}
    
    // @GetMapping("/blog/list")
    // public String blogList(Model model) {
    // List<Map<String, Object>> qrySet = blogDao.listSelect();
    // model.addAttribute("qrySet", qrySet);
    
    // return "html/list";  
    // }


 

@GetMapping("blog/detail")
public String blogDetail(
        HttpServletRequest request,
        Model model) {
    String seq = request.getParameter("seq");
    String title = blogDao.detailSelect(seq).get(0).get("title").toString();
    String content = blogDao.detailSelect(seq).get(0).get("content").toString();
    String searchCount = blogDao.detailSelect(seq).get(0).get("search_count").toString();
    searchCount = Integer.toString(Integer.parseInt(searchCount) + 1);
    blogDao.updateSearchCount(seq, searchCount);
    // int count = blogDao.getCountAnswer(seq).size();

    List<Map<String, Object>> answerQrySet = blogDao.getCountAnswer(seq);
    
    model.addAttribute("title", title);
    model.addAttribute("content", content);
    model.addAttribute("answers", answerQrySet);
    model.addAttribute("seq", seq);

    return "html/detail"; 
}

@GetMapping("blog/insert")
public String insert(){
    return "html/insert";
}

@GetMapping("blog/insert/execute")
public String blogInsert( 
    @RequestParam("title") String title,
    @RequestParam("content") String content,
    @RequestParam("writer") String writer){
    blogDao.insert(title, content, writer);
    return "redirect:/blog/list"; 
}

    @GetMapping("blog/answer")
    public String blogAnswer(
        @RequestParam("seq") String seq,
        Model model
    ){
        List<Map<String, Object>> detail = blogDao.detailSelect(seq);
        String title = detail.get(0).get("title").toString();
        String content = detail.get(0).get("content").toString();
        model.addAttribute("title", title);
        model.addAttribute("content", content);
        model.addAttribute("seq", seq);
        return "html/answer";
    }


    @GetMapping("/blog/answer/execute")
    public String blogAnswerInsert(
            @RequestParam("seq") String seq,
            @RequestParam("answer") String answer) {
        blogDao.insertAnswer(seq, answer);
        return "redirect:/blog/detail?seq=" + seq;
    }

    @GetMapping("blog/update")
    public String blogUpdate(@RequestParam("seq") String seq, Model model) {
        Map<String, Object> blogDetail = blogDao.detailSelect(seq).get(0);
        model.addAttribute("seq", seq);
        model.addAttribute("title", blogDetail.get("title"));
        model.addAttribute("content", blogDetail.get("content"));
        return "html/update";
    }

    @GetMapping("blog/update/execute")
    public String blogUpdateExecute(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("seq") String seq) {   
        blogDao.update(title, content, seq);
        return "redirect:/blog/detail?seq=" + seq;
    }

    @GetMapping("/blog/delete")
    public String blogDelete(@RequestParam("seq") String seq) {
        blogDao.detailSelect(seq).get(0).get("title");
        blogDao.delete(seq);
        return "redirect:/blog/list";
    }

    

    
    

}