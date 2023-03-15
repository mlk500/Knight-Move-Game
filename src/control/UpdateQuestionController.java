package control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.Question;
import model.QuestionUtilities;
import model.SysData;
import model.Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;

import javafx.scene.control.CheckBox;

public class UpdateQuestionController implements Initializable {

	@FXML
	private CheckBox answer1Check;

	@FXML
	private TextField answer1Txt;

	@FXML
	private CheckBox answer2Check;

	@FXML
	private TextField answer2Txt;

	@FXML
	private CheckBox answer3Check;

	@FXML
	private TextField answer3Txt;

	@FXML
	private CheckBox answer4Check;

	@FXML
	private TextField answer4Txt;

	@FXML
	private Button cancelBtn;

	@FXML
	private ComboBox<String> levelsComboBox;

	@FXML
	private TextField questionTxt;

	@FXML
	private ComboBox<String> teamCombobox;

	@FXML
	private Button updateQuestionBtn;

	private int qID;
	private String newQuestionBody;
	private String newAnswer1;
	private String newAnswer2;
	private String newAnswer3;
	private String newAnswer4;
	private int newCheckedAnswer;
	private int newLevel;
	private String newTeam;
	QuestionUtilities questionUtilities = QuestionUtilities.getInstance();
	Utilities utilities = Utilities.getInstance();
	HashMap<Integer, CheckBox> correctAnswer = new HashMap<Integer, CheckBox>();
	SysData sysData = SysData.getInstance();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Question q = sysData.getQuestionByID(qID);
		initData(q);
		initNewFields(q);
		this.updateQuestionBtn.setDisable(true);

		questionTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(q.getQuestion())) {
				if (!utilities.containsWhiteSpacesOnly(newValue)) {
					this.newQuestionBody = newValue.trim();
					this.updateQuestionBtn.setDisable(false);
				}
			}
		});

		answer1Txt.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(q.getAnswers().get(0))) {
				if (!utilities.containsWhiteSpacesOnly(newValue)) {
					this.newAnswer1 = newValue.trim();
					this.updateQuestionBtn.setDisable(false);
				}
			}
		});

		answer2Txt.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(q.getAnswers().get(1))) {
				if (!utilities.containsWhiteSpacesOnly(newValue)) {
					this.newAnswer2 = newValue.trim();
					this.updateQuestionBtn.setDisable(false);
				}
			}
		});

		answer3Txt.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(q.getAnswers().get(2))) {
				if (!utilities.containsWhiteSpacesOnly(newValue)) {
					this.newAnswer3 = newValue.trim();
					this.updateQuestionBtn.setDisable(false);
				}
			}
		});

		answer4Txt.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equals(q.getAnswers().get(3))) {
				if (!utilities.containsWhiteSpacesOnly(newValue)) {
					this.newAnswer4 = newValue.trim();
					this.updateQuestionBtn.setDisable(false);
				}
			}
		});

		this.answer1Check.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (!answer1Check.equals(this.correctAnswer.get(q.getCorrectAnswer()))) {
				this.updateQuestionBtn.setDisable(false);
			}
		});

		this.answer2Check.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (!answer2Check.equals(this.correctAnswer.get(q.getCorrectAnswer())))
				this.updateQuestionBtn.setDisable(false);
		});

		this.answer3Check.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (!answer3Check.equals(this.correctAnswer.get(q.getCorrectAnswer())))
				this.updateQuestionBtn.setDisable(false);
		});

		this.answer4Check.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (!answer1Check.equals(this.correctAnswer.get(q.getCorrectAnswer())))
				this.updateQuestionBtn.setDisable(false);
		});

		this.levelsComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (!newValue.equals(questionUtilities.getQuestionLevels()[(q.getLevel() - 1)])) {
				this.updateQuestionBtn.setDisable(false);
				this.setNewLevel(questionUtilities.convertLeveltoNumber(newValue));
			}

		});

		this.teamCombobox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (!newValue.equals(q.getTeam()))
				this.updateQuestionBtn.setDisable(false);
		});
	}

	private void initNewFields(Question q) {
		this.setNewQuestionBody(q.getQuestion());
		this.setNewAnswer1(q.getAnswers().get(0));
		this.setNewAnswer2(q.getAnswers().get(1));
		this.setNewAnswer3(q.getAnswers().get(2));
		this.setNewAnswer4(q.getAnswers().get(3));
		this.setNewCheckedAnswer(q.getCorrectAnswer());
		this.setNewLevel(q.getLevel());
		this.setNewTeam(q.getTeam());

	}

	// Event Listener on Button[#updateQuestionBtn].onAction
	@FXML
	public void updateBtnAction(ActionEvent event) throws IOException {
//		this.newAnswer1, this.newAnswer2, this.newAnswer3,
//		this.newAnswer4
		List<String> answers = new ArrayList<>();
		answers.add(newAnswer1);
		answers.add(newAnswer2);
		answers.add(newAnswer3);
		answers.add(newAnswer4);
		// check if all answers has been filled
		Optional<String> result = answers.stream().filter(answer -> (utilities.containsWhiteSpacesOnly(answer) == true))
				.findFirst();

		answers.replaceAll(String::trim);

		HashMap<String, Integer> checkboxResult = getCheckboxSelection();
		if (checkboxResult.get("counter") != 1) {
			Alerts.alertBox(AlertType.ERROR, "Failed", "Invalid input", "Please choose one answer only");
		}

		if (!result.isEmpty())
			Alerts.alertBox(AlertType.ERROR, "Failed", "Invalid input", "Please fill all required fields");
		// check if same answer has been filled more than once
		else if (answers.stream().distinct().count() != 4) {
			Alerts.alertBox(AlertType.ERROR, "Failed", "Duplicate Answers", "Answers should be distinct");
		}

		else {
			Question question = new Question(this.getNewQuestionBody(), answers,
					(int) (checkboxResult.get("answerChecked")), this.newLevel, newTeam);
			sysData.updateQuestion(this.qID, question);
			Alerts.alertBox(AlertType.INFORMATION, "Success", "The questions has been updated successfully", "");
			utilities.switchScene(event, "/view/QuestionScene.fxml");
		}
	}

	private List<String> getAnswers() {
		List<String> answers = new ArrayList<String>();
		answers.add(this.newAnswer1);
		answers.add(this.newAnswer2);
		answers.add(this.newAnswer3);
		answers.add(this.newAnswer4);
		return answers;
	}

	@FXML
	public void cancelAction(ActionEvent event) throws IOException {
		utilities.switchScene(event, "/view/QuestionScene.fxml");
	}

	public void initData(Question q) {
		initAnswers();
		levelsComboBox.getItems().addAll(questionUtilities.getQuestionLevels());
		this.teamCombobox.getItems().addAll(questionUtilities.getTeams());
		if (q != null) {
			this.questionTxt.setText(q.getQuestion());
			this.answer1Txt.setText(q.getAnswers().get(0));
			this.answer2Txt.setText(q.getAnswers().get(1));
			this.answer3Txt.setText(q.getAnswers().get(2));
			this.answer4Txt.setText(q.getAnswers().get(3));
			this.correctAnswer.get(q.getCorrectAnswer()).setSelected(true);
			this.levelsComboBox.getSelectionModel().select(questionUtilities.getQuestionLevels()[(q.getLevel() - 1)]);
			this.teamCombobox.getSelectionModel().select(questionUtilities.getTeams().indexOf(q.getTeam()));
		}
	}

	private void initAnswers() {
		correctAnswer.put(1, this.answer1Check);
		correctAnswer.put(2, this.answer2Check);
		correctAnswer.put(3, this.answer3Check);
		correctAnswer.put(4, this.answer4Check);
	}

	private HashMap<String, Integer> getCheckboxSelection() {
		HashMap<String, Integer> results = new HashMap<>();
		int counter = 0;
		results.put("counter", counter);
		results.put("answerChecked", 0);
		if (this.answer1Check.isSelected()) {
			++counter;
			results.put("counter", counter);
			results.put("answerChecked", 1);
		}

		if (this.answer2Check.isSelected()) {
			++counter;
			results.put("counter", counter);
			results.put("answerChecked", 2);
		}

		if (this.answer3Check.isSelected()) {
			++counter;
			results.put("counter", counter);
			results.put("answerChecked", 3);
		}
		if (this.answer4Check.isSelected()) {
			++counter;
			results.put("counter", counter);
			results.put("answerChecked", 4);
		}

		return results;

	}

	public int getqID() {
		return qID;
	}

	public void setqID(int qID) {
		this.qID = qID;
	}

	public String getNewQuestionBody() {
		return newQuestionBody;
	}

	public void setNewQuestionBody(String newQuestionBody) {
		this.newQuestionBody = newQuestionBody;
	}

	public String getNewAnswer1() {
		return newAnswer1;
	}

	public void setNewAnswer1(String newAnswer1) {
		this.newAnswer1 = newAnswer1;
	}

	public String getNewAnswer2() {
		return newAnswer2;
	}

	public void setNewAnswer2(String newAnswer2) {
		this.newAnswer2 = newAnswer2;
	}

	public String getNewAnswer3() {
		return newAnswer3;
	}

	public void setNewAnswer3(String newAnswer3) {
		this.newAnswer3 = newAnswer3;
	}

	public String getNewAnswer4() {
		return newAnswer4;
	}

	public void setNewAnswer4(String newAnswer4) {
		this.newAnswer4 = newAnswer4;
	}

	public int getNewCheckedAnswer() {
		return newCheckedAnswer;
	}

	public void setNewCheckedAnswer(int newCheckedAnswer) {
		this.newCheckedAnswer = newCheckedAnswer;
	}

	public int getNewLevel() {
		return newLevel;
	}

	public void setNewLevel(int newLevel) {
		this.newLevel = newLevel;
	}

	public String getNewTeam() {
		return newTeam;
	}

	public void setNewTeam(String newTeam) {
		this.newTeam = newTeam;
	}

}
