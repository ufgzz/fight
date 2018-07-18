package com.oofgz.fight.controller;

import com.oofgz.fight.bean.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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
        String resultXMoney = doRoundMode(new BigDecimal(xMoney), roundMode, roundType);
        return resultXMoney;
    }


    /**
     * 根据取整方式和取整模式进行取整
     * @param xMoney
     * @param roundMode
     * @param roundType
     * @return
     */
    private String doRoundMode(BigDecimal xMoney, String roundMode, String roundType) {
        BigDecimal resultXMoney = new BigDecimal("0");
        BigDecimal tempXMoney = xMoney;
        xMoney = xMoney.abs();
        switch (roundMode) {
            case "0":
                //取整模式：四舍五入
                resultXMoney = doRoundTypeModeRoundHalf(xMoney, roundType);
                break;
            case "1":
                //取整模式：进一
                resultXMoney = doRoundTypeModeOne(xMoney, roundType);
                break;
            case "2":
                //取整模式：截尾
                resultXMoney = doRoundTypeModeTwo(xMoney, roundType);
                break;
        }
        if (tempXMoney.floatValue() < 0) {
            resultXMoney = resultXMoney.multiply(new BigDecimal("-1"));
        }
        return resultXMoney.setScale(2).toPlainString();
    }

    /**
     * 四舍五入
     * @param xMoney
     * @param roundType
     * @return
     */
    private BigDecimal doRoundTypeModeRoundHalf(BigDecimal xMoney, String roundType) {
        BigDecimal resultXMoney;
        switch (roundType) {
            case "0":
                //按元取整
                resultXMoney = xMoney.setScale(0, BigDecimal.ROUND_HALF_EVEN);
                break;
            case "1":
            case "2":
                //按5 / 10元取整
                resultXMoney = xMoney.setScale(-1, BigDecimal.ROUND_HALF_EVEN);
                break;
            case "3":
            case "4":
                //按50 / 100元取整
                resultXMoney = xMoney.setScale(-2, BigDecimal.ROUND_HALF_EVEN);
                break;
            case "5":
                resultXMoney = xMoney.setScale(1, BigDecimal.ROUND_HALF_EVEN);
                break;
            default:
                resultXMoney = xMoney;
                break;

        }
        return resultXMoney;
    }

    public BigDecimal doRoundTypeModeOne(BigDecimal xMoney, String roundType) {

        return new BigDecimal("0");
    }

    public BigDecimal doRoundTypeModeTwo(BigDecimal xMoney, String roundType) {

        return new BigDecimal("0");
    }


    public static void main(String[] args) {
        String xMoney = "172.45";
        String roundMode = "0";
        String roundType = "3";
        GreetingController controller = new GreetingController();
        System.out.println("取整前：[" + xMoney + "], 取整后：[" + controller.commonRound(xMoney, roundMode, roundType) + "]");
    }
}
