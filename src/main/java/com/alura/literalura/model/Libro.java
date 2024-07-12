package com.alura.literalura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "libros")
public class Libro {

   @Id
   private Long id;

   @Column(name = "titulo", nullable = false)
   private String titulo;

   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinColumn(name = "libro_id")
   private List<Autor> autores;

   @ElementCollection
   @CollectionTable(name = "libro_idiomas", joinColumns = @JoinColumn(name = "libro_id"))
   @Column(name = "idioma")
   @Fetch(FetchMode.JOIN)
   private List<String> idiomas;

   @Column(name = "nro_descargas")
   private Long nroDescargas;
}
