# Övningsfrågor med Svar - Muntlig Examination

**Syfte:** Öva på att svara på frågor genom att läsa frågan, försöka svara själv, och sedan jämföra med modelsvaret.

---

## Hur du använder detta dokument

1. **Läs frågan**
2. **Försök svara högt** (som om examinatorn sitter framför dig)
3. **Läs modelsvaret**
4. **Jämför** - täckte du huvudpunkterna?
5. **Öva igen** tills du känner dig bekväm

---

## Del 1: Grundläggande Förståelse

### Fråga 1.1: Vad var huvudproblemet med originalkoden?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Huvudproblemet var att allt fanns i en enda fil, BoomerangAustralia.java. Den innehöll server-logik, klient-logik, spellogik, kortdata och spelarhantering - allt sammanblandat. Det gjorde koden svår att förstå, omöjlig att testa utan att starta en server, och riskabel att ändra eftersom allt var kopplat till allt. Det bröt mot Single Responsibility Principle eftersom en klass gjorde alldeles för många saker."

**Nyckelord att inkludera:**
- Allt i en fil
- Svår att testa
- Svår att ändra
- Bryter mot SRP

---

### Fråga 1.2: Beskriv din paketstruktur och varför du valde den

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Jag delade upp koden i tre huvudpaket baserat på ansvar. Model-paketet innehåller datastrukturer - Card, Deck och Player. Dessa klasser vet bara om data, inget om spellogik. Scoring-paketet innehåller all poänglogik med ScoringStrategy-gränssnittet och fem implementationer. Detta separerar affärslogiken från resten. Game-paketet innehåller spelflödet med Game-klassen och PlayerController-gränssnittet. Denna separation ger hög kohesion inom varje paket och låg koppling mellan paketen."

**Nyckelord att inkludera:**
- Tre paket: model, scoring, game
- Separation baserat på ansvar
- Hög kohesion, låg koppling

---

### Fråga 1.3: Hur många tester har du och vad testar de?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Jag har 17 JUnit-tester totalt. 12 tester för poänglogik som täcker djurpar, flera par, tre djur, samlingsdubblering, edge cases som exakt 7 vs 8, throw & catch, turistplatser, aktiviteter och regionkomplettering. 5 tester för spellogik som täcker initialisering, validering av spelarantal, validering av poängstrategier, handhantering och draft-hantering. Alla tester passerar när jag kör mvn test."

**Nyckelord att inkludera:**
- 17 tester totalt
- 12 för poäng, 5 för spellogik
- Täcker edge cases
- Alla passerar

---

## Del 2: SOLID-principer

### Fråga 2.1: Ge ett konkret exempel på hur du tillämpar Single Responsibility Principle

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Ett tydligt exempel är AnimalScoring-klassen. Den har bara ett ansvar: att räkna poäng för djurpar. Den vet ingenting om spelflöde, kortlekar eller spelarhantering. Om reglerna för djurpoäng ändras, är det den enda klassen jag behöver ändra. Jämför det med originalkoden där Player-klassen hade poänglogik, handhantering, nätverkskommunikation och mycket mer - den hade många ansvarsområden."

**Nyckelord att inkludera:**
- Konkret klass (AnimalScoring)
- Ett ansvar (räkna djurpoäng)
- Jämförelse med original

---

### Fråga 2.2: Förklara Open/Closed Principle med ett exempel från ditt projekt

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Open/Closed betyder öppen för utökning men stängd för modifiering. Ett exempel är hur jag kan lägga till Boomerang Europa. Jag behöver bara skapa nya klasser: Deck.createEuropeDeck() för europeiska kort, EuropeTransportScoring för transportpoäng, och EuropeCuisineScoring för matpoäng. Sedan ger jag dessa till Game-konstruktorn. Jag behöver inte ändra Game-klassen, ScoringStrategy-gränssnittet eller någon annan befintlig kod. Det är 'öppet för utökning' genom nya klasser, men 'stängt för modifiering' av befintliga klasser."

**Nyckelord att inkludera:**
- Öppen för utökning, stängd för modifiering
- Konkret exempel (lägga till Europa)
- Inga ändringar i befintlig kod

---

