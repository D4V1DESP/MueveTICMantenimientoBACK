package iso.muevetic;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@EnableMongoRepositories
@SpringBootApplication
@RestController
public class App {
	
	@Value("${frontend.url}")
	private String frontend;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@GetMapping("/")
	public RedirectView oauthRedirect() {
		return new RedirectView(frontend + "/oauth/redirect?continue");
	}

	@GetMapping("/login")
	public RedirectView loginRedirect()  {
		return new RedirectView(frontend + "/oauth/redirect?canceled");
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		return modelMapper;
	}

}
