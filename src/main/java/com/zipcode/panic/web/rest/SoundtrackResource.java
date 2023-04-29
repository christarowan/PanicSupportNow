package com.zipcode.panic.web.rest;

import com.zipcode.panic.domain.Soundtrack;
import com.zipcode.panic.repository.SoundtrackRepository;
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
 * REST controller for managing {@link com.zipcode.panic.domain.Soundtrack}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SoundtrackResource {

    private final Logger log = LoggerFactory.getLogger(SoundtrackResource.class);

    private static final String ENTITY_NAME = "soundtrack";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SoundtrackRepository soundtrackRepository;

    public SoundtrackResource(SoundtrackRepository soundtrackRepository) {
        this.soundtrackRepository = soundtrackRepository;
    }

    /**
     * {@code POST  /soundtracks} : Create a new soundtrack.
     *
     * @param soundtrack the soundtrack to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new soundtrack, or with status {@code 400 (Bad Request)} if the soundtrack has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/soundtracks")
    public ResponseEntity<Soundtrack> createSoundtrack(@RequestBody Soundtrack soundtrack) throws URISyntaxException {
        log.debug("REST request to save Soundtrack : {}", soundtrack);
        if (soundtrack.getId() != null) {
            throw new BadRequestAlertException("A new soundtrack cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Soundtrack result = soundtrackRepository.save(soundtrack);
        return ResponseEntity
            .created(new URI("/api/soundtracks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /soundtracks/:id} : Updates an existing soundtrack.
     *
     * @param id the id of the soundtrack to save.
     * @param soundtrack the soundtrack to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soundtrack,
     * or with status {@code 400 (Bad Request)} if the soundtrack is not valid,
     * or with status {@code 500 (Internal Server Error)} if the soundtrack couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/soundtracks/{id}")
    public ResponseEntity<Soundtrack> updateSoundtrack(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Soundtrack soundtrack
    ) throws URISyntaxException {
        log.debug("REST request to update Soundtrack : {}, {}", id, soundtrack);
        if (soundtrack.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soundtrack.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soundtrackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Soundtrack result = soundtrackRepository.save(soundtrack);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, soundtrack.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /soundtracks/:id} : Partial updates given fields of an existing soundtrack, field will ignore if it is null
     *
     * @param id the id of the soundtrack to save.
     * @param soundtrack the soundtrack to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soundtrack,
     * or with status {@code 400 (Bad Request)} if the soundtrack is not valid,
     * or with status {@code 404 (Not Found)} if the soundtrack is not found,
     * or with status {@code 500 (Internal Server Error)} if the soundtrack couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/soundtracks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Soundtrack> partialUpdateSoundtrack(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Soundtrack soundtrack
    ) throws URISyntaxException {
        log.debug("REST request to partial update Soundtrack partially : {}, {}", id, soundtrack);
        if (soundtrack.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soundtrack.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soundtrackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Soundtrack> result = soundtrackRepository
            .findById(soundtrack.getId())
            .map(existingSoundtrack -> {
                if (soundtrack.getFileName() != null) {
                    existingSoundtrack.setFileName(soundtrack.getFileName());
                }
                if (soundtrack.getName() != null) {
                    existingSoundtrack.setName(soundtrack.getName());
                }

                return existingSoundtrack;
            })
            .map(soundtrackRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, soundtrack.getId().toString())
        );
    }

    /**
     * {@code GET  /soundtracks} : get all the soundtracks.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of soundtracks in body.
     */
    @GetMapping("/soundtracks")
    public List<Soundtrack> getAllSoundtracks(@RequestParam(required = false) String filter) {
        if ("actionplan-is-null".equals(filter)) {
            log.debug("REST request to get all Soundtracks where actionPlan is null");
            return StreamSupport
                .stream(soundtrackRepository.findAll().spliterator(), false)
                .filter(soundtrack -> soundtrack.getActionPlan() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Soundtracks");
        return soundtrackRepository.findAll();
    }

    /**
     * {@code GET  /soundtracks/:id} : get the "id" soundtrack.
     *
     * @param id the id of the soundtrack to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the soundtrack, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/soundtracks/{id}")
    public ResponseEntity<Soundtrack> getSoundtrack(@PathVariable Long id) {
        log.debug("REST request to get Soundtrack : {}", id);
        Optional<Soundtrack> soundtrack = soundtrackRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(soundtrack);
    }

    /**
     * {@code DELETE  /soundtracks/:id} : delete the "id" soundtrack.
     *
     * @param id the id of the soundtrack to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/soundtracks/{id}")
    public ResponseEntity<Void> deleteSoundtrack(@PathVariable Long id) {
        log.debug("REST request to delete Soundtrack : {}", id);
        soundtrackRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
