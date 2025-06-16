# Wat is de onderhoudscapaciteit van Code per Ontwikkelaar en moeten we dit meenemen de kwaliteitsmeting?

## Historische Normen en Vroege Studies

Vroeg onderzoek in software-engineering suggereerde dat de productiviteit van een individuele ontwikkelaar relatief bescheiden is wanneer gemeten in regels code (LOC) per jaar. Frederick Brooks stelde in _The Mythical Man-Month_ (1975) vast dat de productiviteit op IBM OS/360-projecten lag op slechts enkele duizenden instructies per **persoon-jaar**. Hij schatte zelfs dat een ontwikkelaar gemiddeld **10 gedebugde regels code per dag** produceert – ruwweg **2.000 LOC per jaar**[^1].  
Modellen zoals **COCOMO** (Constructive Cost Model) uit de jaren ’80 bevestigden deze lage waarden: bij een klein project van 10K LOC was de opbrengst per ontwikkelaar tussen de 2.000–25.000 LOC/jaar, terwijl dat bij een megaproject van 10 miljoen LOC daalde tot slechts **300–5.000 LOC/jaar**[^2].

## Typische Jaarlijkse Onderhoudscapaciteit – Industriële Gemiddelden

Later onderzoek toont aan dat een **gemiddelde professionele ontwikkelaar** ongeveer **3.000 tot 8.000 LOC per jaar** onderhoudt, met veel variatie.  
Bijvoorbeeld:

- Een studie van Cusumano _et al._ (2003) over 104 projecten vond een mediaan van **17,6 LOC per dag** (ongeveer 4.400 LOC/jaar)[^3].
- Een analyse van Amerikaanse defensie- en IT-projecten (2004–2010) toonde aan dat productiviteit varieerde van minder dan **1.000 LOC/jaar** (complexe systemen) tot **4.000+ LOC/jaar** (eenvoudigere systemen)[^4].

Experts schatten dat een goede ontwikkelaar tegenwoordig ongeveer **10.000 LOC per jaar** kan onderhouden[^5].

## Invloed van Grootte en Complexiteit van de Codebase

Onderzoek toont aan dat de **onderhoudscapaciteit per ontwikkelaar afneemt naarmate de codebase en het team groeien**. Voorbeelden:

- In grote projecten zoals Microsoft Windows Vista lag de productiviteit op ongeveer **1.000 LOC per ontwikkelaar per jaar**[^6].
- In kleinere, efficiënte teams kan dat veel hoger liggen. Een bekend voorbeeld is Borland Quattro Pro in de jaren ’90: ongeveer **1.000 LOC per week per ontwikkelaar** – oftewel **50.000 LOC/jaar**, een uitzonderlijk hoge waarde[^6].

**Domein speelt ook een rol**: bijv. veiligheid-kritische software vereist meer effort per regel dan een businessapplicatie, en dus lagere LOC/jaar[^4].

## Ervaring en Vertrouwdheid met de Applicatie

**Ervaring en vertrouwdheid met de codebase hebben grote invloed op hoeveel code een ontwikkelaar kan onderhouden**:

- Nieuwe ontwikkelaars hebben vaak een lange **inwerkperiode** (meerdere maanden) voordat ze net zo productief zijn als ervaren teamleden[^7].
- Ervaren ontwikkelaars of kernbijdragers (zoals in open-source) onderhouden **veel meer** LOC dan nieuwkomers[^8].
- Bij consultancybureaus zoals SIG wordt aangenomen dat een goede ontwikkelaar ongeveer **10.000 regels per jaar** onderhoudt, en dat **ongeveer 15% van een codebase jaarlijks moet worden aangepast** om modern te blijven[^5].

## Samenvatting van de Bevindingen

Ondanks vooruitgang in tooling en talen, blijkt uit studies dat een ontwikkelaar effectief **slechts een paar duizend regels code per jaar** kan onderhouden.

- **Gemiddeld bereik**: **4.000–8.000 LOC per jaar**
- **Lage kant**: ~1.000–2.000 LOC/jaar bij grote of complexe projecten
- **Uitzonderlijk hoge kant**: tot **50.000 LOC/jaar** in kleine, snelle teams
- **Sterke invloed** van ervaring en codebase-vertrouwdheid op deze cijfers

Dit benadrukt waarom het belangrijk is om de **omvang en complexiteit van software te beheersen** – er is een natuurlijke grens aan hoeveel code een menselijk brein effectief kan onderhouden, meestal tussen de **1.000 en 10.000 regels per jaar**.

---

## Bronnen

[^1]: Brooks, F. P. (1975). _The Mythical Man-Month_. Addison-Wesley.
[^2]: Boehm, B. W. (1981). _Software Engineering Economics_. Prentice-Hall.
[^3]: Cusumano, M. A., & Selby, R. W. (2003). _Microsoft Secrets: How the World's Most Powerful Software Company Creates Technology, Shapes Markets, and Manages People_.
[^4]: U.S. Department of Defense. (2004–2010). _Software Technology Support Center (STSC) Software Facts and Figures_.
[^5]: Software Improvement Group (SIG). Whitepapers en blogpublicaties over software-onderhoud (SIG.eu).
[^6]: McConnell, S. (2006). _Software Estimation: Demystifying the Black Art_. Microsoft Press.
[^7]: Microsoft Research. Studies over onboarding en productiviteit van nieuwe ontwikkelaars, waaronder: "The First Day" en "How Developers Learn".
[^8]: Crowston, K., & Howison, J. (2005). _The social structure of free and open source software development_. First Monday, 10(2).
