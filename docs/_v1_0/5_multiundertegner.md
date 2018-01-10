---
identifier: multiundertegner
title: Multiundertegner
layout: default
---

Et multiundertegneroppdrag er et signeringsoppdrag (synkront eller asynkront) som er adressert til minst 2 undertegnere. Selv om det for undertegnerne oppleves likt om det er én eller flere undertegnere, er det noen forskjeller for avsenderen.

## Terminerende handlinger

Hvis det gjøres en _terminerende handling_ på et signeringsoppdrag vil oppdraget avsluttes og ev. undertegnere som fortsatt ikke har signert vil miste muligheten til dette. Avsenderen blir varslet om at signeringsoppdraget er avsluttet med en status som beskriver hvilken terminerende handling som ble gjort. 

De ulike handlingene som avslutter et signeringsoppdrag er:

1. En undertegner avviser oppdraget
1. Avsender kansellerer oppdraget
1. Oppdragets utløpstidspunkt blir passert

**NB!** For offentlige virksomheter vil oppdraget avsluttes _før det blir aktivert for noen undertegnere_ hvis minst én mottaker ikke eksisterer i eller er reservert fra elektronisk kommunikasjon i [Kontakt- og reservasjonsregisteret](https://samarbeid.difi.no/kontakt-og-reservasjonsregisteret).

**Eksempel**

Avsender oppretter et oppdrag med tre undertegnere. __Undertegner 1__ signerer, __undertegner 2__ avviser. Dersom __undertegner 3__ logger inn i signeringsportalen etter at __undertegner 2__ har avvist, vil hun _ikke_ se signeringsoppdraget og vil ikke ha mulighet til å signere.

## Kjedet signatur

_Kjedet signatur er bare relevant for signeringsoppdrag som signeres i signeringsportalen, dvs. asynkrone oppdrag._

Avsender kan spesifisere rekkefølgen et signeringsoppdrag skal bli tilgjengeliggjort for undertegnerne. Når alle undertegnerne i en gruppe<sup>[1](#fotnote-gruppe)</sup> har signert vil oppdraget bli tilgjengelig for neste gruppe undertegnere.

**Terminerende handlinger for kjedete signeringsoppdrag**

En [terminerende handling](#terminerende-handlinger) på et kjedet signeringsoppdrag vil føre til at oppdraget avsluttes for alle undertegnere som enda ikke har signert, inkludert de undertegnere som ikke har fått oppdraget tilgjengeliggjort enda.

Hvis en undertegner i første gruppe avviser oppdraget eller signeringsfristen går ut, vil oppdraget aldri tilgjengeliggjøres for undertegnerne i de senere gruppene og avsender blir varslet om at oppdraget er fullført med en feilende status. 

<a name="fotnote-gruppe"><sup>1</sup></a> En gruppe undertegnere er alle som har samme `order` i APIet og kan bestå av én eller flere undertegnere som skal signere i parallell.
