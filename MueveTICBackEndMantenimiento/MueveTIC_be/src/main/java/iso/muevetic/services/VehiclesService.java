package iso.muevetic.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iso.muevetic.domain.BikeModel;
import iso.muevetic.domain.CarModel;
import iso.muevetic.domain.RateModel;
import iso.muevetic.domain.ScooterModel;
import iso.muevetic.domain.VehicleUpdateModel;
import iso.muevetic.entities.Bike;
import iso.muevetic.entities.Car;
import iso.muevetic.entities.GenericVehicle;
import iso.muevetic.entities.Reserve;
import iso.muevetic.entities.Scooter;
import iso.muevetic.entities.SystemConfig;
import iso.muevetic.enums.ReserveState;
import iso.muevetic.enums.VehicleState;
import iso.muevetic.exceptions.ConflictInDBException;
import iso.muevetic.exceptions.NotAllowedException;
import iso.muevetic.exceptions.NotFoundException;
import iso.muevetic.repository.BikeRepository;
import iso.muevetic.repository.CarRepository;
import iso.muevetic.repository.GenericVehicleRepository;
import iso.muevetic.repository.ReserveRepository;
import iso.muevetic.repository.ScooterRepository;
import jakarta.validation.Valid;


@Service
public class VehiclesService {

	@Autowired
	CarRepository carRepository;

	@Autowired
	BikeRepository bikeRepository;

	@Autowired
	ScooterRepository scooterRepository;

	@Autowired
	GenericVehicleRepository genericVehicleRepository;
	
	@Autowired
	ReserveRepository reserveRepository;
	
	@Autowired
	GenericVehicleRepository vehiclesRepository;

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
    private ConfigService configService;

	public void createCar(@Valid CarModel carModel) throws ConflictInDBException {
		Car car = this.modelMapper.map(carModel, Car.class);
		car.setType(1);
		if (vehiclesRepository.existsById(car.getLicensePlate()))
			throw new ConflictInDBException();

		this.vehiclesRepository.save(car);
	}

	public void createBike(@Valid BikeModel bikeModel) throws ConflictInDBException {
		Bike bike = this.modelMapper.map(bikeModel, Bike.class);
		bike.setType(2);
		if (vehiclesRepository.existsById(bike.getLicensePlate()))
			throw new ConflictInDBException();

		this.vehiclesRepository.save(bike);
	}

	public void createScooter(@Valid ScooterModel scooterModel) throws ConflictInDBException {
		Scooter scooter = this.modelMapper.map(scooterModel, Scooter.class);
		scooter.setType(3);
		if (scooterRepository.existsById(scooter.getLicensePlate()))
			throw new ConflictInDBException();
		
		this.scooterRepository.save(scooter);
		
	}

	public GenericVehicle getVehicle(String licensePlate) throws NotFoundException {
		Optional<GenericVehicle> optionalVehicle = vehiclesRepository.findById(licensePlate);
		
		if (optionalVehicle.isEmpty())
			throw new NotFoundException();
		
		return optionalVehicle.get();
	}

	public Reserve getActiveReserveOfUser(String userId) {
		Optional<Reserve> optionalReserve = reserveRepository.findByUserIdAndState(userId, ReserveState.ACTIVA);

		if (optionalReserve.isEmpty())
			return null;

		return optionalReserve.get();
	}

	public void deactivate(String licensePlate) throws NotFoundException, ConflictInDBException {
		GenericVehicle vehicle = getVehicle(licensePlate);

        if (vehicle == null)
            throw new NotFoundException();
        
        if (vehicle.getStatus() == VehicleState.EN_USO)
        	throw new ConflictInDBException();
        
        vehicle.setDeactivated(true);
    	vehicle.setLicensePlate(UUID.randomUUID().toString());
    	List<Reserve> reserves = reserveRepository.findAllByVehicle(licensePlate);
    	reserves.forEach(reserve -> {
    		reserve.setVehicle(vehicle);
    		reserveRepository.save(reserve);
    	});
    	vehiclesRepository.save(vehicle);
    	vehiclesRepository.deleteById(licensePlate);
	}

