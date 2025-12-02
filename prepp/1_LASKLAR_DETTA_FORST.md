# LÄS DETTA FÖRST! 📖

**Välkommen till ditt förberedelsematerial för den muntliga examinationen!**

---

## 🎯 Vad är detta?

Du har nu **4 omfattande dokument** som hjälper dig förbereda dig för den muntliga examinationen. Detta dokument guidar dig genom hur du använder materialet effektivt.

---

## 📚 Dina Förberedelsedokument

### 1. **muntlig_examination_guide.md** (HUVUDDOKUMENT)
**Vad:** Omfattande guide som förklarar allt i enkla termer  
**När:** Läs detta FÖRST, från början till slut  
**Tid:** ~45 minuter  
**Innehåll:**
- Vad är den muntliga examinationen?
- SOLID-principer förklarat enkelt
- Designmönster förklarat enkelt
- Din arkitektur förklarat enkelt
- Troliga frågor & svar
- Vanliga fallgropar
- Snabbreferens

**👉 BÖRJA HÄR!**

---

### 2. **ovningsfragar_med_svar.md** (ÖVNINGSMATERIAL)
**Vad:** 30+ övningsfrågor med modellsvar  
**När:** Efter du läst huvuddokumentet  
**Tid:** ~60 minuter (öva flera gånger)  
**Hur:**
1. Läs frågan
2. Försök svara HÖGT (viktigt!)
3. Jämför med modelsvaret
4. Notera vad du missade
5. Öva igen på svåra frågor

**👉 ÖVA AKTIVT!**

---

### 3. **visuell_guide.md** (VISUELLT STÖD)
**Vad:** Diagram och visuella representationer  
**När:** Använd som referens under övning  
**Tid:** ~20 minuter att läsa igenom  
**Användning:**
- Titta på diagram för att förstå koncept
- Använd som inspiration för att rita under examinationen
- Referens för att förklara arkitektur

**👉 VISUELLT STÖD!**

---

### 4. **LASKLAR_DETTA_FORST.md** (DETTA DOKUMENT)
**Vad:** Översikt och studieplan  
**När:** Läs först för att få översikt  
**Tid:** ~5 minuter  

---

## 📅 Rekommenderad Studieplan

### Dag 1: Grundläggande Förståelse (2-3 timmar)

#### Morgon/Förmiddag
1. **Läs detta dokument** (5 min)
2. **Läs muntlig_examination_guide.md** (45 min)
   - Fokusera på "SOLID-principer" och "Designmönster"
3. **Paus** (15 min)
4. **Läs visuell_guide.md** (20 min)
   - Titta på alla diagram
5. **Öppna din README.md** (15 min)
   - Fräscha upp minnet om vad du skrev

#### Eftermiddag
6. **Öppna några av dina klasser** (30 min)
   - `Game.java`
   - `AnimalScoring.java`
   - `PlayerController.java`
   - Titta på koden och tänk på VARFÖR
7. **Kör dina tester** (5 min)
   ```bash
   mvn test
   ```
   - Verifiera att alla 17 tester passerar

---

### Dag 2: Aktiv Övning (2-3 timmar)

#### Morgon/Förmiddag
1. **Snabbrepetition** (15 min)
   - Läs "Snabbreferens" i muntlig_examination_guide.md
2. **Övningsfrågor Del 1-5** (60 min)
   - Läs fråga → Svara HÖGT → Jämför med modellsvar
   - Fokusera på frågor du tycker är svåra
3. **Paus** (15 min)

#### Eftermiddag
4. **Övningsfrågor Del 6-10** (60 min)
   - Fortsätt öva aktivt
5. **Identifiera svaga områden** (15 min)
   - Vilka frågor var svåra?
   - Läs om dessa delar i huvuddokumentet

---

### Dag 3: Fördjupning och Repetition (1-2 timmar)

#### Morgon
1. **Fokusera på svaga områden** (30 min)
   - Läs om koncept du tycker är svåra
   - Öva på relaterade frågor igen
2. **Simulera examination** (30 min)
   - Välj 5-10 slumpmässiga frågor
   - Svara som om examinatorn sitter framför dig
   - Tidsätt dig själv (2-3 min per fråga)

