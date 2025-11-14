public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * LPAREN: \(
     * RPAREN: \)
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        // NUM: [0-9]*\.[0-9]+
        Automaton a_num = new AutomatonImpl();
        a_num.addState(0, true, false);  // start state
        a_num.addState(1, false, false); // after digits before dot
        a_num.addState(2, false, false); // after dot
        a_num.addState(3, false, true);  // after digits after dot (accept)
        
        // Optional digits before dot: [0-9]*
        for (char c = '0'; c <= '9'; c++) {
            a_num.addTransition(0, c, 1);
            a_num.addTransition(1, c, 1);
        }
        
        // Dot: \.
        a_num.addTransition(0, '.', 2);
        a_num.addTransition(1, '.', 2);
        
        // Required digits after dot: [0-9]+
        for (char c = '0'; c <= '9'; c++) {
            a_num.addTransition(2, c, 3);
            a_num.addTransition(3, c, 3);
        }
        
        // PLUS: \+
        Automaton a_plus = new AutomatonImpl();
        a_plus.addState(0, true, false);
        a_plus.addState(1, false, true);
        a_plus.addTransition(0, '+', 1);
        
        // MINUS: -
        Automaton a_minus = new AutomatonImpl();
        a_minus.addState(0, true, false);
        a_minus.addState(1, false, true);
        a_minus.addTransition(0, '-', 1);
        
        // TIMES: \*
        Automaton a_times = new AutomatonImpl();
        a_times.addState(0, true, false);
        a_times.addState(1, false, true);
        a_times.addTransition(0, '*', 1);
        
        // DIV: /
        Automaton a_div = new AutomatonImpl();
        a_div.addState(0, true, false);
        a_div.addState(1, false, true);
        a_div.addTransition(0, '/', 1);
        
        // LPAREN: \(
        Automaton a_lparen = new AutomatonImpl();
        a_lparen.addState(0, true, false);
        a_lparen.addState(1, false, true);
        a_lparen.addTransition(0, '(', 1);
        
        // RPAREN: \)
        Automaton a_rparen = new AutomatonImpl();
        a_rparen.addState(0, true, false);
        a_rparen.addState(1, false, true);
        a_rparen.addTransition(0, ')', 1);
        
        // WHITE_SPACE: (' '|\n|\r|\t)*
        Automaton a_ws = new AutomatonImpl();
        a_ws.addState(0, true, true);  // start state is also accept (zero or more)
        a_ws.addState(1, false, true); // accept state after one whitespace
        a_ws.addTransition(0, ' ', 1);
        a_ws.addTransition(0, '\n', 1);
        a_ws.addTransition(0, '\r', 1);
        a_ws.addTransition(0, '\t', 1);
        a_ws.addTransition(1, ' ', 1);
        a_ws.addTransition(1, '\n', 1);
        a_ws.addTransition(1, '\r', 1);
        a_ws.addTransition(1, '\t', 1);
        
        // Create the lexer and add all automata
        lex = new LexerImpl();
        lex.add_automaton(TokenType.NUM, a_num);
        lex.add_automaton(TokenType.PLUS, a_plus);
        lex.add_automaton(TokenType.MINUS, a_minus);
        lex.add_automaton(TokenType.TIMES, a_times);
        lex.add_automaton(TokenType.DIV, a_div);
        lex.add_automaton(TokenType.LPAREN, a_lparen);
        lex.add_automaton(TokenType.RPAREN, a_rparen);
        lex.add_automaton(TokenType.WHITE_SPACE, a_ws);
    }

}