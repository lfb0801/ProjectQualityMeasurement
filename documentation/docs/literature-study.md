# Methode 1: Library – Literature Study

## Doel

Het doel van deze literatuurstudie is om internationaal erkende softwarekwaliteitsmetingen te identificeren en analyseren. Deze metrieken worden vergeleken met wat er binnen onze organisatie (een Java-project) momenteel wordt gebruikt. Dit geeft inzicht in de dekkingsgraad van de huidige meetpraktijken en waar er ruimte is voor verbetering.

## Zoekstrategie

Er is gebruik gemaakt van de volgende zoektermen om relevante bronnen te identificeren:

- "software quality metrics"
- "software maintainability metrics"
- "technical debt measurement"
- "code complexity tools Java"
- "defect density in Java software"
- "software metric validation"

**Gebruikte bronnen:**

- Google Scholar
- Google

## Belangrijke kwaliteitskenmerken en metrieken

Hieronder een overzicht van veelgebruikte kwaliteitskenmerken, bijbehorende metrieken en tools specifiek gericht op Java-omgevingen:

| Kwaliteitskenmerk    | Veelgebruikte metrieken                     | Tools (Java-specifiek)                          |
|----------------------|---------------------------------------------|--------------------------------------------------|
| **Maintainability**  | Maintainability Index, Code Smells          | SonarQube, Structure101, PMD                    |
| **Code Complexity**  | Cyclomatic Complexity, Halstead Metrics     | SonarQube, Checkstyle, CodeMR                  |
| **Test Coverage**    | Line/Branch Coverage                        | JaCoCo, Cobertura, Pitest (mutation testing)   |
| **Defect Density**   | Bugs per KLOC                               | SonarQube, combinatie met Jira of GitLab Issues |
| **Code Duplication** | Percentage duplicated code                  | SonarQube, PMD, Checkstyle                     |
| **Code Churn**       | Frequentie van wijzigingen                  | Git, CodeScene, GitHub Insights                |
| **Bus Factor**       | Aantal unieke auteurs per bestand           | Git log analyse, Gitinspector, CodeScene       |
| **Dependency Risk**  | Aantal en diepte van afhankelijkheden       | Structure101, JDepend, Deptective              |

## Vergelijking met praktijk

Binnen onze organisatie wordt reeds gebruikgemaakt van enkele metrieken, waaronder:

- **Cyclomatic Complexity** (via SonarQube)
- **Code Smells en Duplicatie** (via SonarQube)
- **Test Coverage** (via JaCoCo)

Wat nog ontbreekt of onderbenut is:

- Validatie van metrieken: er wordt niet structureel gekeken naar correlaties met incidenten of onderhoudskosten.
- Metingen zoals **Code Churn**, **Bus Factor** en **Defect Density** worden zelden meegenomen, terwijl deze in de literatuur als belangrijke indicatoren voor technische schuld gelden.
- Analyse van **dependency risks** vindt nauwelijks plaats.
- Zoals beschreven in het **oreilly artikel[^maintainability]** zouden we 10.000 regels verandering per persoon verwachten, maar er is nu geen beeld over het aantal regels per persoon

## Conclusie

De literatuurstudie bevestigt dat er een breed scala aan kwaliteitsmetingen beschikbaar is voor Java-projecten. Onze organisatie gebruikt slechts een beperkt aantal van deze metrieken. Er is ruimte voor verbreding (bijvoorbeeld met dependency- en bus factor-analyse) én voor verdieping, door bestaande metrieken te koppelen aan historische data en validatie-analyses.

Deze inzichten vormen een stevige basis voor vervolgstappen binnen het project:

- Inzichtelijk maken welke metrieken bruikbare voorspellers zijn van onderhoudsproblemen.
- Vergroten van het vertrouwen in meetresultaten bij stakeholders.
- Voorbereiding op de implementatie van een dashboard dat daadwerkelijk zinvolle kwaliteitsdata toont.

[^maintainability]: <https://www.oreilly.com/content/why-you-need-to-know-about-code-maintainability/>.
