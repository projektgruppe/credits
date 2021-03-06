package dk.creditoro.client.view.add_credits;

import dk.creditoro.client.core.ViewHandler;
import dk.creditoro.client.core.ViewModelFactory;
import dk.creditoro.client.core.Views;
import dk.creditoro.client.model.crud.Credit;
import dk.creditoro.client.model.crud.Person;
import dk.creditoro.client.model.crud.Production;
import dk.creditoro.client.view.IViewController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The type Add credit controller.
 */
public class AddCreditController implements IViewController {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ViewHandler viewHandler;
    private AddCreditViewModel addCreditViewModel;
    private List<Credit> deletedCredits = new ArrayList<>();
    private ViewModelFactory viewModelFactory;

    @FXML
    private TextField channelNameTxtField;
    @FXML
    private TextField productionTitleTxtField;
    @FXML
    private TextField nameTxtField;
    @FXML
    private TextField phoneTxtField;
    @FXML
    private TextField jobTxtField;
    @FXML
    private TextField emailTxtField;
    @FXML
    private ListView<Credit> listView;
    @FXML
    private Button btnAccountE;


    @Override
    public void init(ViewModelFactory viewModelFactory, ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
        this.viewModelFactory = viewModelFactory;
        addCreditViewModel = viewModelFactory.getAddCreditViewModel();
        addCreditViewModel.setAccountBtn(btnAccountE);
        listView.setItems(addCreditViewModel.getCredits());

        // Getting persons from database
        getPeople();

        // Set productionTitle
        productionTitleTxtField.textProperty().bindBidirectional(addCreditViewModel.productionTitleProperty());

        // Set channelName
        channelNameTxtField.textProperty().bindBidirectional(addCreditViewModel.channelNameProperty());
    }

    private void getPeople() {
        addCreditViewModel.getPeople();
    }

    /**
     * Btn account.
     */
    public void btnAccount() {
        LOGGER.info("Account button pressed.");
    }

    /**
     * Add credit on action.
     */
    public void addCreditOnAction() {
        Production production = addCreditViewModel.getProduction();
        Person person;

        String email = emailTxtField.getText();
        String job = jobTxtField.getText();
        String name;
        String phone;

        if (phoneTxtField.isDisabled()) {
            if (addCreditViewModel.getPerson(email) == null) {
                createPopup("Personen blev ikke fundet", "Tilføj venligst et navn og telefonnummer for at oprette personen", 5, Pos.BASELINE_CENTER);
                nameTxtField.setDisable(false);
                phoneTxtField.setDisable(false);
                nameTxtField.requestFocus();

            } else {
                person = addCreditViewModel.getPerson(email);

                clearFields();
                Credit temp = new Credit(null,production,person,job);
                addCreditViewModel.getCredits().add(temp);
                addCreditViewModel.getCreatedCredits().add(temp);

                emailTxtField.requestFocus();
            }
        } else {
            phone = phoneTxtField.getText();
            name = nameTxtField.getText();
            person = new Person(phone, email, name);

            Credit temp = new Credit(null,production,person,job);
            addCreditViewModel.getCredits().add(temp);
            addCreditViewModel.getCreatedCredits().add(temp);

            phoneTxtField.setDisable(true);
            nameTxtField.setDisable(true);
            clearFields();
            emailTxtField.requestFocus();
        }
    }

    private void clearFields() {
        nameTxtField.clear();
        emailTxtField.clear();
        phoneTxtField.clear();
        jobTxtField.clear();
    }

    /**
     * Delete on action.
     */
    public void deleteOnAction() {
        LOGGER.info("Credit deleted");
        deletedCredits.add(listView.getSelectionModel().getSelectedItem());
        addCreditViewModel.getCredits().remove(listView.getSelectionModel().getSelectedItem());
    }

    /**
     * Delete all on action.
     */
    public void deleteOnActionAll() {
        LOGGER.info("Credits deleted");
        deletedCredits.addAll(listView.getSelectionModel().getSelectedItems());
        addCreditViewModel.getCredits().clear();
    }

    /**
     * Exit on action.
     */
    public void exitOnAction() {
        clearFields();
        addCreditViewModel.finishCredits(deletedCredits);
        viewHandler.openView(Views.PRODUCTION);
        viewModelFactory.getProductionViewModel().setAccountEmail();
    }

    /**
     * Btn channels.
     */
    public void btnChannels() {
        clearFields();
        viewHandler.openView(Views.BROWSE_CHANNELS);
        viewModelFactory.getBrowseChannelsViewModel().setMail();
    }

    /**
     * Btn productions.
     */
    public void btnProductions() {
        clearFields();
        viewHandler.openView(Views.BROWSE_PRODUCTIONS);
        viewModelFactory.getBrowseProductionsViewModel().setMail();
    }

    /**
     * Btn front page.
     */
    @FXML
    public void btnFrontPage() {
        clearFields();
        viewHandler.openView(Views.FRONTPAGE);
    }

    /**
     * Create popup.
     *
     * @param title    the title
     * @param text     the text
     * @param duration the duration
     * @param position the position
     */
    public void createPopup(String title, String text, int duration, Pos position) {
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(text)
                .hideAfter(Duration.seconds(duration))
                .position(position)
                .onAction(actionEvent -> LOGGER.info("Pressed popup"));
        notificationBuilder.show();
    }

    /**
     * Export on action.
     */
    public void exportOnAction() {
        LOGGER.info("Eksporterer krediteringer");
        addCreditViewModel.saveFile();
    }

    /**
     * Import on action.
     */
    public void importOnAction() {
        LOGGER.info("Importerer krediteringer");
        addCreditViewModel.openFile();
    }
}
