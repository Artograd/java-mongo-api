package me.artograd.javamongoapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.artograd.javamongoapi.model.City;
import me.artograd.javamongoapi.services.CitiesService;
import me.artograd.javamongoapi.utils.CommonUtils;

@RestController
@RequestMapping("/cities")
public class CitiesController {
	
	@Autowired
    private CitiesService citiesService;

    @SuppressWarnings("unchecked")
	@GetMapping
    public ResponseEntity<City> getAllCountries() {
    	List<City> cities = citiesService.getAllCities();
        return (ResponseEntity<City>)CommonUtils.addCacheHeader(cities, 60);
    }
}

