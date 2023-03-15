package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.Horse;
import model.Utilities;

public class Level2Controller extends QueenLevels {

	private final static int FORGTABLE_TILE_NUM = 3;

	// -------------------------------------------------------------------------------------------------------------------------
	// initialize
	@FXML
	public void initialize()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		initAll();
		DROW = Horse.DROWHORSELV234;
		DCOL = Horse.DCOLHORSELV234;
		unMuteButton.setGraphic(Utilities.getAvatar("/images/unMute.png", 25, 25));
		muteButton.setGraphic(Utilities.getAvatar("/images/mute.png", 25, 25));
		runMusic();
		setTotalAndLevelScore();
		ReturnRandomSeries(FORGTABLE_TILE_NUM, "Forgetable");
		currLevel = 2;
		levelOver();
		fillQuestionsByDiff();
	}

	// -------------------------------------------------------------------------------------------------------------------------
	@FXML
	void helpbtn(ActionEvent event) {

	}

	@Override
	void tileAction(ActionEvent event) {
		gameOverQueen();
		try {
			validPos = false;
			tileActiontemp(event);
			if (!eaten && !exit && validPos) {
				setQueenPositions();
				gameOverQueen();
				setTotalAndLevelScore();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
