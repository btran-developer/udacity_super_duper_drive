package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    @FindBy(id="logoutButton")
    private WebElement logoutButton;

    @FindBy(id="fileUpload")
    private WebElement fileUploadInput;

    @FindBy(className="fileName")
    private WebElement fileName;

    @FindBy(id="nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id="addNoteButton")
    private WebElement addNoteButton;

    @FindBy(id="note-title")
    private WebElement noteTitleInput;

    @FindBy(id="note-description")
    private WebElement noteDescriptionInput;

    @FindBy(id="saveNoteButton")
    private WebElement saveNoteButton;

    @FindBy(className="editNoteButton")
    private WebElement editNoteButton;

    @FindBy(className="deleteNoteButton")
    private WebElement deleteNoteButton;

    @FindBy(className="noteTitle")
    private WebElement noteTitle;

    @FindBy(className="noteDescription")
    private WebElement noteDescription;

    @FindBy(id="nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(id="addCredentialButton")
    private WebElement addCredentialButton;

    @FindBy(id="credential-url")
    private WebElement credentialUrlInput;

    @FindBy(id="credential-username")
    private WebElement credentialUsernameInput;

    @FindBy(id="credential-password")
    private WebElement credentialPasswordInput;

    @FindBy(id="saveCredentialButton")
    private WebElement saveCredentialButton;

    @FindBy(className="credentialUrl")
    private WebElement credentialUrl;

    @FindBy(className="credentialUsername")
    private WebElement credentialUsername;

    @FindBy(className="credentialPassword")
    private WebElement credentialPassword;

    @FindBy(className="deleteCredentialButton")
    private WebElement deleteCredentialButton;

    @FindBy(className="editCredentialButton")
    private WebElement editCredentialButton;

    private WebDriverWait wait;

    public HomePage(WebDriver driver ) {
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, 3000);
    }

    public void logout() {
        logoutButton.click();
    }

    public void navigateTo(String tab) {
        switch(tab.toLowerCase()) {
            case "notes":
                navNotesTab.click();
                break;
            case "credentials":
                navCredentialsTab.click();
                break;
        }
    }

    public void uploadFile(String fileDirectory) {
        fileUploadInput.sendKeys(fileDirectory);
        fileUploadInput.submit();
    }

    public String getFirstFileName() throws InterruptedException {
        Thread.sleep(1000);
        return fileName.getText();
    }

    public void addNote(Note note) {
        wait.until(ExpectedConditions.elementToBeClickable(addNoteButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(noteTitleInput)).sendKeys(note.getNoteTitle());
        wait.until(ExpectedConditions.elementToBeClickable(noteDescriptionInput)).sendKeys(note.getNoteDescription());
        wait.until(ExpectedConditions.elementToBeClickable(saveNoteButton)).click();
    }

    public void editFirstNoteTo(Note note) {
        wait.until(ExpectedConditions.elementToBeClickable(editNoteButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(noteTitleInput)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.BACK_SPACE));
        noteTitleInput.sendKeys(note.getNoteTitle());
        wait.until(ExpectedConditions.elementToBeClickable(noteDescriptionInput)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.BACK_SPACE));
        noteDescriptionInput.sendKeys(note.getNoteDescription());
        wait.until(ExpectedConditions.elementToBeClickable(saveNoteButton)).click();
    }

    public void deleteFirstNote() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteNoteButton)).click();
    }

    public Note getFirstNote() throws InterruptedException {
        Thread.sleep(1000);
        Note note = new Note(null, noteTitle.getText(), noteDescription.getText(), null);
        return note;
    }

    public void addCredential(Credential credential) {
        wait.until(ExpectedConditions.elementToBeClickable(addCredentialButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(credentialUrlInput)).sendKeys(credential.getUrl());
        wait.until(ExpectedConditions.elementToBeClickable(credentialUsernameInput)).sendKeys(credential.getUsername());
        wait.until(ExpectedConditions.elementToBeClickable(credentialPasswordInput)).sendKeys(credential.getPassword());
        wait.until(ExpectedConditions.elementToBeClickable(saveCredentialButton)).click();
    }

    public void editFirstCredentialTo(Credential credential) {
        wait.until(ExpectedConditions.elementToBeClickable(editCredentialButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(credentialUrlInput)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.BACK_SPACE));
        credentialUrlInput.sendKeys(credential.getUrl());
        wait.until(ExpectedConditions.elementToBeClickable(credentialUsernameInput)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.BACK_SPACE));
        credentialUsernameInput.sendKeys(credential.getUsername());
        wait.until(ExpectedConditions.elementToBeClickable(credentialPasswordInput)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.BACK_SPACE));
        credentialPasswordInput.sendKeys(credential.getPassword());
        wait.until(ExpectedConditions.elementToBeClickable(saveCredentialButton)).click();
    }

    public void deleteFirstCredential() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteCredentialButton)).click();
    }

    public Credential getFirstCredential() throws InterruptedException {
        Thread.sleep(1000);
        Credential credential =
                new Credential(null, credentialUrl.getText(), credentialUsername.getText(), null, credentialPassword.getText(), null);
        return credential;
    }
}
