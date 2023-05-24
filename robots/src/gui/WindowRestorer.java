package gui;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class WindowRestorer {
    private static Properties properties = new Properties();
    private static final String filename = "config.properties";

    public void save(Component c, Boolean is_Icon){
        String position = String.format("%d,%d,%d,%d", c.getX(), c.getY(), c.getWidth(), c.getHeight());
        properties.put(c.getName(), position + "," + is_Icon);
    }

    public void write() {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filename);
            properties.store(os, "Windows");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean setIcon(Component c) throws FileNotFoundException {
        return setPosition(c);
    }

    public Boolean setPosition(Component component) throws FileNotFoundException {
        setProperties();
        Object key = properties.get(component.getName());
        String[] position = null;
        if (key != null){
            position = key.toString().split(",");
            component.setBounds(Integer.parseInt(position[0]), Integer.parseInt(position[1]),
                    Integer.parseInt(position[2]), Integer.parseInt(position[3]));
            return Objects.equals(position[4], "true");
        }
        return false;
    }

    private void setProperties() throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists())
            throw new FileNotFoundException();
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

