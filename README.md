# Posten Signering – API spesifikasjon

Dette repoet inneholder informasjon om APIene til Posten Signering. 

Mappen `src/main/xsd` inneholder XSD-filer som spesifiserer de ulike objektene som sendes og mottas av APIet.

## Tre ulike scenarier for signeringsoppdrag

Posten Signering er laget med tanke på å håndtere 3 ulike scenarier for signering:

1. **Synkrone signeringsoppdrag med maskin-til-maskin-integrasjon:** Dette scenariet er aktuelt hvis sluttbruker er i en sesjon hos tjenesteeier på tjenesteeiers nettsider. Her ønsker tjenesteeier at sluttbruker skal signere et dokument. Sluttbruker opplever signaturprosessen som en integrert del av tjenesteeiers nettsted.
2. **Asynkrone signeringsoppdrag med maskin-til-maskin-integrasjon:** Dette scenariet er aktuelt der det er ønskelig med signering av dokumenter uten at sluttbruker som skal signere er i en sesjon på tjenesteeierens nettside. Dette kan for eksempel være ved batchutsending av dokumenter som skal signeres, eller for å håndtere et scenario der f.eks bruker har hatt telefondialog med tjenesteeier.
3. **Manuelle asynkrone signeringsoppdrag:** Dette scenariet er aktuelt dersom Signeringstjenesten skal benyttes av enten en mindre tjenesteeier, eller en avdeling av en større tjenesteeier der den aktuelle prosessen er manuell. Denne flyten gjennomføres utelukkende via signeringsportalen, og man bruker derfor ikke APIer for integrasjonen. Derfor ikke beskrevet nærmere i denne dokumentasjonen.

## Integrasjon med klientbiblioteker

Det blir laget klientbiblioteker for både Java og .NET, som forenkler integrasjonsprosessen. Se følgende GitHub-repoer for mer informasjon om integrasjon vha. klientbibliotekene:

