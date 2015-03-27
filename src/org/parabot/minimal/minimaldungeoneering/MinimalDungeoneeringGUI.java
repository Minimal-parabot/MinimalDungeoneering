package org.parabot.minimal.minimaldungeoneering;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

public class MinimalDungeoneeringGUI extends JFrame
{
    private final Font FONT = new Font("Helvetica", Font.PLAIN, 14);

    private final Image DEFAULT_HEADWEAR = MinimalDungeoneering.getImage("http://i.imgur.com/YmLYXpu.png");
    private final Image DEFAULT_BODY = MinimalDungeoneering.getImage("http://i.imgur.com/vxjq2nM.png");
    private final Image DEFAULT_WEAPON = MinimalDungeoneering.getImage("http://i.imgur.com/SrniuOc.png");
    private final Image DEFAULT_LEGWEAR = MinimalDungeoneering.getImage("http://i.imgur.com/59ny8AG.png");

    private JTextField headwearField;
    private JTextField bodyField;
    private JTextField weaponField;
    private JTextField legwearField;

    private int headwearId;
    private int bodyId;
    private int weaponId;
    private int legwearId;

    private Preferences prefs = Preferences.userNodeForPackage(MinimalDungeoneering.class);
    
    public MinimalDungeoneeringGUI()
    {
        setLayout(new GridBagLayout());
        setVisible(true);
        setSize(200, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();
        
        JPanel armorPanel = new ArmorPanel();
        c.gridx = 0;
        c.gridy = 0;
        add(armorPanel, c);

        JPanel buttonPanel = new ButtonPanel();
        c.gridx = 0;
        c.gridy = 1;
        add(buttonPanel, c);
    }

    public class ArmorPanel extends JPanel
    {
        public ArmorPanel()
        {
            setLayout(new GridBagLayout());

            JLabel headwearLabel;
            JLabel bodyLabel;
            JLabel weaponLabel;
            JLabel legwearLabel;

            // Gets the loaded or default values from our preferences
            headwearId = prefs.getInt("Headwear", -1);
            bodyId = prefs.getInt("Body", -1);
            legwearId = prefs.getInt("Legwear", -1);
            weaponId = prefs.getInt("Weapon", -1);

            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;

            // Sets up the text for the field
            String fieldText;

            headwearField = new JTextField((headwearId != -1) ? Integer.toString(headwearId) : "0", 4);
            headwearField.setFont(FONT);
            c.gridx = 4;
            c.gridy = 0;
            add(headwearField, c);

            // Body Field
            bodyField = new JTextField((bodyId != -1) ? Integer.toString(bodyId) : "0", 4);
            bodyField.setFont(FONT);
            c.gridx = 4;
            c.gridy = 3;
            c.insets = new Insets(10, 0, 0, 0);
            add(bodyField, c);

            // Weapon Field
            weaponField = new JTextField((weaponId != -1) ? Integer.toString(weaponId) : "0", 4);
            weaponField.setFont(FONT);
            c.gridx = 2;
            c.gridy = 3;
            c.insets = new Insets(10, 0, 0, 10);
            add(weaponField, c);

            // Legwear field
            legwearField = new JTextField((legwearId != -1) ? Integer.toString(legwearId) : "0", 4);
            legwearField.setFont(FONT);
            c.insets = new Insets(10, 0, 0, 0);
            c.gridx = 4;
            c.gridy = 5;
            add(legwearField, c);

            // Default headwear
            headwearLabel = new JLabel(new ImageIcon(DEFAULT_HEADWEAR));
            c.insets = new Insets(0, 0, 0, 0);
            c.gridx = 4;
            c.gridy = 1;
            add(headwearLabel, c);

            // Default body
            bodyLabel = new JLabel(new ImageIcon(DEFAULT_BODY));
            c.gridx = 4;
            c.gridy = 4;
            c.insets = new Insets(0, 0, 0, 0);
            add(bodyLabel, c);

            // Default weapon
            weaponLabel = new JLabel(new ImageIcon(DEFAULT_WEAPON));
            c.gridx = 2;
            c.gridy = 4;
            c.insets = new Insets(0, 0, 0, 10);
            add(weaponLabel, c);

            // Default legwear
            legwearLabel = new JLabel(new ImageIcon(DEFAULT_LEGWEAR));
            c.insets = new Insets(0, 0, 0, 0);
            c.gridx = 4;
            c.gridy = 6;
            add(legwearLabel, c);
        }
    }
    
    public class ButtonPanel extends JPanel
    {
        public ButtonPanel()
        {
            JButton startButton = new JButton("Start");
            startButton.setFont(new Font("Helvetica", Font.BOLD, 20));
            startButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    updateArmorValues();

                    dispose();
                }
            });

            add(startButton);
        }
    }
    
    private void updateArmorValues()
    {
        try
        {
            headwearId = Integer.parseInt(headwearField.getText());
            bodyId = Integer.parseInt(bodyField.getText());
            legwearId = Integer.parseInt(legwearField.getText());
            weaponId = Integer.parseInt(weaponField.getText());

            prefs.putInt("Headwear", headwearId);
            prefs.putInt("Body", bodyId);
            prefs.putInt("Legwear", legwearId);
            prefs.putInt("Weapon", weaponId);
        }
        catch (NumberFormatException e)
        {
             e.printStackTrace();
        }
    }
    
    public int[] getArmor()
    {
        return new int[] { headwearId, bodyId, legwearId, weaponId };
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                MinimalDungeoneeringGUI gui = new MinimalDungeoneeringGUI();
                gui.setVisible(true);
            }
        });
    }
}