"use strict";

let formBildHochladen = null;
let inputBild         = null;
let inputKiModell     = null;
let buttonHochladen   = null;
let buttonReset       = null;
let divErgebnis       = null;
let divWartezeit      = null;
let imgBild           = null;
let bildVorschauUrl   = null;

let timerErgebnis     = null;
let sekundenWarten    = 0;


/**
 * Lifecycle-Methode: Wird aufgerufen, sobald die Seite vollständig geladen ist.
 */
document.addEventListener( "DOMContentLoaded", function() {

	formBildHochladen = document.getElementById( "bildUploadForm" );
	inputBild         = document.getElementById( "bildInput"      );
	inputKiModell     = document.getElementById( "kiModellInput"  );
	buttonHochladen   = document.getElementById( "uploadButton"   );
	buttonReset       = document.getElementById( "resetButton"    );
	divErgebnis       = document.getElementById( "divErgebnis"    );
	divWartezeit      = document.getElementById( "divWartezeit"   );
	imgBild           = document.getElementById( "bild"           );

	formBildHochladen.addEventListener( "submit", onAnalyseButtonClick );

	buttonReset.addEventListener( "click", onResetButtonClick );
});


/**
 * Event-handler für Klick auf den Reset-Button.
 */
function onResetButtonClick() {

	sekundenWarten = 0;

	divWartezeit.innerHTML = "";
	divErgebnis.innerHTML  = "";

	resetBildVorschau();
}

/**
 * Event-Handler für Button "Analyse starten".
 *
 * @param {SubmitEvent} event Submit-Event
 */
async function onAnalyseButtonClick( event ) {

	event.preventDefault(); // Verhindert das Standardverhalten des Formulars (Seiten-Reload)

	if ( timerErgebnis !== null ) {

		clearInterval( timerErgebnis );
		timerErgebnis = null;
	}

	onResetButtonClick();

	const datei = inputBild.files[0];
	if ( !datei ) {

		divErgebnis.innerHTML =
			"<p><span class=\"fett\">Fehler:</span> Bitte ein JPEG-Bild auswählen.</p>";

		return;
	}

	const istJpeg = datei.type === "image/jpeg"
					 || datei.name.toLowerCase().endsWith( ".jpg"  )
					 || datei.name.toLowerCase().endsWith( ".jpeg" );
	if ( !istJpeg ) {

		divErgebnis.innerHTML =
			"<p><span class=\"fett\">Fehler:</span> Nur JPEG-Bilder sind erlaubt.</p>";
		return;
	}

	zeigeBild( datei );

	const formData = new FormData();
	formData.append( "bild"    , datei               );
	formData.append( "kiModell", inputKiModell.value );

	buttonHochladen.disabled = true;
	buttonReset.disabled     = true;

	zeigeWartezeit();

	timerErgebnis = setInterval( function() {

		sekundenWarten++;
		zeigeWartezeit();
	}, 1000 ); // 1 Sekunde = 1000 Millisekunden


	try {

		const payloadObjekt = {
			method: "POST",
			body  : formData
		};

		const response = await fetch( "/app/v1/tiersuche", payloadObjekt );

		if ( !response.ok ) {

			divErgebnis.innerHTML =
				"<p><span class=\"fett\">Fehler beim Upload:</span> " + text + "</p>";
			return;
		}

		const text = await response.text();

		divErgebnis.innerHTML =
			"<p><span class=\"fett\">Analyse-Ergebnis:</span> " + text + "</p>";

	} catch ( fehler ) {

		divErgebnis.innerHTML =
			"<p><span class=\"fett\">Fehler:</span> Netzwerkfehler beim Upload: " +
			fehler.message + "</p>";

	} finally {

		if ( timerErgebnis !== null ) {

			clearInterval( timerErgebnis );
			timerErgebnis = null;
		}
		sekundenWarten = 0;

		buttonHochladen.disabled = false;
		buttonReset.disabled     = false;
	}
}


/**
 * Zeigt die aktuelle Wartezeit in Sekunden an.
 */
function zeigeWartezeit() {

	divWartezeit.innerHTML =
		`<p><span class=\"fett\">Dauer Bildanalyse:</span> ${sekundenWarten} Sekunden</p>`;
}


/**
 * Evtl. angezeigtes Bild verschwinden lassen.
 */
function resetBildVorschau() {

	if ( bildVorschauUrl !== null ) {

		URL.revokeObjectURL( bildVorschauUrl );
		bildVorschauUrl = null;
	}

	imgBild.removeAttribute( "src" );
	imgBild.style.display = "none";
}


/**
 * Zeigt das ausgewählte Bild im IMG-Element an.
 *
 * @param {File} datei Ausgewählte Bilddatei
 */
function zeigeBild( datei ) {

	resetBildVorschau();

	bildVorschauUrl       = URL.createObjectURL( datei );
	imgBild.src           = bildVorschauUrl;
	imgBild.style.display = "block";
}
