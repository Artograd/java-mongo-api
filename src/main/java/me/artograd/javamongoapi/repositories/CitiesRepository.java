package me.artograd.javamongoapi.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import me.artograd.javamongoapi.model.City;

public interface CitiesRepository extends MongoRepository<City, String> {
    // No additional methods required for basic CRUD operations
}