package service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

public class JSONValidationService {
    private static final Logger LOG = Logger.getLogger(JSONValidationService.class.getName());

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

        LOG.log(CONFIG, "Server has been created:\nhost:port=<{0}:{1}>, backlog={2}, path={3}, delay={4}",
                new Object[] {(null == mConfig.host) ? "localhost" : mConfig.host, mConfig.port,
                mConfig.backlog, mConfig.path, mConfig.delay});
    }

    private void error(String msg, Object... args) throws JVSException {
        throw new JVSException(String.format(msg, args));
    }

    public void start() {
        if(null != mServer) {
            mServer.start();
            LOG.log(INFO, "Server has been started");
        } else {
            LOG.log(WARNING, "Server was not initialized");
        }
    }

    public void stop() {
        if(null != mServer) {
            mServer.stop(mConfig.delay);
            LOG.log(INFO, "Server has been stopped");
        } else {
            LOG.log(WARNING, "Server was not initialized");
        }
    }
}