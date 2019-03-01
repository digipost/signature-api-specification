---
identifier: signeringsoppdrag
title: Signeringsoppdrag
layout: default
---

## Opprette signeringsoppdrag

#### Begrensninger

**Antall undertegnere**

Tjenesten tillater maksimalt 10 undertegnere pr. oppdrag.

**Hastighet**

Tjenesten tillater maksimalt 10 API-kall i sekundet per organisasjonsnummer. Hvis en avsender overskrider denne grensen vil API-et returnere `HTTP 429 Too Many Requests`, og avsenderen vil bli blokkert i 30 sekunder.

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
