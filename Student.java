package com.example.studentgui;

public class Student {
    private String nama;
    private String npm;
    private String jenisKelamin;
    private int umur;
    private String noHp;
    private String alamat;

    public Student(String nama, String npm, String jenisKelamin, int umur, String noHp, String alamat) {
        this.nama = nama;
        this.npm = npm;
        this.jenisKelamin = jenisKelamin;
        this.umur = umur;
        this.noHp = noHp;
        this.alamat = alamat;
    }

    public Object[] toTableRow() {
        return new Object[]{nama, npm, jenisKelamin, umur, noHp, alamat};
    }

    public String toCSV() {
        String safeAlamat = alamat.replaceAll("\r?\n", " ").replaceAll(",", " ");
        return String.format("%s,%s,%s,%d,%s,%s", nama, npm, jenisKelamin, umur, noHp, safeAlamat);
    }
}
