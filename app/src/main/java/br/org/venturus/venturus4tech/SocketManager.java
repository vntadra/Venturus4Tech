package br.org.venturus.venturus4tech;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by vntguca on 13/07/17.
 */

public class SocketManager {

    private Socket mSocket = null;

    private static SocketManager mInstance;

    public static SocketManager getInstance() {
        if (mInstance == null) {
            mInstance = new SocketManager();
        }
        return mInstance;
    }

    private SocketManager() {
        try {
            mSocket = IO.socket("http://192.168.2.117:3000");
        } catch (URISyntaxException e) {
            mSocket = null;
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

}








