package com.plainrequest;

import com.plainrequest.interfaces.OnResultRequest;

/**
 * Classe para eventos de callback do PlainRequest
 *
 * @author Giovani Moura
 */
public abstract class ResultRequest<T> implements OnResultRequest<T> {

    @Override
    public void onPreExecute() {

    }
}