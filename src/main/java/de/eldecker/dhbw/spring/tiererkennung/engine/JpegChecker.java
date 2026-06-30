package de.eldecker.dhbw.spring.tiererkennung.engine;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Hilfs-Bean, die unter Verwendung von <i>Apache Tika</i> überprüft,
 * ob eine übergebene Datei ein JPEG-Bild ist.
 */
@Component
public class JpegChecker {

    /** Objekt für Bestimmung MIME-Type von Grafikdatei (Apache Tika). */
    private final Tika _tika = new Tika();


    /**
     * Überprüft, ob die übergebene Datei ein JPEG-Bild ist.
     *
     * @param resource Übergebene Datei, die ein JPEG-Bild sein soll
     *
     * @throws BildErkennungsException Keine Datei übergeben, Datei konnte nicht gelesen werden
     *                                 oder ungültiger Dateityp (nicht image/jpeg)
     */
    public void ueberpruefeObJpegDatei( Resource resource )
    							throws BildErkennungsException {

        if ( resource == null ) {

            throw new BildErkennungsException( "Keine Datei übergeben." );
        }

        try ( InputStream inputStream = resource.getInputStream() ) {

            final String mimeType = _tika.detect( inputStream, resource.getFilename() );
            if ( "image/jpeg".equalsIgnoreCase( mimeType ) == false ) {

                throw new BildErkennungsException(
                        "Ungültiger Dateityp: Erwartet image/jpeg, gefunden: " + mimeType );
            }

        } catch ( IOException ex ) {

            throw new BildErkennungsException(
                    "Datei konnte nicht gelesen werden: " + ex.getMessage() );
        }
    }
}
