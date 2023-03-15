package control;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import model.Utilities;

public class InstructionsController {
	Utilities utilities = Utilities.getInstance();

    @FXML
    private Tab charactersButton;

    @FXML
    private Tab level1Button;

    @FXML
    private Tab level2Button;

    @FXML
    private Tab level3Button;

    @FXML
    private Tab level4Button;

    @FXML
    private Tab tilesButton;
    @FXML
    void BackToMenu(ActionEvent event) throws IOException {
		utilities.switchScene(event, "/view/Menu.fxml");

    }
}
