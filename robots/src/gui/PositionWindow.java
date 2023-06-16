package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PositionWindow extends JInternalFrame implements Observer {
    private RobotModel m_model;
    private JLabel labelX;
    private JLabel labelY;

    public PositionWindow(RobotModel robotModel) {
        super("Окно модели", true, true, true, true);
        m_model = robotModel;
        m_model.addObserver(this);

        JPanel panel = new JPanel(new BorderLayout());
        labelX = new JLabel("X:" + m_model.getCurrentX());
        labelY = new JLabel("Y: " + m_model.getCurrentY());
        panel.add(labelX, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(labelY, BorderLayout.CENTER);

        getContentPane().add(panel);
        pack();
    }
    @Override
    public void update(Observable o, Object key) {
        if (RobotModel.ROBOT_POSITION_CHANGED == key)
            onTextChanged();
    }

    private void onTextChanged()
    {
        labelX.setText("X:" + m_model.getCurrentX());
        labelY.setText("Y: " + m_model.getCurrentY());
    }
}

