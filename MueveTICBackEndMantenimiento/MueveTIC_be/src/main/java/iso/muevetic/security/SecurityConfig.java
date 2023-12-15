package iso.muevetic.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;import com.azure.spring.cloud.autoconfigure.implementation.aadb2c.security.AadB2cOidcLoginConfigurer;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;

import iso.muevetic.entities.SystemConfig;
import iso.muevetic.enums.UserRole;
import iso.muevetic.services.ConfigService;
import okhttp3.Request;


@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Value("${frontend.url}")
	private String frontend;
	
	@Autowired 
	private AadB2cOidcLoginConfigurer configurer;
	
	@Autowired
    private ConfigService configService;

    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http, 	@Autowired JWTAuthorizationFilter authFilter) throws Exception {
    	return http
    		.csrf()
    			.disable()
    		.authorizeHttpRequests()
    			.requestMatchers("/admins/**")
    			.hasAnyAuthority(UserRole.ADMIN.name(),UserRole.TELEPHONEATTENTION.name())
    			.and()
        	.authorizeHttpRequests()
    			.requestMatchers(HttpMethod.GET, "/me/")
    			.hasAnyAuthority(UserRole.MEMBER.name(), UserRole.ADMIN.name(), UserRole.MAINTENANCE.name(), UserRole.TELEPHONEATTENTION.name())
    			.requestMatchers(HttpMethod.PATCH, "/me/")
    			.hasAnyAuthority(UserRole.MEMBER.name(), UserRole.ADMIN.name(), UserRole.TELEPHONEATTENTION.name())
       			.requestMatchers(HttpMethod.DELETE, "/me/")
    			.hasAnyAuthority(UserRole.MEMBER.name(), UserRole.ADMIN.name(), UserRole.TELEPHONEATTENTION.name())
    			.and()
            .authorizeHttpRequests()
            	.requestMatchers(HttpMethod.GET, "/vehicles/")
            	.authenticated()
            	.requestMatchers(HttpMethod.GET, "/vehicles/*")
            	.authenticated()
            	.and()
            .authorizeHttpRequests()
            	.requestMatchers("/vehicles/**")
            	.hasAnyAuthority(UserRole.ADMIN.name(), UserRole.TELEPHONEATTENTION.name())
            	.and()
    		.authorizeHttpRequests()
    			.requestMatchers("/me/**")
    			.hasAnyAuthority(UserRole.MEMBER.name(), UserRole.TELEPHONEATTENTION.name())
    			.and()
    			.authorizeHttpRequests().requestMatchers("/me/oauth/login/").permitAll().and()
        	.authorizeHttpRequests()
    			.requestMatchers("/", "/login")
    			.permitAll()
    			.and()
        	.authorizeHttpRequests()
    			.anyRequest()
    			.permitAll()
    			.and()
        	.cors()
    			.and()
    		.apply(this.configurer)
    			.and()
    		.oauth2Login()
    			.loginPage("/login")
    			.and()
    		.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
    		.build();
    }

	@Bean
	GraphServiceClient<Request> newGraphClient() {

		SystemConfig config = configService.getConfig();
		
		final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
	        	.clientId(config.getClientId())
	        	.clientSecret(config.getClientSecret())
	        	.tenantId(config.getTenantId())
	        	.build();

        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(Arrays.asList("https://graph.microsoft.com/.default"), clientSecretCredential);

        return GraphServiceClient.builder()
        	.authenticationProvider(tokenCredentialAuthProvider)
        	.buildClient();
	}

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(frontend));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