### Fråga 2.3: Vad är Dependency Inversion och hur använder du det?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Dependency Inversion betyder att klasser ska bero på abstraktioner istället för konkreta implementationer. I mitt projekt använder Game-klassen PlayerController-gränssnittet, inte ConsoleController eller BotController direkt. När jag skapar Game ger jag den en lista av PlayerController via konstruktorn - det är Dependency Injection. Detta gör att Game inte bryr sig om spelarna är människor, botar eller nätverksspelare. Det gör också att jag kan testa Game genom att injicera mock-controllers."

**Nyckelord att inkludera:**
- Beroenden mot gränssnitt
- Konkret exempel (PlayerController)
- Testbarhet genom injection

---

## Del 3: Designmönster

### Fråga 3.1: Varför använde du Strategy-mönstret?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Jag använde Strategy-mönstret för poängräkning eftersom olika speleditioner har olika poängregler. Istället för att ha en stor if-else-sats eller switch-case för varje edition, skapade jag ett ScoringStrategy-gränssnitt med metoden calculateScore(). Sedan implementerade jag fem olika strategier: AnimalScoring, CollectionScoring, ThrowCatchScoring, TouristSiteScoring och ActivityScoring. Game-klassen använder bara gränssnittet, så den bryr sig inte om vilken konkret strategi som används. Det gör det lätt att lägga till nya strategier för Europa eller USA."

**Nyckelord att inkludera:**
- Olika algoritmer (poängregler)
- Gränssnitt + implementationer
- Lätt att utöka

---

### Fråga 3.2: Förklara Factory Method-mönstret i ditt projekt

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Factory Method används för att kapsla in skapandet av kortlekar. Istället för att alla som behöver en kortlek måste veta hur man skapar 28 kort med alla attribut, kan de bara anropa Deck.createAustraliaDeck(). Metoden returnerar en färdig kortlek. Fördelen är att om jag vill ändra hur kort skapas - kanske ladda från CSV eller databas - behöver jag bara ändra factory-metoden. Konsumenterna påverkas inte. I framtiden kan jag lägga till Deck.createEuropeDeck() och Deck.createUSADeck() på samma sätt."

**Nyckelord att inkludera:**
- Kapslar in skapande
- Centraliserad logik
- Lätt att utöka

---

### Fråga 3.3: Vad är Dependency Injection och varför är det viktigt?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Dependency Injection innebär att en klass får sina beroenden utifrån istället för att skapa dem själv. I Game-konstruktorn tar jag emot List<PlayerController> och List<ScoringStrategy> som parametrar. Det är viktigt av två anledningar: Först gör det koden flexibel - jag kan ge Game olika controllers och strategier utan att ändra Game-klassen. Andra gör det koden testbar - jag kan injicera mock-objekt för att testa Game isolerat utan riktig input eller nätverk."

**Nyckelord att inkludera:**
- Beroenden utifrån
- Flexibilitet
- Testbarhet

---

## Del 4: Kvalitetsattribut

### Fråga 4.1: Hur är din kod modifierbar?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Min kod är modifierbar genom separation av bekymmer. Om jag vill ändra djurpoängregler behöver jag bara ändra AnimalScoring.java - ingen risk att påverka turistplatspoäng eller spelflöde. Om jag vill byta från konsol till HTTP för nätverksspel behöver jag bara skapa en ny WebPlayerController - Game-klassen behöver inte ändras. Kortdata kan laddas från CSV istället för hårdkodad data genom Deck.loadFromCSV(). Varje ändring är isolerad till ett litet område."

**Nyckelord att inkludera:**
- Separation av bekymmer
- Isolerade ändringar
- Konkreta exempel

---

### Fråga 4.2: Hur är din kod utbyggbar?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Min kod är utbyggbar genom polymorfism och gränssnitt. För att lägga till Boomerang Europa behöver jag bara skapa nya klasser utan att ändra befintliga. Regioner extraheras dynamiskt från korten via getRegionsFromDeck(), så nya regioner upptäcks automatiskt. Poängstrategier är pluggbara via ScoringStrategy-gränssnittet. Nya spelartyper kan läggas till via PlayerController-gränssnittet. Arkitekturen stödjer utökningar som är additiva snarare än modifierande."

**Nyckelord att inkludera:**
- Nya klasser, inte ändringar
- Dynamisk hantering
- Gränssnitt för utökning

---

