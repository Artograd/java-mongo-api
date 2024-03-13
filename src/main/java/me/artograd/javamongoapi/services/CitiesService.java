package me.artograd.javamongoapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.artograd.javamongoapi.model.City;
import me.artograd.javamongoapi.repositories.CitiesRepository;

@Service
public class CitiesService {
	
	@Autowired
    private CitiesRepository cityRepository;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}
