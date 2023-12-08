package iso.muevetic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import iso.muevetic.entities.Reserve;
import iso.muevetic.enums.ReserveState;


@Repository
public interface ReserveRepository extends MongoRepository<Reserve, String> {

	public Reserve findByVehicle(String licensePlate);

	public List<Reserve> findByUserId(String userId);
	
	public List<Reserve> findAllByVehicle(String licensePlate);
	
	public Optional<Reserve> findByUserIdAndState(String userId, ReserveState state);

	public Reserve findByVehicleAndState(String licensePlate, ReserveState activa);

}
