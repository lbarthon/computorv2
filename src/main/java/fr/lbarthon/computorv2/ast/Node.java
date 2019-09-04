package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.exceptions.NodeCalculationException;
import fr.lbarthon.computorv2.utils.MathUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Node {
    private Token token;

    private Double left;
    private Double right;

    private Node leftNode;
    private Node rightNode;

    public Node(Token token, Double left, Double right) {
        this(token, left, right, null, null);
    }

    public Node(Token token, Node leftNode, Double right) {
        this(token, null, right, leftNode, null);
    }

    public Node(Token token, Double left, Node rightNode) {
        this(token, left, null, null, rightNode);
    }

    public Node(Token token, Node leftNode, Node rightNode) {
        this(token, null, null, leftNode, rightNode);
    }

    /**
     * Warning ! Huge recursive method
     * @return Double of it's values
     * @throws NodeCalculationException When an element is null
     */
    public double calculate() throws NodeCalculationException {
        if (this.leftNode != null) {
            this.left = this.leftNode.calculate();
        }

        if (this.rightNode != null) {
            this.right = this.rightNode.calculate();
        }

        if (this.token == null || this.right == null || this.left == null) {
            throw new NodeCalculationException(this);
        }

        switch (this.token) {
            case DIV:
                return this.left / this.right;
            case MOD:
                return this.left % this.right;
            case MULT:
                return this.left * this.right;
            case PLUS:
                return this.left + this.right;
            case LESS:
                return this.left - this.right;
            case POW:
                return MathUtils.pow(this.left, this.right.intValue());
        }

        throw new NodeCalculationException(this);
    }
}
