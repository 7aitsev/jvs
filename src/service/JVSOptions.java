package service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds parameters for JSON Validation Service.
 * <p>
 * This class is meant to be a POJO object with enumeration of parameters that can be used by JVS. More importantly,
 * it is able to populate all its parameters from a configuration file. It is convenient to use JSON format
 * for such file. Therefor this class got a constructor for building an instance by populating all the parameters
 * from a file.
 * <p>
 * The setup configuration file is in JSON format with the following fields:
 * <ul>
 *     <li>{@code host} - a hostname or an IP address for the server to bind</li>
 *     <li>{@code port} - a port number that is used by the server</li>
 *     <li>{@code baclog} - the maximum number of incoming TCP connections</li>
 *     <li>{@code path} - the location of the service on the given server</li>
 *     <li>{@code delay} - the maximum time in seconds to wait until exchanges have finished</li>
 * </ul>
 * <p>
 * If any errors occurs during deserialization of that JSON, default parameters are used.
 * <p>
 * You can simply create an instance by calling {@link #JVSOptions()} and change its properties via
 * {@link JVSOptionsBuilder} API like this:
 * <pre>JVSOptions = new JVSOptions().newBuilder().setBacklog(0).build();</pre>
 * <p>
 * Or you can use a file to populate all parameters:
 * <pre>JVSOptions = new JVSOptions("myconfig.conf");</pre>
 * <p>
 * Default parameters are:
 * <pre>
 * static final String SERVER_HOST = null; // loopback
 * static final int SERVER_PORT = 8080;
 * static final int SERVER_BACKLOG = 8;
 * static final String SERVER_PATH = "/";
 * static final int SERVER_DELAY = 16;
 * </pre>
 */
public class JVSOptions {
    private static final Logger LOG = Logger.getLogger(JVSOptions.class.getName());

    static final String SERVER_HOST = null; // loopback
    static final int SERVER_PORT = 8080;
    static final int SERVER_BACKLOG = 8;
    static final String SERVER_PATH = "/";
    static final int SERVER_DELAY = 16;

    /** Default location for the configuration file */
    public static final String CONFIG_FILE_PATH = "jvs.properties";

    String host = SERVER_HOST;
    int port = SERVER_PORT;
    int backlog = SERVER_BACKLOG;
    String path = SERVER_PATH;
    int delay = SERVER_DELAY;

    /**
     * Constructs a JVSOptions object with default configuration.
     * <p>
     * The default configuration is listed above (means SERVER_* constants).
     */
    public JVSOptions() { }

    /**
     * Constructs a JVSOptions object using parameters defined in a configuration file.
     * <p>
     * The constructor creates an instance by populating its configuration values from a file.
     * The file path is used from the {@code configPath} argument. If it is null or empty, then
     * {@link #CONFIG_FILE_PATH} is used. If any exception occurs during reading and parsing the file, then
     * the invocation result is the same as for the default constructor.
     *
     * @param configPath  location of the setup configuration file.
     */
    public JVSOptions(String configPath) {
        Gson gson = new Gson();
        JVSOptions options = null;
        JsonReader reader;

        configPath = (null == configPath || "".equals(configPath)) ? CONFIG_FILE_PATH : configPath;
        try(FileReader fileReader = new FileReader(configPath)) {
            reader = new JsonReader(fileReader);

            options = gson.fromJson(reader, JVSOptions.class);
        } catch (FileNotFoundException e) {
            LOG.log(Level.WARNING, "Configuration file not found: cwd={0}\n", System.getProperty("user.dir"));
            return;
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Exception has occurred during reading a setup configuration file {0}", configPath);
            return;
        } catch (JsonSyntaxException e) {
            LOG.log(Level.WARNING,"Configuration: bad json format\n{0}", e.getMessage());
        }

        if(null != options) {
            host = options.host;
            port = options.port;
            backlog = options.backlog;
            path = options.path;
            delay = options.delay;
        }

        checkConstraints();
    }

    /**
     * Returns a new JVSOptionsBuilder containing all configuration used by the current instance.
     *
     * @return  a JVSOptionBuilder instance.
     */
    public JVSOptionsBuilder newBuilder() {
        return new JVSOptionsBuilder(this);
    }

    JVSOptions(String host, int port, int backlog, String path, int delay) {
        this.host = host;
        this.port = port;
        this.backlog = backlog;
        this.path = path;
        this.delay = delay;

        checkConstraints();
    }

    private void checkConstraints() {
        // TODO: check constraints: e.g. port is in range [1, 65535] and so on
    }
}
