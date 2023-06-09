package com.zipcode.panic.web.rest;

import com.zipcode.panic.domain.PhoneLink;
import com.zipcode.panic.repository.PhoneLinkRepository;
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
 * REST controller for managing {@link com.zipcode.panic.domain.PhoneLink}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PhoneLinkResource {

    private final Logger log = LoggerFactory.getLogger(PhoneLinkResource.class);

    private static final String ENTITY_NAME = "phoneLink";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhoneLinkRepository phoneLinkRepository;

    public PhoneLinkResource(PhoneLinkRepository phoneLinkRepository) {
        this.phoneLinkRepository = phoneLinkRepository;
    }

    /**
     * {@code POST  /phone-links} : Create a new phoneLink.
     *
     * @param phoneLink the phoneLink to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phoneLink, or with status {@code 400 (Bad Request)} if the phoneLink has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/phone-links")
    public ResponseEntity<PhoneLink> createPhoneLink(@RequestBody PhoneLink phoneLink) throws URISyntaxException {
        log.debug("REST request to save PhoneLink : {}", phoneLink);
        if (phoneLink.getId() != null) {
            throw new BadRequestAlertException("A new phoneLink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhoneLink result = phoneLinkRepository.save(phoneLink);
        return ResponseEntity
            .created(new URI("/api/phone-links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /phone-links/:id} : Updates an existing phoneLink.
     *
     * @param id the id of the phoneLink to save.
     * @param phoneLink the phoneLink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneLink,
     * or with status {@code 400 (Bad Request)} if the phoneLink is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phoneLink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/phone-links/{id}")
    public ResponseEntity<PhoneLink> updatePhoneLink(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhoneLink phoneLink
    ) throws URISyntaxException {
        log.debug("REST request to update PhoneLink : {}, {}", id, phoneLink);
        if (phoneLink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneLink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhoneLink result = phoneLinkRepository.save(phoneLink);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phoneLink.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /phone-links/:id} : Partial updates given fields of an existing phoneLink, field will ignore if it is null
     *
     * @param id the id of the phoneLink to save.
     * @param phoneLink the phoneLink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneLink,
     * or with status {@code 400 (Bad Request)} if the phoneLink is not valid,
     * or with status {@code 404 (Not Found)} if the phoneLink is not found,
     * or with status {@code 500 (Internal Server Error)} if the phoneLink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/phone-links/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhoneLink> partialUpdatePhoneLink(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhoneLink phoneLink
    ) throws URISyntaxException {
        log.debug("REST request to partial update PhoneLink partially : {}, {}", id, phoneLink);
        if (phoneLink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneLink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhoneLink> result = phoneLinkRepository
            .findById(phoneLink.getId())
            .map(existingPhoneLink -> {
                if (phoneLink.getNumber() != null) {
                    existingPhoneLink.setNumber(phoneLink.getNumber());
                }
                if (phoneLink.getName() != null) {
                    existingPhoneLink.setName(phoneLink.getName());
                }

                return existingPhoneLink;
            })
            .map(phoneLinkRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phoneLink.getId().toString())
        );
    }

    /**
     * {@code GET  /phone-links} : get all the phoneLinks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phoneLinks in body.
     */
    @GetMapping("/phone-links")
    public List<PhoneLink> getAllPhoneLinks() {
        log.debug("REST request to get all PhoneLinks");
        return phoneLinkRepository.findAll();
    }

    /**
     * {@code GET  /phone-links/:id} : get the "id" phoneLink.
     *
     * @param id the id of the phoneLink to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phoneLink, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/phone-links/{id}")
    public ResponseEntity<PhoneLink> getPhoneLink(@PathVariable Long id) {
        log.debug("REST request to get PhoneLink : {}", id);
        Optional<PhoneLink> phoneLink = phoneLinkRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(phoneLink);
    }

    /**
     * {@code DELETE  /phone-links/:id} : delete the "id" phoneLink.
     *
     * @param id the id of the phoneLink to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/phone-links/{id}")
    public ResponseEntity<Void> deletePhoneLink(@PathVariable Long id) {
        log.debug("REST request to delete PhoneLink : {}", id);
        phoneLinkRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
