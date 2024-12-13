package kpi.fict.prist.core.common;

public enum StripeCurrency {
    USD("usd"),
    EUR("eur"),
    UAH("uah");

    private final String code;

    StripeCurrency(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}