package io.spinu;

// MainGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MainGUI {
    private JFrame frame;
    private JTextField alfabetField, stariField, stareInitialaField, stareFinalaField, tranzitiiField;
    private JButton transformButton;
    private JTextArea resultArea;
    private GraphPanel nfaGraphPanel, dfaGraphPanel;
    private NFA nfa;
    private DFA dfa;

    public MainGUI() {
        frame = new JFrame("NFA to DFA Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        // Panou pentru introducerea datelor
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Alfabet (separat prin spatiu):"));
        alfabetField = new JTextField();
        inputPanel.add(alfabetField);

        inputPanel.add(new JLabel("Stari (separat prin spatiu):"));
        stariField = new JTextField();
        inputPanel.add(stariField);

        inputPanel.add(new JLabel("Stare initiala:"));
        stareInitialaField = new JTextField();
        inputPanel.add(stareInitialaField);

        inputPanel.add(new JLabel("Stari finale (separat prin spatiu):"));
        stareFinalaField = new JTextField();
        inputPanel.add(stareFinalaField);

        inputPanel.add(new JLabel("Tranzitii (stare simbol stare, separate prin ;):"));
        tranzitiiField = new JTextField();
        inputPanel.add(tranzitiiField);

        transformButton = new JButton("Transforma in DFA");
        inputPanel.add(transformButton);
        transformButton.addActionListener(new TransformActionListener());

        frame.add(inputPanel, BorderLayout.NORTH);

        // Panouri pentru grafuri
        JPanel graphPanel = new JPanel(new GridLayout(1, 2));
        nfaGraphPanel = new GraphPanel();
        dfaGraphPanel = new GraphPanel();
        graphPanel.add(new JLabel("NFA Graph"));
        graphPanel.add(nfaGraphPanel);
        graphPanel.add(new JLabel("DFA Graph"));
        graphPanel.add(dfaGraphPanel);
        frame.add(graphPanel, BorderLayout.CENTER);

        // Panou pentru afișarea rezultatelor textuale
        resultArea = new JTextArea(10, 50); // Mărim dimensiunea JTextArea
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(1200, 200)); // Ajustăm dimensiunea
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private class TransformActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Citim datele introduse
                Set<Character> alfabet = new HashSet<>();
                for (String simbol : alfabetField.getText().trim().split("\\s+")) {
                    if (simbol.length() != 1) throw new IllegalArgumentException("Simbolul " + simbol + " trebuie sa fie un singur caracter!");
                    alfabet.add(simbol.charAt(0));
                }

                Set<String> stari = new HashSet<>(Arrays.asList(stariField.getText().trim().split("\\s+")));
                String stareInitiala = stareInitialaField.getText().trim();
                Set<String> stareFinala = new HashSet<>(Arrays.asList(stareFinalaField.getText().trim().split("\\s+")));

                // Creăm NFA-ul
                nfa = new NFA(stari, alfabet, stareInitiala, stareFinala);

                // Adăugăm tranzițiile
                for (String tranzitie : tranzitiiField.getText().trim().split(";")) {
                    String[] parts = tranzitie.trim().split("\\s+");
                    if (parts.length != 3) throw new IllegalArgumentException("Tranzitia " + tranzitie + " trebuie sa aiba formatul: stare simbol stare");
                    String fromStare = parts[0];
                    char simbol = parts[1].charAt(0);
                    String toStare = parts[2];
                    nfa.addTranzitie(fromStare, simbol, toStare);
                }

                // Desenăm NFA-ul
                nfaGraphPanel.setNFA(nfa);

                // Transformăm în DFA
                dfa = NFAToDFAConverter.convert(nfa);

                // Desenăm DFA-ul
                dfaGraphPanel.setDFA(dfa);

                // Afișăm rezultatele textuale doar dacă dfa a fost inițializat
                if (dfa != null) {
                    StringBuilder result = new StringBuilder();
                    result.append("Stari DFA:\n");
                    for (Set<String> stare : dfa.getStari()) {
                        result.append(stare.isEmpty() ? "[]\n" : stare + "\n");
                    }
                    result.append("\nAlfabetul DFA: ").append(dfa.getAlfabet()).append("\n");
                    result.append("\nTranzitii DFA:\n");
                    for (Set<String> stare : dfa.getTranzitii().keySet()) {
                        for (char simbol : dfa.getAlfabet()) {
                            Set<String> stareUrmatoare = dfa.getTranzitii().get(stare).get(simbol);
                            result.append(stare).append(" --(").append(simbol).append(")--> ")
                                    .append(stareUrmatoare.isEmpty() ? "[]\n" : stareUrmatoare + "\n");
                        }
                    }
                    result.append("\nStarea initiala DFA: ").append(dfa.getStareInitiala()).append("\n");
                    result.append("\nStarile finale DFA:\n");
                    for (Set<String> finalState : dfa.getStareFinala()) {
                        result.append(finalState).append("\n");
                    }
                    resultArea.setText(result.toString());
                } else {
                    resultArea.setText("Eroare: DFA nu a fost generat.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Eroare: " + ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
                resultArea.setText("Eroare la transformare: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}