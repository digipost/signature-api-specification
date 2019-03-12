---
identifier: varsling
title: Varsling
layout: default
---

### Bruk av Kontakt- og reservasjonsregisteret

Signeringstjenesten gjør oppslag mot Kontakt- og reservasjonsregisteret ved opprettelse av oppdrag for alle offentlige virksomheter for undertegnere uten overstyrt kontaktinformasjon. Hvis undertegnere er reservert mot digital kommunikasjon vil oppdraget bli avvist og påfølgende uthenting av status for oppdraget vil gi en feil med informasjon om hvilke undertegnere som er reservert. Undertegnere med overstyrt kontaktinformasjon bli ikke sjekket for reservasjon.

* Ved utsending av senere varsler (enten utsatt aktivering på grunn av kjedet signatur eller påminnelser) blir det gjort et nytt oppslag mot registeret for å hente ut den sist oppdaterte kontaktinformasjonen.
* Dersom Oppslagstjenesten for Kontakt- og reservasjonsregisteret er utilgjengelig ved utsending av påminnelser vil resultatet fra oppslaget ved opprettelse av oppdraget bli brukt. 
* **Reservasjon ved utsatte førstegangsvarsler:** I scenariet der tjenesteeier har satt en kjedet rekkefølge på undertegnerne, og førstegangsvarsel skal sendes til en undertegner som i perioden mellom oppdraget ble opprettet og førstegangsvarsel skal sendes har reservert seg mot elektronisk kommunikasjon, så vil hele oppdraget feile.
* **Reservasjon ved påminnelser:** Hvis sluttbrukeren har reservert seg etter at oppdraget ble opprettet, men oppdraget allerede er aktivert, vil det ikke bli sendt påminnelser (e-post/SMS), men oppdraget vil heller ikke feile før signeringsfristen eventuelt løper ut.
