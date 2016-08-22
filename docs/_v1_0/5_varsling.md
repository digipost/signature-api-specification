---
identifier: varsling
title: Varsling
layout: default
---

Signeringstjenesten tilbyr varsel og oppdrag til signering på SMS og e-post. Reglene for utsending av varsler avhenger av signeringsfristen:

| Signeringsfrist |     e-post     |       e-post      |        SMS        |
|:---------------:|:--------------:|:-----------------:|:-----------------:|
| 0-24 timer      | Ved aktivering |         -         | Ved aktivering    |
| 2-3 dager       | Ved aktivering | 1 dag før frist   | 1 dag før frist   |
| 4-5 dager       | Ved aktivering | 2 dager før frist | 1 dag før frist   |
| 6-9 dager       | Ved aktivering | 3 dager før frist | 2 dager før frist |
| 10 dager +      | Ved aktivering | 5 dager før frist | 2 dager før frist |

<!-- Tabellen er generert vha. http://www.tablesgenerator.com/markdown_tables -->

Dersom det ikke finnes en e-postadresse for en signatar vil det sendes SMS istedet for e-post ved aktivering.

SMS sendes ikke mellom 22 og 08, med mindre oppdraget opprettes på natten og fristen er så kort at det er nødvendig med umiddelbar utsending.

**Kontaktinformasjon for varsling kan angis på to måter:**

 * Dersom tjenesteeier spesifiserer varslingsinformasjon ved opprettelse av oppdraget vil denne brukes. Overstyrt varslingsinformasjon får prioritet over Kontakt- og reservasjonsregisteret. **NB: Offentlige virksomheter kan kun overstyre varslingsinformasjonen dersom signataren mottar oppdraget i kraft av en rolle knyttet til sitt arbeid.**
 * Dersom varslingsinformasjon ikke spesifiseres, så vil tjenesten hente varslingsinfo fra Kontakt- og reservasjonsregisteret. Dette er kun tilgjengelig for offentlige virksomheter.

**Krav til kontaktinformasjon:**

 * Alle signatarer må ha minst ha én av e-postadresse og mobilnummer.
 * Sending av SMS er frivillig og kan bestilles av tjenesteeieren.
 * Dersom en signatar har mobilnummer og ikke e-postadresse vil det alltid bli sendt SMS.
 * Tjenesten støtter kun norske mobilnumre. Oppdrag med overstyrt kontaktinformasjon med utenlandsk mobilnummer vil bli avvist, mens utenlandske mobilnumre fra Kontakt- og reservasjonsregisteret vil bli ignorert.

### Bruk av Kontakt- og reservasjonsregisteret

Signeringstjenesten gjør oppslag mot Kontakt- og reservasjonsregisteret ved opprettelse av oppdrag for alle offentlige virksomheter for signatarer uten overstyrt kontaktinformasjon. Hvis signatarer er reservert mot digital kommunikasjon vil oppdraget bli avvist og påfølgende uthenting av status for oppdraget vil gi en feil med informasjon om hvilke signatarer som er reservert. Signatarer med overstyrt kontaktinformasjon bli ikke sjekket for reservasjon.

* Ved utsending av senere varsler (enten utsatt aktivering på grunn av kjedet signatur eller påminnelser) blir det gjort et nytt oppslag mot registeret for å hente ut den sist oppdaterte kontaktinformasjonen.
* Dersom Oppslagstjenesten for Kontakt- og reservasjonsregisteret er utilgjengelig ved utsending av påminnelser vil resultatet fra oppslaget ved opprettelse av oppdraget bli brukt. 
* **Reservasjon ved utsatte førstegangsvarsler:** I scenariet der tjenesteeier har satt en kjedet rekkefølge på signatarene, og førstegangsvarsel skal sendes til en signatar som i perioden mellom oppdraget ble opprettet og førstegangsvarsel skal sendes har reservert seg mot elektronisk kommunikasjon, så vil hele oppdraget feiles.
* **Reservasjon ved påminnelser:** Hvis sluttbrukeren har reservert seg etter at oppdraget ble opprettet, men oppdraget allerede er aktivert, vil det ikke bli sendt påminnelser (epost/sms), men oppdraget vil heller ikke feile før signeringsfristen eventuelt løper ut.