package br.org.venturus.venturus4tech;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by vntadra on 05/02/18.
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
            mSocket = IO.socket("http://localhost:3000/");
        } catch (URISyntaxException e) {
            mSocket = null;
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

}








