package fr.lbarthon.computorv2.parser;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.token.Tokenizer;

public class Parser {

    private Computor computor;

    public Parser(Computor computor) {
        this.computor = computor;
    }

    public String parse(String str) {
        Tokenizer tokenizer = new Tokenizer(str);

        tokenizer.getTokens().forEach(System.out::println);

        return str;
    }
}
