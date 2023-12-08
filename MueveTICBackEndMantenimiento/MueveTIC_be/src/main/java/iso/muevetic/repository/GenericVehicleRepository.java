package iso.muevetic.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import iso.muevetic.entities.GenericVehicle;


@Repository
public interface GenericVehicleRepository extends MongoRepository<GenericVehicle, String> {

}
