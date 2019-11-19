package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class AST {

    final Parser parser;

    @Setter
    Node head;
}
