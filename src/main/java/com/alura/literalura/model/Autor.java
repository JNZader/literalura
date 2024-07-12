package com.alura.literalura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "autores")
public class Autor {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "nombre", nullable = false)
   private String nombre;

   @Column(name = "fecha_nacimiento")
   private Integer fechaNacimiento;

   @Column(name = "fecha_muerte")
   private Integer fechaMuerte;

}