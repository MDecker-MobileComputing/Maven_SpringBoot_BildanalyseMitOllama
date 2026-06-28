package de.eldecker.dhbw.spring.tiererkennung;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import de.eldecker.dhbw.spring.tiererkennung.engine.BildTierErkennungsService;


@Service
public class MeinCommandLineRunner implements CommandLineRunner {

	/** Bean, die eigentliche Kommunikation mit Ollama übernimmt. */
	@Autowired
	private final BildTierErkennungsService _tierBildTierErkennungsService = null;

	
	@Override
	public 	void run( String... args ) throws Exception {

		//final Resource bildResource = new ClassPathResource( "bilder/2569336.jpg" ); // Katze
		//final Resource bildResource = new ClassPathResource( "bilder/111695.jpg" );  // Elefant
		//final Resource bildResource = new ClassPathResource( "bilder/667460.jpg" );  // VW Käfer
		//final Resource bildResource = new ClassPathResource( "bilder/9295172.jpg" ); // Papagei
		//final Resource bildResource = new ClassPathResource( "bilder/2513178.jpg" ); // Meerschweinchen
		final Resource bildResource = new ClassPathResource( "bilder/2024041.jpg" ); // Gänse (Zeichnung)

		gibBildMetadatenAus( bildResource );
		
		final long zeitpunktStart = System.nanoTime();
		
		final String antwort = 
				_tierBildTierErkennungsService.bildErkennungDurchfuehren( bildResource );
		
		final long zeitpunktEnde = System.nanoTime();
		
		System.out.println( "\nAntwort KI: " + antwort + "\n" );
		
		final long diffNanosekunden = zeitpunktEnde - zeitpunktStart;
		final double sekunden = diffNanosekunden / 1_000_000_000; // Nanosekunden → Sekunden
		System.out.println( "Dauer: " + sekunden + " sek" );
	}


	private void gibBildMetadatenAus( Resource bildResource ) throws IOException {

		final long dateiGroesseBytes   = bildResource.contentLength();
		final double dateiGroesseKByte = dateiGroesseBytes / 1024.0;

		final BufferedImage bufferedImage = leseBild( bildResource );
		final int breite = bufferedImage.getWidth();
		final int hoehe  = bufferedImage.getHeight();

		System.out.printf( "\nBildgroesse: %.1f kByte%n", dateiGroesseKByte );
		System.out.println( "Aufloesung: " + breite + " x " + hoehe + " px\n" );
	}
	
	
	private BufferedImage leseBild( Resource bildResource ) throws IOException {

		final BufferedImage bufferedImage;
		try ( InputStream inputStream = bildResource.getInputStream() ) {

			bufferedImage = ImageIO.read( inputStream );
		}
		if ( bufferedImage == null ) {

			throw new IOException( 
				"Bild konnte nicht gelesen werden: " + bildResource.getFilename() );
		}
		
		return bufferedImage;
	}
	
}
