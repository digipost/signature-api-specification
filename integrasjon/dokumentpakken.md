# Dokumentpakken

Dokumentpakken i Posten signering er basert på ASiC-E standarden ([Associated Signature Containers, Extended form](http://www.etsi.org/deliver/etsi_ts/102900_102999/102918/01.03.01_60/ts_102918v010301p.pdf)). Profilen er lagd for å ligne på den som er brukt for [sikker digital post](http://begrep.difi.no/SikkerDigitalPost). Les mer om [profilen som er benyttet for ASiC](asic-profile.textile).

Pakken skal inneholde dokumentet som skal signeres (en PDF eller ren tekstfil), en fil kalt `manifest.xml` som beskriver metadata for dokumentet (emner, hvem som skal signere osv.), samt en fil kalt `signatures.xml` som er signaturen over hele dokumentpakken.

### Dokument
Filen er enten PDF eller en ren tekstfil, og kan maksimalt være 3 MB (3 145 728 bytes) stor. Denne filen refereres til med det påkrevde `href`-attributtet i `document`-elementet i `manifest.xml`. Se eksempler for [synkrone](/schema/examples/direct/manifest.xml#L10) og [asynkrone](/schema/examples/portal/manifest.xml#L34) oppdrag.

### `manifest.xml`
Filen følger skjemaet `http://signering.posten.no/schema/v1` som finnes i dette repoet. Eksempler finnes under «Steg 1: opprette signeringsoppdraget» for de ulike integrasjonsmønstrene.

### `signatures.xml`
Filen følger skjemaet `http://uri.etsi.org/2918/v1.2.1#`. Se katalogen [`thirdparty`](/schema/xsd/thirdparty) for kopier av de relevante standardskjemaene.

### Eksempel på komplett fil
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
