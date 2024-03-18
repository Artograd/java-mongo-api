package me.artograd.javamongoapi.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import me.artograd.javamongoapi.model.Tender;

public interface TenderRepository extends MongoRepository<Tender, String> {
	
}
