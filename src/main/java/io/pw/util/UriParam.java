package io.pw.util;

public class UriParam {
    final private String param;
    final private String value;

    UriParam(String param, String value) {
        this.param = param;
        this.value = value;
    }

    public String getParam() {
        return param;
    }

    public String getValue() {
        return value;
    }
}
