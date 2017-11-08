package service;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONHandler implements HttpHandler {
    private static final Logger LOG = Logger.getLogger(JSONHandler.class.getName());

    private static class BadJSONResponse {
        @Expose
        private static int mRequestId = 0;

        int errorCode;
        String errorMessage;
        String errorPlace;
        String resource;
        int requestId;

        BadJSONResponse(String cause, String path) {
            Pattern pattern = Pattern.compile("(.+) (at.*)");
            Matcher matcher = pattern.matcher(cause);
            if(matcher.find()) {
                errorMessage = matcher.group(1);
                errorPlace = matcher.group(2);
            }

            switch (errorMessage) {
                case "Unterminated array":
                    errorCode = 1;
                    break;
                case "Unterminated object":
                    errorCode = 2;
                    break;
                case "Expected name":
                    errorCode = 3;
                    break;
                case "Expected ':'":
                    errorCode = 4;
                    break;
                case "Unexpected value":
                    errorCode = 5;
                    break;
                case "Expected value":
                    errorCode = 6;
                    break;
                case "Unterminated string":
                    errorCode = 7;
                    break;
                case "Unterminated comment":
                    errorCode = 8;
                    break;
                case "Use JsonReader.setLenient(true) to accept malformed JSON":
                    errorMessage = "Malformed JSON";
                    errorCode = 9;
                    break;
                case "Unterminated escape sequence":
                    errorCode = 10;
                    break;
                case "Invalid escape sequence":
                    errorCode = 11;
                    break;
                case "JSON forbids NaN and infinities":
                    errorCode = 12;
                    break;
                default:
                    errorCode = 0;
                    LOG.log(Level.WARNING, "Unexpected error message: {0}", errorMessage);
            }
            resource = path;
            requestId = ++mRequestId;
        }
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String request = readRequest(t.getRequestBody());
        String path = t.getRequestURI().getPath();
        path = path.replaceFirst("^/", "");
        path = ("".equals(path)) ? "json" : path;

        String response = validateRequest(request, path);

        t.getResponseHeaders().set("Content-type", "application/json");

        t.sendResponseHeaders(java.net.HttpURLConnection.HTTP_OK, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String readRequest(InputStream stream) {
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = new BufferedInputStream(stream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            stream.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Exception has occurred during reading a request body", e);
        }
        return builder.toString();
    }

    private String validateRequest(String request, String path) {
        StringBuilder response = new StringBuilder();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();
        JsonElement json;

        try {
            json = parser.parse(request);

            response.append(gson.toJson(json));
        } catch (JsonSyntaxException e) {
            BadJSONResponse resp = new BadJSONResponse(e.getCause().getMessage(), path);

            response.append(gson.toJson(resp));
        }
        return response.append('\n').toString();
    }
}