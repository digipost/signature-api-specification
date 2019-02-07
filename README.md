# Posten signering – API spesifikasjon

Her finner du informasjon om APIene til Posten signering.

Dette dokumentet inneholder teknisk dokumentasjon nødvendig for å integrere med tjenesten. Se også den [funksjonelle dokumentasjonen](https://digipost.github.io/signature-api-specification) for funksjonell beskrivelse av tjenesten og [release notes](/integrasjon/release-notes.md) for historikk over hva som er nytt i tidligere versjoner.

Mappen [`schema/xsd`](/schema/xsd) inneholder XSD-filer som spesifiserer de ulike objektene som sendes og mottas av APIet.

## Tre ulike scenarier for signeringsoppdrag

Posten signering håndterer 3 ulike scenarier for signering:

### 1. Synkrone signeringsoppdrag med maskin-til-maskin-integrasjon

Dette scenariet er aktuelt hvis sluttbruker er i en sesjon hos tjenesteeier på tjenesteeiers nettsider. Her ønsker tjenesteeier at sluttbruker skal signere et dokument. Sluttbruker opplever signaturprosessen som en integrert del av tjenesteeiers nettsted.

### 2. Asynkrone signeringsoppdrag med maskin-til-maskin-integrasjon
Dette scenariet er aktuelt der det er ønskelig med signering av dokumenter uten at sluttbruker som skal signere er i en sesjon på tjenesteeierens nettside. Dette kan for eksempel være ved batchutsending av dokumenter som skal signeres, eller for å håndtere et scenario der bruker f.eks. har hatt telefondialog med tjenesteeier.

### 3. Manuelle asynkrone signeringsoppdrag
Dette scenariet er aktuelt dersom Signeringstjenesten skal benyttes av enten en mindre tjenesteeier, eller en avdeling av en større tjenesteeier der den aktuelle prosessen er manuell. Denne flyten gjennomføres utelukkende via signeringsportalen, og man bruker derfor ikke API-er for integrasjonen. Dette er derfor ikke beskrevet nærmere i denne dokumentasjonen.

## Integrasjon med klientbiblioteker

Det finnes klientbiblioteker som forenkler integrasjonsprosessen. Vi anbefaler bruk av disse der det er mulig:

* [Klientbibliotek for Java](https://github.com/digipost/signature-api-client-java)
* [Klientbibliotek for .NET](https://github.com/digipost/signature-api-client-dotnet)

## Manuell integrasjon

Dokumentasjon av manuell integrasjon finner du beskrevet i vår [integrasjonsguide](/integrasjon).


## Releasing (kun for medlemmer av Digipost organisasjonen)

Se docs/systemer/open-source-biblioteker.md
