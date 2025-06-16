# Verzamelen van data

## Inleiding

Binnen onze organisatie ontbreekt het momenteel aan een helder beeld van de kwaliteit van onze software. Hierdoor worden beslissingen over technische schuld, onderhoudbaarheid en productiviteit genomen zonder voldoende onderbouwing. Dit document onderzoekt met behulp van drie verschillende onderzoeksmethoden hoe wij momenteel de kwaliteit van onze codebase meten en waar verbeterpotentieel ligt.

Hiermee wil ik de volgende deelvraag beantwoorden:

> Op welke manieren wordt de kwaliteit van onze huidige codebase op dit moment gemeten, en in hoeverre sluiten deze metingen aan bij erkende kwaliteitsindicatoren uit literatuur en praktijk?

---

## Methode 1: Library – Literature Study

link: <https://www.ictresearchmethods.nl/library/literature-study>

### Doel

Het in kaart brengen van erkende methoden en kwaliteitsindicatoren uit de wetenschappelijke literatuur die softwarekwaliteit meten, en deze vergelijken met de metrieken die wij in de praktijk gebruiken.

### Opzet

- Bepalen van relevante zoektermen om mogelijke metrieken te bepalen en tools te vinden waarmee we verschillende metrics kunnen meten.
- Analyseren van relevante kwaliteitskenmerken zoals maintainability, code complexity, test coverage, defect density, etc.
- Verzamelen van literatuur uit verschillende bronnen.
- Vergelijken van deze metrieken met de huidige meetpraktijk binnen onze organisatie.

### Verwachte uitkomst

Een overzicht van internationaal erkende kwaliteitsmetingen en de tools waarmee deze gemeten kunnen worden, samengebracht in een tabel die per metriek aangeeft met welke tools deze gegenereerd kan worden.

---

## Methode 2: Field – Problem Analysis

link: <https://www.ictresearchmethods.nl/field/problem-analysis/>

### Doel

Begrijpen waarom bestaande softwarekwaliteitsmetingen in de praktijk mogelijk niet effectief of onvoldoende bruikbaar zijn. Dit helpt bij het achterhalen van knelpunten in de interpretatie, adoptie of toepasbaarheid van metrieken binnen teams.

### Opzet

- Uitvoeren van probleemanalyses met verschillende stakeholders (developers, testers, product owners).
- Gebruik maken van interviews en observaties om bestaande frustraties, knelpunten en onduidelijkheden rond softwarekwaliteit en technische schuld in kaart te brengen.
- Analyseren welke inzichten ontbreken om betere beslissingen te nemen over onderhoud of herontwikkeling.
- Thematische analyse van kwalitatieve data om terugkerende obstakels te identificeren.

### Verwachte uitkomst

Een overzicht van structurele problemen in het huidige gebruik of begrip van metrieken binnen de organisatie. Deze analyse biedt aanknopingspunten voor verbetering in tooling, communicatie of validatie van kwaliteitsdata.

---

## Methode 3: Showroom – Static Program Analysis

link: <https://www.ictresearchmethods.nl/showroom/static-program-analysis/>

### Doel

Objectief in kaart brengen welke kwaliteitsmetingen al aanwezig zijn binnen het huidige systeem en onderzoeken in hoeverre deze representatief zijn voor onderhoudbaarheid, technische schuld en complexiteit.

### Opzet

- Toepassen van statische analyse tools (zoals SonarQube, CodeScene of Structure101) op een representatief deel van de codebase.
- Extractie van bestaande metrieken zoals:
  - **Cyclomatic complexity**
  - **Duplicatiegraad**
  - **Code churn**
  - **Code smells**
  - **Dependency metrics**
  - **Lines of Code (LoC)** per bestand/module
- Analyse van **Git history**:
  - Wijzigingsfrequentie van bestanden
  - Aantal auteurs per bestand (bus factor)
  - Koppelpunten tussen commits en incidenten of bugs
  - Tijdsduur tussen eerste wijziging en incidentmelding
- Evaluatie van hoe deze metrieken zich verhouden tot erkende kwaliteitsmodellen en onderhoudbaarheid.
- Onderzoeken of bepaalde metrieken over- of ondervertegenwoordigd zijn in de bestaande tooling.

### Verwachte uitkomst

Een overzicht van de huidige statische kwaliteitsmetingen, inclusief dekking, volledigheid en mogelijke gaten. Door Git history en Lines of Code mee te nemen, ontstaat een rijker beeld van de evolutie, risico’s en complexiteit van de codebase. Deze inzichten ondersteunen de validatie van bestaande metrieken en vormen een sterke basis voor dashboarding en datagedreven besluitvorming.
