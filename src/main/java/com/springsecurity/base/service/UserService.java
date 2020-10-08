package com.springsecurity.base.service;

import com.springsecurity.base.exception.ResourceNotFoundException;
import com.springsecurity.base.model.User;
import com.springsecurity.base.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service class for {@link User} with business rules and validations.
 *
 * @author Gabriel Oliveira
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Integer id) throws ResourceNotFoundException {
        return this.verifyIfExists(id);
    }

    public User findLoggedUser() throws ResourceNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return this.userRepository.findByEmail(email).orElseThrow(ResourceNotFoundException::new);
    }

    private User verifyIfExists(Integer id) throws ResourceNotFoundException {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for the given ID"));
    }

}
