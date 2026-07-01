package de.eldecker.dhbw.spring.tiererkennung.engine;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


/**
 * Diese Service-Bean kapselt die Kommunikation mit dem von Ollama bereitgestellten
 * KI-Modell über "Spring AI", siehe auch
 * https://docs.spring.io/spring-ai/reference/api/chat/ollama-chat.html
 * <br><br>
 *
 * Siehe auch die Konfigurationen mit {@code spring.ai.ollama.*} in der Datei
 * {@code src/main/resources/application.properties}.
 */
@Service
public class BildTierErkennungsService {

	private final static Logger LOG = LoggerFactory.getLogger( BildTierErkennungsService.class );

    /** Vorlage für Prompt. */
    private final String PROMPT_TEMPLATE  =
    		"""
    		Identify all animals visible in the provided image and return them as a list.
    		Do not include duplicate animals. If the same animal appears multiple times, list it only once.
    		Only output the list of animals separates by commas, e.g.: dog, elephant.
			Append a confidence value at the end in this exact format: " | confidence: NN%" (0-100).
		    If no animal is detected, output exactly: "No animal detected | confidence: NN%".
    		Do not give explanations.
    		""";

	/** Zentrales API-Objekt für Kommunikation mit von Ollama ausgeführtem KI-Modell. */
	private final ChatClient _chatClient;

	/**
	 * Liste aller erlaubten KI-Modelle laut Datei {@code application.properties}.
	 * Diese müssen laut CLI-Aufruf "ollama list" auch lokal vorhanden sein,
	 * also mit Ollama heruntergeladen worden sein. Das erste Modell in dieser
	 * Liste ist das Default-Modell.
	 */
    @Value( "${eldecker.ollama.modelle}" )
    private List<String> _kiModelleList;

    /** Bean zur Überprüfung, ob übergebenes Bild das JPEG-Format hat. */
    @Autowired
    private JpegChecker _jpegChecker;

    
	/**
	 * Konstruktor: {@ code ChatClient}-Objekt erzeugen.
	 */
	@Autowired
	public BildTierErkennungsService( ChatClient.Builder chatClientBuilder ) {

		_chatClient = chatClientBuilder.build();
	}


	/**
	 * Eigentliche Bildanalyse von Ollama bereitgestellter KI durchführen lassen,
	 * die herausfinden soll, ob im Bild Tiere zu sehen sind.
	 *
	 * @param bildRessouce Bild/Foto, das analysiert werden soll; Achtung:
	 *                     dieses Objekt enthält den Dateinamen, daraus könnte
	 *                     die KI auch Rückschlüsse auf den Bildinhalt ziehen.
	 *
	 * @param kiModell KI-Modell, das für die Analyse verwendet werden soll;
	 *                 es wird eine {@code BildErkennungsException} geworfen,
	 *                 wenn es nicht in {@link #kiModelleList} enthalten ist.
	 *                 Wenn ein leerer String übergeben wird, dann wird das
	 *                 Default-Modell verwendet.
	 *
	 * @return Ergebnis der Analyse (Erkannte Tiere auf Englisch oder "No animal
	 *         detected") sowie Confidence-Wert.
	 *
	 * @throws BildErkennungsException Fehler bei Bilderkennung aufgetreten:
	 *                                 ungültiger Wert für {@code kiModell}
	 *                                 oder {@code bildRessource} ist keine
	 *                                 JPEG-Grafikdatei
	 */
	public String bildErkennungDurchfuehren( Resource bildRessouce, String kiModell )
			throws BildErkennungsException {

		final String kiModellEffektiv = kiModellAuswerten( kiModell ); // throws BildErkennungsException
		
		_jpegChecker.ueberpruefeObJpegDatei( bildRessouce ); // throws BildErkennungsException

		final OllamaChatOptions.Builder chatOptions =
						OllamaChatOptions.builder()
						                 .model( kiModellEffektiv )
				                         .temperature( 0.2 );

		LOG.info( "Analyse von Bild \"{}\" wird mit Modell \"{}\" durchgeführt.",
				  bildRessouce.getFilename(), kiModellEffektiv );

		final String kiAntwortString =
				_chatClient.prompt()
		                   .user(
		                          user -> user.text( PROMPT_TEMPLATE )
		                                      .media(
		                                              IMAGE_JPEG,
		                                        	  bildRessouce
                                                    )
		                         )
		                   .options( chatOptions )
		                   .call()
		                   .content();

		return kiAntwortString;
	}


	/**
	 * KI-Modell bestimmen.
	 *
	 * @param kiModellBezeichner Technischer Name von KI-Modell oder leerer String
	 *                           für Default-Modell
	 *
	 * @return Technischer Name KI-Modell, das für Analyse verwendet wird
	 *
	 * @throws BildErkennungsException Unzulässiges Modell übergeben.
	 */
	private String kiModellAuswerten( String kiModellBezeichner )
										throws BildErkennungsException {

		final String kiModellEffektiv;
		if ( kiModellBezeichner == null || kiModellBezeichner.isBlank() ) {

			kiModellEffektiv = _kiModelleList.get(0);

		} else {

			kiModellEffektiv = kiModellBezeichner;
			final boolean modellZugelassen = _kiModelleList.contains( kiModellBezeichner );
			if ( modellZugelassen == false ) {

				throw new BildErkennungsException(
						"KI-Modell \"" + kiModellBezeichner + "\" nicht zugelassen." );
			}
		}

		return kiModellEffektiv;
	}

}
