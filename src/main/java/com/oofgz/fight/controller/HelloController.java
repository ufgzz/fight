package com.oofgz.fight.controller;

import com.oofgz.fight.exception.MyEvalException;
import com.oofgz.fight.exception.MyException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/Hello")
public class HelloController {

    @RequestMapping("/sayHello")
    public String sayHello() {
        return "Hello World";
    }


    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        //ModelAndView modelAndView = new ModelAndView("ftl-index");
        modelAndView.getModelMap().addAttribute("host", "https://swagger.io/");
        return modelAndView;
    }


    @RequestMapping("/error-normal")
    public String errorNormal(ModelMap modelMap) throws Exception {
        throw new Exception("发生错误Exception" + " , author : " + modelMap.get("author"));
    }

    @RequestMapping("/error-define")
    public String errorDefine(ModelMap modelMap) throws MyException {
        throw new MyException("发生错误MyException" + " , author : " + modelMap.get("author"));
    }

    @RequestMapping("/error-define-eval")
    public String errorDefineEval(ModelMap modelMap) throws MyEvalException {
        throw new MyEvalException("发生错误MyEvalException" + " , author : " + modelMap.get("author"));
    }

}
