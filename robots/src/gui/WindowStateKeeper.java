package gui;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.*;

public class WindowStateKeeper {
    private static Properties properties;
    private static final String filename = "config.properties";
    private static File config;
    public static class Saver {
        Saver(){
            properties = new Properties();
        }
        public void save(JInternalFrame c, String key){
            String position = String.format("%d,%d,%d,%d", c.getX(), c.getY(), c.getWidth(), c.getHeight());
            properties.put(key, position);
            properties.put(key + ".is_icon", "" + c.isIcon());
        }

        public void write() {
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(filename);
                properties.store(os, "Windows");
            } catch (IOException ignored) {}
        }
    }

    public static class Restorer {
        Restorer() {
            config = new File(filename);
            properties = new Properties();
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
                    } catch (PropertyVetoException ignored) {}
                else {
                    try {
                        component.setIcon(false);
                    } catch (PropertyVetoException ignored) {}
                }
            }
        }

        private void loadProperties() {
            InputStream is = null;
            try {
                is = new FileInputStream(config);
                properties.load(is);
            } catch (IOException ignored) {}
        }
    }
}

