package com.plainrequest;

import com.plainrequest.interfaces.OnRequestCallback;

/**
 * Classe para eventos de callback do PlainRequest
 *
 * @author Giovani Moura
 */
public abstract class RequestCallback<T> implements OnRequestCallback<T> {

    @Override
    public void onPreExecute() {

    }
}