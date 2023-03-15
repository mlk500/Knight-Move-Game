package control;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.Utilities;

public class InsertNicknameController {
	public static boolean nicknameInserted=false;
	Utilities utilities = Utilities.getInstance();
	public static String name;
    @FXML
    private TextField Nickname;

    @FXML
    private Button Ok;


    @FXML
    void OkAction(ActionEvent event) throws IOException {
    	name=Nickname.getText();
		if (name=="" ) {
			Alerts.alertBox(AlertType.ERROR, "Failed", "Invalid input", "Please fill all required fields!");
		}else if (utilities.containsWhiteSpacesOnly(name) ) {
			Alerts.alertBox(AlertType.ERROR, "Failed", "Invalid input", " A Nickname must begin with a letter and cannot contain spaces!");
		}else
		{
			nicknameInserted=true;
			name=name.trim();
			utilities.switchScene(event, "/view/LevelsMap.fxml");
		}
    }
    @FXML
    void BackToMenu(ActionEvent event) throws IOException {
		utilities.switchScene(event, "/view/Menu.fxml");

    }

}

