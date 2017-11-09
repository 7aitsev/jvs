package service;

/**
 * Constructs JVSOptions using the variety of setter methods.
 *
 * <p>
 * Use this builder to construct a {@link JVSOptions} instance when you need to set configuration
 * options other than the default. {@code JVSOptionsBuilder} is best used by creating it, and then invoking its
 * various configuration methods, and finally calling {@link #build()}.
 * </p>
 */
public class JVSOptionsBuilder {
    private String host = JVSOptions.SERVER_HOST;
    private int port = JVSOptions.SERVER_PORT;
    private int backlog = JVSOptions.SERVER_BACKLOG;
    private String path = JVSOptions.SERVER_PATH;
    private int delay = JVSOptions.SERVER_DELAY;

    /**
     * Creates a JVSOptionsBuilder instance that can be used to build JVSOptions with various configuration
     * settings. JVSOptionsBuilder follows the builder pattern, and it is typically used by first
     * invoking various configuration methods to set desired options, and finally calling {@link #build()}.
     */
    public JVSOptionsBuilder() { }

    /**
     * Constructs a JVSOptionsBuilder instance from a JVSOptions instance. The newly constructed JVSOptionsBuilder
     * has the same configuration as the previously built JVSOptions instance.
     *
     * @param options  the JVSOptions instance whose configuration should be applied to a new JVSOptionsBuilder.
     */
    public JVSOptionsBuilder(JVSOptions options) {
        host = options.host;
        port = options.port;
        backlog = options.backlog;
        path = options.path;
        delay = options.delay;
    }

    /**
     * Configures JVSOptions to use specified hostname and port number.
     *
     * @param host  a hostname or an IP address like 192.0.0.2.
     * @param port  a port number.
     */
    public JVSOptionsBuilder(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Sets a hostname or an IP address at which the server will accept requests.
     *
     * @param host  a hostname or an IP address (e.g. 192.0.0.2).
     * @return  reference to this {@code JVSOptionsBuilder} object to fulfill the "Builder" pattern.
     */
    public JVSOptionsBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Sets a port number that is used by the service.
     *
     * @param port  a port number (1 - 65535)
     * @return  reference to this {@code JVSOptionsBuilder} object to fulfill the "Builder" pattern.
     */
    public JVSOptionsBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Sets a backlog which represents the maximum number of incoming TCP connections which the system
     * will queue internally. Connections are queued while they are waiting to be accepted by the service.
     *
     * @param backlog  the socket backlog. If this value is less than or equal to zero,
     *                 then a system default value is used.
     * @return  reference to this {@code JVSOptionsBuilder} object to fulfill the "Builder" pattern.
     */
    public JVSOptionsBuilder setBacklog(int backlog) {
        this.backlog = backlog;
        return this;
    }

    /**
     * Sets a URI path which represents the location of the service on the given host.
     *
     * @param path  the URI path where the service is run.
     * @return  reference to this {@code JVSOptionsBuilder} object to fulfill the "Builder" pattern.
     */
    public JVSOptionsBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * Stops the JVS service by closing the listening socket and disallowing any new exchanges from being processed.
     *
     * @param delay  the maximum time in seconds to wait until exchanges have finished.
     * @return  reference to this {@code JVSOptionsBuilder} object to fulfill the "Builder" pattern.
     */
    public JVSOptionsBuilder setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    /**
     * Creates a {@link JVSOptions} instance based on the current configuration.
     *
     * @return  an instance of JVSOptions configured with the options currently set in this builder.
     */
    public JVSOptions build() {
        return new JVSOptions(host, port, backlog, path, delay);
    }
}
