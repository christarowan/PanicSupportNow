package com.zipcode.panic.web.rest;

import com.zipcode.panic.domain.ActionPlan;
import com.zipcode.panic.repository.ActionPlanRepository;
import com.zipcode.panic.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.zipcode.panic.domain.ActionPlan}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ActionPlanResource {

    private final Logger log = LoggerFactory.getLogger(ActionPlanResource.class);

    private static final String ENTITY_NAME = "actionPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActionPlanRepository actionPlanRepository;

    public ActionPlanResource(ActionPlanRepository actionPlanRepository) {
        this.actionPlanRepository = actionPlanRepository;
    }

    /**
     * {@code POST  /action-plans} : Create a new actionPlan.
     *
     * @param actionPlan the actionPlan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actionPlan, or with status {@code 400 (Bad Request)} if the actionPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/action-plans")
    public ResponseEntity<ActionPlan> createActionPlan(@RequestBody ActionPlan actionPlan) throws URISyntaxException {
        log.debug("REST request to save ActionPlan : {}", actionPlan);
        if (actionPlan.getId() != null) {
            throw new BadRequestAlertException("A new actionPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActionPlan result = actionPlanRepository.save(actionPlan);
        return ResponseEntity
            .created(new URI("/api/action-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /action-plans/:id} : Updates an existing actionPlan.
     *
     * @param id the id of the actionPlan to save.
     * @param actionPlan the actionPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actionPlan,
     * or with status {@code 400 (Bad Request)} if the actionPlan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actionPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/action-plans/{id}")
    public ResponseEntity<ActionPlan> updateActionPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActionPlan actionPlan
    ) throws URISyntaxException {
        log.debug("REST request to update ActionPlan : {}, {}", id, actionPlan);
        if (actionPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actionPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actionPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ActionPlan result = actionPlanRepository.save(actionPlan);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, actionPlan.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /action-plans/:id} : Partial updates given fields of an existing actionPlan, field will ignore if it is null
     *
     * @param id the id of the actionPlan to save.
     * @param actionPlan the actionPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actionPlan,
     * or with status {@code 400 (Bad Request)} if the actionPlan is not valid,
     * or with status {@code 404 (Not Found)} if the actionPlan is not found,
     * or with status {@code 500 (Internal Server Error)} if the actionPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/action-plans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActionPlan> partialUpdateActionPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActionPlan actionPlan
    ) throws URISyntaxException {
        log.debug("REST request to partial update ActionPlan partially : {}, {}", id, actionPlan);
        if (actionPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actionPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actionPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActionPlan> result = actionPlanRepository
            .findById(actionPlan.getId())
            .map(existingActionPlan -> {
                return existingActionPlan;
            })
            .map(actionPlanRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, actionPlan.getId().toString())
        );
    }

    /**
     * {@code GET  /action-plans} : get all the actionPlans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actionPlans in body.
     */
    @GetMapping("/action-plans")
    public List<ActionPlan> getAllActionPlans() {
        log.debug("REST request to get all ActionPlans");
        return actionPlanRepository.findAll();
    }

    /**
     * {@code GET  /action-plans/:id} : get the "id" actionPlan.
     *
     * @param id the id of the actionPlan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actionPlan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/action-plans/{id}")
    public ResponseEntity<ActionPlan> getActionPlan(@PathVariable Long id) {
        log.debug("REST request to get ActionPlan : {}", id);
        Optional<ActionPlan> actionPlan = actionPlanRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(actionPlan);
    }

    /**
     * {@code DELETE  /action-plans/:id} : delete the "id" actionPlan.
     *
     * @param id the id of the actionPlan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/action-plans/{id}")
    public ResponseEntity<Void> deleteActionPlan(@PathVariable Long id) {
        log.debug("REST request to delete ActionPlan : {}", id);
        actionPlanRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
