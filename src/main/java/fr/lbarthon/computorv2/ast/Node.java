package fr.lbarthon.computorv2.ast;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Node {
    String temp;
    Object token;
    Node left;
    Node right;

    public Node(Object token, Node left, Node right) {
        this.token = token;
        this.left = left;
        this.right = right;
        this.temp = null;
    }

    public Node(String temp) {
        this.token = null;
        this.left = null;
        this.right = null;
        this.temp = temp;
    }

    public boolean isValue() {
        return !(this.token instanceof Token);
    }
}
