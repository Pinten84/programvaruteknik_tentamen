# Boomerang Australia - Tentamenslösning

## Del A: Skriftlig Tentamen

### 1. Enhetstestning
**Ouuppfyllda Krav i Originalkoden:**

**Krav 10d (Djur):** Regeln säger "För varje par av matchande djur... poängsätt". Originalkoden använde `if(frequency == 2)`, vilket endast ger poäng för exakt 2 djur. Att ha 3 eller 4 av samma djur resulterar i 0 poäng. Detta misslyckas med att hantera flera par (t.ex. 4 Emus = 2 par = 8 poäng).

**Krav 10c (Samlingar):** Originalkoden dubblar varje enskilt föremåls antal istället för summan. Rad: `int sumColl = (nr<8)?nr*2:nr;` borde beräkna totalsumman först, sedan tillämpa dubbleringsregeln om summa ≤7.

**JUnit-tester:**
```java
// Test för Krav 10d - Flera par
@Test
public void testAnimalScoring_MultiplePairs() {
    Player player = new Player(1, false);
    List<Card> draft = new ArrayList<>();
    draft.add(new Card("1", "A", "WA", 1, "", "Emus", ""));
    draft.add(new Card("2", "B", "WA", 1, "", "Emus", ""));
    draft.add(new Card("3", "C", "WA", 1, "", "Emus", ""));
    draft.add(new Card("4", "D", "WA", 1, "", "Emus", ""));
    
    AnimalScoring scorer = new AnimalScoring();
    assertEquals(8, scorer.calculateScore(player, draft)); // 2 par * 4 = 8
}

// Test för Krav 10c - Samlingsdubblering
@Test
public void testCollectionScoring_DoublingRule() {
    Player player = new Player(1, false);
    List<Card> draft = new ArrayList<>();
    draft.add(new Card("1", "A", "WA", 1, "Leaves", "", "")); // 1
    draft.add(new Card("2", "B", "WA", 1, "Wildflowers", "", "")); // 2
    draft.add(new Card("3", "C", "WA", 1, "Shells", "", "")); // 3
    // Summa = 6, borde dubblas till 12
    
    CollectionScoring scorer = new CollectionScoring();
    assertEquals(12, scorer.calculateScore(player, draft));
}
```

**Varför Krav 5 är testbart:** Throw-kortets val kan testas genom att verifiera att det lagras separat och inte avslöjas förrän rundan slutar. Dock var originalkoden huvudsakliga problem arkitektoniskt (blandning av val med drafting), inte funktionell felaktighet.

### 2. Mjukvaruarkitektur Design & Refaktorering
**a. Brister i Originaldesignen:**
- **God Class:** `BoomerangAustralia.java` innehöll allt: `main`, `Server`, `Client`, `Player`, `Card` och spellogik. Detta bryter mot **Single Responsibility Principle (SRP)**.
- **Hög Koppling:** Spellogiken var hårt kopplad till Nätverk och Konsol I/O. Att testa spellogiken utan att köra en server var omöjligt.
- **Hårdkodad Data:** Kortdata var hårdkodad i konstruktorn, vilket gjorde det svårt att utöka för Europa/USA (Bryter mot **Open/Closed Principle**).
- **Duplicering:** Poänglogik var upprepad eller inbäddad i `Player`-klassen.

**b. Refaktorerad Design (SOLID & Booch):**

**Paketstruktur:**
Den refaktorerade designen är organiserad i fyra huvudpaket med tydlig separation av bekymmer. Paketet `model` innehåller Card (immutable), Deck (factory) och Player (state) med hög kohesion då det endast hanterar datastrukturer. Paketet `scoring` består av ScoringStrategy-gränssnittet tillsammans med 5 implementationer, vilket ger hög kohesion genom att endast hantera poängregler. Paketet `game` innehåller Game (orkestrator) samt PlayerController-gränssnittet med 3 implementationer, där hög kohesion uppnås genom fokus på spelflöde. Slutligen fungerar `Main` som ingångspunkt med ett enskilt ansvar.

