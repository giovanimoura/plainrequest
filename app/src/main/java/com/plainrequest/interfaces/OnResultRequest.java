package com.plainrequest.interfaces;

import com.android.volley.VolleyError;

/**
 * Inteface para eventos de callback do PlainRequest
 *
 * @author Giovani Moura
 */
public interface OnResultRequest<T> {

    /**
     * Executado antes da request
     * OBS.: Aqui deve ser iniciado um ProgressDialog
     */
    void onPreExecute();

    /**
     * Executado após sucesso no retorno da request
     *
     * @param response
     * @param statusCode
     */
    void onSuccess(T response, int statusCode);

    /**
     * Executado em casos de falha na request
     *
     * @param error
     * @param msgError
     * @param statusCode
     */
    void onError(VolleyError error, String msgError, int statusCode);
}