package io.renren.modules.amazon.dto;

public class MoneyDto {
    /**
     * 三位数的货币代码。格式为 ISO 4217。
     */
    private String currencyCode;
    /**
     * 货币金额。
     */
    private String amount;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MoneyDto{" +
                "currencyCode='" + currencyCode + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
