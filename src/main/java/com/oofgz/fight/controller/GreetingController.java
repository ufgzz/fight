package com.oofgz.fight.controller;

import com.oofgz.fight.dto.greeting.Greeting;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(value = "/Greet")
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @ApiOperation(value = "获取用户名称（返回Hello）", notes = "获取用户名称（返回Hello, ${name}）")
    @ApiImplicitParam(name = "name", value = "用户名称")
    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(
                counter.incrementAndGet(),
                String.format(template, name)
        );
    }

    @ApiOperation(value = "返回Hello World", notes = "返回Hello World")
    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public String sayHello() {
        return "Hello World";
    }

    @ApiOperation(value = "返回index页面", notes = "返回/index.html")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        //ModelAndView modelAndView = new ModelAndView("ftl-index");
        modelAndView.getModelMap().addAttribute("host", "https://swagger.io/");
        return modelAndView;
    }

}
