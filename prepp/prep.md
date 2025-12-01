# Muntlig Examinationsförberedelse - Mjukvaruteknik 2025

## Översikt
Detta dokument förbereder dig för den muntliga examinationen genom att täcka alla nyckelämnen från hemtentans refaktoriseringsprojekt. Studera detta dokument noggrant för att vara redo att förklara och försvara dina designbeslut.

---

## 1. SOLID-principer som tillämpats

### Single Responsibility Principle (SRP)
**Vad det är**: Varje klass ska bara ha en anledning att ändras.

**Hur vi tillämpade det**:
- **Före**: `BoomerangAustralia.java` var en gudaklass som innehöll spellogik, nätverkande, poängräkning och spelarhantering.
- **Efter**: Separerat i:
  - `Card`, `Deck`, `Player` (modellager - endast data)
  - `ScoringStrategy`-implementationer (endast poängräkningslogik)
  - `Game` (endast orkestrering av spelflöde)
  - `PlayerController`-implementationer (endast in/utmatning)

**Var beredd att förklara**: Varför separering av den ursprungliga monolitiska klassen förbättrade underhållbarheten.

### Open/Closed Principle (OCP)
**Vad det är**: Klasser ska vara öppna för utökning men stängda för modifiering.

**Hur vi tillämpade det**:
- **Strategy Pattern för poängräkning**: För att lägga till nya poängregler (t.ex. för Boomerang Europa) behöver vi inte modifiera befintlig kod. Vi skapar bara nya `ScoringStrategy`-implementationer.
- **Kortladdning från CSV**: `Deck.loadFromCSV()` tillåter oss att ladda olika kortuppsättningar utan att ändra spellogiken.

**Var beredd att förklara**: Hur man lägger till Boomerang Europa utan att modifiera befintliga klasser.

### Liskov Substitution Principle (LSP)
**Vad det är**: Subtyper måste kunna ersätta sina bastyper.

**Hur vi tillämpade det**:
- Alla `PlayerController`-implementationer (`ConsoleController`, `BotController`, `NetworkPlayerController`) kan användas omväxlande av `Game`.
- Alla `ScoringStrategy`-implementationer följer samma kontrakt.

**Var beredd att förklara**: Varför `Game` inte behöver veta om den har att göra med en människa eller en bot.

### Interface Segregation Principle (ISP)
**Vad det är**: Klienter ska inte bero på gränssnitt de inte använder.

**Hur vi tillämpade det**:
- `ScoringStrategy` har ett fokuserat gränssnitt (bara `calculateScore`, `getCategoryName`, `getScoreDescription`).
- `PlayerController` exponerar endast metoder som behövs för spelinteraktion.

**Var beredd att förklara**: Varför vi inte la alla spelarrelaterade metoder i ett gigantiskt gränssnitt.

### Dependency Inversion Principle (DIP)
**Vad det är**: Beroende på abstraktioner, inte konkreta klasser.

**Hur vi tillämpade det**:
- `Game` beror på `PlayerController`-gränssnittet, inte specifika implementationer.
- `Game` accepterar `List<ScoringStrategy>` istället för att hårdkoda poängklasser.

**Var beredd att förklara**: Hur dependency injection i `Game`-konstruktorn möjliggör flexibilitet.

---

## 2. Designmönster som använts

### Strategy Pattern
**Var**: `ScoringStrategy`-gränssnitt med implementationer (`AnimalScoring`, `CollectionScoring`, etc.)

**Varför**: Kapslar in olika poängalgoritmer och gör dem utbytbara. Stödjer OCP.

**Var beredd att förklara**: 
- Hur man lägger till en ny poängregel utan att modifiera befintlig kod.
- Skillnaden mellan Strategy och Factory patterns.

### Factory Method Pattern
**Var**: `Deck.createAustraliaDeck()`, `Deck.loadFromCSV()`

**Varför**: Kapslar in objektskapandelogik.

**Var beredd att förklara**: Varför vi använder statiska fabriksmetoder istället för konstruktorer.

### Observer Pattern (implicit)
**Var**: `PlayerController.notifyMessage()`

**Varför**: Tillåter spelet att meddela spelare utan att känna till deras implementationsdetaljer.

---

## 3. Kvalitetsattribut

### Modifierbarhet
**Definition**: Hur lätt det är att göra ändringar i systemet.

**Hur vi uppnådde det**:
- Separerade bekymmer (modell, poängräkning, spellogik, I/O).
- Använde gränssnitt för att frikoppla komponenter.
- Poängregler är isolerade i separata klasser.

