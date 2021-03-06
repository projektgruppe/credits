package dk.creditoro.client.view.login;

import dk.creditoro.client.core.ClientFactory;
import dk.creditoro.client.core.ModelFactory;
import dk.creditoro.client.core.ViewModelFactory;
import javafx.embed.swing.JFXPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

/**
 * The type Login view model test.
 */
class LoginViewModelTest {
    private final LoginViewModel loginViewModel;

    /**
     * Instantiates a new Login view model test.
     */
    public LoginViewModelTest() {
        var clientFactory = new ClientFactory();
        var modelFactory = new ModelFactory(clientFactory);
        var viewModelFactory = new ViewModelFactory(modelFactory);
        loginViewModel = viewModelFactory.getLoginViewModel();
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JFXPanel(); // initializes JavaFX environment
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }

    @BeforeEach
    void setUp() {
        loginViewModel.emailProperty().setValue("string@string.dk");
        loginViewModel.passwordProperty().setValue("string");
    }

    @Test
    void emailProperty() {
        assertEquals("string@string.dk", loginViewModel.emailProperty().get());
    }

    @Test
    void passwordProperty() {
        assertEquals("string", loginViewModel.passwordProperty().get());
    }

    @Test
    void loginResultProperty() {
		assertDoesNotThrow(()-> loginViewModel.login());
        assertNotNull(loginViewModel.loginResponseProperty().get());
    }

    @Test
    void clearFields() {
        loginViewModel.clearFields();
        assertEquals("", loginViewModel.emailProperty().get());
        assertEquals("", loginViewModel.passwordProperty().get());
    }
}
