package start;

import gui.ScrollableTextDisplay;
import io.FileSelector;
import io.QueryFileReader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import automations.AbstractAutomation;
import automations.AccountBalanceAutomation;
import automations.GoogleSearch;
import java.awt.Dimension;
import util.Browser;

/**
 *
 * @author Matt
 */
public class StartPane extends JPanel{
    private Browser selectedBrowser;
    private AbstractAutomation selAutomation;
    private String webDriverPath;
    private File sourceFile;
    private final ScrollableTextDisplay textDisplay;
    
    public StartPane(){
        selectedBrowser = Browser.CHROME;
        selAutomation = new GoogleSearch();
        webDriverPath = null;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Border b = BorderFactory.createLineBorder(Color.black, 5);
        
        ArrayList<JPanel> panels = new ArrayList<>();
        //panels.add(browserPanel());
        panels.add(automationPanel());
        panels.add(webDriverPanel());
        panels.add(sourcePanel());
        textDisplay = new ScrollableTextDisplay("***Program output will appear here***\n");
        panels.add(textDisplay);
        panels.add(runPanel());
        
        panels.forEach((j)->{
            j.setBorder(b);
            add(j);
        });
    }
    
    private JPanel browserPanel(){
        JPanel browserPanel = new JPanel();
        browserPanel.setLayout(new BorderLayout());
        browserPanel.add(new JLabel("Select Browser to use"), BorderLayout.PAGE_START);
        ButtonGroup bg = new ButtonGroup();
        
        //todo: check which browsers the user has installed
        JRadioButton chrome = new JRadioButton("Chrome");
        chrome.addActionListener((e)->{
            selectedBrowser = Browser.CHROME;
        });
        JRadioButton fireFox = new JRadioButton("FireFox");
        fireFox.addActionListener((e)->{
            selectedBrowser = Browser.FIRE_FOX;
        });
        
        bg.add(chrome);
        bg.add(fireFox);
        chrome.setSelected(true);
        
        JPanel browserSelection = new JPanel();
        browserSelection.add(chrome);
        browserSelection.add(fireFox);
        
        browserPanel.add(browserSelection, BorderLayout.CENTER);
        
        return browserPanel;
    }
    
    private JPanel automationPanel(){
        JPanel autoPanel = new JPanel();
        autoPanel.setLayout(new BorderLayout());
        autoPanel.add(new JLabel("Select which automation to run"), BorderLayout.PAGE_START);
        
        ButtonGroup automations = new ButtonGroup();
        JRadioButton gs = new JRadioButton("Google searches");
        gs.addActionListener((e)->{
            selAutomation = new GoogleSearch();
        });
        JRadioButton actBal = new JRadioButton("Account Balances");
        actBal.addActionListener((e)->{
            selAutomation = new AccountBalanceAutomation();
        });
        automations.add(gs);
        automations.add(actBal);
        gs.setSelected(true);
        JPanel autoSel = new JPanel();
        autoSel.add(gs);
        autoSel.add(actBal);
        autoPanel.add(autoSel, BorderLayout.CENTER);
        return autoPanel;
    }
    
    private JPanel webDriverPanel(){
        JPanel webDriverPanel = new JPanel();
        webDriverPanel.setLayout(new BoxLayout(webDriverPanel, BoxLayout.Y_AXIS));
        
        webDriverPanel.add(new JLabel("Select your web driver"));
        
        JLabel currentDriver = new JLabel("no driver selected");
        webDriverPanel.add(currentDriver);
        
        JButton choosePath = new JButton("Choose web driver");
        choosePath.addActionListener((e)->{
            findWebDriver();
            if(webDriverPath != null){
                currentDriver.setText(webDriverPath);
            }
        });
        webDriverPanel.add(choosePath);
        
        return webDriverPanel;
    }
    
    private JPanel sourcePanel(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel("Data Source File"));
        JLabel fileName = new JLabel("No file selected");
        p.add(fileName);
        JButton fileChooser = new JButton("Choose data source");
        fileChooser.addActionListener((e)->{
            chooseFile();
            if(sourceFile != null){
                fileName.setText(sourceFile.getAbsolutePath());
            }
        });
        p.add(fileChooser);
        return p;
    }
    
    private JPanel runPanel(){
        JPanel p = new JPanel();
        JButton run = new JButton("Run automation");
        run.addActionListener((e)->{
            try{
                if(webDriverPath == null){
                    throw new Exception("Please select your web driver path");
                }
                if(sourceFile == null){
                    throw new Exception("Please select a data source file");
                }
                String data = new QueryFileReader().readFile(sourceFile);
                selAutomation.run(data);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(run, ex.getMessage());
                ex.printStackTrace();
            }
        });
        p.add(run);
        return p;
    }
    
    private void findWebDriver(){
        JFileChooser choose = new JFileChooser();
        choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(choose.showOpenDialog(choose) == JFileChooser.APPROVE_OPTION){
            webDriverPath = choose.getSelectedFile().getAbsolutePath();
            System.out.println(webDriverPath);
            //change this
            System.setProperty("webdriver.chrome.driver", webDriverPath);
        }
    }
    
    private void chooseFile(){
        FileSelector.chooseCsvFile((File f)->{
            sourceFile = f;
        });
    }
}
