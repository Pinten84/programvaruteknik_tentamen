# Visuell Guide - Muntlig Examination

**Syfte:** Visuella representationer och diagram för att hjälpa dig förstå och förklara arkitekturen.

---

## 📊 Arkitektur-översikt

### Din Paketstruktur (Förenklad)

```
┌─────────────────────────────────────────────────┐
│                    Main.java                     │
│              (Ingångspunkt/Bootstrap)            │
└────────────┬────────────────────────────────────┘
             │
             │ skapar och konfigurerar
             ▼
┌─────────────────────────────────────────────────┐
│                  Game.java                       │
│            (Orkestrator/Koordinator)             │
│                                                  │
│  Använder:                                       │
│  • List<PlayerController>                        │
│  • List<ScoringStrategy>                         │
└──────┬──────────────────────┬───────────────────┘
       │                      │
       │                      │
       ▼                      ▼
┌──────────────┐      ┌──────────────────┐
│ PlayerController│    │ ScoringStrategy  │
│  (Interface)    │    │   (Interface)    │
└──────┬──────────┘    └──────┬───────────┘
       │                      │
       │                      │
       ▼                      ▼
┌──────────────┐      ┌──────────────────┐
│ Implementationer│    │ Implementationer │
│                 │    │                  │
│ • ConsoleCtrl   │    │ • AnimalScoring  │
│ • BotCtrl       │    │ • CollectionScor │
│ • NetworkCtrl   │    │ • ThrowCatchScor │
│                 │    │ • TouristSiteScor│
│                 │    │ • ActivityScoring│
└──────────────┘      └──────────────────┘
       │                      │
       │                      │
       └──────┬───────────────┘
              │
              │ använder
              ▼
      ┌──────────────┐
      │ model-paket  │
      │              │
      │ • Card       │
      │ • Deck       │
      │ • Player     │
      └──────────────┘
```

---

## 🔄 Jämförelse: Original vs Refaktorerad

### Originalkoden (Monolitisk)

```
┌─────────────────────────────────────┐
│   BoomerangAustralia.java           │
│   (800+ rader, allt i en fil)       │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ main()                      │   │
│  ├─────────────────────────────┤   │
│  │ Server-logik                │   │
│  ├─────────────────────────────┤   │
│  │ Client-logik                │   │
│  ├─────────────────────────────┤   │
│  │ Player (med spellogik)      │   │
│  ├─────────────────────────────┤   │
│  │ Card                        │   │
│  ├─────────────────────────────┤   │
│  │ Poänglogik (inbäddad)       │   │
│  ├─────────────────────────────┤   │
│  │ Nätverkskommunikation       │   │
│  └─────────────────────────────┘   │
│                                     │
│  Problem:                           │
│  ❌ Svår att förstå                 │
│  ❌ Omöjlig att testa               │
│  ❌ Riskabel att ändra              │
│  ❌ Hög koppling                    │
│  ❌ Låg kohesion                    │
└─────────────────────────────────────┘
```

### Din Refaktorerade Kod (Modulär)

```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ model/       │  │ scoring/     │  │ game/        │
│              │  │              │  │              │
│ Card.java    │  │ ScoringStr.. │  │ Game.java    │
│ (50 rader)   │  │ (interface)  │  │ (250 rader)  │
│              │  │              │  │              │
│ Deck.java    │  │ AnimalScor.. │  │ PlayerCtrl.. │
│ (100 rader)  │  │ (50 rader)   │  │ (interface)  │
│              │  │              │  │              │
│ Player.java  │  │ Collection.. │  │ ConsoleCtrl  │
│ (120 rader)  │  │ (40 rader)   │  │ (80 rader)   │
│              │  │              │  │              │
│              │  │ ThrowCatch.. │  │ BotCtrl      │
│              │  │ (40 rader)   │  │ (60 rader)   │
│              │  │              │  │              │
│              │  │ TouristSite..│  │ NetworkCtrl  │
│              │  │ (60 rader)   │  │ (stub)       │
│              │  │              │  │              │
│              │  │ Activity..   │  │              │
│              │  │ (50 rader)   │  │              │
└──────────────┘  └──────────────┘  └──────────────┘

Fördelar:
✅ Lätt att förstå (små, fokuserade klasser)
✅ Lätt att testa (17 automatiska tester)
✅ Säker att ändra (isolerade ändringar)
✅ Låg koppling (via gränssnitt)
✅ Hög kohesion (ett ansvar per klass)
```

