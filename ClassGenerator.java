package kelmore5.java.personal.class_generator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * <pre class="doc_header">
 * <p>
 * </pre>
 *
 * @author kelmore5
 * @custom.date 3/19/17
 */
public class ClassGenerator extends JFrame implements Runnable
{
    /**
     *
     */
    private static final long serialVersionUID = -1817553856496126764L;

    private JPanel mainPanel;
    private JTextField mainText[][];
    private String[] blocks = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
    private File currentFile;

    private ClassGenerator()
    {
        mainPanel = new JPanel(new GridLayout(14, 8));

        currentFile = null;

        mainText = new JTextField[14][8];

        createMainContent();

        createMenu();

        getContentPane().add(mainPanel);
    }

    private boolean selectFile()
    {
        JFileChooser jfc = new JFileChooser();
        int returnValue = jfc.showOpenDialog(ClassGenerator.this);
        if(returnValue == JFileChooser.APPROVE_OPTION)
        {
            currentFile = jfc.getSelectedFile();
        }
        return returnValue == JFileChooser.APPROVE_OPTION;
    }

    private void writeToCurrentFile() throws IOException
    {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentFile));
        oos.writeObject(mainText);
        oos.close();
    }

    private void readFromCurrentFile() throws IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(currentFile));

        mainText = (JTextField[][]) ois.readObject();
        mainPanel.removeAll();
        mainPanel.setLayout(new GridLayout(14, 8));
        for(JTextField[] rows: mainText)
        {
            for(JTextField text: rows)
            {
                mainPanel.add(text);
            }
        }

        mainPanel.validate();
        mainPanel.repaint();

        ois.close();
    }

    private void createMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JButton newClass = new JButton("Pick Blocks");
        menuBar.add(newClass);

        newClass.addActionListener(e -> {
            CheckFrame cf = new CheckFrame();
            javax.swing.SwingUtilities.invokeLater(cf);
        });

        JButton save = new JButton("Save Schedule");
        menuBar.add(save);

        save.addActionListener(e -> {
            if(currentFile == null)
            {
                if(!selectFile()) {
                    return;
                }
            }
            try
            {
                writeToCurrentFile();
            }
            catch(IOException ex)
            {
                JOptionPane.showMessageDialog(null, "The schedule could not be saved.", "Unable to Save File", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton open = new JButton("Open Saved Schedule");
        menuBar.add(open);

        open.addActionListener(e -> {
            if(selectFile())
            {
                try
                {
                    readFromCurrentFile();
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "The schedule could not be opened. Was it saved from this program?", "Unable to Open File", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void createMainContent()
    {
        String mainColumns[] = {"", "Time", "Monday", "Time", "Tuesday", "Wednesday", "Thursday", "Friday" };

        for(int k = 0; k < 8; k++)
        {
            mainText[0][k] = new JTextField(mainColumns[k]);
            mainText[0][k].setEditable(false);
            mainText[0][k].setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
        }

        String mainData[][] =
                {
                        {"", "8:00 to 8:50", "A1", "8:00 to 8:50", "C2", "B3", "D4", "B5" },
                        {"", "8:55 to 9:45", "B1", "8:55 to 9:45", "D2", "A3", "C4", "A5"},
                        {"", "9:50 to 10:40", "C1", "9:50 to 10:40", "E2", "G3", "A4", "C5"},
                        {"", "10:45 to 11:35", "D1", "10:45 to 11:35", "A2", "C3", "E4", "G5"},
                        {"", "", "", "11:35 to 12:15", "A2 Lab", "C3 Lab", "E4 Lab", "G5 Lab"},
                        {"Daytime", "11:35 to 1:20", "Lunch/Meeting", "12:15 to 12:55", "Lunch", "Lunch", "Lunch", "Lunch"},
                        {"", "", "", "12:55 to 1:35", "B2 Lab", "D3 Lab", "F4 Lab", "F5 Lab"},
                        {"", "1:25 to 2:15", "E1", "1:35 to 2:25", "B2", "D3", "F4", "F5"},
                        {"", "2:20 to 3:10", "F1", "2:30 to 3:20", "G2", "F3", "G4", "E5"},
                        {"", "3:15 to 4:05", "G1", "3:25 to 4:15", "F2", "E3", "B4", "D5"},
                        {"", "", "", "", "", "", "", ""},
                        {"Evening", "6:15 to 7:55", "H1", "6:15 to 7:55", "H2", "H3", "H4", ""},
                        {"", "8:35 to 10:05", "I1", "8:35 to 10:05", "I2", "I3", "I4", ""}
                };

        for(int k = 0; k < 8; k++)
        {
            for(int i = 1; i < 14; i++)
            {
                mainText[i][k] = new JTextField(mainData[i-1][k]);
                mainText[i][k].setEditable(false);
                mainText[i][k].setBackground(Color.white);
                mainText[i][k].setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
            }
        }

        for(int k = 0; k < 14; k++)
        {
            for(int i = 0; i < 8; i++)
            {
                mainPanel.add(mainText[k][i]);
            }
        }

    }

    public void run()
    {
        setSize(900,500);
        setTitle("NCSSM Class Generator");
        setVisible(true);
    }

    public static void main(String[] args)
    {
        ClassGenerator cg = new ClassGenerator();
        javax.swing.SwingUtilities.invokeLater(cg);
    }

    private class CheckFrame extends JFrame implements Runnable
    {
        /**
         *
         */

        private static final long serialVersionUID = 1419144773273899456L;

        final private JCheckBox[][] allBlocks;
        private JPanel blockChecks;

        CheckFrame()
        {
            super();

            allBlocks = new JCheckBox[9][7];
            blockChecks = new JPanel(new GridLayout(10, 8));

            createCheckBoxes();

            setLayout(new BorderLayout());
            getContentPane().add(blockChecks, BorderLayout.CENTER);
        }

        private void createCheckBoxes()
        {
            blockChecks.add(new JLabel("Select All"));
            for(int k = 0; k < 7; k++)
            {
                blockChecks.add(new JLabel(""));
            }

            addBlockRow(0, 2);
            addBlockRow(1, 2);
            addBlockRow(2, 3);
            addBlockRow(3, 3);
            addBlockRow(4, 4);
            addBlockRow(5, 4);
            addBlockRow(6, 5);
            addBlockRow(7, 6);
            addBlockRow(8, 6);

            final JCheckBox[] selectAll = new JCheckBox[9];
            for(int k = 0; k < 9; k++)
            {
                selectAll[k] = new JCheckBox();
            }

            for(int i = 0; i < 9; i++)
            {
                final int current = i;
                selectAll[i].addActionListener(e -> {
                    for(int k = 0; k < 7; k++)
                    {
                        allBlocks[current][k].doClick(0);
                    }
                });
            }

            for(int k = 0; k < 9; k++)
            {
                blockChecks.add(selectAll[k]);
                for(int i = 0; i < 7; i++)
                {
                    if((i > 3 && k > 6) || (i == 6 && k != 5))
                    {
                        blockChecks.add(new JLabel(""));
                    }
                    else
                    {
                        blockChecks.add(allBlocks[k][i]);
                    }
                }
            }

            for(JCheckBox[] rows: allBlocks)
            {
                for(final JCheckBox checkBox: rows)
                {
                    String checkText = checkBox.getText();
                    for(JTextField[] textRows: mainText)
                    {
                        for(final JTextField label: textRows)
                        {
                            if(label.getText().equals(checkText))
                            {
                                checkBox.addActionListener(e -> {
                                    if(checkBox.isSelected())
                                    {
                                        label.setBackground(blockColor(label.getText()));
                                    }
                                    else
                                    {
                                        label.setBackground(Color.white);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }

        private Color blockColor(String block)
        {
            if(block.contains("A"))
            {
                return new Color(255, 153, 204);
            }
            else if(block.contains("B"))
            {
                return new Color(255, 204, 153);
            }
            else if(block.contains("C"))
            {
                return new Color(255, 255, 153);
            }
            else if(block.contains("D"))
            {
                return new Color(0, 255, 0);
            }
            else if(block.contains("E"))
            {
                return new Color(204, 255, 255);
            }
            else if(block.contains("F"))
            {
                return new Color(0, 204, 255);
            }
            else if(block.contains("G"))
            {
                return new Color(255, 153, 0);
            }
            else if(block.contains("H"))
            {
                return new Color(255, 0, 0);
            }
            else
            {
                return new Color(0, 175, 80);
            }
        }

        private void addBlockRow(int row, int lab)
        {
            for(int i = 0; i < 7; i++)
            {
                if(i == 6 && row == 5)
                {
                    allBlocks[row][i] = new JCheckBox(blocks[row] + 5 + " Lab");
                }
                else
                {
                    if(i < lab)
                    {
                        allBlocks[row][i] = new JCheckBox(blocks[row] + (i + 1));
                    }
                    else
                    {
                        allBlocks[row][i] = new JCheckBox(blocks[row] + i);
                    }
                    if(i == lab)
                    {
                        allBlocks[row][i] = new JCheckBox(blocks[row] + i + " Lab");
                    }
                }
            }
        }

        public void run()
        {
            setSize(750,500);
            setTitle("Pick Your Blocks");
            setVisible(true);
        }
    }

}
