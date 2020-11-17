package info.podlesov.data;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import info.podlesov.model.Comment;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
  @Query(value = "from comment c join fetch c.user_id where c.topicId = :topicId",
  countQuery = "select count(c) from comment c where c.topicId = :topicId")
  Page<Comment> findByTopicId(@Param("topicId") Long topicId, Pageable pageable);

  @Query(value = "from comment c join fetch c.user_id where c.topicId = :topicId and c.id = :id")
  Optional<Comment> findByTopicIdandId(@Param("topicId") Long topicId, @Param("id") Long id);

  Boolean existsByTopicId(Long topicId);
}
