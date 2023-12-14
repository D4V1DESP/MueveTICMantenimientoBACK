package iso.muevetic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import iso.muevetic.entities.Reserve;
import iso.muevetic.entities.User;
import iso.muevetic.services.AdminsService;
import iso.muevetic.services.VehiclesService;





@Controller
@RequestMapping("attention")
public class TelephoneControllers {

	@Autowired 
	AdminsService adminService;
	@Autowired 
	VehiclesService vehiclesService;
	@GetMapping ("/getUser")
	public Object getUserByMail(@PathVariable String id ) {
		
		try {
				return this.adminService.getUsersByMail(id).getEmail().equals(id); 
		
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}
	
	
	@GetMapping("/getReservesUser")
	public List<Reserve> getAllReserves(@RequestAttribute User user) {
		return this.vehiclesService.getAllReservesOfUser(user.getId());
	}
	
}
