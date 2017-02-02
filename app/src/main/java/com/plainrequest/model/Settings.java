package com.plainrequest.model;

import com.plainrequest.RequestCallback;

import java.util.Map;

/**
 *
 *
 * @autor Giovani Moura
 */
public class Settings {

    // Settings BuilderQueue and BuilderRequest
    public Map<String, String> mapHeaders;
    public int timeOutSeconds;
    public String dateFormat;
    public String dateFormatSerializer;
    public String dateFormatDeserializer;
    public String tagName;
    public boolean enableSSL;

    // Settings BuilderRequest
    public String url;
    public int method;
    public Object params;
    public RequestCallback requestCallback;
    public boolean clearUrlDefault;
    public String nameFindJson;

    // Settings BuilderQueue
    public String urlDefault;
    public boolean buildRelease;
    public String protocolGooglePlayServices;
    public String protocolSSLSocket;

    public Settings() {
        this.timeOutSeconds = 10; // 10 seconds of timeout
        this.enableSSL = true;
        this.protocolGooglePlayServices = "TLSv1.2";
        this.protocolSSLSocket = "TLS";
    }

    public void copy(Settings settings) {
        this.mapHeaders = settings.mapHeaders;
        this.timeOutSeconds = settings.timeOutSeconds;
        this.dateFormat = settings.dateFormat;
        this.dateFormatSerializer = settings.dateFormatSerializer;
        this.dateFormatDeserializer = settings.dateFormatDeserializer;
        this.tagName = settings.tagName;
        this.enableSSL = settings.enableSSL;
    }
}