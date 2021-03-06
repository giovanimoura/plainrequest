package com.plainrequest.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.plainrequest.interfaces.OnInterceptRequest;
import com.plainrequest.interfaces.OnPlainRequest;
import com.plainrequest.model.Settings;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Classe da Request customizada
 *
 * @author Giovani Moura
 */
class RequestCustom<T> extends Request<T> {

    private final String TAG = "PLAINREQUEST";
    private Settings settings;
    private RequestParams requestParams;
    private Type superClass;
    private OnPlainRequest onPlainRequest;
    private int statusCode;
    private OnInterceptRequest onInterceptRequest;

    private final String PROTOCOL_CHARSET = "UTF-8";

    public RequestCustom(Settings settings, Type superClass, OnPlainRequest onPlainRequest, OnInterceptRequest onInterceptRequest) {
        super(settings.method, settings.url, null);
        this.settings = settings;
        this.requestParams = new RequestParams(settings.params, settings.contentTypeEnum, PROTOCOL_CHARSET);
        this.superClass = superClass;
        this.onPlainRequest = onPlainRequest;
        this.onInterceptRequest = onInterceptRequest;
        this.setShouldCache(settings.cacheEnable);

        // Define o timeout da request
        setRetryPolicy(new DefaultRetryPolicy((settings.timeOutSeconds * 1000), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyLog.setTag(TAG);
    }

    @Override
    protected void deliverResponse(T response) {
        this.onPlainRequest.onSuccess(response, statusCode);
    }

    @Override
    public void deliverError(VolleyError error) {
        statusCode = error.networkResponse != null ? error.networkResponse.statusCode : 0;

        String msgError = null;
        if (error instanceof NetworkError) {
            msgError = "Failed to connect to server";

        } else if (error instanceof TimeoutError) {
            msgError = "Timeout for connection exceeded";
        } else {
            if (error.networkResponse != null && error.networkResponse.data != null && !error.networkResponse.data.equals("")) {
                try {
                    msgError = new String(error.networkResponse.data, PROTOCOL_CHARSET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                msgError = error.getMessage();
            }
        }

        this.onPlainRequest.onError(error, msgError, statusCode);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", getContentType());

        if (onInterceptRequest != null) {
            onInterceptRequest.interceptHeader(headers);
        }

        if (!settings.mapHeaders.isEmpty()) {
            for (Map.Entry<String, String> entry : settings.mapHeaders.entrySet()) {
                headers.put(entry.getKey(), entry.getValue());
            }
        }

        return headers;
    }

    @Override
    public String getUrl() {
        return (super.getMethod() != Method.GET || requestParams.isNull()) ? super.getUrl() : getUrlGet();
    }

    /**
     * Retorna a url concatenada com os parametros na request do tipo GET e que possue parametros
     *
     * @return
     */
    private String getUrlGet() {
        try {
            return super.getUrl() + "?" + requestParams.getParamsQuery(); // concatena a url com os parametros
        } catch (Exception e) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", new Object[]{requestParams.getParamsObj(), PROTOCOL_CHARSET});
            return null;
        } finally {
            requestParams = null;
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return requestParams.isNull() ? null : requestParams.getParamsBody().getBytes(PROTOCOL_CHARSET);
        } catch (Exception e) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", new Object[]{requestParams.getParamsObj(), PROTOCOL_CHARSET});
            return null;
        } finally {
            requestParams = null;
        }
    }

    @Override
    public String getBodyContentType() {
        return getContentType();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            this.statusCode = response.statusCode;
            return getResponse(response);
        } catch (UnsupportedEncodingException e1) {
            return Response.error(new ParseError(e1));
        } catch (JSONException e2) {
            return Response.error(new ParseError(e2));
        }
    }

    /**
     * Retorna o Response conforme seu tipo
     *
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     * @throws JSONException
     */
    private Response getResponse(NetworkResponse response) throws UnsupportedEncodingException, JSONException {
        T result;

        byte[] data = response.data;

        if (isResponseCompressed(response)) {
            data = gzipToByte(data);
        }

        if (superClass.equals(JSONObject.class)) {
            result = (T) new JSONObject(convertData(data));
        } else if (superClass.equals(JSONArray.class)) {
            result = (T) new JSONArray(convertData(data));
        } else {
            result = (T) convertData(data);
        }

        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

    private String convertData(byte[] data) {
        String strResult;
        try {
            strResult = new String(data, PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException var4) {
            strResult = new String(data);
        }

        return strResult;
    }

//    private String getStrResult(byte[] data) throws UnsupportedEncodingException {
//        return new String(data, PROTOCOL_CHARSET);
//    }

    private boolean isResponseCompressed(NetworkResponse response) {
        return response.headers.containsKey("Content-Encoding") && response.headers.get("Content-Encoding").equalsIgnoreCase("gzip");
    }

    private byte[] gzipToByte(byte[] data) throws UnsupportedEncodingException {
        byte[] bytes;
        try {

            GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(data));
            bytes = IOUtils.toByteArray(gStream);

        } catch (IOException e) {
            VolleyLog.wtf("Unsupported Encoding GZIP");
            throw new UnsupportedEncodingException(e.getMessage());
        }

        return bytes;
    }

    private String getContentType() {
        return String.format(this.settings.contentTypeEnum.getValue() + "; charset=%s", new Object[]{PROTOCOL_CHARSET});
    }
}