package com.plainrequest.request;

import com.plainrequest.enums.ContentTypeEnum;
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
    private ContentTypeEnum contentTypeEnum;
    private String paramsEncoding; // Tipo de enconding
    private boolean paramNull;

    public RequestParams(Object paramsObj, ContentTypeEnum contentTypeEnum, String paramsEncoding) {
        this.paramsObj = paramsObj;
        this.contentTypeEnum = contentTypeEnum;
        this.paramsEncoding = paramsEncoding;
    }

    /**
     * Retorna os parametros do QueryParam
     *
     * @return
     */
    public String getParamsQuery() {
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
            map = null;
            return encodedParams.toString();
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, var6);
        } finally {
            encodedParams = null;
            paramNull = paramsObj == null;
            paramsObj = null;
            System.gc();
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
            if (contentTypeEnum == ContentTypeEnum.APPLICATION_JSON) {
                if (paramsObj instanceof String || paramsObj instanceof JSONObject || paramsObj instanceof JSONArray) {
                    params = paramsObj.toString();
                } else if (paramsObj instanceof List || paramsObj instanceof ArrayList || paramsObj instanceof LinkedList || paramsObj instanceof Collection) {
                    params = JsonUtil.listToJson((List) paramsObj);
                } else {
                    params = JsonUtil.objToJson(paramsObj);
                }

            } else if (contentTypeEnum == ContentTypeEnum.APPLICATION_FROM_URLENCODED) {
                params = getParamsQuery();
            }
        }
        return params;
    }

    public Object getParamsObj() {
        return paramsObj;
    }

    public boolean isNull() {
        return paramNull;
    }
}