* [Klientbibliotek for Java](https://github.com/digipost/signature-api-client-java)
* [Klientbibliotek for .NET](https://github.com/digipost/signature-api-client-dotnet)

*OBS: klientbibliotekene er pr. desember 2015 "work in progress", og er dermed hverken fullstendig implementert eller releaset i stabil versjon*

## Manuell integrasjon

Før du starter integrasjonen, er det viktig å bestemme seg for hvilket scenario som passer best til deres integrasjon. Da disse to flytene har en del viktige forskjeller er det laget to ulike sett med endepunkter du skal integrere med. Det er også en del felles funksjonalitet du uansett trenger å implementere, slik at du kan gjenbruke mye dersom dere skulle ha behov for å implementere begge flytene.

Nedenfor finner du informasjon om integrasjon i følgende fire kapitler:

* **[FELLES] Sikkerhetsmekanismene** gir en innføring i hvordan sikkerheten er implementert i APIene. Fokuset her er å forklare hvordan du skal integrere, ikke alle detaljene om mekanismene.
* **[FELLES] Dokumentpakken** forklarer hvordan man bygger opp en komplett dokumentpakke bestående av dokumentet som skal signeres av sluttbruker og metadata om dokumentet
* **API-flyt for synkrone signeringsoppdrag** gir en kort innføring i hvordan en normal flyt gjennom APIet implementeres for scenariet *Synkrone signeringsoppdrag med maskin-til-maskin-integrasjon*
* **API-flyt for asynkrone signeringsoppdrag** gir en kort innføring i hvordan en normal flyt gjennom APIet implementeres for scenariet *Asynkrone signeringsoppdrag med maskin-til-maskin-integrasjon*

### [FELLES] Sikkerhetsmekanismene

Sikkerheten i APIet til Posten Signering er implementert vha. toveis TLS. For å benytte APIene trenger du et [godkjent virksomhetssertifikat](https://www.regjeringen.no/no/dokumenter/kravspesifikasjon-for-pki-i-offentlig-se/id611085/) (for eksempel fra [Buypass](https://www.buypass.no/produkter-og-tjenester/virksomhetssertifikat) eller [Commfides](https://www.commfides.com/e-ID/Bestill-Commfides-Virksomhetssertifikat.html)).

De fleste HTTP-klienter har innebygget støtte for toveis TLS. Du kan se eksempler på implementasjonen i våre klientbiblioteker. 

Du benytter ditt eget sertifikat i `keystore` (det du skal identifisere deg med), og legger til Buypass sitt rotsertifikat i `truststore` (det serveren skal identifisere seg med). Sertifikatet ditt vil bli brukt for å verifisere deg mot serveren, og serveren vil bruke Posten Norge AS sitt sertitikat for å verifisere seg. Ved å ha Buypass sitt rotsertifikat i `truststore` får du mesteparten av valideringen derfra (gitt at ditt språk/rammeverk håndterer dette). Det du manuelt må gjøre er å validere at sertifikatet tilhører Posten Norge AS, ved å sjekke organisasjonsnummeret som står i `Common Name`. 

Et godt tips er å benytte eller hente inspirasjon fra Difi sin sertifikatvalidator som er tilgjengelig på [GitHub](https://github.com/difi/certvalidator).

### [FELLES] Dokumentpakken

Dokumentpakken i Posten Signering er basert på ASiC-E standarden ([Associated Signature Containers, Extended form](http://www.etsi.org/deliver/etsi_ts/102900_102999/102918/01.03.01_60/ts_102918v010301p.pdf)). Pakken skal inneholde dokumentene som skal signeres (PDF- eller TXT-filer), en fil kalt `manifest.xml` som beskriver metadata for dokumentet (emner, hvem som skal signere osv.), pluss en fil kalt `signatures.xml` som er signaturen over hele dokumentpakken.

`mainfest.xml`-filen følger skjemaet `http://signering.digipost.no/schema/v1/signature-document` som finnes i dette repoet. Følgende er et eksempel på en komplett fil:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<manifest xmlns="http://signering.digipost.no/schema/v1/signature-document"
          xmlns:common="http://signering.digipost.no/schema/v1/common">
    <signers>
        <common:signer>
            <common:person>
                <common:personal-identification-number>12345678910</common:personal-identification-number>
            </common:person>
        </common:signer>
    </signers>
    <sender>
        <common:organization>123456789</common:organization>
    </sender>
    <primary-document href="document.pdf" mime="application/pdf">
        <common:title lang="NO">
            <common:non-sensitive>Tittel</common:non-sensitive>
        </common:title>
        <common:description>Melding til signatar</common:description>
    </primary-document>
</manifest>
```

`signatures.xml` følger skjemaet `http://uri.etsi.org/2918/v1.2.1#`, se mappen `thirdparty` i dette repoet for kopier av de relevante standardskjemaene. Følgende er et eksempel på en komplett fil:

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

Flere detaljer om dokumentpakken kan finnes i de relevante XSDene. 

### API-flyt for Synkrone signeringsoppdrag

Dette integrasjonsmønsteret vil passe for større tjenesteeiere som har egne portaler og nettløsninger, og som ønsker å tilby signering sømløst som en del av en prosess der brukeren allerede er innlogget i en sesjon på tjenesteeiers nettsider. Signeringsprosessen vil oppleves som en integrert del av brukerflyten på tjenesteiers sider, og brukeren blir derfor sendt tilbake til tjenesteeiers nettsider etter at signeringen er gjennomført.

Relevante typer for denne delen av APIet finnes i filen `signature-job.xsd`.

#### Steg 1: opprette signeringsoppdraget

Flyten begynner ved at tjenesteeier gjør et bak-kanal-kall mot APIene for å opprette signeringsoppdraget. Dette kallet gjøres som ett multipart-request, der den ene delen er dokumentpakken og den andre delen er metadata.

* Kallet gjøres som en `HTTP POST` mot ressursen `/signature-jobs`
* Dokumentpakken legges med multipart-kallet med mediatypen `application/octet-stream`. Se forrige kapittel for mer informasjon om dokumentpakken.
* Metadataene som skal sendes med i dette kallet er definert av elementet `direct-signature-job-request`. Disse legges med multipart-kallet med mediatypen `application/xml`.

En del av metadataene er et sett med URLer definert i elementet `exit-urls`. Disse adressene vil bli benyttet av signeringstjenesten til å redirecte brukeren tilbake til din portal ved fullført signering. Følgende tre URLer skal oppgis:

* **completion-url:** Hit sendes brukeren dersom signeringen er vellykket. Du kan da be om status for å få URLer til nedlasting av signert dokument.
* **cancellation-url:** Hit sendes brukeren dersom han selv velger å avbryte signeringen. Dette er en handling brukeren *selv valgte* å gjennomføre.
* **error-url:** Hit sendes brukeren dersom det skjer noe galt under signeringen. Dette er noe brukeren *ikke* valgte å gjøre selv.

Følgende er et eksempel på metadata for et signeringsoppdrag:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<direct-signature-job-request xmlns="http://signering.digipost.no/schema/v1/signature-job"
                              xmlns:common="http://signering.digipost.no/schema/v1/common">
    <id>1</id>
    <signer>
        <common:person>
            <common:personal-identification-number>12345678910</common:personal-identification-number>
        </common:person>
    </signer>
    <sender>
        <common:organization>123456789</common:organization>
    </sender>
    <primary-document href="document.pdf" mime="application/pdf">
        <common:title lang="NO">
            <common:non-sensitive>Tittel</common:non-sensitive>
        </common:title>
        <common:description>Melding til signatar</common:description>
    </primary-document>
    <exit-urls>
        <completion-url>https://www.sender.org/completed</completion-url>
        <cancellation-url>https://www.sender.org/canceled</cancellation-url>
        <error-url>https://www.sender.org/failed</error-url>
    </exit-urls>
</direct-signature-job-request>
```

Som respons på dette kallet vil man få en respons definert av elementet `direct-signature-job-response`. 

* Denne responsen inneholder en URL (`redirect-url`) som man redirecter brukerens nettleser til for å starte signeringsseremonien.
* I tillegg inneholder den en URL du benytter for å spørre om status på oppdraget. Her skal man **IKKE** benytte seg av polling, man skal derimot vente til brukeren returneres til en av URLene definert i requesten, for deretter å gjøre kallet. For å forhindre polling kreves det et token som du får tilbake ved redirecten, se Steg 3 for nærmere forklaring.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<direct-signature-job-response xmlns="http://signering.digipost.no/schema/v1/signature-job">
    <id>1</id>
    <redirect-url>
        https://signering.posten.no#/redirect/421e7ac38da1f81150cfae8a053cef62f9e7433ffd9395e5805e820980653657
    </redirect-url>
    <status-url>https://api.signering.posten.no/signature-jobs/1/status</status-url>
</direct-signature-job-response>
```

#### Steg 2: signeringsseremonien

Hele dette steget gjennomføres i signeringsportalen. Du redirecter brukeren til portalen ved å benytte URLen du får som svar på opprettelsen av oppdraget. Denne linken inneholder et engangstoken generert av signeringstjenesten, og det er dette tokenet som gjør at brukeren får tilgang til å lese dokumentet og gjennomføre signeringen.

**Noen ord om sikkerheten her:** For å håndtere sikkerheten i dette kallet vil dette tokenet kun fungere én gang. Brukeren vil få en cookie av signeringstjenesten ved første kall, slik at en eventuell refresh ikke stopper flyten, men du kan ikke bruke denne URLen på et senere tidspunkt. Årsaken til at vi kun tillater at den brukes én gang er at URLer kan fremkomme i eventuelle mellomtjeneres logger, og de vil dermed ikke være sikre etter å ha blitt benyttet første gang.

Brukeren gjennomfører signeringsseremonien, og blir deretter sendt tilbake til din portal via URLen spesifisert av deg i `completion-url`. På slutten av denne URLen vil det legges på et query-parameter du senere skal benytte når du spør om status.

*(OBS: Dette query-parameteret er pr. desember 2015 ikke på plass i APIet enda)*

#### Steg 3: hent status

Når brukeren blir sendt tilbake til din portal, så skal du gjøre et bak-kanal-kall (`HTTP GET`) for å hente ned status. Dette gjøres ved å benytte `status-url` du fikk i Steg 1, pluss query-parameter du fikk i Steg 2.

Du skal ikke sende med noen andre data i dette kallet. 

Responsen fra dette kallet er definert gjennom elementet `direct-signature-job-status-response`. Et eksempel på denne responsen ved et suksessfullt signeringsoppdrag vises under:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<direct-signature-job-status-response xmlns="http://signering.digipost.no/schema/v1/signature-job">
    <id>1</id>
    <status>COMPLETED</status>
    <additional-info>
        <success-info>
            <links>
                <xades-url>https://api.signering.posten.no/signature-jobs/1/xades</xades-url>
                <pades-url>https://api.signering.posten.no/signature-jobs/1/pades</pades-url>
                <confirmation-url>https://api.signering.posten.no/signature-jobs/1/change-sender-status</confirmation-url>
            </links>
        </success-info>
    </additional-info>
</direct-signature-job-status-response>
```

#### Steg 4: laste ned PAdES eller XAdES

I forrige steg fikk du to lenker: `xades-url` og `pades-url`. Disse kan du gjøre en `HTTP GET` på for å laste ned det signerte dokumentet i de to formatene.

**XAdES** er et format som brukes til å styrke og standardisere signaturene som kommer fra e-ID-leverandørene. Formatet har støtte for langtidsvalidering, og gjør samtidig at man får ett format å forholde seg til, uavhengig av hvilken e-ID-leverandør som er brukt til signering.

**PAdES** er et signaturformat som inneholder originaldokumentet, alle signaturer og all informasjon som er nødvendig for å validere signaturen. Formatet er spesifisert av ETSI, og bygger på PDF. En unik egenskap med PAdES er at dokumentet kan åpnes i en vilkårlig PDF- leser. Adobe Reader (og eventuelle andre avanserte PDF lesere) vil også kunne vise frem deler av valideringsinformasjonen slik at sluttbrukeren selv kan se at dokumentet er gyldig signert. I tillegg ligger også XAdES-dokumentet vedlagt denne PDFen.

*Mer dokumentasjon av disse formatene kommer…*

#### Steg 5: Bekrefte ferdig prosessering

Til slutt gjør du et `HTTP POST`-kall mot `confirmation-url` for å bekrefte at du har prosessert jobben ferdig. Avhengig av om arkivopsjonen benyttes, så vil dette enten slette oppdraget i signeringsportalen, eller markere oppdraget som ferdig og arkivert.

*Mer informasjon om dette kallet kommer senere…*


### API-flyt for Asynkrone signeringsoppdrag

Kommer…
