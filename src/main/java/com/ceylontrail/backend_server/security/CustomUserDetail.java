package com.ceylontrail.backend_server.security;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@ToString(callSuper = true)
public class CustomUserDetail extends User {

    private final int id;


    public CustomUserDetail(String email, String password, Collection<? extends GrantedAuthority> authorities, int id) {
        super(email, password, authorities);
        this.id = id;
    }


    public CustomUserDetail(String email, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, int id) {
        super(email, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

}
