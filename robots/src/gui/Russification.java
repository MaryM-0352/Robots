package gui;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Russification {
    public static void apply() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Button", new Locale("ru", "RU"));
        String closeName = resourceBundle.getString("close");
        String maxName = resourceBundle.getString("max");
        String minName = resourceBundle.getString("min");
        String restartName = resourceBundle.getString("restart");
        String moveName = resourceBundle.getString("move");
        String sizeName = resourceBundle.getString("size");
        String yes = resourceBundle.getString("yes");
        String no = resourceBundle.getString("no");
        UIManager.put("InternalFrame.closeButtonToolTip", closeName);
        UIManager.put("InternalFrameTitlePane.closeButtonText", closeName);
        UIManager.put("InternalFrame.maxButtonToolTip", maxName);
        UIManager.put("InternalFrameTitlePane.maximizeButtonText", maxName);
        UIManager.put("InternalFrame.iconButtonToolTip", minName);
        UIManager.put("InternalFrameTitlePane.minimizeButtonText", minName);
        UIManager.put("InternalFrameTitlePane.restoreButtonText", restartName);
        UIManager.put("InternalFrameTitlePane.moveButtonText", moveName);
        UIManager.put("InternalFrameTitlePane.sizeButtonText", sizeName);

        UIManager.put("OptionPane.yesButtonText", yes);
        UIManager.put("OptionPane.noButtonText", no);
    }
}
