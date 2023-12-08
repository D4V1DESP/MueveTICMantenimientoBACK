package iso.muevetic.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import iso.muevetic.entities.Car;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

}
