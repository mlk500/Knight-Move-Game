package control;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

import com.sun.javafx.application.LauncherImpl;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Score;
import model.SysData;
import model.TrayIconDemo;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	private AnchorPane mainLayout;
	private Scene scene;
	private FadeTransition fadeOut;
	private AnchorPane root;
	private FadeTransition fadeIn;
	public static boolean isSplash = true;
	public static Stage mainStage;
	String img1 = "-fx-background-image: url('/images/splash1.jpg');" + "-fx-background-size: cover; "
			+ "-fx-background-repeat: no-repeat;";
	String img2 = "-fx-background-image: url('/images/splash2.jpg');"
            + "-fx-background-size: cover; "
            + "-fx-background-repeat: no-repeat;";
	@Override
	public void start(Stage primaryStage) {
		try {

			playSplash(primaryStage,"/view/PreLaunch.fxml",img1);
			mainStage = primaryStage;
//			FXMLLoader loader1 = new FXMLLoader();
//			loader1.setLocation(Main.class.getResource("/view/Menu.fxml"));
//			mainLayout = loader1.load();
//			scene = new Scene(mainLayout, 976, 560);
//			scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playSplash(Stage primaryStage, String view, String img) {
		try {
			root = FXMLLoader.load(getClass().getResource(view));
			root.setStyle(img);
			scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			fadeIn = new FadeTransition(Duration.seconds(3), root);
			fadeIn.setFromValue(0);
			fadeIn.setToValue(1);
			fadeIn.setCycleCount(1);
			
			fadeIn.play();

			fadeOut = new FadeTransition(Duration.seconds(3), root);
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0);
			fadeOut.setCycleCount(1);

			fadeIn.setOnFinished((e) -> {
				fadeOut.play();
			});
			if(isSplash) {
				
				fadeOut.setOnFinished((e)->{
					isSplash = false;
					playSplash(primaryStage, "/view/PreLaunch2.fxml", img2);
				});
			}
			else {
				fadeOut.setOnFinished((e)->{
					try {
						isSplash = true;
						FXMLLoader loader1 = new FXMLLoader();
						loader1.setLocation(Main.class.getResource("/view/Menu.fxml"));
						mainLayout = loader1.load();
						scene = new Scene(mainLayout, 976, 560);
						scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.show();
					}
					catch (Exception e2) {
						e2.printStackTrace();
					}

				});
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) throws AWTException {

		TrayIconDemo td = null;
		if (SystemTray.isSupported()) {
			td = new TrayIconDemo();
		} else {
			System.err.println("System tray not supported!");
		}
		String userHome = System.getProperty("user.home");
		File file = new File(userHome, "my.lock");
		try {
			FileChannel fc = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			FileLock lock = fc.tryLock();
			if (lock == null) {
				if (td != null) {
					td.displayTray(MessageType.ERROR, "Game Cannot be Opened",
							"Can't open game because it is already opened");
				}
				System.exit(1);
			} else {
				td.displayTray(MessageType.INFO, "Opening Knight's Move Game", "");
			}
		} catch (IOException e) {
			throw new Error(e);
		}

		SysData sysData = SysData.getInstance();
		sysData.readQuestions();
		sysData.readScores();
		sysData.readUserPrefs();

		launch(args);
	}
}
