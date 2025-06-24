/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package crud;

import db.DBHelper;
import model.Policy;
import util.GenericFilter;
import util.Lang;
import util.PolicySerializer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.Locale;
import util.LanguageConfig;

/**
 *
 * @author Fikal Alif
 */
public class PolicyFrame extends javax.swing.JFrame {

    private JTable table;
    private DefaultTableModel model;
    // Di PolicyFrame.java
    private JButton btnAdd, btnEdit, btnDelete, btnSave, btnLoad, btnReload, btnLang;
    private JLabel lblTitle;

    private boolean isID = true;

    public PolicyFrame() {
        setTitle("📰 " + Lang.get("title") + " - " + Lang.get("institution"));
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columns = {"ID", Lang.get("title"), Lang.get("institution"), Lang.get("category"), Lang.get("date"), Lang.get("status"), Lang.get("description")};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        btnAdd = new JButton(Lang.get("add"));
        btnEdit = new JButton(Lang.get("edit"));
        btnDelete = new JButton(Lang.get("delete"));
        btnSave = new JButton(Lang.get("save"));
        btnLoad = new JButton("📂 " + Lang.get("open"));
        btnReload = new JButton("🔄 " + Lang.get("reload"));
        btnLang = new JButton("🌐 " + Lang.get("change_language"));

        btnAdd.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnDelete.addActionListener(e -> hapusData());
        btnSave.addActionListener(e -> simpanData());
        btnLoad.addActionListener(e -> muatData());
        btnReload.addActionListener(e -> loadAsync());
        btnLang.addActionListener(e -> gantiBahasa());

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnAdd);
        panelBtn.add(btnEdit);
        panelBtn.add(btnDelete);
        panelBtn.add(btnSave);
        panelBtn.add(btnLoad);
        panelBtn.add(btnReload);
        panelBtn.add(btnLang);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panelBtn, BorderLayout.SOUTH);

        loadPolicies();
    }

    private void loadPolicies() {
        model.setRowCount(0);
        try (Connection conn = DBHelper.connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM policies");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("institution"),
                    rs.getString("category"),
                    rs.getDate("date"),
                    rs.getString("status"),
                    rs.getString("description")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tambahData() {
        Policy p = showInputDialog(null);
        if (p != null) {
            try (Connection conn = DBHelper.connect()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO policies (title, institution, category, date, status, description) VALUES (?, ?, ?, ?, ?, ?)");
                stmt.setString(1, p.getTitle());
                stmt.setString(2, p.getInstitution());
                stmt.setString(3, p.getCategory());
                stmt.setString(4, p.getDate());
                stmt.setString(5, p.getStatus());
                stmt.setString(6, p.getDescription());
                stmt.executeUpdate();
                loadPolicies();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void editData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }

        Policy p = new Policy(
                (int) model.getValueAt(row, 0),
                (String) model.getValueAt(row, 1),
                (String) model.getValueAt(row, 2),
                (String) model.getValueAt(row, 3),
                model.getValueAt(row, 4).toString(),
                (String) model.getValueAt(row, 5),
                (String) model.getValueAt(row, 6)
        );

        Policy edited = showInputDialog(p);
        if (edited != null) {
            try (Connection conn = DBHelper.connect()) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE policies SET title=?, institution=?, category=?, date=?, status=?, description=? WHERE id=?");
                stmt.setString(1, edited.getTitle());
                stmt.setString(2, edited.getInstitution());
                stmt.setString(3, edited.getCategory());
                stmt.setString(4, edited.getDate());
                stmt.setString(5, edited.getStatus());
                stmt.setString(6, edited.getDescription());
                stmt.setInt(7, edited.getId());
                stmt.executeUpdate();
                loadPolicies();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBHelper.connect()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM policies WHERE id=?");
                stmt.setInt(1, id);
                stmt.executeUpdate();
                loadPolicies();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void simpanData() {
        try {
            File file = new File("data_kebijakan.dat");
            List<Policy> data = getTableData();
            PolicySerializer.save(data, file);
            JOptionPane.showMessageDialog(this, "Disimpan ke file terenkripsi!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void muatData() {
        try {
            File file = new File("data_kebijakan.dat");
            List<Policy> data = PolicySerializer.load(file);
            model.setRowCount(0);
            for (Policy p : data) {
                model.addRow(new Object[]{
                    p.getId(), p.getTitle(), p.getInstitution(),
                    p.getCategory(), p.getDate(), p.getStatus(), p.getDescription()
                });
            }
            JOptionPane.showMessageDialog(this, "Berhasil dimuat dari file terenkripsi!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Policy> getTableData() {
        java.util.List<Policy> list = new java.util.ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            list.add(new Policy(
                    (int) model.getValueAt(i, 0),
                    (String) model.getValueAt(i, 1),
                    (String) model.getValueAt(i, 2),
                    (String) model.getValueAt(i, 3),
                    model.getValueAt(i, 4).toString(),
                    (String) model.getValueAt(i, 5),
                    (String) model.getValueAt(i, 6)
            ));
        }
        return list;
    }

    private void loadAsync() {
        new Thread(() -> {
            try {
                Thread.sleep(500); // simulasi loading
                SwingUtilities.invokeLater(this::loadPolicies);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public void updateLanguage() {
        setTitle("📰 " + Lang.get("title") + " - " + Lang.get("institution"));
        String[] columns = {"ID", Lang.get("title"), Lang.get("institution"), Lang.get("category"), Lang.get("date"), Lang.get("status"), Lang.get("description")};
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setHeaderValue(columns[i]);
        }
        table.getTableHeader().repaint();
        btnAdd.setText(Lang.get("add"));
        btnEdit.setText(Lang.get("edit"));
        btnDelete.setText(Lang.get("delete"));
        btnSave.setText(Lang.get("save"));
        btnLoad.setText("📂 " + Lang.get("open"));
        btnReload.setText("🔄 " + Lang.get("reload"));
        btnLang.setText("🌐 " + Lang.get("change_language"));
    }

    private void gantiBahasa() {
        String[] pilihan = {"id", "en"};
        String selected = (String) JOptionPane.showInputDialog(
                this,
                Lang.get("change_language_prompt"),
                Lang.get("change_language"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                pilihan,
                LanguageConfig.loadLanguage()
        );

        if (selected != null) {
            LanguageConfig.saveLanguage(selected);
            Lang.setLocale(selected);
            updateLanguage();
            JOptionPane.showMessageDialog(this, Lang.get("language_changed"), Lang.get("info"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Policy showInputDialog(Policy p) {
        JTextField tfTitle = new JTextField(p != null ? p.getTitle() : "");
        JTextField tfInst = new JTextField(p != null ? p.getInstitution() : "");
        JTextField tfCat = new JTextField(p != null ? p.getCategory() : "");
        JTextField tfDate = new JTextField(p != null ? p.getDate() : "YYYY-MM-DD");
        JTextField tfStat = new JTextField(p != null ? p.getStatus() : "");
        JTextArea taDesc = new JTextArea(p != null ? p.getDescription() : "", 3, 20);
        taDesc.setLineWrap(true);
        taDesc.setWrapStyleWord(true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Margin antar komponen
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(Lang.get("title")), gbc);
        gbc.gridx = 1;
        panel.add(tfTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel(Lang.get("institution")), gbc);
        gbc.gridx = 1;
        panel.add(tfInst, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Kategori"), gbc);
        gbc.gridx = 1;
        panel.add(tfCat, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Tanggal (YYYY-MM-DD)"), gbc);
        gbc.gridx = 1;
        panel.add(tfDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Status"), gbc);
        gbc.gridx = 1;
        panel.add(tfStat, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Deskripsi"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(taDesc), gbc);

// Show dialog
        int res = JOptionPane.showConfirmDialog(this, panel, Lang.get("form_title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            return new Policy(
                    p != null ? p.getId() : 0,
                    tfTitle.getText(), tfInst.getText(), tfCat.getText(),
                    tfDate.getText(), tfStat.getText(), taDesc.getText()
            );
        }
        return null;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PolicyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PolicyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PolicyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PolicyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PolicyFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
