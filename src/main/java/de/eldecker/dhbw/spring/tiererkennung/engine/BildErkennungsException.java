package de.eldecker.dhbw.spring.tiererkennung.engine;

/**
 * Eigene Exception-Klasse.
 */
@SuppressWarnings("serial")
public class BildErkennungsException extends Exception {

	public BildErkennungsException( String fehlertext ) {
		
		super( fehlertext );
	}
}
