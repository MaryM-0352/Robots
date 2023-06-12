package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PositionWindow extends JInternalFrame implements Observer {
    private RobotModel m_model;
    private final JTextArea m_position = new JTextArea();
    public PositionWindow(RobotModel robotModel) {
        super("Окно модели", true, true, true, true);
        m_model = robotModel;
        m_model.addObserver(this);
        m_position.setPreferredSize(new Dimension(250, 80));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_position, BorderLayout.CENTER);

        getContentPane().add(panel);
        pack();
    }
    @Override
    public void update(Observable o, Object arg) {
        onTextChanged();
    }

    private void onTextChanged()
    {
        m_position.setText("X:" + m_model.getCurrentX() + "; Y: " + m_model.getCurrentY());
    }
}

