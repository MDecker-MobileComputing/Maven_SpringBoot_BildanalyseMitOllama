"use strict";

let formBildHochladen = null;
let inputBild         = null;
let buttonHochladen   = null;


/**
 * Lifecycle-Methode: Wird aufgerufen, sobald die Seite vollständig geladen ist.
 */
document.addEventListener( "DOMContentLoaded", function() {

	formBildHochladen = document.getElementById( "bildUploadForm" );
	inputBild         = document.getElementById( "bildInput"      );
	buttonHochladen   = document.getElementById( "uploadButton"   );

	formBildHochladen.addEventListener( "submit", submitHandler );
});


/**
 * Submit-Eventhandler für Bild-Upload-Formular.
 *
 * @param {SubmitEvent} event Submit-Event
 */
async function submitHandler( event ) {

	event.preventDefault(); // Verhindert das Standardverhalten des Formulars (Seiten-Reload)

	const datei = inputBild.files[0];
	if ( !datei ) {

		alert( "Bitte ein JPEG-Bild auswählen." );
		return;
	}

	const istJpeg = datei.type === "image/jpeg"
					 || datei.name.toLowerCase().endsWith( ".jpg"  )
					 || datei.name.toLowerCase().endsWith( ".jpeg" );
	if ( !istJpeg ) {

		alert( "Es sind nur JPEG-Bilder erlaubt." );
		return;
	}

	const formData = new FormData();
	formData.append( "bild", datei );

	buttonHochladen.disabled = true;

	try {

		const payloadObjekt = {
			method: "POST",
			body: formData
		};

		const response = await fetch( "/app/v1/tiersuche", payloadObjekt );

		const text = await response.text();
		if ( !response.ok ) {

			alert( "Fehler beim Upload: " + text );
			return;
		}

		alert( text );

		// Bild-Input zurücksetzen, damit der gleiche Dateiname erneut hochgeladen werden kann
		inputBild.value = "";

	} catch ( fehler ) {

		alert( "Netzwerkfehler beim Upload: " + fehler.message );

	} finally {

		buttonHochladen.disabled = false;
	}
}