package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.json.simple.JSONObject;

public class Score implements JsonObject {
	private static int idCounter = 1;
	private int id;
	private String nickname;
	private int score;
	private int level;
	private String dateTime;

	public Score(String nickname, int score, int level, LocalDateTime dateTime) {
		super();
		this.id = idCounter++;
		this.nickname = nickname;
		this.score = score;
		this.level = level;
		this.dateTime = convertLocalDatetimeToString(dateTime);
	}

	public Score(JSONObject jsonObject) {
		try {
			fromJSON(jsonObject);
		} catch (Exception e) {

		}
	}

	// Getter and Setter
	public int getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	

	@Override
	public void fromJSON(JSONObject jsonObject) {
		this.id = Integer.parseInt(jsonObject.get("id").toString());
		this.nickname = (String) jsonObject.get("nickname");
		this.score = Integer.parseInt(jsonObject.get("score").toString());
		this.level = Integer.parseInt(jsonObject.get("level").toString());
		this.dateTime = (String) jsonObject.get("dateime");
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject score = new JSONObject();
		score.put("id", this.id);
		score.put("nickname", this.nickname);
		score.put("score", this.score);
		score.put("level", this.level);
		score.put("dateime", this.dateTime);
		return score;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateTime, id, level, nickname, score);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Score other = (Score) obj;
		return Objects.equals(dateTime, other.dateTime) && id == other.id && level == other.level
				&& Objects.equals(nickname, other.nickname) && score == other.score;
	}
	
	

	@Override
	public String toString() {
		return "Score [id=" + id + ", nickname=" + nickname + ", score=" + score + ", level=" + level + ", dateTime="
				+ dateTime + "]";
	}

	private String convertLocalDatetimeToString(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String formattedDateTime = dateTime.format(formatter);
		return formattedDateTime;
	}

//	private LocalDateTime convertStringToLocalDatetime(String timeStr) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		LocalDateTime dateTime = LocalDateTime.parse(timeStr, formatter);
//		return dateTime;
//	}

}
