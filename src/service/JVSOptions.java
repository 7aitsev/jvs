package service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JVSOptions {

    static final String SERVER_HOST = null; // loopback
    static final int SERVER_PORT = 8080;
    static final int SERVER_BACKLOG = 8;
    static final String SERVER_PATH = "/";
    static final int SERVER_DELAY = 10;

    public static final String CONFIG_FILE_PATH = "jvs.conf";

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
            System.err.format("Configuration file not found: cwd=%s\n",
                    System.getProperty("user.dir"));
            e.printStackTrace();
            useDefaults();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            useDefaults();
            return;
        } catch (JsonSyntaxException e) {
            System.err.println("Configuration: bad json format");
            e.printStackTrace();
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
