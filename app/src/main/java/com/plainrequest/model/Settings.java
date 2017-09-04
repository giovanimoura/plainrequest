package com.plainrequest.model;

import com.plainrequest.enums.ContentTypeEnum;
import com.plainrequest.interfaces.OnResultRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @autor Giovani Moura
 */
public class Settings implements Cloneable {

    // Settings BuilderQueue and BuilderRequest
    public Map<String, String> mapHeaders;
    public int timeOutSeconds;
    public String tagName;
    public boolean enableSSL;

    // Settings BuilderRequest
    public String url;
    public int method;
    public Object params;
    public OnResultRequest onResultRequest;
    public boolean clearUrlDefault;
    public String nameFindJson;

    // Settings BuilderQueue
    public String urlDefault;
    public boolean buildRelease;
    public boolean cacheEnable;
    public int sizeCache;
    public ContentTypeEnum contentTypeEnum;
    public String tlsVersion;

    public Settings() {
        this.mapHeaders = new HashMap<>();
        this.timeOutSeconds = 10; // 10 seconds of timeout
        this.enableSSL = true;
        this.contentTypeEnum = ContentTypeEnum.APPLICATION_JSON;
        this.sizeCache = 2; // 2MB off cache
        this.tlsVersion = "TLSv1.2";
    }

    @Override
    public Settings clone()  {
        Settings settings = new Settings();
        try {
            settings = (Settings) super.clone();
            settings.mapHeaders = new HashMap<>(mapHeaders);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return settings;
    }
}