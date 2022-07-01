import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class OptionPanel extends JFrame {
    private JButton selectButton;
    private JTextField pathField;
    private JButton ButtonUpLeft;
    private JButton ButtonLEFT;
    private JButton ButtonDownLeft;
    private JButton ButtonRIGHT;
    private JButton ButtonUP;
    private JButton ButtonUpRight;
    private JButton ButtonDOWN;
    private JButton ButtonDownRight;
    private JButton saveButton;
    private JSlider slider;
    private JPanel mainPanel;
    private JList<Selector> selectorList;
    private JButton saveAsButton;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSpinner windowHeightSpinner;
    private JCheckBox originalSizeCheckBox;
    private JSlider slider2;
    private JCheckBox invertMaskCheckBox;
    private JCheckBox showMaskCheckBox;
    PixelSorter pixelSorter;
    final private MyPixelSortApp sketchApplet;
    private boolean updateInProgress = false;

    OptionPanel(MyPixelSortApp applet, PixelSorter sorter) {
        this.sketchApplet = applet;
        this.pixelSorter = sorter;

        $$$setupUI$$$();
        setTitle("MMM OPTIONS");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setContentPane(mainPanel);
        setLocation(0, 10);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sketchApplet.exit();
                dispose();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                super.windowIconified(e);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                super.windowDeiconified(e);
            }
        });
        //================COOL YEAH====================

        //Surface Size Changer SpinnerField
        if (!sketchApplet.surfaceSizeIsOneToOne())
            windowHeightSpinner.setModel(new SpinnerNumberModel(sketchApplet.getSurfaceHeight(), 128, 3000, 50));
        windowHeightSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("windowchangespinner changed lmao");
                int val = (int) windowHeightSpinner.getValue();
                if (!sketchApplet.resizeSurfaceToHeight(val))
                    windowHeightSpinner.setValue(sketchApplet.getSurfaceHeight());
            }
        });

        //Windowsize checkbox
        originalSizeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalSizeCheckBox.isSelected()) {
                    windowHeightSpinner.setEnabled(false);
                    sketchApplet.setSurfaceSizeOneToOne(true);
                } else {
                    sketchApplet.setSurfaceSizeOneToOne(false);
                    windowHeightSpinner.setEnabled(true);
                }
            }
        });

        //TODO: Button Group that shows selected
        ButtonUP.addActionListener(new DirectionListener(xDirection.None, yDirection.Up));
        ButtonUpRight.addActionListener(new DirectionListener(xDirection.Right, yDirection.Up));
        ButtonRIGHT.addActionListener(new DirectionListener(xDirection.Right, yDirection.None));
        ButtonDownRight.addActionListener(new DirectionListener(xDirection.Right, yDirection.Down));
        ButtonDOWN.addActionListener(new DirectionListener(xDirection.None, yDirection.Down));
        ButtonDownLeft.addActionListener(new DirectionListener(xDirection.Left, yDirection.Down));
        ButtonLEFT.addActionListener(new DirectionListener(xDirection.Left, yDirection.None));
        ButtonUpLeft.addActionListener(new DirectionListener(xDirection.Left, yDirection.Up));

        //Save buttons
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sketchApplet.saveImg();
            }
        });

        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Save as
            }
        });


        selectorList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateInProgress = true;
                pixelSorter.setSelector(selectorList.getModel().getElementAt(selectorList.getSelectedIndex()));
                invertMaskCheckBox.setSelected(pixelSorter.getSelector().isInverted());
                slider.getModel().setMinimum(pixelSorter.getSelector().getMin());
                slider.getModel().setMaximum(pixelSorter.getSelector().getMax());
                slider2.setMinimum(pixelSorter.getSelector().getMin());
                slider2.setMaximum(pixelSorter.getSelector().getMax());
                updateInProgress = false;
                updateSelectorValueStart(pixelSorter.getSelector().getStart());
                updateSelectorValueEnd(pixelSorter.getSelector().getEnd());
                slider.setEnabled(true);
                spinner1.setEnabled(true);
                invertMaskCheckBox.setEnabled(true);
                if (pixelSorter.getSelector() instanceof ThresholdSelector) {
                    slider.setEnabled(false);
                    spinner1.setEnabled(false);
                }
                if (pixelSorter.getSelector() instanceof Selectors.RandomSelector)
                    invertMaskCheckBox.setEnabled(false);
            }
        });


        showMaskCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sketchApplet.setShowMask(showMaskCheckBox.isSelected());
            }
        });

        invertMaskCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pixelSorter.getSelector().setInverted(invertMaskCheckBox.isSelected());
                ((myListModel) selectorList.getModel()).fireContentsChanged(0, selectorList.getModel().getSize());
            }
        });


        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updateInProgress) {
                    updateInProgress = true;
                    updateSelectorValueStart(slider.getValue());
                    updateInProgress = false;
                }
            }
        });

        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updateInProgress) {
                    updateInProgress = true;
                    updateSelectorValueEnd(slider2.getValue());
                    updateInProgress = false;
                }
            }
        });

        spinner1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updateInProgress) {
                    updateInProgress = true;
                    updateSelectorValueStart((Integer) spinner1.getValue());
                    updateInProgress = false;
                }
            }
        });


        spinner2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updateInProgress) {     //so if something else changes values, this wont fire
                    updateInProgress = true;        //Hello, im the one updating :>
                    updateSelectorValueEnd((Integer) spinner2.getValue());
                    updateInProgress = false;
                }
            }
        });

        pathField.setEditable(false);
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });

        //Set values on start
        selectorList.setSelectedIndex(2); //Set Default Selector (Hue)
        updateValueUI();

        //END OF YEAH
        pack();
        setVisible(true);

    }


    private void updateSelectorValueStart(int newValue) {
        //check if this slider or spinner would go above the other spinner
        if (newValue >= pixelSorter.getSelector().getEnd()) {
            //if so, push the others one up
            updateSelectorValueEnd(newValue + 0);
            newValue--; // uncommenting this and removing the +1 above makes it so
/*          the value that would be pushed to be dominant (spinners cant push, sliders can't push the other below 0)
            If they CAN push, it stackoverflows because the spinner can overflow the integers */
        }
        pixelSorter.getSelector().setStart(newValue);
        //If something changed
        spinner2.setValue(pixelSorter.getSelector().getEnd());
        slider2.setValue(pixelSorter.getSelector().getEnd());
        //update these two (one of these called this function :>)
        slider.setValue(newValue);
        spinner1.setValue(newValue);
    }

    //TODO: slider model!!
    //sets slider2 and spinner2
    private void updateSelectorValueEnd(int newValue) {
        //check if this slider2 or spinner2 would go below the other sliders value
        if (newValue <= pixelSorter.getSelector().getStart()) {
            //if so, push the others below
            updateSelectorValueStart(newValue - 0);
            newValue++;
        }
        pixelSorter.getSelector().setEnd(newValue);
        //If something changed
        spinner1.setValue(pixelSorter.getSelector().getStart());
        slider.setValue(pixelSorter.getSelector().getStart());
        //update these two (one of these called this function :>)
        slider2.setValue(newValue);
        spinner2.setValue(newValue);
    }

    public void updateValueUI() {
        slider.setValue(pixelSorter.getSelector().getStart());
        slider2.setValue(pixelSorter.getSelector().getEnd());
        spinner1.setValue(pixelSorter.getSelector().getStart());
        spinner2.setValue(pixelSorter.getSelector().getEnd());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        selectorList = new JList<>(new myListModel());
        selectorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectorList.setCellRenderer(new myListCellRenderer());
    }

    void chooseFile() {
        JFileChooser fc = new JFileChooser();
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "tga", "gif"));
        fc.setCurrentDirectory(new File("./data"));
        fc.setFileHidingEnabled(true);
        int returnVal = fc.showOpenDialog(OptionPanel.this);

        try {
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                String path = f.getPath();
                System.out.println(path);
                sketchApplet.loadAndSetImg(path);
                pathField.setText(path);
                pathField.setSelectionEnd(path.length() - 1);
            }
        } catch (Exception ex) {
            System.out.println("Error :(\n" + ex);
        }
    }


    class DirectionListener implements ActionListener {
        xDirection row;
        yDirection column;

        DirectionListener(xDirection row, yDirection column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pixelSorter.setXDirection(row);
            pixelSorter.setYDirection(column);
        }
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(30, 20));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        mainPanel.add(panel1, BorderLayout.SOUTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        mainPanel.add(panel2, BorderLayout.CENTER);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 20), -1, -1));
        panel3.setEnabled(true);
        panel2.add(panel3, BorderLayout.CENTER);
        selectorList.setSelectionMode(0);
        panel3.add(selectorList, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 50), null, 0, false));
        slider = new JSlider();
        slider.setMaximum(255);
        slider.setPaintLabels(false);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);
        slider.setValue(50);
        panel3.add(slider, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinner1 = new JSpinner();
        panel3.add(spinner1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        spinner2 = new JSpinner();
        panel3.add(spinner2, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 20));
        mainPanel.add(panel4, BorderLayout.WEST);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 20, 0, 0), -1, -1, true, true));
        panel4.add(panel5, BorderLayout.NORTH);
        ButtonUpLeft = new JButton();
        ButtonUpLeft.setText("↖");
        panel5.add(ButtonUpLeft, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 50), null, null, 0, false));
        ButtonLEFT = new JButton();
        ButtonLEFT.setText("←");
        panel5.add(ButtonLEFT, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 50), null, null, 0, false));
        ButtonDownLeft = new JButton();
        ButtonDownLeft.setText("↙");
        panel5.add(ButtonDownLeft, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 50), null, null, 0, false));
        ButtonUP = new JButton();
        ButtonUP.setText("↑");
        panel5.add(ButtonUP, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 50), null, null, 0, false));
        ButtonUpRight = new JButton();
        ButtonUpRight.setText("↗");
        panel5.add(ButtonUpRight, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 50), new Dimension(50, 50), null, 0, false));
        ButtonRIGHT = new JButton();
        ButtonRIGHT.setText("→");
        panel5.add(ButtonRIGHT, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 50), null, null, 0, false));
        ButtonDOWN = new JButton();
        ButtonDOWN.setText("↓");
        panel5.add(ButtonDOWN, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 50), null, null, 0, false));
        ButtonDownRight = new JButton();
        ButtonDownRight.setText("↘");
        panel5.add(ButtonDownRight, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, 50), null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 20, 0, 0), -1, -1));
        panel4.add(panel6, BorderLayout.SOUTH);
        saveButton = new JButton();
        saveButton.setText("Save");
        panel6.add(saveButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveAsButton = new JButton();
        saveAsButton.setText("Save as");
        panel6.add(saveAsButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 5, new Insets(20, 20, 0, 20), -1, -1));
        mainPanel.add(panel7, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        label1.setText("Choose File:");
        panel7.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        selectButton = new JButton();
        selectButton.setText("Select...");
        panel7.add(selectButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pathField = new JTextField();
        pathField.setColumns(20);
        panel7.add(pathField, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Window Height");
        panel7.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        windowHeightSpinner = new JSpinner();
        panel7.add(windowHeightSpinner, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(50, -1), null, null, 0, false));
        originalSizeCheckBox = new JCheckBox();
        originalSizeCheckBox.setText("Original Size");
        panel7.add(originalSizeCheckBox, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
