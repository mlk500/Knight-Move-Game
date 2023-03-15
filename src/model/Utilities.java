package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import control.AbstractLevelControl;
import control.KingLevels;
import control.QueenLevels;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Utilities {
	private Parent root;
	private static Utilities instance;

	public static Utilities getInstance() {
		if (instance == null)
			instance = new Utilities();
		return instance;
	}

	public void switchScene(ActionEvent event, String fxmlFile) throws IOException {
		root = FXMLLoader.load(getClass().getResource(fxmlFile));
		Scene adminScene = new Scene(root);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		if (window != null) {
			window.setScene(adminScene);
			window.show();
		}

	}

	public void switchScene2(Stage window, String fxmlFile) throws IOException {
		root = FXMLLoader.load(getClass().getResource(fxmlFile));
		Scene adminScene = new Scene(root);
		if (window != null) {
			window.setScene(adminScene);
			window.show();
		}

	}

	public FXMLLoader getSceneController(String fxmlFile) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlFile));
		return loader;
	}

	public void loadScene(ActionEvent event, FXMLLoader loader) throws IOException {
		root = loader.load();
		Scene adminScene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(adminScene);
		stage.show();
	}

	public String[] getQuestionLevels() {
		String[] levels = { "Easy", "Meduim", "Hard" };
		return levels;
	}

	public ArrayList<String> getTeams() {
		ArrayList<String> teams = new ArrayList<>(Arrays.asList("Spider", "Husky", "Chimps", "Giraffe", "Tiger"));
		return teams;
	}

	// return true if string has only white spaces or less than 2 characters
	public boolean containsWhiteSpacesOnly(String string) {
		if (string.trim().length() > 0)
			return false;
		return true;
	}

	public int convertLeveltoNumber(String level) {
		String[] levels = getQuestionLevels();
		if (levels[0].equals(level))
			return 1;
		else if (levels[1].equals(level))
			return 2;
		else
			return 3;
	}

	public AbstractLevelControl switchSceneWithControl(ActionEvent event, String fxmlFile) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlFile));
		root = loader.load();
		Scene adminScene = new Scene(root);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(adminScene);
		window.show();
		return loader.getController();
	}
	// get possible places

	private static Position getValidIndices(int movei, int movej) {
		if (movei < 0) {
			movei += 8;
		}
		if (movej < 0) {
			movej += 8;
		}
		return new Position(movei % 8, movej % 8);
	}

	public static List<Tile> addNeighbours(Tile position, char[][] matrix, final int numOfRows, final int numOfColumns,
			ArrayList<Integer> dRow, ArrayList<Integer> dCol, String player) {

		List<Tile> list = new LinkedList<Tile>();
		// Go to the adjacent cells
		for (int i = 0; i < dRow.size(); i++) {
			Tile t = position;
			Position indices = new Position(position.getPos().getX() + dRow.get(i),
					position.getPos().getY() + dCol.get(i));
			if (player != "Q") {
				indices = getValidIndices(position.getPos().getX() + dRow.get(i),
						position.getPos().getY() + dCol.get(i));
			}
			int adjx = indices.getX();
			int adjy = indices.getY();
			if (adjx >= 0 && adjx <= 7 && adjy >= 0 && adjy <= 7) {
				if (matrix[adjx][adjy] != '0') {
					t = new Tile(adjx, adjy, position);
					list.add(t);
					if (player == "H") {
						AbstractLevelControl.positionList.add(t.pos);
					}
				}
			}
		}
		return list;
	}

	public boolean containsIgnoreCase(String str, String searchStr) {
		if (str == null || searchStr == null)
			return false;

		final int length = searchStr.length();
		if (length == 0)
			return true;

		for (int i = str.length() - length; i >= 0; i--) {
			if (str.regionMatches(true, i, searchStr, 0, length))
				return true;
		}
		return false;
	}

	// Receives path and size, returns an image in the size sent
	public static ImageView getAvatar(String s, int x, int y) {
		Image avatar = new Image(s);
		ImageView imageAvatar = new ImageView(avatar);
		imageAvatar.setFitWidth(x);
		imageAvatar.setFitHeight(y);
		return imageAvatar;
	}

	public static Tile pathExists(char[][] board, ArrayList<Integer> dRow, ArrayList<Integer> dCol, String player) {
		char matrix[][] = board;
		Tile source = null;
		if (player == "Q") {
			source = QueenLevels.queen.getCurrTile();

		}
		if (player == "K") {
			source = KingLevels.king.getCurrTile();
		}
		Queue<Tile> queue = new LinkedList<Tile>();
		int numOfRows = matrix.length;
		int numOfColumns = matrix[0].length;

		queue.add(source);
		while (!queue.isEmpty()) {
			Tile poped = queue.poll();
			if (matrix[poped.getPos().getX()][poped.getPos().getY()] == 'H') {

				while (poped.getParent().getParent() != null) {
					poped = poped.getParent();
				}
				return poped;
			} else {
				matrix[poped.getPos().getX()][poped.getPos().getY()] = '0';

				List<Tile> neighbourList = addNeighbours(poped, matrix, numOfRows, numOfColumns, dRow, dCol, player);
				queue.addAll(neighbourList);
				if (player == "Q") {
					dRow = King.DROWKING;
					dCol = King.DCOLKING;
				}
			}
		}
		return null;
	}

}
