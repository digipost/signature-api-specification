---
identifier: signeringsoppdrag
title: Signeringsoppdrag
layout: default
---

Et signeringsoppdrag (ev. signeringsjobb) er en jobb som inneholder et dokument som skal signeres, og kan adresseres til en eller flere undertegnere som skal signere. Tjenesten tilbyr to ulike typer signeringsoppdrag hvor _måten de signeres på_ er den vesentlige forskjellen.

## Synkrone signeringsoppdrag

Et synkront oppdrag signeres når undertegner allerede er pålogget i avsenders system. Disse oppdragene passer for avsendere som ønsker at sluttbrukerne skal oppleve signeringsprosessen som en integrert del av deres nettsted.

Flyten ser typisk slik ut:

1. Undertegner er innlogget i avsenders tjeneste, og utfører en prosess der, f.eks. utfylling av et skjema.
1. Avsender oppretter et oppdrag i signeringstjenesten maskinelt
1. Undertegner blir sendt til signeringstjenesten og gjennomfører signeringssermonien
1. Undertegner blir sendt tilbake til avsenders tjeneste
1. Avsender laster ned [signatur](#signerte_dokumenter) og tilbyr en kopi av det signerte dokumentet til undertegner

## Asynkrone signeringsoppdrag

Et asynkront oppdrag signeres i Postens signeringsportal.

Flyten ser typisk slik ut:

1. Avsender oppretter et oppdrag maskinelt eller i avsenderportalen
1. Signeringstjenesten varsler undertegner på e-post (og ev. SMS om spesifiert ved [opprettelse](#opprette-signeringsoppdrag)
1. Undertegner logger inn på signeringsportalen og gjennomfører signeringssermonien
1. Undertegner laster ned [signert kopi](#signerte_dokumenter) av dokumentet
1. Undertegner logger ut av signeringsportalen
1. Avsender laster ned [signatur](#signerte_dokumenter)


## Opprette signeringsoppdrag

Ved opprettelse av signeringsoppdrag kan følgende felter angis:

|                         | synkrone oppdrag | asynkrone oppdrag |   |
|-------------------------|:----------------:|:-----------------:|---|
| Dokument                | __Obligatorisk__ | __Obligatorisk__  |   |
| Undertegner(e)          | __Obligatorisk__ | __Obligatorisk__  | se [undertegners kontaktinfo](#kontaktinfo) |
| Tittel                  | __Obligatorisk__ | __Obligatorisk__  |   |
| Signaturtype            | Valgfritt        | Valgfritt         | se [signaturtype](#signaturtype) |
| Sikkerhetsnivå          | Valgfritt        | Valgfritt         | se [sikkerhetsnivå](#sikkerhetsniv) |
| Melding til mottaker(e) | Valgfritt        | Valgfritt         |   |
| Undertegners identifikator | Valgfritt     | Valgfritt         | se [undertegners identifikator](#undertegners-identifikator) |
| Aktiveringstidspunkt    | _Ikke overstyrbar_<sup>[1](#fotnote-aktivering)</sup> | Valgfritt | |
| Levetid                 | _Ikke overstyrbar_<sup>[2](#fotnote-levetid)</sup> | Valgfritt | |
| E-postadresse           | _Ikke relevant_  | __Obligatorisk__  | se [varsling](#varsling) |
| Mobilnummer             | _Ikke relevant_  | Valgfritt         | se [varsling](#varsling) |
| Rekkefølge              | _Ikke relevant_  | Valgfritt         | se [kjedet signatur](#kjedet-signatur) |

#### Begrensninger

**Antall undertegnere**

Tjenesten tillater maksimalt 10 undertegnere pr. oppdrag.

**Hastighet**

Tjenest tillater maksimalt 10 API-kall i sekundet. Hvis en avsender overskrider denne grensen vil API-et returnere `HTTP 429 Too Many Requests`, og avsenderen vil bli blokkert i 30 sekunder.

**Dokumentformater**

Tjenesten støtter dokumenter av typen ren tekst (`.txt`) og PDF (`.pdf`). Både PDF og PDF/A aksepteres av tjenesten. Det signerte dokumentet vil være av samme type som originaldokumentet.
Et originaldokument som er PDF/A gir et signert PAdES-dokument som er PDF/A, og et originaldokument som er PDF versjon 1.1 – 1.7 gir et signert PAdES-dokument som er PDF versjon 1.7.
For PDF/A vil tjenesten alltid produsere signerte PAdES-dokumenter av typen PDF/A-3b, uavhengig av PDF/A-versjon og -konformitetsnivå (_conformance level_) på originaldokumentet.

For arkivering av signerte dokumenter anbefaler vi å bruke originaldokumenter av typen PDF/A. Dette er et krav hvis det signerte dokumentet skal avleveres til Riksarkivet.

Filen kan maksimalt være 3 MB (`3 145 728 bytes`) stor. PDF-versjoner som støttes er PDF 1.1-1.7.

I [PAdES](#signerte_dokumenter) vil dokumentet alltid presenteres i A4- og portrett-format. For best resultat anbefales det at det innsendte dokumentet også har dette formatet.

Passordbeskyttede dokumenter (begrenset lese- og/eller skrive-tilgang) er ikke støttet av tjenesten.

__Aktiveringstidspunkt__

Angir tidspunkt for når signeringsoppdraget skal tilgjengeliggjøres for undertegner(e). Dersom aktiveringstidspunktet er i fortiden, blir oppdraget tilgjengelig øyeblikkelig etter opprettelse.

For [kjedete signeringsoppdrag](#kjedet-signatur) gjelder aktiveringstidspunktet for _første gruppe_.

<a name="fotnote-aktivering"><sup>1</sup></a> Synkrone signeringsoppdrag blir alltid aktivert øyeblikkelig etter opprettelse

_Standardverdi:_ øyeblikkelig etter opprettelse

__Oppdragets levetid__

Angir hvor lenge _etter aktivering_ et signeringsoppdrag er tilgjengelig for undertegner før det utløper. Kan maksimalt være 90 dager etter aktivering.

For [kjedete signeringsoppdrag](#kjedet-signatur) gjelder levetiden for _hver gruppe_, slik at alle undertegnere får like mye tid på seg til å signere.

<a name="fotnote-levetid"><sup>2</sup></a> Synkrone signeringsoppdrag har alltid 30 dagers levetid for å unngå at et dokument blir signert uhensiktsmessig lenge etter opprettelsen av oppdraget. Eventuell frist fra avsenders perspektiv må kommuniseres og håndteres i avsenders tjenester.

_Standardverdi:_ 30 dager etter aktivering

## Kansellere signeringsoppdrag

_Kansellering av signeringsoppdrag er bare relevant for signeringsoppdrag som signeres i signeringsportalen, dvs. asynkrone oppdrag._

Et signeringsoppdrag kan på et hvilket som helst tidspunkt kanselleres av avsender, så lenge ikke oppdraget allerede er fullført. Kansellerte oppdrag blir utilgjengeliggjort for undertegnere som enda ikke har signert.
