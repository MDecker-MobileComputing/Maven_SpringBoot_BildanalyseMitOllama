package de.eldecker.dhbw.spring.tiererkennung;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import de.eldecker.dhbw.spring.tiererkennung.engine.BildTierErkennungsService;


@Service
public class MeinCommandLineRunner implements CommandLineRunner {

	/** Bean, die eigentliche Kommunikation mit Ollama übernimmt. */
	private final BildTierErkennungsService _tierBildTierErkennungsService;

	public MeinCommandLineRunner( BildTierErkennungsService tierBildTierErkennungsService ) {

		_tierBildTierErkennungsService = tierBildTierErkennungsService;
	}

	
	@Override
	public 	void run( String... args ) throws Exception {

		final Resource bildResource = new ClassPathResource( "bilder/2569336.jpg" );
		final String antwort = _tierBildTierErkennungsService.bildErkennungDurchfuehren( bildResource );
		System.out.println( "Antwort KI: " + antwort );
	}
	
}