**SOLID-principer Tillämpade:**
Single Responsibility Principle (SRP) tillämpas genom att varje klass har en anledning att ändras. Exempelvis ändras `Card` endast om kortdatastrukturen ändras, medan `AnimalScoring` endast ändras om djurpoängreglerna ändras. Open/Closed Principle (OCP) uppfylls då nya speleditioner kräver endast nya klasser (t.ex. `EuropeTransportScoring implements ScoringStrategy`) utan ändringar i befintlig kod. Liskov Substitution Principle (LSP) säkerställs genom att alla `ScoringStrategy`-implementationer är utbytbara och alla `PlayerController`-implementationer fungerar identiskt från `Game`s perspektiv. Interface Segregation Principle (ISP) följs genom att `ScoringStrategy` har ett minimalt gränssnitt med endast 3 metoder, och `PlayerController` separerar input-bekymmer från spellogik. Dependency Inversion Principle (DIP) tillämpas då `Game` beror på abstraktioner (`PlayerController`, `ScoringStrategy`) istället för konkreta klasser.

**Boochs Mätvärden:**
Kopplingen har minskats från monolitisk (där allt var kopplat) till en lagerstruktur (model ← scoring ← game), vilket innebär att `Game` inte vet om `ConsoleController`-detaljer. Kohesionen har ökats dramatiskt jämfört med originalet som hade Card+Player+Game+Network i en enda klass. Nu har varje paket ett enda, väldefinierat syfte.

**Designmönster:**
Tre huvudsakliga designmönster används i den refaktorerade designen. Strategy-mönstret implementeras genom `ScoringStrategy` vilket tillåter runtime-val av poängalgoritmer och möjliggör utbyggbarhet för Europa/USA-editioner. Factory Method-mönstret används i `Deck.createAustraliaDeck()` för att kapsla in deck-skapande, med framtida möjligheter för `Deck.createEuropeDeck()`. Dependency Injection tillämpas genom att `Game` tar emot beroenden via konstruktor, vilket möjliggör testbarhet och flexibilitet.

**c. Kvalitetsattribut:**

**Modifierbarhet:**
Designen uppnår hög modifierbarhet genom flera mekanismer. Varje poängkategori är isolerad, vilket innebär att ändringar av djurpoängvärden endast kräver redigering av `AnimalScoring.java` utan risk att förstöra turistplatspoäng. Nätverkslagret är separerat genom `PlayerController`-gränssnittet, vilket tillåter byte från konsol till HTTP/WebSocket utan att röra `Game.java`. Stubben `NetworkPlayerController` demonstrerar denna separation. Kortdata hanteras genom `Deck.loadFromCSV()` med fallback till hårdkodad data, vilket möjliggör byte av datakällor utan att påverka spellogiken. Konsekvensanalysen visar att ändringar i `Card`-strukturen endast påverkar `model`-paketet, medan ändringar i poäng endast påverkar `scoring`-paketet.

**Utbyggbarhet:**
Arkitekturen är designad för utbyggbarhet på flera nivåer. För att lägga till nya speleditioner som Boomerang Europa krävs endast tre steg: (1) skapa `Deck.createEuropeDeck()` med nya kort, (2) implementera nya poängstrategier (`EuropeTransportScoring`, `EuropeCuisineScoring`), och (3) skicka dessa till `Game`-konstruktorn. Detta kräver noll ändringar i befintliga klasser. Nya spelartyper kan läggas till genom att implementera `WebPlayerController implements PlayerController`, varvid `Game` fungerar oförändrat. Valfria regler som Krav 13 (alternativ passning) kräver endast ändring av `Game.playRound()` passningslogik medan alla andra klasser förblir opåverkade. Som bevis för utbyggbarheten stödjer arkitekturen polymorfism vid två nyckelpunkter (poäng och spelarkontroll), vilket gör utökningar additiva snarare än modifierande.

