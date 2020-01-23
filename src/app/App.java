package app;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class App {

    static JFrame frame;

    public static void main(String[] args) {
        createWindow();
    }

    private static void uiManager() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void createWindow() {

        uiManager();

        // frame to contains GUI elements 
        frame = new JFrame("CSV Converter");

        java.net.URL iconURL = ClassLoader.getSystemResource("resources/nasdoIcon.png");

        ImageIcon icon = new ImageIcon(iconURL);
        frame.setIconImage(icon.getImage());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI(frame);

        // set the size of the frame 
        frame.setSize(400, 150);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

    }

    private static void createUI(JFrame frame) {
        // make a panel to add the buttons and labels 
        JPanel panel = new JPanel();

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;

//        JLabel label = new JLabel("Please select a CSV file to be converted"); // English
        JLabel label = new JLabel("Bitte wählen Sie eine zu konvertierende CSV-Datei aus");
        label.setFont(new Font("Default", Font.BOLD, 13));

        panel.add(label, gbc);

        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttons = new JPanel(new GridBagLayout());

        // button to open open dialog 
        JButton chooseFileButton = new JButton("Datei wählen");

        buttons.add(chooseFileButton, gbc);

        chooseFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String com = e.getActionCommand();

                if (com.equals("Datei wählen")) {

                    // create an object of JFileChooser class 
                    JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                    // resctrict the user to selec files of all types 
                    j.setAcceptAllFileFilterUsed(false);
                    // set a title for the dialog 
                    j.setDialogTitle("Wählen Sie eine Datei aus");
                    // only allow files of .csv extension 
                    FileNameExtensionFilter restrict = new FileNameExtensionFilter(".csv", "csv");
                    j.addChoosableFileFilter(restrict);

                    // invoke the showsOpenDialog function to show the save dialog 
                    int chooseFile = j.showOpenDialog(frame);

                    // if the user selects a file 
                    if (chooseFile == JFileChooser.APPROVE_OPTION) {
                        // set the label to the path of the selected file 
                        String csvAbsPath = j.getSelectedFile().getAbsolutePath();

                        ArrayList returnedCSVList = Converter.csvReader(csvAbsPath);

                        createSaveUI(csvAbsPath, returnedCSVList);

                    } else {
                        label.setText("Bitte wählen Sie eine zu konvertierende CSV-Datei aus");
                    }
                }
            }
        });

        gbc.weighty = 1;
        panel.add(buttons, gbc);

        frame.add(panel);

        // set the frame's visibility 
        frame.setVisible(true);
    }

    private static void createSaveUI(String csvPath, ArrayList returnedCSVList) {
        JDialog dialogFrame = new JDialog(frame, "Als Text speichern");

        // setsize of dialog 
        dialogFrame.setSize(200, 200);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dialogFrame.setLocation(dim.width / 2 - dialogFrame.getSize().width / 2, dim.height / 2 - dialogFrame.getSize().height / 2);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dialogPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;

        JLabel dialogLabel = new JLabel("Import erfolgreich!");
        dialogLabel.setFont(new Font("Default", Font.BOLD, 13));
        dialogPanel.add(dialogLabel, gbc);

        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttons = new JPanel(new GridBagLayout());

        JButton saveText = new JButton("OK");
        buttons.add(saveText, gbc);

        saveText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser saveChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                saveChooser.setAcceptAllFileFilterUsed(false);

                // only allow files of .csv extension 
                FileNameExtensionFilter restrict = new FileNameExtensionFilter(".txt", "txt");
                saveChooser.addChoosableFileFilter(restrict);

//                String textPath = saveChooser.getSelectedFile();
                int retrieval = saveChooser.showSaveDialog(frame);

                if (retrieval == JFileChooser.APPROVE_OPTION) {

                    Converter.csvToTextConverter(csvPath, saveChooser.getSelectedFile(), returnedCSVList);

                    dialogFrame.dispose();

                }
            }
        });

        gbc.weighty = 1;
        dialogPanel.add(buttons, gbc);

        dialogFrame.add(dialogPanel);

        // set visibility of dialog 
        dialogFrame.setVisible(true);

    }
}
