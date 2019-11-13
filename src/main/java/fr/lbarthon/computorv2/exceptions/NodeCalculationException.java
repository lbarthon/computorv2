package fr.lbarthon.computorv2.exceptions;

import fr.lbarthon.computorv2.ast.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NodeCalculationException extends Exception {
    private Node node;

    @Override
    public String getMessage() {
        if (this.node.getToken() == null) {
            return "Null token !";
        }

        if (this.node.getLeft() == null) {
            return "Left part empty !";
        }

        if (this.node.getRight() == null) {
            return "Right part empty !";
        }

        return "Error calculating the node !";
    }
}
