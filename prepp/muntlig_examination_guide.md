# Förberedelse för Muntlig Examination - Mjukvaruteknik

**Syfte:** Detta dokument hjälper dig att förbereda dig för den muntliga examinationen genom att förklara alla koncept i enkla termer och ge dig svar på troliga frågor.

---

## 📋 Innehållsförteckning

1. [Vad är den muntliga examinationen?](#vad-är-den-muntliga-examinationen)
2. [SOLID-principer (Förklarat Enkelt)](#solid-principer-förklarat-enkelt)
3. [Designmönster (Förklarat Enkelt)](#designmönster-förklarat-enkelt)
4. [Din Arkitektur (Förklarat Enkelt)](#din-arkitektur-förklarat-enkelt)
5. [Troliga Frågor & Svar](#troliga-frågor--svar)
6. [Vanliga Fallgropar att Undvika](#vanliga-fallgropar-att-undvika)
7. [Snabbreferens](#snabbreferens)

---

## Vad är den muntliga examinationen?

### Syfte
Examinatorn vill säkerställa att du **förstår** vad du har skrivit, inte bara kopierat från AI eller andra källor.

### Format
- **Tid:** Cirka 15-30 minuter
- **Stil:** Samtal, inte förhör
- **Fokus:** Din design och dina designval

### Vad examinatorn letar efter
✅ Att du kan **förklara varför** du gjorde vissa val  
✅ Att du förstår **konsekvenserna** av dina designval  
✅ Att du kan **jämföra** din lösning med originalkoden  
✅ Att du kan **resonera** om alternativa lösningar  

### Vad examinatorn INTE letar efter
❌ Perfekt memorering av definitioner  
❌ Att du kan citera böcker ordagrant  
❌ Att du känner till alla designmönster som finns  

---

## SOLID-principer (Förklarat Enkelt)

SOLID är fem principer för att skriva bra kod. Tänk på dem som "regler för att hålla koden städad och lätt att ändra".

### 1. Single Responsibility Principle (SRP)
**Enkelt:** En klass ska bara göra EN sak.

**Varför?** Om en klass gör många saker blir den svår att förstå och ändra.

**Exempel från ditt projekt:**
- ❌ **Dåligt (originalkoden):** `BoomerangAustralia.java` gjorde ALLT - server, klient, spellogik, kort, spelare
- ✅ **Bra (din kod):** `Card` hanterar bara kortdata, `AnimalScoring` hanterar bara djurpoäng

**Om examinatorn frågar:**
> "Originalkoden hade allt i en fil. Det var som att ha kök, sovrum och badrum i samma rum - rörigt! Jag delade upp det så att varje klass har ett tydligt ansvar. Card-klassen vet bara om kort, AnimalScoring vet bara om hur man räknar djurpoäng."

### 2. Open/Closed Principle (OCP)
**Enkelt:** Du ska kunna lägga till ny funktionalitet utan att ändra befintlig kod.

**Varför?** Att ändra gammal kod kan introducera nya buggar.

**Exempel från ditt projekt:**
- ❌ **Dåligt:** Hårdkodade regioner i en array - måste ändra Game.java för att lägga till Europa
- ✅ **Bra:** `getRegionsFromDeck()` läser regioner från korten - lägg bara till nya kort för Europa

**Om examinatorn frågar:**
> "För att lägga till Boomerang Europa behöver jag bara skapa nya klasser: Deck.createEuropeDeck() och EuropeTransportScoring. Jag behöver inte ändra Game-klassen alls. Det är som att lägga till en ny app på din telefon - du behöver inte ändra operativsystemet."

### 3. Liskov Substitution Principle (LSP)
**Enkelt:** Om du har en "bas-typ", ska alla "sub-typer" fungera på samma sätt.

**Varför?** Annars kan du inte lita på att koden fungerar när du byter implementation.

**Exempel från ditt projekt:**
- ✅ **Bra:** Alla `ScoringStrategy`-implementationer fungerar likadant från Game's perspektiv
- ✅ **Bra:** `ConsoleController`, `BotController` och `NetworkPlayerController` är alla utbytbara

**Om examinatorn frågar:**
> "Game-klassen bryr sig inte om det är en människa eller en bot som spelar. Den använder PlayerController-gränssnittet, och alla implementationer fungerar likadant. Jag kan byta ut ConsoleController mot BotController utan att Game märker skillnad."

### 4. Interface Segregation Principle (ISP)
**Enkelt:** Gör inte gränssnitt för stora - dela upp dem i mindre delar.

**Varför?** Klasser ska inte tvingas implementera metoder de inte behöver.

**Exempel från ditt projekt:**
- ✅ **Bra:** `ScoringStrategy` har bara 3 metoder (calculateScore, getCategoryName, getScoreDescription)
- ✅ **Bra:** `PlayerController` har bara de metoder som behövs för att kontrollera en spelare

**Om examinatorn frågar:**
> "Mina gränssnitt är minimala. ScoringStrategy har bara det som behövs för att räkna poäng. Jag tvingar inte klasser att implementera massa metoder de inte använder."

### 5. Dependency Inversion Principle (DIP)
**Enkelt:** Beroenden ska peka mot abstraktioner (gränssnitt), inte konkreta klasser.

**Varför?** Gör koden flexibel och testbar.

**Exempel från ditt projekt:**
- ❌ **Dåligt:** Om Game hade `ConsoleController console = new ConsoleController()` direkt
- ✅ **Bra:** Game har `List<PlayerController>` - vet inte om det är konsol, bot eller nätverk

**Om examinatorn frågar:**
> "Game-klassen vet inte om spelarna är människor vid konsolen, botar eller nätverksspelare. Den använder bara PlayerController-gränssnittet. Det gör att jag kan testa Game med mock-controllers utan att behöva riktig input."

---

## Designmönster (Förklarat Enkelt)

Designmönster är "beprövade lösningar på vanliga problem". Som recept för kod.

### Strategy Pattern
**Vad är det?** Ett sätt att göra algoritmer utbytbara.

**Varför använde du det?**
Olika speleditioner har olika poängregler. Istället för att ha en jättestor if-else-sats, skapade jag ett gränssnitt (`ScoringStrategy`) och olika implementationer.

**Konkret exempel:**
```java
// Istället för detta (dåligt):
if (gameEdition == "Australia") {
    // räkna djurpoäng på australiensiskt sätt
} else if (gameEdition == "Europe") {
    // räkna transportpoäng på europeiskt sätt
}

// Gjorde jag detta (bra):
ScoringStrategy scorer = new AnimalScoring(); // eller EuropeTransportScoring
int points = scorer.calculateScore(player, cards);
```

**Om examinatorn frågar:**
> "Strategy-mönstret låter mig byta ut poängalgoritmer. För Australien använder jag AnimalScoring, för Europa skulle jag använda EuropeTransportScoring. Game-klassen behöver inte veta skillnaden - den bara anropar calculateScore()."

### Factory Method
**Vad är det?** Ett sätt att skapa objekt utan att specificera exakt vilken klass.

**Varför använde du det?**
Att skapa en kortlek är komplext (28 kort med olika attribut). Jag vill inte att alla som behöver en kortlek ska behöva veta detaljerna.

**Konkret exempel:**
```java
// Istället för att alla måste göra detta:
Card card1 = new Card("The Bungle Bungles", "A", "Western Australia", 1, "Leaves", "", "Indigenous Culture");
Card card2 = new Card("The Pinnacles", "B", "Western Australia", 1, "", "Kangaroos", "Sightseeing");
// ... 26 kort till

// Kan de bara göra detta:
Deck deck = Deck.createAustraliaDeck();
```

**Om examinatorn frågar:**
> "Factory Method kapslar in hur man skapar en kortlek. Istället för att alla måste veta hur man skapar 28 kort, anropar de bara Deck.createAustraliaDeck(). I framtiden kan jag lägga till Deck.createEuropeDeck() utan att ändra någon annan kod."

### Dependency Injection
**Vad är det?** Att ge en klass dess beroenden utifrån, istället för att klassen skapar dem själv.

**Varför använde du det?**
Gör koden testbar och flexibel.

**Konkret exempel:**
```java
// Dåligt (Game skapar sina egna beroenden):
public class Game {
    private ConsoleController controller = new ConsoleController();
    // Nu är Game låst till konsol!
}

// Bra (Game får beroenden utifrån):
public class Game {
    public Game(List<PlayerController> controllers, List<ScoringStrategy> strategies) {
        // Nu kan jag ge Game vilka controllers jag vill!
    }
}
```

**Om examinatorn frågar:**
> "Dependency Injection innebär att Game får sina beroenden via konstruktorn. Det gör att jag kan ge Game olika controllers (konsol, bot, nätverk) och olika poängstrategier. Det gör också att jag kan testa Game genom att injicera mock-objekt."

---

## Din Arkitektur (Förklarat Enkelt)

### Paketstruktur - Varför tre paket?

**Tänk på det som en stad:**
- **model** = Bostadsområdet (där data bor)
- **scoring** = Affärsområdet (där affärslogik sker)
- **game** = Stadshuset (där allt koordineras)

#### model-paketet
**Vad:** Card, Deck, Player  
**Varför:** Datastrukturer ska vara separerade från logik  
**Analogi:** Som en databas - bara lagrar information

#### scoring-paketet
**Vad:** ScoringStrategy + 5 implementationer  
**Varför:** Poängregler är affärslogik som ändras ofta  
**Analogi:** Som en kalkylator - räknar ut saker

#### game-paketet
**Vad:** Game, PlayerController + implementationer  
**Varför:** Spelflöde och kontroll ska vara separat  
**Analogi:** Som en dirigent - koordinerar allt

### Varför är detta bättre än originalkoden?

**Originalkoden:**
```
BoomerangAustralia.java (1 fil, 800+ rader)
├── main()
├── Server
├── Client
├── Player (med spellogik)
├── Card
└── Allt annat
```
**Problem:** Allt är sammanblandat. Svårt att hitta saker, svårt att testa, svårt att ändra.

**Din kod:**
```
model/
├── Card.java (50 rader)
├── Deck.java (100 rader)
└── Player.java (120 rader)

scoring/
├── ScoringStrategy.java (interface)
├── AnimalScoring.java (50 rader)
├── CollectionScoring.java (40 rader)
└── ... (3 till)

game/
├── Game.java (250 rader)
├── PlayerController.java (interface)
└── ... (3 implementationer)
```
**Fördelar:** Lätt att hitta saker, lätt att testa varje del, lätt att ändra en del utan att påverka andra.

---

## Troliga Frågor & Svar

### Fråga 1: "Varför refaktorerade du koden?"

**Bra svar:**
> "Originalkoden hade allt i en fil vilket gjorde den svår att förstå, testa och ändra. Jag delade upp den i logiska delar baserat på ansvar. Nu är varje klass liten och fokuserad på en sak. Det gör koden lättare att underhålla och utöka."

**Följdfråga:** "Ge ett konkret exempel?"
> "Till exempel, om jag vill ändra hur djurpoäng räknas, behöver jag bara ändra AnimalScoring.java. I originalkoden var poänglogiken inbäddad i Player-klassen tillsammans med massa annat, så det var svårt att hitta och riskabelt att ändra."

### Fråga 2: "Förklara Strategy-mönstret i ditt projekt"

**Bra svar:**
> "Strategy-mönstret låter mig ha olika sätt att räkna poäng. Jag har ett gränssnitt ScoringStrategy med en metod calculateScore(). Sedan har jag fem implementationer: AnimalScoring, CollectionScoring, osv. Game-klassen använder bara gränssnittet, så den bryr sig inte om vilken konkret implementation som används. Det gör det lätt att lägga till nya poängregler för Europa eller USA."

**Följdfråga:** "Hur skulle du lägga till Europa?"
> "Jag skulle skapa EuropeTransportScoring implements ScoringStrategy och EuropeCuisineScoring implements ScoringStrategy. Sedan skulle jag skapa en lista med dessa strategier och ge till Game-konstruktorn. Ingen ändring i Game-klassen behövs."

### Fråga 3: "Vad är skillnaden mellan hög och låg koppling?"

**Bra svar:**
> "Hög koppling betyder att klasser är starkt beroende av varandra. Om jag ändrar en klass måste jag ändra många andra. Låg koppling betyder att klasser är oberoende. I originalkoden var allt hårt kopplat - spellogik, nätverk och UI var sammanblandade. I min kod är Game oberoende av om spelaren är en människa vid konsolen eller en bot - den använder bara PlayerController-gränssnittet."

**Följdfråga:** "Ge ett exempel på hög koppling i originalkoden?"
> "I originalkoden var spellogiken direkt kopplad till Socket-kommunikation. Man kunde inte testa spellogiken utan att starta en server och klient. I min kod är spellogiken i Game och kommunikationen i PlayerController-implementationer, så de är oberoende."

### Fråga 4: "Hur testade du din kod?"

**Bra svar:**
> "Jag skrev 17 JUnit-tester. 12 tester för poänglogik och 5 för spellogik. Eftersom jag använder Dependency Injection kan jag testa varje del isolerat. Till exempel kan jag testa AnimalScoring genom att bara ge den en spelare och några kort, utan att behöva hela spelet. Jag kan också testa Game genom att injicera mock-controllers."

**Följdfråga:** "Varför kunde man inte testa originalkoden lika lätt?"
> "Originalkoden hade allt sammanblandat. För att testa poänglogiken måste man starta en server, ansluta en klient, och spela igenom spelet. I min kod kan jag testa poänglogiken direkt: new AnimalScoring().calculateScore(player, cards). Det tar millisekunder istället för minuter."

### Fråga 5: "Vad är Open/Closed Principle och hur tillämpar du det?"

**Bra svar:**
> "Open/Closed betyder att kod ska vara öppen för utökning men stängd för modifiering. Jag ska kunna lägga till ny funktionalitet utan att ändra befintlig kod. Jag tillämpar det genom Strategy-mönstret för poäng och Factory-metoder för kortlekar. För att lägga till Europa behöver jag bara skapa nya klasser, inte ändra Game eller andra befintliga klasser."

**Följdfråga:** "Var bryter originalkoden mot detta?"
> "Originalkoden hade hårdkodade regioner i en array och hårdkodad poänglogik i Player-klassen. För att lägga till Europa måste man ändra dessa klasser, vilket riskerar att introducera buggar. Min kod extraherar regioner dynamiskt från korten och använder Strategy-mönstret för poäng."

### Fråga 6: "Förklara din paketstruktur"

**Bra svar:**
> "Jag har tre huvudpaket baserat på ansvar. Model-paketet innehåller datastrukturer som Card, Deck och Player. Scoring-paketet innehåller all poänglogik med ScoringStrategy-gränssnittet och fem implementationer. Game-paketet innehåller spelflödet med Game-klassen och PlayerController-gränssnittet. Denna separation gör att ändringar i en del inte påverkar andra delar."

**Följdfråga:** "Varför inte bara två paket?"
> "Jag skulle kunna ha game och model, men då skulle poänglogiken vara i game-paketet. Poängregler ändras ofta och är komplex affärslogik, så de förtjänar sitt eget paket. Det gör också att jag kan testa poänglogik helt oberoende av spelflödet."

### Fråga 7: "Vad är Dependency Inversion och varför är det viktigt?"

**Bra svar:**
> "Dependency Inversion betyder att klasser ska bero på abstraktioner (gränssnitt) istället för konkreta implementationer. Game-klassen beror på PlayerController-gränssnittet, inte på ConsoleController eller BotController. Det gör koden flexibel - jag kan byta implementation utan att ändra Game. Det gör också koden testbar - jag kan injicera mock-controllers för testning."

**Följdfråga:** "Hur skulle det se ut utan Dependency Inversion?"
> "Utan DIP skulle Game ha: ConsoleController console = new ConsoleController(). Då är Game låst till konsol. Jag kan inte använda botar eller nätverksspelare utan att ändra Game. Med DIP har Game: List<PlayerController> controllers, och jag kan ge den vilka implementationer jag vill."

### Fråga 8: "Vilka buggar hittade du i originalkoden?"

**Bra svar:**
> "Jag hittade två huvudbuggar. Första buggen var i djurpoängräkningen - koden använde if(frequency == 2) vilket betyder att exakt 2 djur ger poäng, men 3 eller 4 djur ger 0 poäng. Det borde vara pairs = frequency / 2. Andra buggen var i samlingspoäng - koden dubblade varje enskilt föremål istället för summan. Den räknade varje Leaves som 2 istället för att summera alla föremål först och sedan dubbla om summan är ≤7."

**Följdfråga:** "Hur fixade du dem?"
> "För djurpoäng ändrade jag till int pairs = count / 2; int score = pairs * pointsPerPair. För samlingar räknar jag först ut totalsumman, sedan kollar jag om summan är ≤7 för att avgöra om jag ska dubbla. Jag skrev också JUnit-tester för att verifiera att fixarna fungerar."

### Fråga 9: "Hur skulle du lägga till Boomerang Europa?"

**Bra svar:**
> "Jag skulle göra fyra saker: (1) Skapa Deck.createEuropeDeck() med 28 europeiska kort som har europeiska regioner. (2) Implementera EuropeTransportScoring implements ScoringStrategy för transportpoäng. (3) Implementera EuropeCuisineScoring implements ScoringStrategy för matpoäng. (4) I Main.java, skapa en lista med Europa-strategier och ge till Game-konstruktorn. Noll ändringar i Game-klassen eller andra befintliga klasser behövs."

**Följdfråga:** "Vad med regioner?"
> "Regioner hanteras automatiskt. Game.getRegionsFromDeck() extraherar regioner dynamiskt från korten. När jag lägger till europeiska kort med europeiska regioner, kommer metoden automatiskt hitta dem. Inga hårdkodade region-arrayer."

### Fråga 10: "Vad är Booch's metrics och hur tillämpar du dem?"

**Bra svar:**
> "Booch's metrics handlar om koppling och kohesion. Koppling mäter hur beroende klasser är av varandra - låg koppling är bra. Kohesion mäter hur fokuserad en klass är - hög kohesion är bra. I originalkoden var kopplingen hög (allt var sammanblandat) och kohesionen låg (en klass gjorde allt). I min kod har jag låg koppling genom gränssnitt och hög kohesion genom att varje klass har ett tydligt ansvar."

**Följdfråga:** "Ge ett konkret exempel?"
> "Model-paketet har hög kohesion - alla klasser handlar bara om data. Game-paketet är lågt kopplat till model - Game använder Card och Player men vet inte hur de är implementerade internt. Om jag ändrar hur Player lagrar sin hand, påverkar det inte Game."

---

## Vanliga Fallgropar att Undvika

### ❌ Fallgrop 1: Säga "AI gjorde det"
**Dåligt:** "AI:n genererade koden så jag vet inte riktigt..."  
**Bra:** "Jag använde AI som hjälp, men jag förstår designvalen. Låt mig förklara..."

### ❌ Fallgrop 2: Memorera definitioner
**Dåligt:** "Single Responsibility Principle säger att en klass ska ha endast en anledning att ändras enligt Robert C. Martin..."  
**Bra:** "SRP betyder att en klass ska göra en sak. I min kod gör Card bara kortdata, AnimalScoring bara djurpoäng."

### ❌ Fallgrop 3: Inte kunna förklara egna val
**Dåligt:** "Jag vet inte varför jag valde Strategy-mönstret..."  
**Bra:** "Jag valde Strategy för att olika speleditioner har olika poängregler, och jag ville göra dem utbytbara."

### ❌ Fallgrop 4: Säga att allt är perfekt
**Dåligt:** "Min kod är perfekt, det finns inget att förbättra."  
**Bra:** "Det finns förbättringsområden. Till exempel hanterar ActivityScoring lite annorlunda än andra strategier, vilket jag kunde gjort mer konsekvent."

### ❌ Fallgrop 5: Inte kunna jämföra med originalkoden
**Dåligt:** "Jag har inte tittat på originalkoden..."  
**Bra:** "Originalkoden hade allt i en fil vilket gjorde den svår att testa. Min kod är uppdelad i paket vilket gör varje del testbar."

---

## Snabbreferens

### SOLID - Snabbversion
- **S** = En klass, en uppgift
- **O** = Lägg till nytt utan att ändra gammalt
- **L** = Alla implementationer fungerar likadant
- **I** = Små gränssnitt, inte stora
- **D** = Beroenden mot gränssnitt, inte klasser

### Dina Designmönster
- **Strategy** = Utbytbara algoritmer (poäng)
- **Factory** = Kapsla in skapande (kortlekar)
- **Dependency Injection** = Ge beroenden utifrån (testbarhet)

### Dina Paket
- **model** = Data (Card, Deck, Player)
- **scoring** = Affärslogik (5 poängstrategier)
- **game** = Koordinering (Game, controllers)

### Buggar du Fixade
1. **Djurpoäng:** frequency == 2 → pairs = count / 2
2. **Samlingar:** Dubbla varje → Summera först, dubbla sedan

### Förbättringar från Original
- ✅ Testbar (17 tester)
- ✅ Utbyggbar (lägg till Europa utan ändringar)
- ✅ Modifierbar (ändra en del utan att påverka andra)
- ✅ Läsbar (små klasser med tydligt ansvar)

---

## Tips för Examinationen

### Innan
1. **Läs igenom din README** - fräscha upp minnet
2. **Kör dina tester** - se att allt fungerar
3. **Öppna några klasser** - titta på koden
4. **Tänk på VARFÖR** - varför gjorde du vissa val?

### Under
1. **Var ärlig** - säg om du inte vet något
2. **Använd exempel** - peka på konkret kod
3. **Jämför** - visa skillnad mot originalkoden
4. **Resonera** - förklara ditt tänkande
5. **Var lugn** - det är ett samtal, inte ett förhör

### Om du fastnar
- **Be om förtydligande:** "Kan du omformulera frågan?"
- **Tänk högt:** "Låt mig tänka... jag skulle säga att..."
- **Använd analogier:** "Det är som att..."
- **Peka på kod:** "Kan jag visa i koden?"

---

## Avslutande Råd

### Du vet mer än du tror
Du har skrivit koden, läst feedbacken, och förbättrat lösningen. Du förstår koncepten även om du inte kan alla facktermer.

### Examinatorn är på din sida
Målet är inte att "fälla" dig, utan att verifiera att du förstår ditt arbete.

### Fokusera på VARFÖR
Viktigare än att kunna definitioner är att kunna förklara varför du gjorde vissa val och vad konsekvenserna är.

### Använd dina egna ord
Bättre att förklara med enkla ord du förstår än att försöka använda komplicerade termer du inte är säker på.

---

## Lycka till! 🍀

Du har gjort ett bra arbete. Lita på din förberedelse och var dig själv. Om du kan förklara de koncept som finns i detta dokument med dina egna ord, kommer du klara examinationen utan problem.

**Kom ihåg:** Det är okej att säga "Jag är inte säker, men jag tror att..." eller "Kan jag visa i koden istället för att förklara abstrakt?"

**Sista tipset:** Ta med dig en utskrift av din README och dina klassdiagram (om du har några). Det är lättare att peka och förklara med visuellt stöd.
