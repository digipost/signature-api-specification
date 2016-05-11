# Posten Signering – API spesifikasjon

Dette repoet inneholder informasjon om APIene til Posten Signering.

Mappen `schema/xsd` inneholder XSD-filer som spesifiserer de ulike objektene som sendes og mottas av APIet.

## Tre ulike scenarier for signeringsoppdrag

Posten Signering er laget med tanke på å håndtere 3 ulike scenarier for signering:

1. **Synkrone signeringsoppdrag med maskin-til-maskin-integrasjon:** Dette scenariet er aktuelt hvis sluttbruker er i en sesjon hos tjenesteeier på tjenesteeiers nettsider. Her ønsker tjenesteeier at sluttbruker skal signere et dokument. Sluttbruker opplever signaturprosessen som en integrert del av tjenesteeiers nettsted.
2. **Asynkrone signeringsoppdrag med maskin-til-maskin-integrasjon:** Dette scenariet er aktuelt der det er ønskelig med signering av dokumenter uten at sluttbruker som skal signere er i en sesjon på tjenesteeierens nettside. Dette kan for eksempel være ved batchutsending av dokumenter som skal signeres, eller for å håndtere et scenario der f.eks bruker har hatt telefondialog med tjenesteeier.
3. **Manuelle asynkrone signeringsoppdrag:** Dette scenariet er aktuelt dersom Signeringstjenesten skal benyttes av enten en mindre tjenesteeier, eller en avdeling av en større tjenesteeier der den aktuelle prosessen er manuell. Denne flyten gjennomføres utelukkende via signeringsportalen, og man bruker derfor ikke APIer for integrasjonen. Derfor ikke beskrevet nærmere i denne dokumentasjonen.

## Integrasjon med klientbiblioteker

Det finnes klientbiblioteker som forenkler integrasjonsprosessen:

