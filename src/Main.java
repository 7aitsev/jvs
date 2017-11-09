import service.JSONValidationService;
import service.JVSException;
import service.JVSOptions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entry point of the project.
 *
 * Creates {@link JSONValidationService} using configurations from the {@code jvs.properties} file and starts it.
 * There is no way to stop the service other than to send SIGKILL to the process.
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        JSONValidationService service;

        try {
            service = new JSONValidationService(new JVSOptions(null));
            service.start();
        } catch (JVSException e) {
            LOG.log(Level.SEVERE, "Service exception", e);
            System.exit(1);
        }
    }
}