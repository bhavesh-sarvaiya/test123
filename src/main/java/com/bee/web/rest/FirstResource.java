package com.bee.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bee.domain.First;

import com.bee.repository.FirstRepository;
import com.bee.web.rest.errors.BadRequestAlertException;
import com.bee.web.rest.util.HeaderUtil;
import com.bee.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing First.
 */
@RestController
@RequestMapping("/api")
public class FirstResource {

    private final Logger log = LoggerFactory.getLogger(FirstResource.class);

    private static final String ENTITY_NAME = "first";

    private final FirstRepository firstRepository;

    public FirstResource(FirstRepository firstRepository) {
        this.firstRepository = firstRepository;
    }

    /**
     * POST  /firsts : Create a new first.
     *
     * @param first the first to create
     * @return the ResponseEntity with status 201 (Created) and with body the new first, or with status 400 (Bad Request) if the first has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/firsts")
    @Timed
    public ResponseEntity<First> createFirst(@RequestBody First first) throws URISyntaxException {
        log.debug("REST request to save First : {}", first);
        if (first.getId() != null) {
            throw new BadRequestAlertException("A new first cannot already have an ID", ENTITY_NAME, "idexists");
        }
        First result = firstRepository.save(first);
        return ResponseEntity.created(new URI("/api/firsts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /firsts : Updates an existing first.
     *
     * @param first the first to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated first,
     * or with status 400 (Bad Request) if the first is not valid,
     * or with status 500 (Internal Server Error) if the first couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/firsts")
    @Timed
    public ResponseEntity<First> updateFirst(@RequestBody First first) throws URISyntaxException {
        log.debug("REST request to update First : {}", first);
        if (first.getId() == null) {
            return createFirst(first);
        }
        First result = firstRepository.save(first);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, first.getId().toString()))
            .body(result);
    }

    /**
     * GET  /firsts : get all the firsts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of firsts in body
     */
    @GetMapping("/firsts")
    @Timed
    public ResponseEntity<List<First>> getAllFirsts(Pageable pageable) {
        log.debug("REST request to get a page of Firsts");
        Page<First> page = firstRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/firsts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /firsts/:id : get the "id" first.
     *
     * @param id the id of the first to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the first, or with status 404 (Not Found)
     */
    @GetMapping("/firsts/{id}")
    @Timed
    public ResponseEntity<First> getFirst(@PathVariable Long id) {
        log.debug("REST request to get First : {}", id);
        First first = firstRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(first));
    }

    /**
     * DELETE  /firsts/:id : delete the "id" first.
     *
     * @param id the id of the first to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/firsts/{id}")
    @Timed
    public ResponseEntity<Void> deleteFirst(@PathVariable Long id) {
        log.debug("REST request to delete First : {}", id);
        firstRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
