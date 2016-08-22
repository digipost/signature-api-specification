---
identifier: signeringsoppdrag
title: Signeringsoppdrag
layout: default
---

Et signeringsoppdrag (ev. signeringsjobb) er en jobb som inneholder et dokument som skal signeres, og kan adresseres til en eller flere mottakere som skal signere (signatarer). Tjenesten tilbyr to ulike typer signeringsoppdrag hvor _måten de signeres på_ er den vesentlige forskjellen.

## Synkrone signeringsoppdrag

Et synkront oppdrag signeres når signatar allerede er pålogget i avsenders system. Disse oppdragene passer for avsendere som ønsker at sluttbrukerne skal oppleve signeringsprosessen som en integrert del av deres nettsted.

Flyten ser typisk slik ut:

1. Sluttbruker fyller ut et skjema e.l. i avsenders tjeneste
1. Avsender oppretter et oppdrag i signeringstjenesten maskinelt
1. Sluttbruker blir sendt til signeringstjenesten og gjennomfører signeringssermonien
1. Sluttbruker blir sendt tilbake til avsenders tjeneste
1. Avsender laster ned [signatur](#signerte_dokumenter) og tilbyr en kopi av PAdES til sluttbruker

## Asynkrone signeringsoppdrag

Et asynkront oppdrag signeres i Postens signeringsportal.

Flyten ser typisk slik ut:

1. Avsender oppretter et oppdrag maskinelt eller i avsenderportalen
1. Signeringstjenesten varsler signatar på e-post (og ev. SMS om spesifiert ved [opprettelse](#opprette-signeringsoppdrag)
1. Sluttbruker logger inn på signeringsportalen og gjennomfører signeringssermonien
1. Sluttbruker laster ned [signert kopi](#signerte_dokumenter) av dokumentet
1. Sluttbruker logger ut av signeringsportalen
1. Avsender laster ned [signatur](#signerte_dokumenter)


## Opprette signeringsoppdrag

Ved opprettelse av signeringsoppdrag kan følgende felter oppgis:

|                         | synkrone oppdrag | asynkrone oppdrag |   |
|-------------------------|:----------------:|:-----------------:|---|
| Dokument                | __Obligatorisk__ | __Obligatorisk__  |   |
| Mottaker(e)             | __Obligatorisk__ | __Obligatorisk__  |   |
| Tittel                  | __Obligatorisk__ | __Obligatorisk__  |   |
| Melding til mottaker(e) | Valgfritt        | Valgfritt         |   |
| Aktiveringstidspunkt    | _Ikke relevant_  | Valgfritt         |   |
| Levetid                 | _Ikke relevant_  | Valgfritt         |   |
| E-postadresse           | _Ikke relevant_  | __Obligatorisk__  | se [varsling](#varsling) |
| Mobilnummer             | _Ikke relevant_  | Valgfritt         | se [varsling](#varsling) |
| Rekkefølge              | _Ikke relevant_  | Valgfritt         | se [kjedet signatur](#kjedet-signatur) |

#### Begrensninger

**Antall signatarer**

Tjenesten tillater maksimalt 10 signatarer pr. oppdrag.

**Dokumentformater**

Tjenesten støtter dokumenter av ren tekst (`.txt`) og PDF. Filen kan maksimalt være 3 MB (`3 145 728 bytes`) stor.

I [PAdESen](#signerte_dokumenter) vil dokumentet alltid presenteres i A4- og portrett-format. For best resultat anbefales det at det innsendte dokumentet også har dette formatet.

Passordbeskyttede dokumenter (begrenset lese- og/eller skrive-tilgang) er ikke støttet av tjenesten.

__Aktiveringstidspunkt__

Angir tidspunkt for når signeringsoppdraget skal tilgjengeliggjøres for signataren(e). Dersom aktiveringstidspunktet er i fortiden, blir oppdraget tilgjengelig øyeblikkelig etter opprettelse.

For [kjedete signeringsoppdrag](#kjedet-signatur) gjelder aktiveringstidspunktet for _første gruppe_.

_Standardverdi:_ øyeblikkelig etter opprettelse

__Oppdragets levetid__

Angir hvor lenge _etter aktivering_ et signeringsoppdrag er tilgjengelig for signatar før det utløper. Kan maksimalt være 90 dager etter aktivering.

For [kjedete signeringsoppdrag](#kjedet-signatur) gjelder levetiden for _hver gruppe_, slik at alle signatarer får like mye tid på seg til å signere. 

_Standardverdi:_ 30 dager etter aktivering

## Kansellere signeringsoppdrag

_Kansellering av signeringsoppdrag er bare relevant for signeringsoppdrag som signeres i signeringsportalen, dvs. asynkrone oppdrag._

Et signeringsoppdrag kan på et hvilket som helst tidspunkt kanselleres av avsender, så lenge ikke oppdraget allerede er fullført. Kansellerte oppdrag blir utilgjengeliggjort for signatarer som enda ikke har signert.

Hvis minst én signatar har signert på et oppdrag som senere blir kansellert kan avsender laste ned [signaturene](#signerte_dokumenter) som finnes på kanselleringstidspunktet.