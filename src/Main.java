import service.JSONValidationService;
import service.JVSException;
import service.JVSOptions;
import service.JVSOptionsBuilder;

public class Main {

    public static void main(String[] args) {
        JSONValidationService service;

        try {
            service = new JSONValidationService();
            service.start();
        } catch (JVSException e) {
            e.printStackTrace();
        }
    }
}