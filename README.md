# Bildanalyse mit *Spring AI* und Ollama #

<br>

Dieses Repo enthält eine Spring-Boot-Anwendung, die mit [Spring AI](https://spring.io/projects/spring-ai)
eine Bildanalyse (Erkennung von Tieren) durchführt. Hierzu wird das Bild zusammen mit einem Prompt
an ein von [Ollama](https://ollama.com/) lokal ausgeführtes KI-Modell übergeben, das im Bild Tiere
erkennen soll.

<br>

----

## Bilder von Pixabay im Repository ##

<br>

Die folgenden Bilder von [Pixabay](https://pixabay.com/) sind im Unterordner
[bilder/](bilder/) enthalten.

<br>

**Tiere:**

* [Katze auf Stuhl](https://pixabay.com/photos/animal-cat-feline-mammal-pet-2569336/)
  von Nutzer [karishea](https://pixabay.com/users/karishea-10087552/).

* [Elefant](https://pixabay.com/photos/elephant-animal-zoo-111695/)
  von Nutzer [kikatani](https://pixabay.com/users/kikatani-35407/).

* [Papagei (Blaustirnamazone)](https://pixabay.com/photos/parrot-bird-macaw-plumage-amazon-9295172/)
  von Nutzer [DavidClode](https://pixabay.com/users/davidclode-43394210/).

* [Meerschweinchen](https://pixabay.com/photos/guinea-pig-the-animal-hamster-hair-2513178/)
  von Nutzer [Ajale](https://pixabay.com/users/ajale-1481387/).

* [Zwei Gänse (Zeichnung)](https://pixabay.com/vectors/animal-animals-farm-geese-goose-2024041/)
  von Nutzer [OpenClipart-Vectors](https://pixabay.com/users/openclipart-vectors-30363/).

* [Bremer Stadtmusikanten (Fotomontage)](https://pixabay.com/photos/pile-animals-fairy-tale-1651945/)
  von Nutzer [TeeFarm](https://pixabay.com/users/teefarm-199315/).

* [Pfeilgiftfrosch](https://pixabay.com/photos/nature-wildlife-amphibian-frog-3333637/)
  von Nutzer [Xaya](https://pixabay.com/users/xaya-258703/).

* [Basilisk (Jesus-Christus-Echse)](https://pixabay.com/photos/basilisk-lizard-animal-reptile-5951351/)
  von Nutzer [YanCabrera](https://pixabay.com/users/yancabrera-3521614/).

* [Gummi-Enten](https://pixabay.com/photos/duck-meet-ducks-rubber-ducks-4818719/)
  von Nutzer [manfredrichter](https://pixabay.com/users/manfredrichter-4055600/).

* [Hund (Airedale Terrier)](https://pixabay.com/de/photos/airedale-terrier-gl%C3%BCcklich-strand-1875288/)
  von Nutzer [airedalelover](https://pixabay.com/de/users/airedalelover-3879600/).

* [Nasenaffe](https://pixabay.com/photos/proboscis-monkey-primate-monkey-2422095/)
  von Nutzer [pen_ash](https://pixabay.com/users/pen_ash-5526837/).

* [Blaufußtölpel](https://pixabay.com/photos/ecuador-blue-footed-boobies-wildlife-4259426/)
  von Nutzer [hbieser](https://pixabay.com/users/hbieser-343207/).

* [Vogel (Wiedehopf)](https://pixabay.com/photos/bird-hoopoe-feathers-plumage-5918935/)
  von Nutzer [BarbeeAnne](https://pixabay.com/users/barbeeanne-516629/).

* [Katze in Schachtel](https://pixabay.com/photos/cat-amazon-box-siberian-cat-6029230/)
  von Nutzer [lorivanv](https://pixabay.com/users/lorivanv-20160681/).

* [Einäugiges Monster](https://pixabay.com/illustrations/cute-monster-cartoon-character-9864872/)
  von Nutzer [izafi](https://pixabay.com/users/izafi-5325907/).

<br>

**Keine Tiere:**

* [Drehstuhl](https://pixabay.com/illustrations/ai-generated-chair-office-chair-8895614/)
  von Nutzer [geralt](https://pixabay.com/users/geralt-9301/).

* [Auto (VW Käfer)](https://pixabay.com/photos/vw-beetle-yellow-beetle-667460/)
  von Nutzer [congerdesign](https://pixabay.com/users/congerdesign-509903/).

<br>

Die Bilder wurden in der kleinsten verfügbaren Auflösung heruntergeladen, um die Laufzeit
der KI-Verarbeitung möglichst gering zu halten.

<br>

----

## License ##

<br>

See the [LICENSE file](LICENSE.md) for license rights and limitations (BSD 3-Clause License).

<br>