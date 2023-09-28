package mk.ukim.finki.akreditacii.config;

import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class FacultyUserDetails implements UserDetails {

    private User user;


    private Professor professor;

    private String password;

    public FacultyUserDetails(User user, String password) {
        this.user = user;
        this.password = password;
    }

    public FacultyUserDetails(User user, Professor professor, String password) {
        this.user = user;
        this.professor = professor;
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().applicationRole.roleName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return user.getId();
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

    public Professor getProfessor() {
        return professor;
    }

}
