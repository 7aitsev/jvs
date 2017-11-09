package service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

/**
 * RESTful web-service for JSON validation.
 * <p>
 * This is the main class for using JSON Validation Service (JVS). Its goal is to handle client requests
 * and respond to them whether received JSON is valid (<a href="http://www.ietf.org/rfc/rfc7159.txt">RFC 7159</a>)
 * or some edits should be done in order to get a valid JSON.
 * <p>
 * JVS is used by constructing a JSONValidationService instance and then invoking {@link #start()} method on it.
 * Likewise, to stop the service call {@link #stop()} method.
 * <p>
 * You can create a JVS instance by invoking {@code new JSONValidationService()}. In this case
 * the default configuration is used. A configuration can be changed using an instance of {@link JVSOptions}
 * class in the  overloaded constructor. JVSOption reads {@code jvs.properties} configuration file
 * or another file specified in its constructor. If the file cannot be used, it uses predefined parameters.
 * You can also use {@link JVSOptionsBuilder} to build a JVSOptions instance with desired parameters.
 * <p>
 * Here is an example of how JVS is used for JSON validation:
 * <pre>
 * JVSOptions options = JVSOptionsBuilder("example.com", 80).setPath("myapp/path").build();
 * JSONValidationService service;
 *
 * try {
 *     service = new JSONValidationService(options);
 *
 *     service.start();
 * } catch (JVSException e) {
 *     e.printStackTrace();
 * }</pre>
 * <p>
 * As you can see, you have to deal with {@link JVSException} which could be thrown if a port number is already in use,
 * a hostname is unresolved and so on.
 * <p>
 * Read log messages to retrieve contextual information about service execution. {@code CONFIG} level is verbose
 * enough.
 */
public class JSONValidationService {
    private static final Logger LOG = Logger.getLogger(JSONValidationService.class.getName());

    private JVSOptions mOptions;
    private HttpServer mServer = null;

    /**
     * Creates an instance of the class using defaults parameters.
     * <p>
     * The method creates an instance of the JSONValidationService class using parameters that
     * are defined by the default constructor for {@link JVSOptions} class.
     *
     * @throws JVSException  if it is impossible to proceed the creation of the class instance.
     */
    public JSONValidationService() throws JVSException {
        init(new JVSOptions());
    }

    /**
     * Creates a new instance using parameters defined in {@code options}.
     * <p>
     * The method creates an instance of the JSONValidationService class using parameters that
     * are defined by {@link JVSOptions} class.
     *
     * @param options  {@code JVSOptions} instances which holds all the parameters.
     * @throws JVSException  if it is impossible to proceed the creation of the class instance.
     */
    public JSONValidationService(JVSOptions options) throws JVSException {
        init((null != options) ? options : new JVSOptions());
    }

    private void init(JVSOptions options) throws JVSException {
        mOptions = options;
        InetAddress address = null;
        InetSocketAddress socket = null;

        try {
            mServer = HttpServer.create();
        } catch (IOException e) {
            error("Server could not be created");
        }

        try {
            address = InetAddress.getByName(mOptions.host);
        } catch (UnknownHostException e) {
            error("Could not determine the IP of a host for <%s>", mOptions.host);
        } catch (SecurityException e) {
            error("Operation is not allowed");
        }

        try {
            socket = new InetSocketAddress(address, mOptions.port);
        } catch (IllegalArgumentException e) {
            error("Port is outside of the range of valid port values: %d", mOptions.port);
        } catch (SecurityException e) {
            error("Operation is not allowed");
        }

        try {
            mServer.bind(socket, mOptions.backlog);
        } catch (BindException e) {
            error("Server cannot bind to the requested address: <%s>", mOptions.host);
        } catch (IOException e) {
            error("Bind failed");
        }

        try {
            mServer.createContext(mOptions.path, new JSONHandler());
        } catch (IllegalArgumentException e) {
            error("Path <%s> is invalid", mOptions.path);
        }

        LOG.log(CONFIG, "Server has been created:\nhost:port=<{0}:{1}>, backlog={2}, path={3}, delay={4}",
                new Object[] {(null == mOptions.host) ? "localhost" : mOptions.host, mOptions.port,
                mOptions.backlog, mOptions.path, mOptions.delay});
    }

    private void error(String msg, Object... args) throws JVSException {
        mOptions = null;
        throw new JVSException(String.format(msg, args));
    }

    /**
     * Starts the service.
     * <p>
     * The method starts the JVS service if it was properly constructed.
     */
    public void start() {
        if(null != mServer) {
            mServer.start();
            LOG.log(INFO, "Server has been started");
        } else {
            LOG.log(WARNING, "Server was not initialized");
        }
    }

    /**
     * Stops the service.
     * <p>
     * The method stops the JVS service which will wait {@code delay} seconds
     * until exchanges have finished.
     *
     * @see JVSOptionsBuilder#setDelay(int)
     */
    public void stop() {
        if(null != mServer) {
            mServer.stop(mOptions.delay);
            LOG.log(INFO, "Server has been stopped");
        } else {
            LOG.log(WARNING, "Server was not initialized");
        }
    }
}