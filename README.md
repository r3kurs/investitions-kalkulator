# Investitions-Kalkulator

Eine einfache Android-App zur Berechnung der passenden Wertpapier-Stücke für einen Trade.

Der **Investitions-Kalkulator** berechnet, wie viele Wertpapier-Stücke du bei einem Trade kaufen kannst, damit dein maximaler Verlust zum akzeptierten Risiko passt.

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

## Installation

1. Lade die aktuelle APK-Datei aus dem Bereich **Releases** herunter.
2. Öffne die APK-Datei auf deinem Android-Gerät.
3. Erlaube bei Bedarf die Installation aus unbekannten Quellen.
4. Installiere die App.

Hinweis: Da die App nicht aus dem Google Play Store installiert wird, kann Android eine Sicherheitsabfrage anzeigen.

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

Die App dient als Rechenhilfe. Sie stellt keine Finanzberatung dar.
