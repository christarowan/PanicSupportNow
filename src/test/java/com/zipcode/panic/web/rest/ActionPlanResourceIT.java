package com.zipcode.panic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zipcode.panic.IntegrationTest;
import com.zipcode.panic.domain.ActionPlan;
import com.zipcode.panic.repository.ActionPlanRepository;
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
 * Integration tests for the {@link ActionPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActionPlanResourceIT {

    private static final String ENTITY_API_URL = "/api/action-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActionPlanRepository actionPlanRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActionPlanMockMvc;

    private ActionPlan actionPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActionPlan createEntity(EntityManager em) {
        ActionPlan actionPlan = new ActionPlan();
        return actionPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActionPlan createUpdatedEntity(EntityManager em) {
        ActionPlan actionPlan = new ActionPlan();
        return actionPlan;
    }

    @BeforeEach
    public void initTest() {
        actionPlan = createEntity(em);
    }

    @Test
    @Transactional
    void createActionPlan() throws Exception {
        int databaseSizeBeforeCreate = actionPlanRepository.findAll().size();
        // Create the ActionPlan
        restActionPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actionPlan)))
            .andExpect(status().isCreated());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeCreate + 1);
        ActionPlan testActionPlan = actionPlanList.get(actionPlanList.size() - 1);
    }

    @Test
    @Transactional
    void createActionPlanWithExistingId() throws Exception {
        // Create the ActionPlan with an existing ID
        actionPlan.setId(1L);

        int databaseSizeBeforeCreate = actionPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actionPlan)))
            .andExpect(status().isBadRequest());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActionPlans() throws Exception {
        // Initialize the database
        actionPlanRepository.saveAndFlush(actionPlan);

        // Get all the actionPlanList
        restActionPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actionPlan.getId().intValue())));
    }

    @Test
    @Transactional
    void getActionPlan() throws Exception {
        // Initialize the database
        actionPlanRepository.saveAndFlush(actionPlan);

        // Get the actionPlan
        restActionPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, actionPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(actionPlan.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingActionPlan() throws Exception {
        // Get the actionPlan
        restActionPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActionPlan() throws Exception {
        // Initialize the database
        actionPlanRepository.saveAndFlush(actionPlan);

        int databaseSizeBeforeUpdate = actionPlanRepository.findAll().size();

        // Update the actionPlan
        ActionPlan updatedActionPlan = actionPlanRepository.findById(actionPlan.getId()).get();
        // Disconnect from session so that the updates on updatedActionPlan are not directly saved in db
        em.detach(updatedActionPlan);

        restActionPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActionPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedActionPlan))
            )
            .andExpect(status().isOk());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeUpdate);
        ActionPlan testActionPlan = actionPlanList.get(actionPlanList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingActionPlan() throws Exception {
        int databaseSizeBeforeUpdate = actionPlanRepository.findAll().size();
        actionPlan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actionPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActionPlan() throws Exception {
        int databaseSizeBeforeUpdate = actionPlanRepository.findAll().size();
        actionPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActionPlan() throws Exception {
        int databaseSizeBeforeUpdate = actionPlanRepository.findAll().size();
        actionPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actionPlan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActionPlanWithPatch() throws Exception {
        // Initialize the database
        actionPlanRepository.saveAndFlush(actionPlan);

        int databaseSizeBeforeUpdate = actionPlanRepository.findAll().size();

        // Update the actionPlan using partial update
        ActionPlan partialUpdatedActionPlan = new ActionPlan();
        partialUpdatedActionPlan.setId(actionPlan.getId());

        restActionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActionPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActionPlan))
            )
            .andExpect(status().isOk());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeUpdate);
        ActionPlan testActionPlan = actionPlanList.get(actionPlanList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateActionPlanWithPatch() throws Exception {
        // Initialize the database
        actionPlanRepository.saveAndFlush(actionPlan);

        int databaseSizeBeforeUpdate = actionPlanRepository.findAll().size();

        // Update the actionPlan using partial update
        ActionPlan partialUpdatedActionPlan = new ActionPlan();
        partialUpdatedActionPlan.setId(actionPlan.getId());

        restActionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActionPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActionPlan))
            )
            .andExpect(status().isOk());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeUpdate);
        ActionPlan testActionPlan = actionPlanList.get(actionPlanList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingActionPlan() throws Exception {
        int databaseSizeBeforeUpdate = actionPlanRepository.findAll().size();
        actionPlan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, actionPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActionPlan() throws Exception {
        int databaseSizeBeforeUpdate = actionPlanRepository.findAll().size();
        actionPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActionPlan() throws Exception {
        int databaseSizeBeforeUpdate = actionPlanRepository.findAll().size();
        actionPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(actionPlan))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActionPlan in the database
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActionPlan() throws Exception {
        // Initialize the database
        actionPlanRepository.saveAndFlush(actionPlan);

        int databaseSizeBeforeDelete = actionPlanRepository.findAll().size();

        // Delete the actionPlan
        restActionPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, actionPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActionPlan> actionPlanList = actionPlanRepository.findAll();
        assertThat(actionPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
