package com.example.studentgui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class StudentForm extends JFrame {
    private JTextField tfNama;
    private JTextField tfNpm;
    private JRadioButton rbLaki;
    private JRadioButton rbPerempuan;
    private JSpinner spUmur;
    private JTextField tfNoHp;
    private JTextArea taAlamat;
    private DefaultTableModel tableModel;
    private Path csvPath;

    public StudentForm() {
        super("Input Data Mahasiswa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        csvPath = Path.of("students.csv");

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        tfNama = new JTextField(20);
        tfNpm = new JTextField(12);
        rbLaki = new JRadioButton("Laki-laki");
        rbPerempuan = new JRadioButton("Perempuan");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbLaki); bg.add(rbPerempuan);
        spUmur = new JSpinner(new SpinnerNumberModel(18, 0, 120, 1));
        tfNoHp = new JTextField(14);
        taAlamat = new JTextArea(3, 20);
        JScrollPane spAlamat = new JScrollPane(taAlamat);

        int row = 0;
        addLabelAnd(c, form, "Nama:", tfNama, row++);
        addLabelAnd(c, form, "NPM:", tfNpm, row++);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        genderPanel.add(rbLaki); genderPanel.add(rbPerempuan);
        addLabelAnd(c, form, "Jenis Kelamin:", genderPanel, row++);
        addLabelAnd(c, form, "Umur:", spUmur, row++);
        addLabelAnd(c, form, "No. HP:", tfNoHp, row++);
        addLabelAnd(c, form, "Alamat:", spAlamat, row++);

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.addActionListener(this::onSimpan);
        c.gridx = 1; c.gridy = row++; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        form.add(btnSimpan, c);

        String[] cols = {"Nama","NPM","Jenis Kelamin","Umur","No HP","Alamat"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, form, tableScroll);
        split.setDividerLocation(250);
        add(split);

        loadCsvIfExists();
    }

    private void addLabelAnd(GridBagConstraints c, JPanel panel, String label, Component comp, int row) {
        c.gridx = 0; c.gridy = row; c.gridwidth = 1;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.gridy = row; c.gridwidth = 2;
        panel.add(comp, c);
    }

    private void onSimpan(ActionEvent e) {
        String nama = tfNama.getText().trim();
        String npm = tfNpm.getText().trim();
        String jk = rbLaki.isSelected() ? "Laki-laki" : rbPerempuan.isSelected() ? "Perempuan" : "";
        int umur = (Integer) spUmur.getValue();
        String noHp = tfNoHp.getText().trim();
        String alamat = taAlamat.getText().trim();

        if (nama.isEmpty() || npm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan NPM wajib diisi", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student s = new Student(nama, npm, jk, umur, noHp, alamat);
        tableModel.addRow(s.toTableRow());

        try {
            String line = s.toCSV() + System.lineSeparator();
            Files.writeString(csvPath, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan ke file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        clearForm();
    }

    private void clearForm() {
        tfNama.setText("");
        tfNpm.setText("");
        bgClear();
        spUmur.setValue(18);
        tfNoHp.setText("");
        taAlamat.setText("");
    }

    private void bgClear() {
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
    }

    private void loadCsvIfExists() {
        if (Files.exists(csvPath)) {
            try {
                for (String line : Files.readAllLines(csvPath)) {
                    if (line.isBlank()) continue;
                    String[] parts = line.split(",", 6);
                    if (parts.length == 6) {
                        tableModel.addRow(new Object[]{parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5]});
                    }
                }
            } catch (IOException ex) {
                // ignore loading errors
            }
        }
    }
}