	public List<GenericVehicle> getVehicles() {
		return vehiclesRepository.findAll();
	}

	public Reserve newReserve(String id, String licensePlate) throws NotAllowedException, NotFoundException, ConflictInDBException {	
		if (getActiveReserveOfUser(id) != null)
			throw new NotAllowedException();

		GenericVehicle vehicle = getVehicle(licensePlate);

		Reserve actualReserve = reserveRepository.findByVehicleAndState(licensePlate, ReserveState.ACTIVA);

		if (actualReserve != null)
			throw new ConflictInDBException();
		
		Reserve reserve = new Reserve();
		
		reserve.setStart(new Date(java.lang.System.currentTimeMillis()));
		reserve.setState(ReserveState.ACTIVA);

		vehicle.setStatus(VehicleState.EN_USO);
		
		reserve.setUserId(id);
		reserve.setVehicle(vehicle);

		reserveRepository.save(reserve);
		vehiclesRepository.save(vehicle);

		return reserve;
	}

	public Reserve setReserveStateFromUser(String userId, ReserveState state) throws NotFoundException {
		Reserve activeReserve = getActiveReserveOfUser(userId);
		if (activeReserve == null)
			throw new NotFoundException();
		
		activeReserve.setEnd(new Date(java.lang.System.currentTimeMillis()));

		activeReserve.setState(state);
		GenericVehicle vehicle = activeReserve.getVehicle();

		int battery = vehicle.getBattery();
		
		SystemConfig config = configService.getConfig();
		
		if (battery - config.getBatteryPerRide() <= config.getMinBattery()) {
			vehicle.setStatus(VehicleState.PENDIENTE_CARGA);
		} else {
			vehicle.setStatus(VehicleState.DISPONIBLE);
		}
		
		vehicle.setBattery(battery - config.getBatteryPerRide());
		
    	vehiclesRepository.save(vehicle);
		reserveRepository.save(activeReserve);
		
		return activeReserve;
	}

	public void update(String licensePlate, VehicleUpdateModel vehicleUpdateModel) {
		if (vehicleUpdateModel.getType() == 1) {
			Optional<Car> optionalCar = carRepository.findById(licensePlate);
			if (optionalCar.isPresent()) {
				Car car = optionalCar.get();
				modelMapper.map(vehicleUpdateModel, car);
				carRepository.save(car);
			}
		}else if (vehicleUpdateModel.getType() == 2) {
			Optional<Bike> optionalBike = bikeRepository.findById(licensePlate);
			if (optionalBike.isPresent()) {
				Bike bike = optionalBike.get();
				modelMapper.map(vehicleUpdateModel, bike);
				bikeRepository.save(bike);
			}
		}else if (vehicleUpdateModel.getType() == 3) {
			Optional<Scooter> optionalScooter = scooterRepository.findById(licensePlate);
			if (optionalScooter.isPresent()) {
				Scooter scooter = optionalScooter.get();
				modelMapper.map(vehicleUpdateModel, scooter);
				scooterRepository.save(scooter);
			}
		}
	}

	public List<Reserve> getAllReservesOfUser(String id) {
		return this.reserveRepository.findByUserId(id);
	}

	public void setRating(String id, @Valid RateModel rateModel) throws NotFoundException {
		Optional<Reserve> optionalReserve = this.reserveRepository.findById(id);
		
		if (optionalReserve.isEmpty())
			throw new NotFoundException();
		
		Reserve reserve = optionalReserve.get(); 
		
		reserve.setComment(rateModel.getComment());
		reserve.setRating(rateModel.getRating());
		
		this.reserveRepository.save(reserve);
	}

	public List<Reserve> getAllReserves() {
		return this.reserveRepository.findAll();
	}
}
