package com.plainrequest.interfaces;

import java.util.Map;

/**
 * @author Giovani Moura
 */
public interface OnInterceptRequest {

    Map<String, String> interceptHeader(Map<String, String> mapHeaders);
}