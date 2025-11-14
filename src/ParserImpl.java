
public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    @Override
    public Expr do_parse() throws Exception {
        return parseT();
    }
    
    // T -> F AddOp T | F
    private Expr parseT() throws Exception {
        Expr f = parseF();
        
        // Check if we have an AddOp (PLUS or MINUS)
        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token addOp = parseAddOp();
            Expr t = parseT();
            
            if (addOp.ty == TokenType.PLUS) {
                return new PlusExpr(f, t);
            } else {
                return new MinusExpr(f, t);
            }
        }
        
        // Otherwise, just return F
        return f;
    }
    
    // F -> Lit MulOp F | Lit
    private Expr parseF() throws Exception {
        Expr lit = parseLit();
        
        // Check if we have a MulOp (TIMES or DIV)
        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token mulOp = parseMulOp();
            Expr f = parseF();
            
            if (mulOp.ty == TokenType.TIMES) {
                return new TimesExpr(lit, f);
            } else {
                return new DivExpr(lit, f);
            }
        }
        
        // Otherwise, just return Lit
        return lit;
    }
    
    // Lit -> NUM | LPAREN T RPAREN
    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token num = consume(TokenType.NUM);
            return new FloatExpr(Float.parseFloat(num.lexeme));
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr t = parseT();
            consume(TokenType.RPAREN);
            return t;
        } else {
            throw new Exception("Expected NUM or LPAREN");
        }
    }
    
    // AddOp -> PLUS | MINUS
    private Token parseAddOp() throws Exception {
        if (peek(TokenType.PLUS, 0)) {
            return consume(TokenType.PLUS);
        } else if (peek(TokenType.MINUS, 0)) {
            return consume(TokenType.MINUS);
        } else {
            throw new Exception("Expected PLUS or MINUS");
        }
    }
    
    // MulOp -> TIMES | DIV
    private Token parseMulOp() throws Exception {
        if (peek(TokenType.TIMES, 0)) {
            return consume(TokenType.TIMES);
        } else if (peek(TokenType.DIV, 0)) {
            return consume(TokenType.DIV);
        } else {
            throw new Exception("Expected TIMES or DIV");
        }
    }

}