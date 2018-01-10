# Manuell integrasjon

## Scenarier

Før du starter integrasjonen, er det viktig å bestemme seg for hvilket scenario som passer best til deres integrasjon. Ettersom synkron og asynkron flyt har en del viktige forskjeller er det laget to ulike sett med API-endepunkter for disse. Det er også en del felles funksjonalitet du uansett trenger å implementere, slik at du kan gjenbruke mye dersom dere skulle ha behov for å implementere begge flytene.

## Rot-URL
I informasjonen nedenfor er det beskrevet en rekke stier/endepunkter du skal gå mot når du integrerer. Disse skal være relative til rot-URL for din avsenderkonto. For å finne denne URLen trenger du to ting:

1.  Du må vite hvilket miljø du skal gå mot. Dette kan f.eks være `https://api.difitest.signering.posten.no/api` for testmiljøet eller `https://api.signering.posten.no/api` for produksjon.
2.  Du må også vite din avsenderidentifikator; behandlingsansvarliges organisasjonsnummer (organisasjonen som innhenter signaturen).

Rot-URLen blir da eksempelvis `https://api.difitest.signering.posten.no/api/984661185/` for en integrasjon gjort av Posten Norge AS. Et eksempel på en faktisk URL for å opprette synkrone signeringsoppdrag blir da `https://api.difitest.signering.posten.no/api/984661185/direct/signature-jobs`

## Informasjon om integrasjon

Den resterende dokumentasjonen om integrasjon er delt i fire deler:

1. **[Sikkerhet](sikkerhet.md)**: Gir en innføring i hvordan sikkerheten er implementert i APIene. Gjelder begge flytene.
1. **[Dokumentpakken](dokumentpakken.md)**: Forklarer hvordan man bygger opp en komplett dokumentpakke bestående av dokumentet som skal signeres av sluttbruker og metadata om dokumentet. Gjelder begge flytene.
1. **[API-flyt for synkrone signeringsoppdrag](synkron.md)**: Gir en kort innføring i hvordan en normal flyt gjennom APIet implementeres for scenariet *Synkrone signeringsoppdrag med maskin-til-maskin-integrasjon*
1. **[API-flyt for asynkrone signeringsoppdrag](asynkron.md)**: Gir en kort innføring i hvordan en normal flyt gjennom APIet implementeres for scenariet *Asynkrone signeringsoppdrag med maskin-til-maskin-integrasjon*

## Tilleggstjeneste for langtidslagring

Posten tilbyr en tilleggstjeneste for langtidslagring. Denne er nærmere beskrevet i den [funksjonelle dokumentasjonen](http://digipost.github.io/signature-api-specification/v1.0/#langtidslagring). 

Om langtidslagring er aktivert vil nedlasting av XAdES og PAdES (som beskrevet for hhv. [synkrone](synkron.md#steg-4-laste-ned-pades-eller-xades) og [asynkrone](asynkron.md#steg-3-laste-ned-pades-eller-xades) oppdrag) være tilgjengelig i 50 år.

Benyttes ikke tilleggstjenesten for langtidslagring, slettes XAdES og PAdES 30 dager etter signering.
