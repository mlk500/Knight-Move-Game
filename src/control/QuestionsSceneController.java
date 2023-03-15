package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.AutoCompletionEvent;
import org.controlsfx.control.textfield.TextFields;

import model.Question;
import model.QuestionsListAdapter;
import model.SysData;
import model.Utilities;

public class QuestionsSceneController {

	@FXML
	private ImageView imgLevel;

	@FXML
	private VBox questionsLayout;

	@FXML
	private Label answerLbl;

	@FXML
	private Label questionLbl;

	@FXML
	private Button openQuestionBtn;

	@FXML
	private Button addQuestionBtn;

	@FXML
	private Button deleteQuestionBtn;

	@FXML
	private Button editQuestionBtn;
	@FXML
	private Button BackQuestionBtn;
	private QuestionsListAdapter questionsAdapter;
	
	@FXML
	private TextField searchbar;
	@FXML
	private Button clear;
	@FXML
	private ListView<String> history;
	SysData sysData = SysData.getInstance();
	
	private AutoCompletionBinding<String> autoComplete;
	private Set<String> suggestions;
	private List<Question> questions = sysData.getQuestionsList();
	private List<String> historyList;
	

	@FXML
	public void initialize() {
		history.getItems().addAll(sysData.getHistory());
		refresh(this.questions);
		suggestions = sysData.getSuggestions();
		this.autoComplete = TextFields.bindAutoCompletion(searchbar, sysData.getHistory());
		history.setVisible(false);
		searchbar.setOnMouseClicked(new EventHandler<>() {
			
			@Override
			public void handle(MouseEvent event) {
				if (searchbar.getText().isBlank() || searchbar.getText().length() == 0
						|| searchbar.getText().isEmpty()) {
					historyList = sysData.getHistory();
					Collections.reverse(historyList);
					ObservableList<String> historyObs = FXCollections.observableList(historyList);
					history.setVisible(true);
					history.setItems(historyObs);
					history.prefHeightProperty().bind(Bindings.size(historyObs).multiply(24));

					history.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent event) {
							searchbar.setText(history.getSelectionModel().getSelectedItem());
							search(history.getSelectionModel().getSelectedItem());
							history.setVisible(false);
						}
					});
				}

				else {
					history.setVisible(false);
					autoComplete.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {

						@Override
						public void handle(AutoCompletionEvent<String> event) {
							search(event.getCompletion());
							
						}
					});
				}

			}

		});
		searchbar.setOnKeyPressed(new EventHandler<>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCharacter().length() == 0) {
					history.setVisible(true);
				} else {
					autoComplete.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {

						@Override
						public void handle(AutoCompletionEvent<String> event) {
							search(event.getCompletion());
							
						}
					});
					history.setVisible(false);
				}
				if (event.getCode() == KeyCode.ENTER) {
					if(!Utilities.getInstance().containsWhiteSpacesOnly(searchbar.getText())) {
						suggestions.add(searchbar.getText().trim());
						if (autoComplete != null)
							autoComplete.dispose();
						autoComplete = TextFields.bindAutoCompletion(searchbar, suggestions);
						search(searchbar.getText().trim());
						sysData.addSuggestion(searchbar.getText().trim());
						sysData.addHistory(searchbar.getText().trim());
					}
					
				}
			}

		});
	}

	@FXML
	void addQuestion(ActionEvent event) throws IOException {
		Utilities.getInstance().switchScene(event, "/view/AddQuestionScene.fxml");
	}

	@FXML
	void BackQuestion(ActionEvent event) throws IOException {
		Utilities.getInstance().switchScene(event, "/view/Menu.fxml");
	}

	void refresh(List<Question> questionsScrollPane) {
		questionsLayout.getChildren().clear();
		questionsAdapter = new QuestionsListAdapter(questionsScrollPane, questionsLayout);
		this.questionsLayout = (VBox) questionsAdapter.getVbox();
	}

	void search(String searchStr) {
		List<Question> toReturn = new ArrayList<>();
		for (Question q : questions) {
			if (q.containsText(searchStr))
				toReturn.add(q);
		}
		refresh(toReturn);
	}

	@FXML
	void clearBtn(ActionEvent event) {
		this.searchbar.clear();
		this.history.setVisible(false);
		refresh(this.questions);
	}

}
