package info.podlesov.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents User
 */

@Data
@Entity
@Table(name="user")
@NoArgsConstructor
public class User {
  @JsonProperty("id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonProperty("name")
  @NotNull
  @Column(name="name")
  @Size(max = 250)
  private String name;

  @JsonIgnore()
  @Column(name="password")
  @Size(max = 250)
  private String password;
}
