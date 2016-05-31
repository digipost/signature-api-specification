## 2016-05-31 – Varsling og arkiv

![Skjermbilde av varsling i virksomhetsadmin](screenshots/2016-05-31-virksomhetsgrensesnitt-varsling.png)

**Denne versjonen bryter bakoverkompabilitet ved opprettelse av portaloppdrag via API.** Se [teknisk](#teknisk) for mer informasjon. Det er per dags dato ingen integrasjoner som benytter berørt funksjonalitet i produksjon. Vi anbefaler at alle som bruker klientbiblioteker oppgraderer til siste versjon så snart som mulig.

### Funksjonelt

* **Langtidsarkivering av signeringsoppdrag.**
  Ved arkivering vil oppdrag oppbevares i virksomhetens akriv i Signeringstjenesten i 50 år. Arkivering skrus på for hver enkelt virksomhet, og vil ikke påvirke eksistserende virksomheter.
* **Varsling av signatarer.** 
  Oppslagstjenesten slår opp kontaktinformasjon for borgere i Difis Kontakt- og reservasjonsregister for oppdrag som skal signeres i portalen. Dette gjøres for alle nyopprettede portaloppdrag, og vil derfor endre oppførselen for eksisterende virksomheter. Se [den funksjonelle dokumentasjonen](funksjonell-spesifikasjon.md#varsling) for mer informasjon om varsling. Endringen er ikke relevant for synkrone signeringsoppdrag.
* **Ikke-sensitiv tittel for oppdrag.**
  Virksomheter kan spesifisere en ikke-sensitiv tittel for signeringsoppdraget. Denne tittelen kan brukes i kontekster der brukeren ikke er sterkt autentisert, som for eksempel i varsler eller kommunikasjon til saksbehandlere hos virksomheten.

### Teknisk

* **Bryter bakoverkompabilitet: endret respons ved polling av status på signeringsoppdrag**.
  `status` i `signature` er endret fra en XML-enum til `string`. `status` kan i fremtiden få nye verdier som vil være ukjent for eksisterende klienter. Klienter som bruker forrige versjon av APIet vil oppleve feil når de mottar responser med nye enum-typer. Ved ukjent signatarstatus skal klienten forholde seg til overordnet signaturstatus, som beskrevet under. *Denne endringen påvirker bare integrasjoner som oppretter portaloppdrag.*
* **Enklere status på oppdrag:** Overordnet status på et signeringsoppdrag kan hentes ut i `status`-feltet på `portal-signature-job-status-change-response`. Mulig status er `IN_PROGRESS`, `COMPLETED_SUCCESSFULLY` eller `FAILED`. `FAILED` er en ny enum-status som ikke eksisterte i forrige versjon av APIet.
