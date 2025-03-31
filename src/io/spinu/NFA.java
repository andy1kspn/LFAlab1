package io.spinu;

// NFA.java
import java.util.*;

public class NFA {
    private Set<String> stari;
    private Set<Character> alfabet;
    private Map<String, Map<Character, Set<String>>> tranzitii;
    private String stareInitiala;
    private Set<String> stareFinala;

    public NFA(Set<String> stari, Set<Character> alfabet, String stareInitiala, Set<String> stareFinala) {
        this.stari = new HashSet<>(stari);
        this.alfabet = new HashSet<>(alfabet);
        this.stareInitiala = stareInitiala;
        this.stareFinala = new HashSet<>(stareFinala);

        if (!stari.contains(stareInitiala)) {
            throw new IllegalArgumentException("Starea initiala " + stareInitiala + " nu apartine multimii de stari!");
        }
        for (String stare : stareFinala) {
            if (!stari.contains(stare)) {
                throw new IllegalArgumentException("Starea finala " + stare + " nu apartine multimii de stari!");
            }
        }

        this.tranzitii = new HashMap<>();
        for (String stare : stari) {
            tranzitii.put(stare, new HashMap<>());
            for (char simbol : alfabet) {
                tranzitii.get(stare).put(simbol, new HashSet<>());
            }
        }
    }

    public void addTranzitie(String fromStare, char simbol, String toStare) {
        if (!stari.contains(fromStare) || !stari.contains(toStare)) {
            throw new IllegalArgumentException("Starea " + fromStare + " sau " + toStare + " nu exista!");
        }
        if (!alfabet.contains(simbol)) {
            throw new IllegalArgumentException("Simbolul " + simbol + " nu apartine alfabetului!");
        }
        tranzitii.get(fromStare).get(simbol).add(toStare);
    }

    public Set<String> getStari() {
        return stari;
    }

    public Set<Character> getAlfabet() {
        return alfabet;
    }

    public Map<String, Map<Character, Set<String>>> getTranzitii() {
        return tranzitii;
    }

    public String getStareInitiala() {
        return stareInitiala;
    }

    public Set<String> getStareFinala() {
        return stareFinala;
    }
}