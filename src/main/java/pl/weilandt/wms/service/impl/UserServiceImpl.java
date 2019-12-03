package pl.weilandt.wms.service.impl;

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
import pl.weilandt.wms.dto.UserDTO;
import pl.weilandt.wms.exception.NoUserException;
import pl.weilandt.wms.model.Role;
import pl.weilandt.wms.model.User;
import pl.weilandt.wms.repository.UserRepository;
import pl.weilandt.wms.service.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
        User user = userRepository.findByNameIgnoreCase(login).orElse(null);

//        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
//        if (user != null){
//            builder = org.springframework.security.core.userdetails.User.withUsername(login);
//            builder.password(user.getPassword());
//            Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);
//            builder.authorities(grantedAuthorities);
//        } else {
//            throw new UsernameNotFoundException("User not found.");
//        }
//
//        return builder.build();

//        User user = userDao.findByUsername(userId);
        if(user == null){
            logger.error("Invalid username or password.");
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);


        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), grantedAuthorities);
    }

    @Override
    public UserDTO save(UserDTO user) {
        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return List.ofAll(this.userRepository.findAll()).map(User::toUserDTO);
    }

    @Override
    public UserDTO getUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        return  user.map( u-> u.toUserDTO()).orElseThrow(
                ()-> new NoUserException(id)
        );
    }

    @Override
    public void delete(long id) {

    }
}
