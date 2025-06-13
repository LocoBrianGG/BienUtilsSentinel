package dev.bienstudios.bienutilssentinel.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.bienstudios.bienutilssentinel.core.SanitizationService;
import dev.bienstudios.bienutilssentinel.core.SanitizationResult;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.json.JSONObject;

/**
 * Manejador HTTP que expone un endpoint de sanitización de texto.
 * Recibe una solicitud POST con un JSON que contiene:
 * - "input": texto a analizar
 * - "profile": perfil de limpieza ("alnum", "any", etc.)
 * - "encoding": tipo de codificación ("HTML", "UNICODE", etc.)
 *
 * Devuelve un JSON con los resultados del análisis y limpieza.
 *
 * Ejemplo de uso (POST /sanitizar):
 * {
 *   "input": "DROP TABLE users",
 *   "profile": "alnum",
 *   "encoding": "UNICODE"
 * }
 *
 * @author BienStudios Develops.
 * @since 1.0.0
 */
public class SanitizationHandler implements HttpHandler {

    private final SanitizationService service = new SanitizationService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        // Leer cuerpo del request
        String body;
        try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
            body = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
        }

        JSONObject inputJson = new JSONObject(body);

        String input = inputJson.optString("input", "");
        String profile = inputJson.optString("profile", "any");
        String encoding = inputJson.optString("encoding", "NONE");

        SanitizationResult result = service.sanitize(input, profile, encoding);

        // Armar respuesta JSON
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("value", result.getCleanedValue());
        jsonResponse.put("hostility", result.isHostility());
        jsonResponse.put("matches", result.getMatchesByCategory());
        jsonResponse.put("profile", result.getPatternProfile());
        jsonResponse.put("encoding", result.getEncodingMode());

        byte[] responseBytes = jsonResponse.toString().getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}