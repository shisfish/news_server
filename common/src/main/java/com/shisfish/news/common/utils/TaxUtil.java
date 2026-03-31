package com.shisfish.news.common.utils;

import java.math.BigDecimal;

/**
 * @author shisfish
 * @date 2023/9/18
 * @Description 税率工具
 */
public class TaxUtil {

    /**
     * 税额 = 含税总额 / (1 + 税率) * 税率
     * 如果是负数金额，先转成正数计算税金再取反
     *
     * @param charge
     * @param commission
     * @return
     */
    public static BigDecimal calcChargeTax(BigDecimal charge, BigDecimal commission) {
        BigDecimal tax;
        if (charge.compareTo(BigDecimal.ZERO) >= 0) {
            tax = charge
                    .multiply(commission)
                    .divide(BigDecimal.ONE.add(commission), 2, BigDecimal.ROUND_HALF_EVEN);
        } else {
            BigDecimal fooch = BigDecimal.ZERO.subtract(charge);
            BigDecimal ftax = fooch
                    .multiply(commission)
                    .divide(BigDecimal.ONE.add(commission), 2, BigDecimal.ROUND_HALF_EVEN);
            tax = BigDecimal.ZERO.subtract(ftax);
        }
        return tax;
    }

}
