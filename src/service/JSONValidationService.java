package service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class JSONValidationService {
    private Configuration mConfig;
    private HttpServer mServer = null;

    public JSONValidationService() throws JVSException {
        init(new JVSOptions());
    }

    public JSONValidationService(JVSOptions options) throws JVSException {
        init((null != options) ? options : new JVSOptions());
    }

    private void init(JVSOptions options) throws JVSException {
        mConfig = options.getConfiguration();
        InetAddress address = null;
        InetSocketAddress socket = null;

        try {
            mServer = HttpServer.create();
        } catch (IOException e) {
            error("Server could not be created");
        }

        try {
            address = InetAddress.getByName(mConfig.host);
        } catch (UnknownHostException e) {
            error("Could not determine the IP of a host for <%s>", mConfig.host);
        } catch (SecurityException e) {
            error("Operation is not allowed");
        }

        try {
            socket = new InetSocketAddress(address, mConfig.port);
        } catch (IllegalArgumentException e) {
            error("Port is outside of the range of valid port values: %d", mConfig.port);
        } catch (SecurityException e) {
            error("Operation is not allowed");
        }

        try {
            mServer.bind(socket, mConfig.backlog);
        } catch (BindException e) {
            error("Server cannot bind to the requested address: <%s>", mConfig.host);
        } catch (IOException e) {
            error("Bind failed");
        }

        try {
            mServer.createContext(mConfig.path, new JSONHandler());
        } catch (IllegalArgumentException e) {
            error("Path <%s> is invalid", mConfig.path);
        }

        System.out.format("Server has been created:\nhost:port=<%s:%d>, backlog=%d, path=%s, delay=%d\n",
                (null == mConfig.host) ? "localhost" : mConfig.host, mConfig.port,
                mConfig.backlog, mConfig.path, mConfig.delay);
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

    public void stop() {
        if(null != mServer) {
            mServer.stop(mConfig.delay);
            System.out.println("Server has been stopped");
        } else {
            System.err.println("Server was not initialized");
        }
    }
}