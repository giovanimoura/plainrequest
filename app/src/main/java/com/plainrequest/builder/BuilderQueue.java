package com.plainrequest.builder;

import android.app.Application;

import com.plainrequest.PlainRequestQueue;

/**
 * @autor Giovani Moura
 */
public class BuilderQueue extends BuilderBase<BuilderQueue> {

    /**
     * Inicia a lib PlainRequest
     *
     * @param app
     */
    public void start(Application app) {
        PlainRequestQueue.getInstance().start(app);
    }

    /**
     * Define uma URL padrão para as request's
     *
     * @param urlDefault
     */
    public BuilderQueue urlDefault(String urlDefault) {
        this.settings.urlDefault = urlDefault;
        return this;
    }

    /**
     *
     * @param protocolGooglePlayService
     */
    public BuilderQueue protocolGooglePlayService(String protocolGooglePlayService) {
        this.settings.protocolGooglePlayService = protocolGooglePlayService;
        return this;
    }

    /**
     *
     * @param protocolSSLSocket
     */
    public BuilderQueue protocolSSLSocket(String protocolSSLSocket) {
        this.settings.protocolSSLSocket = protocolSSLSocket;
        return this;
    }

    /**
     * Define se o build da app é do tipo Release
     * Se for true, não serão registrados os log's
     * Ex.: .release(!BuildConfig.DEBUG)
     *
     *
     * @param buildRelease
     */
    public BuilderQueue release(boolean buildRelease) {
        this.settings.buildRelease = buildRelease;
        return this;
    }
}