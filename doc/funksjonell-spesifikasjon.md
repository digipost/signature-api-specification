## Varsling

Signeringstjenesten tilbyr varsel og oppdrag til signering på SMS og e-post. Reglene for utsending av varsler avhenger av signeringsfristen:

| Signeringsfrist |     e-post     |       e-post      |        SMS        |
|:---------------:|:--------------:|:-----------------:|:-----------------:|
| 0-24 timer      | Ved aktivering |         -         | Ved aktivering    |
| 2-3 dager       | Ved aktivering | 1 dag før frist   | 1 dag før frist   |
| 4-5 dager       | Ved aktivering | 2 dager før frist | 1 dag før frist   |
| 6-9 dager       | Ved aktivering | 3 dager før frist | 2 dager før frist |
| 10 dager +      | Ved aktivering | 5 dager før frist | 2 dager før frist |

SMS sendes ikke mellom 22 og 08, med mindre oppdraget opprettes på natten og fristen er så kort at det er nødvendig med umiddelbar utsending.

**Kontaktinformasjon for varsling kan angis på to måter:**

 * Dersom tjenesteeier spesifiserer varslingsinformasjon ved opprettelse av oppdraget vil denne brukes.
 * Dersom varslingsinformasjon ikke spesifiseres, så vil tjenesten hente varslingsinfo fra Kontakt- og reservasjonsregisteret. Dette er kun tilgjengelig for offentlige virksomheter.

**Krav om at kontaktinfo finnes:**

 * Alle signatarer må ha en e-postadresse.
 * Dersom SMS-varsling bestilles for signataren må det finnes et mobilnummer for signataren.
 * Dersom et av disse kravene feiler for en av signatarene i oppdraget, så vil tjenesteeier ved statushenting i API få en beskjed som tilsier at oppdraget ikke kunne sendes på grunn av manglende kontaktinfo. For portaloppdrag vil denne beskjeden komme ved oppretting av oppdraget.

### Bruk av Kontakt- og reservasjonsregisteret

Før et oppdrag gjøres tilgjengelig slås alle signatarene opp i Kontakt- og reservasjonsregisteret. Hvis en eller flere av
signatarene er reservert mot digital kommunikasjon vil oppdraget bli avvist og påfølgende uthenting av status for oppdraget
vil gi en feil med informasjon om hvilke signatar(er) som er reservert. En slik reservasjon vil feile oppdraget selv om
tjenesteeier har spesifisert egen varslingsinfo for signataren, da en reservasjon mot digital kommunikasjon trumfer dette.

* Ved utsending av senere varsler (enten utsatt aktivering på grunn av kjedet signatur eller påminnelser) blir det gjort et nytt oppslag mot registeret for å hente ut den sist oppdaterte kontaktinformasjonen.
* Dersom Oppslagstjenesten for Kontakt- og reservasjonsregisteret er utilgjengelig ved utsending av påminnelser vil resultatet fra oppslaget ved opprettelse av oppdraget bli brukt. 
* **Reservasjon ved utsatte førstegangsvarsler:** I scenariet der tjenesteeier har satt en kjedet rekkefølge på signatarene, og førstegangsvarsel skal sendes til en signatar som i perioden mellom oppdraget ble opprettet og førstegangsvarsel skal sendes har reservert seg mot elektronisk kommunikasjon, så vil hele oppdraget feiles.
* **Reservasjon ved påminnelser:** Hvis sluttbrukeren har reservert seg etter at oppdraget ble opprettet, men oppdraget allerede er aktivert, vil det ikke bli sendt påminnelser (epost/sms), men oppdraget vil heller ikke feile før signeringsfristen eventuelt løper ut.
