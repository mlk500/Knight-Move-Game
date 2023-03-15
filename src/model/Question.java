package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Question {
	private int questionID;
	private String question;
	private List<String> answers = new ArrayList<String>();
	private int correctAnswer;
	private int level;
	private String team;

	public Question(String question, List<String> answers, int correctAnswer, int level, String team) {
		super();
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		this.level = level;
		this.team = team;
	}

	public Question(JSONObject jsonObject) {
		try {
			fromJSON(jsonObject);
		} catch (Exception e) {

		}

	}


	public void fromJSON(JSONObject jsonObject) {
		this.question = (String) jsonObject.get("question");

		try {
			JSONArray jsonArray = (JSONArray) jsonObject.get("answers");
			answers.clear();
			for (int i = 0; i < jsonArray.size(); i++) {

				answers.add((String) jsonArray.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		this.correctAnswer = Integer.parseInt(jsonObject.get("correctAnswer").toString());
		this.level = Integer.parseInt(jsonObject.get("level").toString());
		this.team = (String) jsonObject.get("team");
//		System.out.println(correctAnswer+" "+level+" "+team);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject question = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		question.put("question", this.question);
		for (int i = 0; i < answers.size(); i++) {
			jsonArray.add(answers.get(i));
		}
		question.put("answers", jsonArray);
		question.put("correctAnswer", this.correctAnswer);
		question.put("level", this.level);
		question.put("team", this.team);
		return question;

	}

	public int getQuestionID() {
		return questionID;
	}

	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	@Override
	public int hashCode() {
		return Objects.hash(answers, correctAnswer, level, question, questionID, team);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		return Objects.equals(answers, other.answers) && Objects.equals(correctAnswer, other.correctAnswer)
				&& Objects.equals(level, other.level) && Objects.equals(question, other.question)
				&& questionID == other.questionID && Objects.equals(team, other.team);
	}

	@Override
	public String toString() {
		return "Question [questionID=" + questionID + ", question=" + question + ", answers=" + answers
				+ ", correctAnswer=" + correctAnswer + ", level=" + level + ", team=" + team + "]";
	}
	
	private List<String> getAttributesAsString() {
		List<String> allAttributes = new ArrayList<String>();
		allAttributes.add(question);
		for(String ans : this.answers) {
			allAttributes.add(ans);
		}
		allAttributes.add(Utilities.getInstance().getQuestionLevels()[this.level-1]);
		allAttributes.add(team);
		return allAttributes;
	}
	public boolean containsText(String searchTxt) {
		for (String str : getAttributesAsString()) {
			if (Utilities.getInstance().containsIgnoreCase(str, searchTxt))
				return true;
		}
		return false;
	}


}
