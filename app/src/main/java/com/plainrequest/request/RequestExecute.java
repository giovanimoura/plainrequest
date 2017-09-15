package com.plainrequest.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.plainrequest.PlainRequestQueue;
import com.plainrequest.interfaces.OnPlainRequest;
import com.plainrequest.model.Settings;
import com.plainrequest.util.SSLUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Classe para executar a Request
 *
 * @author Giovani Moura
 */
public class RequestExecute implements OnPlainRequest {

    private String tagNameRequest;
    private String TAG = "PLAINREQUEST";
    private Settings settings;
    private Type superClass;
    private long timeRequest;

    public RequestExecute() {
        this.timeRequest = System.currentTimeMillis();
    }

    /**
     * Executa a request
     *
     * @param settings
     * @param <T>
     */
    public <T> void execute(final Settings settings) {
        this.settings = settings;

        if (!settings.clearUrlDefault)
            settings.url = settings.urlDefault + settings.url; // Concatena a url

        print("Request: " + settings.url);

        // Executa o onPreExecute
        settings.onResultRequest.onPreExecute();
        // Recupera a superClass para obter o tipo de retorno
        if (settings.onResultRequest.getClass().getGenericInterfaces().length > 0)
            superClass = ((ParameterizedType) settings.onResultRequest.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        else
            superClass = ((ParameterizedType) settings.onResultRequest.getClass().getGenericSuperclass()).getActualTypeArguments()[0];


        PlainRequestQueue plainRequestQueue = PlainRequestQueue.getInstance();

        if (settings.enableSSL)
            SSLUtil.updateSecurityProvider(plainRequestQueue.getContext(), settings.tlsVersion);

        // Criação da request
        RequestCustom request = new RequestCustom(settings, superClass, this, plainRequestQueue.getRequestIntercept());

        defineTagNameRequest(plainRequestQueue);
        request.setTag(this.tagNameRequest); // Define tag para a request

        if (settings.cacheEnable)
            plainRequestQueue.getRequestQueue().start(); // para cache

        plainRequestQueue.getRequestQueue().add(request);
        plainRequestQueue.getRequestQueue().addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                PlainRequestQueue plainRequestQueue = PlainRequestQueue.getInstance();

                if (settings.cacheEnable && plainRequestQueue.getListRequests().size() <= 0)
                    plainRequestQueue.getRequestQueue().stop(); // para cache
            }
        });
    }

    private void defineTagNameRequest(PlainRequestQueue plainRequestQueue) {
        List<String> requests = plainRequestQueue.getListRequests();
        this.tagNameRequest = "REQ_PLAIN_" + (requests.size() + 1);
        requests.add(this.tagNameRequest);

        if (settings.tagName != null && !settings.tagName.isEmpty()) {
            this.tagNameRequest = settings.tagName;
        }
    }

    /**
     * Retorno de sucesso do RequestCustom
     *
     * @param response   String de resposta
     * @param statusCode Codigo de resposta
     */
    @Override
    public <T> void onSuccess(T response, int statusCode) {
        timeRequest();
        print("StatusCode: " + statusCode);
        print("Response:" + response.toString());

        removeListRequest();

        settings.onResultRequest.onSuccess(new ResponseConvert().convert(response, settings, superClass), statusCode);
    }

    /**
     * Retorno da falha do RequestCustom
     *
     * @param error      Informações da falha
     * @param msgError   Mensagem do erro
     * @param statusCode Codigo de resposta
     */
    @Override
    public void onError(VolleyError error, String msgError, int statusCode) {
        timeRequest();
        print("StatusCode: " + statusCode);

        if (!settings.buildRelease)
            Log.e(TAG, "Error: " + msgError);

        removeListRequest();

        settings.onResultRequest.onError(error, msgError, statusCode);
    }

    private void removeListRequest() {
        PlainRequestQueue.getInstance().getListRequests().remove(this.tagNameRequest);
    }

    private void timeRequest() {
        timeRequest = System.currentTimeMillis() - timeRequest;
        String time = String.format("%dm:%ds:%d",
                TimeUnit.MILLISECONDS.toSeconds(timeRequest) / 60,
                TimeUnit.MILLISECONDS.toSeconds(timeRequest) % 60,
                timeRequest -= TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(timeRequest) % 60));

        print("Time Request: " + time);
    }

    private void print(String message) {
        if (!settings.buildRelease)
            Log.i(TAG, message);
    }
}