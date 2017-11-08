import service.JSONValidationService;
import service.JVSException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        JSONValidationService service;

        try {
            service = new JSONValidationService();
            service.start();
        } catch (JVSException e) {
            LOG.log(Level.SEVERE, "Service exception", e);
            System.exit(1);
        }
    }
}