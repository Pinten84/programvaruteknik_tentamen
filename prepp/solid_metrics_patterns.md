# SOLID Principles, Code Metrics och Design Patterns - En Enkel Förklaring

Detta dokument förklarar skillnaderna mellan tre viktiga koncept inom mjukvaruutveckling på ett sätt som är lätt att förstå.

## Snabb Översikt

| Koncept | Vad är det? | Syfte |
|---------|-------------|-------|
| **SOLID Principles** | Fem grundregler för bra kodstruktur | Hjälper dig skriva kod som är lätt att ändra och underhålla |
| **Code Metrics** | Mätverktyg för kodkvalitet | Mäter hur bra/dålig din kod är (som ett betyg) |
| **Design Patterns** | Färdiga lösningar på vanliga problem | Ger dig beprövade recept för hur man löser specifika problem |

---

## 1. SOLID Principles - Grundreglerna

**Tänk på det som:** Regler för att bygga ett stabilt hus.

SOLID är fem principer som hjälper dig skriva kod som är lätt att förstå, ändra och utöka:

### S - Single Responsibility Principle (Ett ansvar)
- **Enkelt:** Varje klass ska bara göra EN sak
- **Exempel:** En klass som hanterar spelare ska inte också hantera poängräkning
- **Varför:** Lättare att hitta och fixa buggar när varje del har ett tydligt syfte

### O - Open/Closed Principle (Öppen för utökning, stängd för ändring)
- **Enkelt:** Du ska kunna lägga till ny funktionalitet utan att ändra befintlig kod
- **Exempel:** Istället för att ändra i poängräkningsklassen när du lägger till nya regler, skapar du nya klasser
- **Varför:** Minskar risken att förstöra något som redan fungerar

### L - Liskov Substitution Principle (Utbytbarhet)
- **Enkelt:** Om du har en basklass, ska alla underklasser kunna användas på samma sätt
- **Exempel:** Om du har en "Djur"-klass, ska både "Hund" och "Katt" fungera överallt där "Djur" används
- **Varför:** Gör koden förutsägbar och pålitlig

### I - Interface Segregation Principle (Dela upp gränssnitt)
- **Enkelt:** Tvinga inte klasser att implementera metoder de inte behöver
- **Exempel:** En "Fågel"-klass ska inte tvingas ha en "simma()"-metod
- **Varför:** Håller koden enkel och relevant

### D - Dependency Inversion Principle (Beroendeinversion)
- **Enkelt:** Kod ska bero på abstraktioner, inte konkreta implementationer
- **Exempel:** Spelet ska bero på "ScoringStrategy" (abstrakt), inte "AnimalScoring" (konkret)
- **Varför:** Gör det lätt att byta ut delar av systemet

---

## 2. Code Metrics - Mätinstrumenten

**Tänk på det som:** Betyg och mätningar på din kod.

Code metrics är siffror som mäter olika aspekter av kodkvalitet:

### Vanliga Metrics:

#### Cyclomatic Complexity (Komplexitet)
- **Mäter:** Hur många olika vägar koden kan ta (if-satser, loopar, etc.)
- **Exempel:** En metod med 5 if-satser har högre komplexitet än en med 1
- **Bra värde:** Under 10 per metod
- **Varför det spelar roll:** Hög komplexitet = svårare att testa och förstå

#### Code Coverage (Testtäckning)
- **Mäter:** Hur många procent av koden som testas av automatiska tester
- **Exempel:** Om du har 100 rader kod och tester kör 80 av dem = 80% coverage
- **Bra värde:** Över 70-80%
- **Varför det spelar roll:** Högre täckning = färre oupptäckta buggar

#### Lines of Code (Antal kodrader)
- **Mäter:** Hur många rader kod en klass/metod har
- **Exempel:** En metod på 200 rader vs en på 20 rader
- **Bra värde:** Metoder under 50 rader, klasser under 300 rader
- **Varför det spelar roll:** Kortare kod är lättare att förstå

#### Coupling (Koppling)
- **Mäter:** Hur många andra klasser en klass är beroende av
- **Exempel:** En klass som använder 15 andra klasser vs en som använder 3
- **Bra värde:** Låg koppling
- **Varför det spelar roll:** Låg koppling = lättare att ändra kod utan att påverka annat

