package fr.lbarthon.computorv2.parser;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.ast.Node;
import fr.lbarthon.computorv2.ast.Token;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.utils.StringUtils;
import fr.lbarthon.computorv2.variables.Complex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    private Computor computor;

    public Parser(Computor computor) {
        this.computor = computor;
    }

    public void parse(Node node) throws ParseException {
        String data = node.getTempAndClear();
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

            if (node.getToken() == Token.LESS && node.getLeft().getTemp().isEmpty()) {
                node.setToken(Complex.valueOf(Token.LESS.getToken() + node.getRight().getTemp()));
            } else {
                parse(node.getLeft());
                parse(node.getRight());
            }
        }
    }

    private Integer getTokenIndex(String str, boolean depthZero) throws ParseException {
        List<Integer> tmp, indexes;
        Integer tokenIndex;
        for (int i = Token.getMaxPrio(); i >= 0; i--) {
            indexes = new ArrayList<>();
            tmp = Token.getPrioValues(i).stream()
                    .map(t -> StringUtils.lastIndexOf(str, t.getToken()))
                    .filter(nbr -> nbr >= 0).collect(Collectors.toList());

            for (Integer index : tmp) {
                if ((StringUtils.getDepthCheck(str, index) == 0) == depthZero) {
                    indexes.add(index);
                }
            }

            tokenIndex = indexes.stream().max(Comparator.comparingInt(nbr -> nbr)).orElse(null);

            if (tokenIndex != null) {
                return tokenIndex;
            }
        }
        return null;
    }
}
