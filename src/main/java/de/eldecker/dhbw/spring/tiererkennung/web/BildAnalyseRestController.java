package de.eldecker.dhbw.spring.tiererkennung.web;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import de.eldecker.dhbw.spring.tiererkennung.engine.BildErkennungsException;
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
	 * @param kiModell Technischer Name KI-Modell (optional); wenn ein Modell
	 *                 angegeben ist, dieses Modell aber nicht vorhanden ist,
	 *                 dann wird eine Exception geworfen
	 *
	 * @return ResponseEntity mit Text, der an den Client zurückgegeben wird.
	 *
	 * @throws BildErkennungsException Fehler bei Bilderkennung
	 */
	@PostMapping( value = "/tiersuche", consumes = MULTIPART_FORM_DATA_VALUE )
	public ResponseEntity<String> sucheTiere(
              @RequestParam("bild") MultipartFile bild,
			  @RequestParam(value = "kiModell", defaultValue = "") String kiModell
							                ) throws BildErkennungsException {

		LOG.info( "Bild \"{}\" für Analyse erhalten ({} Bytes); Modell=\"{}\".",
				  bild.getOriginalFilename(), bild.getSize(), kiModell );

		final Resource bildAlsResource = bild.getResource();

		final String ergebnis =
				_bildTierErkennung.bildErkennungDurchfuehren( bildAlsResource, 
						                                      kiModell );

		LOG.info( "Analyse-Ergebnis für \"{}\": \"{}\"",
		          bild.getOriginalFilename(), ergebnis );

		return ResponseEntity.ok( ergebnis );
	}
}
