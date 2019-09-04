package fr.lbarthon.computorv2.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParsePriority {
    HIGHEST(5),
    HIGH(4),
    NORMAL(3),
    LOW(2),
    LOWEST(1);

    private int nbr;
}