### Fråga 4.3: Hur är din kod testbar?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Min kod är mycket testbar. ScoringStrategy-implementationer är rena funktioner utan sidoeffekter - jag ger dem en spelare och kort, de returnerar poäng. Inga beroenden på databaser, nätverk eller UI. PlayerController-gränssnittet låter mig skapa mock-controllers för att testa Game utan riktig input. Dependency Injection gör att jag kan injicera test-objekt. Jag kan testa varje komponent isolerat - AnimalScoring utan Game, Game utan riktig input. Originalkoden krävde server+klient för att testa något."

**Nyckelord att inkludera:**
- Rena funktioner
- Mock-objekt
- Isolerad testning
- Jämförelse med original

---

## Del 5: Buggar och Förbättringar

### Fråga 5.1: Vilka buggar hittade du och hur fixade du dem?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Jag hittade två huvudbuggar. Första buggen var i djurpoängräkningen. Originalkoden använde if(frequency == 2) vilket betyder att exakt 2 djur ger poäng, men 3 eller 4 djur ger 0 poäng. Det borde räkna par: 4 Emus = 2 par = 8 poäng. Jag fixade det med int pairs = count / 2; int score = pairs * pointsPerPair. Andra buggen var i samlingspoäng. Originalkoden dubblade varje enskilt föremål istället för summan. Jag fixade det genom att först summera alla föremål, sedan kolla om summan är ≤7 för att avgöra om jag ska dubbla. Jag skrev JUnit-tester för att verifiera båda fixarna."

**Nyckelord att inkludera:**
- Två buggar: djur och samlingar
- Konkret förklaring av buggen
- Hur du fixade
- Tester för verifiering

---

### Fråga 5.2: Vad är den största förbättringen från originalkoden?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Den största förbättringen är testbarheten. Originalkoden var omöjlig att testa utan att starta en server och klient. Nu kan jag testa varje del isolerat på millisekunder. Men det finns fler förbättringar: koden är utbyggbar (lägg till Europa utan ändringar), modifierbar (ändra en del utan att påverka andra), och läsbar (små klasser med tydligt ansvar). Från 800+ rader i en fil till välorganiserade paket med klasser på 40-250 rader."

**Nyckelord att inkludera:**
- Testbarhet som huvudförbättring
- Andra förbättringar
- Konkreta siffror

---

## Del 6: Arkitektur och Design

### Fråga 6.1: Varför separerade du poänglogik i ett eget paket?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Jag separerade poänglogik i scoring-paketet av flera anledningar. Först är poängregler komplex affärslogik som ändras ofta - olika editioner har olika regler. Genom att ha dem i ett eget paket kan jag ändra poängregler utan att röra spelflöde eller datastrukturer. Andra gör det koden mer testbar - jag kan testa poänglogik helt oberoende. Tredje följer det Single Responsibility Principle - scoring-paketet har ett ansvar: räkna poäng. Det ger hög kohesion inom paketet."

**Nyckelord att inkludera:**
- Komplex affärslogik
- Ändras ofta
- Testbarhet
- SRP och kohesion

---

### Fråga 6.2: Förklara hur Game-klassen fungerar

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Game-klassen är orkestratorn som koordinerar spelflödet. Den tar emot PlayerControllers och ScoringStrategies via konstruktorn. I start()-metoden kör den 4 rundor. Varje runda består av tre faser: först väljer alla spelare ett Throw-kort, sedan draftar de 6 kort genom att välja ett och passa resten, slutligen räknas poäng med alla strategier. Game använder ExecutorService för att låta spelare välja kort samtidigt. Den vet inte om spelarna är människor eller botar - den använder bara PlayerController-gränssnittet. Den vet inte heller om poängreglerna - den använder bara ScoringStrategy-gränssnittet."

**Nyckelord att inkludera:**
- Orkestrator/koordinator
- Tre faser per runda
- Använder gränssnitt
- Agnostisk till implementation

---

### Fråga 6.3: Vad är skillnaden mellan hög och låg kohesion?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Hög kohesion betyder att allt i en klass eller paket hör ihop och har samma syfte. Låg kohesion betyder att saker som inte hör ihop är blandade. I originalkoden hade BoomerangAustralia.java låg kohesion - den hade server-logik, spellogik, kortdata, allt blandat. I min kod har varje paket hög kohesion: model-paketet har bara datastrukturer, scoring-paketet har bara poänglogik, game-paketet har bara spelflöde. Hög kohesion gör koden lättare att förstå och underhålla."

