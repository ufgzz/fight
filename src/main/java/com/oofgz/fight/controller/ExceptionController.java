package com.oofgz.fight.controller;

import com.oofgz.fight.dto.exception.MyEvalException;
import com.oofgz.fight.dto.exception.MyException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Exception")
public class ExceptionController {

    @RequestMapping(value = "/error-normal", method = RequestMethod.GET)
    public String errorNormal(ModelMap modelMap) throws Exception {
        throw new Exception("发生错误Exception" + " , author : " + modelMap.get("author"));
    }

    @RequestMapping(value = "/error-define", method = RequestMethod.GET)
    public String errorDefine(ModelMap modelMap) throws MyException {
        throw new MyException("发生错误MyException" + " , author : " + modelMap.get("author"));
    }

    @RequestMapping(value = "/error-define-eval", method = RequestMethod.GET)
    public String errorDefineEval(ModelMap modelMap) throws MyEvalException {
        throw new MyEvalException("发生错误MyEvalException" + " , author : " + modelMap.get("author"));
    }
}
