package io.spinu;

// DFA.java
import java.util.*;

public class DFA {
    private Set<Set<String>> stari;
    private Set<Character> alfabet;
    private Map<Set<String>, Map<Character, Set<String>>> tranzitii;
    private Set<String> stareInitiala;
    private Set<Set<String>> stareFinala;

    public DFA(Set<Set<String>> stari, Set<Character> alfabet, Set<String> stareInitiala, Set<Set<String>> stareFinala) {
        this.stari = stari;
        this.alfabet = alfabet;
        this.stareInitiala = stareInitiala;
        this.stareFinala = stareFinala;

        this.tranzitii = new HashMap<>();
        for (Set<String> stare : stari) {
            tranzitii.put(stare, new HashMap<>());
            for (char simbol : alfabet) {
                tranzitii.get(stare).put(simbol, new HashSet<>());
            }
        }
    }

    public void afiseazaDFA() {
        System.out.println("\nStari DFA:");
        for (Set<String> stare : stari) {
            if (stare.isEmpty()) {
                System.out.println("[]");
            } else {
                System.out.println(stare);
            }
        }

        System.out.println("\nAlfabetul DFA: " + alfabet);

        System.out.println("\nTranzitii DFA:");
        for (Set<String> stare : tranzitii.keySet()) {
            for (char simbol : alfabet) {
                Set<String> stareUrmatoare = tranzitii.get(stare).get(simbol);
                System.out.print(stare + " --(" + simbol + ")--> ");
                if (stareUrmatoare.isEmpty()) {
                    System.out.println("[]");
                } else {
                    System.out.println(stareUrmatoare);
                }
            }
        }

        System.out.println("\nStarea initiala DFA: " + stareInitiala);

        System.out.println("\nStarile finale DFA:");
        for (Set<String> stareFinala : this.stareFinala) {
            System.out.println(stareFinala);
        }
    }

    public Map<Set<String>, Map<Character, Set<String>>> getTranzitii() {
        return tranzitii;
    }

    public void setTranzitie(Set<String> fromStare, char simbol, Set<String> toStare) {
        tranzitii.get(fromStare).put(simbol, toStare);
    }

    public Set<Set<String>> getStari() {
        return stari;
    }

    public Set<Character> getAlfabet() {
        return alfabet;
    }

    public Set<String> getStareInitiala() {
        return stareInitiala;
    }

    public Set<Set<String>> getStareFinala() {
        return stareFinala;
    }
}