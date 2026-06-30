"use strict";

let formBildHochladen = null;
let inputBild         = null;
let buttonHochladen   = null;
let divErgebnis       = null;
let timerErgebnis     = null;
let sekundenWarten    = 0;


/**
 * Lifecycle-Methode: Wird aufgerufen, sobald die Seite vollständig geladen ist.
 */
document.addEventListener( "DOMContentLoaded", function() {

	formBildHochladen = document.getElementById( "bildUploadForm" );
	inputBild         = document.getElementById( "bildInput"      );
	buttonHochladen   = document.getElementById( "uploadButton"   );
	divErgebnis       = document.getElementById( "divErgebnis"    );

	formBildHochladen.addEventListener( "submit", submitHandler );
});


/**
 * Submit-Eventhandler für Bild-Upload-Formular.
 *
 * @param {SubmitEvent} event Submit-Event
 */
async function submitHandler( event ) {

	event.preventDefault(); // Verhindert das Standardverhalten des Formulars (Seiten-Reload)

	if ( timerErgebnis !== null ) {

		clearInterval( timerErgebnis );
		timerErgebnis = null;
	}
	sekundenWarten = 0;

	divErgebnis.innerHTML = "";

	const datei = inputBild.files[0];
	if ( !datei ) {

		alert( "Bitte ein JPEG-Bild auswählen." );
		return;
	}

	const istJpeg = datei.type === "image/jpeg"
					 || datei.name.toLowerCase().endsWith( ".jpg"  )
					 || datei.name.toLowerCase().endsWith( ".jpeg" );
	if ( !istJpeg ) {

		divErgebnis.innerHTML = "<p>Fehler: Nur JPEG-Bilder sind erlaubt.</p>";
		return;
	}

	const formData = new FormData();
	formData.append( "bild", datei );

	buttonHochladen.disabled = true;
	divErgebnis.innerHTML = "<p>Warte auf Antwort: 0 Sekunden ...</p>";
	timerErgebnis = setInterval( function() {

		sekundenWarten++;
		divErgebnis.innerHTML = "<p>Warte auf Antwort: " + sekundenWarten + " Sekunden ...</p>";
	}, 1000 );

	try {

		const payloadObjekt = {
			method: "POST",
			body: formData
		};

		const response = await fetch( "/app/v1/tiersuche", payloadObjekt );

		const text = await response.text();
		clearInterval( timerErgebnis );
		timerErgebnis = null;
		if ( !response.ok ) {

			divErgebnis.innerHTML = "<p>Fehler beim Upload: " + text + "</p>";
			return;
		}

		divErgebnis.innerHTML = "<p>" + text + "</p>";

		// Bild-Input zurücksetzen, damit der gleiche Dateiname erneut hochgeladen werden kann
		inputBild.value = "";

	} catch ( fehler ) {

		clearInterval( timerErgebnis );
		timerErgebnis = null;

		alert( "Netzwerkfehler beim Upload: " + fehler.message );

	} finally {

		if ( timerErgebnis !== null ) {

			clearInterval( timerErgebnis );
			timerErgebnis = null;
		}
		sekundenWarten = 0;

		buttonHochladen.disabled = false;
	}
}