---

## 🎯 SOLID-principer Visualiserade

### Single Responsibility Principle (SRP)

```
❌ DÅLIGT (Original):
┌─────────────────────────┐
│   Player                │
│                         │
│ • Håller kortdata       │
│ • Hanterar hand         │
│ • Räknar poäng          │
│ • Nätverkskommunikation │
│ • Console I/O           │
│ • Spellogik             │
└─────────────────────────┘
För många ansvarsområden!

✅ BRA (Din kod):
┌──────────┐  ┌──────────────┐  ┌──────────────┐
│ Player   │  │ AnimalScoring│  │ ConsoleCtrl  │
│          │  │              │  │              │
│ • Hand   │  │ • Räkna      │  │ • Input      │
│ • Draft  │  │   djurpoäng  │  │ • Output     │
│ • Score  │  │              │  │              │
└──────────┘  └──────────────┘  └──────────────┘
Ett ansvar per klass!
```

### Open/Closed Principle (OCP)

```
❌ DÅLIGT (Original):
För att lägga till Europa:
1. Ändra BoomerangAustralia.java
2. Lägg till if-satser för Europa
3. Riskera att förstöra Australien-logik

✅ BRA (Din kod):
För att lägga till Europa:
1. Skapa Deck.createEuropeDeck()
2. Skapa EuropeTransportScoring
3. Skapa EuropeCuisineScoring
4. Ge till Game-konstruktorn
→ Noll ändringar i befintlig kod!
```

### Dependency Inversion Principle (DIP)

```
❌ DÅLIGT:
┌──────────────────────┐
│ Game                 │
│                      │
│ ConsoleController    │◄─── Hårdkodad beroende
│ console = new ...    │     till konkret klass
└──────────────────────┘

✅ BRA:
┌──────────────────────┐
│ Game                 │
│                      │
│ List<PlayerController>◄─── Beroende på
│                      │     abstraktion
└──────────────────────┘
         ▲
         │ kan vara
         │
    ┌────┴────┬────────┬─────────┐
    │         │        │         │
Console    Bot    Network    Mock
 Ctrl      Ctrl     Ctrl     (test)
```

---

## 🔧 Designmönster Visualiserade

### Strategy Pattern

```
┌─────────────────────────────────────┐
│         ScoringStrategy             │
│         (Interface)                 │
│                                     │
│  + calculateScore(player, cards)    │
│  + getCategoryName()                │
│  + getScoreDescription()            │
└──────────────┬──────────────────────┘
               │
               │ implementeras av
               │
    ┌──────────┼──────────┬──────────┬──────────┐
    │          │          │          │          │
    ▼          ▼          ▼          ▼          ▼
┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│Animal  │ │Collect │ │Throw   │ │Tourist │ │Activity│
│Scoring │ │Scoring │ │Catch   │ │Site    │ │Scoring │
└────────┘ └────────┘ └────────┘ └────────┘ └────────┘

Game använder:
for (ScoringStrategy strategy : strategies) {
    int score = strategy.calculateScore(player, cards);
}

→ Game bryr sig inte om vilken konkret strategi!
```

### Factory Method

```
Utan Factory (Dåligt):
┌─────────────────────────────────────┐
│ Varje klass som behöver kort måste: │
│                                     │
│ Card c1 = new Card("Bungle...", ..);│
│ Card c2 = new Card("Pinnacles",...);│
│ ... 26 kort till ...                │
│                                     │
│ → Duplicerad kod överallt!          │
└─────────────────────────────────────┘

Med Factory (Bra):
┌─────────────────────────────────────┐
│ Alla kan bara göra:                 │
│                                     │
│ Deck deck = Deck.createAustraliaDeck();│
│                                     │
│ → Enkel, centraliserad skapande!    │
└─────────────────────────────────────┘

Framtida utökning:
Deck.createEuropeDeck();
Deck.createUSADeck();
```