#### Kvällen innan examinationen
3. **Lätt repetition** (30 min)
   - Läs "Snabbreferens" igen
   - Titta på diagram i visuell_guide.md
   - Läs "Tips för Examinationen"
4. **Slappna av!**
   - Du har förberett dig väl
   - Lita på din förberedelse

---

## ✅ Checklista: Är du redo?

Gå igenom denna checklista innan examinationen:

### Grundläggande Förståelse
- [ ] Jag kan förklara vad SOLID betyder (inte bara bokstäverna)
- [ ] Jag kan ge exempel på SRP, OCP och DIP från min kod
- [ ] Jag kan förklara Strategy-mönstret med egna ord
- [ ] Jag kan förklara Factory Method med egna ord
- [ ] Jag kan förklara Dependency Injection med egna ord

### Min Kod
- [ ] Jag kan beskriva min paketstruktur (model, scoring, game)
- [ ] Jag kan förklara varför jag valde denna struktur
- [ ] Jag vet vilka buggar jag hittade och hur jag fixade dem
- [ ] Jag kan förklara hur mina tester fungerar
- [ ] Jag har kört `mvn test` och alla tester passerar

### Jämförelser
- [ ] Jag kan förklara huvudproblemen med originalkoden
- [ ] Jag kan jämföra testbarhet: original vs min kod
- [ ] Jag kan jämföra utbyggbarhet: original vs min kod
- [ ] Jag kan förklara skillnaden i koppling och kohesion

### Praktiska Exempel
- [ ] Jag kan förklara hur jag skulle lägga till Boomerang Europa
- [ ] Jag kan peka på konkreta klasser och förklara deras ansvar
- [ ] Jag kan visa hur Strategy-mönstret är implementerat
- [ ] Jag kan visa hur Dependency Injection används

### Attityd och Approach
- [ ] Jag är beredd att vara ärlig om jag inte vet något
- [ ] Jag är beredd att tänka högt och resonera
- [ ] Jag är beredd att använda egna ord, inte memorerade definitioner
- [ ] Jag är beredd att peka på kod och ge konkreta exempel

---

## 🎯 Viktigaste Koncepten (Top 10)

Om du bara har tid att fokusera på det viktigaste, här är top 10:

### 1. **Single Responsibility Principle**
En klass, en uppgift. Exempel: AnimalScoring gör bara djurpoäng.

### 2. **Open/Closed Principle**
Lägg till nytt utan att ändra gammalt. Exempel: Lägga till Europa med nya klasser.

### 3. **Dependency Inversion Principle**
Beroenden mot gränssnitt. Exempel: Game använder PlayerController-interface.

### 4. **Strategy Pattern**
Utbytbara algoritmer. Exempel: ScoringStrategy med 5 implementationer.

### 5. **Factory Method**
Kapsla in skapande. Exempel: Deck.createAustraliaDeck().

### 6. **Dependency Injection**
Ge beroenden utifrån. Exempel: Game får controllers via konstruktor.

### 7. **Paketstruktur**
Tre paket: model (data), scoring (affärslogik), game (koordinering).

### 8. **Testbarhet**
Original: omöjlig att testa. Din kod: 17 automatiska tester.

### 9. **Buggar**
Djurpoäng: frequency == 2 → pairs = count / 2  
Samlingar: dubbla varje → summera först, dubbla sedan

### 10. **Koppling och Kohesion**
Original: hög koppling, låg kohesion  
Din kod: låg koppling (via gränssnitt), hög kohesion (ett ansvar per klass)

---

## 💡 Tips för Olika Lärstilar

### Visuell Lärare
- Fokusera på **visuell_guide.md**
- Rita diagram själv
- Använd färgpennor för att markera viktiga delar
- Skapa egna mindmaps

### Auditiv Lärare
- Läs högt för dig själv
- Förklara koncept för någon annan (eller en gummianka)
- Spela in dig själv när du svarar på frågor
- Lyssna på inspelningarna

### Kinestetisk Lärare
- Skriv ut dokumenten och anteckna i marginalen
- Öppna koden och navigera medan du läser
- Gör egna anteckningar för hand
- Öva genom att faktiskt köra koden

### Läsande/Skrivande Lärare
- Läs alla dokument noggrant
- Gör egna sammanfattningar
- Skriv ner svar på övningsfrågorna
- Skapa egna flashcards

