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
import it.nexsoft.spring.springeshop.models.User;
import it.nexsoft.spring.springeshop.repositories.UserRepository;

@SpringBootTest(classes = ApplicationLauncher.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class UserCrud {

	private static final String ENTITY_API_URL = "/prova/";
	private static final String GET_URL = "getallusers";
	private static final String POST_URL = "postuser";
	private static final String PUT_URL = "putuser";
	private static final String DELETE_URL = "deleteuser";
	private static final String DELETE_URL_1 = "deleteallusers";

	// Variabili per la creazione
	private static final String CREATED_FNAME = "provanome";
	private static final String CREATED_LNAME = "provacognome";
	private static final String CREATED_EMAIL = "provaemail";
	private static final String CREATED_PASSWORD = "$2a$10$Po1.sDsJpv8vln496vV9v.cVRO9Hg64lZ439.7PXBaDDUWJDTlks2";
	private static final String CREATED_PHONE = "provaphone";

	// Variabili per l'aggiornamento
	private static final String UPDATED_FNAME = "aggiornanome";
	private static final String UPDATED_LNAME = "aggiornacognome";
	private static final String UPDATED_EMAIL = "aggiornaemail";
	private static final String UPDATED_PASSWORD = "$2a$10$38qOT5gILqj3dc.yMaNIMu/Fw0NrjTIrWUM3eClZQ4sEgxvTLAQh6";
	private static final String UPDATED_PHONE = "aggiornaphone";

	// Variabili per creazione utenti di prova
	private static final String MOCK_FNAME = "mocknome";
	private static final String MOCK_LNAME = "mockcognome";
	private static final String MOCK_EMAIL = "mockemail";
	private static final String MOCK_PASSWORD = "$2a$10$Po1.sDsJpv8vln496vV9v.cVRO9Hg64lZ439.7PXBaDDUWJDTlks2";
	private static final String MOCK_PHONE = "mockphone";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MockMvc userControllerMockMvc;

	// Cambiato valore email in quanto chiave unica
	@BeforeEach
	public void initDB() {
		userRepository.save(new User(MOCK_FNAME, MOCK_LNAME, MOCK_EMAIL, MOCK_PASSWORD, MOCK_PHONE));
		userRepository.save(new User(MOCK_FNAME, MOCK_LNAME, MOCK_EMAIL + 1, MOCK_PASSWORD, MOCK_PHONE));
		userRepository.save(new User(MOCK_FNAME, MOCK_LNAME, MOCK_EMAIL + 2, MOCK_PASSWORD, MOCK_PHONE));
	}

	@AfterEach
	public void clearDB() {
		userRepository.deleteAll();
	}

	@Test
	public void addUser() throws Exception {
		final int databaseSizeBeforeCreate = userRepository.findAll().size();

		// Creazione nuovo utente
		final User u = new User();
		u.setFirstName(CREATED_FNAME);
		u.setLastName(CREATED_LNAME);
		u.setEmail(CREATED_EMAIL);
		u.setPassword(CREATED_PASSWORD);
		u.setPhone(CREATED_PHONE);

		// Chiamata metodo POST
		userControllerMockMvc.perform(
				post(ENTITY_API_URL + POST_URL).contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(u)))
				.andExpect(status().isCreated());

		// Controllo su taglia del DB
		final List<User> usersList = userRepository.findAll();
		assertThat(usersList).hasSize(databaseSizeBeforeCreate + 1);

		final User retrieveUser = userRepository.findAll().get(usersList.size() - 1);

		// Controllo su inserimento singoli campi
		assertThat(retrieveUser.getFirstName()).isEqualTo(CREATED_FNAME);
		assertThat(retrieveUser.getLastName()).isEqualTo(CREATED_LNAME);
		assertThat(retrieveUser.getEmail()).isEqualTo(CREATED_EMAIL);
		assertThat(retrieveUser.getPassword()).isEqualTo(CREATED_PASSWORD);
		assertThat(retrieveUser.getPhone()).isEqualTo(CREATED_PHONE);
	}

	@Test
	public void getAllUsers() throws Exception {
		final int databaseOriginalSize = userRepository.findAll().size();

		// Creazione nuovo utente
		final User u = new User();
		u.setFirstName(CREATED_FNAME);
		u.setLastName(CREATED_LNAME);
		u.setEmail(CREATED_EMAIL);
		u.setPassword(CREATED_PASSWORD);
		u.setPhone(CREATED_PHONE);

		// Chiamata metodo POST
		userControllerMockMvc.perform(
				post(ENTITY_API_URL + POST_URL).contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(u)))
				.andExpect(status().isCreated());

		// Chiamata metodo GET
		userControllerMockMvc.perform(get(ENTITY_API_URL + GET_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		final int databaseActualSize = userRepository.findAll().size();

		// Controllo su taglia del DB
		assertThat(databaseActualSize).isEqualTo(databaseOriginalSize + 1);
	}

	@Test
	public void updateUser() throws Exception {
		final List<User> originalList = userRepository.findAll();

		final User original = originalList.get(originalList.size() - 1);

		// Aggiornamento dei campi
		original.setFirstName(UPDATED_FNAME);
		original.setLastName(UPDATED_LNAME);
		original.setEmail(UPDATED_EMAIL);
		original.setPassword(UPDATED_PASSWORD);
		original.setPhone(UPDATED_PHONE);

		// Chiamata metodo PUT
		userControllerMockMvc
				.perform(put(ENTITY_API_URL + PUT_URL + "/{id}", original.getId())
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(original)))
				.andExpect(status().isOk());

		final List<User> updatedList = userRepository.findAll();

		// Prendiamo l'ultimo utente
		final User updated = userRepository.findAll().get(updatedList.size() - 1);

		// Controllo aggiornamento singoli campi
		assertThat(updated.getFirstName()).isEqualTo(UPDATED_FNAME);
		assertThat(updated.getLastName()).isEqualTo(UPDATED_LNAME);
		assertThat(updated.getEmail()).isEqualTo(UPDATED_EMAIL);
		assertThat(updated.getPassword()).isEqualTo(UPDATED_PASSWORD);
		assertThat(updated.getPhone()).isEqualTo(UPDATED_PHONE);
	}

	@Test
	public void deleteUser() throws Exception {
		final int databaseOriginalSize = userRepository.findAll().size();

		// Prendiamo l'ultimo utente
		final User deleteUser = userRepository.findAll().get(databaseOriginalSize - 1);

		// Chiamata metodo DELETE
		userControllerMockMvc.perform(delete(ENTITY_API_URL + DELETE_URL + "/{id}", deleteUser.getId()))
				.andExpect(status().isNoContent());

		final int databaseUpdatedSize = userRepository.findAll().size();

		// Controllo su taglia del DB
		assertThat(databaseUpdatedSize).isEqualTo(databaseOriginalSize - 1);
	}

	@Test
	public void deleteAll() throws Exception {
		final int expectedDatabaseSize = 0;

		// Chiamata metodo DELETE
		userControllerMockMvc.perform(delete(ENTITY_API_URL + DELETE_URL_1)).andExpect(status().isNoContent());

		final int actualDatabaseSize = userRepository.findAll().size();

		// Controllo su taglia del DB
		assertThat(actualDatabaseSize).isEqualTo(expectedDatabaseSize);
	}

}
