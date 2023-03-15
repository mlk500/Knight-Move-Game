package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Horse;
import model.Queen;
import model.Tile;
import model.Utilities;

public abstract class QueenLevels extends AbstractLevelControl {
	public static Queen queen;
	protected final ImageView QUEENIMAGE = Utilities.getAvatar("/images/QueenIcon.png", 35, 45);

	@FXML
	protected Text numberHelp;

	@FXML
	private Text timer;

	@FXML
	abstract void helpbtn(ActionEvent event);

	char befQueenPosValue;

	public void initAll()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		initParent();
		timerImpl();
		befQueenPosValue = '1';
		queen = new Queen(new Tile(0, 7, null));
		horse = new Horse(InsertNicknameController.name, new Tile(0, 0, null, true));
		Button queenPosiButton = getButton("i0j7");
		queenPosiButton.setGraphic(QUEENIMAGE);
	}

	public void setQueenPositions() throws NoSuchMethodException, SecurityException, NoSuchFieldException,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		if (!gameOverQueen()) {
			setBoard();
			Tile nextTile = Utilities.pathExists(board, queen.getQueenPos("row"), queen.getQueenPos("col"), "Q");
			int i = nextTile.getPos().getX();
			int j = nextTile.getPos().getY();
			int x = (queen.getCurrTile().getPos().getX());
			int y = queen.getCurrTile().getPos().getY();
			tempBoard[x][y] = befQueenPosValue;
			possibleTiles.add(fromIndicesToNum(x, y));
			queen.getCurrTile().getPos().setX(i);
			queen.getCurrTile().getPos().setY(j);
			befQueenPosValue = tempBoard[i][j];
			tempBoard[i][j] = 'E';
			possibleTiles.remove(fromIndicesToNum(i, j));
			Button queengPosiButton = fromIndicesToButton(i, j);
			queengPosiButton.setGraphic(QUEENIMAGE);
		} else {
			toScene.setDisable(false);
			exitBtn.setDisable(false);
		}
	}

	public boolean gameOverQueen() {
		if (queen.getCurrTile().getPos().getX() == horse.getCurrTile().getPos().getX()
				&& queen.getCurrTile().getPos().getY() == horse.getCurrTile().getPos().getY()) {
			exit = true;
			eaten = true;
			levelOver.set(PAUSE_LEVEL);
			time.cancel();
			time.pause();
			time.cancel();
			return true;
		}
		return false;
	}
	
	@Override
	public void pauseAll() {
		time.pause();
		d2.pause();
		
	}
	
	@Override
	public void resumeAll() {
		time.resume();
		d2.play();
	}
	
	@Override
	public void timeCancel() {
		time.cancel();
	}

}
