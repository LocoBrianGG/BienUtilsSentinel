package dev.bienstudios.bienutilssentinel.core;

import java.util.List;
import java.util.Map;

/**
 * Representa el resultado de un proceso de sanitización aplicado sobre una cadena de texto.
 * Contiene tanto la forma limpia del valor, como indicadores de potencial hostilidad.
 *
 * Este objeto puede ser devuelto por un servicio de validación y limpieza como
 * respuesta estandarizada para entornos frontend, APIs u otros backends.
 *
 * @author BienStudios Develops.
 * @since 1.0.0
 */
public class SanitizationResult {

    /**
     * Valor original recibido, sin modificar.
     */
    private String originalValue;

    /**
     * Valor limpio, procesado según las reglas aplicadas.
     */
    private String cleanedValue;

    /**
     * Indica si se detectaron patrones maliciosos conocidos.
     */
    private boolean hostility;

    /**
     * Mapa con las coincidencias encontradas por categoría (ej: sql, xss, cmd).
     */
    private Map<String, List<String>> matchesByCategory;

    /**
     * Tipo de codificación aplicada sobre caracteres especiales (ASCII, UNICODE, HTML).
     */
    private String encodingMode;

    /**
     * Perfil de sanitización aplicado (alnum, numeric+dot, any).
     */
    private String patternProfile;

    // --- Constructor completo ---
    public SanitizationResult(
        String originalValue,
        String cleanedValue,
        boolean hostility,
        Map<String, List<String>> matchesByCategory,
        String encodingMode,
        String patternProfile
    ) {
        this.originalValue = originalValue;
        this.cleanedValue = cleanedValue;
        this.hostility = hostility;
        this.matchesByCategory = matchesByCategory;
        this.encodingMode = encodingMode;
        this.patternProfile = patternProfile;
    }

    // --- Getters y Setters ---

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getCleanedValue() {
        return cleanedValue;
    }

    public void setCleanedValue(String cleanedValue) {
        this.cleanedValue = cleanedValue;
    }

    public boolean isHostility() {
        return hostility;
    }

    public void setHostility(boolean hostility) {
        this.hostility = hostility;
    }

    public Map<String, List<String>> getMatchesByCategory() {
        return matchesByCategory;
    }

    public void setMatchesByCategory(Map<String, List<String>> matchesByCategory) {
        this.matchesByCategory = matchesByCategory;
    }

    public String getEncodingMode() {
        return encodingMode;
    }

    public void setEncodingMode(String encodingMode) {
        this.encodingMode = encodingMode;
    }

    public String getPatternProfile() {
        return patternProfile;
    }

    public void setPatternProfile(String patternProfile) {
        this.patternProfile = patternProfile;
    }
}