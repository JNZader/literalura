package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.sevice.ConsumoAPI;
import com.alura.literalura.sevice.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

   private static final String URL_BASE = "https://gutendex.com/books/";
   private final ConsumoAPI consumoAPI;
   private final ConvierteDatos convierteDatos;
   private final LibroRepository libroRepository;
   private final AutorRepository autorRepository;
   private final Scanner scanner;

   @Autowired
   public Principal(ConsumoAPI consumoAPI, ConvierteDatos convierteDatos,
                    LibroRepository libroRepository, AutorRepository autorRepository) {
      this.consumoAPI = consumoAPI;
      this.convierteDatos = convierteDatos;
      this.libroRepository = libroRepository;
      this.autorRepository = autorRepository;
      this.scanner = new Scanner(System.in);
   }

   public void mostrarMenu() {
      int opcion = -1;
      while (opcion != 0) {
         var menu = """
               1 - Buscar libros por título
               2 - Listar libros registrados
               3 - Listar autores registrados
               4 - Listar autores vivos en un año
               5 - Listar libros por idioma
               6 - Listar el top 10 libros más descargados
               7 - Estadísticas
               0 - Salir
               """;
         System.out.println(menu);
         System.out.print("Elige una opción: ");
         try {
            opcion = scanner.nextInt();
            scanner.nextLine();
         } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
            scanner.nextLine();
            continue;
         }

         switch (opcion) {
            case 1:
               buscarLibrosPorTitulo();
               break;
            case 2:
               listarLibrosRegistrados();
               break;
            case 3:
               listarAutoresRegistrados();
               break;
            case 4:
               listarAutoresVivos();
               break;
            case 5:
               listarLibrosPorIdioma();
               break;
            case 6:
               listarTop10Libros();
               break;
            case 7:
               mostrarEstadisticas();
               break;
            case 0:
               System.out.println("Saliendo...");
               scanner.close();
               System.exit(0);
            default:
               System.out.println("Opción no válida. Por favor, elige nuevamente.");
         }
      }
   }

   private void buscarLibrosPorTitulo() {
      int sourceOption = elegirFuente();
      scanner.nextLine();

      System.out.print("Ingrese el nombre del libro que desea buscar: ");
      var tituloLibro = scanner.nextLine();

      if (sourceOption == 1) {
         System.out.println("Buscando libros por título en la base de datos...");
         List<Libro> libros = libroRepository.findByTituloContainingIgnoreCase(tituloLibro);
         if (libros.isEmpty()) {
            System.out.println("Libro no encontrado en la base de datos");
         } else {
            libros.forEach(libro -> {
               System.out.println("Libro encontrado: " + libro.getTitulo());
               System.out.println("Autor(es): " + libro.getAutores().stream().map(Autor::getNombre).collect(Collectors.joining(", ")));
               System.out.println("Idiomas: " + libro.getIdiomas());
               System.out.println("Número de Descargas: " + libro.getNroDescargas());
               System.out.println("----------");
            });
         }
      } else if (sourceOption == 2) {
         System.out.println("Buscando libros por título en la API...");
         var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
         var datosBusqueda = convierteDatos.obtenerDatos(json, Datos.class);

         Optional<DatosLibros> libroBuscado = datosBusqueda.libros().stream()
               .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
               .findFirst();

         libroBuscado.ifPresentOrElse(
               value -> {
                  System.out.println("Libro encontrado: " + libroBuscado.get().titulo());
                  System.out.println("Autor(es): " + libroBuscado.get().datosAutor().stream().map(da -> da.nombre()).collect(Collectors.joining(", ")));
                  System.out.println("Idiomas: " + libroBuscado.get().idiomas());
                  System.out.println("Número de Descargas: " + libroBuscado.get().nroDescargas());
                  guardarLibroEnBaseDeDatos(libroBuscado.get());
               },
               () -> System.out.println("Libro no encontrado en la API")
         );
      }
   }

   public void guardarLibroEnBaseDeDatos(DatosLibros datosLibros) {
      boolean libroExiste = libroRepository.existsById(datosLibros.id());
      if (!libroExiste) {
         Libro nuevoLibro = new Libro();
         nuevoLibro.setId(datosLibros.id()); // El ID se obtiene del JSON
         nuevoLibro.setTitulo(datosLibros.titulo());
         nuevoLibro.setNroDescargas(datosLibros.nroDescargas());
         nuevoLibro.setIdiomas(datosLibros.idiomas());


         List<Autor> autores = datosLibros.datosAutor().stream()
               .map(da -> new Autor(
                     null,
                     da.nombre(),
                     da.fechaNac() != null ? da.fechaNac() : null,
                     da.fechaMuerte() != null ? da.fechaMuerte() : null
               ))
               .collect(Collectors.toList());
         nuevoLibro.setAutores(autores);

         libroRepository.save(nuevoLibro);
         System.out.println("Libro guardado en la base de datos.");
      } else {
         System.out.println("El libro ya existe en la base de datos.");
      }
   }

   private void listarLibrosRegistrados() {
      int sourceOption = elegirFuente();
      scanner.nextLine();

      if (sourceOption == 1) {
         System.out.println("Listando libros registrados en la base de datos...");
         List<Libro> libros = libroRepository.findAll();
         libros.forEach(libro -> System.out.println(libro.getTitulo()));
      } else if (sourceOption == 2) {
         System.out.println("Listando libros registrados en la API...");
         Datos datos = obtenerLibros();
         datos.libros().forEach(libro -> System.out.println(libro.titulo()));
      }
   }

   private void listarAutoresRegistrados() {
      int sourceOption = elegirFuente();
      scanner.nextLine();

      if (sourceOption == 1) {
         System.out.println("Listando autores registrados en la base de datos...");
         List<Autor> autores = autorRepository.findAll();
         autores.forEach(autor -> System.out.println(autor.getNombre()));
      } else if (sourceOption == 2) {
         System.out.println("Listando autores registrados en la API...");
         Datos datos = obtenerLibros();
         datos.libros().stream()
               .flatMap(libro -> libro.datosAutor().stream())
               .map(autor -> autor.nombre())
               .distinct()
               .forEach(System.out::println);
      }
   }

   private void listarAutoresVivos() {
      System.out.print("Ingrese el año de inicio (author_year_start): ");
      int yearStart = 0;
      int yearEnd = 0;

      try {
         yearStart = scanner.nextInt();
         System.out.print("Ingrese el año de fin (author_year_end): ");
         yearEnd = scanner.nextInt();
         scanner.nextLine();

         if (yearStart > yearEnd) {
            System.out.println("El año de inicio no puede ser mayor que el año de fin.");
            return;
         }

         int sourceOption = elegirFuente();

         if (sourceOption == 1) {
            System.out.println("Listando autores vivos entre " + yearStart + " y " + yearEnd + " en la base de datos...");
            List<Autor> autores = autorRepository.findAuthorsAliveInYear(yearStart, yearEnd);
            if (autores.isEmpty()) {
               System.out.println("No hay autores vivos en ese rango de fechas.");
            } else {
               autores.forEach(autor -> System.out.println(autor.getNombre()));
            }
         } else if (sourceOption == 2) {
            System.out.println("Listando autores vivos entre " + yearStart + " y " + yearEnd + " en la API...");
            Datos datos = obtenerLibros();
            int finalYearStart = yearStart;
            int finalYearEnd = yearEnd;
            List<String> nombresAutores = datos.libros().stream()
                  .flatMap(libro -> libro.datosAutor().stream())
                  .filter(autor -> {
                     Integer fechaNacimiento = autor.fechaNac();
                     Integer fechaMuerte = autor.fechaMuerte() != null ? autor.fechaMuerte() : Integer.MAX_VALUE;
                     return (finalYearStart >= fechaNacimiento && finalYearStart <= fechaMuerte) ||
                           (finalYearEnd >= fechaNacimiento && finalYearEnd <= fechaMuerte) ||
                           (fechaNacimiento <= finalYearStart && fechaMuerte >= finalYearEnd);
                  })
                  .map(autor -> autor.nombre())
                  .distinct()
                  .toList();

            if (nombresAutores.isEmpty()) {
               System.out.println("No hay autores vivos en ese rango de fechas.");
            } else {
               nombresAutores.forEach(System.out::println);
            }
         }
      } catch (InputMismatchException e) {
         System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
         scanner.nextLine();
      }
   }

   private void listarLibrosPorIdioma() {
      int sourceOption = elegirFuente();
      scanner.nextLine();

      System.out.print("Ingrese el idioma: ");
      var idioma = scanner.nextLine();

      if (sourceOption == 1) {
         System.out.println("Listando libros por idioma en la base de datos...");
         List<Libro> libros = libroRepository.findByIdiomasContaining(idioma);
         if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
         } else {
            libros.forEach(libro -> System.out.println(libro.getTitulo()));
         }
      } else if (sourceOption == 2) {
         System.out.println("Listando libros por idioma en la API...");
         Datos datos = obtenerLibros();
         List<String> libros = datos.libros().stream()
               .filter(libro -> libro.idiomas().contains(idioma))
               .map(libro -> libro.titulo())
               .toList();
         if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
         } else {
            libros.forEach(System.out::println);
         }
      }
   }

   private void listarTop10Libros() {
      int sourceOption = elegirFuente();
      scanner.nextLine();

      if (sourceOption == 1) {
         System.out.println("Listando top 10 libros más descargados en la base de datos...");
         List<Libro> libros = libroRepository.findTop10ByOrderByNroDescargasDesc();
         libros.forEach(libro -> System.out.println(libro.getTitulo() + " - Descargas: " + libro.getNroDescargas()));
      } else if (sourceOption == 2) {
         System.out.println("Listando top 10 libros más descargados en la API...");
         Datos datos = obtenerLibros();
         List<String> libros = datos.libros().stream()
               .sorted(Comparator.comparing(DatosLibros::nroDescargas).reversed())
               .limit(10)
               .map(libro -> libro.titulo() + " - Descargas: " + libro.nroDescargas())
               .collect(Collectors.toList());
         libros.forEach(System.out::println);
      }
   }

   private void mostrarEstadisticas() {
      int sourceOption = elegirFuente();
      scanner.nextLine();

      if (sourceOption == 1) {
         System.out.println("Mostrando estadísticas de la base de datos...");
         List<Libro> libros = libroRepository.findAll();
         DoubleSummaryStatistics statistics = libros.stream()
               .collect(Collectors.summarizingDouble(Libro::getNroDescargas));
         System.out.println("Número total de descargas: " + statistics.getSum());
         System.out.println("Promedio de descargas: " + statistics.getAverage());
         System.out.println("Máximo de descargas: " + statistics.getMax());
         System.out.println("Mínimo de descargas: " + statistics.getMin());
      } else if (sourceOption == 2) {
         System.out.println("Mostrando estadísticas de la API...");
         Datos datos = obtenerLibros();
         DoubleSummaryStatistics statistics = datos.libros().stream()
               .collect(Collectors.summarizingDouble(DatosLibros::nroDescargas));
         System.out.println("Número total de descargas: " + statistics.getSum());
         System.out.println("Promedio de descargas: " + statistics.getAverage());
         System.out.println("Máximo de descargas: " + statistics.getMax());
         System.out.println("Mínimo de descargas: " + statistics.getMin());
      }
   }

   private Datos obtenerLibros() {
      System.out.println("Obteniendo libros desde la API...");
      var json = consumoAPI.obtenerDatos(URL_BASE);
      return convierteDatos.obtenerDatos(json, Datos.class);
   }

   private int elegirFuente() {
      int opcion = 0;
      boolean entradaValida = false;

      while (!entradaValida) {
         System.out.println("Elige la fuente de datos:");
         System.out.println("1 - Base de Datos");
         System.out.println("2 - API");
         System.out.print("Elige una opción: ");

         try {
            opcion = scanner.nextInt();
            if (opcion == 1 || opcion == 2) {
               entradaValida = true;
            } else {
               System.out.println("Opción inválida. Por favor, elige 1 o 2.");
            }
         } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingresa un número entero.");
            scanner.nextLine();
         }
      }
      return opcion;
   }

}
