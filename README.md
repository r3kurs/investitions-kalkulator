# Investitions-Kalkulator

Der **Investitions-Kalkulator** ist eine kompakte Android-App für Trader. Sie berechnet, wie viele Wertpapier-Stücke du bei einem Trade kaufen kannst, damit dein maximaler Verlust zu deinem definierten Risiko passt.

<blockquote>
<details>
<summary>Details zur Berechnung von Risiko und Positionsgröße (ausklappbar)</summary>

Mit anderen Worten hilft die App dabei, die passende **Positionsgröße** für einen geplanten Trade zu bestimmen.

Voraussetzung dafür ist immer ein vorher definierter **Trading-Plan**! Dazu gehören insbesondere ein geplanter Einstiegskurs, ein StoppLoss-Kurs zur Verlustbegrenzung und idealerweise auch ein Verkaufskurs für die Gewinnmitnahme.

Gerade für unerfahrene Trader ist die Bestimmung der Positionsgröße oft schwierig. Aber auch erfahrene Trader profitieren davon, weil sich die Stückzahl auf Basis des vorhandenen Handelskapitals und des selbst festgelegten Risikos schnell und nachvollziehbar berechnen lässt.

Das **Risikokapital** ergibt sich aus dem investierbaren Vermögen multipliziert mit dem gewählten Risiko-Prozentsatz. Beispiel: Bei 10.000 € Vermögen und 2 % Risiko beträgt das Risikokapital 200 €. Werden zusätzlich 10 € Gebühren berücksichtigt, bleiben als Einsatz für den eigentlichen Trade noch 190 € übrig.

Die Anzahl der **Wertpapier-Stücke** ergibt sich anschließend aus dem noch verfügbaren Risikokapital geteilt durch das Risiko pro Stück. Das Risiko pro Stück ist die Differenz zwischen Kaufkurs und StoppLoss-Kurs. Beispiel: Liegt der Kaufkurs bei 50 € und der StoppLoss bei 47 €, dann beträgt das Risiko pro Stück 3 €. Bei 190 € verfügbarem Risikokapital können somit 63 Stück gekauft werden; in der Praxis wird auf ganze Stücke abgerundet.

Wichtig ist dabei: Der berechnete maximale Verlust ist ein Planwert unter normalen Annahmen. Der tatsächliche Verlust kann davon abweichen, etwa durch Gebühren, Slippage, die Art der verwendeten Stopp-Order oder durch Kurslücken. Bei einer Kurslücke kann es passieren, dass ein StoppLimit gar nicht ausgeführt wird oder ein StoppLoss erst zu einem deutlich ungünstigeren Kurs ausgeführt wird.

</details>
</blockquote>

Die App ist eine Rechenhilfe für Risiko- bzw. Positionsgrößen-Berechnungen und stellt keine Finanzberatung dar.

## Funktionen

- Berechnung der **Wertpapier-Stücke**
- Berechnung des **Kaufwerts**
- Berechnung des **maximalen Verlusts**
- Speicherung allgemeiner Vorgaben
- Anzeige des berechneten Risikokapitals
- Plausibilitätsprüfungen und entsprechende Hinweise
- Eingabefehler werden möglichst sofort erkannt
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

## Veröffentlichung und Versionen

Die aktuelle Version kann man über den
[**Latest Release**](https://github.com/r3kurs/investitions-kalkulator/releases/latest)
dieses GitHub-Projekts herunter laden.

Dort unter Assets die Datei "investitions-kalkulator-vX.Y.Z.apk" runter laden.

Alle veröffentlichten Versionen kann man über den Bereich [**Releases**](https://github.com/r3kurs/investitions-kalkulator/releases) dieses GitHub-Projekts einsehen.

## Installation

1. Lade die aktuelle APK-Datei aus dem Assets-Abschnitt des [**Latest Release**](https://github.com/r3kurs/investitions-kalkulator/releases/latest) herunter.
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

## Lizenz

Dieses Projekt ist unter der GNU General Public License Version 3 (GPLv3) lizenziert. Siehe die Datei LICENSE für Details.

## Hinweise

Diese App ist KI-generiert.

Creator: **r3kurs**

Diese App wurde mit Hilfe der KI Perplexity generiert, von mir ausführlich getestet, aber der Code nur oberflächlich geprüft. Ich bin kein App-Experte und freue mich, wenn Fachleute den Code zusätzlich überprüfen.
