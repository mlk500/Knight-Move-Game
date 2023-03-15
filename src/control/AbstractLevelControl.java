package control;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import model.Horse;
import model.Position;
import model.Question;
import model.Score;
import model.SysData;
import model.Tile;
import model.Timer;
import model.Utilities;

public abstract class AbstractLevelControl {

	// initialize two variables from singleton classes whose methods we would like
	// to
	SysData sysData;
	Utilities utilities;
	public static ArrayList<Integer> DROW = new ArrayList<>();
	public static ArrayList<Integer> DCOL = new ArrayList<>();
	// Definitions known in advance and not changing:
	protected final int ROW_NUM = 8;
	protected final int COL_NUM = 8;
	public final int PAUSE_LEVEL = 1;
	public final static int RESUME_LEVEL = 0;
	private static final int LEVEL_TIME = 61;
	public static final int NEXT_LEVEL = 2;
	public static final int WINNER = 200;
	public final String BLACKCOLOR = "-fx-background-color: black";
	public final String VISITCOLOR = "-fx-background-color: red";
	public final String WHITECOLOR = "-fx-background-color: white";
	public final ImageView HORSEIMAGE = Utilities.getAvatar("/images/horseIcon.png", 35, 45);
	protected final ImageView ESAYQIMAGE = Utilities.getAvatar("/images/WhiteEasyQ.png", 45, 45);
	protected final ImageView HardQIMAGE = Utilities.getAvatar("/images/RedHardQ.png", 45, 45);
	protected final ImageView MEDQIMAGE = Utilities.getAvatar("/images/YellowMedQ.png", 45, 45);
	// initialize the possible places for the horse at every step
	public static List<Position> positionList;
	// initialize all the horse steps to help us run a forgetful slot
	private Stack<Tile> horseActions;

	// initialize the horse's place at a gallop at every given step
	private int horseI;
	private int horseJ;
	// initialize the places of the possible tile in which to scatter the different
	// types of tile
	static int[] initPossibleTiles;
	public List<Integer> possibleTiles;

	// Initialize a matrix game board to help us run our algorithm
	public char[][] tempBoard;

	// initialize Player(Horse)
	public Horse horse;
	@FXML
	private Text numberHelp;
	boolean run;

	char[][] board;
	private boolean help;
	private static List<Position> currPoss;

	// If the horse is eaten the variable becomes true
	public static boolean eaten;
	// If the time is up the variable becomes true
	public static boolean exit;

	// initialize the questions from 3 different levels
	HashMap<Integer, Integer> questionLevels;
	public static HashSet<Integer> questionTiles;
	List<Question> hard;
	List<Question> med;
	List<Question> easy;
	List<Integer> diffQuestion;

	// timer
	public static IntegerProperty second;
	public static Timer time;

	// Initialize the previous score value to help us calculate the total score
	// value
	public static int prevScore;

	// Initialize the current level value
	public int currLevel;

	// Variable helps us to continue and stop the game time
	public static IntegerProperty pauseChar;

	// Score
	private static int totalScoreCode;
	protected static int levelScoreCode;

	// continue button
	@FXML
	public Button toScene;

	// exit button
	@FXML
	public Button exitBtn;

	@FXML
	private MediaView music = new MediaView();

	// tiles
	@FXML
	public Button i0j0, i0j1, i0j2, i0j3, i0j4, i0j5, i0j6, i0j7, i1j0, i1j1, i1j2, i1j3, i1j4, i1j5, i1j6, i1j7, i2j0,
			i2j1, i2j2, i2j3, i2j4, i2j5, i2j6, i2j7, i3j0, i3j1, i3j2, i3j3, i3j4, i3j5, i3j6, i3j7, i4j0, i4j1, i4j2,
			i4j3, i4j4, i4j5, i4j6, i4j7, i5j0, i5j1, i5j2, i5j3, i5j4, i5j5, i5j6, i5j7, i6j0, i6j1, i6j2, i6j3, i6j4,
			i6j5, i6j6, i6j7, i7j0, i7j1, i7j2, i7j3, i7j4, i7j5, i7j6, i7j7;

	// Receiving updates
	@FXML
	public Label alert;

	@FXML
	protected Text levelScore;

	@FXML
	private Text timer;

	@FXML
	protected Label displayNickName;
	static int helpCount = 3;
	@FXML
	protected Text totalScore;
	public static IntegerProperty levelOver;
	public static boolean timeup;
	public static boolean validPos;

