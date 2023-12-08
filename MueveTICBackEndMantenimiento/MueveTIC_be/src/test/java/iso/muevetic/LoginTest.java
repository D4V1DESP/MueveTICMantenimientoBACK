package iso.muevetic;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class LoginTest {/*

	@Autowired
	private MockMvc server;
	
	@Autowired
	private UserRepository repo;

	private final String DEFAULT_DNI = "12345678A";
	private final String DEFAULT_PASSWORD = "123456789";
	private final String DEFAULT_EMAIL = "email@email.com";

	@BeforeEach
	void insertDefaultUser() {
		User user = new User();
		
		//user.setDni(DEFAULT_DNI);
		user.setEmail(DEFAULT_EMAIL);

		//user.setPassword(new BCryptPasswordEncoder().encode(DEFAULT_PASSWORD));
		
		repo.deleteAll();
		
		repo.save(user);
	}

	@Test
	void contextLoads() throws Exception {
		assertThat(server).isNotNull();
		assertThat(repo.findById(DEFAULT_EMAIL)).isPresent();
	}

    @Test
    void testInvalidLogin() throws Exception {
    	ResultActions loginResult = doLoginRequest("notanemail", "12345678");
    	loginResult.andExpect(status().isUnauthorized());

    	loginResult = doLoginRequest(DEFAULT_EMAIL, "wrongpassword");
    	loginResult.andExpect(status().isUnauthorized());
    	
    	loginResult = doLoginRequest("non@existing.email", DEFAULT_PASSWORD);
    	loginResult.andExpect(status().isUnauthorized());
    }

    @Test 
    void testValidLogin() throws Exception
    {
    	ResultActions loginResult = doLoginRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    	loginResult.andExpect(status().isOk());
    }

    private ResultActions doLoginRequest(String email, String password) throws Exception {
    	JSONObject payload = new JSONObject()
    			.put("email", email)
    			.put("password", password);

    	RequestBuilder request = MockMvcRequestBuilders
    			.post("/users/login/")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(payload.toString());

    	return this.server.perform(request);
    }

*/}
