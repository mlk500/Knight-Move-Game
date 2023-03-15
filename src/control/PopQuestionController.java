package control;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Question;
import model.QuestionUtilities;
import model.Utilities;

public class PopQuestionController implements Initializable {
	Utilities utilities = Utilities.getInstance();
	private Question question;
	int correctAnswer;
	int levelQuestion;
	int levelQame;

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
	private Button checkBtn;

	@FXML
	private TextField questionTxt;
	@FXML
	private Label difficulty;

	@FXML
	private Label team;

	HashMap<Integer, Integer> correctScores = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> wrongScores = new HashMap<Integer, Integer>();
	private Scene preScene;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		disableEdit();
		// show the question
		correctAnswer = question.getCorrectAnswer();
		levelQuestion = question.getCorrectAnswer();
		questionTxt.setText(question.getQuestion());
		List<String> answers = question.getAnswers();
		answer1Txt.setText(answers.get(0));
		answer2Txt.setText(answers.get(1));
		answer3Txt.setText(answers.get(2));
		answer4Txt.setText(answers.get(3));
		this.difficulty.setText(
				"Difficulty: " + QuestionUtilities.getInstance().getQuestionLevels()[(question.getLevel() - 1)]);
		this.team.setText("Team: " + question.getTeam());
		initScoreMaps();

	}

	private void disableEdit() {
		this.questionTxt.setDisable(true);
		this.answer1Txt.setDisable(true);
		this.answer2Txt.setDisable(true);
		this.answer3Txt.setDisable(true);
		this.answer4Txt.setDisable(true);

	}

	// method that stores how many points to add/deduct according to level
	private void initScoreMaps() {
		for (int i = 1; i < 4; i++) {
			this.correctScores.put(i, i);
			this.wrongScores.put(i, i + 1);
		}

	}

	public void setPreScene(Scene preScene) {
		this.preScene = preScene;
	}

	@FXML
	void checkBtn(ActionEvent event) throws IOException {
		if ((!answer1Check.isSelected()) && (!answer2Check.isSelected()) && (!answer3Check.isSelected())
				&& (!answer4Check.isSelected())) {
			Alerts.alertBox(AlertType.ERROR, "", "You have to select an answer", null);
			return;
		}
		if (answer1Check.isSelected() && this.correctAnswer == 1) {
			rightAnswerAction(event);
		}

		else if (answer2Check.isSelected() && this.correctAnswer == 2) {
			rightAnswerAction(event);

		}

		else if (answer3Check.isSelected() && this.correctAnswer == 3) {
			rightAnswerAction(event);

		} else if (answer4Check.isSelected() && this.correctAnswer == 4) {
			rightAnswerAction(event);

		} else {
			int score = editScores(false);
			Alerts.alertBox(AlertType.INFORMATION, "Wrong Answer!",
					"The right answer is \n" + question.getAnswers().get((correctAnswer - 1)) + '\n' + +score
							+ " points" + " were deducted from you ",
					"Level Score " + AbstractLevelControl.getLevelScore() + '\n' + "Total Score "
							+ AbstractLevelControl.getTotalScore());
			returnToLevel(event);
		}

	}

	private void rightAnswerAction(ActionEvent event) {
		int score = editScores(true);
		Alerts.alertBox(AlertType.INFORMATION, "Correct Answer!", "You are awarded with " + score + " points",
				"Level Score " + AbstractLevelControl.getLevelScore() + '\n' + "Total Score "
						+ AbstractLevelControl.getTotalScore());
		returnToLevel(event);

	}

	private int editScores(boolean isCorrect) {
		int score;
		if (isCorrect) {
			score = this.correctScores.get(question.getLevel());
			// AbstractLevelControl.setLevelScore(AbstractLevelControl.getLevelScore() +
			// score);
			AbstractLevelControl.levelScoreCode = AbstractLevelControl.getLevelScore() + score;
			AbstractLevelControl.setLevelScore(AbstractLevelControl.levelScoreCode);
			AbstractLevelControl.setTotalAndLevelScore();

			return score;
		} else {
			score = this.wrongScores.get(question.getLevel());
			// AbstractLevelControl.setLevelScore(AbstractLevelControl.getLevelScore() -
			// score);
			AbstractLevelControl.levelScoreCode = AbstractLevelControl.getLevelScore() - score;
			AbstractLevelControl.setLevelScore(AbstractLevelControl.levelScoreCode);
			AbstractLevelControl.setTotalAndLevelScore();
			return score;
		}

	}

	private void returnToLevel(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(preScene);
		AbstractLevelControl.pauseChar.set(AbstractLevelControl.RESUME_LEVEL);
		stage.show();
	}

	@FXML
	void answer1Check(ActionEvent event) {
		this.answer1Check.setSelected(true);
		this.answer2Check.setSelected(false);
		this.answer3Check.setSelected(false);
		this.answer4Check.setSelected(false);
	}

	@FXML
	void answer2Check(ActionEvent event) {
		this.answer1Check.setSelected(false);
		this.answer2Check.setSelected(true);
		this.answer3Check.setSelected(false);
		this.answer4Check.setSelected(false);
	}

	@FXML
	void answer3Check(ActionEvent event) {
		this.answer1Check.setSelected(false);
		this.answer2Check.setSelected(false);
		this.answer3Check.setSelected(true);
		this.answer4Check.setSelected(false);
	}

	@FXML
	void answer4Check(ActionEvent event) {
		this.answer1Check.setSelected(false);
		this.answer2Check.setSelected(false);
		this.answer3Check.setSelected(false);
		this.answer4Check.setSelected(true);
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		System.out.println(question);
		this.question = question;
	}

}