package dev.bienstudios.bienutilssentinel;

import com.sun.net.httpserver.HttpServer;
import dev.bienstudios.bienutilssentinel.http.SanitizationHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Clase principal del proyecto BienUtils Sentinel.
 * Levanta un servidor HTTP en el puerto configurado y registra
 * el endpoint de sanitización. Esta es la puerta de entrada al sistema.
 *
 * Configuración tomada desde: /resources/config/server.properties
 *
 * @author BienStudios Develops.
 * @since 1.0.0
 */
public class BienUtilsSentinel {

    public static void main(String[] args) {
        try {
            // Leer configuración
            Properties config = new Properties();
            try (InputStream in = BienUtilsSentinel.class.getClassLoader()
                    .getResourceAsStream("config/server.properties")) {

                if (in != null) {
                    config.load(in);
                } else {
                    System.err.println("[ERROR] No se encontró server.properties. Usando valores por defecto.");
                }
            }
            String envPort = System.getenv("PORT");
            int port = envPort != null ? Integer.parseInt(envPort) : Integer.parseInt(config.getProperty("server.port", "8080"));

            String endpoint = config.getProperty("server.endpoint", "/sanitize");

            // Iniciar servidor
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext(endpoint, new SanitizationHandler());
            server.setExecutor(null); // usa el executor por defecto

            System.out.println("BienUtils Sentinel arrancó en http://localhost:" + port + endpoint);
            System.out.println("Esperando solicitudes...");

            server.start();

        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo iniciar el servidor: " + e.getMessage());
        }
    }
}