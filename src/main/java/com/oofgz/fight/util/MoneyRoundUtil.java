package com.oofgz.fight.util;

import java.math.BigDecimal;

public class MoneyRoundUtil {

    // --------------取整需要的常量 BEGIN-----------------//
    public static final BigDecimal C_FIVE = new BigDecimal("5");
    public static final BigDecimal C_TEN = new BigDecimal("10");
    public static final BigDecimal C_FIFTY = new BigDecimal("50");
    public static final BigDecimal C_HUNDRED = new BigDecimal("100");
    public static final BigDecimal C_POINT_ONE = new BigDecimal("0.1");
    public static final BigDecimal C_NEGATIVE_ONE = new BigDecimal("-1");
    public static final BigDecimal C_INIT_ZERO = new BigDecimal("0");
    // --------------取整需要的常量 END-----------------//


    public static String commonRoundMoney(String xMoney,
                                          String roundMode,
                                          String roundType) {
        if (null == xMoney || "".equals(xMoney.trim())) {
            xMoney = "0";
        }
        return commonRoundMoney(new BigDecimal(xMoney), roundMode, roundType);
    }
    /**
     * 根据取整方式和取整模式进行取整，返回取整后的金额
     *
     * @param xMoney
     * @param roundMode
     * @param roundType
     * @return
     */
    public static String commonRoundMoney(BigDecimal xMoney,
                                          String roundMode,
                                          String roundType) {
        BigDecimal tempXMoney = xMoney;
        BigDecimal resultXMoney;
        xMoney = xMoney.abs();
        switch (roundMode) {
            case "0":
                //取整模式：四舍五入
                resultXMoney = doRoundTypeModeRounding(xMoney, roundType);
                break;
            case "1":
                //取整模式：进一
                resultXMoney = doRoundTypeModeFurther(xMoney, roundType);
                break;
            case "2":
                //取整模式：截尾
                resultXMoney = doRoundTypeModeTruncation(xMoney, roundType);
                break;
            default:
                resultXMoney = xMoney;
                break;
        }
        if (tempXMoney.compareTo(C_INIT_ZERO) < 0) {
            resultXMoney = resultXMoney.multiply(C_NEGATIVE_ONE);
        }
        return resultXMoney.setScale(2).toPlainString();
    }

    /**
     * 四舍五入
     *
     * @param xMoney
     * @param roundType
     * @return
     */
    private static BigDecimal doRoundTypeModeRounding(BigDecimal xMoney, String roundType) {
        BigDecimal resultXMoney;
        switch (roundType) {
            case "0":
                //按元取整
                resultXMoney = xMoney.setScale(0, BigDecimal.ROUND_HALF_UP);
                break;
            case "1":
            case "2":
                //按5 / 10元取整
                resultXMoney = xMoney.setScale(-1, BigDecimal.ROUND_HALF_UP);
                break;
            case "3":
            case "4":
                //按50 / 100元取整
                resultXMoney = xMoney.setScale(-2, BigDecimal.ROUND_HALF_UP);
                break;
            case "5":
                //按角取整
                resultXMoney = xMoney.setScale(1, BigDecimal.ROUND_HALF_UP);
                break;
            default:
                resultXMoney = xMoney;
                break;

        }
        return resultXMoney;
    }

    /**
     * 进一
     * @param xMoney
     * @param roundType
     * @return
     */
    private static BigDecimal doRoundTypeModeFurther(BigDecimal xMoney, String roundType) {
        BigDecimal resultXMoney;
        switch (roundType) {
            case "0":
                //按元进一
                resultXMoney = xMoney.setScale(0, BigDecimal.ROUND_CEILING);
                break;
            case "1":
                //按5元进一
                xMoney = xMoney.divide(C_FIVE, 0, BigDecimal.ROUND_CEILING);
                resultXMoney = xMoney.multiply(C_FIVE);
                break;
            case "2":
                //按10元进一
                xMoney = xMoney.divide(C_TEN, 0, BigDecimal.ROUND_CEILING);
                resultXMoney = xMoney.multiply(C_TEN);
                break;
            case "3":
                //按50元进一
                xMoney = xMoney.divide(C_FIFTY, 0, BigDecimal.ROUND_CEILING);
                resultXMoney = xMoney.multiply(C_FIFTY);
                break;
            case "4":
                //按100元进一
                xMoney = xMoney.divide(C_HUNDRED, 0, BigDecimal.ROUND_CEILING);
                resultXMoney = xMoney.multiply(C_HUNDRED);
                break;
            case "5":
                //按角进一
                xMoney = xMoney.divide(C_POINT_ONE, 0, BigDecimal.ROUND_CEILING);
                resultXMoney = xMoney.multiply(C_POINT_ONE);
                break;
            default:
                resultXMoney = xMoney;
                break;

        }
        return resultXMoney;
    }

    /**
     * 截尾
     * @param xMoney
     * @param roundType
     * @return
     */
    private static BigDecimal doRoundTypeModeTruncation(BigDecimal xMoney, String roundType) {
        BigDecimal resultXMoney;
        switch (roundType) {
            case "0":
                //按元截尾
                resultXMoney = xMoney.setScale(0, BigDecimal.ROUND_FLOOR);
                break;
            case "1":
                //按5元截尾
                xMoney = xMoney.divide(C_FIVE, 0, BigDecimal.ROUND_FLOOR);
                resultXMoney = xMoney.multiply(C_FIVE);
                break;
            case "2":
                //按10元截尾
                xMoney = xMoney.divide(C_TEN, 0, BigDecimal.ROUND_FLOOR);
                resultXMoney = xMoney.multiply(C_TEN);
                break;
            case "3":
                //按50元截尾
                xMoney = xMoney.divide(C_FIFTY, 0, BigDecimal.ROUND_FLOOR);
                resultXMoney = xMoney.multiply(C_FIFTY);
                break;
            case "4":
                //按100元截尾
                xMoney = xMoney.divide(C_HUNDRED, 0, BigDecimal.ROUND_FLOOR);
                resultXMoney = xMoney.multiply(C_HUNDRED);
                break;
            case "5":
                //按角截尾
                xMoney = xMoney.divide(C_POINT_ONE, 0, BigDecimal.ROUND_FLOOR);
                resultXMoney = xMoney.multiply(C_POINT_ONE);
                break;
            default:
                resultXMoney = xMoney;
                break;

        }
        return resultXMoney;
    }


}
