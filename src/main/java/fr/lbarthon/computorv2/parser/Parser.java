package fr.lbarthon.computorv2.parser;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.Validator;
import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.ast.Node;
import fr.lbarthon.computorv2.ast.Token;
import fr.lbarthon.computorv2.exceptions.ComplexFormatException;
import fr.lbarthon.computorv2.exceptions.MatrixFormatException;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.exceptions.UnknownFunctionException;
import fr.lbarthon.computorv2.utils.StringUtils;
import fr.lbarthon.computorv2.variables.CallableFunction;
import fr.lbarthon.computorv2.variables.Complex;
import fr.lbarthon.computorv2.variables.Function;
import fr.lbarthon.computorv2.variables.Matrix;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.regex.Pattern;

@AllArgsConstructor
public class Parser {

    private static final Pattern DEPTH_CHECK = Pattern.compile("^\\s*\\(.*\\)\\s*$");

    private Computor computor;

    public void parse(Node node) throws ParseException, ComplexFormatException, MatrixFormatException {
        String data = node.getTempAndClear().trim();
        Integer tokenIndex = getTokenIndex(data, true);

        if (tokenIndex == null) {
            if (getTokenIndex(data, false) == null && !hasDepth(data)) {
                Complex complex = Complex.valueOf(data);
                if (complex != null) {
                    node.setToken(complex);
                } else {
                    try {
                        if (!data.isEmpty() && data.contains("[")) {
                            Matrix matrix = new Validator(data)
                                    // Check that brackets are well closed and all that stuff
                                    .brackets('[', ']')
                                    // Getting the matrix using the validator (why not ?)
                                    .matrix();
                            if (matrix != null) {
                                node.setToken(matrix);
                                return;
                            }
                        }
                    } catch (ParseException | MatrixFormatException e) {
                        this.computor.getAst().setException(e);
                    }

                    if (StringUtils.isAlphabetic(data)) {
                        node.setToken(data);
                    } else {
                        throw new ParseException(data, StringUtils.firstCharNonAlphabetic(data));
                    }
                }
                return;
            }

            AST ast = new AST(this, new Node(this.computor));
            Node head = ast.getHead();
            head.setTemp(StringUtils.removeDepth(data, 1));

            if (head.getTemp() == null) {
                throw new ParseException(data, data.indexOf(StringUtils.DEPTH_START));
            }

            // Prevent weird stuff
            if (!DEPTH_CHECK.matcher(data).matches()) {
                int index = data.indexOf(StringUtils.DEPTH_START);
                String toRetest = data.substring(index);
                if (DEPTH_CHECK.matcher(toRetest).matches()) {
                    // Handle function
                    String functionName = data.substring(0, index);
                    new Validator(functionName).functionName();
                    Function function = this.computor.getFunctions().get(functionName);
                    if (function == null) {
                        this.computor.getAst().setException(new UnknownFunctionException(functionName));
                        // Here we handle function assignation by setting data str in the token
                        node.setToken(data);
                        return;
                    }
                    CallableFunction callableFunction = new CallableFunction(function);
                    callableFunction.addArg(ast);
                    node.setToken(callableFunction);
                } else {
                    throw new ParseException(data, 0);
                }
            } else {
                node.setToken(ast);
            }

            parse(head);
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

    private boolean hasDepth(String str) {
        return str.indexOf(StringUtils.DEPTH_START) != -1;
    }
}
