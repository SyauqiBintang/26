package com.example.studentgui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentForm f = new StudentForm();
            f.setVisible(true);
        });
    }
}