---

## 📈 Kvalitetsattribut Visualiserade

### Modifierbarhet

```
Scenario: Ändra djurpoängregler

Original:
┌─────────────────────────────────────┐
│ 1. Hitta poänglogik i Player.java  │
│    (bland 300+ rader)               │
│ 2. Ändra logiken                    │
│ 3. Risk att påverka:                │
│    • Turistplatspoäng               │
│    • Samlingspoäng                  │
│    • Spelflöde                      │
│ 4. Svårt att testa isolerat         │
└─────────────────────────────────────┘

Din kod:
┌─────────────────────────────────────┐
│ 1. Öppna AnimalScoring.java         │
│    (50 rader, bara djurlogik)       │
│ 2. Ändra calculateScore()           │
│ 3. Ingen risk för andra delar       │
│ 4. Kör AnimalScoringTest            │
│ 5. Klart!                           │
└─────────────────────────────────────┘
```

### Utbyggbarhet

```
Scenario: Lägg till Boomerang Europa

Original:
┌─────────────────────────────────────┐
│ 1. Ändra BoomerangAustralia.java    │
│ 2. Lägg till if (edition == "EU")   │
│ 3. Hårdkoda europeiska regioner     │
│ 4. Lägg till EU-poänglogik i Player │
│ 5. Risk för buggar i AU-logik       │
│ 6. Svårt att testa separat          │
└─────────────────────────────────────┘

Din kod:
┌─────────────────────────────────────┐
│ 1. Skapa Deck.createEuropeDeck()    │
│ 2. Skapa EuropeTransportScoring     │
│ 3. Skapa EuropeCuisineScoring       │
│ 4. I Main: new Game(ctrl, euStrat)  │
│ 5. Noll ändringar i befintlig kod   │
│ 6. AU och EU kan testas separat     │
└─────────────────────────────────────┘
```

### Testbarhet

```
Original:
┌─────────────────────────────────────┐
│ För att testa djurpoäng:            │
│                                     │
│ 1. Starta server                    │
│ 2. Anslut klient                    │
│ 3. Spela igenom spelet              │
│ 4. Kolla output manuellt            │
│                                     │
│ Tid: ~5 minuter per test            │
│ Automatisering: Omöjlig             │
└─────────────────────────────────────┘

Din kod:
┌─────────────────────────────────────┐
│ För att testa djurpoäng:            │
│                                     │
│ Player p = new Player(1, false);    │
│ List<Card> cards = ...;             │
│ AnimalScoring scorer = new ...;     │
│ assertEquals(8, scorer.calc...);    │
│                                     │
│ Tid: ~10 millisekunder              │
│ Automatisering: 17 tester körs      │
└─────────────────────────────────────┘
```

---

## 🐛 Buggar Visualiserade

### Bugg 1: Djurpoäng

```
Original kod:
if (frequency == 2 && animal.equals("Emus")) {
    score += 4;
}

Problem:
┌─────────┬────────┬────────┬────────┐
│ Antal   │ 1      │ 2      │ 3      │ 4      │
├─────────┼────────┼────────┼────────┼────────┤
│ Poäng   │ 0      │ 4 ✓    │ 0 ✗    │ 0 ✗    │
└─────────┴────────┴────────┴────────┴────────┘
3 eller 4 Emus ger 0 poäng!

Din fix:
int pairs = count / 2;
int score = pairs * 4;

Resultat:
┌─────────┬────────┬────────┬────────┬────────┐
│ Antal   │ 1      │ 2      │ 3      │ 4      │
├─────────┼────────┼────────┼────────┼────────┤
│ Par     │ 0      │ 1      │ 1      │ 2      │
│ Poäng   │ 0 ✓    │ 4 ✓    │ 4 ✓    │ 8 ✓    │
└─────────┴────────┴────────┴────────┴────────┘
Korrekt!
```

### Bugg 2: Samlingspoäng

