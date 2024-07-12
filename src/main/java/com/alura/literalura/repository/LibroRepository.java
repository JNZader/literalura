package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

   @Transactional
   List<Libro> findByTituloContainingIgnoreCase(String titulo);

   List<Libro> findByIdiomasContaining(String idioma);

   @Query("SELECT l FROM Libro l ORDER BY l.nroDescargas DESC")
   List<Libro> findTop10ByOrderByNroDescargasDesc();

   @Query("SELECT l FROM Libro l JOIN l.autores a WHERE l.titulo = :titulo AND a.nombre IN :nombresAutores")
   List<Libro> findByTituloAndAutores(String titulo, List<String> nombresAutores);

   boolean existsById(Long id);

}
