package ChatLocale;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class Notifica {

    public static Boolean windowFocus = false;

    public void visualizzaNotifica(String msg) throws AWTException {
        if (windowFocus) {
            SystemTray tray = SystemTray.getSystemTray();

            // If the icon is a file
            Image image = Toolkit.getDefaultToolkit().createImage("notificationIcon.png");
            // Alternative (if the icon is on the classpath):
            // Image image =
            // Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            // Let the system resize the image if needed
            trayIcon.setImageAutoSize(true);
            // Set tooltip text for the tray icon
            trayIcon.setToolTip("System tray icon demo");
            tray.add(trayIcon);

            trayIcon.displayMessage("Loner Chat Locale", msg, MessageType.INFO);
        }
    }
}
