package info.podlesov.data;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import info.podlesov.model.Topic;

public interface TopicRepository extends PagingAndSortingRepository<Topic, Long> {
  @Query(value = "from topic t join fetch t.author",
          countQuery = "select count(t) from topic t")
  Page<Topic> findAll(Pageable pageable);

  @Query(value = "from topic t join fetch  t.author where t.id = :topicId")
  Optional<Topic> findById(@Param("topicId") Long id);
}
