package iso.muevetic.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import iso.muevetic.entities.SystemConfig;


@Repository
public interface SystemConfigRepository extends MongoRepository<SystemConfig, String> {
	
}
