package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

   @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :yearEnd AND (a.fechaMuerte IS NULL OR a.fechaMuerte >= :yearStart)")
   List<Autor> findAuthorsAliveInYear(@Param("yearStart") int yearStart, @Param("yearEnd") int yearEnd);
}
