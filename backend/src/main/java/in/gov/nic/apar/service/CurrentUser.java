package in.gov.nic.apar.service;

import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component;
@Component public class CurrentUser { public String email(){return SecurityContextHolder.getContext().getAuthentication().getName();} public boolean authority(){return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_AUTHORITY"));} }
