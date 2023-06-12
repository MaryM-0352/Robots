package localization;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Russification {
    public static void apply() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Button", new Locale("ru", "RU"));
        String closeName = resourceBundle.getString("menu.close");
        String maxName = resourceBundle.getString("menu.max");
        String minName = resourceBundle.getString("menu.min");
        String restartName = resourceBundle.getString("menu.restart");
        String moveName = resourceBundle.getString("menu.move");
        String sizeName = resourceBundle.getString("menu.size");
        String yes = resourceBundle.getString("close_dialog.yes");
        String no = resourceBundle.getString("close_dialog.no");
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
