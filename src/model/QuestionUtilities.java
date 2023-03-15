package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class QuestionUtilities {
	
	private static QuestionUtilities instance;

	public static QuestionUtilities getInstance() {
		if (instance == null)
			instance = new QuestionUtilities();
		return instance;
	}
	
	public String[] getQuestionLevels() {
		String[] levels = { "Easy", "Meduim", "Hard" };
		return levels;
	}

	public ArrayList<String> getTeams() {
		ArrayList<String> teams = new ArrayList<>(Arrays.asList("Spider", "Husky", "Chimps", "Giraffe", "Tiger"));
		return teams;
	}
	
	public int convertLeveltoNumber(String level) {
		String[] levels = getQuestionLevels();
		if (levels[0].equals(level))
			return 1;
		else if (levels[1].equals(level))
			return 2;
		else
			return 3;
	}
	//function that returns answers as List
	public List<String> getAnswers(TextField answer1Txt, TextField answer2Txt,
			TextField answer3Txt, TextField answer4Txt) {
		List<String> answers = new ArrayList<String>();
		answers.add(answer1Txt.getText());
		answers.add(answer2Txt.getText());
		answers.add(answer3Txt.getText());
		answers.add(answer4Txt.getText());
		return answers;
	}

	//function that returns how many checkboxes has been selected
	public HashMap<String, Integer> getCheckboxSelection(CheckBox answer1Check, CheckBox answer2Check,
			CheckBox answer3Check, CheckBox answer4Check) {
		HashMap<String, Integer> results = new HashMap<>();
		int counter = 0;
		results.put("counter", counter);
		results.put("answerChecked", 0);
		if (answer1Check.isSelected()) {
			++counter;
			results.put("counter", counter);
			results.put("answerChecked", 1);
		}

		if (answer2Check.isSelected()) {
			++counter;
			results.put("counter", counter);
			results.put("answerChecked", 2);
		}

		if (answer3Check.isSelected()) {
			++counter;
			results.put("counter", counter);
			results.put("answerChecked", 3);
		}
		if (answer4Check.isSelected()) {
			++counter;
			results.put("counter", counter);
			results.put("answerChecked", 4);
		}

		return results;

	}

}
