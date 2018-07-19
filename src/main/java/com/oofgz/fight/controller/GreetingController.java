package com.oofgz.fight.controller;

import com.oofgz.fight.bean.Greeting;
import com.oofgz.fight.util.MoneyRoundUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(
                counter.incrementAndGet(),
                String.format(template, name)
        );
    }

    /**
     * 取整方式
     * @param xMoney
     * @param roundMode
     * @param roundType
     * @return
     */
    @RequestMapping("/commonRound")
    public String commonRound(@RequestParam(value = "xMoney") String xMoney,
                              @RequestParam(value = "roundMode") String roundMode,
                              @RequestParam(value = "roundType") String roundType) {
        return MoneyRoundUtil.commonRoundMoney(xMoney, roundMode, roundType);
    }

    public static void main(String[] args) {
        String xMoney = "52.58";
        String roundMode = "2";
        String roundType = "2";
        GreetingController controller = new GreetingController();
        System.out.println("取整前：[" + xMoney + "], 取整后：[" + controller.commonRound(xMoney, roundMode, roundType) + "]");
    }
}
