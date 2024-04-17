package com.example.service.users;

import com.example.models.UserEntity;
import com.example.respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = (UserEntity) userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("El usuario "+ username +" no existe!"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_".
                concat(userEntity.getRol().getRol().toString()));

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(authority);

        Collection<? extends GrantedAuthority> authorities = authorityList;
        return new User(userEntity.getUsername(),userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities);

    }
}
