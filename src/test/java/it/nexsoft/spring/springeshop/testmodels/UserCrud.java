package it.nexsoft.spring.springeshop.testmodels;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import it.nexsoft.spring.springeshop.ApplicationLauncher;
import it.nexsoft.spring.springeshop.models.User;
import it.nexsoft.spring.springeshop.repositories.UserRepository;

@SpringBootTest(classes = ApplicationLauncher.class)
@AutoConfigureMockMvc
@WithMockUser
@Transactional
@ActiveProfiles("test")
public class UserCrud {

	private static final String ENTITY_API_URL = "/prova/";
	private static final String GET_URL = "getallusers";
	private static final String POST_URL = "postuser";
	private static final String PUT_URL = "putuser";
	private static final String DELETE_URL = "deleteuser";

	// Variabili per la creazione
	private static final String CREATED_FNAME = "provanome";
	private static final String CREATED_LNAME = "provacognome";
	private static final String CREATED_EMAIL = "provaemail";
	private static final String CREATED_PHONE = "provaphone";

	// Variabili per l'aggiornamento
	private static final String UPDATED_FNAME = "aggiornanome";
	private static final String UPDATED_LNAME = "aggiornacognome";
	private static final String UPDATED_EMAIL = "aggiornaemail";
	private static final String UPDATED_PHONE = "aggiornaphone";

	// Variabili per creazione utenti di prova
	private static final String MOCK_FNAME = "mocknome";
	private static final String MOCK_LNAME = "mockcognome";
	private static final String MOCK_EMAIL = "mockemail";
	private static final String MOCK_PHONE = "mockphone";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MockMvc userControllerMockMvc;

	private final ArrayList<Long> idUsers = new ArrayList<>();

	// Cambiato valore email in quanto chiave unica
	@BeforeEach
	public void initDB() {
		// vengono inseriti gli id degli utenti salvati in una ArrayList, in modo che
		// dopo possono essere tolti dal db
		idUsers.add(userRepository.save(new User(MOCK_FNAME, MOCK_LNAME, MOCK_EMAIL, MOCK_PHONE)).getId());
		idUsers.add(userRepository.save(new User(MOCK_FNAME, MOCK_LNAME, MOCK_EMAIL + 1, MOCK_PHONE)).getId());
		idUsers.add(userRepository.save(new User(MOCK_FNAME, MOCK_LNAME, MOCK_EMAIL + 2, MOCK_PHONE)).getId());
	}

	@AfterEach
	public void clearDB() {
		for (final Long id : idUsers) {
			userRepository.deleteById(id);
		}
	}

	@Test
	public void addUser() throws Exception {
		final int databaseSizeBeforeCreate = userRepository.findAll().size();

		// Creazione nuovo utente
		final User u = new User();
		u.setFirstName(CREATED_FNAME);
		u.setLastName(CREATED_LNAME);
		u.setEmail(CREATED_EMAIL);
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
		assertThat(retrieveUser.getPhone()).isEqualTo(CREATED_PHONE);

		// cancellazione utente creato
		userRepository.deleteById(retrieveUser.getId());
	}

	@Test
	public void getAllUsers() throws Exception {

		final ArrayList<User> users = (ArrayList<User>) userRepository.findAllById(idUsers);

		// Chiamata metodo GET
		userControllerMockMvc.perform(get(ENTITY_API_URL + GET_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Controllo sugli id degli utenti trovati con la find
		assertThat(users.get(0).getId()).isEqualTo(idUsers.get(0));
		assertThat(users.get(1).getId()).isEqualTo(idUsers.get(1));
		assertThat(users.get(2).getId()).isEqualTo(idUsers.get(2));
	}

	@Test
	public void updateUser() throws Exception {

		// Prendiamo il primo utente di quelli già inseriti
		final Optional<User> dataOriginal = userRepository.findById(idUsers.get(0));
		assertThat(dataOriginal.isPresent()).isEqualTo(true);
		final User original = dataOriginal.get();

		// Aggiornamento dei campi
		original.setFirstName(UPDATED_FNAME);
		original.setLastName(UPDATED_LNAME);
		original.setEmail(UPDATED_EMAIL);
		original.setPhone(UPDATED_PHONE);

		// Chiamata metodo PUT
		userControllerMockMvc
				.perform(put(ENTITY_API_URL + PUT_URL + "/{id}", original.getId())
						.contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(original)))
				.andExpect(status().isOk());

		// Prendiamo il primo utente
		final Optional<User> dataUpdated = userRepository.findById(idUsers.get(0));
		assertThat(dataUpdated.isPresent()).isEqualTo(true);

		final User updated = dataUpdated.get();

		// Controllo aggiornamento singoli campi
		assertThat(updated.getFirstName()).isEqualTo(UPDATED_FNAME);
		assertThat(updated.getLastName()).isEqualTo(UPDATED_LNAME);
		assertThat(updated.getEmail()).isEqualTo(UPDATED_EMAIL);
		assertThat(updated.getPhone()).isEqualTo(UPDATED_PHONE);
	}

	@Test
	public void deleteUser() throws Exception {

		// Creazione nuovo utente
		final User u = new User();
		u.setFirstName(CREATED_FNAME);
		u.setLastName(CREATED_LNAME);
		u.setEmail(CREATED_EMAIL);
		u.setPhone(CREATED_PHONE);

		// Salviamo il nuovo utente nel DB
		final User deleteUser = userRepository.save(u);
		// Chiamata metodo DELETE
		userControllerMockMvc.perform(delete(ENTITY_API_URL + DELETE_URL + "/{id}", deleteUser.getId()))
				.andExpect(status().isNoContent());

		// Controllo tramite fallimento findById()
		final Optional<User> userData = userRepository.findById(deleteUser.getId());
		assertThat(userData.isPresent()).isEqualTo(false);
	}

}
