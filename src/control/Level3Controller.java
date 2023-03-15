package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.Horse;
import model.Utilities;

public class Level3Controller extends KingLevels {
	private final static int RANDOM_TILE_NUM = 2;
	private final static int FORGTABLE_TILE_NUM = 2;

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

		initAll();
		horse.getCurrTile().getPos().getX();
		horse.getCurrTile().getPos().getY();
		ReturnRandomSeries(RANDOM_TILE_NUM, "Random");
		ReturnRandomSeries(FORGTABLE_TILE_NUM, "Forgetable");
		levelOver();
		tempInitialize();
		currLevel = 3;
		fillQuestionsByDiff();
		totalScore.setText(Integer.toString(getTotalScore()));
	}

}
