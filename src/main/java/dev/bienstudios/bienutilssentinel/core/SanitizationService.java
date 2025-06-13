package dev.bienstudios.bienutilssentinel.core;

import dev.bienstudios.bienutilssentinel.config.BlacklistLoader;

import java.util.*;

/**
 * Servicio central de limpieza y validación de texto.
 *
 * Esta clase se encarga de detectar patrones maliciosos dentro de una cadena
 * de texto y devolver un resultado con la versión limpia, codificada y clasificada.
 * Aplica perfiles de sanitización y tipos de codificación según parámetros recibidos.
 *
 * @author BienStudios Develops.
 * @since 1.0.0
 */
public class SanitizationService {

    private final BlacklistLoader blacklist;

    /**
     * Constructor que inicializa el cargador de blacklist.
     */
    public SanitizationService() {
        this.blacklist = new BlacklistLoader();
    }

    /**
     * Sanitiza un texto según el perfil y modo de codificación especificados.
     *
     * @param input         Texto a analizar y limpiar
     * @param patternProfile Perfil de sanitización: "alnum", "numeric+dot", "any"
     * @param encodingMode   Modo de codificación para caracteres especiales: "ASCII", "UNICODE", "HTML", "NONE"
     * @return Un objeto de tipo {@link SanitizationResult} con todos los datos del análisis
     */
    public SanitizationResult sanitize(String input, String patternProfile, String encodingMode) {
        if (input == null) input = "";

        Map<String, List<String>> matches = blacklist.findMatches(input);
        boolean hostile = !matches.isEmpty();

        String cleaned = applyPattern(input, patternProfile);
        String encoded = applyEncoding(cleaned, encodingMode);

        return new SanitizationResult(
                input,
                encoded,
                hostile,
                matches,
                encodingMode,
                patternProfile
        );
    }

    /**
     * Aplica un patrón de sanitización que filtra caracteres según el perfil deseado.
     *
     * @param input  Texto original
     * @param profile Perfil a aplicar: "alnum", "numeric+dot", "any"
     * @return Texto limpio según el patrón solicitado
     */
    private String applyPattern(String input, String profile) {
        return switch (profile.toLowerCase()) {
            case "alnum" -> input.replaceAll("[^a-zA-Z0-9]", "");
            case "numeric+dot" -> input.replaceAll("[^0-9.]", "");
            case "any", "" -> input;
            default -> input;
        };
    }

    /**
     * Codifica los caracteres especiales según el modo solicitado.
     *
     * @param input Texto a codificar
     * @param mode  Modo de codificación: "ASCII", "UNICODE", "HTML", "NONE"
     * @return Texto resultante tras la codificación
     */
    private String applyEncoding(String input, String mode) {
        if (input == null) return "";
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            } else {
                sb.append(switch (mode.toUpperCase()) {
                    case "ASCII" -> String.format("\\x%02X", (int) c);
                    case "UNICODE" -> String.format("\\u%04X", (int) c);
                    case "HTML" -> encodeHtmlEntity(c);
                    case "NONE", "" -> String.valueOf(c);
                    default -> String.valueOf(c);
                });
            }
        }

        return sb.toString();
    }

    /**
     * Retorna la entidad HTML asociada a un carácter especial, o su valor decimal.
     *
     * @param c Carácter a convertir
     * @return Cadena codificada como entidad HTML
     */
    private String encodeHtmlEntity(char c) {
        return switch (c) {
            case '<' -> "&lt;";
            case '>' -> "&gt;";
            case '&' -> "&amp;";
            case '"' -> "&quot;";
            case '\'' -> "&#x27;";
            default -> "&#" + (int) c + ";";
        };
    }
}