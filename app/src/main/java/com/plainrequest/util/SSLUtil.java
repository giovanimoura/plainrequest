package com.plainrequest.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.plainrequest.model.Settings;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author Giovani Moura
 */
public class SSLUtil {

    public static void updateSecurityProvider(Context context, Settings settings) {
        try {
            SSLContext.getInstance(settings.protocolGooglePlayServices);
            ProviderInstaller.installIfNeeded(context);

        } catch (GooglePlayServicesRepairableException e) {
            // Mensagem informando que esta desatualizado o Google Play Service
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            int result = googleAPI.isGooglePlayServicesAvailable(context);
            if(result != ConnectionResult.SUCCESS) {
                if(googleAPI.isUserResolvableError(result)) {
                    googleAPI.getErrorDialog((Activity) context, result, 9000).show();
                }
            }
        } catch (GooglePlayServicesNotAvailableException e) {
            try {
                // Se no android não existir o serviço Google Play Service, altera o protocolo do SSLSocketFactory, default TSL
                HttpsURLConnection.setDefaultSSLSocketFactory(new SSLSocketCustomFactory(settings.protocolSSLSocket));

            } catch (Exception e1) {
                Log.e("SecurityException", "Google Play Services not available.");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    static class SSLSocketCustomFactory extends SSLSocketFactory {

        private SSLSocketFactory internalSSLSocketFactory;

        public SSLSocketCustomFactory(String protocol) throws KeyManagementException, NoSuchAlgorithmException {
            SSLContext context = SSLContext.getInstance(protocol);
            context.init(null, null, null);
            internalSSLSocketFactory = context.getSocketFactory();
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return internalSSLSocketFactory.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return internalSSLSocketFactory.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return enableTLSOnSocket(internalSSLSocketFactory.createSocket(address, port, localAddress, localPort));
        }

        private Socket enableTLSOnSocket(Socket socket) {
            if(socket != null && (socket instanceof SSLSocket)) {
                ((SSLSocket)socket).setEnabledProtocols(new String[] {"TLSv1.1", "TLSv1.2"});
            }
            return socket;
        }
    }
}