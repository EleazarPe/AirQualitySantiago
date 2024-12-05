package com.example.airquality.repository;

import com.example.airquality.model.Mediciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface MedicionesRepository extends JpaRepository<Mediciones, Long> {
    List<Mediciones> findAll();
    List<Mediciones> findByDispositivo(String dispositivo);
    List<Mediciones> findByEscala(String escala);
    List<Mediciones> findAllByFecha(Date fecha);

}
