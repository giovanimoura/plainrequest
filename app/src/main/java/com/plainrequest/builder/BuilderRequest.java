package com.plainrequest.builder;

import com.plainrequest.PlainRequestQueue;
import com.plainrequest.ResultRequest;
import com.plainrequest.interfaces.OnResultRequest;
import com.plainrequest.request.RequestExecute;

import java.util.HashMap;
import java.util.Map;

/**
 * @autor Giovani Moura
 */
public class BuilderRequest extends BuilderBase<BuilderRequest> {

    protected Map<String, Object> mapParams;
    private RequestExecute requestExecute;

    public BuilderRequest(String url, int method) {
        super(PlainRequestQueue.builder().settings);

        this.settings.url = url;
        this.settings.method = method;
        this.mapParams = new HashMap<>();
        this.requestExecute = new RequestExecute();
    }

    /**
     * Executa a request
     */
    public void request() {
        requestExecute.execute(settings);
    }

    /**
     * Resultado da request, com retorno generico no response do onSuccess
     *
     * onPreExecute - Executado antes da request
     * onSuccess    - Executado após sucesso no retorno da request
     * onError      - Executado em casos de exception da request
     *
     * @param resultRequest
     * @param <T>
     */
    public <T> BuilderRequest result(ResultRequest<T> resultRequest) {
        settings.onResultRequest = resultRequest;
        return this;
    }

    /**
     * Resultado da request, com retorno generico no response do onSuccess
     *
     * onPreExecute - Executado antes da request
     * onSuccess    - Executado após sucesso no retorno da request
     * onError      - Executado em casos de exception da request
     *
     * @param onResultRequest
     * @param <T>
     * @return
     */
    public <T> BuilderRequest result(OnResultRequest<T> onResultRequest) {
        settings.onResultRequest = onResultRequest;
        return this;
    }

    /**
     * Limpar dados da urlDefault na atual request
     */
    public BuilderRequest clearUrlDefault() {
        settings.clearUrlDefault = true;
        return this;
    }

    /**
     * Define um campo para ser localizado e desserializado no Json
     * Será aplicado no response
     *
     * @param nameFindJson
     */
    public BuilderRequest findJson(String nameFindJson) {
        settings.nameFindJson = nameFindJson;
        return this;
    }
}