package info.podlesov;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import info.podlesov.data.UserRepository;
import info.podlesov.model.User;

/**
 * This service binds users to user table in db
 */
@Service(value="simple_auth")
public class UserDetailsImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  public class MyUserDetails implements UserDetails {

    final private User u;
    MyUserDetails(User u) {
      this.u = u;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return Arrays.asList(new SimpleGrantedAuthority("write:topic"));
    }

    @Override
    public String getPassword() {
      return u.getPassword();
    }

    @Override
    public String getUsername() {
      return u.getName();
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

    public User getUser() {
      return u;
    }
  };
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User u = userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return new MyUserDetails(u);
  }
}
