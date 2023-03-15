package model;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class TrayIconDemo {

	public void displayTray(MessageType messageType, String title, String message) throws AWTException {
		// Obtain only one instance of the SystemTray object
		SystemTray tray = SystemTray.getSystemTray();

		// If the icon is a file
		Image image = Toolkit.getDefaultToolkit().createImage("/images/horse2.jpg");

		TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
		// Let the system resize the image if needed
		trayIcon.setImageAutoSize(true);
		// Set tooltip text for the tray icon
		trayIcon.setToolTip("Knight's Move");
		tray.add(trayIcon);
		trayIcon.displayMessage(title, message, messageType);
	}
}