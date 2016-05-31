## 2016-05-31 – Varsling og arkiv

**Denne versjonen bryter bakoverkompabilitet ved opprettelse av portaloppdrag via API.** Se [teknisk](#teknisk) for mer informasjon.

### Funksjonelt

* **Langtidsarkivering av signeringsoppdrag.**
  Ved arkivering vil oppdrag oppbevares i virksomhetens akriv i Signeringstjenesten i 50 år. Arkivering skrus på for hver enkelt virksomhet, og vil ikke påvirke eksistserende virksomheter.
* **Varsling av signatarer.** 
  Oppslagstjenesten slår opp kontaktinformasjon for borgere i Difis Kontakt- og reservasjonsregister for oppdrag som skal signeres i portalen. Dette gjøres for alle nyopprettede portaloppdrag, og vil derfor endre oppførselen for eksisterende virksomheter. Se [den funksjonelle dokumentasjonen](funksjonell-spesifikasjon.md#varsling) for mer informasjon om varsling. Endringen er ikke relevant for synkrone signeringsoppdrag.
* **Ikke-sensitiv tittel for oppdrag.**
  Virksomheter kan spesifisere en ikke-sensitiv tittel for signeringsoppdraget. Denne tittelen kan brukes i kontekster der brukeren ikke er sterkt autentisert, som for eksempel i varsler eller kommunikasjon til saksbehandlere hos virksomheten.

### Teknisk

* **Bryter bakoverkompabilitet: endret API ved polling av status på signeringsoppdrag**.
  `status` i `signature` er endret fra en XML-enum til en `string`. `status` kan i fremtiden få verdier eksisterende klienter ikke håndterer, så klienter må støtte at dette feltet kan ha ukjente verdier. Overordnet status på oppdraget kan hentes ut i `status`-feltet på `portal-signature-job-status-change-response`, som fortsatt er en enum som kan ha verdiene `IN_PROGRESS`, `COMPLETED_SUCCESSFULLY` eller `FAILED`. Merk at `FAILED` er en ny enum-status her. Denne endringen påvirker bare integrasjoner som oppretter portaloppdrag.