#### Cohesion (Sammanhållning)
- **Mäter:** Hur relaterade metoderna i en klass är till varandra
- **Exempel:** En "Player"-klass där alla metoder handlar om spelaren (hög cohesion) vs en klass med metoder för både spelare och spelregler (låg cohesion)
- **Bra värde:** Hög cohesion
- **Varför det spelar roll:** Hög cohesion = tydligare ansvar

---

## 3. Design Patterns - Receptboken

**Tänk på det som:** Beprövade recept för vanliga programmeringsproblem.

Design patterns är färdiga lösningar som andra programmerare har utvecklat och testat:

### Vanliga Patterns:

#### Strategy Pattern (Strategimönstret)
- **Problem:** Du behöver kunna byta ut algoritmer/regler dynamiskt
- **Lösning:** Skapa ett interface och olika implementationer
- **Exempel i Boomerang:** `ScoringStrategy` med olika implementationer (`AnimalScoring`, `CollectionScoring`, etc.)
- **Fördelar:** Lätt att lägga till nya strategier utan att ändra befintlig kod

#### Factory Pattern (Fabriksmönstret)
- **Problem:** Du behöver skapa objekt men vill inte hårdkoda vilken typ
- **Lösning:** En "fabrik"-klass som skapar rätt typ av objekt
- **Exempel:** En `CardFactory` som skapar olika typer av kort
- **Fördelar:** Centraliserad objektskapande logik

#### Observer Pattern (Observatörsmönstret)
- **Problem:** Flera objekt behöver veta när något händer
- **Lösning:** Objekt "prenumererar" på händelser
- **Exempel:** Flera UI-komponenter som uppdateras när spelarens poäng ändras
- **Fördelar:** Löst kopplad kommunikation mellan objekt

#### Singleton Pattern (Singelmönstret)
- **Problem:** Du vill bara ha EN instans av en klass i hela programmet
- **Lösning:** Klassen kontrollerar sin egen instansiering
- **Exempel:** En `GameManager` som hanterar hela spelet
- **Fördelar:** Garanterar att det bara finns en instans

#### Decorator Pattern (Dekoratörsmönstret)
- **Problem:** Du vill lägga till funktionalitet till objekt dynamiskt
- **Lösning:** "Wrappa" objekt i andra objekt som lägger till funktionalitet
- **Exempel:** Lägga till bonuspoäng till olika typer av kort
- **Fördelar:** Flexibel utökning utan att ändra originalkoden

---

## Hur Hänger Allt Ihop?

### Analogin: Att Bygga ett Hus

- **SOLID Principles** = Byggreglerna (hur man bygger säkert och hållbart)
- **Code Metrics** = Inspektionen (mätningar som säkerställer att reglerna följs)
- **Design Patterns** = Arkitektritningar (beprövade sätt att lösa vanliga byggproblem)

### I Praktiken:

1. **SOLID** säger *vad* du ska göra (principer att följa)
2. **Metrics** säger *hur bra* du gör det (mätningar av kvalitet)
3. **Patterns** säger *hur* du gör det (konkreta lösningar)

### Exempel från Boomerang Australia:

**SOLID i praktiken:**
- Varje scoring-klass har ett ansvar (S)
- Nya scoring-regler läggs till utan att ändra befintliga (O)
- Alla scoring-klasser kan användas via `ScoringStrategy` (L, D)

**Metrics i praktiken:**
- Testtäckning: 17 tester för att säkerställa att koden fungerar
- Komplexitet: Små, fokuserade metoder istället för stora
- Koppling: Låg - klasser är oberoende av varandra

**Patterns i praktiken:**
- Strategy Pattern: `ScoringStrategy` med olika implementationer
- Interface: `PlayerController` för att separera UI från logik

---

## Sammanfattning

| När använder man det? | Vad gör man? |
|----------------------|--------------|
| **SOLID** | När du *designar och skriver* kod - följ dessa principer |
| **Metrics** | När du *utvärderar* kod - mät kvaliteten |
| **Patterns** | När du *löser problem* - använd beprövade lösningar |

**Kom ihåg:** 
- SOLID = Reglerna
- Metrics = Betyget
- Patterns = Verktygslådan
