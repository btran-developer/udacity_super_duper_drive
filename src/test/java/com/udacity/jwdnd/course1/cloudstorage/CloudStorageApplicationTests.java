package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	UserService userService;
	@Autowired
	EncryptionService encryptionService;
	@Autowired
	NoteService noteService;
	@Autowired
	CredentialService credentialService;

	private User user;

	private WebDriver driver;
	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;

	private void doLogin(String username, String password) {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		user = new User(null, "test", null, "123", "tesing", "account");
		userService.createUser(user);
		user = userService.getUserByUserName("test");
		user.setPassword("123");
	}

	@AfterEach
	public void afterEach() {
		userService.removeUserByUsername(user.getUsername());
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");

		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");

		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void redirectToLoginWhenNotLoggedIn() {
		driver.get("http://localhost:" + this.port);

		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testAuthenticationFlow() {
		driver.get("http://localhost:" + this.port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.doSignup("John", "Doe", "abc", "def");
		doLogin("abc", "def");
		Assertions.assertEquals("Home", driver.getTitle());
		driver.get("http://localhost:" + this.port);
		homePage = new HomePage(driver);
		homePage.logout();
		driver.get("http://localhost:" + this.port);

		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void uploadFile() throws InterruptedException {
		File file = new File("src/test/java/com/udacity/jwdnd/course1/cloudstorage/test.txt");
		final String fileName = file.getName();
		final String directory = file.getAbsolutePath();

		doLogin(user.getUsername(), user.getPassword());
		driver.get("http://localhost:" + this.port);
		homePage = new HomePage(driver);
		homePage.uploadFile(directory);
		driver.get("http://localhost:" + this.port);

		Assertions.assertEquals(fileName, homePage.getFirstFileName());
	}

	@Test
	public void createNote() throws InterruptedException {
		final String noteTitle = "testNote1";
		final String noteDescription = "this is a test note";
		Note note = new Note(null, noteTitle, noteDescription, null);

		doLogin(user.getUsername(), user.getPassword());
		driver.get("http://localhost:" + this.port);
		homePage = new HomePage(driver);
		homePage.navigateTo("notes");
		homePage.addNote(note);
		driver.get("http://localhost:" + this.port);
		homePage.navigateTo("notes");
		Note result = homePage.getFirstNote();

		Assertions.assertEquals(note.getNoteTitle(), result.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), result.getNoteDescription());
	}

	@Test
	public void editNote() throws InterruptedException {
		final String fromNoteTitle = "fromNote";
		final String fromNoteDescription = "from note description";
		Note oldNote = new Note(null, fromNoteTitle, fromNoteDescription, user.getUserId());
		final String toNoteTitle = "toNote";
		final String toNoteDescription = "to note description";
		Note updatedNote = new Note(null, toNoteTitle, toNoteDescription, null);

		noteService.storeNewNote(oldNote);
		doLogin(user.getUsername(), user.getPassword());
		driver.get("http://localhost:" + this.port);
		homePage = new HomePage(driver);
		homePage.navigateTo("notes");
		homePage.editFirstNoteTo(updatedNote);
		driver.get("http://localhost:" + this.port);
		homePage.navigateTo("notes");
		Note result = homePage.getFirstNote();

		Assertions.assertEquals(updatedNote.getNoteTitle(), result.getNoteTitle());
		Assertions.assertEquals(updatedNote.getNoteDescription(), result.getNoteDescription());
	}

	@Test
	public void deleteNote() {
		final String noteTitle = "testNote1";
		final String noteDescription = "this is a test note";
		Note note = new Note(null, noteTitle, noteDescription, user.getUserId());

		noteService.storeNewNote(note);
		doLogin(user.getUsername(), user.getPassword());
		driver.get("http://localhost:" + this.port);
		homePage = new HomePage(driver);
		homePage.navigateTo("notes");
		homePage.deleteFirstNote();
		driver.get("http://localhost:" + this.port);

		Assertions.assertEquals(0, driver.findElements(By.className("noteRow")).size());
	}

	@Test
	public void addCredential() throws InterruptedException {
		final String url = "http://localhost:8000";
		final String username = "test123";
		final String password = "password";
		Credential credential =
				new Credential(null, url, username, null, password, null);

		doLogin(user.getUsername(), user.getPassword());
		homePage = new HomePage(driver);
		homePage.navigateTo("credentials");
		homePage.addCredential(credential);
		driver.get("http://localhost:" + this.port);
		homePage.navigateTo("credentials");
		Credential result = homePage.getFirstCredential();

		Assertions.assertEquals(credential.getUrl(), result.getUrl());
		Assertions.assertEquals(credential.getUsername(), result.getUsername());
		Assertions.assertNotEquals(credential.getPassword(), result.getPassword());
	}

	@Test
	public void editCredential() throws InterruptedException {
		final String fromUrl = "www.gmail.com";
		final String fromUsername = "testing123";
		final String fromPassword = "password";
		Credential oldCredential =
				new Credential(null, fromUrl, fromUsername, null, fromPassword, user.getUserId());
		final String toUrl = "www.hotmail.com";
		final String toUsername = "testing456";
		final String toPassword = "newpassword";
		Credential toCredential =
				new Credential(null, toUrl, toUsername, null, toPassword, null);

		credentialService.storeNewCredential(oldCredential);
		doLogin(user.getUsername(), user.getPassword());
		driver.get("http://localhost:" + this.port);
		homePage = new HomePage(driver);
		homePage.navigateTo("credentials");
		homePage.editFirstCredentialTo(toCredential);
		driver.get("http://localhost:" + this.port);
		homePage.navigateTo("credentials");
		Credential result = homePage.getFirstCredential();
		List<Credential> userCredentials = credentialService.getUserCredentials(user.getUsername());
		String key = userCredentials.get(0).getKey();

		Assertions.assertEquals(toCredential.getUrl(), result.getUrl());
		Assertions.assertEquals(toCredential.getUsername(), result.getUsername());
		Assertions.assertNotEquals(toCredential.getPassword(), result.getPassword());
		Assertions.assertEquals(toCredential.getPassword(), encryptionService.decryptValue(result.getPassword(), key));
	}

	@Test
	public void deleteCredential() {
		final String url = "http://localhost:8000";
		final String username = "test123";
		final String password = "password";
		Credential credential =
				new Credential(null, url, username, null, password, user.getUserId());

		credentialService.storeNewCredential(credential);
		doLogin(user.getUsername(), user.getPassword());
		driver.get("http://localhost:" + this.port);
		homePage = new HomePage(driver);
		homePage.navigateTo("credentials");
		homePage.deleteFirstCredential();
		driver.get("http://localhost:" + this.port);

		Assertions.assertEquals(0, driver.findElements(By.className("credentialRow")).size());
	}
}
