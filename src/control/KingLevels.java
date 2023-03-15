package control;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import model.King;
import model.Tile;
import model.Timer;
import model.Utilities;

public abstract class KingLevels extends AbstractLevelControl {

	// Definitions known in advance and not changing:
	private final int BLOCKED_TILE_NUM = 8;
	private static long UPDATE_SPEED_FREQUENCY = 3001;
	private final float SPEED_PERC = (float) 0.9;
	private final long SPEED_CHANGE_DURATION = 10001;
	protected final ImageView KINGIMAGE = Utilities.getAvatar("/images/KingIcon.png", 35, 45);
	public static King king;
	private Timer time1;

	char befKingPosValue;

	public void print2D(char mat[][]) {
		// Loop through all rows
		for (int i = 0; i < mat.length; i++)

			// Loop through all elements of current row
			for (int j = 0; j < mat[i].length; j++)
				System.out.print(mat[i][j] + " ");
		System.out.println();
		System.out.println("-------------------------");
	}

	// Initialize game variables
	public void initAll()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		initParent();
		king = new King(new Tile(0, 7, null));
		befKingPosValue = '1';
	}

	// Handles the timing of the king's actions
	public void newThread(Button button, ImageView avatar) {
		final AtomicLong counter = new AtomicLong(-1);
		final Thread countThread = new Thread(new Runnable() {
			@Override
			public void run() {
				long count = 0;
				while (true) {
					count++;
					if (counter.getAndSet(count) == -1) {
						updateUI(counter, button, avatar);
					}
				}
			}
		});
		countThread.setDaemon(true);
		countThread.start();
	}

	// Put the king's picture in its place on the game board
	private void updateUI(final AtomicLong counter, final Button button, ImageView avatar) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				button.setGraphic(avatar);
			}
		});
	}

	@FXML
	abstract void initialize() throws NoSuchMethodException, SecurityException, NoSuchFieldException,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException;

	public void tempInitialize() throws NoSuchMethodException, SecurityException, NoSuchFieldException,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
// Add the array to list
		setKingPositions(0, 7);
		Button KingPosiButton = getButton("i0j7");
		newThread(KingPosiButton, KINGIMAGE);
		timerImpl();
		observeTimer();
		returnToAction();
		returnToAction();
	}

	public void observeTimer() {
		second.addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() != 0 && oldValue.intValue() % 10 == 0) {
					updateSpeed(UPDATE_SPEED_FREQUENCY);
					UPDATE_SPEED_FREQUENCY = (long) (UPDATE_SPEED_FREQUENCY * SPEED_PERC);
					if (checkIfAllVisited()) {
						pauseChar.set(PAUSE_LEVEL);
						toScene.setDisable(false);
					}
				}
			}

		});
	}

	private boolean checkIfAllVisited() {
		if ((horse.getVisitedPositions().size() == COL_NUM * ROW_NUM - BLOCKED_TILE_NUM && currLevel == 4)
				|| horse.getVisitedPositions().size() == COL_NUM * ROW_NUM)
			return true;

		return false;
	}

	private void updateSpeed(Long duration) {

		time1 = new Timer(duration, SPEED_CHANGE_DURATION) {
			@Override
			protected void onTick() throws NoSuchMethodException, SecurityException, NoSuchFieldException,
					ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
				if (run && !eaten && !exit) {
					Tile nextTile;
					setBoard();
					nextTile = Utilities.pathExists(board, King.DROWKING, King.DCOLKING, "K");
					try {
						if (run && !exit) {
							int prevI = king.getCurrTile().getPos().getX();
							int prevJ = king.getCurrTile().getPos().getY();
							Button prevTile = fromIndicesToButton(prevI, prevJ);
							setKingPositions(nextTile.getPos().getX(), nextTile.getPos().getY());
							if (tempBoard[prevI][prevJ] == '5') {
								newThread(prevTile, ESAYQIMAGE);
							} else if (tempBoard[prevI][prevJ] == '6') {
								newThread(prevTile, MEDQIMAGE);
							} else if (tempBoard[prevI][prevJ] == '7') {
								newThread(prevTile, HardQIMAGE);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				run = true;
			}

			@Override
			protected void onFinish() {
				// TODO Auto-generated method stub
			}
		};
		time1.start();
	}

	public void setKingPositions(int i, int j) throws NoSuchMethodException, SecurityException, NoSuchFieldException,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		int x = (king.getCurrTile().getPos().getX());
		int y = king.getCurrTile().getPos().getY();
		tempBoard[x][y] = befKingPosValue;
		possibleTiles.add(fromIndicesToNum(x, y));
		king.getCurrTile().getPos().setX(i);
		king.getCurrTile().getPos().setY(j);
		befKingPosValue = tempBoard[i][j];
		tempBoard[i][j] = 'E';
		possibleTiles.remove(fromIndicesToNum(i, j));
		Button KingPosiButton = fromIndicesToButton(i, j);
		newThread(KingPosiButton, KINGIMAGE);
		if (gameOver()) {
			toScene.setDisable(false);
			exitBtn.setDisable(false);
		}
	}

	public boolean gameOver() {
		if (king.getCurrTile().getPos().getX() == horse.getCurrTile().getPos().getX()
				&& king.getCurrTile().getPos().getY() == horse.getCurrTile().getPos().getY()) {
			exit = true;
			eaten = true;
			levelOver.set(PAUSE_LEVEL);
			time1.pause();
			time.cancel();
			time.pause();
			time.cancel();
			return true;
		}
		return false;
	}

	// method for stopping and resuming timer
	public void returnToAction() {
		AbstractLevelControl.pauseChar.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() == PAUSE_LEVEL) {
					time1.pause();
					time.pause();
				} else {
					time1.resume();
					time.resume();
					;

				}
			}
		});
	}

	@Override
	public void pauseAll() {
		time1.pause();
		time.pause();
		d2.pause();

	}

	@Override
	public void resumeAll() {
		time1.resume();
		time.resume();
		d2.play();
	}

	@Override
	public void timeCancel() {
		time1.cancel();
		time.cancel();
	}
}
