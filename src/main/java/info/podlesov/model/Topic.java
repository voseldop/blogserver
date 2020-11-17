package info.podlesov.model;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Constraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Root level topic entry. Topics without comments can be deleted.
 */
@Data
@Entity(name="topic")
@Table(name="topic")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Validated
public class Topic {
  @JsonProperty("id")
  @ApiModelProperty(value = "")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonProperty("name")
  @ApiModelProperty(example = "Sample headline", required = true, value = "Topic headline")
  @Size(max = 250)
  @NotBlank
  @Column(name="name")
  private String name;

  @JsonProperty("body")
  @ApiModelProperty(example = "Sample text.", required = true, value = "Topic text")
  @NotNull
  @Size(max = 250)
  @Column(name="body")
  private String body;

  @ApiModelProperty(value = "Topic picture")
  @JsonIgnore
  @Column(name="photo")
  @Lob
  @Basic(fetch = FetchType.LAZY, optional = false)
  private Blob picture;

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
  private User author;

  @JsonProperty("author")
  @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, example = "admin", required = true, value = "Author name")
  @Transient
  public String getAuthorName() {
    return author == null ? null : author.getName();
  }
}

