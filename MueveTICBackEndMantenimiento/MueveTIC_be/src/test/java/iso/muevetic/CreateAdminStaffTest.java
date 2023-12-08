package iso.muevetic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.microsoft.graph.requests.GraphServiceClient;

import iso.muevetic.domain.AdminModel;
import iso.muevetic.domain.DniModel;
import iso.muevetic.domain.MaintenanceStaffModel;
import iso.muevetic.entities.Admin;
import iso.muevetic.entities.MaintenanceStaff;
import iso.muevetic.services.AdminsService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import okhttp3.Request;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class CreateAdminStaffTest {

	@Autowired
	private AdminsService adminsService;
	
	@Autowired
	ModelMapper modelMapper;
	
	private Validator validator;
	
	public static String testId;
	
	@BeforeEach
	void getValidator() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@Test
	void testCreateAdmin(@Autowired GraphServiceClient<Request> graphClient) throws Exception {
		
		AdminModel adminModel = createAdminModel();
		validateAdminModel(adminModel);
		testId = adminsService.createAdmin(adminModel);
		
		System.out.println(testId);
		
		Admin added = (Admin) adminsService.getUserById(testId);
		
		assertThat(added.getDni()).isEqualTo(adminModel.getDni().getDni());
		assertThat(added.getName()).isEqualTo(adminModel.getName());
		assertThat(added.getSurname()).isEqualTo(adminModel.getSurname());
		assertThat(added.getCity()).isEqualTo(adminModel.getCity());
		
		cleanDB(graphClient);
	}
	
	@Test
	void testCreateDuplicateAdmin(@Autowired GraphServiceClient<Request> graphClient) throws Exception {
		
		AdminModel adminModel = createAdminModel();
		
		testId = adminsService.createAdmin(adminModel);
		assertThrows(Exception.class, () -> adminsService.createAdmin(adminModel));
		
		cleanDB(graphClient);
	}

	@Test
	void testShortAdminPassword() {
		AdminModel adminModel = createAdminModel();
		adminModel.setPassword("abc");
		
		assertThrows(Exception.class, () -> validateAdminModel(adminModel));
	}
	
	@Test
	void testAdminNullName() {
		AdminModel adminModel = createAdminModel();
		adminModel.setName(null);
		
		assertThrows(Exception.class, () -> validateAdminModel(adminModel));
	}
	
	@Test
	void testAdminNullSurname() {
		AdminModel adminModel = createAdminModel();
		adminModel.setSurname(null);
		
		assertThrows(Exception.class, () -> validateAdminModel(adminModel));
	}
	
	@Test
	void testCreateMaintenanceStaff(@Autowired GraphServiceClient<Request> graphClient) throws Exception {
		
		MaintenanceStaffModel staffModel = createMaintenanceStaff();
		validateStaffModel(staffModel);
		testId = adminsService.createMaintenanceStaff(staffModel);
		
		MaintenanceStaff added = (MaintenanceStaff) adminsService.getUserById(testId);
		
		assertThat(added.getDni()).isEqualTo(staffModel.getDni().getDni());
		assertThat(added.getName()).isEqualTo(staffModel.getName());
		assertThat(added.getSurname()).isEqualTo(staffModel.getSurname());
		assertThat(added.getCity()).isEqualTo(staffModel.getCity());
		assertThat(added.getDrivingLicense()).isEqualTo(staffModel.getDrivingLicense());
		assertThat(added.getExperience()).isEqualTo(staffModel.getExperience());
		
		cleanDB(graphClient);
	}
	
	@Test
	void testCreateDuplicateStaff(@Autowired GraphServiceClient<Request> graphClient) throws Exception {
		
		MaintenanceStaffModel staffModel = createMaintenanceStaff();
		validateStaffModel(staffModel);
		
		testId = adminsService.createMaintenanceStaff(staffModel);
		assertThrows(Exception.class, () -> adminsService.createMaintenanceStaff(staffModel));
		
		cleanDB(graphClient);
		
	}
	
	@Test
	void testStaffNullName() {
		MaintenanceStaffModel staffModel = createMaintenanceStaff();
		staffModel.setName(null);
		
		assertThrows(Exception.class, () -> validateStaffModel(staffModel));
	}
	
	@Test
	void testStaffNullSurname() {
		MaintenanceStaffModel staffModel = createMaintenanceStaff();
		staffModel.setSurname(null);
		
		assertThrows(Exception.class, () -> validateStaffModel(staffModel));
	}
	
	
	public static AdminModel createAdminModel() {
		AdminModel adminModel = new AdminModel();

		adminModel.setPassword("yZsKfR7QPx!vL@3b");
		
		adminModel.setDni(new DniModel("87654321Z"));
		adminModel.setName("AdminTestName");
		adminModel.setSurname("AdminTestSurname");
		adminModel.setCiy("ExampleCity");
		
		return adminModel;
	}
	
	public static MaintenanceStaffModel createMaintenanceStaff() {
		MaintenanceStaffModel staffModel = new MaintenanceStaffModel();

		staffModel.setPassword("gZsHfR9QPe-vL@3b");
		
		staffModel.setDni(new DniModel("12345679B"));
		staffModel.setName("StaffTestName");
		staffModel.setSurname("StaffTestSurname");
		staffModel.setCiy("TestCity");
		staffModel.setDrivingLicense("B");
		staffModel.setExperience(5);
		
		return staffModel;
		
	}
	
	private void validateAdminModel(AdminModel adminModel) throws Exception {
    	Set<ConstraintViolation<AdminModel>> violations = validator.validate(adminModel);

    	if (violations.size() != 0)
    		throw new Exception();
    }
	
	private void validateStaffModel(MaintenanceStaffModel staffModel) throws Exception {
    	Set<ConstraintViolation<MaintenanceStaffModel>> violations = validator.validate(staffModel);

    	if (violations.size() != 0)
    		throw new Exception();
    }
	
	void cleanDB(@Autowired GraphServiceClient<Request> graphClient) {
		graphClient.users(testId)
		.buildRequest()
		.delete();
	}
	
	
}
