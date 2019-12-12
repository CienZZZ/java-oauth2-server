package pl.weilandt.wms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.weilandt.wms.user.NewUserDTO;
import pl.weilandt.wms.user.UserRepository;
import pl.weilandt.wms.user.UserService;
import pl.weilandt.wms.user.role.Role;
import pl.weilandt.wms.user.role.RoleRepository;
import pl.weilandt.wms.user.role.RoleType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class WmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WmsApplication.class, args);
	}

	@Bean
	CommandLineRunner initRolesIfNotPresent(RoleRepository roleRepository){
		return args -> {
			List<String> roles = new ArrayList<>();
			roles.add(RoleType.ADMIN.toString());
			Role roleAdmin = new Role();
			roleAdmin.setName(RoleType.ADMIN);
			roleAdmin.setDescription("Admin");
			roleAdmin.setCreatedOn(LocalDate.now());
			Role roleUser = new Role();
			roleUser.setName(RoleType.USER);
			roleUser.setDescription("User");
			roleUser.setCreatedOn(LocalDate.now());
			if (roleRepository.find(roles).isEmpty()){
				roleRepository.save(roleAdmin);
				roleRepository.save(roleUser);
			}
		};
	}

	@Bean
	CommandLineRunner initAdminIfNotPresent(UserRepository userRepository, UserService userService, RoleRepository roleRepository){
		return args -> {
			if(!userRepository.findByNameIgnoreCase("admin").isPresent()){
				java.util.List<String> roleStr = new ArrayList<>();
				roleStr.add(RoleType.ADMIN.toString());
				roleStr.add(RoleType.USER.toString());
				Set<Role> role = roleRepository.find(roleStr);
				userService.createNew(new NewUserDTO(
						"admin", "admin", LocalDate.now(), true, false, null, role
				));
			}
		};
	}
}
