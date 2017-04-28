package com.plainrequest.builder;

import com.plainrequest.model.Settings;

/**
 * @autor Giovani Moura
 */
class BuilderBase<T extends BuilderBase> {

    protected Settings settings;

    BuilderBase() {
        this.settings = new Settings();
    }

    BuilderBase(Settings settings) {
        this.settings = settings.clone(); // clona o objeto settings
    }

    /**
     * Adiciona parâmetros no header da request
     *
     * @param key
     * @param value
     */
    public T header(String key, String value) {
        settings.mapHeaders.put(key, value);
        return (T) this;
    }

    /**
     * Define o timeOut da request em segundos
     * Default value 10 seconds
     *
     * @param timeOut
     */
    public T timeOutSeconds(int timeOut) {
        settings.timeOutSeconds = timeOut;
        return (T) this;
    }

    /**
     * Define uma tag para a request
     *
     * @param tagName
     */
    public T tagRequest(String tagName) {
        settings.tagName = tagName;
        return (T) this;
    }

    /**
     * Ativar/Desativar o SSL
     * Default value true
     *
     * @param enableSSL
     */
    public T SSLSecurity(boolean enableSSL) {
        settings.enableSSL = enableSSL;
        return (T) this;
    }
}