package com.plainrequest.request;

import com.plainrequest.util.JsonUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe que customiza os parametros para Body ou QueryParam
 *
 * @author Giovani Moura
 */
class RequestParams {

    private Object paramsObj;
    private Gson gson;

    public RequestParams(Object paramsObj) {
        this.paramsObj = paramsObj;
        this.gson = new Gson();
    }

    /**
     * Retorna os parametros do QueryParam
     *
     * @param paramsEncoding Tipo de enconding
     * @return
     */
    public String getParamsQuery(String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();

        try {
            Map<String, String> map = (Map<String, String>) paramsObj;
            Iterator uee = map.entrySet().iterator();
            String sep = "";
            while(uee.hasNext()) {
                Map.Entry entry = (Map.Entry)uee.next();
                encodedParams.append(sep);
                encodedParams.append(URLEncoder.encode((String)entry.getKey(), paramsEncoding));
                encodedParams.append("=");
                encodedParams.append(URLEncoder.encode((String)entry.getValue(), paramsEncoding));
                sep = "&";
            }

            return encodedParams.toString();
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, var6);
        }
    }

    /**
     * Retorna os parametros do Body
     *
     * @return
     * @throws Exception
     */
    public String getParamsBody() throws Exception {
        String paramsStr = null;

        if (paramsObj != null) {
            if (paramsObj instanceof String || paramsObj instanceof JSONObject || paramsObj instanceof JSONArray) {
                paramsStr = paramsObj.toString();
            } else if (paramsObj instanceof List || paramsObj instanceof ArrayList || paramsObj instanceof LinkedList || paramsObj instanceof Collection) {
                paramsStr = JsonUtil.listToJson((List) paramsObj);
            } else {
                paramsStr = JsonUtil.objToJson(paramsObj);
            }
        }

        return paramsStr;
    }

    public Object getParamsObj() {
        return paramsObj;
    }

    public boolean isNull() {
        return paramsObj == null;
    }
}