```
Original kod:
for (String item : items) {
    int count = countItems(item);
    int score = (count < 8) ? count * 2 : count;
    totalScore += score;
}

Problem:
3 Leaves (värde 1 vardera):
→ count = 3
→ score = 3 * 2 = 6 (varje Leaves dubblas)
→ totalScore = 6 + 6 + 6 = 18 ✗

Korrekt borde vara:
→ sum = 3 * 1 = 3
→ score = 3 * 2 = 6 (summan dubblas)

Din fix:
int sum = 0;
for (String item : items) {
    sum += countItems(item) * itemValue;
}
int score = (sum <= 7) ? sum * 2 : sum;

Resultat:
3 Leaves:
→ sum = 3 * 1 = 3
→ score = 3 * 2 = 6 ✓
Korrekt!
```

---

## 🎓 Koppling och Kohesion

### Koppling (Coupling)

```
HÖG KOPPLING (Dåligt):
┌─────┐     ┌─────┐     ┌─────┐
│  A  │────▶│  B  │────▶│  C  │
└─────┘     └─────┘     └─────┘
  │           │           │
  └───────────┴───────────┘
Ändra A → måste ändra B och C

LÅG KOPPLING (Bra):
┌─────┐     ┌──────────┐     ┌─────┐
│  A  │────▶│Interface │◀────│  B  │
└─────┘     └──────────┘     └─────┘
Ändra A → B påverkas inte
```

### Kohesion (Cohesion)

```
LÅG KOHESION (Dåligt):
┌─────────────────────────┐
│   BoomerangAustralia    │
│                         │
│ • Server                │
│ • Client                │
│ • Spellogik             │
│ • Kortdata              │
│ • Poängräkning          │
│ • Nätverkskommunikation │
└─────────────────────────┘
Allt blandat!

HÖG KOHESION (Bra):
┌──────────┐  ┌──────────┐  ┌──────────┐
│  model   │  │ scoring  │  │   game   │
│          │  │          │  │          │
│ Bara     │  │ Bara     │  │ Bara     │
│ data     │  │ poäng    │  │ flöde    │
└──────────┘  └──────────┘  └──────────┘
Varje del fokuserad!
```

---

## 📝 Snabb Checklista för Examinationen

### Innan du går in
- [ ] Läst igenom README
- [ ] Kört `mvn test` (alla 17 tester passerar)
- [ ] Öppnat några klasser och tittat på koden
- [ ] Tänkt igenom VARFÖR du gjorde vissa val

### Koncept att kunna förklara
- [ ] SOLID-principer (särskilt SRP, OCP, DIP)
- [ ] Strategy-mönstret
- [ ] Factory Method
- [ ] Dependency Injection
- [ ] Koppling och kohesion
- [ ] Dina två buggar och hur du fixade dem

### Frågor att vara beredd på
- [ ] Varför refaktorerade du?
- [ ] Förklara din paketstruktur
- [ ] Hur är koden testbar?
- [ ] Hur skulle du lägga till Europa?
- [ ] Vad är skillnaden mot originalkoden?

### Under examinationen
- [ ] Använd dina egna ord
- [ ] Peka på konkret kod
- [ ] Ge exempel
- [ ] Var ärlig om du inte vet
- [ ] Tänk högt

---

## 🎯 Nyckelbudskap att Komma Ihåg

### 1. Separation av Bekymmer
> "Jag delade upp koden i logiska delar. Varje del har ett tydligt ansvar."

### 2. Testbarhet
> "Originalkoden var omöjlig att testa. Min kod har 17 automatiska tester."

### 3. Utbyggbarhet
> "För att lägga till Europa behöver jag bara skapa nya klasser, inte ändra befintliga."

### 4. SOLID
> "Jag tillämpade SOLID-principer för att göra koden lätt att förstå, testa och ändra."

### 5. Designmönster
> "Strategy-mönstret gör poängregler utbytbara. Factory Method kapslar in skapande."

---

**Lycka till!** 🍀

Använd dessa visuella hjälpmedel för att förklara koncept under examinationen. Det är ofta lättare att rita eller peka än att förklara abstrakt.
