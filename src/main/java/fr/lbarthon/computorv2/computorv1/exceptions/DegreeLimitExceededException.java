package fr.lbarthon.computorv2.computorv1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DegreeLimitExceededException extends Exception {
    long degree;
}
