# BienUtilsSentinel – Validador de Seguridad

![BienUtilsSentinel Banner](media/banner.png)

**Versión:** 1.0.0
**Autor:** BienStudios Develops
**Colaboración:** Microsoft Copilot
**Licencia:** [MIT con cláusula de atribución](#licencia)

---

## ¿Qué es BienUtilsSentinel?

BienUtilsSentinel es una herramienta de sanitización y validación de entradas de texto, diseñada para proteger aplicaciones web y backend contra ataques comunes como:

- Inyecciones SQL
- Cross-Site Scripting (XSS)
- Comandos maliciosos (CMD)
- Caracteres simbólicos sospechosos

Su objetivo es brindar una capa de defensa simple, extensible y accesible para desarrolladores que buscan mejorar la seguridad de sus sistemas sin depender de frameworks pesados.

---

## ¿Por qué existe?

Porque la seguridad no debería ser un lujo. Esta herramienta nace del deseo de proteger a usuarios y desarrolladores de ataques maliciosos. Si está en nuestras manos prevenir, lo haremos.

---

## Características principales

- Detección de patrones maliciosos por categoría (`sql`, `xss`, `cmd`, `sym`)
- Limpieza configurable por perfil:
  - `"alnum"`: solo letras y números
  - `"numeric+dot"`: solo números y puntos (ideal para IPs)
  - `"any"`: sin filtrado
- Codificación opcional:
  - `"HTML"`: entidades HTML (`&lt;`, `&quot;`)
  - `"UNICODE"`: `\uXXXX`
  - `"ASCII"`: `\xNN`
- Endpoint HTTP listo para consumir desde frontend o Postman

---

## Cómo consumir la API

**POST** `/sanitize`
**Content-Type:** `application/json`
**Body:**

```json
{
  "input": "DROP TABLE users; <script>alert(1);</script>",
  "profile": "alnum",
  "encoding": "HTML"
}
```

**Respuesta:**

```json
{
  "value": "DROPTABLEusersscriptalert1script",
  "hostility": true,
  "matches": {
    "sql": ["drop", ";"],
    "xss": ["<script", "</script>", "alert("],
    "cmd": [";"],
    "sym": ["<", ">", "(", ")"]
  },
  "profile": "alnum",
  "encoding": "HTML"
}
```

## Licencia

Este proyecto está bajo una licencia MIT modificada:
Se permite el uso, modificación y distribución del código con fines personales o comerciales, siempre que se mantenga la atribución al autor original.
No se permite redistribuir el código como si fuera propio.

## Contacto

- Correo electrónico: <mailto:brianmerfue98@gmail.com>
- WhatsApp: <https://wa.me/542233066532>
