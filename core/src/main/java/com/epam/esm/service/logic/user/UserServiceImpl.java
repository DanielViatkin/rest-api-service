package com.epam.esm.service.logic.user;

import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.RoleRepositoryImpl;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_ROLE = "USER";
    private static final String ACTIVE_STATUS = "ACTIVE";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Validator<User> userValidator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepository,
                           UserValidator userValidator,
                           RoleRepositoryImpl roleRepository,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getById(long id) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User authUser = userRepository.findByLogin(login).get();
        String roleName =authUser.getRoles().iterator().next().getName();

        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundEntityException("user.not.found", id));
        if (!isAdmin(roleName)){
            isSelfCheck(user.getLogin());
        }
        return user;
    }

    private void isSelfCheck(String userDbLogin){

        String loginFromJwt = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!loginFromJwt.equals(userDbLogin)){
            throw new NoAuthoritiesException("no.self.check", "40301");
        }
    }

    @Override
    public List<User> getAll(int page, int size) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(login).get();
        String roleName =user.getRoles().iterator().next().getName();
        isAdminElseThrow(roleName);
        Pageable pageable = getPageble(page, size);
        return userRepository.findAll(pageable);
    }

    private void isAdminElseThrow(String roleName){
        if(!isAdmin(roleName)){
            throw new NoAuthoritiesException("no.self.check", "40301");
        }
    }
    private boolean isAdmin(String roleName){
        return roleName.equals(com.epam.esm.model.security.Role.ADMIN.name());
    }

    private Pageable getPageble(int page, int size){
        Pageable pageble;
        try {
            pageble = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidPagebaleParametersException("pageable.invalid.data",e);
        }
        return pageble;
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("user.not.found.login"));
    }

    @Override
    public User create(User user) {
        validateUser(user);

        userRepository.findByLogin(user.getLogin()).
                ifPresent(s -> {throw new EntityAlreadyExistException("user.already.exist");
        });


        return createUser(user);
    }

    private User createUser(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Optional<Role> role = roleRepository.findByName(USER_ROLE);
        if (!role.isPresent()){
            throw new NotFoundEntityException("role.not.found", USER_ROLE);
        }
        user.setRoles(Collections.singleton(role.get()));
        user.setStatus(ACTIVE_STATUS);

        return userRepository.create(user);
    }

    private void validateUser(User user){
        if (!userValidator.isValid(user)){
            throw new InvalidUserException("invalid.user.login", user.getLogin());
        }
    }

    @Override
    public void changePassword(String login, String currentPassword, String newPassword) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("user.not.found.login"));
        verifyPassword(user.getPassword(), currentPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.update(user);
    }

    private void verifyPassword(String userPassword, String frontPassword){
        if (!passwordEncoder.matches(frontPassword, userPassword)){
            throw new InvalidUserPassword("invalid.user.password");
        }
    }
}
