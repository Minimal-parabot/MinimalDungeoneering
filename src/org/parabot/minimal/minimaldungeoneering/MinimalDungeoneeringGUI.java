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

    private JPanel armorPanel = null;
    private JPanel buttonPanel = null;
    private JPanel prayerPanel = null;

    private final Image DEFAULT_HEADWEAR = MinimalDungeoneering.getImage("http://i.imgur.com/YmLYXpu.png");
    private final Image DEFAULT_BODY = MinimalDungeoneering.getImage("http://i.imgur.com/vxjq2nM.png");
    private final Image DEFAULT_WEAPON = MinimalDungeoneering.getImage("http://i.imgur.com/SrniuOc.png");
    private final Image DEFAULT_LEGWEAR = MinimalDungeoneering.getImage("http://i.imgur.com/59ny8AG.png");

    private JTextField headwearField;
    private JTextField bodyField;
    private JTextField weaponField;
    private JTextField legwearField;

    private JLabel headwearLabel;
    private JLabel bodyLabel;
    private JLabel weaponLabel;
    private JLabel legwearLabel;

    private final int COL_SIZE = 4;

    private int headwearId;
    private int bodyId;
    private int weaponId;
    private int legwearId;

    private boolean normalPrayers = false;

    private Preferences prefs = Preferences.userNodeForPackage(MinimalDungeoneering.class);
    
    public MinimalDungeoneeringGUI()
    {
        setLayout(new FlowLayout());
        setVisible(true);
        setSize(150, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        armorPanel = new ArmorPanel();
        buttonPanel = new ButtonPanel();
        prayerPanel = new PrayerPanel();
        
        add(armorPanel);
        add(prayerPanel);
        add(buttonPanel);
    }

    public class PrayerPanel extends JPanel
    {
        public PrayerPanel()
        {
            final JCheckBox prayers = new JCheckBox("Ancient Curses");
            prayers.setFont(FONT);
            prayers.setSelected(true);

            prayers.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (prayers.isSelected())
                    {
                        normalPrayers = false;

                        prayers.setText("Ancient Curses");
                    }
                    else
                    {
                        normalPrayers = true;

                        prayers.setText("Normal");
                    }
                }
            });

            add(prayers);
        }
    }

    public class ArmorPanel extends JPanel
    {
        public ArmorPanel()
        {
            setLayout(new GridBagLayout());

            // Gets the loaded or default values from our preferences
            headwearId = prefs.getInt("Headwear", -1);
            bodyId = prefs.getInt("Body", -1);
            legwearId = prefs.getInt("Legwear", -1);
            weaponId = prefs.getInt("Weapon", -1);

            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;

            // Sets up the text for the field
            String fieldText;

            // Headwear Field
            fieldText = ((headwearId != -1) ? Integer.toString(headwearId) : "0");
            headwearField = createTextField(fieldText);
            c.gridx = 4;
            c.gridy = 0;
            add(headwearField, c);

            // Body Field
            fieldText = ((bodyId != -1) ? Integer.toString(bodyId) : "0");
            bodyField = createTextField(fieldText);
            c.gridx = 4;
            c.gridy = 3;
            c.insets = new Insets(10, 0, 0, 0);
            add(bodyField, c);

            // Weapon Field
            fieldText = ((weaponId != -1) ? Integer.toString(weaponId) : "0");
            weaponField = createTextField(fieldText);
            c.gridx = 2;
            c.gridy = 3;
            c.insets = new Insets(10, 0, 0, 10);
            add(weaponField, c);

            // Legwear field
            fieldText = ((legwearId != -1) ? Integer.toString(legwearId) : "0");
            legwearField = createTextField(fieldText);
            c.insets = new Insets(10, 0, 0, 0);
            c.gridx = 4;
            c.gridy = 5;
            add(legwearField, c);

            // Default headwear
            headwearLabel = new JLabel(new ImageIcon(DEFAULT_HEADWEAR));
            for (Equipment eq : Equipment.values())
            {
                if (Integer.parseInt(headwearField.getText()) == eq.getId())
                {
                    headwearLabel.setIcon(new ImageIcon(eq.getImage()));

                    break;
                }
            }
            c.insets = new Insets(0, 0, 0, 0);
            c.gridx = 4;
            c.gridy = 1;
            add(headwearLabel, c);

            // Default body
            bodyLabel = new JLabel(new ImageIcon(DEFAULT_BODY));
            for (Equipment eq : Equipment.values())
            {
                if (Integer.parseInt(bodyField.getText()) == eq.getId())
                {
                    bodyLabel.setIcon(new ImageIcon(eq.getImage()));

                    break;
                }
            }
            c.gridx = 4;
            c.gridy = 4;
            c.insets = new Insets(0, 0, 0, 0);
            add(bodyLabel, c);

            // Default weapon
            weaponLabel = new JLabel(new ImageIcon(DEFAULT_WEAPON));
            for (Equipment eq : Equipment.values())
            {
                if (Integer.parseInt(weaponField.getText()) == eq.getId())
                {
                    weaponLabel.setIcon(new ImageIcon(eq.getImage()));

                    break;
                }
            }
            c.gridx = 2;
            c.gridy = 4;
            c.insets = new Insets(0, 0, 0, 10);
            add(weaponLabel, c);

            // Default legwear
            legwearLabel = new JLabel(new ImageIcon(DEFAULT_LEGWEAR));
            for (Equipment eq : Equipment.values())
            {
                if (Integer.parseInt(legwearField.getText()) == eq.getId())
                {
                    legwearLabel.setIcon(new ImageIcon(eq.getImage()));

                    break;
                }
            }
            c.insets = new Insets(0, 0, 0, 0);
            c.gridx = 4;
            c.gridy = 6;
            add(legwearLabel, c);
        }

        public JTextField createTextField(String str)
        {
            JTextField jTextField = new JTextField(COL_SIZE);

            jTextField.setFont(FONT);
            jTextField.setText(str);
            jTextField.getDocument().addDocumentListener(new Listener());

            return jTextField;
        }
    }

    public class Listener implements DocumentListener
    {
        @Override
        public void insertUpdate(DocumentEvent e)
        {
            try
            {
                if (e.getDocument() == headwearField.getDocument())
                {
                    for (Equipment eq : Equipment.values())
                    {
                        if (Integer.parseInt(headwearField.getText()) == eq.getId())
                        {
                            headwearLabel.setIcon(new ImageIcon(eq.getImage()));

                            break;
                        }
                        else
                            headwearLabel.setIcon(new ImageIcon(DEFAULT_HEADWEAR));
                    }
                }
                else if (e.getDocument() == bodyField.getDocument())
                {
                    for (Equipment eq : Equipment.values())
                    {
                        if (Integer.parseInt(bodyField.getText()) == eq.getId())
                        {
                            bodyLabel.setIcon(new ImageIcon(eq.getImage()));

                            break;
                        }
                        else
                            bodyLabel.setIcon(new ImageIcon(DEFAULT_BODY));
                    }
                }
                else if (e.getDocument() == weaponField.getDocument())
                {
                    for (Equipment eq : Equipment.values())
                    {
                        if (Integer.parseInt(weaponField.getText()) == eq.getId())
                        {
                            weaponLabel.setIcon(new ImageIcon(eq.getImage()));

                            break;
                        }
                        else
                            weaponLabel.setIcon(new ImageIcon(DEFAULT_WEAPON));
                    }
                }
                else if (e.getDocument() == legwearField.getDocument())
                {
                    for (Equipment eq : Equipment.values())
                    {
                        if (Integer.parseInt(legwearField.getText()) == eq.getId())
                        {
                            legwearLabel.setIcon(new ImageIcon(eq.getImage()));

                            break;
                        }
                        else
                            legwearLabel.setIcon(new ImageIcon(DEFAULT_LEGWEAR));
                    }
                }
            }
            catch (Exception ex)
            {

            }
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            try
            {
                if (e.getDocument() == headwearField.getDocument())
                {
                    for (Equipment eq : Equipment.values())
                    {
                        if (Integer.parseInt(headwearField.getText()) == eq.getId())
                        {
                            headwearLabel.setIcon(new ImageIcon(eq.getImage()));

                            break;
                        }
                        else
                            headwearLabel.setIcon(new ImageIcon(DEFAULT_HEADWEAR));
                    }
                }
                else if (e.getDocument() == bodyField.getDocument())
                {
                    for (Equipment eq : Equipment.values())
                    {
                        if (Integer.parseInt(bodyField.getText()) == eq.getId())
                        {
                            bodyLabel.setIcon(new ImageIcon(eq.getImage()));

                            break;
                        }
                        else
                            bodyLabel.setIcon(new ImageIcon(DEFAULT_BODY));
                    }
                }
                else if (e.getDocument() == weaponField.getDocument())
                {
                    for (Equipment eq : Equipment.values())
                    {
                        if (Integer.parseInt(weaponField.getText()) == eq.getId())
                        {
                            weaponLabel.setIcon(new ImageIcon(eq.getImage()));

                            break;
                        }
                        else
                            weaponLabel.setIcon(new ImageIcon(DEFAULT_WEAPON));
                    }
                }
                else if (e.getDocument() == legwearField.getDocument())
                {
                    for (Equipment eq : Equipment.values())
                    {
                        if (Integer.parseInt(legwearField.getText()) == eq.getId())
                        {
                            legwearLabel.setIcon(new ImageIcon(eq.getImage()));

                            break;
                        }
                        else
                            legwearLabel.setIcon(new ImageIcon(DEFAULT_LEGWEAR));
                    }
                }
            }
            catch (Exception ex)
            {

            }
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
            // This never even activates
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
            // e.printStackTrace();
        }
    }
    
    public int[] getArmor()
    {
        return new int[] { headwearId, bodyId, legwearId, weaponId };
    }

    public boolean getPrayer()
    {
        return normalPrayers;
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                MinimalDungeoneeringGUI gui = new MinimalDungeoneeringGUI();
            }
        });
    }
}