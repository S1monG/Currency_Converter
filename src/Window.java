import java.awt.Container;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/* Använder java swing biblioteket för att skapa ett fönster med
valmöjligheter för valutor och belopp att konvertera */

public class Window {
    
    public Window(Map<String, Double> currencyMap, String lastUpdated) {
        SwingUtilities.invokeLater(() -> createWindow(currencyMap, "Currency Converter", lastUpdated, 600, 350));
    }

    public void createWindow(Map<String, Double> currencyMap, String title, String lastUpdated, int width, int height) {

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();

        JTabbedPane tabs = new JTabbedPane();

        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();          
        JPanel centerPanel = new JPanel();
        JPanel allPanels = new JPanel(new BorderLayout());

        // 1 = center alignment, 20 = Horizontal gap, 40 = Vertical gap
        centerPanel.setLayout(new FlowLayout(1, 20, 40));  
        bottomPanel.setLayout(new FlowLayout(1, 10, 60));

        JLabel datum = new JLabel("Senast uppdaterad: " + lastUpdated);
        topPanel.add(datum);

        Set<String> currencySet = currencyMap.keySet();
        String[] c = new String[currencySet.size()];
        JComboBox<String> currencyList1 = new JComboBox<String>(currencySet.toArray(c));
        JComboBox<String> currencyList2 = new JComboBox<String>(currencySet.toArray(c));
        
        currencyList1.setEditable(true);
        currencyList2.setEditable(true);

        centerPanel.add(new JLabel("From:"));
        centerPanel.add(currencyList1);
        centerPanel.add(new JLabel("To:"));
        centerPanel.add(currencyList2);
        JTextField selectedAmout = new JTextField("Amout");
        selectedAmout.setColumns(12);
        centerPanel.add(selectedAmout);
        
        JLabel message = new JLabel();
        message.setPreferredSize(new Dimension(100, 20));
        
        JButton convert = new JButton("Convert");
        convert.addActionListener(event -> {
            String from = (String) currencyList1.getSelectedItem();
            String to = (String) currencyList2.getSelectedItem();
            try {
                Double amout = Double.parseDouble(selectedAmout.getText());
                if (currencySet.contains(from) && currencySet.contains(to)) {
                    double newAmout = Currencies.convert(amout, from, to);
                    message.setText(String.valueOf(Math.round(newAmout * 100)/100.0) + "  " + to);    // rounds to 2 decimals
                    
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Wrong format for amout", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(convert);
        bottomPanel.add(message);

        allPanels.add(topPanel, BorderLayout.NORTH);
        allPanels.add(centerPanel, BorderLayout.CENTER);
        allPanels.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane scrollConverter = new JScrollPane(allPanels);

        ArrayList<String> listCurrency = new ArrayList<>();
        currencyMap.forEach((k,v) -> listCurrency.add("EUR -> " + k + " : " + v));
        JList<String> list = new JList(listCurrency.toArray());
        JScrollPane scrollCurrencies = new JScrollPane(list);
        
        tabs.add("Converter", scrollConverter);
        tabs.add("Currencies", scrollCurrencies);

        pane.add(tabs);

        frame.setMinimumSize(new Dimension(width, height));
        frame.setVisible(true);
    }

}