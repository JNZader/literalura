# LiteraLura - Challenge Alura

LiteraLura es una aplicación Java desarrollada como parte del challenge de Alura. Esta aplicación permite a los usuarios buscar y gestionar información sobre libros y autores, utilizando tanto una API externa (Gutendex) como una base de datos local.

## Características

- Búsqueda de libros por título
- Listado de libros registrados
- Listado de autores registrados
- Búsqueda de autores vivos en un rango de años específico
- Listado de libros por idioma
- Top 10 de libros más descargados
- Estadísticas de descargas de libros

## Tecnologías utilizadas

- Java
- Spring Boot
- JPA / Hibernate
- RESTful API (Gutendex)

## Requisitos previos

- Java JDK 11 o superior
- Maven
- Una base de datos (MySQL, PostgreSQL, etc.)

## Configuración

1. Clona este repositorio:
   git clone https://github.com/tu-usuario/literalura.git
2. Navega al directorio del proyecto:
   cd literalura
3. Configura tu base de datos en src/main/resources/application.properties
4. Ejecuta la aplicación:
   mvn spring-boot:run

## Uso

La aplicación presenta un menú interactivo en la consola con las siguientes opciones:

1. Buscar libros por título
2. Listar libros registrados
3. Listar autores registrados
4. Listar autores vivos en un año
5. Listar libros por idioma
6. Listar el top 10 libros más descargados
7. Estadísticas
0. Salir

Para cada opción, puedes elegir entre usar la base de datos local o la API externa como fuente de datos.

## Contribuir

Este proyecto fue desarrollado como parte de un challenge de Alura. Si tienes sugerencias o mejoras, no dudes en abrir un issue o hacer un pull request.

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.

## Reconocimientos

- Alura por proporcionar el challenge y los recursos de aprendizaje.
- Gutendex por proporcionar la API de libros.

---

Desarrollado como parte del challenge de Alura
