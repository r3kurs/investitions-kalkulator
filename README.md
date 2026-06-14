# Investitions-Kalkulator

Der **Investitions-Kalkulator** ist eine kompakte Android-App für Trader. Sie berechnet, wie viele Wertpapier-Stücke du bei einem Trade kaufen kannst, damit dein maximaler Verlust zum eigenen Risiko passt.

Die App dient als Rechenhilfe. Sie stellt keine Finanzberatung dar.

## Funktionen

- Berechnung der **Wertpapier-Stücke**
- Berechnung des **Kaufwerts**
- Berechnung des **maximalen Verlusts**
- Speicherung allgemeiner Vorgaben
- Anzeige des berechneten Risikokapitals
- Warnhinweis, wenn der Kaufwert größer als das investierbare Vermögen ist
- Hinweis, wenn die berechneten Wertpapier-Stücke `0` ergeben
- Deutsche Zahlenformate mit Komma und Tausenderpunkt
- Hilfe-Dialog direkt in der App
- Funktioniert offline
- Benötigt keine zusätzlichen Berechtigungen

## Allgemeine Vorgaben

- **Investierbares Vermögen**: Dein gesamtes verfügbares Kapital für Wertpapierhandel.
- **Risiko (%)**: Der Anteil deines Vermögens, den du pro Kauf maximal riskieren möchtest. Übliche Werte liegen zwischen 1 bis 3 %.
- **Risikokapital**: Wird automatisch aus Vermögen und Risiko berechnet.
- **Gebühren**: Alle Handelsgebühren (Kauf und Verkauf). Wird vor Berechnung vom Risikokapital abgezogen.

## Order Angaben

- **Kaufkurs**: Geplanter Kaufpreis pro Stück.
- **StoppLoss-Kurs**: Kurs, bei dem du die Position zur Verlustbegrenzung verkaufen würdest.

## Ergebnis

- **Wertpapier-Stücke**: Anzahl der Stücke, abgerundet auf eine ganze Zahl.
- **Kaufwert**: Kaufkurs × Wertpapier-Stücke.
- **Maximaler Verlust**: Differenz zwischen Kaufwert und Verkaufswert bei StoppLoss.

## Screenshots

Die folgenden Ansichten zeigen die Hauptansicht und den Hilfebildschirm der App.

<p align="center">
  <img src="./investitionskalkulator%20_mainscreen.jpg" width="300" alt="Hauptansicht der App" />
  <img src="./investitionskalkulator%20_helpscreen.jpg" width="300" alt="Hilfebildschirm der App" />
</p>

## Installation

1. Lade die aktuelle APK-Datei aus dem Bereich **Releases** herunter.
2. Öffne die APK-Datei auf deinem Android-Gerät.
3. Erlaube bei Bedarf die Installation aus unbekannten Quellen.
4. Installiere die App.

Hinweis: Da die App nicht aus dem Google Play Store installiert wird, kann Android eine Sicherheitsabfrage anzeigen.

<details>
<summary>Apps aus unbekannten Quellen installieren (Android)</summary>

> Achtung: APKs nur aus vertrauenswürdigen Quellen installieren, da Apps von fremden Seiten Schadsoftware enthalten oder Daten stehlen können.

**Schritt-für-Schritt (Text)**  
- Heise: Externe Apps/APK-Dateien installieren  
  https://www.heise.de/tipps-tricks/Externe-Apps-APK-Dateien-bei-Android-installieren-so-klappt-s-3714330.html  

- CHIP: Android-Apps manuell installieren  
  https://www.chip.de/ratgeber/handy/android-apps-manuell-installieren-so-geht-s_efc29566-8a1a-4b09-a6f4-0e19c07815e0.html  

- Tutonaut: Apps aus unbekannten Quellen installieren  
  https://www.tutonaut.de/anleitung-android-apps-unbekannten-quellen-installieren/  

**Videoanleitungen**  
- Unbekannte Apps installieren (Android 13)  
  https://www.youtube.com/watch?v=z6tfYtFhx8s  

- Unbekannte Apps installieren (Android 14)  
  https://www.youtube.com/watch?v=JXaT-4-kE3k  

- Unbekannte Apps installieren (Android 15)  
  https://www.youtube.com/watch?v=zqobQ9bouY8  

</details>

## Veröffentlichung

Die aktuelle Version kann über den Bereich **Releases** dieses GitHub-Projekts heruntergeladen werden.

Empfohlener Dateiname:

```text
investitions-kalkulator-v1.1.2.apk
```

## Version

Aktuelle Version: **1.1.2**

## Hinweise

Diese App ist KI-generiert.

Creator: **r3kurs**

Diese App wurde mit Hilfe der KI Perplexity generiert, von mir getestet und der Code nur oberflächlich geprüft. Ich bin kein App-Experte und freue mich, wenn Fachleute den Code zusätzlich überprüfen.
