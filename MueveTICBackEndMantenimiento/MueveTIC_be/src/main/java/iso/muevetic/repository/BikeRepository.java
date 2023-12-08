package iso.muevetic.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import iso.muevetic.entities.Bike;

@Repository
public interface BikeRepository extends MongoRepository<Bike, String> {

}
