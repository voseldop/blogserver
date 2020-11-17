package info.podlesov.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import info.podlesov.api.CommentApi;
import info.podlesov.data.CommentRepository;
import info.podlesov.model.Comment;

/**
 * Trivial implementation of Comment Api
 */
@RestController
@Validated
public class CommentApiController implements CommentApi {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Page<Comment> getComment(Long topicId, Pageable page) {
        return commentRepository.findByTopicId(topicId, page);
    }

    public Comment postComment(Long topicId, String body) {
      Comment c = new Comment();
      c.setTopicId(topicId);
      c.setBody(body);
      c = commentRepository.save(c);
      return c;
    }

    public Comment putComment(Long topicId, Long id, String body) {
      Comment c = commentRepository.findByTopicIdandId(topicId, id).orElseThrow(() -> new ResourceNotFoundException());
      c.setBody(body);
      c = commentRepository.save(c);
      return c;
    }

}
