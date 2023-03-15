package control;

import java.awt.Menu;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Score;
import model.SysData;
import model.Utilities;

public class LevelsMapController implements Initializable{
	private Utilities utilities = Utilities.getInstance();

	@FXML
	private Button Level4;

	@FXML
	private Button Level2;

	@FXML
	private Button Level3;

	@FXML
	private Button Level1;

	SysData sysData = SysData.getInstance();
	public static IntegerProperty levelPoints = new SimpleIntegerProperty(-1);
	private static int currLevel = 1;
	public static boolean lost = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			startNextLevel(currLevel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void disableButtons() {
		Level1.setDisable(true);
		Level2.setDisable(true);
		Level3.setDisable(true);
		Level4.setDisable(true);
	}

	@FXML
	void Level1Action(ActionEvent event) throws IOException {
		utilities.switchScene(event, "/view/Level1.fxml");
	}

	@FXML
	void Level2Action(ActionEvent event) throws IOException {
		utilities.switchScene(event, "/view/Level2.fxml");
	}

	@FXML
	void Level3Action(ActionEvent event) throws IOException, InterruptedException {
		utilities.switchSceneWithControl(event, "/view/Level3.fxml");
	}

	@FXML
	void Level4Action(ActionEvent event) throws IOException {
		utilities.switchSceneWithControl(event, "/view/Level4.fxml");
	}

	@FXML
	void BackToMenu(ActionEvent event) throws IOException {
		utilities.switchScene(event, "/view/Menu.fxml");

	}

	@FXML
	void UpdateNickName(ActionEvent event) throws IOException {
		utilities.switchScene(event, "/view/InsertNickname.fxml");
	}

	public void initPoints() {
		levelPoints.set(-1);
	}

	// save score in Player history
	public void saveScore(int level) {
		LocalDateTime now = LocalDateTime.now();
		Score score = new Score(InsertNicknameController.name, AbstractLevelControl.getTotalScore(), level, now);
		SysData.getInstance().addScore(score);
	}

	public void endGame() throws IOException {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				disableButtons();
				saveScore(currLevel-1);
				try {
					utilities.switchScene2(Main.mainStage, "/view/Menu.fxml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void startNextLevel(int level) throws IOException {
		disableButtons();
		if(lost) {
			endGame();
		}
		else {
			if (level == 1) {
				Level1.setDisable(false);
			}

			else if (level == 2) {
				Level2.setDisable(false);
			}

			else if (level == 3) {
				Level3.setDisable(false);
			}

			else if (level == 4) {
				Level4.setDisable(false);
			}
			else {
				saveScore(4);
				System.out.println("finish level 4");
				String mainPageMsg = "Your score will be saved and you will be directed to main page";
				String msg = "Congratulations! You've Finished all Levels";
				String winner = "You've scored "+AbstractLevelControl.WINNER+ 
						" points or higher in all levels!\nYou won a trophy!"+mainPageMsg;
				String notWinner = "Good job finishing all the levels!\n"+mainPageMsg;
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						try {
							if(AbstractLevelControl.getTotalScore() >= AbstractLevelControl.WINNER) {
								Alerts.alertBox(AlertType.INFORMATION,msg, winner, "");
								utilities.switchScene2(Main.mainStage, "/view/Menu.fxml");
							}
							else {
								Alerts.alertBox(AlertType.INFORMATION,msg, notWinner, "");
								utilities.switchScene2(Main.mainStage, "/view/Menu.fxml");				
							}
							return;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
			}

		}
		
	}

	public static void setCurrLevel(int currLevel) {
		LevelsMapController.currLevel = currLevel;
	}
	
}
