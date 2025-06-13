package com.mycompany;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;


public class MyFrame extends javax.swing.JFrame {

public MyFrame() {
        this.setTitle("Umabawadu Service Schedule  v0.0.0.1");
        this.setSize(400, 400);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        
        javax.swing.ImageIcon icon = new javax.swing.ImageIcon("src/main/java/com/mycompany/gui/image1.png");
        this.setIconImage(icon.getImage());
        
        this.getContentPane().setBackground(new java.awt.Color(128, 128, 128));

//Main panelnya ini kocag
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // padding        

//Panel kedua kebawah
        JPanel Panel2 = new JPanel();
        Panel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5 ));
        Panel2.setOpaque(false);
        JLabel label2 = new JLabel("Nama Anda");
        Panel2.add(label2);   
        JTextField namaanda = new JTextField(18);
        Panel2.add(namaanda);
        mainPanel.add(Panel2);
        mainPanel.add(Box.createVerticalStrut(1));

//Panel ketiga kebawah
        JPanel Panel3 = new JPanel();
        Panel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10 ,5));        
        JLabel label3 = new JLabel("No.Telepon"); // nanti ini pas di run jadi Plat nomor
        Panel3.add(label3);   
        JTextField nomortelp = new JTextField(18);
        Panel3.add(nomortelp);
        mainPanel.add(Panel3);
        mainPanel.add(Box.createVerticalStrut(1));


//Panel keempat kebawah
        JPanel Panel4 = new JPanel();
        Panel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        JLabel labelplat = new JLabel("Plat Nomor Kendaraan"); 
        Panel4.add(labelplat);   
        JTextField platnomor1 = new JTextField(12);
        platnomor1.setMaximumSize(new java.awt.Dimension(200, 10));
        Panel4.add(platnomor1);

        mainPanel.add(Panel4);


// Panel Radio button atau Combo box (Pilih atu)
        JPanel Panel4a = new JPanel();
        Panel4a.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        JLabel labeljenis = new JLabel("Layanan Servis");
        Panel4a.add(labeljenis);

        JCheckBox cbMesin = new JCheckBox("Mesin");
        JCheckBox cbTransmisi = new JCheckBox("Transmisi");
        JCheckBox cbRem = new JCheckBox("Rem");
        JCheckBox cbSuspensi = new JCheckBox("Suspensi");

        Panel4a.add(cbMesin);
        Panel4a.add(cbTransmisi);
        Panel4a.add(cbRem);
        Panel4a.add(cbSuspensi);

mainPanel.add(Panel4a);

//Panel kelima kebawah
        JPanel Panel5 = new JPanel();
        Panel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        JLabel labeltanggal = new JLabel("Tanggal terakhir servis"); 
        Panel5.add(labeltanggal);   

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        Panel5.add(dateSpinner);
        mainPanel.add(Panel5);       

//Panel keenam kebawah (panel tombol)
JPanel panelButton = new JPanel();
panelButton.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));

JButton submitbutton = new JButton("Submit");
JButton hasilGAButton = new JButton("Hasil GA");

panelButton.add(submitbutton);
panelButton.add(hasilGAButton);

mainPanel.add(Box.createVerticalStrut(15)); // jarak atas tombol
mainPanel.add(panelButton);

this.add(mainPanel);
this.pack();
this.setLocationRelativeTo(null);
this.setVisible(true);

// ActionListener tombol Submit (sudah ada)
submitbutton.addActionListener(e -> {
    
            String nama = namaanda.getText();
            String telepon = nomortelp.getText();
            String plat = platnomor1.getText();
            String tanggalStr = new java.text.SimpleDateFormat("dd/MM/yyyy").format(dateSpinner.getValue());
            
            String raw = nama + telepon + plat + tanggalStr + System.currentTimeMillis();
            String specialCode = Integer.toHexString(raw.hashCode()).toUpperCase();  
            
            StringBuilder layananServis = new StringBuilder();
                if (cbMesin.isSelected()) {
                        layananServis.append("Mesin, ");
                }
                if (cbTransmisi.isSelected()) {
                        layananServis.append("Transmisi, ");
                }
                if (cbRem.isSelected()) {
                        layananServis.append("Rem, ");
                }
                if (cbSuspensi.isSelected()) {
                        layananServis.append("Suspensi, ");
                }
                if (layananServis.length() > 0) {
                        layananServis.setLength(layananServis.length() - 2); // menghapus koma terakhir
                } else {
                        layananServis.append("Tidak ada layanan yang dipilih");
                }
            
            
            System.out.println("Special Code: " + specialCode);
            System.out.println(nama);
            System.out.println( telepon);
            System.out.println(plat);
            System.out.println(tanggalStr);

            try {java.io.FileWriter writer = new java.io.FileWriter("data.txt", true);

                writer.write("Kode Khusus: " + specialCode + "\n");
                writer.write("Nama: " + nama + "\n");
                writer.write("No.Telepon: " + telepon + "\n");
                writer.write("Plat Nomor: " + plat + "\n");
                writer.write("Layanan Servis: " + layananServis.toString() + "\n");
                writer.write("Terakhir Servis: " + tanggalStr + "\n");
                writer.write("-----------------------------\n");
                writer.close();
                
                javax.swing.JOptionPane.showMessageDialog(this, "Data udeh disimpen di Database");
                
            } catch (java.io.IOException ex) {
            }
            
        }       );
        
// ActionListener tombol Hasil GA
hasilGAButton.addActionListener(e -> {
    try {
        // Jalankan GA (pastikan GAJadwalBengkel.main() tidak exit program)
        GAJadwalBengkel.main(new String[0]);
        // Tampilkan hasil ke user (misal, tampilkan isi file hasil_jadwal.txt)
        java.nio.file.Path hasilPath = java.nio.file.Paths.get("hasil_jadwal.txt");
        String hasil = java.nio.file.Files.readString(hasilPath);
        javax.swing.JOptionPane.showMessageDialog(this, hasil, "Hasil Jadwal GA", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal menjalankan GA atau membaca hasil!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
});
}
}
