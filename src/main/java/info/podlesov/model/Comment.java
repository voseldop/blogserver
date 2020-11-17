package info.podlesov.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represent comment for the topic. Can't be deleted but can be updated.
 * No nesting comments support
 */
@Data
@Entity(name="comment")
@Table(name="comment")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {
  @JsonProperty("id")
  @ApiModelProperty(hidden = true)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @ApiModelProperty(hidden = true)
  private Long topicId;

  @JsonProperty("body")
  @ApiModelProperty(value = "Comment body", required = true)
  @Size(max = 250)
  @NotBlank
  private String body;

  @CreatedDate
  @Column(name="created_date")
  @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, value = "Creation date")
  private LocalDateTime created_date;

  @LastModifiedDate
  @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, value = "Modification date")
  @Column(name="modified_date")
  private LocalDateTime modified_date;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(referencedColumnName = "id", name = "user_id")
  @CreatedBy
  @ApiModelProperty(hidden = true)
  private User user_id;

  @JsonProperty("author")
  @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, example = "Admin", required = true, value = "Author name")
  @Transient
  public String getUserName() {
    return user_id == null ? null : user_id.getName();
  }
}

