package uy.edu.ude.sipro.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

//@SpringUI(path="login2")
public class LoginUI extends UI{

	@Override
		protected void init(VaadinRequest request) {
			// TODO Auto-generated method stub
			FormLayout nameLayout = new FormLayout();

	        TextField username = new TextField();
	        username.setCaption("Username");
	        username.setPlaceholder("username");

	        PasswordField passwordField = new PasswordField();
	        passwordField.setCaption("Password");
	        passwordField.setPlaceholder("***");

	        Button loginButton = new Button("login");

	        loginButton.addClickListener(event -> {
	        });

	        nameLayout.addComponent(username);
	        nameLayout.addComponent(passwordField);
	        setContent(nameLayout);
		}

	}
	
