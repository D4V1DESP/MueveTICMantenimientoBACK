package iso.muevetic.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.Request;

import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.DirectoryObject;
import com.microsoft.graph.requests.DirectoryObjectCollectionWithReferencesPage;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserRequestBuilder;

import iso.muevetic.entities.Admin;
import iso.muevetic.entities.GenericUser;
import iso.muevetic.entities.MaintenanceStaff;
import iso.muevetic.entities.SystemConfig;
import iso.muevetic.entities.User;
import iso.muevetic.services.ConfigService;


@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
    private ConfigService configService;

	@Autowired
	private GraphServiceClient<Request> graphClient;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
			final OAuth2User user = ((OAuth2AuthenticationToken) authentication).getPrincipal();

		    try {
		    	UserRequestBuilder req = this.graphClient.users(user.getName());
		    	DirectoryObjectCollectionWithReferencesPage groups = req.memberOf().buildRequest().get();
		    	List<DirectoryObject> roles = groups.getCurrentPage();

		    	String role = "";

		    	if (!roles.isEmpty()) {
		    		role = roles.get(0).id;
		    	}

		    	GenericUser generic = null;

		    	SystemConfig config = configService.getConfig();
		    	
    			if (role.equals(config.getAdminRoleId())) {
		    		generic = new Admin(user);
					request.setAttribute("admin", generic);
		    	} else if (role.equals(config.getMaintenanceStaffRoleId())) {
		    		generic = new MaintenanceStaff(user);
					request.setAttribute("maintenance", generic);
		    	} else {
		    		generic = new User(user);
					request.setAttribute("user", generic);
		    	}
	    		
	    		SecurityContextHolder.getContext().setAuthentication(
	    				new OAuth2AuthenticationToken(
	    						user, 
	    						List.of(new SimpleGrantedAuthority(generic.getRole())), 
	    						((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId()
	    						)
	    				);
	    		request.setAttribute("generic", generic);
		    } catch(ClientException e) {
		    	e.printStackTrace();
		    	authentication.setAuthenticated(false);
		    }
		}

		filterChain.doFilter(request, response);
	}

}