	@FXML
	void nextScene(ActionEvent event) throws IOException {
	}

	// abstract method thats realized in the sons and grandsons department
	@FXML
	abstract void tileAction(ActionEvent event);

	// player's turn
	public void tileActiontemp(ActionEvent event) throws IOException, NoSuchMethodException, SecurityException,
			NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		int tileAlert = 0;
		int prevQ = levelScoreCode;
		if (!exit && !eaten) {// Checks if the time isn't up or the horse isn't eaten by the king
			if (help) {
				getAndSetHelp(false);
				help = false;
			}
			positionList.clear();
			Utilities.addNeighbours(horse.getCurrTile(), tempBoard, ROW_NUM, COL_NUM, DROW, DCOL, "H");
			alert.setText("");
			int prevTileI = horse.getCurrTile().getPos().getX();
			int prevTileJ = horse.getCurrTile().getPos().getY();
			Button btnPrev = fromIndicesToButton(prevTileI, prevTileJ);
			possibleTiles.add(fromIndicesToNum(prevTileI, prevTileJ)); // Moves the previous horse back to a place that
																		// can accommodate special types of tiles

			horseI = getTileIndices(event.getSource().toString().split("=|\\,")[1])[0];
			horseJ = getTileIndices(event.getSource().toString().split("=|\\,")[1])[1];

			Position pos = new Position(horseI, horseJ);
			if (positionList.contains(pos)) {

				// Checks Tile type
				boolean isQuesTile = false;
				boolean isRandomTile = false;
				boolean isForgetableTile = false;
				if (tempBoard[horseI][horseJ] >= '5' && tempBoard[horseI][horseJ] <= '7') {
					isQuesTile = true;
					alert.setText("Stuck in Question Tile");
//					tileAlert(1);
				} else if (tempBoard[horseI][horseJ] == '3') {
					isRandomTile = true;
					alert.setText("Stuck in Random Tile");
					tileAlert = 2;
				} else if (tempBoard[horseI][horseJ] == '4') {
					tileAlert = 3;
					isForgetableTile = true;
					alert.setText("Stuck in Forgetable Tile");
				}
				tileAlert(tileAlert);
				if (isRandomTile) {
					randomTileAction();
				}
				if (isForgetableTile) {
					forgtableAction();
				}
				setHorsePositions(horseI, horseJ);
				Tile t = horse.getCurrTile();
				Button btn = fromIndicesToButton(horseI, horseJ);

				pos = new Position(horseI, horseJ);
				if (isQuesTile) {// Checks if the tile he stood on is a question tile
					QustionTileAction(t, event, horseI, horseJ);
				} else {
					if (!(horse.getVisitedPositions().contains(pos))) {
						horseActions.push(t);
						t.setScore(1);
						btn.setStyle(VISITCOLOR);
						horse.getVisitedPositions().add(horse.getCurrTile().getPos());
						levelScoreCode += t.getScore();
					} else {// regular tile
						t.setVisited(false);
						t.setScore(-1);
						horseActions.push(t);
						horse.getVisitedPositions().remove(pos);
						levelScoreCode += t.getScore();
						returnToUnvisitedMode(horseI, horseJ, btn);
					}
				}
				if (isQuesTile) {
					Tile temp = horseActions.pop();
					temp.setScore(levelScoreCode - prevQ);
					horseActions.push(temp);
				}
				btnPrev.setGraphic(null);
				btn.setGraphic(HORSEIMAGE);
				validPos = true;

			}
			// update score
			setLevelScore(levelScoreCode);
			setTotalAndLevelScore();
			totalScore.setText(getTotalScore() + "");
			levelScore.setText(getLevelScore() + "");
			positionList.clear();
		} else {// Can't play
			alert.setText("The game is over, to move forward press the continue button");
		}
	}

	// Deleting the last 3 steps according to the requirements of this tile type
	void forgtableAction()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		System.out.println("in forgettable before " + levelScoreCode);
		int i = 0;
		while (!horseActions.isEmpty() && i < 3) {
			Tile tileAct = horseActions.pop();
			Button btnAct = fromIndicesToButton(tileAct.getPos().getX(), tileAct.getPos().getY());
			if (!tileAct.getVisited()) {
				btnAct.setStyle(VISITCOLOR);
				horse.getVisitedPositions().add(tileAct.getPos());
				levelScoreCode += tileAct.getScore();
			} else {
				returnToUnvisitedMode(tileAct.getPos().getX(), tileAct.getPos().getY(), btnAct);
				horse.getVisitedPositions().remove(tileAct.getPos());
				levelScoreCode += tileAct.getScore();
			}
			i++;

		}
		System.out.println("in forgettable after " + levelScoreCode);
		ReturnRandomSeries(1, "Forgetable");

	}

	// this method returns the tile to its initial position
	private void returnToUnvisitedMode(int horseI, int horseJ, Button btn) {

		if (((horseI % 2 == 0) && (horseJ % 2 == 0)) || ((horseI % 2 == 1) && (horseJ % 2 == 1))) {
			btn.setStyle(WHITECOLOR);

		} else {
			btn.setStyle(BLACKCOLOR);
		}

	}

	// Question tile - represent the question and delete it the questions that can
	// be displayed so as not to repeat it again and changes the place of the
	// Qustion tile
	private void QustionTileAction(Tile t, ActionEvent event, int horseI, int horseJ) throws IOException,
			NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		int numFromIndex = fromIndicesToNum(horseI, horseJ);
		questionTiles.remove(numFromIndex);
		int i = diffQuestion.indexOf(numFromIndex);
		diffQuestion.add(i, -1);
		showQuestion(event, horseI, horseJ, i);

		horseActions.push(t);
		questionTiles.addAll(ReturnRandomSeries(1, "Question"));
	}

	// Random Tile - chooses a new place for the horse, moves it and changes the
	// place of the random tile
	private void randomTileAction() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
		tempBoard[horseI][horseJ] = '1';
		int newPos = ReturnRandomSeries(1, "Horse").get(0);
		Position p = fromNumToIndices(newPos);
		horseI = p.getX();
		horseJ = p.getY();
		ReturnRandomSeries(1, "Random");
	}

