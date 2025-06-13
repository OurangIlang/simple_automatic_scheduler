package com.mycompany;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GAJadwalBengkel {
    static final String[] HARI = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat"};
    static final int JAM_BUKA = 9, JAM_TUTUP = 21;
    static final int POP_SIZE = 50, MAX_GEN = 200;
    static final double MUTATION_RATE = 0.1;

    static Map<String, Integer> durasiLayanan = Map.of(
        "Mesin", 5,
        "Transmisi", 3,
        "Rem", 2,
        "Suspensi", 2
    );

    static class Servis {
        String nama, plat, layanan;
        int durasi;
        Servis(String nama, String plat, String layanan, int durasi) {
            this.nama = nama; this.plat = plat; this.layanan = layanan; this.durasi = durasi;
        }
    }
    static class Jadwal {
        Servis servis;
        String hari;
        int jamMulai, jamSelesai;
        Jadwal(Servis s, String h, int m) {
            servis = s; hari = h; jamMulai = m; jamSelesai = m + s.durasi;
        }
    }
    static class Individu {
        List<Jadwal> jadwals = new ArrayList<>();
        int fitness;
    }

// Baca data dari txtx
    static List<Servis> bacaData() throws IOException {
        // DEBUG data
        System.out.println("Path file yang dicari: " + new java.io.File("data.txt").getAbsolutePath());

        List<Servis> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line, nama = "", plat = "", layanan = "";
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Nama:")) nama = line.substring(5).trim();
                else if (line.startsWith("Plat Nomor:")) plat = line.substring(11).trim();
                else if (line.startsWith("Layanan Servis:")) layanan = line.substring(15).trim();
                else if (line.startsWith("-----------------------------")) {
                   
                    for (String l : layanan.split(",")) {
                        String lay = l.trim();
                        if (durasiLayanan.containsKey(lay)) {
                            list.add(new Servis(nama, plat, lay, durasiLayanan.get(lay)));
                            // Debug lagi
                            System.out.println("Data terbaca: " + nama + " | " + plat + " | " + lay);
                        }
                    }
                    
                    nama = ""; plat = ""; layanan = "";
                }
            }
        }
        System.out.println("Total servis terbaca: " + list.size());
        return list;
    }

    // Fitness, beri penalti apabila over/ di luar jam operasional
    static int hitungFitness(Individu ind) {
        int score = 0;
        for (Jadwal a : ind.jadwals) {
            // Cek jam operasional
            if (a.jamMulai < JAM_BUKA || a.jamSelesai > JAM_TUTUP) score -= 10;
            //  overlap
            for (Jadwal b : ind.jadwals) {
                if (a != b && a.hari.equals(b.hari) &&
                    Math.max(a.jamMulai, b.jamMulai) < Math.min(a.jamSelesai, b.jamSelesai)) {
                    score -= 20;
                }
            }
        }
        return score;
    }

    public static void main(String[] args) throws IOException {
        List<Servis> dataServis = bacaData();
        Random rand = new Random();
        List<Individu> pop = new ArrayList<>();

        //  populasi
        for (int i = 0; i < POP_SIZE; i++) {
            Individu ind = new Individu();
            for (Servis s : dataServis) {
                String hari = HARI[rand.nextInt(HARI.length)];
                int mulai = JAM_BUKA + rand.nextInt(JAM_TUTUP - JAM_BUKA - s.durasi + 1);
                ind.jadwals.add(new Jadwal(s, hari, mulai));
            }
            ind.fitness = hitungFitness(ind);
            pop.add(ind);
        }

        // Evolusi
        for (int gen = 0; gen < MAX_GEN; gen++) {
            pop.sort((a, b) -> b.fitness - a.fitness);
            if (pop.get(0).fitness == 0) break; // solusi optimal
            List<Individu> newPop = new ArrayList<>();
            for (int i = 0; i < POP_SIZE; i++) {
                Individu parent1 = pop.get(rand.nextInt(POP_SIZE / 2));
                Individu parent2 = pop.get(rand.nextInt(POP_SIZE / 2));
                Individu child = new Individu();
                for (int j = 0; j < dataServis.size(); j++) {
                    Jadwal js = rand.nextBoolean() ? parent1.jadwals.get(j) : parent2.jadwals.get(j);
    // Mutasi
                    if (rand.nextDouble() < MUTATION_RATE) {
                        String hari = HARI[rand.nextInt(HARI.length)];
                        int mulai = JAM_BUKA + rand.nextInt(JAM_TUTUP - JAM_BUKA - js.servis.durasi + 1);
                        js = new Jadwal(js.servis, hari, mulai);
                    }
                    child.jadwals.add(js);
                }
                child.fitness = hitungFitness(child);
                newPop.add(child);
            }
            pop = newPop;
        }

        // Output solusi 
        Individu best = pop.get(0);
        System.out.println("Jadwal Servis Bengkel Umbawadu");

        try (java.io.FileWriter writer = new java.io.FileWriter("hasil_jadwal.txt")) {
            writer.write("=== Jadwal Servis Bengkel ===\n");
            for (Jadwal js : best.jadwals) {
                String line = String.format("%-10s | %-10s | %-10s | %02d:00 - %02d:00\n",
                    js.servis.nama, js.servis.plat, js.hari, js.jamMulai, js.jamSelesai);
                System.out.print(line);
                writer.write(line);
            }
            writer.write("\n");
            System.out.println("\nMantap Niga");
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
    }
}