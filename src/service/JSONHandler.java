package service;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

import static java.net.HttpURLConnection.HTTP_OK;

public class JSONHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        String request = readRequest(t.getRequestBody());

        String response = validateRequest(request);

        t.getResponseHeaders().set("Content-type", "application/json");

        t.sendResponseHeaders(HTTP_OK, response.length());
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
                builder.append(line);
            }
            stream.close();
        } catch (IOException e) {
            System.err.println("Exception has occurred during reading a request body");
            e.printStackTrace();
        }
        return builder.toString();
    }

    private String validateRequest(String request) {
        StringBuilder response = new StringBuilder();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();
        JsonElement json;

        try {
            json = parser.parse(request);

            response.append(gson.toJson(json));
        } catch (JsonSyntaxException e) {
            response.append(e.getMessage());
        }
        return response.append('\n').toString();
    }
}