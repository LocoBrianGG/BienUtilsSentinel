package dev.bienstudios.bienutilssentinel.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Clase responsable de cargar y leer las cadenas maliciosas provistas en la lista negra.
 * Este Loader se encarga de proveer la lista de términos que deban rastrearse en
 * determinada cadena de texto.
 * @author BienStudios Develops.
 * @since 1.0.0
 */
public class BlacklistLoader {

    private static final String CONFIG_PATH = "config/blacklist.properties";
    private final Map<String, List<String>> blacklist = new HashMap<>();

    public BlacklistLoader() {
        loadBlacklist();
    }

    /**
     * Carga el archivo .properties y organiza los valores por categoría (prefijo de clave).
     */
    private void loadBlacklist() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            if (input == null) {
                System.err.println("[BlacklistLoader] No se encontró el archivo: " + CONFIG_PATH);
                return;
            }

            Properties props = new Properties();
            props.load(input);

            for (String key : props.stringPropertyNames()) {
                String[] parts = key.split("\\.");
                if (parts.length != 2) continue; // Evita claves mal formateadas

                String category = parts[0];
                String value = props.getProperty(key).trim().toLowerCase();

                blacklist.computeIfAbsent(category, k -> new ArrayList<>()).add(value);
            }

            System.out.println("[BlacklistLoader] Cargadas " + blacklist.size() + " categorías de blacklist.");

        } catch (IOException e) {
            System.err.println("[BlacklistLoader] Error al leer el archivo: " + e.getMessage());
        }
    }

    /**
     * Devuelve el mapa completo de blacklist agrupado por categoría.
     * @return mapa inmutable donde cada clave es una categoría, y su
     * valor una lista de cadenas maliciosas dentro de la categoría
     */
    public Map<String, List<String>> getBlacklist() {
        return Collections.unmodifiableMap(blacklist);
    }

    /**
     * Devuelve los términos maliciosos de una categoría específica.
     * @param category Nombre de la categoría buscada.
     * @return Lista de cadenas sospechosas dentro de la categoría asignada.
     */
    public List<String> getByCategory(String category) {
        return blacklist.getOrDefault(category, Collections.emptyList());
    }

    /**
     * Verifica si el input contiene alguna cadena maliciosa, sin importar la categoría.
     * @param input Texto de entrada, el cual se quiere revisar.
     * @return Verdadero (true) en caso de encontrar una coincidencia, falso (false) en
     * el caso contrario.
     */
    public boolean containsAny(String input) {
        String lowered = input.toLowerCase();
        return blacklist.values().stream()
            .flatMap(Collection::stream)
            .anyMatch(lowered::contains);
    }

    /**
     * Devuelve un mapa de coincidencias por categoría con las cadenas encontradas.
     * @param input Texto el cual se quiere validar, procedente directamente de un input.
     * @return Mapa con las coincidencias encontradas, cuya clave será la categoría y su
     * valor, la lista de coincidencias dentro de esa categoría encontradas.
     */
    public Map<String, List<String>> findMatches(String input) {
        String lowered = input.toLowerCase();
        Map<String, List<String>> matches = new HashMap<>();

        for (var entry : blacklist.entrySet()) {
            List<String> found = new ArrayList<>();
            for (String token : entry.getValue()) {
                if (lowered.contains(token)) {
                    found.add(token);
                }
            }
            if (!found.isEmpty()) {
                matches.put(entry.getKey(), found);
            }
        }

        return matches;
    }
}