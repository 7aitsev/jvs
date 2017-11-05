package service;

public class JVSOptionsBuilder {

    private final Configuration mConfig = new JVSOptions().getConfiguration();

    public JVSOptionsBuilder() { }

    public JVSOptionsBuilder(String host, int port) {
        mConfig.host = host;
        mConfig.port = port;
    }

    public JVSOptionsBuilder setHost(String host) {
        mConfig.host = host;
        return this;
    }

    public JVSOptionsBuilder setPort(int port) {
        mConfig.port = port;
        return this;
    }

    public JVSOptionsBuilder setBacklog(int backlog) {
        mConfig.backlog = backlog;
        return this;
    }

    public JVSOptionsBuilder setPath(String path) {
        mConfig.path = path;
        return this;
    }

    public JVSOptionsBuilder setDelay(int delay) {
        mConfig.delay = delay;
        return this;
    }

    public JVSOptions build() {
        return new JVSOptions(this);
    }

    Configuration getConfiguration() {
        return mConfig;
    }
}
