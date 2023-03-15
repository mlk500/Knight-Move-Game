package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SysData {

	static final String QUESTIONS_FILENAME = "Questions.json";
	static final String QUESTIONS_JSONOBJECT = "questions";
	static final int MAX_CAPACITY_QUESTIONS = 100;
	static final String SCORES_FILENAME = "Scores.json";
	static final String SCORES_JSONOBJECT = "scores";
	static final String USER_PREFS_FILENAME = "UserPrefs.json";
	static final String QUESTIONS_SUGGESTIONS_JSONOBJECT = "qSuggestions";
	static final String QUESTIONS_HISTORY_JSONOBJECT = "qHistory";
	static final int MAX_CAPACITY_USERPREFS = 10;
	static final int MAX_CAPACITY_SCORES = 20;

	private static SysData instance;
	private List<Question> questionsList;
	private static int questionID = 0;
	private JSONArray questionsListJson;
	private List<Score> scoresList;
	private JSONArray scoresListJson;
	private List<String> suggestionsList;
	private List<String> historyList;
	private JSONArray historyJson;
	private JSONArray suggestionsJson;

	public static SysData getInstance() {
		if (instance == null)
			instance = new SysData();
		return instance;
	}

	/**
	 * function to write data to json file
	 * 
	 * @param objectList the objects in json array
	 * @param objectName before array of questions in json
	 * @param fileName   name of json file
	 */
	@SuppressWarnings("unchecked")
	public void writeJsonFile(JSONArray objectList, String objectName, String fileName) {
		try (FileWriter file = new FileWriter(fileName)) {
			JSONObject obj = new JSONObject();
			obj.put(objectName, objectList);
			file.write(obj.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * function to read a json file (name of file is passed as an argument)
	 * 
	 * @param objectName
	 * @param fileName
	 */
	private JSONArray readJsonFile(String objectName, String fileName) {
		try (FileReader reader = new FileReader(fileName)) {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			return (JSONArray) jsonObject.get(objectName);
		} catch (FileNotFoundException e) {
			if (objectName.equals(QUESTIONS_JSONOBJECT)) {
				writeJsonFile(questionsListJson, objectName, fileName);
			} else if (objectName.equals(SCORES_JSONOBJECT)) {
				writeJsonFile(scoresListJson, objectName, fileName);
			} else {
				writeUserPrefJson();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONArray();
	}

	// --------------------------------------------------- Question Functions
	// ---------------------------------------------------

	public List<Question> getQuestionsList() {
		return questionsList;
	}

	public void setQuestionsList(List<Question> questionsList) {
		this.questionsList = questionsList;
	}

	/**
	 * A function to read the questions, it calls readJsonfile function, and passes
	 * the suitable parameters to it, the function adds questions to jsonArray
	 * called questionsListJson, and this function converts the objects from JSON to
	 * Question sets an id to each question, and adds them to a list called
	 * questionsList
	 * 
	 */
	public void readQuestions() {
		questionsList = new ArrayList<Question>();
		questionsListJson = readJsonFile(QUESTIONS_JSONOBJECT, QUESTIONS_FILENAME);
		if (questionsListJson == null) {
			this.questionsListJson = new JSONArray();
			return;
		}
		Question q;
		for (int i = 0; i < this.questionsListJson.size(); i++) {
			JSONObject exploreObject = (JSONObject) this.questionsListJson.get(i);
			q = new Question(exploreObject);
			q.setQuestionID(SysData.questionID++);
			questionsList.add(q);
		}
//		System.out.println("Question List ");
//		System.out.println(questionsList);
	}

	/**
	 * Gets a question and adds it to questionsList, and then writes it to questions
	 * json file
	 * 
	 * @param question
	 */
	@SuppressWarnings("unchecked")
	public void addQuestion(Question question) {
		if (this.questionsList.size() >= MAX_CAPACITY_QUESTIONS) {
			this.questionsList.remove(0);
			this.questionsListJson.remove(0);
		}
			
		question.setQuestionID(SysData.questionID++);
		this.questionsListJson.add(question.toJSON());
		this.questionsList.add(question);
		writeJsonFile(this.questionsListJson, QUESTIONS_JSONOBJECT, QUESTIONS_FILENAME);
	}

	/**
	 * Gets a question ID and updates the question after getting its index in the
	 * List, then json file is re-written
	 * 
	 * @param qID
	 * @param question
	 */
	public void updateQuestion(int qID, Question question) {
		int index = getQuestionIndexByID(qID);
		if (index != -1) {
			deleteQuestion(qID);
			addQuestion(question);
			writeJsonFile(this.questionsListJson, QUESTIONS_JSONOBJECT, QUESTIONS_FILENAME);
		}

	}

	/**
	 * Gets a questionID, retrieves the question, deletes it and then updates
	 * questionsJson file and questionsJsonList
	 * 
	 * @param questionID
	 * @return
	 */
	public boolean deleteQuestion(int questionID) {
		Question toBeDeleted = this.getQuestionByID(questionID);
		if (toBeDeleted == null)
			return false;
		else {
			this.questionsList.remove(toBeDeleted);
			updateQuestionsJsonList();
			writeJsonFile(this.questionsListJson, QUESTIONS_JSONOBJECT, QUESTIONS_FILENAME);
			return true;
		}
	}

	public Question getQuestionByID(int questionID) {
		for (Question q : this.questionsList) {
			if (q.getQuestionID() == questionID)
				return q;
		}
		return null;
	}

	public int getQuestionIndexByID(int questionID) {
		for (int i = 0; i < this.questionsList.size(); i++) {
			if (questionsList.get(i).getQuestionID() == questionID) {
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	private void updateQuestionsJsonList() {
		this.questionsListJson = new JSONArray();
		for (Question q : this.questionsList) {
			this.questionsListJson.add(q.toJSON());
		}
	}

	public boolean isDuplicateQuestion(String questionBody) {
		return this.questionsList.stream().filter(o -> o.getQuestion().equals(questionBody)).findFirst().isPresent();
	}

	// --------------------------------------------------- Question search Functions
	// ---------------------------------------------------

	public List<String> getHistory() {
		return this.historyList;
	}

	public Set<String> getSuggestions() {
		return new HashSet<>(this.suggestionsList);
	}

	public void addHistory(String str) {
		if (this.historyList.size() >= MAX_CAPACITY_USERPREFS) {
			historyList.remove(0);
			historyJson.remove(0);
		}
		if(historyList.stream().filter(s -> equals(str)).findFirst().isPresent()) {
			historyList.remove(str);
			historyJson.remove(str);
		}
		historyJson.add(str);
		historyList.add(str);
		writeUserPrefJson();
	}

	@SuppressWarnings("unchecked")
	public void addSuggestion(String suggestion) {
		suggestionsJson.add(suggestion);
		suggestionsList.add(suggestion);
		writeUserPrefJson();
	}

	@SuppressWarnings("unchecked")
	private void writeUserPrefJson() {
		try (FileWriter file = new FileWriter(USER_PREFS_FILENAME)) {
			JSONObject obj = new JSONObject();
			obj.put(QUESTIONS_SUGGESTIONS_JSONOBJECT, this.suggestionsList);
			obj.put(QUESTIONS_HISTORY_JSONOBJECT, this.historyList);
			file.write(obj.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readUserPrefs() {
		this.historyList = new ArrayList<String>();
		this.suggestionsList = new ArrayList<String>();
		this.historyJson = readJsonFile(QUESTIONS_HISTORY_JSONOBJECT, USER_PREFS_FILENAME);
		this.suggestionsJson = readJsonFile(QUESTIONS_SUGGESTIONS_JSONOBJECT, USER_PREFS_FILENAME);
		if (historyJson == null) {
			this.historyJson = new JSONArray();
		} else {
			for (int i = 0; i < this.historyJson.size(); i++) {
				String exploreObject = (String) this.historyJson.get(i);
				historyList.add(exploreObject);
			}
		}
		if (suggestionsJson == null) {
			this.suggestionsJson = new JSONArray();
		} else {
			for (int i = 0; i < this.suggestionsJson.size(); i++) {
				String exploreObject = (String) this.suggestionsJson.get(i);
				suggestionsList.add(exploreObject);
			}
		}

	}

	// --------------------------------------------------- Score Functions
	// ---------------------------------------------------

	public List<Score> getScoresList() {
		return scoresList;
	}

	/**
	 * A function to read the scores, it calls readJsonfile function, and passes the
	 * suitable parameters to it, the function adds scores to jsonArray called
	 * scoresListJson, and this function converts the objects from JSON to Score,
	 * and adds them to a list called scoresListJson
	 * 
	 */
	public void readScores() {
		scoresList = new ArrayList<Score>();
		this.scoresListJson = readJsonFile(SCORES_JSONOBJECT, SCORES_FILENAME);
		if (scoresListJson == null) {
			this.scoresListJson = new JSONArray();
			return;
		}
		Score s;
		for (int i = 0; i < scoresListJson.size(); i++) {
			JSONObject exploreObject = (JSONObject) this.scoresListJson.get(i);
			s = new Score(exploreObject);
			scoresList.add(s);
		}
	}

	/**
	 * Gets a score and adds it to scoresList, and then writes it to scores json
	 * file
	 * 
	 * @param score
	 */
	@SuppressWarnings("unchecked")
	public void addScore(Score score) {
		if (this.scoresList.size() >= MAX_CAPACITY_SCORES)
			this.scoresList.remove(0);
		this.scoresListJson.add(score.toJSON());
		this.scoresList.add(score);
		writeJsonFile(this.scoresListJson, SCORES_JSONOBJECT, SCORES_FILENAME);
	}

}
