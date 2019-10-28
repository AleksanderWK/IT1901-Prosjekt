# Modul for web-serveren

Dette prosjektet inneholder web-serveren til REST-api-et for [Mailprosjektet](../readme.md)

## Web-serveren

For � realisere rest-api'et trenger vi en web-server. Vi har valgt � bruke Jersey, som implementerer JAX-RS-Standarden, p� samme m�te som simpleexample2.

Siden implementasjonen av JAX-RS bare tar h�nd om � enklere koble sammen vanlig java-metoder til HTTP kall som GET, POST, PUT osv., m� vi ogs� ha noe som faktisk implementerer en HTTP server for oss. Her har vi igjen fulgt simpleexample2 og bruker Grizzly for � gj�re dette.

Ved � bruke disse ferdilagde l�sningene for serveren blir koden v�r s�rdeles kort og enkel (ca. 2 filer med 20 linjer). Klassene gjenspeiler ogs� stort sett hvordan det gj�res i simpleexample2, men vi har simplifisert det for v�rt form�l, med de to f�lgende klassene:
* **Config** - Dette er en enkel klasse som sier hva slags ressurser som skal v�re med. For oss blir det (AccountService fra rest-api og ting reltarert til JSON format).
* **GrizzlyApp** - Denne vil sette opp serveren med enkel oppstartskode og bruker en instans av Config til � sette opp api-et.

## Bygging med Gradle

N�r det kommer til bygging med gradle for serveren prosjektet er det viktig � f� alt av avhengigheter fra jersey og girzzly til � fungere riktig. Dette gjenspeiler igjen mye av det som er gjort i simpleexample2. I tillegg har jo serveren avhengigheter fra project_api underprosjektet, s� det er ogs� tatt med. Ekstra avhengigheter er da kun jackson biblioteket for JSON h�ndtering. Ellers er det de andre tingene som g�r igjen i alle prosjekter: testing, testrapportering med jacoco, kodesjekking og rapportering med checkstyles og spotbugs.
