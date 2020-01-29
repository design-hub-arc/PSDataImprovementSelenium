package gui;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import logging.Logger;

/**
 * Not sure if I want this to implement Logger,
 * since while is does need to report messages, it needn't store them.
 * 
 * @author Matt Crow
 */
public class ScrollableTextDisplay extends JPanel implements Logger{
    private final JTextArea textArea;
    private final JScrollPane pane;
    private final StringBuilder text;
    private final StringBuilder log;
    
    public ScrollableTextDisplay(String displayText){
        super();
        setLayout(new GridLayout(1, 1));
        text = new StringBuilder();
        log = new StringBuilder();
        text.append(displayText);
        
        textArea = new JTextArea(displayText);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setRows(4);
        textArea.setBackground(Color.white);
        pane = new JScrollPane(textArea);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane);
        revalidate();
        repaint();
    }

    ScrollableTextDisplay() {
        this("");
    }
    
    public void appendText(String newText){
        text.append(newText);
        textArea.setText(text.toString());
        repaint();
        SwingUtilities.invokeLater(()->{
            JScrollBar bar = pane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }
    public void setText(String newText){
        clear();
        appendText(newText);
    }
    public void clear(){
        text.delete(0, text.length());
        textArea.setText("");
    }

    @Override
    public void log(String s) {
        log.append(s);
        appendText(s);
    }

    @Override
    public String getLog() {
        return log.toString();
    }
}
