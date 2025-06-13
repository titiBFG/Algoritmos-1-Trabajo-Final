Proyecto TP Integrador Algoritmos 1

# Grupo Nº3 #
Integrantes: Pontiroli, Nicolás - Scarpin, Julián - Semec, Santino - Sosa, Agustina.
Fecha: 17 de Junio de 2025

## Descripción ##
 
Biblioteca en Java para manipulación y análisis de datos tabulares (2D), inspirada en pandas de Python. Permite cargar, modificar, filtrar y transformar datos manteniendo tipado fuerte y siguiendo principios SOLID y buenas prácticas de Clean Code.

# Contenidos #

-Introducción 

-Objetivo y Alcance

-Cronograma de Entregas

-Metodología de Trabajo

-Colaboración del Equipo

-Funcionalidades Principales

-Uso Básico

-Diseño y Arquitectura

-Demostración

-Lecciones Aprendidas y Oportunidades de Mejora

-Contribuciones

## Introducción

Este proyecto integrador reúne conceptos de POO y estructuras de datos vistas en Algoritmos 1 para crear una librería en Java que maneje datos tabulares de manera sencilla y extensible. Se documentó el análisis y diseño previo, implementando el código según los principios de calidad y manteniendo la extensibilidad y mantenibilidad.

Objetivo y Alcance

Objetivo: Implementar una librería Java que proporcione operaciones de carga, acceso, modificación, filtrado, ordenamiento y análisis de datos tabulares.

Alcance: Funcionalidades mínimas obligatorias descritas en el enunciado del TPI, siguiendo criterios de evaluacion de la materia.

Cronograma de Entregas

Fecha

Tipo de Entrega

Contenido

29/04/2025

Seguimiento

Documentación de Análisis

09/05/2025

Seguimiento

Documentación de Diseño

13/06/2025

Final

Código Fuente, Documentación, Presentación

17/06/2025

Defensa

Presentación y Defensa en Clase

Metodología de Trabajo

Se adoptó un modelo iterativo-incremental con entregas parciales para validación continua.Actividades clave: análisis de requisitos, diseño orientado a objetos, implementación en Java, pruebas unitarias y documentación.

Colaboración del Equipo

Comunicación: Whatsapp para chat asíncrono, reuniones semanales por videoconferencia.

Control de versiones: Git con branching modelo dev → main, pull requests y revisiones de código.

Funcionalidades Principales

Información básica: filas, columnas, etiquetas, tipos (ENTERO, REAL, BOOLEANO, CADENA, NA).

Acceso indexado: por etiqueta y posición.

Carga/descarga CSV con delimitador configurable.

Visualización en consola con límites de filas/columnas.

Generación de tablas: desde CSV, copias profundas, arrays y listas de Java.

Modificación: set de celdas, insertar/eliminar filas y columnas.

Selección (slicing): head(), tail(), vistas parciales.

Filtrado: operadores comparativos y lógicos con combinaciones.

Copia independiente y concatenación de tablas.

Ordenamiento por una o más columnas.

Imputación de valores faltantes (NA).

Muestreo aleatorio según porcentaje.

Uso Básico

// Cargar CSV
DataTable tabla = CsvReader.read("datos.csv", ",", true);
// Mostrar primeras 5 filas
System.out.println(tabla.head(5));
// Filtrar
DataTable filtrada = tabla.filter(new And(
    new Gt("edad", 18), new Eq("activo", true)
));
// Ordenar por columna
DataTable ordenada = filtrada.sort(Arrays.asList("apellido", "nombre"));

Diseño y Arquitectura

Diagrama de Clases General: DataTable, Column, Row, Filter, CsvReader, CsvWriter, NA y enums de tipos y operadores.

Diagrama de Secuencia: Operación de filtrado de tabla.

Principios SOLID: Responsabilidad única en clases, inversión de dependencias para parsers y filtros.

Demostración

Durante la defensa se muestra en vivo:

Debug paso a paso en IDE para ilustrar el flujo interno.

Lecciones Aprendidas y Oportunidades de Mejora

Mejorar la eficiencia de operaciones con técnicas de indexing y caching.

Añadir paralelización para cargas y agregaciones.

Extender soporte a nuevos tipos de datos y formatos (JSON, Parquet).

Contribuciones

Se aceptan issues y pull requests.

