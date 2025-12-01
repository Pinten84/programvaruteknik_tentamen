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

**SVAR**: Den ursprungliga `BoomerangAustralia.java` hade hundratals rader kod som blandade flera ansvarsområden:
- När vi behövde ändra poängräkningen var vi tvungna att leta genom spellogik
- När vi behövde fixa nätverkskod riskerade vi att bryta poängräkning
- Testning var nästan omöjlig eftersom allt var sammankopplat
- Flera utvecklare kunde inte arbeta parallellt på olika delar

Efter refaktoriseringen:
- Varje klass har ett tydligt ansvar och kan förstås isolerat
- Ändringar i en del påverkar inte andra delar
- Vi kan testa varje komponent separat
- Ny utvecklare kan förstå en klass i taget istället för att behöva läsa 1000+ rader
- Flera utvecklare kan arbeta på olika paket samtidigt utan merge-konflikter

### Open/Closed Principle (OCP)
**Vad det är**: Klasser ska vara öppna för utökning men stängda för modifiering.

**Hur vi tillämpade det**:
- **Strategy Pattern för poängräkning**: För att lägga till nya poängregler (t.ex. för Boomerang Europa) behöver vi inte modifiera befintlig kod. Vi skapar bara nya `ScoringStrategy`-implementationer.
- **Kortladdning från CSV**: `Deck.loadFromCSV()` tillåter oss att ladda olika kortuppsättningar utan att ändra spellogiken.

**Var beredd att förklara**: Hur man lägger till Boomerang Europa utan att modifiera befintliga klasser.

**SVAR**: Steg för steg:
1. **Skapa ny kortlek**: `Deck.createEuropeDeck()` eller `europe_cards.csv` med europeiska platser
2. **Skapa nya scoring-strategier** (om Europa har andra regler):
   - `EuropeTouristSiteScoring` (implementerar `ScoringStrategy`)
   - `EuropeCollectionScoring` om samlingar är olika
3. **I Main.java**: Välj vilken edition som ska spelas baserat på argument:
   ```java
   String edition = args[2]; // "australia" eller "europe"
   List<ScoringStrategy> strategies = getStrategiesForEdition(edition);
   Deck deck = edition.equals("europe") ? 
       Deck.createEuropeDeck() : Deck.createAustraliaDeck();
   ```
4. **Ingen befintlig kod ändras**: `Game`, `Player`, `Card`, `PlayerController` är helt oförändrade

Detta är OCP i praktiken: systemet är **öppet för utökning** (nya strategier, nya kortlekar) men **stängt för modifiering** (befintliga klasser ändras inte).

### Liskov Substitution Principle (LSP)
**Vad det är**: Subtyper måste kunna ersätta sina bastyper.

**Hur vi tillämpade det**:
- Alla `PlayerController`-implementationer (`ConsoleController`, `BotController`, `NetworkPlayerController`) kan användas omväxlande av `Game`.
- Alla `ScoringStrategy`-implementationer följer samma kontrakt.

**Var beredd att förklara**: Varför `Game` inte behöver veta om den har att göra med en människa eller en bot.

**SVAR**: Detta är Liskov Substitution Principle i praktiken:
- `Game` arbetar endast mot `PlayerController`-gränssnittet
- Den anropar `selectCardToDraft(hand)` utan att bry sig om:
  - Om svaret kom från en människa som tryckte på tangenter
  - Om svaret kom från en bot som valde slumpmässigt
  - Om svaret kom över nätverket från en fjärrspelare

**Fördelar**:
- Vi kan lägga till nya spelartyper (AI med machine learning, web-baserad spelare) utan att ändra `Game`
- Vi kan byta ut en bot mot en människa mitt i utvecklingen utan kodändringar
- Testning är enkel: skapa en `MockController` som returnerar förutbestämda kort

**Om vi hade brutit mot LSP**: Om `Game` hade if-satser som kollade `if (player.isBot())` skulle vi behöva modifiera `Game` varje gång vi lägger till en ny spelartyp.

### Interface Segregation Principle (ISP)
**Vad det är**: Klienter ska inte bero på gränssnitt de inte använder.

