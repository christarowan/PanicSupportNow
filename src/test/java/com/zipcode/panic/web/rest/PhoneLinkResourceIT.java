package com.zipcode.panic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zipcode.panic.IntegrationTest;
import com.zipcode.panic.domain.PhoneLink;
import com.zipcode.panic.repository.PhoneLinkRepository;
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
 * Integration tests for the {@link PhoneLinkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhoneLinkResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/phone-links";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhoneLinkRepository phoneLinkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhoneLinkMockMvc;

    private PhoneLink phoneLink;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhoneLink createEntity(EntityManager em) {
        PhoneLink phoneLink = new PhoneLink().number(DEFAULT_NUMBER).name(DEFAULT_NAME);
        return phoneLink;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhoneLink createUpdatedEntity(EntityManager em) {
        PhoneLink phoneLink = new PhoneLink().number(UPDATED_NUMBER).name(UPDATED_NAME);
        return phoneLink;
    }

    @BeforeEach
    public void initTest() {
        phoneLink = createEntity(em);
    }

    @Test
    @Transactional
    void createPhoneLink() throws Exception {
        int databaseSizeBeforeCreate = phoneLinkRepository.findAll().size();
        // Create the PhoneLink
        restPhoneLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneLink)))
            .andExpect(status().isCreated());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeCreate + 1);
        PhoneLink testPhoneLink = phoneLinkList.get(phoneLinkList.size() - 1);
        assertThat(testPhoneLink.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPhoneLink.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPhoneLinkWithExistingId() throws Exception {
        // Create the PhoneLink with an existing ID
        phoneLink.setId(1L);

        int databaseSizeBeforeCreate = phoneLinkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneLink)))
            .andExpect(status().isBadRequest());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPhoneLinks() throws Exception {
        // Initialize the database
        phoneLinkRepository.saveAndFlush(phoneLink);

        // Get all the phoneLinkList
        restPhoneLinkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phoneLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPhoneLink() throws Exception {
        // Initialize the database
        phoneLinkRepository.saveAndFlush(phoneLink);

        // Get the phoneLink
        restPhoneLinkMockMvc
            .perform(get(ENTITY_API_URL_ID, phoneLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phoneLink.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPhoneLink() throws Exception {
        // Get the phoneLink
        restPhoneLinkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPhoneLink() throws Exception {
        // Initialize the database
        phoneLinkRepository.saveAndFlush(phoneLink);

        int databaseSizeBeforeUpdate = phoneLinkRepository.findAll().size();

        // Update the phoneLink
        PhoneLink updatedPhoneLink = phoneLinkRepository.findById(phoneLink.getId()).get();
        // Disconnect from session so that the updates on updatedPhoneLink are not directly saved in db
        em.detach(updatedPhoneLink);
        updatedPhoneLink.number(UPDATED_NUMBER).name(UPDATED_NAME);

        restPhoneLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhoneLink.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPhoneLink))
            )
            .andExpect(status().isOk());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeUpdate);
        PhoneLink testPhoneLink = phoneLinkList.get(phoneLinkList.size() - 1);
        assertThat(testPhoneLink.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPhoneLink.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPhoneLink() throws Exception {
        int databaseSizeBeforeUpdate = phoneLinkRepository.findAll().size();
        phoneLink.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phoneLink.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhoneLink() throws Exception {
        int databaseSizeBeforeUpdate = phoneLinkRepository.findAll().size();
        phoneLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhoneLink() throws Exception {
        int databaseSizeBeforeUpdate = phoneLinkRepository.findAll().size();
        phoneLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneLinkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneLink)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhoneLinkWithPatch() throws Exception {
        // Initialize the database
        phoneLinkRepository.saveAndFlush(phoneLink);

        int databaseSizeBeforeUpdate = phoneLinkRepository.findAll().size();

        // Update the phoneLink using partial update
        PhoneLink partialUpdatedPhoneLink = new PhoneLink();
        partialUpdatedPhoneLink.setId(phoneLink.getId());

        partialUpdatedPhoneLink.number(UPDATED_NUMBER);

        restPhoneLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoneLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoneLink))
            )
            .andExpect(status().isOk());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeUpdate);
        PhoneLink testPhoneLink = phoneLinkList.get(phoneLinkList.size() - 1);
        assertThat(testPhoneLink.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPhoneLink.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePhoneLinkWithPatch() throws Exception {
        // Initialize the database
        phoneLinkRepository.saveAndFlush(phoneLink);

        int databaseSizeBeforeUpdate = phoneLinkRepository.findAll().size();

        // Update the phoneLink using partial update
        PhoneLink partialUpdatedPhoneLink = new PhoneLink();
        partialUpdatedPhoneLink.setId(phoneLink.getId());

        partialUpdatedPhoneLink.number(UPDATED_NUMBER).name(UPDATED_NAME);

        restPhoneLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoneLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoneLink))
            )
            .andExpect(status().isOk());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeUpdate);
        PhoneLink testPhoneLink = phoneLinkList.get(phoneLinkList.size() - 1);
        assertThat(testPhoneLink.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPhoneLink.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPhoneLink() throws Exception {
        int databaseSizeBeforeUpdate = phoneLinkRepository.findAll().size();
        phoneLink.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phoneLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhoneLink() throws Exception {
        int databaseSizeBeforeUpdate = phoneLinkRepository.findAll().size();
        phoneLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhoneLink() throws Exception {
        int databaseSizeBeforeUpdate = phoneLinkRepository.findAll().size();
        phoneLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneLinkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(phoneLink))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhoneLink in the database
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhoneLink() throws Exception {
        // Initialize the database
        phoneLinkRepository.saveAndFlush(phoneLink);

        int databaseSizeBeforeDelete = phoneLinkRepository.findAll().size();

        // Delete the phoneLink
        restPhoneLinkMockMvc
            .perform(delete(ENTITY_API_URL_ID, phoneLink.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PhoneLink> phoneLinkList = phoneLinkRepository.findAll();
        assertThat(phoneLinkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