**Testbarhet:**
Testbarheten har förbättrats dramatiskt jämfört med originalkoden. `ScoringStrategy`-implementationer är rena funktioner med tydliga input (Player+Cards) och output (int), utan I/O eller tillstånd, vilket gör dem lätta att testa. `PlayerController`-gränssnittet tillåter mock-implementationer för att testa `Game` utan mänsklig input. Varje komponent kan testas isolerat, exempelvis kan `AnimalScoring` testas utan `Deck`, `Player` eller `Game`. Till skillnad från originalkoden som krävde körning av server+klient för att testa något, kan nu poäng testas på millisekunder genom enkla anrop som `new AnimalScoring().calculateScore(player, cards)`.

**d. Designmönster:**

**Strategy-mönster (Poäng):**
Strategy-mönstret används för att kapsla in poängalgoritmer och göra dem utbytbara. Implementationen består av ett `ScoringStrategy`-gränssnitt med 5 konkreta implementationer (ThrowCatch, TouristSite, Collection, Animal, Activity). Fördelen med detta mönster är att tillägg av nya poängregler, såsom Europas "Transport"-poäng, endast kräver skapande av en ny klass `EuropeTransportScoring implements ScoringStrategy` utan några ändringar i `Game`-klassen. Detta uppfyller Open/Closed Principle.

**Factory Method (Deck-skapande):**
Factory Method-mönstret används för att kapsla in objektskapande och ge flexibilitet i deck-instansiering. Implementationen använder en statisk factory-metod `Deck.createAustraliaDeck()`, med framtida möjligheter för `Deck.createEuropeDeck()` och `Deck.createUSADeck()`. Fördelen är att kortdata centraliseras, vilket gör det möjligt att lägga till CSV-laddning, databasladdning eller API-laddning utan att påverka konsumenter av Deck-klassen.

**Dependency Injection (Konstruktor-injektion):**
Dependency Injection används för att invertera beroenden och möjliggöra testbarhet och flexibilitet. Implementationen innebär att `Game` tar emot `List<PlayerController>` och `List<ScoringStrategy>` via konstruktorn. Fördelen är att mock-controllers kan injiceras för testning, och olika poängstrategier kan konfigureras utan att ändra `Game`-klassen.

**Varför INTE Observer-mönster:**
Även om `PlayerController.notifyMessage()` ytligt liknar Observer-mönstret, är det faktiskt en enkel callback-mekanism. Ett äkta Observer-mönster skulle kräva subscribe/unsubscribe-funktionalitet och event-propagering, vilket är overkill för detta användningsfall där enkel envägskommunikation från spelet till spelarna är tillräckligt.

---

## Del B: Refaktoreringimplementation

### 3. Kvalitetsattribut & Implementation
**a. Utbyggbarhet (Krav 6/14):**
Spelet är mycket utbyggbart:
- **Regioner:** `Game.getRegionsFromDeck()` extraherar regioner dynamiskt från kort, vilket eliminerar hårdkodade region-arrayer. Att lägga till Europa kräver endast nya kort med europeiska regioner.
- **Poäng:** Strategy-mönstret tillåter tillägg av `EuropeTransportScoring`, `EuropeCuisineScoring` utan att ändra `Game`.
- **Decks:** Factory-metoder (`Deck.createAustraliaDeck()`, framtid: `Deck.createEuropeDeck()`) kapslar in deck-skapande.
- **Exempel:** För att lägga till Boomerang Europa:
  1. Skapa `Deck.createEuropeDeck()` med 28 europeiska kort
  2. Implementera `EuropeTransportScoring implements ScoringStrategy`
  3. Implementera `EuropeCuisineScoring implements ScoringStrategy`
  4. Skicka till `Game`-konstruktor: `new Game(controllers, europeStrategies)`
  5. Noll ändringar i befintliga klasser

**b. Modifierbarhet:**
- **Nätverkslager:** `PlayerController`-gränssnittet isolerar input-källor. Kan byta från konsol till HTTP/WebSocket utan att röra `Game.java`.
- **Poängregler:** Varje kategori isolerad. Att ändra djurpoäng kräver endast redigering av `AnimalScoring.java`.
- **Kortdata:** `Deck.loadFromCSV()` med fallback möjliggör datadriven konfiguration.
- **Spelflöde:** Trådlogik isolerad i `Game.playRound()`. Kan ändra passningsriktning (Krav 13) utan att påverka poäng.