* [Klientbibliotek for Java](https://github.com/digipost/signature-api-client-java)
* [Klientbibliotek for .NET](https://github.com/digipost/signature-api-client-dotnet)

*OBS: klientbibliotekene og api-spesifikasjonen forandrer seg løpende. Se under "Releases" på GitHub for å finne siste BETA-versjon som skal fungere mot testmiljøet.*

## Manuell integrasjon

**Om Scenarier:** Før du starter integrasjonen, er det viktig å bestemme seg for hvilket scenario som passer best til deres integrasjon. Da disse to flytene har en del viktige forskjeller er det laget to ulike sett med endepunkter du skal integrere med. Det er også en del felles funksjonalitet du uansett trenger å implementere, slik at du kan gjenbruke mye dersom dere skulle ha behov for å implementere begge flytene.

**Om rot-URL:**

I informasjonen nedenfor er det beskrevet en rekke `path`-er du skal gå mot når du integrerer. Disse skal være relative til rot-URL for din avsenderkonto. For å finne denne URLen trenger du to ting:

1.  Du må vite hvilket miljø du skal gå mot. Dette kan f.eks være `https://api.difitest.signering.posten.no/api` for testmiljøet eller `https://api.signering.posten.no/api` for produksjon.
2.  Du må også vite din avsenderidentifikator; behandlingsansvarliges organisasjonsnummer (organisasjonen som innhenter signaturen).

Rot-URLen blir da eksempelvis `https://api.difitest.signering.posten.no/api/984661185/` for en integrasjon gjort av `Posten Norge AS`. Et eksempel på en faktisk URL for å opprette synkrone signeringsoppdrag blir da `https://api.difitest.signering.posten.no/api/984661185/direct/signature-jobs`

**Nedenfor finner du informasjon om integrasjon i følgende fire kapitler:**

* **[FELLES] Sikkerhet** gir en innføring i hvordan sikkerheten er implementert i APIene.
* **[FELLES] Dokumentpakken** forklarer hvordan man bygger opp en komplett dokumentpakke bestående av dokumentet som skal signeres av sluttbruker og metadata om dokumentet
* **API-flyt for synkrone signeringsoppdrag** gir en kort innføring i hvordan en normal flyt gjennom APIet implementeres for scenariet *Synkrone signeringsoppdrag med maskin-til-maskin-integrasjon*
* **API-flyt for asynkrone signeringsoppdrag** gir en kort innføring i hvordan en normal flyt gjennom APIet implementeres for scenariet *Asynkrone signeringsoppdrag med maskin-til-maskin-integrasjon*

### [FELLES] Sikkerhet

Signeringstjenesten benytter to-veis TLS for å sikre konfidensialitet og meldingsintegritet på transportlaget. Dokumentpakken med dokumentet som skal signeres er integritetssikret med ASiC-E.

#### To-veis TLS

For å benytte APIene trenger du et [godkjent virksomhetssertifikat](https://www.regjeringen.no/no/dokumenter/kravspesifikasjon-for-pki-i-offentlig-se/id611085/), som beskrevet for [digital post](http://begrep.difi.no/SikkerDigitalPost/1.2.0/sikkerhet/sertifikathandtering).

Du skal benytte virksomhetssertifikatet som har spesifisert KeyUsage = DigitalSignature og ExtendedKeyUsage = clientAuth.

De fleste HTTP-klienter har innebygget støtte for toveis TLS. Du kan se eksempler på implementasjonen i våre klientbiblioteker. Signeringstjenesten støtter kun TLS 1.2 for to-veis TLS.

Du benytter ditt eget sertifikat i `keystore` (det du skal identifisere deg med), og legger til [tillitsankrene (CA-sertifikater)](http://begrep.difi.no/SikkerDigitalPost/1.2.0/sikkerhet/sertifikathandtering) i `truststore` (det serveren skal identifisere seg med). Sertifikatet ditt vil bli brukt for å verifisere deg mot serveren, og serveren vil bruke Posten Norge AS sitt sertitikat for å identifisere seg. Ved å ha tillitsankrene i `truststore` får du mesteparten av valideringen derfra (gitt at ditt språk/rammeverk håndterer dette). Det du manuelt må gjøre er å validere at sertifikatet tilhører Posten Norge AS, ved å sjekke organisasjonsnummeret som står i `Common Name`.

Et godt tips er å benytte eller hente inspirasjon fra Difi sin sertifikatvalidator, som er tilgjengelig på [GitHub](https://github.com/difi/certvalidator).

##### Vanlige problemer med oppsett av to-veis TLS

* Det benyttes feil trustStore for klienten. I testmiljøet må trustStore inneholde testsertifikatene, i produksjon må det være produksjonssertifikater.
* Sertifikatet som benyttes er ikke et virksomhetssertifikat. Virksomhetssertifikater utstedes typisk av Buypass eller Commfides.
* Klienten støtter ikke TLS v1.2. Java 6 støtter ikke TLS v1.2, i Java 7 må dette skrus på eksplisitt.
* Sertifikatet er utstedt av Commfides SHA-1 rotsertifikat. Kun sertifikater med SHA-256 fra Commfides er støttet. Dette gjelder primært eldre sertifikater.

#### Personopplysninger

Personopplysninger og sensitive personopplysninger skal kun legges i følgende felter:

* `personal-identification-number` – signatarens fødselsnummer eller d-nummer
* `title` – tittelen/emnet til dokumentet, som oppsummerer hva signaturoppdraget handler om
* `description` – kan inneholde en personlig melding, tilleggsinformasjon til dokumentet eller beskrivelse av dokumentet

Øvrige felter skal ikke inneholde sensitive personopplysninger eller personopplysninger. Eksempelvis vil referansen (`reference`) brukes utenfor en sikker kontekst (f.eks i epost-varslinger), og kan derfor ikke inneholde personopplysninger.

Se nærmere beskrivelse av begrepene personopplysninger og sensitive personopplysninger på [Datatilsynet sine nettsider](https://www.datatilsynet.no/personvern/personopplysninger/).

### [FELLES] Dokumentpakken

Dokumentpakken i Posten Signering er basert på ASiC-E standarden ([Associated Signature Containers, Extended form](http://www.etsi.org/deliver/etsi_ts/102900_102999/102918/01.03.01_60/ts_102918v010301p.pdf). Profilen er lagd for å ligne på den som er brukt for [Digital post](http://begrep.difi.no/SikkerDigitalPost). Les mer om [profilen som er benyttet for ASiC](doc/asic-profile.textile).

Pakken skal inneholde dokumentet som skal signeres (én PDF- eller ren tekstfil), en fil kalt `manifest.xml` som beskriver metadata for dokumentet (emner, hvem som skal signere osv.), pluss en fil kalt `signatures.xml` som er signaturen over hele dokumentpakken.

**Dokument:** filen er enten PDF eller en ren tekstfil, og kan maksimalt være 3 MB (3 145 728 bytes) stor. Denne filen refereres til [med det påkrevde `href`-attributtet i `document`-elementet](https://github.com/digipost/signature-api-specification/blob/master/schema/xsd/common.xsd#L120) i `manifest.xml`.

**`manifest.xml`:** Filen følger skjemaet `http://signering.posten.no/schema/v1` som finnes i dette repoet. Eksempler finnes under «Steg 1: opprette signeringsoppdraget» for de ulike integrasjonsmønstrene.

**`signatures.xml`:** Filen følger skjemaet `http://uri.etsi.org/2918/v1.2.1#`, se mappen `thirdparty` i dette repoet for kopier av de relevante standardskjemaene. Følgende er et eksempel på en komplett fil:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<XAdESSignatures xmlns="http://uri.etsi.org/2918/v1.2.1#">
    <Signature xmlns="http://www.w3.org/2000/09/xmldsig#" Id="Signature">
        <SignedInfo>
            <CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>
            <SignatureMethod Algorithm="http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"/>
            <Reference Id="ID_0" URI="document.pdf">
                <DigestMethod Algorithm="http://www.w3.org/2001/04/xmlenc#sha256"/>
                <DigestValue>LPJNul+wow4m6DsqxbninhsWHlwfp0JecwQzYpOLmCQ=</DigestValue>
            </Reference>
            <Reference Id="ID_1" URI="manifest.xml">
                <DigestMethod Algorithm="http://www.w3.org/2001/04/xmlenc#sha256"/>
                <DigestValue>xTqm6xkL7NOjnf5oYa+m0+XKzq1cwWMkcoESLa+/Lko=</DigestValue>
            </Reference>
            <Reference Type="http://uri.etsi.org/01903#SignedProperties" URI="#SignedProperties">
                <Transforms>
                    <Transform Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>
                </Transforms>
                <DigestMethod Algorithm="http://www.w3.org/2001/04/xmlenc#sha256"/>
                <DigestValue>cV6hPhJ6f8hnl0J9czhubj08rWhJmzCQsomxODfYyYM=</DigestValue>
            </Reference>
        </SignedInfo>
        <SignatureValue>VTc0CBKyuDD0DRsJx7JJf6c2iH+TndFcx1aj0nP99snV6magufrPR8JSYadWIn4QICpHFcpAp5s+
            XgIY9jfkAVLtAbiko9VcRpSopP1zj6tM3lrSaoo/lBKP0rWNZCQiNgw8f3pkGbi1bUKVwhbRI4XF
            +bc2rZiZoaWgOByhsFZ25hWrl+GgC3PNsEzA+WN3JbGAq00xtKudRG1vMjdjfsGtmvFYVQ0xQlUY
            5Vxdfovoia3x6zm5zbHWRQdVoUTS3vv5Mv6GAs7JAnDwSNxRVSizN5QB6xB60xRn+BFwdVedQTMa
            cuReWIxY2r8yocS9MAZxFG+4SAHGxjJkNkglHg==
        </SignatureValue>
        <KeyInfo>
            <X509Data>
                <X509Certificate>MIIDYzCCAkugAwIBAgIEIyZ/HzANBgkqhkiG9w0BAQsFADBiMQswCQYDVQQGEwJOTzELMAkGA1UE
                    CBMCTk8xDTALBgNVBAcTBE9zbG8xETAPBgNVBAoTCEF2c2VuZGVyMREwDwYDVQQLEwhBdnNlbmRl
                    cjERMA8GA1UEAxMIQXZzZW5kZXIwHhcNMTQwNTIwMTIxMDA3WhcNMTUwNTE1MTIxMDA3WjBiMQsw
                    CQYDVQQGEwJOTzELMAkGA1UECBMCTk8xDTALBgNVBAcTBE9zbG8xETAPBgNVBAoTCEF2c2VuZGVy
                    MREwDwYDVQQLEwhBdnNlbmRlcjERMA8GA1UEAxMIQXZzZW5kZXIwggEiMA0GCSqGSIb3DQEBAQUA
                    A4IBDwAwggEKAoIBAQCOq505/fn9fDCZjvMlSWJNEj0kVLeFo233MEfhOUOhU44Q7ClGLJfEIdMr
                    ZCzWR58Eo8Fn90bEIosI8rCvw8XsNaDaeVgZ3PDtXTuA8luL/IphWXfuHxdmFm3LPD0XIQS+V8pg
                    J215NIScrZGkgBKjb7uVo+usGYUpqkbjl5kctTziRZo0k2i73iJd1+DjPGZ2OzAR1lMb8EEWheiX
                    qE+pRwpHQOkMiRlNrZxDD1mTJR2RLYYp/guW93YbIgJv4mhV3ZpJL8idU2YECkM5p6Wg2wfznCyx
                    ZU0E5Us4SvuDPrg48ELe140AtXQb8xyuGrLhCm5JyHhAFJM0IoiGQwqPAgMBAAGjITAfMB0GA1Ud
                    DgQWBBSpQuhFDLFSUauhNrCbx+g8QXv9oTANBgkqhkiG9w0BAQsFAAOCAQEAHAENC+ZsxNSXOb6D
                    e90uHqkEJFFZF3CfdtRHx23s2wjDFJI4CEY8WHsOD90ynOyDZV/sWcu1Emi1nZ7rEdtt/wKfhIdI
                    CBzZ+GkU04niu+jpM9OCk1JS1LU+e+ljz6ZL7OZVegTE6tLI8JwfInf7dBjSBnf69Gs4xRK/TmO5
                    i5KipdivkTGXJCaLf8lwFM3bxM5t0H8AzbrHc0JVVTTXHjbIvgg7JhFuiC1z1vM6hsplOysb9gFP
                    89AvNttWcdSb0rQAVz0rjl+GKzM07Aw4tYBl8j42POtjMCjV5e0TCeNfLfnZ3r9DCSUNlAwhisoX
                    l5gXsb+YU/RPOlg+xh5xRA==
                </X509Certificate>
            </X509Data>
        </KeyInfo>
        <Object>
            <QualifyingProperties xmlns="http://uri.etsi.org/01903/v1.3.2#"
                                  xmlns:ns2="http://www.w3.org/2000/09/xmldsig#" Target="#Signature">
                <SignedProperties Id="SignedProperties">
                    <SignedSignatureProperties>
                        <SigningTime>2015-11-25T15:45:42.115+01:00</SigningTime>
                        <SigningCertificate>
                            <Cert>
                                <CertDigest>
                                    <ns2:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
                                    <ns2:DigestValue>6Gko40cr8upGenUAxIT6bBVcRfo=</ns2:DigestValue>
                                </CertDigest>
                                <IssuerSerial>
                                    <ns2:X509IssuerName>CN=Avsender, OU=Avsender, O=Avsender, L=Oslo, ST=NO, C=NO</ns2:X509IssuerName>
                                    <ns2:X509SerialNumber>589725471</ns2:X509SerialNumber>
                                </IssuerSerial>
                            </Cert>
                        </SigningCertificate>
                    </SignedSignatureProperties>
                    <SignedDataObjectProperties>
                        <DataObjectFormat ObjectReference="#ID_0">
                            <MimeType>application/pdf</MimeType>
                        </DataObjectFormat>
                        <DataObjectFormat ObjectReference="#ID_1">
                            <MimeType>application/xml</MimeType>
                        </DataObjectFormat>
                    </SignedDataObjectProperties>
                </SignedProperties>
            </QualifyingProperties>
        </Object>
    </Signature>
</XAdESSignatures>
```

### API-flyt for Synkrone signeringsoppdrag

Dette integrasjonsmønsteret vil passe for større tjenesteeiere som har egne portaler og nettløsninger, og som ønsker å tilby signering sømløst som en del av en prosess der brukeren allerede er innlogget i en sesjon på tjenesteeiers nettsider. Signeringsprosessen vil oppleves som en integrert del av brukerflyten på tjenesteiers sider, og brukeren blir derfor sendt tilbake til tjenesteeiers nettsider etter at signeringen er gjennomført.

Relevante typer for denne delen av APIet finnes i filen `direct.xsd`.

![Flytskjema for Synkrone signeringsoppdrag](/doc/flytskjemaer/synkron-maskin-til-maskin.png?raw=true "Flytskjema for Synkrone signeringsoppdrag")
**Flytskjema for det synkrone scenariet:** *skjemaet viser flyten helt fra en bruker logger inn på tjenesteeiers nettsider til oppdraget er fullstendig signert. Heltrukne linjer viser brukerflyt, mens stiplede linjer viser API-kall*

#### Steg 1: opprette signeringsoppdraget

Flyten begynner ved at tjenesteeier gjør et bak-kanal-kall mot APIene for å opprette signeringsoppdraget. Dette kallet gjøres som ett multipart-request, der den ene delen er dokumentpakken og den andre delen er metadata.

* Kallet gjøres som en `HTTP POST` mot ressursen `<rot-URL>/direct/signature-jobs`
* Dokumentpakken legges med multipart-kallet med mediatypen `application/octet-stream`. Se forrige kapittel for mer informasjon om dokumentpakken.
* Metadataene som skal sendes med i dette kallet er definert av elementet `direct-signature-job-request`. Disse legges med multipart-kallet med mediatypen `application/xml`.

En del av metadataene er et sett med URLer definert i elementet `exit-urls`. Disse adressene vil bli benyttet av signeringstjenesten til å redirecte brukeren tilbake til din portal ved fullført signering. Følgende tre URLer skal oppgis:

* **completion-url:** Hit sendes brukeren dersom signeringen er vellykket. Du kan da be om status for å få URLer til nedlasting av signert dokument.
* **rejection-url:** Hit sendes brukeren dersom han selv velger å avbryte signeringen. Dette er en handling brukeren *selv valgte* å gjennomføre.
* **error-url:** Hit sendes brukeren dersom det skjer noe galt under signeringen. Dette er noe brukeren *ikke* valgte å gjøre selv.

Følgende er et eksempel på metadata for et signeringsoppdrag:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<direct-signature-job-request xmlns="http://signering.posten.no/schema/v1">
    <reference>123-ABC</reference>
    <exit-urls>
        <completion-url>https://www.sender.org/completed</completion-url>
        <rejection-url>https://www.sender.org/rejected</rejection-url>
        <error-url>https://www.sender.org/failed</error-url>
    </exit-urls>
</direct-signature-job-request>
```

Følgende er et eksempel på `manifest.xml` fra dokumentpakken:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<direct-signature-job-manifest xmlns="http://signering.posten.no/schema/v1">
    <signer>
        <personal-identification-number>12345678910</personal-identification-number>
    </signer>
    <sender>
        <organization-number>123456789</organization-number>
    </sender>
    <document href="document.pdf" mime="application/pdf">
        <title>Tittel</title>
        <description>Melding til signatar</description>
    </document>
</direct-signature-job-manifest>
```

Som respons på dette kallet vil man få en respons definert av elementet `direct-signature-job-response`.

* Denne responsen inneholder en URL (`redirect-url`) som man redirecter brukerens nettleser til for å starte signeringsseremonien.
* I tillegg inneholder den en URL du benytter for å spørre om status på oppdraget. Her skal man **IKKE** benytte seg av polling, man skal derimot vente til brukeren returneres til en av URLene definert i requesten, for deretter å gjøre kallet. For å forhindre polling kreves det et token som du får tilbake ved redirecten, se Steg 3 for nærmere forklaring.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<direct-signature-job-response xmlns="http://signering.posten.no/schema/v1">
    <signature-job-id>1</signature-job-id>
    <redirect-url>
        https://signering.posten.no#/redirect/421e7ac38da1f81150cfae8a053cef62f9e7433ffd9395e5805e820980653657
    </redirect-url>
    <status-url>https://api.signering.posten.no/api/{sender-identifier}/direct/signature-jobs/1/status</status-url>
</direct-signature-job-response>
```

#### Steg 2: signeringsseremonien

Hele dette steget gjennomføres i signeringsportalen. Du redirecter brukeren til portalen ved å benytte URLen du får som svar på opprettelsen av oppdraget. Denne linken inneholder et engangstoken generert av signeringstjenesten, og det er dette tokenet som gjør at brukeren får tilgang til å lese dokumentet og gjennomføre signeringen.

**Noen ord om sikkerheten her:** For å håndtere sikkerheten i dette kallet vil dette tokenet kun fungere én gang. Brukeren vil få en cookie av signeringstjenesten ved første kall, slik at en eventuell refresh ikke stopper flyten, men du kan ikke bruke denne URLen på et senere tidspunkt. Årsaken til at vi kun tillater at den brukes én gang er at URLer kan fremkomme i eventuelle mellomtjeneres logger, og de vil dermed ikke være sikre etter å ha blitt benyttet første gang.

Brukeren gjennomfører signeringsseremonien, og blir deretter sendt tilbake til din portal via URLen spesifisert av deg i `completion-url`. På slutten av denne URLen vil det legges på et query-parameter (`status_query_token`) du senere skal benytte når du spør om status.

#### Steg 3: hent status

Når brukeren blir sendt tilbake til din portal, så skal du gjøre et bak-kanal-kall (`HTTP GET`) for å hente ned status. Dette gjøres ved å benytte `status-url` du fikk i Steg 1, pluss query-parameter (`status_query_token`) du fikk i Steg 2.

Du skal ikke sende med noen andre data i dette kallet.

Responsen fra dette kallet er definert gjennom elementet `direct-signature-job-status-response`. Et eksempel på denne responsen ved et suksessfullt signeringsoppdrag vises under:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<direct-signature-job-status-response xmlns="http://signering.posten.no/schema/v1">
    <signature-job-id>1</signature-job-id>
    <status>COMPLETED_SUCCESSFULLY</status>
    <confirmation-url>https://api.signering.posten.no/api/{sender-identifier}/direct/signature-jobs/1/complete</confirmation-url>
    <xades-url>https://api.signering.posten.no/api/{sender-identifier}/direct/signature-jobs/1/xades/1</xades-url>
    <pades-url>https://api.signering.posten.no/api/{sender-identifier}/direct/signature-jobs/1/pades</pades-url>
</direct-signature-job-status-response>
```

#### Steg 4: laste ned PAdES eller XAdES

I forrige steg fikk du to lenker: `xades-url` og `pades-url`. Disse kan du gjøre en `HTTP GET` på for å laste ned det signerte dokumentet i de to formatene.

**XAdES** er et format som brukes til å styrke og standardisere signaturene som kommer fra e-ID-leverandørene. Formatet har støtte for langtidsvalidering, og gjør samtidig at man får ett format å forholde seg til, uavhengig av hvilken e-ID-leverandør som er brukt til signering.

**PAdES** er et signaturformat som inneholder originaldokumentet, alle signaturer og all informasjon som er nødvendig for å validere signaturen. Formatet er spesifisert av ETSI, og bygger på PDF. En unik egenskap med PAdES er at dokumentet kan åpnes i en vilkårlig PDF- leser. Adobe Reader (og eventuelle andre avanserte PDF lesere) vil også kunne vise frem deler av valideringsinformasjonen slik at sluttbrukeren selv kan se at dokumentet er gyldig signert. I tillegg ligger også XAdES-dokumentet vedlagt denne PDFen.

#### Steg 5: Bekrefte ferdig prosessering

Til slutt gjør du et `HTTP POST`-kall mot `confirmation-url` for å bekrefte at du har prosessert jobben ferdig. Avhengig av om arkivopsjonen benyttes, så vil dette enten slette oppdraget i signeringsportalen, eller markere oppdraget som ferdig og arkivert.


### API-flyt for Asynkrone signeringsoppdrag

Dette integrasjonsmønsteret passer for tjenesteeiere som ønsker å opprette signeringsoppdrag i signeringstjenesten som et ledd i en flyt som ikke starter med at sluttbruker befinner seg på tjenesteeiers nettsider. Signeringsseremonien gjennomføres av sluttbruker i Signeringsportalen, og tjenesteeier vil deretter asynkront kunne polle på status og hente ned det signerte dokumentet.

Dette scenariet er også utviklet med tanke på å støtte prosesser der det er behov for å innhente signaturer fra flere enn én sluttbruker på et dokument

Relevante typer for denne delen av APIet finnes i filen `portal.xsd`.

![Flytskjema for Asynkrone signeringsoppdrag](/doc/flytskjemaer/asynkron-maskin-til-maskin.png?raw=true "Flytskjema for Asynkrone signeringsoppdrag")
**Flytskjema for det asynkrone scenariet:** *skjemaet viser flyten fra tjenesteeier sender inn oppdrag, starer polling, via at sluttbruker(e) signerer oppdragene, og tjenesteeier får svar på polling og kan laste ned signert versjon. Dersom du ikke sender et oppdrag til mer enn en bruker (multisignatar) kan du se bort i fra den første "steg 4"-seksjonen. Heltrukne linjer viser brukerflyt, mens stiplede linjer viser API-kall*

#### Steg 1: opprette signeringsoppdraget

Flyten begynner ved at tjenesteeier gjør et bak-kanal-kall mot APIene for å opprette signeringsoppdraget. Dette kallet gjøres som ett multipart-request, der den ene delen er dokumentpakken og den andre delen er metadata.

* Kallet gjøres som en `HTTP POST` mot ressursen `<rot-URL>/portal/signature-jobs`
* Dokumentpakken legges med multipart-kallet med mediatypen `application/octet-stream`. Se tidligere kapittel for mer informasjon om dokumentpakken.
* Metadataene som skal sendes med i dette kallet er definert av elementet `portal-signature-job-request`. Disse legges med multipart-kallet med mediatypen `application/xml`.



Følgende er et eksempel på metadata for et asynkront signeringsoppdrag:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<portal-signature-job-request xmlns="http://signering.posten.no/schema/v1">
    <reference>123-ABC</reference>
</portal-signature-job-request>
```

Følgende er et eksempel på `manifest.xml` fra dokumentpakken for et signeringsoppdrag som skal signeres av fire signatarer:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<portal-signature-job-manifest xmlns="http://signering.posten.no/schema/v1">
    <signers>
        <signer order="1">
            <personal-identification-number>12345678910</personal-identification-number>
            <notifications-using-lookup>
                <email/>
            </notifications-using-lookup>
        </signer>
        <signer order="2">
            <personal-identification-number>10987654321</personal-identification-number>
            <notifications-using-lookup>
                <email/>
            </notifications-using-lookup>
        </signer>
        <signer order="2">
            <personal-identification-number>01013300001</personal-identification-number>
            <notifications-using-lookup>
                <email/>
            </notifications-using-lookup>
        </signer>
        <signer order="3">
            <personal-identification-number>02038412546</personal-identification-number>
            <notifications-using-lookup>
                <email/>
                <sms/>
            </notifications-using-lookup>
        </signer>
    </signers>
    <sender>
        <organization-number>123456789</organization-number>
    </sender>
    <document href="document.pdf" mime="application/pdf">
        <title>Tittel</title>
        <description>Melding til signatar</description>
    </document>
    <availability>
        <activation-time>2016-02-10T12:00:00+01:00</activation-time>
        <available-seconds>864000</available-seconds>
    </availability>
</portal-signature-job-manifest>
```

`order`-attributtet på `signer` brukes til å angi rekkefølgen på signatarene. I eksempelet over vil oppdraget først bli tilgjengelig for signatarene med `order="2"` når signataren med `order="1"` har signert, og for signataren med `order="3"` når begge de med `order="2"` har signert.

`availability` brukes til å kontrollere tidsrommet et dokument er tilgjengelig for mottaker(e) for signering. 
Tidspunktet angitt i `activation-time` angir når jobben aktiveres, og de første signatarene får tilgang til dokumentet til signering.
Tiden angitt i `available-seconds` gjelder for alle signatarer; d.v.s alle signatarer vil ha like lang tid på seg til å signere eller avvise mottatt dokument fra det blir tilgjengelig for dem. Dette tidsrommet gjelder altså _for hvert sett med signatarer med samme `order`_. Dersom man angir f.eks. _345600_ sekunder (4 dager) vil signatarer med `order=1` få maks 4 dager fra `activation-time` til å signere. Signatarer med `order=2` vil få tilgjengeliggjort dokumentet umiddelbart når _alle signatarer med `order=1` har signert_, og de vil da få maks 4 nye dager fra _tidspunktet de fikk dokumentet tilgjengelig_. En jobb utløper og stopper dersom minst 1 signatar ikke agerer innenfor sitt tidsrom når dokumentet er tilgjengelig. Dersom man utelater `availability` vil jobben aktiveres umiddelbart, og dokumentet vil være tilgjengelig i maks 2 592 000 sekunder (30 dager) for hvert sett med `order`-grupperte signatarer. Jobber som angir større `available-seconds` enn 7 776 000 sekunder (90 dager) blir avvist av tjenesten.

Som respons på dette kallet vil man få en respons definert av elementet `portal-signature-job-response`. Denne responsen inneholder en ID generert av signeringstjenesten. Du må lagre denne IDen i dine systemer slik at du senere kan koble resultatene du får fra polling-mekanismen til riktig oppdrag.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<portal-signature-job-response xmlns="http://signering.posten.no/schema/v1">
    <signature-job-id>1</signature-job-id>
    <cancellation-url>https://api.signering.posten.no/api/{sender-identifier}/portal/signature-jobs/1/cancel</cancellation-url>
</portal-signature-job-response>
```

#### Steg 2: Polling på status

Siden dette er en asynkron flyt, så må du jevnlig spørre signeringstjenesten om det har skjedd noen endringer på noen av signeringsoppdragene for din organisasjon. Dette gjør du på tvers av alle signeringsoppdrag du har opprettet, hvis ikke ville du måtte foretatt en voldsom mengde spørringer dersom du har flere aktive signeringsoppdrag i gang samtidig (noe som er veldig sannsynlig).

For å gjøre en polling, så gjør du en `HTTP GET` mot `<rot-URL>/portal/signature-jobs`. Du skal ikke ha med noen request-body på dette kallet.

Responsen på dette kallet vil være én av to ting:

1. **0 oppdateringer:** Dersom det ikke er noen oppdateringer på tvers av alle dine aktive signeringsoppdrag så vil da få en HTTP respons med statuskode `204 No Content`.
2. **Minst 1 oppdatering:** Dersom det er oppdateringer på dine oppdrag, så vil du få en `200 OK` med responsbody som inneholder informasjon om oppdateringen. Denne er definert av elementet `portal-signature-job-status-change-response`.

Følgende er et eksempel på en respons der en del av signeringsoppdraget har blitt fullført:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<portal-signature-job-status-change-response xmlns="http://signering.posten.no/schema/v1">
    <signature-job-id>1</signature-job-id>
    <status>IN_PROGRESS</status>
    <confirmation-url>https://api.signering.posten.no/api/{sender-identifier}/portal/signature-jobs/1/complete</confirmation-url>
    <signatures>
        <signature>
            <status>SIGNED</status>
            <personal-identification-number>12345678910</personal-identification-number>
            <xades-url>https://api.signering.posten.no/api/{sender-identifier}/portal/signature-jobs/1/xades/1</xades-url>
        </signature>
        <signature>
            <status>WAITING</status>
            <personal-identification-number>98765432100</personal-identification-number>
        </signature>
        <pades-url>https://api.signering.posten.no/api/{sender-identifier}/portal/signature-jobs/1/pades</pades-url>
    </signatures>
</portal-signature-job-status-change-response>
```

#### Steg 3: laste ned PAdES eller XAdES

I forrige steg fikk du to lenker: `xades-url` og `pades-url`. Disse kan du gjøre en `HTTP GET` på for å laste ned det signerte dokumentet i de to formatene.

XAdES-filen laster du ned pr. signatar, mens PAdES-filen lastes ned på tvers av alle signatarer. Denne vil inneholde signeringsinformasjon for alle signatarer som frem til nå har signert på oppdraget. I de aller fleste tilfeller er det ikke aktuelt å laste ned denne før alle signatarene har statusen `SIGNED`.

Se nærmere forklaring av disse to formatene i dokumentasjonen på det synkrone scenariet.

#### Steg 4: Bekrefte ferdig prosessering

Til slutt gjør du et `HTTP POST`-kall mot `confirmation-url` for å bekrefte at du har prosessert statusoppdateringen ferdig. Dersom statusen indikerer at oppdraget er helt ferdig, så vil denne bekreftelsen også bekrefte at du er ferdig med å prosessere hele oppdraget. Avhengig av om arkivopsjonen benyttes, så vil dette enten slette oppdraget i signeringsportalen, eller markere oppdraget som ferdig og arkivert.

I tillegg vil dette kallet gjøre at du ikke lenger får informasjon om denne statusoppdateringen ved polling. Se mer informasjon om det nedenfor, i avsnittet om fler-server-scenarioet.

#### Mer informasjon om pollingmekanismen

##### Hvor ofte skal du polle?
Mekanikken fungerer slik at du venter en viss periode mellom hver gang du spør om oppdateringer. Oppdateringenene vil komme på en kø, og så lenge du får en ny statusoppdatering, så kan du umiddelbart etter å ha prosessert denne igjen spørre om en oppdatering. Dersom du får beskjed om at det ikke er flere oppdateringer igjen, så skal du ikke spørre om oppdateringer før det har gått en viss periode. Når du gjør denne pollingen så vil du alltid få en HTTP-header (`X-Next-permitted-poll-time`) som respons som forteller deg når du kan gjøre neste polling.

##### Hva med et fler-server-scenario:
Signeringstjenestens pollingmekaniske er laget med tanke på at det skal være enkelt å gjøre pollingen fra flere servere uten at du skal måtte synkronisere pollingen på tvers av disse. Dersom du bruker flere servere uten synkronisering så vil du komme opp i situasjoner der en av serverene poller før neste poll-tid, selv om en annen server har fått beskjed om dette. Det er en helt OK oppførsel, du vil da få en HTTP respons med statusen `429 Too Many Requests` tilbake, som vil inneholde headeren `X-Next-permitted-poll-time`. Så lenge du etter det kallet respekterer poll-tiden for den serveren, så vil alt fungere bra.

Statusoppdateringer du henter fra køen ved polling vil forsvinne fra køen, slik at en eventuell annen server som kommer inn ikke vil få den samme statusoppdateringen. Selv om du kaller på polling-APIet på samme tid, så er det garantert at du ikke får samme oppdatering to ganger. For å håndtere at feil kan skje enten i overføringen av statusen til deres servere eller at det kan skje feil i prosesseringen på deres side, så vil en oppdatering som hentes fra køen og ikke bekreftes dukke opp igjen på køen. Pr. i dag så er det satt en venteperiode på 10 minutter før en oppdateing igjen forekommer på køen. På grunn av dette så er det essensielt at prosesseringsbekrefelse sendes som beskrevet i Steg 4.