//A method responsible for displaying the game time
	public void timerImpl() {
		second = new SimpleIntegerProperty(LEVEL_TIME);
		time = new Timer(1001, 60001) {
			@Override
			protected void onTick() {

				int val = second.getValue();
				val--;
				second.setValue(val);
				AbstractLevelControl.this.timer.setText(Integer.toString(second.getValue()));
				setLevelScore(levelScoreCode);
				setTotalAndLevelScore();
				totalScore.setText(getTotalScore() + "");
				levelScore.setText(getLevelScore() + "");
			}

			@Override
			protected void onFinish() {
				time.cancel();
				AbstractLevelControl.timeup = true;
				levelOver.set(PAUSE_LEVEL);
				toScene.setDisable(false);
				exitBtn.setDisable(false);

			}
		};
		time.start();
	}

//Initialize game variables	
	public void initParent()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		helpCount = 3;
		numberHelp.setText(helpCount + "");
		currPoss = new ArrayList<Position>();
		horseActions = new Stack<Tile>();
		char[][] toinInitialCase = { { 'H', '1', '1', '1', '1', '1', '1', 'E' },
				{ '1', '1', '1', '1', '1', '1', '1', '1' }, { '1', '1', '1', '1', '1', '1', '1', '1' },
				{ '1', '1', '1', '1', '1', '1', '1', '1' }, { '1', '1', '1', '1', '1', '1', '1', '1' },
				{ '1', '1', '1', '1', '1', '1', '1', '1' }, { '1', '1', '1', '1', '1', '1', '1', '1' },
				{ '1', '1', '1', '1', '1', '1', '1', '1' } };
		initPossibleTiles = IntStream.rangeClosed(2, 64).toArray();
		possibleTiles = Arrays.stream(initPossibleTiles).boxed().collect(Collectors.toList());
		horseI = 0;
		horseJ = 0;
		tempBoard = toinInitialCase;
		positionList = new LinkedList<Position>();
		run = false;
		horse = new Horse(InsertNicknameController.name, new Tile(0, 0, null, true));
		board = new char[ROW_NUM][COL_NUM];
		help = false;
		validPos = false;
		setLevelScore(0);
		levelScore.setText("0");
		pauseChar = new SimpleIntegerProperty(RESUME_LEVEL);
		questionLevels = new HashMap<Integer, Integer>();
		sysData = SysData.getInstance();
		utilities = Utilities.getInstance();
		alert.setText("");
		levelScore.setText("0");
		questionTiles = new HashSet<Integer>();
		exit = false;
		prevScore = levelScoreCode;
		diffQuestion = new ArrayList<Integer>();
