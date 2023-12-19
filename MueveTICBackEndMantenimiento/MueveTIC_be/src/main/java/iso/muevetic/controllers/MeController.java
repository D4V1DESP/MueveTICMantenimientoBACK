package iso.muevetic.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import iso.muevetic.services.AdminsService;
import iso.muevetic.services.VehiclesService;
import jakarta.validation.Valid;
import iso.muevetic.domain.RateModel;
import iso.muevetic.domain.UserUpdateModel;
import iso.muevetic.entities.GenericUser;
import iso.muevetic.entities.Reserve;
import iso.muevetic.entities.User;
import iso.muevetic.enums.ReserveState;
import iso.muevetic.exceptions.ConflictInDBException;
import iso.muevetic.exceptions.NotAllowedException;
import iso.muevetic.exceptions.NotFoundException;


@RestController
@RequestMapping("me")
public class MeController {

	@Autowired
	VehiclesService vehiclesService;

	@Autowired
	AdminsService adminsService;

	@GetMapping("/reserved")
	public Reserve getReserved(@RequestAttribute User user) {
		return this.vehiclesService.getActiveReserveOfUser(user.getId());
	}
	
	@GetMapping("/reserved/all")
	public List<Reserve> getAllReserves(@RequestAttribute User user) {
		return this.vehiclesService.getAllReservesOfUser(user.getId());
	}

	@DeleteMapping("/reserved/cancel")
	public void deleteReserve(@RequestAttribute User user) {
		try {
			this.vehiclesService.setReserveStateFromUser(user.getId(), ReserveState.CANCELADA);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("reserved/cancelTelefonica/{idUsuario}")
	public void deleteReserve(@PathVariable String idUsuario) {
		try {
			this.vehiclesService.setReserveStateFromUser(idUsuario, ReserveState.CANCELADA);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	@PatchMapping("/reserved/finalize")
	public void finalizeReserve(@RequestAttribute User user, @Valid @RequestBody RateModel rateModel) {
		try {
			Reserve reserve = this.vehiclesService.setReserveStateFromUser(user.getId(), ReserveState.FINALIZADA);
			this.vehiclesService.setRating(reserve.getId(), rateModel);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/reserved/allReserve")
	public List<Reserve> getAllReserves(){
		try {
			return this.vehiclesService.getAllReserves();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/reserve/{licensePlate}")
	public Reserve postReserve(@RequestAttribute GenericUser user, @PathVariable String licensePlate) {
		try {
			return this.vehiclesService.newReserve(user.getId(), licensePlate);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		} catch (NotAllowedException e) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@PostMapping("/reserveAttT/{licensePlate}")
	public Reserve postReserveAttT(@RequestBody Map<String,Object>info ,@PathVariable String licensePlate) {
		try {
			return this.vehiclesService.newReserve(info.get("id").toString(), licensePlate);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		} catch (NotAllowedException e) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
		}
	}
 
	@GetMapping("/")
	public GenericUser me(@RequestAttribute GenericUser generic) {
		return generic;
	}

	@DeleteMapping("/")
	public void delete(@RequestAttribute GenericUser generic) {
		try {
			this.adminsService.deleteUser(generic.getId());
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/")
	public GenericUser patch(@RequestAttribute GenericUser generic, @Valid @RequestBody UserUpdateModel userUpdateModel) {
		try {
			return this.adminsService.updateUser(generic.getId(), userUpdateModel);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

}
