package fr.lbarthon.computorv2.parser;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.ast.Node;
import fr.lbarthon.computorv2.ast.Token;
import fr.lbarthon.computorv2.exceptions.ComplexFormatException;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.utils.StringUtils;
import fr.lbarthon.computorv2.variables.Complex;

import java.util.Comparator;

public class Parser {

    private Computor computor;

    public Parser(Computor computor) {
        this.computor = computor;
    }

    public void parse(Node node) throws ParseException, ComplexFormatException {
        String data = node.getTempAndClear().trim();
        Integer tokenIndex = getTokenIndex(data, true);

        if (tokenIndex == null) {
            if (getTokenIndex(data, false) == null) {
                Complex complex = Complex.valueOf(data);
                if (complex != null) {
                    node.setToken(complex);
                } else {
                    node.setToken(data);
                }
            } else {
                AST ast = new AST(this, new Node(this.computor));
                node.setToken(ast);
                Node head = ast.getHead();
                head.setTemp(StringUtils.removeDepth(data, 1));

                if (head.getTemp() == null) {
                    throw new ParseException(data, data.indexOf(StringUtils.DEPTH_START));
                }
                parse(head);
            }
        } else {
            node.setToken(Token.fromChar(data.charAt(tokenIndex)));
            node.setLeft(new Node(this.computor, data.substring(0, tokenIndex).trim()));
            node.setRight(new Node(this.computor, data.substring(tokenIndex + 1).trim()));
            parse(node.getLeft());
            parse(node.getRight());
        }
    }

    private Integer getTokenIndex(String str, boolean depthZero) {
        Integer tokenIndex;
        for (int i = Token.getMaxPrio(); i >= 0; i--) {
            tokenIndex = Token.getPrioValues(i).stream()
                    // Getting all indexes of this token
                    .map(t -> StringUtils.indexesOf(str, t.getToken()).stream())
                    // Converting the stream of streams to an unique stream
                    .flatMap(nbr -> nbr)
                    .filter(index -> {
                        int ret = StringUtils.getDepthCheck(str, index);
                        if (ret == -1) return false;
                        return (ret == 0) == depthZero;
                    })
                    .max(Comparator.comparingInt(nbr -> nbr))
                    .orElse(null);

            if (tokenIndex != null) {
                return tokenIndex;
            }
        }
        return null;
    }
}
