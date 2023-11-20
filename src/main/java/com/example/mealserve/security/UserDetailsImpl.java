package com.example.mealserve.security;

import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {


    private final Account account;

    public Account getUser() {
        return account;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    public Long getUserId() {
        return account.getId();
    }

    public Store getStore() {
        return account.getStore();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = account.getRole().getAuthority();
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
