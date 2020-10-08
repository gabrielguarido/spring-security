package com.springsecurity.base.security;

import com.springsecurity.base.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;

/**
 * Spring Security's system user information.
 */
public class SystemUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    private User user;

    public SystemUser(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemUser)) return false;
        if (!super.equals(o)) return false;
        SystemUser that = (SystemUser) o;
        return getUser().equals(that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUser());
    }

}
