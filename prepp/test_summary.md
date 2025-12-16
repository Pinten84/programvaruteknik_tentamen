# Sammanfattning av Tester för Boomerang Australia

Detta dokument beskriver på ett enkelt sätt vad de automatiserade testerna i projektet kontrollerar. Testerna fungerar som en säkerhetskontroll för att garantera att spelets regler följs och att poängräkningen sker korrekt.

## Översikt

Projektet innehåller **17 automatiserade tester** fördelade över **2 testfiler**:

1. **GameTest.java** - 5 tester för spelmekanik
2. **ScoringTest.java** - 12 tester för poängräkning

### Filsökvägar
- `src/test/java/se/lnu/boomerang/game/GameTest.java`
- `src/test/java/se/lnu/boomerang/scoring/ScoringTest.java`

---

## 1. Spelmekanik (GameTest) - 5 tester

Dessa tester kontrollerar att själva "motorn" i spelet fungerar som den ska.

### 1.1 `testGameInitialization()`
Kontrollerar att spelet kan startas korrekt med giltiga parametrar (2 spelare och minst en poängregel).

### 1.2 `testGameRequiresTwoToFourPlayers()`
Testar att spelet **inte** kan startas med:
- Endast 1 spelare (för få)
- 5 spelare (för många)
- Null-värde istället för spelare

Spelet kräver exakt 2-4 spelare.

### 1.3 `testGameRequiresScoringStrategies()`
Kontrollerar att spelet måste ha minst en poängregel. Testet säkerställer att spelet inte kan startas med:
- Null-värde istället för poängregler
- Tom lista av poängregler

### 1.4 `testPlayerHandManagement()`
Testar att spelarna kan hantera kort på handen:
- Lägga till kort till handen
- Ta bort specifika kort från handen (med kortets bokstav)
- Försöka ta bort kort som inte finns (ska returnera null)

### 1.5 `testDraftManagement()`
Kontrollerar "draft"-funktionen där spelare samlar kort:
- Lägga till kort i draft-området
- Rensa draft-området mellan rundor

---

## 2. Poängräkning (ScoringTest) - 12 tester

Här testas alla olika sätt man kan få poäng på i spelet för att se till att slutresultatet blir rätt.

### 2.1 Djur (Animals) - 4 tester

#### `testAnimalScoring_Pairs()`
Kontrollerar att ett par av samma djur (t.ex. 2 Kängurur) ger 3 poäng.

#### `testAnimalScoring_MultiplePairs()`
Testar att flera par ger rätt poäng. Exempel: 4 Emuer = 2 par = 8 poäng (4 poäng per par).

#### `testAnimalScoring_NoPairs()`
Verifierar att enstaka djur (utan par) ger 0 poäng.

#### `testAnimalScoring_ThreeSameAnimal()`
Specialfall: 3 av samma djur ska ge poäng för 1 par (3 poäng), inte 0 poäng.

### 2.2 Samlingar (Collections) - 4 tester

#### `testCollectionScoring_Doubling()`
Kontrollerar att samlingar med totalt värde ≤7 får sina poäng **dubblerade**.
Exempel: Löv (1) + Vildblommor (2) = 3 poäng → dubblas till 6 poäng.

#### `testCollectionScoring_NoDoubling()`
Testar att samlingar med totalt värde >7 **inte** dubbleras.
Exempel: 2 Souvenirer (5+5=10) ger 10 poäng (ingen dubbling).

#### `testCollectionScoring_EdgeCase_Exactly7()`
Gränsfall: Exakt 7 poäng ska **dubbleras** till 14 poäng.

#### `testCollectionScoring_EdgeCase_Exactly8()`
Gränsfall: Exakt 8 poäng ska **inte dubbleras** (stannar på 8 poäng).

### 2.3 Kasta och Fånga (Throw & Catch) - 1 test

#### `testThrowCatchScoring()`
Kontrollerar att skillnaden mellan "Throw"-kortets position och "Catch"-kortets position räknas som poäng.
Exempel: Throw på position 1, Catch på position 6 → 5 poäng.

### 2.4 Turistattraktioner (Tourist Sites) - 2 tester

#### `testTouristSiteScoring()`
Testar att:
- Varje ny besökt plats ger 1 poäng
- Redan besökta platser inte ger poäng igen
- Systemet håller koll på vilka platser som besökts

#### `testRegionCompletion()`
Kontrollerar att systemet kan spåra när en spelare besökt 4 platser i samma region (vilket ger bonuspoäng i själva spelet).

### 2.5 Aktiviteter (Activities) - 1 test

#### `testActivityScoring_Options()`
Testar att:
- Systemet räknar hur många kort av varje aktivitet spelaren har
- Aktiviteter som redan poängsatts inte kan väljas igen
- Rätt poäng tilldelas baserat på antal kort
