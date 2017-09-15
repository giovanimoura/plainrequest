package com.plainrequest;

import android.app.Application;
import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.plainrequest.builder.BuilderQueue;
import com.plainrequest.interfaces.OnInterceptRequest;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> listRequests;

    private PlainRequestQueue() {
        this.builderQueue = new BuilderQueue();
        this.listRequests = new ArrayList<>();
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

    /**
     * Inicia uma instancia da lib PlainRequest
     * utilizando cache do volley
     *
     * @param app
     * @param sizeCache // tamanho do cache em MB
     */
    public void start(Application app, int sizeCache) {
        if(context == null) {
            context = app.getApplicationContext();
            // Cache
            Cache cache = new DiskBasedCache(app.getCacheDir(), (1024 * 1024) * sizeCache);
            Network network = new BasicNetwork(new HurlStack());
            queue = new RequestQueue(cache, network); // Criação do RequestQueue
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

    public List<String> getListRequests() {
        return listRequests;
    }

    public OnInterceptRequest getRequestIntercept() {
        return context instanceof OnInterceptRequest ? (OnInterceptRequest) context : null;
    }
}