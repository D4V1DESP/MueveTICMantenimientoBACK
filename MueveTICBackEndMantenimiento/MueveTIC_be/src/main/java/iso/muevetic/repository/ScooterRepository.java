package iso.muevetic.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import iso.muevetic.entities.Scooter;

@Repository
public interface ScooterRepository extends MongoRepository<Scooter, String> {

}