**Hur vi tillämpade det**:
- `ScoringStrategy` har ett fokuserat gränssnitt (bara `calculateScore`, `getCategoryName`, `getScoreDescription`).
- `PlayerController` exponerar endast metoder som behövs för spelinteraktion.

**Var beredd att förklara**: Varför vi inte la alla spelarrelaterade metoder i ett gigantiskt gränssnitt.

**SVAR**: Om vi hade ett gigantiskt gränssnitt `PlayerInterface` med metoder som:
```java
interface PlayerInterface {
    void setPlayer(Player p);
    Card selectCard(List<Card> hand);
    void renderGraphics(); // För GUI-version
    void playSound(String soundFile); // För ljudeffekter
    void sendNetworkMessage(String msg); // För nätverksspel
    void saveToDatabase(); // För persistent state
    void updateLeaderboard(); // För online rankings
}
```

**Problemet**:
- `ConsoleController` måste implementera `renderGraphics()` och `playSound()` även om den aldrig använder dem
- `BotController` måste implementera `sendNetworkMessage()` även om bots är lokala
- Varje ny metod tvingar ALLA implementationer att uppdateras
- Klienter (som `Game`) exponeras för metoder de inte behöver

**Vår lösning** (ISP):
- `PlayerController`: Endast spelinteraktion
- Framtida gränssnitt: `NetworkCapable`, `Renderable`, `Persistable` (om behövs)
- Klasser implementerar endast de gränssnitt de faktiskt använder

### Dependency Inversion Principle (DIP)
**Vad det är**: Beroende på abstraktioner, inte konkreta klasser.

**Hur vi tillämpade det**:
- `Game` beror på `PlayerController`-gränssnittet, inte specifika implementationer.
- `Game` accepterar `List<ScoringStrategy>` istället för att hårdkoda poängklasser.

**Var beredd att förklara**: Hur dependency injection i `Game`-konstruktorn möjliggör flexibilitet.

**SVAR**: **Utan DIP** (dålig design):
```java
public class Game {
    private ConsoleController player1; // Hårdkodat!
    private BotController player2;
    private AnimalScoring animalScorer = new AnimalScoring();
    
    public Game() {
        player1 = new ConsoleController(); // Tight coupling
        player2 = new BotController();
    }
}
```
Problem: Omöjligt att testa, måste ha konsol, kan inte byta spelaretyp.

**Med DIP** (vår design):
```java
public Game(List<PlayerController> controllers, 
            List<ScoringStrategy> strategies) {
    this.controllers = controllers; // Injicerat!
    this.strategies = strategies;
}
```

**Fördelar**:
1. **Testbarhet**: Injicera mock-controllers och mock-strategier
2. **Flexibilitet**: Stöd för 2-4 spelare, valfri mix av människor/bots
3. **Utbyggbarhet**: Nya strategier läggs till utan att ändra `Game`
4. **Konfiguration**: `Main` bestämmer setup, inte `Game`

Detta är DIP: `Game` beror på abstraktioner (`PlayerController`, `ScoringStrategy`), inte konkreta klasser.

---

## 2. Designmönster som använts

### Strategy Pattern
**Var**: `ScoringStrategy`-gränssnitt med implementationer (`AnimalScoring`, `CollectionScoring`, etc.)

**Varför**: Kapslar in olika poängalgoritmer och gör dem utbytbara. Stödjer OCP.

**Var beredd att förklara**: 
- Hur man lägger till en ny poängregel utan att modifiera befintlig kod.
- Skillnaden mellan Strategy och Factory patterns.

**SVAR 1 - Ny poängregel**:
T.ex. "Landmark Scoring" (bonuspoäng för att besöka Sydney Opera House):
```java
public class LandmarkScoring implements ScoringStrategy {
    @Override
    public int calculateScore(Player player, List<Card> roundDraft) {
        boolean hasSydneyOpera = roundDraft.stream()
            .anyMatch(c -> c.getName().equals("Sydney Opera House"));
        return hasSydneyOpera ? 5 : 0;
    }
    
    @Override
    public String getCategoryName() { return "Landmarks"; }
    
    @Override
    public String getScoreDescription() { return "5 points for Sydney Opera House"; }
}
```
I `Main.java`:
```java
strategies.add(new LandmarkScoring()); // Det är allt!
```
Ingen annan kod ändras. Detta är Strategy pattern + OCP.

