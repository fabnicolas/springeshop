package it.nexsoft.spring.springeshop.testmodels;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;

import it.nexsoft.spring.springeshop.ApplicationLauncher;
import it.nexsoft.spring.springeshop.models.Article;
import it.nexsoft.spring.springeshop.repositories.ArticleRepository;

@SpringBootTest(classes = ApplicationLauncher.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class ArticleCrud {

	private static final String ENTITY_API_URL = "/prova/";
	private static final String GET_URL = "getallarticles";
	private static final String POST_URL = "postarticle";
	private static final String PUT_URL = "putarticle";
	private static final String DELETE_URL = "deletearticle";
	private static final String DELETE_URL_1 = "deleteallarticles";

	// Variabili per la creazione
	private static final String CREATED_NAME = "provaarticolo";
	private static final String CREATED_DESCRIPTION = "descrizioneprovaarticolo";
	private static final float CREATED_PRICE = 900;

	// Variabili per l'aggiornamento
	private static final String UPDATED_NAME = "articoloaggiornato";
	private static final String UPDATED_DESCRIPTION = "descrizionearticoloaggiornato";
	private static final float UPDATED_PRICE = 1000;

	// Variabili per creazione articoli di prova del DB
	private static final String MOCK_NAME = "mockarticolo";
	private static final String MOCK_DESCRIPTION = "mockdescarticolo";
	private static final float MOCK_PRICE = 10;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private MockMvc articleControllerMockMvc;

	@BeforeEach
	public void initDB() {
		articleRepository.save(new Article(MOCK_NAME, MOCK_DESCRIPTION, MOCK_PRICE));
		articleRepository.save(new Article(MOCK_NAME, MOCK_DESCRIPTION, MOCK_PRICE));
		articleRepository.save(new Article(MOCK_NAME, MOCK_DESCRIPTION, MOCK_PRICE));
	}

	@AfterEach
	public void clearDB() {
		articleRepository.deleteAll();
	}

	@Test
	public void addArticle() throws Exception {
		final int databaseSizeBeforeCreate = articleRepository.findAll().size();

		// Creazione nuovo articolo
		final Article a = new Article();
		a.setName(CREATED_NAME);
		a.setDescription(CREATED_DESCRIPTION);
		a.setPrice(CREATED_PRICE);

		// Chiamata metodo POST
		articleControllerMockMvc.perform(
				post(ENTITY_API_URL + POST_URL).contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(a)))
				.andExpect(status().isCreated());

		// Controllo su taglia del DB
		final List<Article> articleList = articleRepository.findAll();
		assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);

		final Article retrieveArticle = articleRepository.findAll().get(articleList.size() - 1);

		// Controllo su inserimento singoli campi
		assertThat(retrieveArticle.getName()).isEqualTo(CREATED_NAME);
		assertThat(retrieveArticle.getDescription()).isEqualTo(CREATED_DESCRIPTION);
		assertThat(retrieveArticle.getPrice()).isEqualTo(CREATED_PRICE);
	}

	@Test
	public void getAllArticles() throws Exception {
		final int databaseOriginalSize = articleRepository.findAll().size();

		// Creazione nuovo articolo
		final Article a = new Article();
		a.setName(CREATED_NAME);
		a.setDescription(CREATED_DESCRIPTION);
		a.setPrice(CREATED_PRICE);

		// Chiamata metodo POST
		articleControllerMockMvc.perform(
				post(ENTITY_API_URL + POST_URL).contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(a)))
				.andExpect(status().isCreated());

		// Chiamata metodo GET
		articleControllerMockMvc.perform(get(ENTITY_API_URL + GET_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		final int databaseActualSize = articleRepository.findAll().size();

		// Controllo su taglia del DB
		assertThat(databaseActualSize).isEqualTo(databaseOriginalSize + 1);
	}

	@Test
	public void updateArticle() throws Exception {
		final List<Article> originalList = articleRepository.findAll();

		final Article original = originalList.get(originalList.size() - 1);

		// Aggiornamento dei campi
		original.setName(UPDATED_NAME);
		original.setDescription(UPDATED_DESCRIPTION);
		original.setPrice(UPDATED_PRICE);

		// Chiamata metodo PUT
		articleControllerMockMvc
				.perform(put(ENTITY_API_URL + PUT_URL + "/{id}", original.getId())
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(original)))
				.andExpect(status().isOk());

		final List<Article> updatedList = articleRepository.findAll();

		// Prendiamo l'ultimo articolo
		final Article updated = articleRepository.findAll().get(updatedList.size() - 1);

		// Controllo aggiornamento singoli campi
		assertThat(updated.getName()).isEqualTo(UPDATED_NAME);
		assertThat(updated.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
		assertThat(updated.getPrice()).isEqualTo(UPDATED_PRICE);
	}

	@Test
	public void deleteArticle() throws Exception {
		final int databaseOriginalSize = articleRepository.findAll().size();

		// Prendiamo l'ultimo articolo
		final Article deleteArticle = articleRepository.findAll().get(databaseOriginalSize - 1);

		// Chiamata metodo DELETE
		articleControllerMockMvc.perform(delete(ENTITY_API_URL + DELETE_URL + "/{id}", deleteArticle.getId()))
				.andExpect(status().isNoContent());

		final int databaseUpdatedSize = articleRepository.findAll().size();

		// Controllo su taglia del DB
		assertThat(databaseUpdatedSize).isEqualTo(databaseOriginalSize - 1);
	}

	@Test
	public void deleteAll() throws Exception {
		final int expectedDatabaseSize = 0;

		// Chiamata metodo DELETE
		articleControllerMockMvc.perform(delete(ENTITY_API_URL + DELETE_URL_1)).andExpect(status().isNoContent());

		final int actualDatabaseSize = articleRepository.findAll().size();

		// Controllo su taglia del DB
		assertThat(actualDatabaseSize).isEqualTo(expectedDatabaseSize);
	}

}
