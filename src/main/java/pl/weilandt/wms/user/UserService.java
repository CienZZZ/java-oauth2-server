package pl.weilandt.wms.user;

import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.weilandt.wms.exception.NoUserException;
import pl.weilandt.wms.exception.ResourceExistsException;
import pl.weilandt.wms.exception.ResourceNotFoundException;
import pl.weilandt.wms.user.role.Role;
import pl.weilandt.wms.user.role.RoleRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service("userService")
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roleByUserId = user.getRoles();
        final Set<GrantedAuthority> authorities = roleByUserId.stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString().toUpperCase())).collect(Collectors.toSet());
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = this.userRepository.findByNameIgnoreCase(login).orElse(null);

        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        if (user != null){
            builder = org.springframework.security.core.userdetails.User.withUsername(login);
            builder.password(user.getPassword());
            Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);
            builder.authorities(grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }


    public List<UserDTO> getAllUsers() {
        return List.ofAll(this.userRepository.findAll()).map(User::toUserDTO);
    }


    public UserDTO getUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        return  user.map( u-> u.toUserDTO()).orElseThrow(
                ()-> new NoUserException(id)
        );
    }


    public UserDTO createNew(NewUserDTO newUser) {
        if (this.userRepository.findByNameIgnoreCase(newUser.getName()).isPresent()){
            throw new ResourceExistsException(newUser.getName());
        } else {
            //java.util.List<RoleType> roleTypes = new ArrayList<>();
//            Set<Role> roleTypes = null;
//            newUser.getRoles().stream().map(role -> roleTypes.add(RoleType.valueOf(role.toString()));
//            newUser.roles = roleRepository.find(roleTypes);
            return this.userRepository.save(new User(        // TODO zapisuje uzytkownika z rolami, ale role rozroznia po ID, trzeba zrobic zeby po nazwie ?
                    newUser.name,
                    passwordEncoder.encode(newUser.password),
                    newUser.registerDate,
                    newUser.active,
                    newUser.changedPassword,
                    newUser.dateLastChange,
                    newUser.roles
            )).toUserDTO();
        }
    }


    public void delete(long userId) {
        if(!userRepository.findById(userId).isPresent()){
            throw new ResourceNotFoundException();
        } else {
            this.userRepository.deleteById(userId);
        }
    }


    public UserDTO changePassword(long id, String newPassword) {
        final Optional<User> user = this.userRepository.findById(id);
        return user.map(u->{
            u.setPassword(passwordEncoder.encode(newPassword));
            return u.toUserDTO();
        }).orElseThrow(
                ()-> new NoUserException(id)
        );
    }

    public UserDTO getUserByName(String name) {
        Optional<User> user = this.userRepository.findByNameIgnoreCase(name);
        return  user.map( u-> u.toUserDTO()).orElseThrow(
                ()-> new UsernameNotFoundException(name)
        );
    }
}
