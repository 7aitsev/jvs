import service.JSONValidationService;
import service.JVSException;

public class Main {

    public static void main(String[] args) {
        JSONValidationService service = null;
        try {
            service = new JSONValidationService();
            service.start();
        } catch (JVSException e) {
            e.printStackTrace();
        }
    }
}