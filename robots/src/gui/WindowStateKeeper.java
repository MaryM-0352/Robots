package gui;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.*;

public class WindowStateKeeper {
    private static Properties properties;
    private static final String filename = "config.properties";
    private static File config;
    private static final Restorer restorer = new Restorer();

    public static void restoreState(Savable component) {
        restorer.restoreState((JInternalFrame) component, component.getIdentification());
    }

    public static class Saver {
        Saver(){
            properties = new Properties();
        }
        public void save(Savable c){
            JInternalFrame window = (JInternalFrame) c;
            String key = c.getIdentification();
            String position = String.format("%d,%d,%d,%d", window.getX(), window.getY(), window.getWidth(), window.getHeight());
            properties.put(key, position);
            properties.put(key + ".is_icon", "" + window.isIcon());
        }

        public void write(JFrame frame) {
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(filename);
                properties.store(os, "Windows");
            } catch (IOException ex) {
                int answer = JOptionPane.showConfirmDialog(
                        frame,
                        "Ошибка записи! Вы точно хотите выйти?",
                        "Окно подтверждения",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (answer == JOptionPane.NO_OPTION){
                    write(frame);
                }
            }
        }
    }

    private static class Restorer {
        private static boolean isLoad;
        Restorer() {
            config = new File(filename);
            properties = new Properties();
            isLoad = false;
        }

        public void restoreState(JInternalFrame component, String key) {
            loadProperties();
            Object positionKey = properties.get(key);
            if (positionKey != null){
                String[] position = positionKey.toString().split(",");
                component.setBounds(Integer.parseInt(position[0]), Integer.parseInt(position[1]),
                        Integer.parseInt(position[2]), Integer.parseInt(position[3]));
            }
            restoreIcon(component, key);
        }

        private void restoreIcon(JInternalFrame component, String key) {
            Object iconKey = properties.get(key + ".is_icon");
            if (iconKey != null) {
                if (Objects.equals(iconKey, "true"))
                    try {
                        component.setIcon(true);
                    } catch (PropertyVetoException ignored) {
                        //полностью контролируемая передача параметра, недопустимые значения невозможны
                    }
                else {
                    try {
                        component.setIcon(false);
                    } catch (PropertyVetoException ignored) {
                        //полностью контролируемая передача параметра, недопустимые значения невозможны
                    }
                }
            }
        }

        private void loadProperties() {
            if (!isLoad){
                InputStream is = null;
                try {
                    is = new FileInputStream(config);
                    properties.load(is);
                    isLoad = true;
                } catch (IOException ignored) {}
            }
        }
    }
}
