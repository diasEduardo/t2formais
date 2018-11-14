/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import trabalho2.model.automato.Automaton;
import trabalho2.model.automato.State;
import trabalho2.model.automato.Transitions;
    import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author nathan
 */
public class AnalisadorLexico {
    private Automaton AUTOMATO;
    final Pattern reservedpattern = Pattern.compile("if|them|else|while|break|do|true|false|basic");
    final Pattern operatorsPattern = Pattern.compile("\\{|\\}|\\[|\\]|;|={1,2}|\\(|\\)|\\|{1,2}|&{2}|<|>|\\+|-|\\/|\\*");
    public AnalisadorLexico() {
        AUTOMATO = createAnalisisAutomaton();
    }
    
    public enum TokenType {
        ID, RESERVED, OPERATOR, ERROR;

        public static TokenType get(String typeName) {
            for (TokenType categoria : TokenType.values()) {
                if (typeName.equals(categoria.toString())) {
                    return categoria;
                }
            }

            return ERROR;
        }
    }
    
    private TokenType checkTokenType(String token) {
        Matcher reservedMatcher = reservedpattern.matcher(token);
        Matcher operatorsMatcher = operatorsPattern.matcher(token);
        if (reservedMatcher.find()) {
            return TokenType.RESERVED;
        } else if (operatorsMatcher.find()) {
            return TokenType.OPERATOR;
        }
        
        return TokenType.ID;
    }
    
    private Automaton createAnalisisAutomaton() {
        ArrayList<State> states = new ArrayList<State>();
        ArrayList<Character> alphabet = new ArrayList<>();
        Transitions transitions = new Transitions();
        State initialState;
        ArrayList<State> finalState = new ArrayList<>();
        String id = "Automato";
        
        State A = new State("A");
        State B = new State("B");
        State C = new State("C");
        states.add(A);
        states.add(B);
        states.add(C);
        alphabet.add('i');
        alphabet.add('f');
        initialState = A;
        finalState.add(C);
        transitions.addTransition(A, 'i', B);
        transitions.addTransition(B, 'f', C);
        return new 
        Automaton(states, alphabet, transitions, initialState, finalState, id);
    }
    

    public void analise(String sourceCode) {
        String[] conjuntoDePalavras = sourceCode.trim().split(" ");
        HashMap<TokenType, HashSet<String>> tabelaDeTokens = new HashMap<TokenType, HashSet<String>>() {
            {
                for (TokenType categoria : TokenType.values()) {
                    put(categoria, new HashSet<String>());
                }
            }
        };
        for (String palavra : conjuntoDePalavras) {
            System.err.println(palavra);
            tabelaDeTokens.get(doLexAnalisis(AUTOMATO.getInitialState(), palavra, palavra)).add(palavra);
        }
        System.out.println(tabelaDeTokens);
    }
    
    public TokenType doLexAnalisis (State actual, String word, String initialWord) {
        if (AUTOMATO.getFinalStates().contains(actual)){
            return checkTokenType(initialWord);
        }
        try {
            State estadoDestino = AUTOMATO.getNextStates(actual, word.charAt(0)).get(0);
            System.out.println(estadoDestino);
            if (estadoDestino != null) {
                return doLexAnalisis(estadoDestino, word.substring(1), initialWord);
            } 
        } catch(Exception e) {}
        return TokenType.ERROR;
    }

    public String getNextToken() {
        return "";
    }
}
