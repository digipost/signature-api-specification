# Posten Signering – API spesifikasjon

Dette repoet inneholder informasjon om APIene til Posten Signering. 

Mappen `src/main/xsd` inneholder XSD-filer som spesifiserer de ulike objektene som sendes og mottas av APIet.

## Tre ulike scenarier for signeringsoppdrag

Posten Signering er laget med tanke på å håndtere 3 ulike scenarier for signering:

1. **Synkrone signeringsoppdrag med maskin-til-maskin-integrasjon:** Dette scenariet er aktuelt hvis sluttbruker er i en sesjon hos tjenesteeier på tjenesteeiers nettsider. Her ønsker tjenesteeier at sluttbruker skal signere et dokument. Sluttbruker opplever signaturprosessen som en integrert del av tjenesteeiers nettsted.
2. **Asynkrone signeringsoppdrag med maskin-til-maskin-integrasjon:** Dette scenariet er aktuelt der det er ønskelig med signering av dokumenter uten at sluttbruker som skal signere er i en sesjon på tjenesteeierens nettside. Dette kan for eksempel være ved batch- utsending av dokumenter som skal signeres uten at sluttbruker er til stede på tjenesteeiers nettsted.
3. **Manuelle asynkrone signeringsoppdrag:** Dette scenariet er aktuelt dersom Signeringstjenesten skal benyttes av enten en mindre tjenesteeier, eller en avdeling av en større tjenesteeier der den aktuelle prosessen er manuell. Denne flyten gjennomføres utelukkende via signeringsportalen, og man bruker derfor ikke APIer for integrasjonen.

## Integrasjon vha. klientbiblioteker

Det blir laget klientbiblioteker for både Java og .NET, som forenkler integrasjonsprosessen. Se følgende GitHub-repoer for mer informasjon om integrasjon vha. klientbibliotekene:

* [Klientbibliotek for Java](https://github.com/digipost/signature-api-client-java)
* *[Klientbibliotek for .NET](https://github.com/digipost/signature-api-client-dotnet)

*OBS: klientbibliotekene er pr. Desember 2015 "work in progress", og er dermed hverken fullstendig implementert eller releaset i stabil versjon*

## Manuell integrasjon

Før du starter integrasjonen, er det viktig å bestemme seg for hvilket scenarie som passer best til deres integrasjon. Da disse to flytene har en del viktige forskjeller, så er det laget to ulike sett med endepunkter du skal integrere med. Det er også en del felles funksjonalitet du uansett trenger å implementere, slik at du kan gjenbruke mye dersom dere skulle ha behov for å implementere begge flytene.

Nedenfor finner du informasjon om integrasjon i følgende fire kapitler:

* **[FELLES] Sikkerhetsmekanismene** gir en innføring i hvordan sikkerheten er implementert i APIene. Fokuset her er å forklare hvordan du skal integrere, ikke alle detaljene om mekanismene. Dette er felles for begge APIene.
* **[FELLES] Dokumentpakken** forklarer hvordan man bygger opp en komplett dokumentpakke bestående av dokumentet som skal signeres av sluttbruker og metadata om dokumentet
* **[SCENARIE 1] API-flyt** gir en kort innføring i hvordan en normal flyt gjennom APIet implementeres for scenariet *Synkrone 
* signeringsoppdrag med maskin-til-maskin-integrasjon*
* **[SCENARIE 1] API-flyt** gir en kort innføring i hvordan en normal flyt gjennom APIet implementeres for scenariet *Asynkrone signeringsoppdrag med maskin-til-maskin-integrasjon*

### [FELLES] Sikkerhetsmekanismene

SIkkerheten i APIet til Posten SIgnering er implementert vha. toveis TLS. For å benytte APIene trenger du et [godkjent virksomhetssertifikat](https://www.regjeringen.no/no/dokumenter/kravspesifikasjon-for-pki-i-offentlig-se/id611085/) (for eksempel fra [Buypass](https://www.buypass.no/produkter-og-tjenester/virksomhetssertifikat) eller [Commfides](https://www.commfides.com/e-ID/Bestill-Commfides-Virksomhetssertifikat.html)).

De fleste HTTP-klienter har innebygget støtte for TO-veis SSL. Du kan se eksempler på implementasjonen i våre klientbiblioteker. 

Du benytter ditt eget sertifikat i keystore'n, og legger til Buypass sitt rotsertifikat i truststoren. Deretter validerer du at sertifikatet tilhører Posten Norge AS, vha å sjekke organisasjonsnummer som står i Common Name. Et godt tips kan være å benytte Difi sin sertifikatvalidator som er tilgjengelig på [GitHub](https://github.com/difi/certvalidator).

### [FELLES] Dokumentpakken

Dokumentpakken i Posten Signering er basert på ASiCE-standarden. Pakken skal inneholde dokumentene som skal signeres (PDF-filer eller TXT-filer), en fil kalt `manifest.xml` som beskriver metadata for dokumentet (emner, hvem som skal signere osv), pluss en fil kalt `signatures.xml` som er signaturen over hele dokumentpakken.

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

`signatures.xml` følger skjemaet `http://uri.etsi.org/2918/v1.2.1#`, se mappen `thirdparty` i dette repoet for kopier at de relevante standardskjemaene. Følgende er et eksempel på en komplett fil:

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

### [SCENARIE 1] API-flyt

TODO

### [SCENARIE 2] API-flyt

TODO