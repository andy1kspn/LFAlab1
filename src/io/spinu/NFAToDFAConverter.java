package io.spinu;
// NFAToDFAConverter.java
import java.util.*;

public class NFAToDFAConverter {
    public static DFA convert(NFA nfa) {git init
        Set<Set<String>> stariDFA = new HashSet<>();
        Set<Character> alfabet = nfa.getAlfabet();
        Set<Set<String>> stareFinalaDFA = new HashSet<>();

        Set<String> stareInitialaDFA = new HashSet<>();
        stareInitialaDFA.add(nfa.getStareInitiala());
        stariDFA.add(stareInitialaDFA);

        Queue<Set<String>> stariNemarcate = new LinkedList<>();
        stariNemarcate.add(stareInitialaDFA);

        while (!stariNemarcate.isEmpty()) {
            Set<String> stareCurenta = stariNemarcate.poll();

            for (char simbol : alfabet) {
                Set<String> stareUrmatoare = new HashSet<>();
                for (String s : stareCurenta) {
                    stareUrmatoare.addAll(nfa.getTranzitii().get(s).get(simbol));
                }

                if (!stareUrmatoare.isEmpty() && !stariDFA.contains(stareUrmatoare)) {
                    stariDFA.add(stareUrmatoare);
                    stariNemarcate.add(stareUrmatoare);
                }
            }

            for (String stare : stareCurenta) {
                if (nfa.getStareFinala().contains(stare)) {
                    stareFinalaDFA.add(stareCurenta);
                    break;
                }
            }
        }

        stariDFA.add(new HashSet<>());
        DFA dfa = new DFA(stariDFA, alfabet, stareInitialaDFA, stareFinalaDFA);

        for (Set<String> stare : stariDFA) {
            for (char simbol : alfabet) {
                Set<String> stareUrmatoare = new HashSet<>();
                for (String s : stare) {
                    stareUrmatoare.addAll(nfa.getTranzitii().get(s).get(simbol));
                }
                dfa.setTranzitie(stare, simbol, stareUrmatoare);
            }
        }

        return dfa;
    }
}