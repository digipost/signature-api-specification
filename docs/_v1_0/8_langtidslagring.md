---
identifier: langtidslagring
title: Langtidslagring
layout: default
---

Posten signering tilbyr en tilleggstjeneste for langtidslagring av originaldokument og [signerte dokumenter](#signerte_dokumenter). Tjenesten må aktiveres før en avsender kan ta den i bruk.

For avsendere med langtidslagring aktivert, lagres alle dokumenter i minst __50 år__. Dersom langtidslagring _ikke_ er aktivert kan dokumentene bli utilgjengeliggjort etter __30 dager__.

Avsendere som bruker [avsenderportalen](#org-portal) kan aksessere langtidslagrede dokumenter via webgrensesnittet. Dokumentene er tilgjengelig via REST-grensesnittet for avsendere som integrerer mot tjenesten maskinelt, som beskrevet i [teknisk dokumentasjon](https://github.com/digipost/signature-api-specification/tree/master/integrasjon#tilleggstjeneste-for-langtidslagring).

Signeringstjenesten langtidslagrer kun XAdES. Når man henter PAdES genereres denne "on demand" basert på XAdES.

### Sletting av dokumenter

Avsendere kan slette arkiverte dokumenter hvis de ønsker å fjerne dem fra arkivet. Avsendere som bruker [avsenderportalen](#org-portal) kan slette dokumenter via webgrensesnittet. Dokumentene kan slettes via REST-grensesnittet for avsendere som integrerer mot tjenesten maskinelt, som beskrevet i [teknisk dokumentasjon](https://github.com/digipost/signature-api-specification/tree/master/integrasjon#sletting-fra-arkivet).

#### Begrensninger:

- Alle undertegnere må ha signert før dokumentene kan slettes.
- Dokumenter kan tidligst slettes 24 timer etter signeringtidspunkt. 
- Dokumenter som skal sendes til undertegners digitale postkasse kan ikke slettes før de har blitt sendt dit. Dersom undertegner ikke har digital postkasse ventes det inntil 7 dager før dokumentene kan slettes.
