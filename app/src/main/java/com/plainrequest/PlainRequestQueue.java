package com.plainrequest;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.plainrequest.builder.BuilderQueue;
import com.plainrequest.interfaces.OnInterceptRequest;

/**
 * Classe para instanciar a RequestQueue
 *
 * OBS.: Obrigatório executar o método start na classe Application no método onCreate
 *
 * @author Giovani Moura
 */
public class PlainRequestQueue {

    private static PlainRequestQueue plainRequestQueue;
    private Context context;
    private RequestQueue queue;
    private BuilderQueue builderQueue;

    private PlainRequestQueue() {
        this.builderQueue = new BuilderQueue();
    }

    public static BuilderQueue builder() {
        return getInstance().builderQueue;
    }

    public static synchronized PlainRequestQueue getInstance() {
        if (plainRequestQueue == null)
            plainRequestQueue = new PlainRequestQueue();

        return plainRequestQueue;
    }

    /**
     * Inicia uma instancia da lib PlainRequest
     *
     * @param app
     */
    public void start(Application app) {
        if(context == null) {
            context = app.getApplicationContext();
            queue = Volley.newRequestQueue(context); // Criação do RequestQueue
        }
    }

    public RequestQueue getRequestQueue() {
        if (queue == null)
            queue = Volley.newRequestQueue(context);

        return queue;
    }

    public Context getContext() {
        return context;
    }

    public OnInterceptRequest getRequestIntercept() {
        return context instanceof OnInterceptRequest ? (OnInterceptRequest) context : null;
    }
}