package com.zipcode.panic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zipcode.panic.IntegrationTest;
import com.zipcode.panic.domain.CopingStrategies;
import com.zipcode.panic.repository.CopingStrategiesRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CopingStrategiesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CopingStrategiesResourceIT {

    private static final String DEFAULT_CONTENTS = "AAAAAAAAAA";
    private static final String UPDATED_CONTENTS = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/coping-strategies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CopingStrategiesRepository copingStrategiesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCopingStrategiesMockMvc;

    private CopingStrategies copingStrategies;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CopingStrategies createEntity(EntityManager em) {
        CopingStrategies copingStrategies = new CopingStrategies().contents(DEFAULT_CONTENTS).name(DEFAULT_NAME);
        return copingStrategies;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CopingStrategies createUpdatedEntity(EntityManager em) {
        CopingStrategies copingStrategies = new CopingStrategies().contents(UPDATED_CONTENTS).name(UPDATED_NAME);
        return copingStrategies;
    }

    @BeforeEach
    public void initTest() {
        copingStrategies = createEntity(em);
    }

    @Test
    @Transactional
    void createCopingStrategies() throws Exception {
        int databaseSizeBeforeCreate = copingStrategiesRepository.findAll().size();
        // Create the CopingStrategies
        restCopingStrategiesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(copingStrategies))
            )
            .andExpect(status().isCreated());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeCreate + 1);
        CopingStrategies testCopingStrategies = copingStrategiesList.get(copingStrategiesList.size() - 1);
        assertThat(testCopingStrategies.getContents()).isEqualTo(DEFAULT_CONTENTS);
        assertThat(testCopingStrategies.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCopingStrategiesWithExistingId() throws Exception {
        // Create the CopingStrategies with an existing ID
        copingStrategies.setId(1L);

        int databaseSizeBeforeCreate = copingStrategiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCopingStrategiesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(copingStrategies))
            )
            .andExpect(status().isBadRequest());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCopingStrategies() throws Exception {
        // Initialize the database
        copingStrategiesRepository.saveAndFlush(copingStrategies);

        // Get all the copingStrategiesList
        restCopingStrategiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(copingStrategies.getId().intValue())))
            .andExpect(jsonPath("$.[*].contents").value(hasItem(DEFAULT_CONTENTS)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCopingStrategies() throws Exception {
        // Initialize the database
        copingStrategiesRepository.saveAndFlush(copingStrategies);

        // Get the copingStrategies
        restCopingStrategiesMockMvc
            .perform(get(ENTITY_API_URL_ID, copingStrategies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(copingStrategies.getId().intValue()))
            .andExpect(jsonPath("$.contents").value(DEFAULT_CONTENTS))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCopingStrategies() throws Exception {
        // Get the copingStrategies
        restCopingStrategiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCopingStrategies() throws Exception {
        // Initialize the database
        copingStrategiesRepository.saveAndFlush(copingStrategies);

        int databaseSizeBeforeUpdate = copingStrategiesRepository.findAll().size();

        // Update the copingStrategies
        CopingStrategies updatedCopingStrategies = copingStrategiesRepository.findById(copingStrategies.getId()).get();
        // Disconnect from session so that the updates on updatedCopingStrategies are not directly saved in db
        em.detach(updatedCopingStrategies);
        updatedCopingStrategies.contents(UPDATED_CONTENTS).name(UPDATED_NAME);

        restCopingStrategiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCopingStrategies.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCopingStrategies))
            )
            .andExpect(status().isOk());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeUpdate);
        CopingStrategies testCopingStrategies = copingStrategiesList.get(copingStrategiesList.size() - 1);
        assertThat(testCopingStrategies.getContents()).isEqualTo(UPDATED_CONTENTS);
        assertThat(testCopingStrategies.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCopingStrategies() throws Exception {
        int databaseSizeBeforeUpdate = copingStrategiesRepository.findAll().size();
        copingStrategies.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCopingStrategiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, copingStrategies.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(copingStrategies))
            )
            .andExpect(status().isBadRequest());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCopingStrategies() throws Exception {
        int databaseSizeBeforeUpdate = copingStrategiesRepository.findAll().size();
        copingStrategies.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCopingStrategiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(copingStrategies))
            )
            .andExpect(status().isBadRequest());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCopingStrategies() throws Exception {
        int databaseSizeBeforeUpdate = copingStrategiesRepository.findAll().size();
        copingStrategies.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCopingStrategiesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(copingStrategies))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCopingStrategiesWithPatch() throws Exception {
        // Initialize the database
        copingStrategiesRepository.saveAndFlush(copingStrategies);

        int databaseSizeBeforeUpdate = copingStrategiesRepository.findAll().size();

        // Update the copingStrategies using partial update
        CopingStrategies partialUpdatedCopingStrategies = new CopingStrategies();
        partialUpdatedCopingStrategies.setId(copingStrategies.getId());

        restCopingStrategiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCopingStrategies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCopingStrategies))
            )
            .andExpect(status().isOk());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeUpdate);
        CopingStrategies testCopingStrategies = copingStrategiesList.get(copingStrategiesList.size() - 1);
        assertThat(testCopingStrategies.getContents()).isEqualTo(DEFAULT_CONTENTS);
        assertThat(testCopingStrategies.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCopingStrategiesWithPatch() throws Exception {
        // Initialize the database
        copingStrategiesRepository.saveAndFlush(copingStrategies);

        int databaseSizeBeforeUpdate = copingStrategiesRepository.findAll().size();

        // Update the copingStrategies using partial update
        CopingStrategies partialUpdatedCopingStrategies = new CopingStrategies();
        partialUpdatedCopingStrategies.setId(copingStrategies.getId());

        partialUpdatedCopingStrategies.contents(UPDATED_CONTENTS).name(UPDATED_NAME);

        restCopingStrategiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCopingStrategies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCopingStrategies))
            )
            .andExpect(status().isOk());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeUpdate);
        CopingStrategies testCopingStrategies = copingStrategiesList.get(copingStrategiesList.size() - 1);
        assertThat(testCopingStrategies.getContents()).isEqualTo(UPDATED_CONTENTS);
        assertThat(testCopingStrategies.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCopingStrategies() throws Exception {
        int databaseSizeBeforeUpdate = copingStrategiesRepository.findAll().size();
        copingStrategies.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCopingStrategiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, copingStrategies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(copingStrategies))
            )
            .andExpect(status().isBadRequest());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCopingStrategies() throws Exception {
        int databaseSizeBeforeUpdate = copingStrategiesRepository.findAll().size();
        copingStrategies.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCopingStrategiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(copingStrategies))
            )
            .andExpect(status().isBadRequest());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCopingStrategies() throws Exception {
        int databaseSizeBeforeUpdate = copingStrategiesRepository.findAll().size();
        copingStrategies.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCopingStrategiesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(copingStrategies))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CopingStrategies in the database
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCopingStrategies() throws Exception {
        // Initialize the database
        copingStrategiesRepository.saveAndFlush(copingStrategies);

        int databaseSizeBeforeDelete = copingStrategiesRepository.findAll().size();

        // Delete the copingStrategies
        restCopingStrategiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, copingStrategies.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CopingStrategies> copingStrategiesList = copingStrategiesRepository.findAll();
        assertThat(copingStrategiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