**Var beredd att förklara**: Hur du skulle modifiera poängräkningen för Samlingar (t.ex. ändra dubbleringstreshold från 7 till 5).

### Utbyggbarhet (Extensibility)
**Definition**: Hur lätt det är att lägga till nya funktioner.

**Hur vi uppnådde det**:
- Strategy pattern tillåter att lägga till nya poängregler.
- `PlayerController`-gränssnitt tillåter att lägga till nya spelartyper (t.ex. AI med olika strategier).
- CSV-laddning tillåter att lägga till nya kortuppsättningar.

**Var beredd att förklara**: 
- Steg för steg: hur man lägger till Boomerang Europa.
- Vilka klasser du skulle behöva skapa vs. modifiera.

### Testbarhet
**Definition**: Hur lätt det är att skriva automatiserade tester.

**Hur vi uppnådde det**:
- Rena funktioner i poängstrategier (inga sidoeffekter förutom `TouristSiteScoring`).
- Dependency injection möjliggör mockning.
- Separerad I/O från affärslogik.

**Var beredd att förklara**: Varför originalkoden var svår att testa.

---

## 4. Arkitektur & Paketstruktur

### Paketorganisation
```
se.lnu.boomerang
├── model (Card, Deck, Player)
├── scoring (ScoringStrategy, AnimalScoring, etc.)
├── game (Game, PlayerController, ConsoleController, BotController, NetworkPlayerController)
└── Main
```

**Var beredd att förklara**: 
- Varför vi valde denna struktur.
- Rollen för varje paket.
- Vad som skulle hända om vi la allt i ett paket.

---

## 5. Viktiga refaktoriseringsbeslut

### Buggfixar
**Djurpoängbuggen**:
- **Original**: Använde `if (frequency == 2)`, vilket endast gav poäng för exakt 2 djur.
- **Fixad**: `int pairs = count / 2` hanterar korrekt flera par (t.ex. 4 Emus = 2 par).

**Var beredd att förklara**: 
- Varför originalet var fel.
- Hur du testade fixen.

### Strategy Injection
**Beslut**: `Game`-konstruktorn accepterar `List<ScoringStrategy>`.

**Varför**: 
- Stödjer olika speleditioner (Australien, Europa, USA).
- Gör testning enklare (kan injicera mock-strategier).
- Följer DIP.

**Var beredd att förklara**: Alternativa tillvägagångssätt (t.ex. hårdkodning av strategier i `Game`).

### CSV-laddning
**Beslut**: `Deck.loadFromCSV()` med fallback till hårdkodad data.

**Varför**:
- Gör systemet datadriven.
- Stödjer olika speleditioner.
- Fallback säkerställer att spelet fortfarande fungerar om CSV saknas.

**Var beredd att förklara**: Avvägningen mellan flexibilitet och enkelhet.

---

## 6. Teststrategi

### Enhetstester som implementerats
- `testAnimalScoring_Pairs`: Testar 2 Kangaroos = 3 poäng.
- `testAnimalScoring_MultiplePairs`: Testar 4 Emus = 8 poäng (buggfixen).
- `testCollectionScoring_Doubling`: Testar dubbleringsregeln (summa ≤ 7).
- `testThrowCatchScoring`: Testar skillnadsberäkningen.
- `testTouristSiteScoring`: Testar nya platser och dubbletter.
- `testActivityScoring_Options`: Testar tillgängliga poängalternativ.

**Var beredd att förklara**:
- Hur du valde vad som skulle testas.
- Varför edge cases (som flera par) är viktiga.
- Hur du skulle testa `Game`-klassen (integrationstestning).

---

## 7. Nätverksarkitektur (Stub)

### Design
- **Gränssnitt**: `PlayerController` abstraherar spelartyp.
- **Stub**: `NetworkPlayerController` visar hur fjärrspelare skulle integreras.
- **Separation**: Nätverkslogik är isolerad; spellogik är nätverksoberoende.

**Var beredd att förklara**:
- Hur du skulle implementera fullständig nätverksstöd.
- Rollen för sockets, serialisering och threading.
- Varför stubben är användbar även utan full implementation.

---

## 8. Kravtäckning

