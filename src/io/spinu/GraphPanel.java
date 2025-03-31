package io.spinu;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
public class GraphPanel extends JPanel {
    private Map<String, Point> statePositions;
    private NFA nfa;
    private DFA dfa;
    private boolean isNFA;

    public GraphPanel() {
        statePositions = new HashMap<>();
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
    }

    public void setNFA(NFA nfa) {
        this.nfa = nfa;
        this.isNFA = true;
        positionStates();
        repaint();
    }

    public void setDFA(DFA dfa) {
        this.dfa = dfa;
        this.isNFA = false;
        positionStates();
        repaint();
    }

    private void positionStates() {
        statePositions.clear();
        Set<?> states = isNFA ? nfa.getStari() : dfa.getStari();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        // Ajustăm raza cercului în funcție de numărul de stări pentru a evita suprapunerile
        int radius = Math.min(getWidth(), getHeight()) / 2 - 50; // Rază mai mare
        int i = 0;
        for (Object state : states) {
            double angle = 2 * Math.PI * i / states.size();
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            statePositions.put(state.toString(), new Point(x, y));
            i++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isNFA && nfa != null) {
            drawGraph(g, nfa.getStari(), nfa.getTranzitii(), nfa.getStareInitiala(), nfa.getStareFinala());
        } else if (!isNFA && dfa != null) {
            drawGraph(g, dfa.getStari(), dfa.getTranzitii(), dfa.getStareInitiala(), dfa.getStareFinala());
        }
    }

    private <T> void drawGraph(Graphics g, Set<T> states, Map<T, Map<Character, Set<String>>> transitions, T initialState, Set<T> finalStatesSet) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Desenăm tranzițiile
        for (T fromState : transitions.keySet()) {
            Map<Character, Set<String>> trans = transitions.get(fromState);
            for (char symbol : trans.keySet()) {
                Set<String> toStateSet = trans.get(symbol);
                if (toStateSet.isEmpty()) continue;
                String toState = toStateSet.toString();
                Point p1 = statePositions.get(fromState.toString());
                Point p2 = statePositions.get(toState);
                if (p1 != null && p2 != null) {
                    g2d.setColor(Color.BLUE); // Tranziții în albastru
                    if (p1.equals(p2)) {
                        // Buclă
                        g2d.drawOval(p1.x - 20, p1.y - 40, 40, 40);
                        g2d.drawString(String.valueOf(symbol), p1.x, p1.y - 40);
                    } else {
                        // Săgeată
                        drawArrow(g2d, p1.x, p1.y, p2.x, p2.y, String.valueOf(symbol));
                    }
                }
            }
        }

        // Desenăm stările
        for (T state : states) {
            Point p = statePositions.get(state.toString());
            if (p != null) {
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillOval(p.x - 20, p.y - 20, 40, 40);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(p.x - 20, p.y - 20, 40, 40);
                if (finalStatesSet.contains(state)) {
                    g2d.setColor(Color.RED); // Stări finale cu cerc dublu roșu
                    g2d.drawOval(p.x - 15, p.y - 15, 30, 30);
                    g2d.setColor(Color.BLACK);
                }
                if (state.equals(initialState)) {
                    g2d.setColor(Color.GREEN); // Stare inițială cu săgeată verde
                    g2d.drawLine(p.x - 40, p.y, p.x - 20, p.y);
                    g2d.setColor(Color.BLACK);
                }
                g2d.drawString(state.toString(), p.x - 10, p.y + 5);
            }
        }
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2, String label) {
        double dx = x2 - x1, dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);
        double arrowLength = 10;
        double arrowAngle = Math.toRadians(30);

        dx /= length;
        dy /= length;

        x2 = (int) (x2 - dx * 20);
        y2 = (int) (y2 - dy * 20);

        g2d.drawLine(x1, y1, x2, y2);

        int arrowX1 = (int) (x2 - arrowLength * Math.cos(Math.atan2(dy, dx) - arrowAngle));
        int arrowY1 = (int) (y2 - arrowLength * Math.sin(Math.atan2(dy, dx) - arrowAngle));
        int arrowX2 = (int) (x2 - arrowLength * Math.cos(Math.atan2(dy, dx) + arrowAngle));
        int arrowY2 = (int) (y2 - arrowLength * Math.sin(Math.atan2(dy, dx) + arrowAngle));
        g2d.drawLine(x2, y2, arrowX1, arrowY1);
        g2d.drawLine(x2, y2, arrowX2, arrowY2);

        // Ajustăm poziția etichetei pentru a evita suprapunerile
        int labelX = (x1 + x2) / 2 + (y2 > y1 ? 10 : -10);
        int labelY = (y1 + y2) / 2 + (x2 > x1 ? -10 : 10);
        g2d.drawString(label, labelX, labelY);
    }
}