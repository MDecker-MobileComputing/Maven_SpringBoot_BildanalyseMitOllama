package de.eldecker.dhbw.spring.tiererkennung.web;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import de.eldecker.dhbw.spring.tiererkennung.engine.BildTierErkennungsService;


/**
 * Controller-Bean mit REST-Endpunkten.
 */
@Controller
@RequestMapping( "/app/v1" )
public class BildAnalyseRestController {

	private final static Logger LOG = LoggerFactory.getLogger( BildAnalyseRestController.class ); 
	
	/** Service-Bean mit eigentlicher KI-Kommunikation. */
	@Autowired
	private BildTierErkennungsService _bildTierErkennung;


	/**
	 * Controller-Methode für HTTP-POST, mit dem Bild zur Analage (Tiere finden)
	 * hochgeladen wird.
	 *
	 * @param bild Hochgeladenes Bild
	 *
	 * @return
	 */
	@PostMapping( value = "/tiersuche", consumes = MULTIPART_FORM_DATA_VALUE )
	public ResponseEntity<String> sucheTiere( @RequestParam("bild") MultipartFile bild ) {

		final int bildBytes = (int) bild.getSize();

		// Antwort absichtlich um 10 Sekunden verzögern
		try {

			Thread.sleep( 10_000 );

		} catch ( InterruptedException ex ) {

			LOG.error( "Fehler während Wartezeit: " + ex.getMessage() ); 					 
		}

		return ResponseEntity.ok(
				"Bildanalyse erfolgreich durchgeführt: " + bildBytes + " Bytes" );
	}
}