//		levelScoreCode = 0;
		eaten = false;
		timeup = false;
		displayNickName.setText(horse.getNickname());
		Button horsePosiButton = getButton("i0j0");
		horsePosiButton.setGraphic(HORSEIMAGE);
		possibleTiles.remove(fromIndicesToNum(0, 7));
		questionTiles.addAll(ReturnRandomSeries(3, "Question"));
		levelOver = new SimpleIntegerProperty(RESUME_LEVEL);
	}

	// Handles an exit button
	@FXML
	void exitBtn(ActionEvent event) throws IOException {
		d2.pause();
		pauseChar.set(PAUSE_LEVEL);
		// Asks the Playe if he wants to go out
		Alert conf = Alerts.createConfirmation(AlertType.CONFIRMATION, "Exit Game",
				"Are you sure you want to exit the level?\nIf you press yes, your progress will not be saved", null);
		Optional<ButtonType> result = conf.showAndWait();
		if (result.isPresent() && result.get() == conf.getButtonTypes().get(0)) { // If so, then return it to the menu
																					// and do not save it in the
																					// list of players
			time.cancel();
			utilities.switchScene(event, "/view/Menu.fxml");
		} else// Otherwise it continues
			pauseChar.set(RESUME_LEVEL);
	}

	// pause game
	@FXML
	void pauseBtn(ActionEvent event) {
		pauseChar.set(PAUSE_LEVEL);
	}

	// resume game
	@FXML
	void resumeBtn(ActionEvent event) {
		pauseChar.set(RESUME_LEVEL);
	}

	// Presenting the question to the user and handling:
	// 1. Retrieving the question
	// 2. Checking the correctness of the question and scoring
	public void showQuestion(ActionEvent event, int horseI, int horseJ, int level) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		d2.pause();
		PopQuestionController popQuestionController;
		pauseChar.set(PAUSE_LEVEL);

		FXMLLoader loader = utilities.getSceneController("/view/PopQuestion.fxml");
		popQuestionController = loader.getController();
		if (popQuestionController == null) {
			popQuestionController = new PopQuestionController();
			loader.setController(popQuestionController);
		}
		Button btn = this.fromIndicesToButton(horseI, horseJ);
		Question q = getQuestion(level);
		if (q == null) {
			pauseChar.set(RESUME_LEVEL);
			return;
		}
		popQuestionController.setQuestion(q);

		// saving this scene in popQuestion so we can resume from where we stopped
		popQuestionController.setPreScene(btn.getScene());
		d2.pause();
		utilities.loadScene(event, loader);
	}

	// convert tile number (1-64) to indices i j
	public int[] getTileIndices(String tileID) {
		char[] idToChar = tileID.toCharArray();
		int[] ijID = new int[] { Character.getNumericValue(idToChar[1]), Character.getNumericValue(idToChar[3]) };
		return ijID;
	}

	// convert a string (Button name) to button
	public Button getButton(String btn)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Field field = this.getClass().getField(btn);
		final Button button = (Button) field.get(this);
		return button;
	}

	// convert indices i j in matrix 8x8 to number (0-64)
	public static Integer fromIndicesToNum(int i, int j) {
		return ((i * 8) + (j % 8)) + 1;
	}

	// convert number (0-64) to indices i j in matrix 8x8
	public static Position fromNumToIndices(int num) {
		num = (num - 1);
		return new Position(num / 8, (num % 8));
	}

	// Gets indexes and returns the button representing them
	public Button fromIndicesToButton(int i, int j)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String btn = "i" + i + "j" + j;
		Button posiButton = getButton(btn);
		return posiButton;
	}

	// Makes sure to pull out a question from the relevant level each time
	private Question getQuestion(int diff) {

		if (sysData.getQuestionsList() == null || sysData.getQuestionsList().isEmpty()) {
			alert.setText("Questions Repositort is Empty , There are no questions found to present");
		}

		else {
			if (diff == 2) {
				if (hard == null || hard.isEmpty()) {
					fillQuestionsByDiff();
				}
				Question q = hard.get(0);
				hard.remove(q);
				return q;
			} else if (diff == 1) {
				if (med == null || med.isEmpty()) {
					fillQuestionsByDiff();
				}
				Question q = med.get(0);
				med.remove(q);
				return q;
			}

			else {
				if (easy == null || easy.isEmpty()) {
					fillQuestionsByDiff();
				}

				Question q = easy.get(0);
				easy.remove(q);
				return q;
			}
		}
		return null;
	}

	// Pulls out a question from the relevant level
	public void fillQuestionsByDiff() {
		if (this.hard == null || this.hard.isEmpty()) {
			hard = new ArrayList<>();
			for (Question q : sysData.getQuestionsList()) {
				if (q.getLevel() == (3)) {
					hard.add(q);
				}
			}
			Collections.shuffle(this.hard);
		}

		if (this.med == null || this.med.isEmpty()) {
			med = new ArrayList<>();
			for (Question q : sysData.getQuestionsList()) {
				if (q.getLevel() == (2)) {
					med.add(q);
				}
			}
			Collections.shuffle(this.med);
		}

		if (this.easy == null || this.easy.isEmpty()) {
			easy = new ArrayList<>();
			for (Question q : sysData.getQuestionsList()) {
				if (q.getLevel() == (1)) {
					easy.add(q);
				}
			}
			Collections.shuffle(this.easy);
		}

	}

	// get set initialize level score
	public static int getLevelScore() {
		return levelScoreCode;
	}

	public static void initLevelScore() {
		AbstractLevelControl.levelScoreCode = 0;
	}

	public static void setLevelScore(int levelScore) {
		if (levelScore < 0)
			AbstractLevelControl.levelScoreCode = 0;
		else
			AbstractLevelControl.levelScoreCode = levelScore;
	}

	// get set total score
	public static int getTotalScore() {
		return totalScoreCode;
	}

	public static void setTotalScore(int totalScore) {
		if (totalScore < 0)
			AbstractLevelControl.totalScoreCode = 0;
		else
			AbstractLevelControl.totalScoreCode = totalScore;
	}

	public static void setTotalAndLevelScore() {
		if (getLevelScore() == 0) {
			prevScore = 0;
		}
		int diffrence = getLevelScore() - prevScore;
		int totalTemp = getTotalScore();
		totalTemp = totalTemp + diffrence;
		setTotalScore(totalTemp);
		prevScore = getLevelScore();
	}

	Media m = new Media(getClass().getResource("/music/musicBackGround.mp3").toExternalForm());
	MediaPlayer d2 = new MediaPlayer(m);

	public void runMusic() {
		music.setMediaPlayer(d2);
		d2.play();
		d2.setCycleCount(MediaPlayer.INDEFINITE);
	}

	@FXML
	protected Button unMuteButton;

	@FXML
	protected Button muteButton;

	@FXML
	void mute(ActionEvent event) {
		this.d2.pause();
	}

	@FXML
	void unMute(ActionEvent event) {
		this.d2.play();
	}

	@FXML
	void helpbtn(ActionEvent event)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (helpCount > 0) {
			help = true;
			helpCount--;
			getAndSetHelp(help);
			numberHelp.setText(helpCount + "");
		}

	}

	private void getAndSetHelp(boolean giveSign)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (giveSign) {
			Utilities.addNeighbours(horse.getCurrTile(), tempBoard, ROW_NUM, COL_NUM, DROW, DCOL, "H");
			currPoss = positionList;
		}
		System.out.println(currPoss.toString());
		for (int i = 0; i < currPoss.size(); i++) {
			int x = currPoss.get(i).getX();
			int y = currPoss.get(i).getY();
			Button btn = fromIndicesToButton(x, y);
			if (giveSign) {
				btn.setText("X");

			} else if (!giveSign) {
				btn.setText(" ");
			}

		}
		if (!giveSign) {
			currPoss.clear();
		}
	}

	// Updates the horse's location in all relevant places
	public void setHorsePositions(int i, int j) throws NoSuchMethodException, SecurityException, NoSuchFieldException,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		tempBoard[(horse.getCurrTile().getPos().getX())][horse.getCurrTile().getPos().getY()] = '1';
		Tile tempTile = new Tile(i, j, horse.getCurrTile(), true);
		horse.setCurrTile(tempTile);
		tempBoard[i][j] = 'H';
		possibleTiles.remove(fromIndicesToNum(i, j));

	}

	// Choose the new place of in the requested tile, according to the array of
	// possible tile that is updated if you give a new place to the tile
	public List<Integer> ReturnRandomSeries(int stream, String tileType)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		ArrayList<Integer> randomArray = new ArrayList<Integer>();
		int rnd;
		for (int i = 0; i < stream; i++) {
			rnd = new Random().nextInt(possibleTiles.size());
			int numInMat = possibleTiles.get(rnd);
			randomArray.add(numInMat);
			int row = fromNumToIndices(numInMat).getX();
			int col = fromNumToIndices(numInMat).getY();
			Button btn = fromIndicesToButton(row, col);
			if (tileType == "Blocked") {
				btn.setStyle("-fx-background-image: url(" + "'/images/TileBlocked.png'" + "); "
						+ "-fx-background-size: 76px 60px;");
				tempBoard[row][col] = '0';
			} else if (tileType == "Question") {
				int diff = 0;
				if (stream == 3) {
					diff = i;
				} else {
					diff = diffQuestion.indexOf(-1);
				}
				if (diff == 0) {
					tempBoard[row][col] = '5';
					btn.setGraphic(ESAYQIMAGE);
					diffQuestion.add(0, numInMat);
				} else if (diff == 1) {
					tempBoard[row][col] = '6';
					btn.setGraphic(MEDQIMAGE);
					diffQuestion.add(1, numInMat);
				} else if (diff == 2) {
					tempBoard[row][col] = '7';
					btn.setGraphic(HardQIMAGE);
					diffQuestion.add(2, numInMat);
				}
				questionTiles.add(numInMat);
			} else if (tileType == "Random") {
				tempBoard[row][col] = '3';
			} else if (tileType == "Forgetable") {
				tempBoard[row][col] = '4';
			}
		}
		possibleTiles.removeAll(randomArray);
		return randomArray;
	}

	public void setBoard() {

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[x][y] = tempBoard[x][y];
			}
		}
	}

	public boolean reachedLevelPoints() {
		if (getLevelScore() < NEXT_LEVEL)
			return false;
		return true;
	}

	public void levelOver() {
		AbstractLevelControl.levelOver.addListener(new ChangeListener<Number>() {
			String modeMsg = "";
			String pointsMsg = "You've scored " + NEXT_LEVEL + " points or higher :)\nYou can proceed to next level";
			String noPointsMsg = "Failed to score " + NEXT_LEVEL
					+ " points :(\nYou will be directed to main page and your score will be saved";
			String finish = "You've finished all levels";

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() == PAUSE_LEVEL) {
					pauseAll();
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (timeup) {
								modeMsg = "Time's Up";
								if (currLevel == 4) {
									Alerts.alertBox(AlertType.INFORMATION, modeMsg, finish, "");
									returnToMap();
								} else {
									if (reachedLevelPoints()) {
										Alerts.alertBox(AlertType.INFORMATION, modeMsg, pointsMsg, "");
										returnToMap();
									} else {
										Alerts.alertBox(AlertType.INFORMATION, modeMsg, noPointsMsg, "");
										LevelsMapController.lost = true;
										returnToMap();
									}
								}

							} else {
								if (currLevel == 1 || currLevel == 2)
									modeMsg = "Queen got you!";
								else
									modeMsg = "King got you!";

								if (currLevel == 4) {
									Alerts.alertBox(AlertType.INFORMATION, finish, modeMsg, "");
									returnToMap();
								} else {
									if (reachedLevelPoints()) {
										Alerts.alertBox(AlertType.INFORMATION, modeMsg, pointsMsg, "");
										returnToMap();
									} else {
										Alerts.alertBox(AlertType.INFORMATION, modeMsg, noPointsMsg, "");
										LevelsMapController.lost = true;
										returnToMap();
									}
								}
							}
						}
					});

				}

			}
		});
	}

	public void returnToMap() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					LevelsMapController.setCurrLevel(currLevel + 1);
					utilities.switchScene2(Main.mainStage, "/view/LevelsMap.fxml");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void tileAlert(int tile) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (tile <= 3 && tile >= 1) {
					pauseAll();
					String msg = "";
					String action = "";
					if (tile == 1) { // q
						msg = "Question Tile";
						action = "A question will show up next and you need to solve it";
					} else if (tile == 2) { // r
						msg = "Random tile";
						action = "You've been moved to a random tile";
					} else if (tile == 3) { // f
						msg = "Forgettable tile";
						action = "Your last two moves will be reversed";
					}
					Alert conf = Alerts.inGameMsgs(AlertType.INFORMATION, msg, action, "");
					Optional<ButtonType> result = conf.showAndWait();
					if (result.isPresent() && result.get() == conf.getButtonTypes().get(0)) {
						time.resume();
						return;
					}
				}
			}
		});

	}

	public abstract void resumeAll();

	public abstract void pauseAll();

	public abstract void timeCancel();
}
