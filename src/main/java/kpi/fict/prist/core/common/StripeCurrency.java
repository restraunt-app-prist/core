package kpi.fict.prist.core.common;

import lombok.Getter;

@Getter
public enum StripeCurrency {
    USD("usd"),
    EUR("eur"),
    UAH("uah");

    private final String code;

    StripeCurrency(String code) {
        this.code = code;
    }

}