**SVAR 2 - Strategy vs Factory**:
- **Strategy**: Kapslar in *algoritmer/beteenden* som kan bytas ut vid runtime
  - Exempel: Olika sätt att beräkna poäng
  - Fokus: *Hur* något görs
- **Factory**: Kapslar in *objektskapande*
  - Exempel: `Deck.createAustraliaDeck()` skapar kort
  - Fokus: *Vad* som skapas
  
Vi använder båda: Factory för att skapa kortlekar, Strategy för att beräkna poäng.

### Factory Method Pattern
**Var**: `Deck.createAustraliaDeck()`, `Deck.loadFromCSV()`

**Varför**: Kapslar in objektskapandelogik.

**Var beredd att förklara**: Varför vi använder statiska fabriksmetoder istället för konstruktorer.

**SVAR**: **Fördelar med Factory Methods**:
1. **Beskrivande namn**: 
   - `Deck.createAustraliaDeck()` är tydligare än `new Deck(someComplexSetup)`
   - `Deck.loadFromCSV(path)` förklarar vad som händer

2. **Kan returnera subtyper**:
   - Factory kan returnera `AustraliaDeck`, `EuropeDeck` (om vi hade subklasser)
   - Konstruktor måste returnera exakt den klassen

3. **Kan cachea objekt**:
   - Factory kan returnera samma instans om lämpligt
   - Konstruktor skapar alltid nytt objekt

4. **Komplex initialisering**:
   - `createAustraliaDeck()` döljer 28 `deck.add()` anrop
   - `loadFromCSV()` hanterar filläsning, parsing, felhantering
   - Konstruktorn förblir enkel

**Exempel från vår kod**:
```java
// Tydligt och enkelt
Deck deck = Deck.createAustraliaDeck();

// Jämför med hypotetisk konstruktor
Deck deck = new Deck(card1, card2, ..., card28); // Ej praktiskt!
```

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

**SVAR**: **Tack vare separation av concerns behöver vi bara ändra EN fil**:

I `CollectionScoring.java`, hitta:
```java
int sum = leaves + wildflowers + shells + souvenirs;
if (sum <= 7) {
    score = sum * 2; // Dubbla om summa ≤ 7
}
```

Ändra till:
```java
int sum = leaves + wildflowers + shells + souvenirs;
if (sum <= 5) { // Ändrad threshold
    score = sum * 2;
}
```

**Varför det är enkelt**:
- Ingen annan klass behöver ändras
- `Game`, `Player`, `Card` vet inget om denna regel
- Tester för `CollectionScoring` kan uppdateras isolerat
- Om ändringen bryter något ser vi det bara i `ScoringTest`

**Om vi hade ursprunglig monolitisk kod**:
- Måste leta genom hundratals rader för att hitta rätt ställe
- Risk att ändra fel variabel (många `if` för olika poängregler)
- Svårt att testa ändringen isolerat

### Utbyggbarhet (Extensibility)
**Definition**: Hur lätt det är att lägga till nya funktioner.

**Hur vi uppnådde det**:
- Strategy pattern tillåter att lägga till nya poängregler.
- `PlayerController`-gränssnitt tillåter att lägga till nya spelartyper (t.ex. AI med olika strategier).
- CSV-laddning tillåter att lägga till nya kortuppsättningar.

**Var beredd att förklara**: 
- Steg för steg: hur man lägger till Boomerang Europa.
- Vilka klasser du skulle behöva skapa vs. modifiera.

**SVAR - Steg för steg**:

**NYA klasser att SKAPA**:
1. `europe_cards.csv` - 28 europeiska platser
2. `EuropeTouristSiteScoring.java` (om regler skiljer sig)
3. `EuropeCollectionScoring.java` (om samlingar är olika)
4. Eventuellt: `EuropeThrowCatchScoring.java`

**Klasser att MODIFIERA**:
1. `Main.java` - Lägg till edition-val:
   ```java
   String edition = args.length > 2 ? args[2] : "australia";
   Deck deck = edition.equals("europe") ? 
       Deck.loadFromCSV("/europe_cards.csv") : 
       Deck.createAustraliaDeck();
   List<ScoringStrategy> strategies = 
       getStrategiesForEdition(edition);
   ```

