package iso.muevetic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.microsoft.graph.models.PasswordProfile;
import com.microsoft.graph.requests.GraphServiceClient;

import iso.muevetic.domain.DniModel;
import iso.muevetic.domain.UserUpdateModel;
import iso.muevetic.entities.GenericUser;
import iso.muevetic.entities.User;
import iso.muevetic.services.AdminsService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import okhttp3.Request;

import java.util.UUID;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UpdateUserTest {

	@Autowired
	private AdminsService adminsService;

	private Validator validator;

	public static User testUser;

	@BeforeAll
	public static void insertBefore(@Autowired GraphServiceClient<Request> graphClient) {
		testUser = new User();

		testUser.setDni("12345678A");
		testUser.setName("test");
		testUser.setSurname(UUID.randomUUID().toString());
		testUser.setDrivingLicense("licencia");
		testUser.setTelephone(123456789);

		PasswordProfile passwordProfile = new PasswordProfile();
		passwordProfile.forceChangePasswordNextSignIn = false;
		passwordProfile.password = "xWwvJ]6NMw+bWH-d";

		com.microsoft.graph.models.User azureUser = testUser.toAzureUser();
		
		azureUser.accountEnabled = true;

		azureUser.passwordProfile = passwordProfile;

		azureUser = graphClient.users()
			.buildRequest()
			.post(azureUser);

		testUser = new User(azureUser);
	}
	
	@BeforeEach
	void getValidator() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	void testUpdate() throws Exception {
    	UserUpdateModel updateModel = new UserUpdateModel();

    	Date birthdate = new Date(java.lang.System.currentTimeMillis());
    	String surname = UUID.randomUUID().toString();

    	updateModel.setBirthdate(birthdate);
    	updateModel.setName("test_new");
    	updateModel.setSurname(surname);
    	updateModel.setTelephone(100000000);
    	updateModel.setDni(new DniModel("12345678A"));

		User serviceUpdated = (User) doUpdateRequestAdmin(testUser.getId(), updateModel);

		User updated = (User) adminsService.getUserById(testUser.getId());

		assertThat(updated.getBirthdate()).isEqualTo(updateModel.getBirthdate());
		assertThat(updated.getName()).isEqualTo(updateModel.getName());
		assertThat(updated.getSurname()).isEqualTo(updateModel.getSurname());
		assertThat(updated.getTelephone()).isEqualTo(updateModel.getTelephone());

		assertThat(updated.getBirthdate()).isEqualTo(serviceUpdated.getBirthdate());
		assertThat(updated.getName()).isEqualTo(serviceUpdated.getName());
		assertThat(updated.getSurname()).isEqualTo(serviceUpdated.getSurname());
		assertThat(updated.getTelephone()).isEqualTo(serviceUpdated.getTelephone());

		assertThat(updated.getDni()).isEqualTo(serviceUpdated.getDni());
	}

	@Test
	void testUpdateValidationTelephone() {
		UserUpdateModel updateModel = new UserUpdateModel();

    	updateModel.setTelephone(99999999);
    	
    	assertThrows(Exception.class, () -> validateModel(updateModel));

    	updateModel.setTelephone(1000000000);
    	
    	assertThrows(Exception.class, () -> validateModel(updateModel));
	}

	@Test
	void testUpdateValidationDni() {
    	UserUpdateModel updateModel = new UserUpdateModel();

    	updateModel.setDni(new DniModel("a00000000"));

    	assertThrows(Exception.class, () -> validateModel(updateModel));

    	updateModel.setDni(new DniModel("0000000a"));

    	assertThrows(Exception.class, () -> validateModel(updateModel));
	}
	
	@Test
	void testUpdateValidationDate() throws Exception {
    	UserUpdateModel updateModel = new UserUpdateModel();

    	updateModel.setBirthdate(new java.util.Date(946681200000L));

    	validateModel(updateModel);

    	updateModel.setBirthdate(new java.util.Date(4070905200000L));

    	assertThrows(Exception.class, () -> validateModel(updateModel));
	}

	@AfterAll
	public static void afterAll(@Autowired GraphServiceClient<Request> graphClient) {
		graphClient.users(testUser.getId())
			.buildRequest()
			.delete();
	}

    private GenericUser doUpdateRequestAdmin(String id, UserUpdateModel updateModel) throws Exception {
    	validateModel(updateModel);
    	return adminsService.updateUser(testUser.getId(), updateModel);
    }
    
    private void validateModel(UserUpdateModel updateModel) throws Exception {
    	Set<ConstraintViolation<UserUpdateModel>> violations = validator.validate(updateModel);

    	if (violations.size() != 0)
    		throw new Exception();
    }

}
