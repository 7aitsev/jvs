package service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JVSOptions {
    private static final Logger LOG = Logger.getLogger(JVSOptions.class.getName());

    static final String SERVER_HOST = null; // loopback
    static final int SERVER_PORT = 8080;
    static final int SERVER_BACKLOG = 8;
    static final String SERVER_PATH = "/";
    static final int SERVER_DELAY = 10;

    public static final String CONFIG_FILE_PATH = "jvs.properties";

    private Configuration mConfig;

    public JVSOptions() {
        useDefaults();
    }

    public JVSOptions(String configPath) {
        Gson gson = new Gson();
        JsonReader reader;

        configPath = (null == configPath || "".equals(configPath)) ? CONFIG_FILE_PATH : configPath;
        try(FileReader fileReader = new FileReader(configPath)) {
            reader = new JsonReader(fileReader);

            mConfig = gson.fromJson(reader, Configuration.class);
        } catch (FileNotFoundException e) {
            LOG.log(Level.WARNING, "Configuration file not found: cwd={0}\n", System.getProperty("user.dir"));
            useDefaults();
            return;
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Exception has occurred during reading a setup configuration file {0}", configPath);
            useDefaults();
            return;
        } catch (JsonSyntaxException e) {
            LOG.log(Level.WARNING,"Configuration: bad json format\n{0}", e.getMessage());
            useDefaults();
        }

        checkConstraints();
    }

    private void useDefaults() {
        mConfig = new Configuration();
        mConfig.host = SERVER_HOST;
        mConfig.port = SERVER_PORT;
        mConfig.backlog = SERVER_BACKLOG;
        mConfig.path = SERVER_PATH;
        mConfig.delay = SERVER_DELAY;
    }

    JVSOptions(JVSOptionsBuilder builder) {
        mConfig = builder.getConfiguration();

        checkConstraints();
    }

    private void checkConstraints() {
        // TODO: check constraints: e.g. port is in range [1, 65535] and so on
    }

    Configuration getConfiguration() {
        return mConfig;
    }
}
