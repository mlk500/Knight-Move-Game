package control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import model.Score;
import model.SysData;
import model.Utilities;

public class MenuController implements Initializable {
	private Utilities utilities = Utilities.getInstance();
	@FXML
	private MediaView AvatarTalk = new MediaView();
	@FXML
	private ImageView avatarImage = new ImageView();

	@FXML
	private MediaView music = new MediaView();
	
	Media m2 = new Media(getClass().getResource("/music/musicBackGround.mp3").toExternalForm());
	MediaPlayer d2 = new MediaPlayer(m2);
	Media m = new Media(getClass().getResource("/music/avatarTalk.m4a").toExternalForm());
	MediaPlayer d1 = new MediaPlayer(m);
	@FXML
	private Button play;

	@FXML
	private Button QuestionWizard;

	@FXML
	private Button Instructions;

	@FXML
	private TableView<Score> scoresTable;

	@FXML
	private TableColumn<Score, String> time;

	@FXML
	private TableColumn<Score, Integer> level;
	@FXML
	private ImageView avatrDancing;

	@FXML
	private TableColumn<Score, Integer> totalScore;

	@FXML
	private TableColumn<Score, String> nickName;

	@FXML
	private Button Exit;
	private SysData sysData = SysData.getInstance();

	private ObservableList<Score> scores = FXCollections.observableArrayList(sysData.getScoresList());


	@FXML
	private Button unMuteButton;

	@FXML
	private Button muteButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		unMuteButton.setGraphic(Utilities.getAvatar("/images/unMute.png", 50, 50));
		muteButton.setGraphic(Utilities.getAvatar("/images/mute.png", 50, 50));
		runMusic();

		nickName.setCellValueFactory(new PropertyValueFactory<Score, String>("nickname"));
		time.setCellValueFactory(new PropertyValueFactory<Score, String>("dateTime"));
		level.setCellValueFactory(new PropertyValueFactory<Score, Integer>("level"));
		totalScore.setCellValueFactory(new PropertyValueFactory<Score, Integer>("score"));
		scoresTable.setItems(scores);
	}

	@FXML
	void PlayAction(ActionEvent event) throws IOException {
		d2.pause();
		d1.pause();
//		utilities.switchScene(event, "/view/Level1.fxml");

		if (!InsertNicknameController.nicknameInserted) {
			utilities.switchScene(event, "/view/InsertNickname.fxml");
		} else {
			AbstractLevelControl.setTotalScore(0);
			AbstractLevelControl.setLevelScore(0);
			LevelsMapController.setCurrLevel(1);
			LevelsMapController.lost = false;
			utilities.switchScene(event, "/view/LevelsMap.fxml");
		}
	}

	private void runMusic() {
		AvatarTalk.setMediaPlayer(d1);
		d1.play();
		d1.setCycleCount(1);
		d1.setOnEndOfMedia(() -> {
			avatarImage.setOpacity(0);
			avatrDancing.setOpacity(0.7);
			music.setMediaPlayer(d2);
			d2.play();
			d2.setCycleCount(MediaPlayer.INDEFINITE);
		});
	}

	@FXML
	void InstructionsAction(ActionEvent event) throws IOException {
		this.d2.pause();
		utilities.switchScene(event, "/view/Instructions.fxml");

	}

	@FXML
	void QuestionWizardAction(ActionEvent event) throws IOException {
		this.d2.pause();
		utilities.switchScene(event, "/view/QuestionScene.fxml");
	}

	@FXML
	void ExitAction(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void mute(ActionEvent event) {
		d1.pause();
		this.d2.pause();
	}

	@FXML
	void unMute(ActionEvent event) {
		d1.play();
		this.d2.play();
	}

}
