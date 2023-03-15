package control;

import java.io.IOException;
import java.util.Optional;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.SysData;
import model.Utilities;

public class QuestionItemController{
	Utilities utilities = Utilities.getInstance();

	private int qId;

    @FXML
    private Label answerLbl;

    @FXML
    private Button deleteQuestionBtn;

    @FXML
    private Button editQuestionBtn;

    @FXML
    private ImageView imgLevel;

    @FXML
    private Button openQuestionBtn;

    @FXML
    private Label questionLbl;
	
	 public int getqId() {
		return qId;
	}



	public void setqId(int qId) {
		this.qId = qId;
	}



	public Label getAnswerLbl() {
		return answerLbl;
	}



	public void setAnswerLbl(Label answerLbl) {
		this.answerLbl = answerLbl;
	}



	public ImageView getImgLevel() {
		return imgLevel;
	}



	public void setImgLevel(ImageView imgLevel) {
		this.imgLevel = imgLevel;
	}



	public Label getQuestionLbl() {
		return questionLbl;
	}



	public void setQuestionLbl(Label questionLbl) {
		this.questionLbl = questionLbl;
	}



	@FXML
	    void deleteQuestion(ActionEvent event) throws IOException {
		 Alert conf = Alerts.createConfirmation(AlertType.CONFIRMATION, "Delete Question", "Are you sure you want to delete this question?\nIf you press yes, it will be permanently deleted", 
				 null);	 
		 Optional<ButtonType> result = conf.showAndWait();
		 if (result.isPresent() && result.get() == conf.getButtonTypes().get(0)) {
			 boolean ans = SysData.getInstance().deleteQuestion(this.qId);
			 if(ans == true) {
				 Alerts.alertBox(AlertType.INFORMATION, "Question removal", "Question has been deleted successfully", null);
				 utilities.switchScene(event, "/view/QuestionScene.fxml");
			 }
	    }
	 }

	    @FXML
	    void editQuestion(ActionEvent event) throws IOException {
	    	Stage stage;
	    	UpdateQuestionController updateQuestionController = new UpdateQuestionController();
	    	FXMLLoader loader = utilities.getSceneController("/view/UpdateQuestion.fxml");
			loader.setController(updateQuestionController);
			updateQuestionController.setqID(this.qId);
			utilities.loadScene(event, loader);
	    }

	    @FXML
	    void openQuestion(ActionEvent event) throws IOException {
	    	System.out.println("qID in view "+this.qId);
	    	Stage stage;
	    	ViewQuestionController viewQuestionController = new ViewQuestionController();
	    	
	    	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/ViewQuestion.fxml"));
			loader.setController(viewQuestionController);
			viewQuestionController.setqID(this.qId);			
			Parent root = loader.load();			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
			stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
	    }
	    
	    public void setImage(int level) {
	    	if(level == 1) {
	    		imgLevel.setImage(new Image(getClass().getResourceAsStream("/images/EsayIcon.png")));
	    	}
	    	else if(level == 2) {
	    		imgLevel.setImage(new Image(getClass().getResourceAsStream("/images/MeduimIcon.png")));
	    	}
	    	else {
	    		imgLevel.setImage(new Image(getClass().getResourceAsStream("/images/HardIcon.png")));
	    	}
	    }
	
	
}
