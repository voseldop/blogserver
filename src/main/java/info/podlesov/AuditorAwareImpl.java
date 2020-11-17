package info.podlesov;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import info.podlesov.data.UserRepository;
import info.podlesov.model.User;

/**
 * This class converts security context to user entry audit in comment and topic
 */
public class AuditorAwareImpl implements AuditorAware<User> {
  @Autowired
  private UserRepository userRepository;

  @Override
  public Optional<User> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal()!=null) {
      return Optional.of(((UserDetailsImpl.MyUserDetails)authentication.getPrincipal()).getUser());
    }
    if (authentication.getName() != null) {
      return userRepository.findByName(authentication.getName());
    }
    return Optional.empty();
  }
}
