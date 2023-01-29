package io.pw.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class UriQueryParser {
    public List<UriParam> parse(URI uri) {
        List<UriParam> params = new ArrayList<>();

        String[] stringParams = uri.getQuery().split("&");

        for (String stringParam : stringParams) {
            String[] paramAndValue = stringParam.split("=");
            params.add(new UriParam(paramAndValue[0], paramAndValue[1]));
        }

        return params;
    }
}
