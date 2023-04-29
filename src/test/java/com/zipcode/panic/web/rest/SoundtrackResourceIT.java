package com.zipcode.panic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zipcode.panic.IntegrationTest;
import com.zipcode.panic.domain.Soundtrack;
import com.zipcode.panic.repository.SoundtrackRepository;
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
 * Integration tests for the {@link SoundtrackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SoundtrackResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/soundtracks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SoundtrackRepository soundtrackRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSoundtrackMockMvc;

    private Soundtrack soundtrack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Soundtrack createEntity(EntityManager em) {
        Soundtrack soundtrack = new Soundtrack().fileName(DEFAULT_FILE_NAME).name(DEFAULT_NAME);
        return soundtrack;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Soundtrack createUpdatedEntity(EntityManager em) {
        Soundtrack soundtrack = new Soundtrack().fileName(UPDATED_FILE_NAME).name(UPDATED_NAME);
        return soundtrack;
    }

    @BeforeEach
    public void initTest() {
        soundtrack = createEntity(em);
    }

    @Test
    @Transactional
    void createSoundtrack() throws Exception {
        int databaseSizeBeforeCreate = soundtrackRepository.findAll().size();
        // Create the Soundtrack
        restSoundtrackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soundtrack)))
            .andExpect(status().isCreated());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeCreate + 1);
        Soundtrack testSoundtrack = soundtrackList.get(soundtrackList.size() - 1);
        assertThat(testSoundtrack.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testSoundtrack.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSoundtrackWithExistingId() throws Exception {
        // Create the Soundtrack with an existing ID
        soundtrack.setId(1L);

        int databaseSizeBeforeCreate = soundtrackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoundtrackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soundtrack)))
            .andExpect(status().isBadRequest());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSoundtracks() throws Exception {
        // Initialize the database
        soundtrackRepository.saveAndFlush(soundtrack);

        // Get all the soundtrackList
        restSoundtrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(soundtrack.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSoundtrack() throws Exception {
        // Initialize the database
        soundtrackRepository.saveAndFlush(soundtrack);

        // Get the soundtrack
        restSoundtrackMockMvc
            .perform(get(ENTITY_API_URL_ID, soundtrack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(soundtrack.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSoundtrack() throws Exception {
        // Get the soundtrack
        restSoundtrackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSoundtrack() throws Exception {
        // Initialize the database
        soundtrackRepository.saveAndFlush(soundtrack);

        int databaseSizeBeforeUpdate = soundtrackRepository.findAll().size();

        // Update the soundtrack
        Soundtrack updatedSoundtrack = soundtrackRepository.findById(soundtrack.getId()).get();
        // Disconnect from session so that the updates on updatedSoundtrack are not directly saved in db
        em.detach(updatedSoundtrack);
        updatedSoundtrack.fileName(UPDATED_FILE_NAME).name(UPDATED_NAME);

        restSoundtrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSoundtrack.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSoundtrack))
            )
            .andExpect(status().isOk());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeUpdate);
        Soundtrack testSoundtrack = soundtrackList.get(soundtrackList.size() - 1);
        assertThat(testSoundtrack.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testSoundtrack.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSoundtrack() throws Exception {
        int databaseSizeBeforeUpdate = soundtrackRepository.findAll().size();
        soundtrack.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoundtrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, soundtrack.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soundtrack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSoundtrack() throws Exception {
        int databaseSizeBeforeUpdate = soundtrackRepository.findAll().size();
        soundtrack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoundtrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soundtrack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSoundtrack() throws Exception {
        int databaseSizeBeforeUpdate = soundtrackRepository.findAll().size();
        soundtrack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoundtrackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soundtrack)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSoundtrackWithPatch() throws Exception {
        // Initialize the database
        soundtrackRepository.saveAndFlush(soundtrack);

        int databaseSizeBeforeUpdate = soundtrackRepository.findAll().size();

        // Update the soundtrack using partial update
        Soundtrack partialUpdatedSoundtrack = new Soundtrack();
        partialUpdatedSoundtrack.setId(soundtrack.getId());

        partialUpdatedSoundtrack.fileName(UPDATED_FILE_NAME);

        restSoundtrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoundtrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoundtrack))
            )
            .andExpect(status().isOk());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeUpdate);
        Soundtrack testSoundtrack = soundtrackList.get(soundtrackList.size() - 1);
        assertThat(testSoundtrack.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testSoundtrack.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSoundtrackWithPatch() throws Exception {
        // Initialize the database
        soundtrackRepository.saveAndFlush(soundtrack);

        int databaseSizeBeforeUpdate = soundtrackRepository.findAll().size();

        // Update the soundtrack using partial update
        Soundtrack partialUpdatedSoundtrack = new Soundtrack();
        partialUpdatedSoundtrack.setId(soundtrack.getId());

        partialUpdatedSoundtrack.fileName(UPDATED_FILE_NAME).name(UPDATED_NAME);

        restSoundtrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoundtrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoundtrack))
            )
            .andExpect(status().isOk());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeUpdate);
        Soundtrack testSoundtrack = soundtrackList.get(soundtrackList.size() - 1);
        assertThat(testSoundtrack.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testSoundtrack.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSoundtrack() throws Exception {
        int databaseSizeBeforeUpdate = soundtrackRepository.findAll().size();
        soundtrack.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoundtrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, soundtrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soundtrack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSoundtrack() throws Exception {
        int databaseSizeBeforeUpdate = soundtrackRepository.findAll().size();
        soundtrack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoundtrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soundtrack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSoundtrack() throws Exception {
        int databaseSizeBeforeUpdate = soundtrackRepository.findAll().size();
        soundtrack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoundtrackMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(soundtrack))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Soundtrack in the database
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSoundtrack() throws Exception {
        // Initialize the database
        soundtrackRepository.saveAndFlush(soundtrack);

        int databaseSizeBeforeDelete = soundtrackRepository.findAll().size();

        // Delete the soundtrack
        restSoundtrackMockMvc
            .perform(delete(ENTITY_API_URL_ID, soundtrack.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Soundtrack> soundtrackList = soundtrackRepository.findAll();
        assertThat(soundtrackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