2. Eventuellt: `Deck.java` - Lägg till `createEuropeDeck()` factory method

**Klasser som INTE ändras** (detta är poängen!):
- `Game.java` - Vet inte om edition
- `Player.java` - Edition-oberoende
- `Card.java` - Fungerar för alla kort
- `PlayerController` och implementationer - Oförändrade
- Alla befintliga scoring-strategier

**Sammanfattning**: ~2-4 nya filer, ~1-2 små ändringar, 15+ filer oförändrade. Detta är utbyggbarhet!

### Testbarhet
**Definition**: Hur lätt det är att skriva automatiserade tester.

**Hur vi uppnådde det**:
- Rena funktioner i poängstrategier (inga sidoeffekter förutom `TouristSiteScoring`).
- Dependency injection möjliggör mockning.
- Separerad I/O från affärslogik.

**Var beredd att förklara**: Varför originalkoden var svår att testa.

**SVAR**: **Problem med `BoomerangAustralia.java`**:

1. **Tight coupling med I/O**:
   - Läste direkt från `System.in`
   - Skrev direkt till `System.out`
   - Omöjligt att testa utan att faktiskt skriva input på konsolen

2. **Hårdkodade beroenden**:
   - Skapade spelare, kort, nätverk i samma metod
   - Kan inte mocka komponenter
   - Måste köra hela spelet för att testa en liten del

3. **State överallt**:
   - Globala variabler för kort, poäng, spelare
   - Sidoeffekter gör testning opålitlig
   - Ett test påverkar nästa test

4. **Ingen separation**:
   - För att testa djurpoäng måste man köra hela spelrundan
   - Kan inte testa spellogik utan nätverkskod

**Vår lösning (testbar design)**:
```java
@Test
public void testAnimalScoring_Pairs() {
    Player p = new Player(1, false);
    List<Card> draft = List.of(
        new Card("K1", "A", "WA", 1, "", "Kangaroos", ""),
        new Card("K2", "B", "WA", 1, "", "Kangaroos", "")
    );
    AnimalScoring scorer = new AnimalScoring();
    assertEquals(3, scorer.calculateScore(p, draft));
}
```
- Inga sidoeffekter
- Ingen I/O
- Testet är snabbt, isolerat, deterministiskt

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

**SVAR - Varför denna struktur**:
Vi följer **separation of concerns** och **layer architecture**:

1. **`model` (Domain Layer)**:
   - **Roll**: Ren data, inga beroenden
   - **Innehåll**: `Card`, `Deck`, `Player`
   - **Regel**: Kan inte importera från `game` eller `scoring`
   - **Fördel**: Kan återanvändas i andra spel

2. **`scoring` (Business Logic Layer)**:
   - **Roll**: Affärsregler för poängberäkning
   - **Innehåll**: `ScoringStrategy` + implementationer
   - **Beroenden**: Endast `model`
   - **Fördel**: Testbar utan `Game`

3. **`game` (Application Layer)**:
   - **Roll**: Orkestrering, flödeskontroll, I/O
   - **Innehåll**: `Game`, `PlayerController` + implementationer
   - **Beroenden**: `model` och `scoring`
   - **Fördel**: Kan byta I/O (konsol → GUI) utan att ändra logik

4. **`Main` (Presentation Layer)**:
   - **Roll**: Entry point, konfiguration
   - **Beroenden**: Alla andra

**Om vi la allt i ett paket**:
- ❌ Ingen tydlig struktur
- ❌ Cirkulära beroenden möjliga
- ❌ Svårt att se vad som beror på vad
- ❌ Kan inte återanvända delar
- ❌ Ny utvecklare ser 20+ filer utan struktur

**Vår struktur**:
- ✅ Tydlig beroendehierarki: Main → game → scoring → model
- ✅ Lätt att hitta saker: Poängbug? Kolla `scoring`-paketet
- ✅ Kan återanvända `model` i annat projekt

---

## 5. Viktiga refaktoriseringsbeslut

### Buggfixar
**Djurpoängbuggen**:
- **Original**: Använde `if (frequency == 2)`, vilket endast gav poäng för exakt 2 djur.
- **Fixad**: `int pairs = count / 2` hanterar korrekt flera par (t.ex. 4 Emus = 2 par).

