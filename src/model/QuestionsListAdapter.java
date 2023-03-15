package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import control.QuestionItemController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class QuestionsListAdapter implements ListAdapter{
	private List<Question> questions;
	private Pane vbox;
	private QuestionItemController questionItemController;

	public QuestionsListAdapter(List<Question> questions, Pane questionsLayout) {
		super();
		this.questions = new ArrayList<>(questions);
		this.vbox = questionsLayout;
		setData();
	}


	public void setData() {
		for (int i = 0; i<this.questions.size();i++) {
			vbox.getChildren().add(getLayout(i));
		}		
	}


	@Override
	public Pane getLayout(int index) {
		Pane hbox = new HBox();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/QuestionItem.fxml"));
		Question q = this.questions.get(index);
		try {
			hbox = loader.load();
			this.questionItemController = loader.getController();
			questionItemController.getQuestionLbl().setText(q.getQuestion());
			questionItemController.getAnswerLbl().setText(q.getAnswers().get(q.getCorrectAnswer()-1));
			questionItemController.setqId(q.getQuestionID());
			questionItemController.setImage(q.getLevel());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hbox;
	}


	public Pane getVbox() {
		return vbox;
	}
}
