package iso.muevetic.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iso.muevetic.domain.ConfigModel;
import iso.muevetic.entities.SystemConfig;
import iso.muevetic.exceptions.MissingSystemConfigException;
import iso.muevetic.repository.SystemConfigRepository;

@Service
public class ConfigService {
	
	@Autowired
	SystemConfigRepository configRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	private static final String MUEVETIC_CONFIG = "MueveTIC_config";

	public SystemConfig getConfig() {
		
		try {
			
			Optional<SystemConfig> configOptional = configRepository.findById(MUEVETIC_CONFIG);
	    	
	    	if(!configOptional.isPresent()) {
	    		throw new MissingSystemConfigException();
			}
	    	
	    	return configOptional.get();
	    	
		} catch (MissingSystemConfigException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SystemConfig updateConfig(ConfigModel configModel) {
		
		SystemConfig config = getConfig();

        modelMapper.map(configModel, config);
        configRepository.save(config);

        return config;
	}
	
}
