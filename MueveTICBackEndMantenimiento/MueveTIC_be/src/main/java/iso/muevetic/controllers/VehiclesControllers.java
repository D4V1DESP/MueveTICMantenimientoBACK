package iso.muevetic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import iso.muevetic.domain.BikeModel;
import iso.muevetic.domain.CarModel;
import iso.muevetic.domain.ScooterModel;
import iso.muevetic.domain.VehicleUpdateModel;
import iso.muevetic.entities.GenericVehicle;
import iso.muevetic.exceptions.ConflictInDBException;
import iso.muevetic.exceptions.NotFoundException;
import iso.muevetic.services.VehiclesService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("vehicles")
public class VehiclesControllers {

	@Autowired
	VehiclesService vehiclesService;

	@PostMapping("/createCar/")
	public void createCar(@Valid @RequestBody CarModel carModel) {
		try {
			this.vehiclesService.createCar(carModel);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/createBike/")
	public void createBike(@Valid @RequestBody BikeModel bikeModel) {
		try {
			this.vehiclesService.createBike(bikeModel);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		} 
	}
	
	@PostMapping("/createScooter/")
	public void createScooter(@Valid @RequestBody ScooterModel scooterModel) {
		try {
			this.vehiclesService.createScooter(scooterModel);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}
	

	@PutMapping("/{licensePlate}")
	public void deactivate(@PathVariable String licensePlate) {
		try {
			this.vehiclesService.deactivate(licensePlate);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/{licensePlate}")
	public Object getVehicleInfo(@PathVariable String licensePlate) {
		try {
			return this.vehiclesService.getVehicle(licensePlate);
			
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/")
	public List<GenericVehicle> getVehicles() {
		return this.vehiclesService.getVehicles();
	}
	
	@PatchMapping("/{licensePlate}")
	public void update(@PathVariable @Valid String licensePlate, @Valid @RequestBody VehicleUpdateModel vehicleUpdateModel) {
		this.vehiclesService.update(licensePlate, vehicleUpdateModel);
	}
}
