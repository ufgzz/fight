package com.oofgz.fight.controller;

import com.oofgz.fight.bean.LombokUser;
import com.oofgz.fight.exception.MyEvalException;
import com.oofgz.fight.exception.MyException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/Hello")
public class HelloController {

    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public String sayHello() {
        return "Hello World";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        //ModelAndView modelAndView = new ModelAndView("ftl-index");
        modelAndView.getModelMap().addAttribute("host", "https://swagger.io/");
        return modelAndView;
    }


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

    @ApiOperation(value = "测试Lombok的用法", notes = "Spring Boot以及Feign使用Java 8中LocalDate等时间日期类的问题解决")
    @ApiImplicitParam(name = "lombokUser", value = "LombokUser实体", required = true, dataType = "LombokUser")
    @RequestMapping(value = "/getLombokUser", method = RequestMethod.POST)
    public LombokUser getLombokUser(@RequestBody LombokUser lombokUser) {
        //lombokUser.setUserName("zfgican");
        return lombokUser;
    }

    /**
     * com.fasterxml.jackson.datatype:jackson-datatype-jsr310
     * 注：在设置了spring boot的parent的情况下不需要指定具体的版本，也不建议指定某个具体版本
     * 应用主类中增加这个序列化模块，并禁用对日期以时间戳方式输出的特性
     * @return ObjectMapper
     */
    /*@Bean
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }*/

}
