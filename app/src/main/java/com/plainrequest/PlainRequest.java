package com.plainrequest;

import com.android.volley.Request;
import com.plainrequest.builder.BuilderRequest;

/**
 * Classe para definição e execução da resquest dos tipos GET, POST, DELETE ou PUT
 *
 * @author Giovani Moura
 */
public class PlainRequest {

    private PlainRequest() {
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Cancela a request conforme tagName
     *
     * @param tagName
     */
    public static void cancel(String tagName) {
        PlainRequestQueue.getInstance().getRequestQueue().cancelAll(tagName);
    }

    public static class Builder {

        /**
         * Define uma request do tipo GET
         * URL será concatenada com urlDefault
         *
         * @param url
         */
        public BuilderGet get(String url) {
            return new BuilderGet(url, Request.Method.GET);
        }

        /**
         * Define uma request do tipo POST
         * URL será concatenada com urlDefault
         *
         * @param url
         */
        public BuilderPost post(String url) {
            return new BuilderPost(url, Request.Method.POST);
        }

        /**
         * Define uma request do tipo DELETE
         * URL será concatenada com urlDefault
         *
         * @param url
         */
        public BuilderPost delete(String url) {
            return new BuilderPost(url, Request.Method.DELETE);
        }

        /**
         * Define uma request do tipo PUT
         * URL será concatenada com urlDefault
         *
         * @param url
         */
        public BuilderPost put(String url) {
            return new BuilderPost(url, Request.Method.PUT);
        }

        public class BuilderGet extends BuilderRequest {

            public BuilderGet(String url, int method) {
                super(url, method);
            }

            /**
             * Adiciona parâmetros para a request do tipo GET
             *
             * @param key
             * @param value
             */
            public BuilderGet param(String key, String value) {
                mapParams.put(key, value);
                settings.params = mapParams;
                return this;
            }
        }

        public class BuilderPost extends BuilderRequest {

            public BuilderPost(String url, int method) {
                super(url, method);
            }

            /**
             * Adiciona parâmetros para a request do tipo POST
             * Ex.: .param(cliente) - o parâmetro cliente será convertido para Json e adicionado no body da request
             *
             * @param param Entidade que será convertido para Json
             */
            public BuilderPost param(Object param) {
                settings.params = param;
                return this;
            }

            /**
             * Adiciona parâmetros para a request do tipo POST
             *
             * @param key
             * @param value
             */
            public BuilderPost param(String key, Object value) {
                mapParams.put(key, value);
                settings.params = mapParams;
                return this;
            }
        }
    }
}