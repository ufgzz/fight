package com.oofgz.fight.controller;

import com.oofgz.fight.bean.Greeting;
import com.oofgz.fight.util.MoneyRoundUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(value = "/Greet")
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParam(name = "name", value = "用户名称")
    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
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
    @ApiOperation(value = "取整", notes = "根据取整方式和取整模式进行取整")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xMoney", value = "金额", required = true),
            @ApiImplicitParam(name = "roundMode", value = "取整模式", required = true),
            @ApiImplicitParam(name = "roundType", value = "取整方式", required = true)
    })
    @RequestMapping(value = "/commonRound", method = RequestMethod.POST)
    public String commonRound(@RequestParam(value = "xMoney", defaultValue = "0") String xMoney,
                              @RequestParam(value = "roundMode", defaultValue = "3") String roundMode,
                              @RequestParam(value = "roundType", defaultValue = "0") String roundType) {
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
