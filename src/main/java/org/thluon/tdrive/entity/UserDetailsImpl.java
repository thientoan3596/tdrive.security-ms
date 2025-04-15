package org.thluon.tdrive.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table("user")
public class UserDetailsImpl implements UserDetails {
  @Id UUID id;
  String name;
  String email;

  @Column("hashed_password")
  String hashedPassword;

  @Column("is_locked")
  boolean isLocked;

  @Column("lock_reason")
  String lockReason;

  String role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
  }

  @Override
  public String getPassword() {
    return hashedPassword;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !isLocked;
  }
}
