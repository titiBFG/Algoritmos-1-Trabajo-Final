# JADA1: Java Data Analysis 1
**JADA1** es un proyecto desarrollado como trabajo final para la materia _Algoritmos 1_. El objetivo principal es poner en práctica los conceptos fundamentales de programación y estructuras de datos utilizando el lenguaje _Java_. A través de este proyecto, se busca afianzar habilidades como el diseño algorítmico, la resolución de problemas y la implementación eficiente de soluciones. **JADA1** está pensado como una base sólida para quienes inician en el mundo del desarrollo de software, promoviendo buenas prácticas y el aprendizaje colaborativo.

**JADA1** es una libreria desarrollada en _Java_ que permite utilizar funciones basicas para la exploracion y el analisis de conjuntos de datos.
Los datos puedes ser cargados a través de ***CSV*** o estructuras nativas de java ***lineales*** o ***bidimensionales***

Permite
- Cargar datos desde archivos csv o estructuras nativas de java.
- Procesar información tabular.
- Ordenar y filtrar datos, implementando algoritmos clásicos como búsqueda lineal, ordenamiento por selección y otros.

## Colaboración
Fue armado en colaboración con [Nicolas Pontiroli](https://github.com/Npontiroli21), [Agustina Sosa](https://github.com/aguscodes) y [Julian Scarpin](https://github.com/scarpin-julian)

## Estructura del proyecto
El proyecto consta de 3 modulos: 
- Principal
- Utils
- I/O

**Diagrama de clases**

https://github.com/user-attachments/assets/0b77cac1-4e76-4b41-84ef-aa689796ab3e

**Estructura de paquetes de java**

![image](https://github.com/user-attachments/assets/5560684f-bb8c-423a-bd72-0dc48001c033)

## Funcionalidades Principales

**Información básica:** filas, columnas, etiquetas, tipos (ENTERO, REAL, BOOLEANO, CADENA, NA).

**Acceso indexado:** por etiqueta y posición.

**Carga/descarga CSV** con delimitador configurable.

**Visualización en consola** con límites de filas/columnas.

**Generación de tablas:** desde CSV, copias profundas, arrays y listas de Java.

**Modificación:** set de celdas, insertar/eliminar filas y columnas.

**Selección (slicing):** head(), tail(), vistas parciales.

**Filtrado:** operadores comparativos y lógicos con combinaciones.

**Copia independiente y concatenación** de tablas.

**Ordenamiento** por una o más columnas.

**Imputación de valores faltantes (NA).**

**Muestreo aleatorio** según porcentaje.

## Uso Básico

// Cargar CSV DataTable tabla = CsvReader.read("datos.csv", ",", true); // Mostrar primeras 5 filas System.out.println(tabla.head(5)); // Filtrar DataTable filtrada = tabla.filter(new And( new Gt("edad", 18), new Eq("activo", true) )); // Ordenar por columna DataTable ordenada = filtrada.sort(Arrays.asList("apellido", "nombre"));

Diseño y Arquitectura

Diagrama de Clases General: DataTable, Column, Row, Filter, CsvReader, CsvWriter, NA y enums de tipos y operadores.

Diagrama de Secuencia: Operación de filtrado de tabla.

Principios SOLID: Responsabilidad única en clases, inversión de dependencias para parsers y filtros.

_Demostración_

_Durante la defensa se muestra en vivo:_

_Debug paso a paso en IDE para ilustrar el flujo interno._

_Lecciones Aprendidas y Oportunidades de Mejora_

_Mejorar la eficiencia de operaciones con técnicas de indexing y caching._

_Añadir paralelización para cargas y agregaciones._

_Extender soporte a nuevos tipos de datos y formatos (JSON, Parquet)._

## Mejoras pendientes
- **Mejor visualización de las tablas:** Optimizar la presentación de tablas para facilitar la lectura y comprensión de los datos, utilizando mejores estilos o formatos.
- **Implementación de selección:** Agregar funcionalidades que permitan seleccionar filas o elementos dentro de las tablas para realizar acciones específicas.
- **Agrupamiento de datos:** Incorporar opciones para agrupar información según distintos criterios, facilitando el análisis de los datos presentados.
- **Mejora de funcionalidades existentes:** Revisar y optimizar funciones ya implementadas que podrían estar mejor estructuradas o ser más eficientes, asegurando mayor claridad y rendimiento en el código.
