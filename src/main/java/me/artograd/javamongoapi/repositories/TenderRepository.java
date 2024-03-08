package me.artograd.javamongoapi.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import me.artograd.javamongoapi.model.Tender;

public interface TenderRepository extends MongoRepository<Tender, String> {
    /*
	@Query("{ 'title': ?0, 'location': { $in: ?1 }, 'status': { $in: ?2 }, 'owner': ?3 }")
    List<Tender> findByTitleLocationStatusOwner(String title, List<String> locations, List<String> statuses, String owner);
    */
}
