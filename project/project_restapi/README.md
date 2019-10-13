# RESTapi modulen

Dette er en readme for REST-api-et som [Mailprosjektet](../README.md) laget av [gr1922](https://gitlab.stud.idi.ntnu.no/it1901/gr1922/gr1922) bruker.

## REST-api beskrivelse

REST-api-et er en klasse som brukes til � h�ndtere kommunikasjonen mellom UI og domenelogikken til programmet. UI kan bygge HTTP-url-er og sende dette til serveren som tar i mot � videref�rer dette til klassen [AccountService](../project_restapi/src/main/java/project_restapi/AccountService.java). **AccountService** f�r da et kall fra serveren p� riktig metode iht. path-parameteret satt i url-en. 

F�r � gj�re dette har vi brukt **JAX-RS** biblioteket. **JAX-RS** bruker @-annotasjonen for � sette ulike parametere for metodene i tilleg til de vanlige java parameterne man finner i metodenavnet. Dette gj�r av vi kan si at en metode f.eks. er en **GET** eller **POST** i forhold til hvordan vi �nsker at serveren skal kommunisere med objektene som �nsker informasjon. For eksempel kan vi sette **@GET** p� en metode slik at det m� v�re en http get request for at den metoden skal brukes. Videre bruker vi **@PATH** for � videre spesifisere hvilken GET vi �nsker i det tilfellet det er flere metoder med samme annotasjon. Vi kan ogs� spesifisere at vi �nsker � bruke Path delen av requesten som et argument i metoden ved � bruke **@PathParam**. **@CONSUMES** spesifiserer at metoden nettop bruker argumenter som blir sendt med url-en og resultatet av et kall til en metode blir bestemt av **@PRODUCES**. I begge disse to annotasjoenne setter vi **MediaType.APPLICATION_JSON** fordi vi bruker JSON. 

Serveren tilbyr fem metoder i APIet:

* Legge til en account i txt dokumentet.
* Lese inn **Message**-objekter til en en inboks som tilh�rer et **Account**-objekt
* Legge til **Message**-objekt i en gitt account sin inboks, og laste opp denne nye versjonen av inboksen til presistenslaget. 
* Endre messages i inboksen til en account.
* Sjekke om en account er gyldig

## Bygging med Gradle

REST-api-et har avhengigheter som hentes inn av build.gradle i REST-api-et. Dette gjelder Jackson og JAX-RS standarden. 