**c. Testbarhet:**
- **Rena funktioner:** `ScoringStrategy`-implementationer har inga sidoeffekter (input: Player+Cards, output: int).
- **Dependency injection:** `Game` tar emot beroenden via konstruktor, vilket möjliggör mock-injektion.
- **Isolering:** Kan testa `AnimalScoring` utan `Deck`, `Player` eller `Game`.
- **Ingen I/O i logik:** All I/O isolerad i `PlayerController`-implementationer.

**d. Enhetstestning:**
17 tester implementerade (se `src/test/java`):
- **Poängtester (12):** Djurpar, flera par, tre djur, samlingsdubblering, edge cases (7 vs 8), throw & catch, turistplatser, aktiviteter, regionkomplettering
- **Speltester (5):** Initialisering, spelarantal-validering, poängstrategi-validering, handhantering, draft-hantering
- **Täckning:** Alla poängregler (10a-e), edge cases, valideringslogik
- **Alla tester passerar:** `mvn test` visar 17/17 passerar

**e. Best Practices:**
- Standard Maven-struktur (`src/main/java`, `src/test/java`)
- Java-namnkonventioner (CamelCase-klasser, camelCase-metoder)
- Tydlig separation av bekymmer (model/game/scoring-paket)
- Gränssnitt för polymorfism (`ScoringStrategy`, `PlayerController`)
- Javadoc på alla publika API:er och komplexa metoder
- Konsekvent kodstil och formatering

**f. Korrekthet:**
Alla 12 krav implementerade:
- Krav 1-4: 2-4 spelare, 28 kort, blanda, dela 7 ✓
- Krav 5: Throw-kort dolt ✓
- Krav 6-9: Kortpassning, drafting, catch-kort ✓
- Krav 10a-e: Alla poängkategorier (med buggar fixade) ✓
- Krav 11-12: 4 rundor, vinnare-bestämning ✓
- **Buggar fixade:** Djurpoäng hanterar nu flera par korrekt. Samlingspoäng summerar före dubblering.

**g. Felhantering:**
- **Validering:** Game-konstruktor validerar 2-4 spelare och icke-tomma strategier
- **Null-kontroller:** Player-metoder hanterar null-input graciöst
- **Meningsfulla fel:** IllegalArgumentException med beskrivande meddelanden
- **Graciös degradering:** CSV-laddning faller tillbaka till hårdkodad deck
- **Trådstädning:** ExecutorService stängs ner korrekt i finally-block
- **Användarfeedback:** Felmeddelanden visas för spelare via `notifyMessage()`

**h. Dokumentation:**
- **README:** Kompletta instruktioner för byggning, körning och förståelse av arkitekturen
- **Javadoc:** Alla publika klasser, gränssnitt och komplexa metoder dokumenterade
- **Kodkommentarer:** Komplex logik (trådning, kortpassning) förklarad inline
- **Exempel:** JUnit-tester fungerar som användningsexempel för varje komponent

**i. Trogen Designen:**
Implementationen matchar designen från Fråga 2:
- Paketstruktur exakt som beskrivet (model/game/scoring)
- Strategy-mönster korrekt implementerat för poäng
- Factory-metod för deck-skapande
- Dependency injection via konstruktor
- Alla klasser och gränssnitt som specificerat
- SOLID-principer tillämpade konsekvent genomgående

## Hur man Kör
### Förutsättningar
- Java 17+
- Maven

### Bygg
```bash
mvn package
```

### Kör Lokalt Spel (1 Människa, 1 Bot)
```bash
java -jar target/boomerang-australia-1.0-SNAPSHOT.jar 1 1
```

### Kör Server (2 Människor)
*Notera: Nätverksimplementation är förberedd via gränssnitt men körs för närvarande i lokalt konsolläge för demonstration av arkitekturen.*
```bash
java -jar target/boomerang-australia-1.0-SNAPSHOT.jar 2 0
```
