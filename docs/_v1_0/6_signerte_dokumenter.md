---
identifier: signerte_dokumenter
title: Signerte dokumenter
layout: default
---

Tjenesten tilgjengeliggjør signaturer i to formater når et dokument har blitt signert – [PAdES](#pades) og [XAdES](#xades). Det vil maksimalt finnes én PAdES for et signeringsoppdrag, mens det vil finnes én XAdES _per undertegner som har signert_.

PAdES er tilgjengelig når minst én undertegner har signert og vil inneholde de signaturene som er gjennomført for oppdraget i nedlastningsøyeblikket. XAdES er tilgjengelig fra det øyeblikket undertegner har signert dokumentet.

Alle dokumenter kan lastes ned i en periode etter at signeringsoppdraget er fullført. Levetiden er avhengig av om [langtidslagring](#langtidslagring) er aktivert for avsenderen.

## Undertegners identifikator

Under opprettelse av signeringsoppdrag kan avsender velge hvordan undertegnerne skal identifiseres i de signerte dokumentene.
Dette kan for eksempel være nyttig hvis dokumentet skal signeres av flere enn én person og du ikke ønsker å utlevere fødselsnumrene til undertegnerne. Avsender velger å inkludere én av følgende identifikatorer i signerte dokumenter:

1. Navn og fødselsnummer (standardverdi)
2. Navn og fødselsdato
3. Navn

Følgende tabell viser hvilke valg som er gyldige for hhv. offentlige og private avsendervirksomheter når det innhentes [avanserte eller autentiserte signaturer](#autentisert-og-avansert-e-signatur):

|             | Privat | Offentlig |
|-------------|--------|-----------|
| Avansert    | 1, 2   | 1         |
| Autentisert | N/A    | 1, 3      |

_Merk_: Alternativ 1 kan kun benyttes om fødselsnummer er brukt for å identifiseres undertegner ved opprettelse av oppdraget. Se for øvrig avsnittet om [undertegners identifikator og kontaktinfo](#identifikator-kontaktinfo)

Hvis du utelater fødselsnummer i de signerte dokumentene kan vi ikke påvise identiteten med 100 % sikkerhet.<sup>[1](#fotnote-e-id-identifikator)</sup> Vi kan likevel i de aller fleste tilfeller oppnå tilstrekkelig beviskraft, på bakgrunn av konteksten signeringen skjer i.

Sannsynligheten er for eksempel svært liten for at 2 personer med navn Kari Olsen signerer en lærekontrakt med Lærlingebedrift AS på eksakt samme tidspunkt. I tillegg vil tekniske spor (audit trail), og andre eksterne forhold som kunderelasjon eller opplysninger i dokumentet også støtte opp under identiteten til den som har signert.

<a name="fotnote-e-id-identifikator"><sup>1</sup></a> Det signerte dokumentet inneholder en anonymisert identifikator som identifiserer undertegneren med 100 % sikkerhet hos leverandøren av e-ID, for eksempel hos BankID. Dette krever oppslag hos leverandøren av e-ID og støttes kun ved avansert e-signatur.