---

## 🚨 Vanliga Misstag att Undvika

### ❌ Misstag 1: Bara läsa, inte öva
**Problem:** Du känner igen svaren men kan inte producera dem själv  
**Lösning:** Öva AKTIVT genom att svara högt på frågorna

### ❌ Misstag 2: Memorera definitioner
**Problem:** Låter robotiskt och oäkta  
**Lösning:** Förstå koncepten och använd egna ord

### ❌ Misstag 3: Inte titta på koden
**Problem:** Kan inte ge konkreta exempel  
**Lösning:** Öppna dina klasser och bekanta dig med koden

### ❌ Misstag 4: Stressa inför examinationen
**Problem:** Glömmer saker du faktiskt kan  
**Lösning:** Förbered dig väl, sedan lita på din förberedelse

### ❌ Misstag 5: Försöka vara perfekt
**Problem:** Fastnar på detaljer, blir nervös  
**Lösning:** Det är okej att inte veta allt, visa ditt resonemang

---

## 🎓 Under Examinationen

### När du kommer in
1. **Ta ett djupt andetag**
2. **Kom ihåg:** Det är ett samtal, inte ett förhör
3. **Var dig själv**

### När du svarar
1. **Lyssna på hela frågan**
2. **Tänk några sekunder** (det är okej!)
3. **Svara med egna ord**
4. **Ge konkreta exempel**
5. **Peka på kod om möjligt**

### Om du fastnar
1. **Var ärlig:** "Jag är inte helt säker, men..."
2. **Tänk högt:** "Låt mig resonera..."
3. **Be om förtydligande:** "Kan du omformulera frågan?"
4. **Använd analogier:** "Det är som att..."

### Kom ihåg
- Examinatorn vill att du ska lyckas
- Du vet mer än du tror
- Det är okej att inte veta allt
- Visa ditt tänkande, inte bara svar

---

## 📞 Sista Minuten Checklista

**30 minuter innan:**
- [ ] Läs "Snabbreferens" i muntlig_examination_guide.md
- [ ] Titta på diagram i visuell_guide.md
- [ ] Öppna README.md och skumma igenom

**10 minuter innan:**
- [ ] Ta några djupa andetag
- [ ] Påminn dig själv: "Jag har förberett mig väl"
- [ ] Tänk på en fråga du känner dig säker på

**Precis innan:**
- [ ] Smile! 😊
- [ ] Du klarar detta!

---

## 🎉 Avslutande Ord

Du har gjort ett bra arbete med tentamen. Du har refaktorerat koden, skrivit tester, och förbättrat lösningen baserat på feedback. Nu handlar det bara om att visa att du förstår vad du har gjort.

**Tre saker att komma ihåg:**

1. **Du vet mer än du tror**
   - Du har skrivit koden
   - Du har läst feedbacken
   - Du har förbättrat lösningen

2. **Examinatorn är på din sida**
   - Målet är att verifiera förståelse
   - Inte att "fälla" dig
   - Visa ditt tänkande

3. **Var dig själv**
   - Använd egna ord
   - Var ärlig
   - Resonera högt

---

## 📖 Läsordning Sammanfattning

```
1. LASKLAR_DETTA_FORST.md (detta dokument)
   ↓
2. muntlig_examination_guide.md (läs hela)
   ↓
3. visuell_guide.md (titta på diagram)
   ↓
4. ovningsfragar_med_svar.md (öva aktivt)
   ↓
5. Repetera svaga områden
   ↓
6. Snabbrepetition innan examination
```

---

## 🍀 LYCKA TILL!

Du har allt du behöver för att lyckas. Lita på din förberedelse och var dig själv.

**Kom ihåg:** Det viktigaste är inte att kunna alla facktermer, utan att kunna förklara ditt tänkande och dina designval.

**Du klarar detta!** 💪

---

**Frågor? Osäkerheter?**
- Läs om i huvuddokumentet
- Öva mer på övningsfrågorna
- Titta på diagram i visuell guide
- Öppna din kod och utforska

**Allt material finns i `prepp/`-mappen:**
- `muntlig_examination_guide.md`
- `ovningsfragar_med_svar.md`
- `visuell_guide.md`
- `LASKLAR_DETTA_FORST.md` (detta dokument)