**Var beredd att förklara**: 
- Varför originalet var fel.
- Hur du testade fixen.

**SVAR - Varför originalet var fel**:
**Original kod**:
```java
for (Map.Entry<String, Integer> entry : animalCount.entrySet()) {
    if (entry.getValue() == 2) { // BUG!
        score += 3;
    }
}
```

**Problem**:
- Testar exakt likhet med 2
- 4 Emus → `entry.getValue() == 4` → `false` → 0 poäng (FEL!)
- Enligt reglerna: 4 Emus = 2 par = 6 poäng

**Vår fix**:
```java
for (Map.Entry<String, Integer> entry : animalCount.entrySet()) {
    int count = entry.getValue();
    int pairs = count / 2; // Heltalsdivision: 4/2=2, 3/2=1
    score += pairs * 3;
}
```

**Hur vi testade fixen**:
```java
@Test
public void testAnimalScoring_MultiplePairs() {
    Player p = new Player(1, false);
    List<Card> draft = List.of(
        new Card("E1", "A", "NT", 1, "", "Emus", ""),
        new Card("E2", "B", "NT", 1, "", "Emus", ""),
        new Card("E3", "C", "NT", 1, "", "Emus", ""),
        new Card("E4", "D", "NT", 1, "", "Emus", "")
    );
    AnimalScoring scorer = new AnimalScoring();
    int score = scorer.calculateScore(p, draft);
    assertEquals(6, score); // 2 par * 3 poäng = 6
}
```
Testet failar med original kod (0 poäng), passerar med fix (6 poäng).

### Strategy Injection
**Beslut**: `Game`-konstruktorn accepterar `List<ScoringStrategy>`.

**Varför**: 
- Stödjer olika speleditioner (Australien, Europa, USA).
- Gör testning enklare (kan injicera mock-strategier).
- Följer DIP.

**Var beredd att förklara**: Alternativa tillvägagångssätt (t.ex. hårdkodning av strategier i `Game`).

**SVAR - Alternativ och deras konsekvenser**:

**Alternativ 1: Hårdkodning i `Game`**:
```java
public class Game {
    private AnimalScoring animalScorer = new AnimalScoring();
    private CollectionScoring collectionScorer = new CollectionScoring();
    // etc...
}
```
**Nackdelar**:
- Tight coupling: `Game` måste känna till alla strategier
- Bryter mot OCP: För att lägga till ny strategi måste `Game` ändras
- Omöjligt att testa med mock-strategier
- Kan inte stödja olika editions (Europa, USA)

**Alternativ 2: Factory in `Game`**:
```java
public Game(String edition) {
    this.strategies = StrategyFactory.getStrategies(edition);
}
```
**Fördelar**: Mer flexibelt än hårdkodning
**Nackdelar**: 
- `Game` har fortfarande beroende till factory
- Mindre flexibelt än direkt injection

**Vår lösning: Constructor Injection**:
```java
public Game(List<PlayerController> controllers, 
            List<ScoringStrategy> strategies) {
    this.strategies = strategies; // Injicerat!
}
```
**Fördelar**:
- `Game` är oberoende av hur strategier skapas
- `Main` eller testkod bestämmer konfiguration
- Maximal flexibilitet och testbarhet
- Följer DIP perfekt

### CSV-laddning
**Beslut**: `Deck.loadFromCSV()` med fallback till hårdkodad data.

**Varför**:
- Gör systemet datadriven.
- Stödjer olika speleditioner.
- Fallback säkerställer att spelet fortfarande fungerar om CSV saknas.

**Var beredd att förklara**: Avvägningen mellan flexibilitet och enkelhet.

**SVAR**: **Trade-offs**:

**Flexibilitet (vad vi vann)**:
- Kan ladda olika editions utan kodändring
- Lätt att uppdatera kortdata utan omkompilering
- Modders kan skapa egna kortuppsättningar
- Fallback säkerställer att spelet fungerar

