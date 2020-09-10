package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id="inputFirstName")
    private WebElement inputFirstName;

    @FindBy(id="inputLastName")
    private WebElement inputLastName;

    @FindBy(id="inputUsername")
    private WebElement inputUsername;

    @FindBy(id="inputPassword")
    private WebElement inputPassword;

    @FindBy(id="submitButton")
    private WebElement submitButton;

    @FindBy(id="successMessage")
    private WebElement successMessage;

    @FindBy(id="errorMessage")
    private WebElement errorMessage;

    @FindBy(id="loginLink")
    private WebElement loginLink;

    public SignupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void doSignup(String firstName, String lastName, String username, String password) {
        inputFirstName.sendKeys(firstName);
        inputLastName.sendKeys(lastName);
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        submitButton.click();
    }

    public String getSuccessMessage() {
        return successMessage.getText();
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public void useLoginLink() {
        loginLink.click();
    }
}
