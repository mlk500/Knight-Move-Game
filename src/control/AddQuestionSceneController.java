package control;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import model.Question;
import model.QuestionUtilities;
import model.SysData;
import model.Utilities;
import javafx.scene.control.ComboBox;

import javafx.scene.control.CheckBox;

public class AddQuestionSceneController implements Initializable {
	Utilities utilities = Utilities.getInstance();
	QuestionUtilities questionUtilities = QuestionUtilities.getInstance();

	@FXML
	private TextField questionTxt;
	@FXML
	private TextField answer3Txt;
	@FXML
	private CheckBox answer3Check;
	@FXML
	private TextField answer2Txt;
	@FXML
	private CheckBox answer2Check;
	@FXML
	private TextField answer4Txt;
	@FXML
	private CheckBox answer4Check;
	@FXML
	private TextField answer1Txt;
	@FXML
	private CheckBox answer1Check;
	@FXML
	private ComboBox<String> levelsComboBox;
	@FXML
	private ComboBox<String> teamCombobox;
	@FXML
	private Button addQuestionBtn;
	@FXML
	private Button cancelBtn;
	
	SysData sysData  = SysData.getInstance();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		levelsComboBox.getItems().addAll(questionUtilities.getQuestionLevels());
		this.teamCombobox.getItems().addAll(questionUtilities.getTeams());
		this.teamCombobox.getSelectionModel().select(questionUtilities.getTeams().get(0)); // setting "Spider" option as
																							// default in combobox
	}

	public void addBtnAction(ActionEvent event) throws IOException {
		//getting question's data
		String questionBody = getQuestion();
		List<String> answers = questionUtilities.getAnswers(this.answer1Txt, this.answer2Txt, this.answer3Txt,
				this.answer4Txt);
		HashMap<String, Integer> checkboxResult = questionUtilities.getCheckboxSelection(this.answer1Check,
				this.answer2Check, this.answer3Check, this.answer4Check);
		String level = getLevel();
		String team = getTeam();

		// check if all answers has been filled
		Optional<String> result = answers.stream().filter(answer -> (utilities.containsWhiteSpacesOnly(answer) == true))
				.findFirst();

		answers.replaceAll(String::trim);

		if (!result.isEmpty())
			Alerts.alertBox(AlertType.ERROR, "Failed", "Invalid input", "Please fill all required fields");
		// check if same answer has been filled more than once
		else if (answers.stream().distinct().count() != 4) {
			Alerts.alertBox(AlertType.ERROR, "Failed", "Duplicate Answers", "Answers should be distinct");
		}

		// check if question is not empty
		else if (utilities.containsWhiteSpacesOnly(questionBody)) {
			Alerts.alertBox(AlertType.ERROR, "Failed", "Invalid input", "Please fill all required fields");
		}
		else if(sysData.isDuplicateQuestion(questionBody.trim()))
			Alerts.alertBox(AlertType.ERROR, "Failed", "Duplicate Questions", "The question already exists");

		// check if user selected one checkbox only as correct answer
		else if (checkboxResult.get("counter") != 1) {
			Alerts.alertBox(AlertType.ERROR, "Failed", "Invalid input", "Please choose one answer only");

			// checking if user selected a level for question
		} else if (level == null) {
			Alerts.alertBox(AlertType.ERROR, "Failed", "Invalid input", "Please choose the question's difficulty");

		} else

		{
			Question question = new Question(questionBody.trim(), answers, (int) (checkboxResult.get("answerChecked")),
					questionUtilities.convertLeveltoNumber(level), team);
			sysData.addQuestion(question);
			Alerts.alertBox(AlertType.INFORMATION, "Success", "The questions has been added successfully", "");
			utilities.switchScene(event, "/view/QuestionScene.fxml");
		}

	}

	private String getLevel() {
		return this.levelsComboBox.getValue();
	}

	private String getTeam() {
		return this.teamCombobox.getValue();
	}

	private String getQuestion() {
		return this.questionTxt.getText();
	}

	@FXML
	void cancelAction(ActionEvent event) throws IOException {
		utilities.switchScene(event, "/view/QuestionScene.fxml");
	}

}
