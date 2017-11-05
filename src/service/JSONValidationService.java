package service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class JSONValidationService {
    /* should be in [class JVSConfiguration] */
    public static int SERVER_PORT = 8080;
    public static String SERVER_HOST = null; // loopback
    public static int SERVER_BACKLOG = 8;
    public static String SERVER_PATH = "/";
    public static int SERVER_DELAY = 10;

    private HttpServer mServer = null;

    public JSONValidationService() throws JVSException {
        InetAddress address = null;
        InetSocketAddress socket = null;

        try {
            mServer = HttpServer.create();
        } catch (IOException e) {
            error("Server could not be created");
        }

        try {
            address = InetAddress.getByName(SERVER_HOST);
        } catch (UnknownHostException e) {
            error("Could not determine the IP of a host for <%s>", SERVER_HOST);
        } catch (SecurityException e) {
            error("Operation is not allowed");
        }

        try {
            socket = new InetSocketAddress(address, SERVER_PORT);
        } catch (IllegalArgumentException e) {
            error("Port is outside of the range of valid port values: %d", SERVER_PORT);
        } catch (SecurityException e) {
            error("Operation is not allowed");
        }

        try {
            mServer.bind(socket, SERVER_BACKLOG);
        } catch (BindException e) {
            error("Server cannot bind to the requested address: <%s>", SERVER_HOST);
        } catch (IOException e) {
            error("Bind failed");
        }

        try {
            mServer.createContext(SERVER_PATH, new JSONHandler());
        } catch (IllegalArgumentException e) {
            error("Path <%s> is invalid", SERVER_PATH);
        }

        System.out.format("Server has been created:\nhost:port=<%s:%d>, backlog=%d, path=%s, delay=%d\n",
                (null == SERVER_HOST) ? "localhost" : SERVER_HOST, SERVER_PORT,
                SERVER_BACKLOG, SERVER_PATH, SERVER_DELAY);
    }

    private void error(String msg, Object... args) throws JVSException {
        System.err.format(msg + "\n", args);
        throw new JVSException();
    }

    public void start() {
        if(null != mServer) {
            mServer.start();
            System.out.println("Server has been started");
        } else {
            System.err.println("Server was not initialized");
        }
    }
/*
    public void stop() {
        if(null != mServer) {
            try {
                mServer.stop(SERVER_DELAY);
            } catch (IllegalArgumentException e) {
                System.err.println("Delay is negative");
                mServer.stop(0);
            }
            System.out.println("Server has been stopped");
        } else {
            System.err.println("Server was not initialized");
        }
    }
*/
}