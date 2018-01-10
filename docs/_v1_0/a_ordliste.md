---
identifier: ordliste
title: Ordliste
layout: default
---

<a name="avsender"></a> **avsender** &mdash;
avsenderen av et [signeringsoppdrag](#sign-jobb). Dette er parten mottakere ser som avsender av dokumenter som skal signeres.

<a name="undertegner"></a> **undertegner** &mdash;
en mottaker av et dokument som skal signeres. Et [signeringsoppdrag](#sign-jobb) kan adresseres til flere undertegnere.

<a name="sign-jobb"></a> **signeringsoppdrag (alt: signeringsjobb)** &mdash;
en jobb inneholder et dokument som skal signeres, og kan adresseres til en eller flere [undertegnere](#undertegner) som skal signere.

<a name="sign-synkron"></a> **synkront signeringsoppdrag** &mdash;
signeringsoppdrag som signeres mens [undertegner](#undertegner) allerede er pålogget i et eksternt system. Selve signeringen blir dermed en del av en eksisterende flyt i [avsenders](#avsenders) system ved at undertegner blir _videresendt_ til en egen tjeneste hos Posten signering. Man utfører signeringen, og blir til slutt redirectet tilbake til avsenders applikasjon.

<a name="sign-asynkron"></a> **asynkront signeringsoppdrag** &mdash;
signeringsoppdrag som signeres i [signeringsportalen](#sign-portal).

<a name="sign-portal"></a> **signeringsportal** &mdash;
nettsted for [undertegnere](#undertegner). Her logger man seg inn og signerer eller avviser mottatte dokumenter.

<a name="org-portal"></a> **avsenderportal** &mdash;
nettsted for [avsendere](#avsender) hvor man kan sende ut dokumenter som skal signeres, samt administrere [signeringsoppdrag](#sign-jobb).

<a name="pades"></a> <abbr title="PDF Advanced Electronic Signatures">**PAdES**</abbr> &mdash;
PDF-fil som inneholder alle ferdige signaturer, samt originaldokumentet. En PAdES kan åpnes i en vanlig PDF-leser, og lesere som støtter det vil i tillegg vise hvilke [undertegnere](#undertegner) som har undertegnet dokumentet.

<a name="xades"></a> <abbr title="XML Advanced Electronic Signatures">**XAdES**</abbr> &mdash;
XML-representasjon av et dokument med digital signatur fra _én_ [undertegner](#undertegner).