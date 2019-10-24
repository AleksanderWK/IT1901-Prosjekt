# CORE

Dette java-prosjektet inneholder domenelogikken til [Mailprosjektet]() til [gr1922]()

## Domenelaget

Siden dette er et mailprosjekt trenger vi klasser som kan h�ndtere og representere data knyttet til kontoer, meldinger og inboxer. Derfor har vi bygget opp v�rt domenelag rundt klassene Account, Message og Inbox. Account klassen inneholder data om brukernavn, passord og inbox (som blir delegert til inbox-klassen). Account-klassen vil ogs� kunne kommunisere med systemet (presistenslaget), f.eks om kontoen eksisterer eller er gyldig forhold til det som er lagret (dette gj�res ved hjelp av AccountIO). Message klassen er bare en enkelt ikke-muterbar klasse som inneholder data om selve meldingen, emne, fra- og til konto. Inbox er en klasse som represterer en liste med meldinger, og mye av funksjonene er delgert til List-interfacet og ArrayList implementasjonen i Java. Her vil ogs� Inbox-klassen kunne kommunisere med systemet (presistenslaget), f.eks ved � skrive over alle meldingene med inbox-objektet sine meldinger eller bare laste opp en enkel melding.

I tillegg har vi utvidet funksjonaliteten etter en brukerhistorie om at brukeren skal kunne automatisk se alle som har sendt meldinger til brukeren. Dette gj�res da med klassen Contacts, som implementerer InboxListener-interfacet fordi den trenger � lytte p� eventuelle endringer som kan skje i Inbox for � oppdatere kontaktene.

Her er et enkelt klasse diagram av hvordan klassene henger sammen.

```plantuml
class Account {
	String email_address
	String password
}

class Message {
	String subject
	String message
}

class Inbox {
	
}

interface MailReader {
	
}

class InboxIO {

}

class AccountIO {
	
}

interface InboxListener {
	void inboxChanged(List<Message> messages)
	void addedMessage(Message message)
}

class Contacts {
	
}

Account "account" -- "inbox" Inbox
Account *--> "1" AccountIO : "io"

Message *--> Account: "to/from"

Inbox *--> "*" InboxListener: "listeners"
Inbox *--> "*" Message: "messages"
Inbox *--> "1" InboxIO: "io"

Contacts *--> "*" Account: "accounts"
InboxListener <|-- Contacts

MailReader <|-- InboxIO
```


## Presistenslaget

For at v�r mailapplikasjon skal fungere som forventent, m� vi naturligvis kunne lagre meldinger p� en maskin (om det er lokalt eller p� en server) s� de kan leses uavhengig av om man avslutter applikasjonen eller ikke. Dataene som m� lagres er dermed b�de konto data, alts� brukernavn og passord (siden vi har login og logout funksjonalitet), og alt av meldingsdata som ligger i en enekel account sin inbox. Klassene som h�ndtere skriving og lesing er derfor AccountIO og InboxIO. N�r det gjelder InboxIO, vil den ogs� implementere et grensesnitt MailReader, som gj�r av vi kan implementere skrivingen og lesning p� forskjellige m�ter. Vi har to implementasjoner, InboxIO og InboxIOJson, men InboxIO som har vanlig textformat er den som er for �yeblikket i bruk. AccountIO er bare en enkel klasse som skriver og leser brukernavn og passord fra/til fil. 

Vi har i tillegg klasser som h�ndtere serialisering og deserialisering av hovedklassene (Account og Message) v�r til JSON format. Dette er i hovedsak for � kunne enkelt sende data og ta imot data fra et REST-api p� en enkel og forventet m�te. Men, kan ogs� brukes til � gj�re lesing og skriving til filer enklere (selv om vi bruker tekst som ogs� funker greit). Vi har ogs� en CompleteObjectMapper som er en klasse som utvider ObjectMapper, som er den klassen i jackson-biblioteket som faktisk utf�rer konverteringen, og inneholder alle de klassene vi bruker for seralisering/deserialisering. Da kan man til slutt ha alt av seraliserings/deserialiserings funksjonalitet i en klasse slik at det blir enkelt � bare bruke den klassen til � utf�re selve jobben.

## Bygging med Gradle

N�r det kommer til bygging med gradle for core prosjektet er det bare � f� alt av standard java funksjonalitet ordnet. Ekstra avhengigheter er da kun jackson biblioteket for JSON h�ndtering. Ellers er det de andre tingene som g�r igjen i alle prosjekter: testing, testrapportering med jacoco, kodesjekking og rapportering med checkstyles og spotbugs.
