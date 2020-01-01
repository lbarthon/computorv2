package fr.lbarthon.computorv2.exceptions;

import lombok.AllArgsConstructor;

/**
 * Created by Louis on 01/01/2020 for computorv2.
 */
@AllArgsConstructor
public class AssignationException extends Exception {
    private String message;

    @Override
    public String getMessage() {
        return this.message;
    }
}
