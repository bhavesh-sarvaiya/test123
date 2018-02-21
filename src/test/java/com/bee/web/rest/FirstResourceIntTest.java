package com.bee.web.rest;

import com.bee.BeeApp;

import com.bee.domain.First;
import com.bee.repository.FirstRepository;
import com.bee.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.bee.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FirstResource REST controller.
 *
 * @see FirstResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeeApp.class)
public class FirstResourceIntTest {

    private static final Integer DEFAULT_NO = 1;
    private static final Integer UPDATED_NO = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private FirstRepository firstRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFirstMockMvc;

    private First first;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FirstResource firstResource = new FirstResource(firstRepository);
        this.restFirstMockMvc = MockMvcBuilders.standaloneSetup(firstResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static First createEntity(EntityManager em) {
        First first = new First()
            .no(DEFAULT_NO)
            .name(DEFAULT_NAME);
        return first;
    }

    @Before
    public void initTest() {
        first = createEntity(em);
    }

    @Test
    @Transactional
    public void createFirst() throws Exception {
        int databaseSizeBeforeCreate = firstRepository.findAll().size();

        // Create the First
        restFirstMockMvc.perform(post("/api/firsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(first)))
            .andExpect(status().isCreated());

        // Validate the First in the database
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeCreate + 1);
        First testFirst = firstList.get(firstList.size() - 1);
        assertThat(testFirst.getNo()).isEqualTo(DEFAULT_NO);
        assertThat(testFirst.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFirstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = firstRepository.findAll().size();

        // Create the First with an existing ID
        first.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFirstMockMvc.perform(post("/api/firsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(first)))
            .andExpect(status().isBadRequest());

        // Validate the First in the database
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFirsts() throws Exception {
        // Initialize the database
        firstRepository.saveAndFlush(first);

        // Get all the firstList
        restFirstMockMvc.perform(get("/api/firsts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(first.getId().intValue())))
            .andExpect(jsonPath("$.[*].no").value(hasItem(DEFAULT_NO)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFirst() throws Exception {
        // Initialize the database
        firstRepository.saveAndFlush(first);

        // Get the first
        restFirstMockMvc.perform(get("/api/firsts/{id}", first.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(first.getId().intValue()))
            .andExpect(jsonPath("$.no").value(DEFAULT_NO))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFirst() throws Exception {
        // Get the first
        restFirstMockMvc.perform(get("/api/firsts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFirst() throws Exception {
        // Initialize the database
        firstRepository.saveAndFlush(first);
        int databaseSizeBeforeUpdate = firstRepository.findAll().size();

        // Update the first
        First updatedFirst = firstRepository.findOne(first.getId());
        // Disconnect from session so that the updates on updatedFirst are not directly saved in db
        em.detach(updatedFirst);
        updatedFirst
            .no(UPDATED_NO)
            .name(UPDATED_NAME);

        restFirstMockMvc.perform(put("/api/firsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFirst)))
            .andExpect(status().isOk());

        // Validate the First in the database
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeUpdate);
        First testFirst = firstList.get(firstList.size() - 1);
        assertThat(testFirst.getNo()).isEqualTo(UPDATED_NO);
        assertThat(testFirst.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingFirst() throws Exception {
        int databaseSizeBeforeUpdate = firstRepository.findAll().size();

        // Create the First

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFirstMockMvc.perform(put("/api/firsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(first)))
            .andExpect(status().isCreated());

        // Validate the First in the database
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFirst() throws Exception {
        // Initialize the database
        firstRepository.saveAndFlush(first);
        int databaseSizeBeforeDelete = firstRepository.findAll().size();

        // Get the first
        restFirstMockMvc.perform(delete("/api/firsts/{id}", first.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(First.class);
        First first1 = new First();
        first1.setId(1L);
        First first2 = new First();
        first2.setId(first1.getId());
        assertThat(first1).isEqualTo(first2);
        first2.setId(2L);
        assertThat(first1).isNotEqualTo(first2);
        first1.setId(null);
        assertThat(first1).isNotEqualTo(first2);
    }
}
