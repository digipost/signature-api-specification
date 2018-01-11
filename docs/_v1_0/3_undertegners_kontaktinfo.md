---
identifier: identifikator-kontaktinfo
title: Undertegners identifikator og kontaktinfo
layout: default
---

Undertegner kan adresseres/identifiseres på ulike vis, basert på om avsender benytter synkron eller asynkron flyt og hvilken informasjon avsender har om undertegner.

### Adressering med fødselsnummer

Dette gir en sikker identifisering, da kun riktig person har mulighet til å åpne og signere dokumentet.

_For [asynkrone signeringsoppdrag](#asynkrone-signeringsoppdrag)_: Undertegner må først identifisere seg ved å logge inn i signeringsportalen med ID-porten, BankID, BankID på mobil eller Buypass. Deretter kan hun åpne og signere dokumentet med f.eks. BankID. BankID vil derfor bli brukt 2 ganger.

_For [synkrone signeringsoppdrag](#synkrone-signeringsoppdrag)_: Avsender er ansvarlig for å sende riktig person til riktig lenke i signeringstjenesten (returneres fra tjenesten ved opprettelse av oppdraget). Undertegner kan deretter åpne og signere dokumentet med f.eks. BankID.

Signerte dokumenter vil inneholde navn på undertegner og hvilken elektronisk ID som ble brukt. Man kan for øvrig velge hvilken [identifikator man ønsker i signerte dokumenter](#undertegners-identifikator).

### Adressering med kontaktinfo for asynkrone oppdrag

_Kun tilgjengelig for private avsendere_.

Denne signeringstypen krever ingen innlogging, og du trenger ikke vite undertegners fødselsnummer. I stedet sender du inn e-postadressen og/eller mobilnummeret som skal motta varsel om signeringen. Se [regler for varselutsending](#regler-for-utsending) for en oversikt over hvilke varsler som sendes til hvilke tidspunkter.

**Du som avsender er ansvarlig for at riktig person åpner og signerer dokumentet.**

Undertegner får en lenke til dokumentet og fyller inn en sikkerhetskode som er oppgitt i varselet. Undertegneren kan deretter åpne og signere dokumentet direkte med BankID, BankID på mobil eller Buypass.

Selve signaturen er like sikker som hvis man logger inn før signering.

Signaturen vil inneholde navn og fødselsdato på undertegner, samt hvilken elektronisk ID som ble brukt.


### Adressering med egenvalgt identifikator for synkrone oppdrag

For synkrone oppdrag er det mulig å benytte en egenvalgt identifikator, f.eks. kundenummer, for å adresse undertegner. Dette er et velegnet alternativ om:

- Avsender ikke kjenner undertegners fødselsnummer, _eller_
- Det er mindre vesentlig nøyaktig _hvem_ som signerer, så lenge det f.eks. er en representant for en spesifikk virksomhet.

Når fødselsnummer ikke benyttes til adressering vil man ikke kunne sikre at personen oppdraget adresseres til faktisk er den som undertegner. En del forretningsprosesser krever ikke denne konfidensialitetssikringen, eller sikker adressering og autentisering av undertegner, og da kan det være praktisk å slippe å kjenne til undertegners fødselsnummer i forkant av signeringen.

Behovet for konfidensialitet kan også være mindre for dokumenter som ikke inneholder personopplysninger.

Når fødselsnummer ikke brukes under adressering vil det heller ikke kunne inkluderes i [signerte dokumenter](#undertegners-identifikator).

_Merk_: I prosesser der det er spesielt viktig å sikre hele prosessen rundt signering og å ha sterk autensitet knyttet til det signerte dokumentet (f.eks. i de tilfeller der man krever avansert e-signatur) vil det normalt ikke være anbefalt å la være å adressere undertegner med fødselsnummer. 
