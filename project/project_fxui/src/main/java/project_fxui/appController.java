package project_fxui;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import project_core.Account;
import project_core.Message;
import project_core.io.AccountIO;

public class appController {
	
	@FXML private AnchorPane loginPane, CreateAccountPane;
	@FXML private SplitPane splitPane;
	@FXML private Label inboxLabel, welcomeLabel, emailLabel, errorLabel, toLabel, fromLabel;
	@FXML private TextField emailField, toField, fromField, subjectField, txt_C_Email;
	@FXML private PasswordField passwordField, txt_C_password;
	@FXML private TextArea textArea;
	@FXML private ListView<String> inbox;
	@FXML private Button loginButton, logoutButton, newMessageButton, sendButton, btnConfirm;
	
	private Account currentAccount;
	
	/**
	 * Makes the app visible
	 */
	private void appVisibility() {
		loginPane.setVisible(false);
		splitPane.setVisible(true);
		CreateAccountPane.setVisible(false);
	}
	
	/**
	 * Makes the login menu visible
	 */
	private void loginVisibility() {
		loginPane.setVisible(true);
		splitPane.setVisible(false);
		CreateAccountPane.setVisible(false);
	}
	
	private void createAccountVisiblity() {
		loginPane.setVisible(false);
		splitPane.setVisible(false);
		CreateAccountPane.setVisible(true);
	}
	
	/**
	 * This method clears text in the app: Inbox, Textareas, TextFields, other Labels, etc.
	 */
	private void clear() {
		inbox.getItems().clear();
		textArea.setText("");
		toLabel.setText("To: ");
		fromLabel.setText("From: ");
		passwordField.setText("");
		errorLabel.setText("");
		toField.setText("");
		fromField.setText("");
		subjectField.setText("");
	}
	
	/**
	 * Checks if the login is valid with accounts in the system. If so the mail application will be visible.
	 */
	public void loginCheck() {
		String emailInput = emailField.getText();
		String passwordInput = passwordField.getText();
		Account account = new Account(emailInput, passwordInput);
		try {
			if (account.isValid()) {
				currentAccount = account;
				clear();
				appVisibility();
				updateInbox();
			}
			else {
				errorLabel.setText("Error: No username/password combination like that.");
				errorLabel.setVisible(true);
			}
		} catch (IOException e) {
			errorLabel.setText("Error: " + e.getMessage());
			errorLabel.setVisible(true);
			System.out.println(AccountIO.resourceFilepath);
		}
		
	}
	
	/**
	 * logs this account out, and returns back to the login menu.
	 */
	public void logout() {
		clear();
		this.currentAccount = null;
		loginVisibility();
	}
	
	/**
	 * This method initializes the new message-screen for the user
	 * 
	 * (Not done)
	 */
	public void initNewMessage() {
		textArea.setText("");
		toField.setText("");
		fromField.setText("");
		subjectField.setText("");
		textArea.setEditable(true);
	}
	
	/**
	 * Sends the message with the current information filled in.
	 */
	public void sendMessage() {
		Account toAccount = new Account(toField.getText());
		String subject = subjectField.getText();
		String text = textArea.getText();
		Message message = new Message(subject , text, toAccount, currentAccount);
		
		try {
			currentAccount.sendMessage(message, toAccount);
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		// Bare for � teste
		updateInbox();
		
		System.out.println("Message sent");
	}
	
	/**
	 * Loads the messages of the system to the inbox of the current account.
	 * Then adds the subjects of the messages to the inbox-UI.
	 */
	public void updateInbox() {
		
		try {
			currentAccount.getInbox().loadMessages();
		} catch (IOException e) {
			System.out.println("Couldn't load new messages.");
		}
		
		List<String> subjects = currentAccount.getInbox().getMessages().stream().map(m -> m.getSubject()).collect(Collectors.toList());
		inbox.getItems().clear();
		inbox.getItems().addAll(subjects);
	}
	
	/**
	 * Deletes the current selected message in the Inbox-UI.
	 * Then it uploads the new inbox to the system. (only to a test file right now)
	 * 
	 * This functionality is only a test right now.
	 */
	public void deleteMessage() {
		int messageIndex = inbox.getSelectionModel().getSelectedIndex();
		if (messageIndex == -1) return;
		
		currentAccount.getInbox().deleteMessage(messageIndex);
		try {
			currentAccount.getInbox().uploadInbox();
		} catch (IOException e) {
			System.out.println("Couldn't edit the Inbox file");
		}
		
		this.updateInbox();
		List<String> subjects = currentAccount.getInbox().getMessages().stream().map(m -> m.getSubject()).collect(Collectors.toList());
		System.out.println(subjects);
		System.out.println(currentAccount.getInbox().getMessages());
		inbox.getItems().clear();
		inbox.getItems().addAll(subjects);
	}
	
	/**
	 * Displays the selected message in the Inbox-UI on the text area.
	 */
	public void displayMessage() {
		int messageIndex = inbox.getSelectionModel().getSelectedIndex();
		if (messageIndex == -1) return;
		Message message = currentAccount.getInbox().getMessage(messageIndex);
		
		textArea.setText(message.getMessage());
		textArea.setEditable(false);
		toField.setText(message.getTo().getMail_address());
		fromField.setText(message.getFrom().getMail_address());
		subjectField.setText(message.getSubject());
	}
	
	/**
	 * Makes the CA pane visible when the user clicks on the "Create Account" button on the login pane
	 */
	
	public void handle_switch_CA() {
		createAccountVisiblity();
	}
	
	/**
	 * Creates a new account object and inputs it to the appIO.
	 * 
	 * Returns to login page after completion.
	 */
	public void handleCreateAccount() {
		//oppretter nytt Acc object og bruker io til � sende det til txt
		String mail = txt_C_Email.getText();
		String password = txt_C_password.getText();
		Account newAccount = new Account(mail, password);
		try {
			newAccount.createAccount();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
		}
		//endre visiblity fra createAccount til login
		loginVisibility();
	}
}