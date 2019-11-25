package fr.lbarthon.computorv2.exceptions;

/**
 * This exception is thrown in nodes to prevent further calculation,
 * so we can save function statuses and just solve the needed part
 */
public class StopCalculationException extends Exception {
}
