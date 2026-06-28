package de.eldecker.dhbw.spring.tiererkennung.engine;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
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

    /** 
     * Vorlage für Prompt.
     */
    private final String PROMPT_TEMPLATE  = 
    		"""
    		Identify all animals visible in the provided image and return them as a list. 
    		Do not include duplicate animals. If the same animal appears multiple times, list it only once.
    		Only output the list of animals separates by commas, e.g.: dog, elephant.
    		Do not give explanations.
    		When no animal is detected write "No animal detected".
    		""";
	
    
	/** Zentrales API-Objekt für Kommunikation mit von Ollama ausgeführtes KI-Modell. */
	private final ChatClient _chatClient;

	
	/**
	 * ChatClient-Objekt erzeugen.
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
	 *                     die KI auch Rückschlüsse auf den Bildinhalt ziehen
	 * 
	 * @return Ergebnis der Analyse (Erkannte Tiere auf Englisch oder "No animal 
	 *         detected")
	 */
	public String bildErkennungDurchfuehren( Resource bildRessouce ) {
		
		final String kiAntwortString = _chatClient.prompt()
		                                          .user( 
		                                        		user -> user.text( PROMPT_TEMPLATE )
		                                        		            .media( IMAGE_JPEG, bildRessouce )
		                                        	   )
		                                          .call()
		                                          .content();
		return kiAntwortString;
	}
	
}
