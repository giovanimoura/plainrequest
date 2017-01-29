package com.plainrequest.request;

import android.util.Log;

import com.android.volley.VolleyError;
import com.plainrequest.PlainRequestQueue;
import com.plainrequest.interfaces.OnPlainRequest;
import com.plainrequest.model.Settings;
import com.plainrequest.util.SSLUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * Classe para executar a Request
 *
 * @author Giovani Moura
 */
public class RequestExecute implements OnPlainRequest {

    private final String TAG = "PLAINREQUEST";
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
    public <T> void execute(Settings settings) {
        this.settings = settings;
        print("Request: " + settings.url);

        // Executa o onPreExecute
        settings.requestCallback.onPreExecute();
        // Recupera a superClass para obter o tipo de retorno
        superClass = ((ParameterizedType) settings.requestCallback.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        if (settings.enableSSL)
            SSLUtil.updateSecurityProvider(PlainRequestQueue.getInstance().getContext(), settings);

        PlainRequestQueue plainRequestQueue = PlainRequestQueue.getInstance();

        // Criação da request
        RequestCustom request = new RequestCustom(settings, superClass, this, plainRequestQueue.getRequestIntercept());
        // Execução da request
        plainRequestQueue.getRequestQueue().add(request);
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

        settings.requestCallback.onSuccess(new ResponseConvert().convert(response, settings.nameFindJson, superClass), statusCode);
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

        settings.requestCallback.onError(error, msgError, statusCode);
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