### Kärnkrav (1-14)
**Var beredd att förklara hur varje är implementerat**:
1. **28 kort**: Laddade från `Deck.createAustraliaDeck()` eller CSV.
2. **Specifika kort**: Hårdkodade eller i CSV.
3. **Blanda**: `deck.shuffle()` i `Game.start()`.
4. **Dela 7 kort**: Loop i `Game.start()`.
5. **Kastkort**: Valt före drafting i `playRound()`.
6. **Draft**: Samtidig val med `ExecutorService`.
7. **Sista kortet till föregående**: Hanterat i `playRound()` med `i == 5`-kontroll.
8. **6 drafsiterationer**: `for (int i = 0; i < 6; i++)` loop.
9. **Fångkort**: Sista kortet går automatiskt in i draft.
10. **Poängräkning**: Varje `ScoringStrategy`-implementation.
11. **Återställ kortlek**: Ny kortlek skapas varje rond.
12. **4 ronder**: `for (int round = 0; round < 4; round++)` loop.
13. **Alternerande passning**: Redo för utökning (skulle parametrisera passningsriktning).
14. **Europa/USA**: Redo via `ScoringStrategy` och CSV-laddning.

---

## 9. Kodgenomgång

### Nyckelklasser att förklara

**`Game.java`**:
- Orkestrerar hela spelflödet.
- Använder `ExecutorService` för samtidig kortval.
- Delegerar poängräkning till strategier.
- Hanterar rondprogression.

**`ScoringStrategy`-hierarki**:
- Gränssnitt definierar kontrakt.
- Varje implementation hanterar en poängkategori.
- `ActivityScoring` är speciell (kräver användarinput).

**`PlayerController`-hierarki**:
- Gränssnitt abstraherar spelartyp.
- `ConsoleController`: Människa via konsol.
- `BotController`: Enkel AI (slumpmässiga val).
- `NetworkPlayerController`: Stub för fjärrspelare.

**Var beredd att**:
- Spåra flödet för en komplett rond.
- Förklara hur samtidighet hanteras.
- Motivera designbeslut.

---

## 10. Potentiella muntliga tentamensfrågor

### Designfrågor
1. **"Varför använde du Strategy pattern för poängräkning?"**
   - Svar: Kapslar in algoritmer, stödjer OCP, gör det enkelt att lägga till nya regler.

2. **"Hur skulle du lägga till Boomerang Europa?"**
   - Svar: Skapa nya poängstrategier, ny kortlek (CSV eller factory), injicera i `Game`.

3. **"Vilka är nackdelarna med din design?"**
   - Svar: Viss komplexitetsöverhead, dependency injection behöver konfiguration, CSV-parsing lägger till runtime overhead.

4. **"Varför separera `PlayerController` från `Player`?"**
   - Svar: `Player` är data (modell), `PlayerController` är beteende (controller). Följer MVC/SRP.

### Implementationsfrågor
5. **"Hur fungerar samtidigt kortval?"**
   - Svar: `ExecutorService` med `Callable` tasks, en per spelare, `invokeAll()` väntar på alla.

6. **"Vad händer om CSV-laddning misslyckas?"**
   - Svar: Fallback till `createAustraliaDeck()`, skriver ut fel, spelet fortsätter.

7. **"Hur testar du `ActivityScoring`?"**
   - Svar: Testa `getAvailableOptions()` separat, mocka spelartillstånd.

### SOLID-frågor
8. **"Vilken SOLID-princip stödjer Strategy pattern?"**
   - Svar: Främst OCP, även SRP och DIP.

9. **"Ge ett exempel på DIP i din kod."**
   - Svar: `Game` beror på `PlayerController`-gränssnitt, inte `ConsoleController`.

10. **"Vad om du inte använde gränssnitt?"**
    - Svar: Tight coupling, svårt att testa, bryter mot LSP/DIP, kan inte enkelt byta implementationer.

---

## 11. Slutchecklista

Innan tentan, säkerställ att du kan:
- [ ] Förklara varje SOLID-princip med exempel från din kod.
- [ ] Beskriva Strategy, Factory och Observer patterns.
- [ ] Gå igenom spelflödet från start till slut.
- [ ] Motivera varje större designbeslut.
- [ ] Förklara hur man utökar systemet (Europa, nya poängregler).
- [ ] Diskutera avvägningar (t.ex. komplexitet vs flexibilitet).
- [ ] Svara på "Vad skulle du göra annorlunda?" (t.ex. använda ett ramverk, lägga till loggning, förbättra felhantering).

---

## Lycka till!
Kom ihåg: Tentan handlar om att demonstrera din **förståelse** av mjukvaruteknikprinciper, inte bara att memorera kod. Var beredd att diskutera alternativ, avvägningar och förbättringar.
