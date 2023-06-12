
package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final RobotModel robotModel = new RobotModel();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);
        WindowStateKeeper.Restorer restorer = new WindowStateKeeper.Restorer();

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        restorer.restoreState(logWindow, "logWindowComponent");
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow(robotModel);
        gameWindow.setSize(400, 400);
        restorer.restoreState(gameWindow, "gameWindowComponent");
        addWindow(gameWindow);

        PositionWindow positionWindow = new PositionWindow(robotModel);
        restorer.restoreState(positionWindow, "positionWindowComponent");
        addWindow(positionWindow);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int answer = JOptionPane.showConfirmDialog(
                        desktopPane,
                        "Уверены, что хотите выйти из игры?",
                        "Окно подтверждения",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (answer == JOptionPane.YES_OPTION){
                    WindowStateKeeper.Saver saver = new WindowStateKeeper.Saver();
                    saver.save(gameWindow, "gameWindowComponent");
                    saver.save(logWindow, "logWindowComponent");
                    saver.save(positionWindow, "positionWindowComponent");
                    saver.write();
                    MainApplicationFrame.this.dispose();
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
                }

            }
        });
        setJMenuBar(WindowMenu.generateMenuBar(this));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}
