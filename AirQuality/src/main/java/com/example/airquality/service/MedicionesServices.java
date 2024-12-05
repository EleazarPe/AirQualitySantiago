package com.example.airquality.service;

import com.example.airquality.model.Mediciones;
import com.example.airquality.repository.MedicionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MedicionesServices {

    @Autowired
    private MedicionesRepository medicionesRepository;

    @Transactional
    public void save(Mediciones mediciones) {
        medicionesRepository.save(mediciones);
    }
    @Transactional
    public List<Mediciones> buscarTodos() {
        return medicionesRepository.findAll();
    }
    @Transactional
    public List<Mediciones> buscarPorDispositivo(String dispositivo) {
        return medicionesRepository.findByDispositivo(dispositivo);
    }
    @Transactional
    public List<Mediciones> buscarPorEscala(String escala) {
        return medicionesRepository.findByEscala(escala);
    }
    @Transactional
    public List<Mediciones> buscarPorFecha(Date fecha) {
        return medicionesRepository.findAllByFecha(fecha);
    }
}
