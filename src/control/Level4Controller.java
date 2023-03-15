package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.Horse;
import model.Utilities;

public class Level4Controller extends KingLevels {
	private final static int Blocked_Tile_Num = 8;

	// player action
	@FXML
	void tileAction(ActionEvent event) {
		gameOver();
		try {
			tileActiontemp(event);
			setTotalAndLevelScore();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (exit == true) {
			alert.setText("You've been eaten by the king!");
		}
	}

	// initialize game
	@FXML
	void initialize() throws NoSuchMethodException, SecurityException, NoSuchFieldException, ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException {
		DROW = Horse.DROWHORSELV234;
		DCOL = Horse.DCOLHORSELV234;
		unMuteButton.setGraphic(Utilities.getAvatar("/images/unMute.png", 25, 25));
		muteButton.setGraphic(Utilities.getAvatar("/images/mute.png", 25, 25));
		runMusic();
		setTotalAndLevelScore();
		initAll();
		ReturnRandomSeries(Blocked_Tile_Num, "Blocked");
		tempInitialize();
		currLevel = 4;
		levelOver();
		fillQuestionsByDiff();
		totalScore.setText(Integer.toString(getTotalScore()));
	}

}
