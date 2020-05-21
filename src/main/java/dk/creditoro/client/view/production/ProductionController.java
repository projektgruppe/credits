package dk.creditoro.client.view.production;


import dk.creditoro.client.core.ViewHandler;
import dk.creditoro.client.core.ViewModelFactory;
import dk.creditoro.client.core.Views;
import dk.creditoro.client.model.crud.Credit;
import dk.creditoro.client.view.IViewController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * The type Browse channels controller.
 */
public class ProductionController implements IViewController {
    private ProductionViewModel productionViewModel;
    private ViewHandler viewHandler;

    @FXML
    public Text cast;
    @FXML
    public Text credit;
    @FXML
    public Label lblStartMenu;
    @FXML
    public TextField search;
    @FXML
    public Label title;

    @Override
    public void init(ViewModelFactory viewModelFactory, ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
        productionViewModel = viewModelFactory.getProductionViewModel();

        //Add Listener to search area
        search.textProperty().bindBidirectional(productionViewModel.queryParamProperty());
        productionViewModel.listPropertyProperty().addListener(((observableValue, credits, newValue) -> updateList(newValue)));
        getCredits();

        //Set title
        title.textProperty().bindBidirectional(productionViewModel.titleProperty());
    }

    @FXML
    private void getCredits() {
        productionViewModel.getCredits();
    }

    @FXML
    private void onSearch() {
        productionViewModel.search();
    }

    public void updateList(ObservableList<Credit> list) {
        //Foreach credit, append credit
        cast.setText("");
        credit.setText("");

        for (Credit c : list) {
            //Get existing text
            String existingCast = cast.getText();
            String existingCredit = credit.getText();

            //Append
            cast.setText(String.format("%n %s", c.getPerson().getName()) + existingCast);
            credit.setText(String.format("%n %s", c.getJob()) + existingCredit);
        }
    }

    @FXML
    public void btnAccount(ActionEvent actionEvent) {
        //Code
    }

    @FXML
    public void btnNewCredit(MouseEvent mouseEvent) {
        viewHandler.openView(Views.ADD_CREDITS);
    }

    @FXML
    public void btnProductions(ActionEvent actionEvent) {
        viewHandler.openView(Views.BROWSE_PRODUCTIONS);
    }

    @FXML
    public void btnSearch(ActionEvent actionEvent) {
        viewHandler.openView(Views.FRONTPAGE);
    }

    @FXML
    public void btnChannels(ActionEvent actionEvent) {
        viewHandler.openView(Views.BROWSE_CHANNELS);
    }

    @FXML
    public void btnFrontPage(MouseEvent mouseEvent) {
        viewHandler.openView(Views.FRONTPAGE);
    }
}
