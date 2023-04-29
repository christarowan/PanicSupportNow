package com.zipcode.panic.web.rest;

import com.zipcode.panic.domain.CopingStrategies;
import com.zipcode.panic.repository.CopingStrategiesRepository;
import com.zipcode.panic.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.zipcode.panic.domain.CopingStrategies}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CopingStrategiesResource {

    private final Logger log = LoggerFactory.getLogger(CopingStrategiesResource.class);

    private static final String ENTITY_NAME = "copingStrategies";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CopingStrategiesRepository copingStrategiesRepository;

    public CopingStrategiesResource(CopingStrategiesRepository copingStrategiesRepository) {
        this.copingStrategiesRepository = copingStrategiesRepository;
    }

    /**
     * {@code POST  /coping-strategies} : Create a new copingStrategies.
     *
     * @param copingStrategies the copingStrategies to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new copingStrategies, or with status {@code 400 (Bad Request)} if the copingStrategies has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coping-strategies")
    public ResponseEntity<CopingStrategies> createCopingStrategies(@RequestBody CopingStrategies copingStrategies)
        throws URISyntaxException {
        log.debug("REST request to save CopingStrategies : {}", copingStrategies);
        if (copingStrategies.getId() != null) {
            throw new BadRequestAlertException("A new copingStrategies cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CopingStrategies result = copingStrategiesRepository.save(copingStrategies);
        return ResponseEntity
            .created(new URI("/api/coping-strategies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coping-strategies/:id} : Updates an existing copingStrategies.
     *
     * @param id the id of the copingStrategies to save.
     * @param copingStrategies the copingStrategies to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated copingStrategies,
     * or with status {@code 400 (Bad Request)} if the copingStrategies is not valid,
     * or with status {@code 500 (Internal Server Error)} if the copingStrategies couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coping-strategies/{id}")
    public ResponseEntity<CopingStrategies> updateCopingStrategies(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CopingStrategies copingStrategies
    ) throws URISyntaxException {
        log.debug("REST request to update CopingStrategies : {}, {}", id, copingStrategies);
        if (copingStrategies.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, copingStrategies.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!copingStrategiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CopingStrategies result = copingStrategiesRepository.save(copingStrategies);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, copingStrategies.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /coping-strategies/:id} : Partial updates given fields of an existing copingStrategies, field will ignore if it is null
     *
     * @param id the id of the copingStrategies to save.
     * @param copingStrategies the copingStrategies to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated copingStrategies,
     * or with status {@code 400 (Bad Request)} if the copingStrategies is not valid,
     * or with status {@code 404 (Not Found)} if the copingStrategies is not found,
     * or with status {@code 500 (Internal Server Error)} if the copingStrategies couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/coping-strategies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CopingStrategies> partialUpdateCopingStrategies(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CopingStrategies copingStrategies
    ) throws URISyntaxException {
        log.debug("REST request to partial update CopingStrategies partially : {}, {}", id, copingStrategies);
        if (copingStrategies.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, copingStrategies.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!copingStrategiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CopingStrategies> result = copingStrategiesRepository
            .findById(copingStrategies.getId())
            .map(existingCopingStrategies -> {
                if (copingStrategies.getContents() != null) {
                    existingCopingStrategies.setContents(copingStrategies.getContents());
                }
                if (copingStrategies.getName() != null) {
                    existingCopingStrategies.setName(copingStrategies.getName());
                }

                return existingCopingStrategies;
            })
            .map(copingStrategiesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, copingStrategies.getId().toString())
        );
    }

    /**
     * {@code GET  /coping-strategies} : get all the copingStrategies.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of copingStrategies in body.
     */
    @GetMapping("/coping-strategies")
    public List<CopingStrategies> getAllCopingStrategies(@RequestParam(required = false) String filter) {
        if ("actionplan-is-null".equals(filter)) {
            log.debug("REST request to get all CopingStrategiess where actionPlan is null");
            return StreamSupport
                .stream(copingStrategiesRepository.findAll().spliterator(), false)
                .filter(copingStrategies -> copingStrategies.getActionPlan() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all CopingStrategies");
        return copingStrategiesRepository.findAll();
    }

    /**
     * {@code GET  /coping-strategies/:id} : get the "id" copingStrategies.
     *
     * @param id the id of the copingStrategies to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the copingStrategies, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coping-strategies/{id}")
    public ResponseEntity<CopingStrategies> getCopingStrategies(@PathVariable Long id) {
        log.debug("REST request to get CopingStrategies : {}", id);
        Optional<CopingStrategies> copingStrategies = copingStrategiesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(copingStrategies);
    }

    /**
     * {@code DELETE  /coping-strategies/:id} : delete the "id" copingStrategies.
     *
     * @param id the id of the copingStrategies to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coping-strategies/{id}")
    public ResponseEntity<Void> deleteCopingStrategies(@PathVariable Long id) {
        log.debug("REST request to delete CopingStrategies : {}", id);
        copingStrategiesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