**Komplexitet (vad det kostade)**:
- **Mer kod**: 50+ rader för CSV-parsing vs. 0 om vi bara hårdkodar
- **Runtime overhead**: Filläsning och parsing tar tid
- **Felhantering**: Måste hantera IOException, parsing errors, fel format
- **Testning**: Måste testa både CSV-laddning och fallback

**Varför det var värt det**:
- **Krav 14**: "Förbered för Europa/USA" → CSV gör detta trivialt
- **Maintainability**: Att ändra ett kort kräver bara CSV-edit, inte omkompilering
- **Fallback**: Säkerhetsval om CSV saknas → minimal risk

**Kodexempel**:
```java
public static Deck loadFromCSV(String csvPath) {
    try {
        // ~50 rader CSV-parsing
        return parseCSV(reader);
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Failed to load CSV, using fallback deck.");
        return createAustraliaDeck(); // Säkerhetsnät!
    }
}
```

**Avvägning**: Vi valde flexibilitet för framtida behov, men behöll enkelhet genom fallback.

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

**SVAR 1 - Hur vi valde vad som skulle testas**:
Vi följde **risk-baserad testning**:
1. **Buggiga delar**: Djurpoäng hade bug → prioriterad testning
2. **Komplex logik**: Samlingsdubbleringsregeln → behöver test
3. **Edge cases**: Flera par, exakt threshold-värden
4. **Business-kritiskt**: Poängberäkning är kärnan → måste fungera

**SVAR 2 - Varför edge cases är viktiga**:
**Exempel: Djurpoäng**
- **Happy path**: 2 Kangaroos → 3 poäng (lätt att få rätt)
- **Edge case**: 4 Emus → 6 poäng (var buggen lurade)
- **Edge case**: 3 Koalas → 3 poäng (udda antal)
- **Edge case**: 0 djur → 0 poäng

**Varför de är kritiska**:
- Originalkoden failade på "4 Emus"-caset
- De flesta bugs lurkar i edge cases, inte happy paths
- Produktionsspel kommer ha alla kombinationer

**SVAR 3 - Testa `Game`-klassen (integration)**:
```java
@Test
public void testFullRound() {
    // Setup
    List<PlayerController> controllers = List.of(
        new MockController(0, List.of("A", "B", "C", "D", "E", "F")),
        new MockController(1, List.of("G", "H", "I", "J", "K", "L"))
    );
    List<ScoringStrategy> strategies = List.of(
        new ThrowCatchScoring(),
        new AnimalScoring()
    );
    
    Game game = new Game(controllers, strategies);
    
    // Act
    game.start();
    
    // Assert
    assertTrue(controllers.get(0).getPlayer().getTotalScore() > 0);
    // Verifiera att 4 ronder kördes
    // Verifiera att alla spelare fick poäng
}
```

**MockController** returnerar förutbestämda kort → deterministiska tester.

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

**SVAR 1 - Full nätverksimplementation**:
```java
public class NetworkPlayerController implements PlayerController {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public NetworkPlayerController(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }
    
    @Override
    public Card selectCardToDraft(List<Card> hand) {
        try {
            // Skicka hand till klient
            out.writeObject(hand);
            out.flush();
            
            // Ta emot val från klient
            String selectedLetter = (String) in.readObject();
            return hand.stream()
                .filter(c -> c.getLetter().equals(selectedLetter))
                .findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return hand.get(0); // Fallback
        }
    }
}
```

**SVAR 2 - Rollen för komponenter**:
- **Sockets**: TCP-anslutning mellan server och klienter
- **Serialisering**: Konvertera Java-objekt (Card, List) till bytes för nätverksöverföring
- **Threading**: Varje `NetworkPlayerController` kör i egen tråd för att hantera I/O utan att blockera spelet
- **ExecutorService**: Vi använder redan detta för samtidig input → enkelt att integrera nätverk

**SVAR 3 - Varför stubben är användbar**:
1. **Visar arkitekturen**: Demonstrerar att design stödjer nätverk
2. **Plats för expansion**: Kommentarer visar var logik ska läggas
3. **Testbar integration**: `Game` vet inte skillnaden mellan stub och riktig network controller
4. **Dokumentation**: Visar examinatorer att vi förstår nätverksarkitektur även om vi inte implementerat det fullt
5. **MVP-approach**: Fungerar lokalt nu, kan utökas senare

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
