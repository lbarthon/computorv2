package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.exceptions.ComplexFormatException;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.parser.Parser;
import fr.lbarthon.computorv2.variables.Complex;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class AST {

    private final Parser parser;
    @Getter
    private final Node head;
    @Setter
    @Getter
    private boolean equation = false;

    public void createFrom(String data) throws ParseException, ComplexFormatException {
        this.head.setTemp(data);
        this.parser.parse(this.head);
    }

    public Complex solve() throws ArithmeticException {
        return this.head.solve();
    }

    @Override
    public String toString() {
        return this.head.toString();
    }
}
