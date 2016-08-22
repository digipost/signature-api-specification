---
identifier: multisignatar
title: Multisignatar
layout: default
---

Et multisignataroppdrag er et signeringsoppdrag (synkront eller asynkront) som er adressert til minst 2 signatarer. Selv om det for signatarene oppleves likt om det er én eller flere mottakere, er det noen forskjeller for avsenderen.

## Terminerende handlinger

Hvis det gjøres en _terminerende handling_ på et signeringsoppdrag vil oppdraget avsluttes og ev. signatarer som fortsatt ikke har signert vil miste muligheten til dette. Avsenderen blir varslet om at signeringsoppdraget er avsluttet med en status som beskriver hvilken terminerende handling som ble gjort. 

De ulike handlingene som avslutter et signeringsoppdrag er:

1. Signatar avviser oppdraget
1. Avsender kansellerer oppdraget
1. Oppdragets utløpstidspunkt blir passert

**NB!** For offentlige virksomheter vil oppdraget avsluttes _før det blir aktivert for noen signatarer_ hvis minst én mottaker ikke eksisterer i eller er reservert fra elektronisk kommunikasjon i Kontakt- og reservasjonsregisteret.

**Eksempel**

Avsender oppretter et oppdrag med tre signatarer. __Signatar 1__ signerer, __signatar 2__ avviser. Dersom __signatar 3__ logger inn i signeringsportalen etter at __signatar 2__ har avvist, vil hun _ikke_ se singeringsoppdraget og vil ikke ha mulighet til å signere.

## Kjedet signatur

_Kjedet signatur er bare relevant for signeringsoppdrag som signeres i signeringsportalen, dvs. asynkrone oppdrag._

Avsender kan spesifisere rekkefølgen et signeringsoppdrag skal bli tilgjengeliggjort for signatarene. Når alle signatarene i en gruppe<sup>[1](#fotnote-gruppe)</sup> har gjennomført signeringen vil oppdraget bli tilgjengelig for neste gruppe signatarer.

**Terminerende handlinger for kjedete signeringsoppdrag**

En [terminerende handling](#terminerende-handlinger) på et kjedet signeringsoppdrag vil føre til at oppdraget avsluttes for alle signatarer som enda ikke har signert, inkludert de signatarer som ikke har fått oppdraget tilgjengeliggjort enda.

Hvis en signatar i første gruppe avviser oppdraget eller signeringsfristen går ut, vil oppdraget aldri tilgjengeliggjøres for signatarene i de senere gruppene og avsender blir varslet om at oppdraget er fullført med en feilende status. 

<a name="fotnote-gruppe"><sup>1</sup></a> En gruppe signatarer er alle signatarer som har samme [`order` i APIet](https://github.com/digipost/signature-api-specification/blob/master/schema/xsd/portal.xsd#L70) og kan bestå av én eller flere signatarer som skal signere i parallell.
