package com.plainrequest.interfaces;

import com.android.volley.VolleyError;

/**
 * Inteface para eventos de callback do RequestCustom
 *
 * @author Giovani Moura
 */
public interface OnPlainRequest {

    /**
     * Método executado após a request, quando obter sucesso no retorno
     *
     * @param response   String de resposta
     * @param statusCode Codigo de resposta
     */
    <T> void onSuccess(T response, int statusCode);

    /**
     * Método executado em casos de falha na request
     *
     * @param error      Informações da falha
     * @param msgError   Mensagem do erro
     * @param statusCode Codigo de resposta
     */
    void onError(VolleyError error, String msgError, int statusCode);
}
