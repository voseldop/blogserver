package info.podlesov.rest;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import info.podlesov.api.TopicApi;
import info.podlesov.data.CommentRepository;
import info.podlesov.data.TopicRepository;
import info.podlesov.model.Topic;

/**
 * Trivial implementation of Topic Api
 * Topic can be created via JSON Post or Multipart upload.
 */
@RestController
@Validated
public class TopicApiController implements TopicApi {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public void deleteTopic(Long topicId) {
        try {
            if (commentRepository.existsByTopicId(topicId)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Can't delete topic with comments");
            }
            topicRepository.deleteById(topicId);
        } catch (EmptyResultDataAccessException ex) {
            // We can just return noContent here to make delete idempotent.
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase());
        }
    }

    @Override
    public Page<Topic> getTopic(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    @Override
    public Topic getTopicById(Long topicId) {
         return topicRepository.findById(topicId).orElseThrow(() ->  new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

    @Override
    public Resource getPictureTopicById(Long topicId) throws SQLException {
        Topic target = topicRepository.findById(topicId).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        Blob blob = target.getPicture();

        if (blob == null) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        return new ByteArrayResource(blob.getBytes(0, (int)blob.length()));
    }

    @Override
    public Topic postTopic(String name, String body, MultipartFile file) throws IOException {
        Topic.TopicBuilder builder = Topic.builder().name(name).body(body);
        // File is optional
        if (file != null) {
            if (file.getSize() > 300 * 1024 * 1024) {
                throw new FileSizeLimitExceededException("File size limit exceeded", file.getSize(), 300 * 1024 * 1024);
            }
            builder.picture(BlobProxy.generateProxy(file.getBytes()));
        }
        Topic result = topicRepository.save(builder.build());
        return result;
    }

    @Override
    public Topic postTopicJson(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public Topic updateTopic(Long topicId, String name, String body, MultipartFile file) throws IOException {
        Topic target = topicRepository.findById(topicId).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        if (name != null) {
            target.setName(name);
        }
        if (body != null) {
            target.setBody(body);
        }
        if (file != null) {
            // TBD: Check if picture format is valid
            target.setPicture(BlobProxy.generateProxy(file.getBytes()));
        }
        topicRepository.save(target);
        return target;
    }
}
