package com.plainrequest;

import com.android.volley.VolleyError;

/**
 * Classe para eventos de callback do PlainRequest
 *
 * @author Giovani Moura
 */
public abstract class RequestCallback<T> {

    /**
     * Método executado antes da request
     * OBS.: Aqui deve ser iniciado um ProgressDialog
     *
     * @throws Exception
     */
    public void onPreExecute() {
    }

    /**
     * Método executado após sucesso no retorno da request
     *
     * @param response   String de resposta
     * @param statusCode StatusCode da resposta
     */
    public abstract void onSuccess(T response, int statusCode);

    /**
     * Método executado em casos de falha na request
     *
     * @param error      Informações da falha
     * @param msgError   Mensagem do erro
     * @param statusCode StatusCode do erro
     */
    public abstract void onError(VolleyError error, String msgError, int statusCode);
}
