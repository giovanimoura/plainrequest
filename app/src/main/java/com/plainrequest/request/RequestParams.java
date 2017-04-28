package com.plainrequest.request;

import android.util.Log;

import com.plainrequest.util.JsonUtil;

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

    private final String TAG = "PLAINREQUEST";
    private Object paramsObj;

    public RequestParams(Object paramsObj) {
        this.paramsObj = paramsObj;
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

            Log.i(TAG, "Params: " + encodedParams.toString());
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
        String params = null;

        if (paramsObj != null) {
            if (paramsObj instanceof String || paramsObj instanceof JSONObject || paramsObj instanceof JSONArray) {
                params = paramsObj.toString();
            } else if (paramsObj instanceof List || paramsObj instanceof ArrayList || paramsObj instanceof LinkedList || paramsObj instanceof Collection) {
                params = JsonUtil.listToJson((List) paramsObj);
            } else {
                params = JsonUtil.objToJson(paramsObj);
            }
        }
        Log.i(TAG, "Params: " + params);
        return params;
    }

    public Object getParamsObj() {
        return paramsObj;
    }

    public boolean isNull() {
        return paramsObj == null;
    }
}
