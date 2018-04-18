package com.github.qiyue.code.factory.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * created by yebinghuan on 2018/4/17
 *
 * 视图控制器
 */
@Controller
@RequestMapping("")
public class ViewController {

    @GetMapping(value = "index")
    public String index(Map<String,Object> map){
        return "index";
    }

    @GetMapping("about")
    public String about(){
        return "about";
    }


    @GetMapping("codeFactory")
    public String user(){
        return "codeFactory/list";
    }
}
