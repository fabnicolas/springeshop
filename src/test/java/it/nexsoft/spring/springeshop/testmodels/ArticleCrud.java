package it.nexsoft.spring.springeshop.testmodels;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import it.nexsoft.spring.springeshop.ApplicationLauncher;
import it.nexsoft.spring.springeshop.models.Article;
import it.nexsoft.spring.springeshop.repositories.ArticleRepository;

@SpringBootTest(classes = ApplicationLauncher.class)
@AutoConfigureMockMvc
@WithMockUser
@Transactional
@ActiveProfiles("test")
public class ArticleCrud {

	private static final String ENTITY_API_URL = "/prova/";
	private static final String GET_URL = "getallarticles";
	private static final String POST_URL = "postarticle";
	private static final String PUT_URL = "putarticle";
	private static final String DELETE_URL = "deletearticle";

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

	private final ArrayList<Long> idArticles = new ArrayList<>();

	@BeforeEach
	public void initDB() {
		// vengono inseriti gli id degli articoli salvati in una ArrayList, in modo che
		// dopo possono essere tolti dal db
		idArticles.add(articleRepository.save(new Article(MOCK_NAME, MOCK_DESCRIPTION, MOCK_PRICE)).getId());
		idArticles.add(articleRepository.save(new Article(MOCK_NAME, MOCK_DESCRIPTION, MOCK_PRICE)).getId());
		idArticles.add(articleRepository.save(new Article(MOCK_NAME, MOCK_DESCRIPTION, MOCK_PRICE)).getId());
	}

	@AfterEach
	public void clearDB() {
		for (final Long id : idArticles) {
			articleRepository.deleteById(id);
		}
	}

	@Test
	public void addArticle() throws Exception {

		// Creazione nuovo articolo
		final Article a = new Article();
		a.setName(CREATED_NAME);
		a.setDescription(CREATED_DESCRIPTION);
		a.setPrice(CREATED_PRICE);

		// Chiamata metodo POST
		final MvcResult result = articleControllerMockMvc.perform(
				post(ENTITY_API_URL + POST_URL).contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(a)))
				.andExpect(status().isCreated()).andReturn();

		final String resultString = result.getResponse().getContentAsString();
		final Gson g = new Gson();

		// Conversione del risultato da formato JSON a oggetto Article
		final Article inserted = g.fromJson(resultString, Article.class);

		// Controllo corretto inserimento con findById()
		final Optional<Article> retrieveData = articleRepository.findById(inserted.getId());
		assertThat(retrieveData.isPresent()).isEqualTo(true);

		final Article retrieveArticle = retrieveData.get();
		// Controllo su inserimento singoli campi
		assertThat(retrieveArticle.getName()).isEqualTo(CREATED_NAME);
		assertThat(retrieveArticle.getDescription()).isEqualTo(CREATED_DESCRIPTION);
		assertThat(retrieveArticle.getPrice()).isEqualTo(CREATED_PRICE);

		// cancellazione articolo creato
		articleRepository.deleteById(retrieveArticle.getId());
	}

	@Test
	public void getAllArticles() throws Exception {

		final ArrayList<Article> articles = (ArrayList<Article>) articleRepository.findAllById(idArticles);

		// Chiamata metodo GET
		articleControllerMockMvc.perform(get(ENTITY_API_URL + GET_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Controllo sugli id degli articoli inseriti con il metodo initDB()
		assertThat(articles.get(0).getId()).isEqualTo(idArticles.get(0));
		assertThat(articles.get(1).getId()).isEqualTo(idArticles.get(1));
		assertThat(articles.get(2).getId()).isEqualTo(idArticles.get(2));
	}

	@Test
	public void updateArticle() throws Exception {

		// Controllo tramite ricerca per id
		final Optional<Article> data = articleRepository.findById(idArticles.get(0));
		assertThat(data.get()).isNotNull();

		final Article original = data.get();
		// Aggiornamento dei campi per il primo articolo già presente nel DB
		// inizializzato con il metodo initDB()
		original.setName(UPDATED_NAME);
		original.setDescription(UPDATED_DESCRIPTION);
		original.setPrice(UPDATED_PRICE);

		// Chiamata metodo PUT
		articleControllerMockMvc
				.perform(put(ENTITY_API_URL + PUT_URL + "/{id}", original.getId())
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(original)))
				.andExpect(status().isOk());

		// Prendiamo il primo articolo
		final Optional<Article> dataUpdated = articleRepository.findById(idArticles.get(0));
		assertThat(dataUpdated.get()).isNotNull();

		final Article updated = dataUpdated.get();
		// Controllo aggiornamento singoli campi
		assertThat(updated.getName()).isEqualTo(UPDATED_NAME);
		assertThat(updated.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
		assertThat(updated.getPrice()).isEqualTo(UPDATED_PRICE);
	}

	@Test
	public void deleteArticle() throws Exception {

		// Creazione nuovo articolo
		final Article newArticle = new Article();
		newArticle.setName(CREATED_NAME);
		newArticle.setDescription(CREATED_DESCRIPTION);
		newArticle.setPrice(CREATED_PRICE);

		// Salvataggio nuovo articolo nel DB; verrà cancellato
		final Article deleteArticle = articleRepository.save(newArticle);

		// Chiamata metodo DELETE
		articleControllerMockMvc.perform(delete(ENTITY_API_URL + DELETE_URL + "/{id}", deleteArticle.getId()))
				.andExpect(status().isNoContent());

		// Controllo tramite fallimento findById
		final Optional<Article> data = articleRepository.findById(deleteArticle.getId());
		assertThat(data.isPresent()).isEqualTo(false);
	}

}
