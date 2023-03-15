package control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Question;
import model.QuestionUtilities;
import model.SysData;
import model.Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.CheckBox;

public class ViewQuestionController implements Initializable {
	@FXML
	private TextField questionTxt;
	@FXML
	private TextField answer1Txt;
	@FXML
	private CheckBox answer1Check;
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
	private Label difficultyLbl;
	@FXML
	private Label teamLbl;
	@FXML
	private Button backBtn;
	private int qID;
	SysData sysData = SysData.getInstance();
	Utilities utilities = Utilities.getInstance();
	QuestionUtilities questionUtilities = QuestionUtilities.getInstance();
	HashMap<Integer, CheckBox> correctAnswer = new HashMap<Integer, CheckBox>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Question q = sysData.getQuestionByID(qID);
		initData(q);
		disableActions();

	}
	
	//disable editing the fields in view mode
	private void disableActions() {
		this.questionTxt.setEditable(false);
		this.answer1Txt.setEditable(false);
		this.answer2Txt.setEditable(false);
		this.answer3Txt.setEditable(false);
		this.answer4Txt.setEditable(false);
		this.answer1Check.setDisable(true);
		this.answer2Check.setDisable(true);
		this.answer3Check.setDisable(true);
		this.answer4Check.setDisable(true);

	}

	// init data with the question's data
	public void initData(Question q) {
		initAnswers();
		if (q != null) {
			this.questionTxt.setText(q.getQuestion());
			this.answer1Txt.setText(q.getAnswers().get(0));
			this.answer2Txt.setText(q.getAnswers().get(1));
			this.answer3Txt.setText(q.getAnswers().get(2));
			this.answer4Txt.setText(q.getAnswers().get(3));
			this.correctAnswer.get(q.getCorrectAnswer()).setSelected(true);
			this.difficultyLbl.setText(questionUtilities.getQuestionLevels()[(q.getLevel() - 1)]);
			this.teamLbl.setText(q.getTeam());
		}
	}

	private void initAnswers() {
		correctAnswer.put(1, this.answer1Check);
		correctAnswer.put(2, this.answer2Check);
		correctAnswer.put(3, this.answer3Check);
		correctAnswer.put(4, this.answer4Check);
	}

	@FXML
	private void BackBtn(ActionEvent event) throws IOException {
		utilities.switchScene(event, "/view/QuestionScene.fxml");
	}

	public int getqID() {
		return qID;
	}

	public void setqID(int qID) {
		this.qID = qID;
	}

}
