package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id="inputUsername")
    private WebElement inputUsername;

    @FindBy(id="inputPassword")
    private WebElement inputPassword;

    @FindBy(id="loginButton")
    private WebElement loginButton;

    @FindBy(id="signupLink")
    private WebElement signupLink;

    @FindBy(id="invalidUsernameOrPasswordMessage")
    private WebElement invalidUsernameOrPasswordMessage;

    @FindBy(id="successfulLogoutMessage")
    private WebElement successfulLogoutMessage;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        loginButton.click();
    }

    public void useSignupLink() {
        signupLink.click();
    }

    public String getInvalidUsernameOrPasswordMessage() {
        return invalidUsernameOrPasswordMessage.getText();
    }

    public String getsuccessfulLogoutMessage() {
        return successfulLogoutMessage.getText();
    }
}
