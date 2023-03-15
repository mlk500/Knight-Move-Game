package control;

import javafx.scene.control.Alert;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class Alerts {
	//we created an alert class to save an alert in it and each time we wanna create an alert we just call this alert class instead of declaring a new alert each time Alerts.AlertBox
	public static void alertBox(AlertType alertType, String title, String head,String content) {
		if(alertType.equals(AlertType.CONFIRMATION)) {
			createConfirmation(alertType, title, head, content);
		}
		else {
			Alert alert = new Alert(alertType);
			alert.setTitle(title);
			alert.setHeaderText(head);
			alert.setContentText(content);
			alert.showAndWait();
		}

	}

	public static Alert createConfirmation(AlertType alertType, String title, String head, String content) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		ButtonType okButton = new ButtonType("Yes", ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(okButton, noButton);
		
		alert.setHeaderText(head);
		alert.setContentText(content);
		
		
		return alert;
		
	}
	
	public static Alert createAlertEndTime(AlertType alertType, String title, String head, String content) {
		Alert alert = new Alert(Alert.AlertType.NONE);
		alert.setTitle(title);
		ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
		
		alert.getButtonTypes().setAll(okButton);
		
		alert.setHeaderText(head);
		alert.setContentText(content);
		
		
		return alert;
		
	}
	
	public static Alert inGameMsgs(AlertType alertType, String title, String head, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
		
		alert.getButtonTypes().setAll(okButton);
		
		alert.setHeaderText(head);
		alert.setContentText(content);
		
		
		return alert;
		
	}
}

