---
identifier: signaturtype
title: Signaturtype
layout: default
---

### Autentisert og avansert e-signatur
Avsender velger om den vil innhente autentisert eller avansert signatur fra mottaker. I [esignaturloven](https://lovdata.no/dokument/NL/lov/2001-06-15-81) er dette omtalt som hhv. [_elektronisk signatur_ og _avansert elektronisk signatur_](https://lovdata.no/lov/2001-06-15-81/§3).

Om ingen signaturtype angis ved opprettelse av oppdraget, vil _avansert e-signatur_ innhentes som standard.

### Sikkerhetsnivå
Avsender må angi hvilket sikkerhetsnivå signeringsoppdraget skal ha. Dette kan være _3_ eller _4_, og begrenser hvilke elektroniske ID-er undertegner kan bruke for å signere dokumentet, se tabellen under. Sikkerhetsnivået begrenser også hvilke innloggingsmetoder undertegner kan bruke for å se signeringsoppdraget og dets detaljer, samt begynne selve signeringen.

Om ingen signaturtype angis ved opprettelse av oppdraget, vil _nivå 4_ settes som standard.

### Begrenset visning av oppdrag
Er bruker innlogget på et lavere nivå enn det som kreves for å se detaljene om signeringsoppdraget, vil hun få en begrenset visning av oppdraget, der kun en _ikke-sensitiv tittel_ er synlig. For å se alle detaljer om oppdraget vil hun bli bedt om å logge inn på nytt på et høyere sikkerhetsnivå. Brukeren vil alltid bli veiledet til den innloggingsmetoden som kreves for oppdraget som skal signeres, slik at brukeropplevelsen blir så god som mulig.

### Oversikt over elektroniske ID-er

#### Autentisert e-signatur

For offentlige virksomheter vil alle e-IDer tilbys gjennom ID-porten

| e-ID | Gjelder | Nivå |
| ---               | ---     | ---  |
| Min ID            | Offentlige virksomheter | 3 |
| BankID            | Offentlige virksomheter | 4 |
| Buypass           | Offentlige virksomheter | 4 |
| Commfides         | Offentlige virksomheter | 4 |
| BankID på mobil   | Offentlige virksomheter | 4 |

#### Avansert e-signatur

| e-ID | Gjelder | Nivå |
| ---               | ---     | ---  |
| BankID            | Offentlige og private virksomheter | 4 |
| Buypass           | Offentlige og private virksomheter | 4 |
