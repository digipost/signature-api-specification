---
identifier: langtidslagring
title: Langtidslagring
layout: default
---

Posten signering tilbyr en tilleggstjeneste for langtidslagring av originaldokument og [signerte dokumenter](#signerte_dokumenter). Tjenesten må aktiveres før en avsender kan ta den i bruk.

For avsendere med langtidslagring aktivert, lagres alle dokumenter i minst __50 år__. Dersom langtidslagring _ikke_ er aktivert kan dokumentene bli utilgjengeliggjort etter __30 dager__.

Avsendere som bruker [avsenderportalen](#org-portal) kan aksessere langtidslagrede dokumenter via webgrensesnittet. Dokumentene er tilgjengelig via REST-grensesnittet for avsendere som integrerer mot tjenesten maskinelt, som beskrevet i [teknisk dokumentasjon](https://github.com/digipost/signature-api-specification/integrasjon/README.md#tilleggstjeneste-for-langtidslagring).

Signeringstjenesten langtidslagrer kun XAdES. Når man henter PAdES genereres denne "on demand" basert på XAdES.