**Nyckelord att inkludera:**
- Hög kohesion = saker hör ihop
- Låg kohesion = blandat
- Exempel från ditt projekt
- Fördelar

---

## Del 7: Framtida Utökningar

### Fråga 7.1: Hur skulle du lägga till Boomerang Europa? (Steg för steg)

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Jag skulle göra det i fyra steg. Steg 1: Skapa Deck.createEuropeDeck() som returnerar en Deck med 28 europeiska kort. Korten har europeiska regioner och nya attribut som Transport och Cuisine. Steg 2: Implementera EuropeTransportScoring implements ScoringStrategy för att räkna transportpoäng baserat på par. Steg 3: Implementera EuropeCuisineScoring implements ScoringStrategy för matpoäng med halveringsregeln. Steg 4: I Main.java, skapa en lista med Europa-strategier och ge till Game-konstruktorn. Inga ändringar i Game, ScoringStrategy-gränssnittet eller andra befintliga klasser behövs."

**Nyckelord att inkludera:**
- Fyra konkreta steg
- Nya klasser, inte ändringar
- Nämn konkreta klassnamn

---

### Fråga 7.2: Vad skulle du förbättra i din kod?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Det finns några förbättringsområden. ActivityScoring hanteras lite annorlunda än andra strategier - calculateScore() returnerar 0 och actual scoring sker i Game-loopen. Jag kunde gjort det mer konsekvent. Jag kunde också implementera tiebreaker-logiken för vinnare (Throw & Catch poäng vid lika). GameTest kunde ha fler integration tests som kör en hel runda istället för bara validering. Och jag kunde parametrisera scoring-strategierna i Main istället för att hårdkoda dem, för att göra det ännu mer flexibelt."

**Nyckelord att inkludera:**
- Specifika förbättringsområden
- Ärlig om brister
- Konstruktiva förslag

---

## Del 8: Jämförelser

### Fråga 8.1: Jämför testbarheten mellan din kod och originalkoden

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Skillnaden är enorm. Originalkoden var omöjlig att testa utan att starta en server och ansluta klienter. Spellogiken var hårt kopplad till Socket-kommunikation och console I/O. För att testa poänglogik måste man spela igenom hela spelet. I min kod kan jag testa varje del isolerat. För att testa AnimalScoring: new AnimalScoring().calculateScore(player, cards) - tar millisekunder. För att testa Game kan jag injicera mock-controllers. Jag har 17 automatiska tester som kör på sekunder. Originalkoden hade inga tester alls."

**Nyckelord att inkludera:**
- Original: omöjlig att testa
- Din kod: isolerad testning
- Konkreta exempel
- Tidsbesparingar

---

### Fråga 8.2: Jämför utbyggbarheten mellan din kod och originalkoden

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Originalkoden hade hårdkodade regioner i en array och hårdkodad poänglogik i Player-klassen. För att lägga till Europa måste man ändra dessa klasser, vilket riskerar att introducera buggar i befintlig funktionalitet. Det bryter mot Open/Closed Principle. Min kod extraherar regioner dynamiskt från korten och använder Strategy-mönstret för poäng. För att lägga till Europa behöver jag bara skapa nya klasser - Deck.createEuropeDeck() och nya ScoringStrategy-implementationer. Noll ändringar i befintlig kod. Det är additiv utökning istället för modifierande."

**Nyckelord att inkludera:**
- Original: hårdkodat, måste ändra
- Din kod: dynamiskt, nya klasser
- OCP
- Additiv vs modifierande

---

## Del 9: Tekniska Detaljer

### Fråga 9.1: Förklara hur Strategy-mönstret är implementerat i din kod

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Strategy-mönstret består av tre delar i min kod. Först har jag ScoringStrategy-gränssnittet med tre metoder: calculateScore(), getCategoryName() och getScoreDescription(). Sedan har jag fem konkreta implementationer: AnimalScoring, CollectionScoring, ThrowCatchScoring, TouristSiteScoring och ActivityScoring. Varje implementation har sin egen algoritm för att räkna poäng. Tredje har Game-klassen en lista av ScoringStrategy som den får via konstruktorn. I playRound() loopar Game genom alla strategier och anropar calculateScore() på var och en. Game vet inte vilka konkreta strategier som används - den använder bara gränssnittet."

