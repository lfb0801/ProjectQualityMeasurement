# Projectoverzicht

Binnen de huidige ontwikkelomgeving ontbreekt het aan voldoende inzicht in de kwaliteit van de software. Dit gebrek leidt tot problemen op het gebied van technische schuld, productiviteit en communicatie tussen ontwikkelaars en productmanagement. Dit project beoogt hier verandering in te brengen door het opzetten van een geautomatiseerd en transparant dashboard voor kwaliteitsmetingen, afgestemd op de behoeften van zowel technische als niet-technische stakeholders.

Het uiteindelijke doel is het verbeteren van besluitvorming, het kunnen onderbouwen en prioriteren van technische verbeteringen, en het bevorderen van de onderhoudbaarheid van software op de lange termijn. Het dashboard zal geïntegreerd worden in de bestaande ontwikkelprocessen van PinkRoccade Local Government en moet leiden tot duurzame verbeteringen in softwarekwaliteit en samenwerking.

# Onderzoeksvraag

Om richting te geven aan het onderzoek en de ontwikkeling van het framework is de volgende onderzoeksvraag geformuleerd:

**Op welke wijze kunnen softwarekwaliteitsmetingen systematisch worden geselecteerd, gevalideerd en gecommuniceerd, zodat ze bijdragen aan onderbouwde besluitvorming over technische schuld en veranderbaarheid binnen softwaresystemen?**

Deze vraag weerspiegelt de dubbele focus van het project: het opzetten van een technisch onderbouwde meetstructuur én het waarborgen dat de verkregen inzichten bruikbaar zijn voor verschillende doelgroepen binnen de organisatie. Daarbij staat de praktische toepasbaarheid centraal: de metingen moeten concrete impact hebben op codekwaliteit, teamgedrag en besluitvorming.

# Reflectie op reikwijdte

De onderzoeksvraag is ambitieus en raakt drie domeinen: selectie, validatie en communicatie van kwaliteitsmetingen. In de uitwerking zal afhankelijk van tijd en haalbaarheid mogelijk de nadruk sterker op één of twee domeinen komen te liggen, met de derde als ondersteunend element. Dit zorgt voor diepgang zonder het project onhaalbaar te maken.

# Aangepaste Onderzoeksvraag

**Op welke wijze kunnen bestaande softwarekwaliteitsmetingen gevalideerd worden, zodat ze betrouwbare en bruikbare input leveren voor besluitvorming over technische schuld en veranderbaarheid binnen softwaresystemen?**

# Toelichting

Hoewel de selectie en communicatie van metrieken belangrijke onderdelen zijn in het bredere kader van kwaliteitsverbetering, richt dit project zich primair op de **validatie** van reeds aanwezige softwarekwaliteitsmetingen. Binnen de organisatie bestaan al diverse metrieken, maar het is onduidelijk in hoeverre deze daadwerkelijk representatief zijn voor de kwaliteit van de software en de onderliggende technische schuld.

De centrale aanname is dat **betrouwbare validatie** een noodzakelijke voorwaarde is voor effectieve communicatie met stakeholders én voor het maken van zinvolle keuzes op basis van kwaliteitsdata. Door te focussen op validatie, kunnen bestaande metrieken worden geëvalueerd op bruikbaarheid, nauwkeurigheid, en voorspellende waarde — bijvoorbeeld door ze te koppelen aan historische trends, incidenten of ontwikkeldata.

# Gevolg voor projectfocus

- **Selectie** van metrieken blijft beperkt tot het inventariseren van wat er al is. Nieuwe metrieken worden alleen overwogen als blijkt dat bestaande metrieken bepaalde aspecten niet dekken.
- **Validatie** vormt het zwaartepunt van het onderzoek en richt zich op vragen als: _Wat meten we écht? Waar zitten inconsistenties? In hoeverre correleren metrieken met werkelijke technische schuld of wijzigingslast?_
- **Communicatie** wordt meegenomen in de vorm van een eerste opzet voor dashboarding, maar volgt als logisch vervolg op het validatieonderzoek.

# Deelvragen

- Hoe bepalen bestaande tools de kwaliteit van een applicatie?
- Hoe spelen regels code mee in code kwaliteit?
- Wat is Sonar ontwijkend gedrag? en wat is het gevolg van dit gedrag op kwaliteitsmetingen?
- Kunnen we een kwaliteitsmeting/oordeel over de code maken zonder tegen Goodhart's law aan te lopen?
- Wat gebeurt er als we een LOC count van iedere directory naast de hoeveelheid commits leggen?