**Nyckelord att inkludera:**
- Tre delar: gränssnitt, implementationer, användning
- Konkreta klassnamn
- Hur Game använder dem

---

### Fråga 9.2: Hur hanterar du regioner dynamiskt?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Jag har en metod getRegionsFromDeck() i Game-klassen. Den loopar genom alla kort i decken och samlar unika regioner i en Set. Sedan returnerar den en array av dessa regioner. Detta betyder att regioner inte är hårdkodade - de upptäcks automatiskt från korten. När jag lägger till europeiska kort med europeiska regioner, kommer metoden automatiskt hitta dem. I playRound() använder jag denna array för att kolla regionkomplettering. Det gör koden extensible - nya editioner kräver bara nya kort, inte kodändringar."

**Nyckelord att inkludera:**
- getRegionsFromDeck()
- Extraherar från kort
- Automatisk upptäckt
- Extensibility

---

### Fråga 9.3: Varför använder du gränssnitt istället för abstrakta klasser?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Jag använder gränssnitt för ScoringStrategy och PlayerController av flera anledningar. Först ger gränssnitt maximal flexibilitet - en klass kan implementera flera gränssnitt men bara ärva från en abstrakt klass. Andra är gränssnitten minimala - de definierar bara det som behövs, inget mer. Det följer Interface Segregation Principle. Tredje gör gränssnitt det tydligt att det är ett kontrakt - implementationer måste följa kontraktet men kan göra det på vilket sätt de vill. För mina användningsfall behövde jag ingen delad implementation, så gränssnitt var det bästa valet."

**Nyckelord att inkludera:**
- Flexibilitet
- Minimala kontrakt
- ISP
- Ingen delad implementation behövdes

---

## Del 10: Reflektionsfrågor

### Fråga 10.1: Vad lärde du dig av denna uppgift?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Jag lärde mig vikten av separation av bekymmer. När allt är sammanblandat blir koden svår att förstå, testa och ändra. Genom att dela upp i logiska delar med tydliga ansvar blir varje del hanterbar. Jag lärde mig också praktisk tillämpning av SOLID-principer - inte bara teorin utan hur de faktiskt förbättrar kod. Strategy-mönstret var särskilt värdefullt för att hantera variation i affärslogik. Och jag insåg hur viktigt Dependency Injection är för testbarhet - utan det är automatisk testning nästan omöjlig."

**Nyckelord att inkludera:**
- Konkreta lärdomar
- Praktisk tillämpning
- Vad som var mest värdefullt

---

### Fråga 10.2: Om du skulle göra om projektet, vad skulle du göra annorlunda?

**Ditt svar:** (Försök svara först)

**Modellsvar:**
> "Om jag skulle göra om det skulle jag börja med att skriva tester först - Test-Driven Development. Det skulle hjälpt mig designa bättre gränssnitt från början. Jag skulle också göra ActivityScoring mer konsekvent med andra strategier istället för att hantera det speciellt i Game-loopen. Kanske skapa en InteractiveScoringStrategy som extends ScoringStrategy för strategier som behöver spelarval. Jag skulle också parametrisera scoring-strategierna i Main istället för att hårdkoda dem, för att göra det ännu mer flexibelt. Men överlag är jag nöjd med arkitekturen."

**Nyckelord att inkludera:**
- Konkreta förbättringar
- TDD
- Mer konsistens
- Balanserad reflektion

---

## Avslutande Tips

### När du övar
1. **Läs frågan högt**
2. **Svara högt** (inte bara i huvudet)
3. **Jämför med modelsvaret**
4. **Notera vad du missade**
5. **Öva igen på de svåra frågorna**

### Under examinationen
- **Använd dina egna ord** - modelsvaren är exempel, inte script
- **Peka på kod** - visa konkreta exempel
- **Var ärlig** - säg om du inte är säker
- **Tänk högt** - visa ditt resonemang

### Kom ihåg
Du behöver inte svara exakt som modelsvaren. Viktigt är att du:
- Förstår koncepten
- Kan förklara med egna ord
- Kan ge konkreta exempel från din kod
- Kan resonera om för- och nackdelar

---

**Lycka till med övningen!** 🎯

Ju fler gånger du övar på dessa frågor, desto mer naturligt kommer svaren kännas under den riktiga examinationen.
