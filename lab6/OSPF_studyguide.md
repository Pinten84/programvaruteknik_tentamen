# Lab 11.6.2: Challenge OSPF Configuration - Studyguide för Muntlig Examination

**Beräknad tid:** 1 timme  
**Ämne:** OSPF-routing konfiguration och verifiering

---

## Inledning

Denna guide är utformad för att förbereda dig inför den muntliga examinationen av Lab 11.6.2. Här kommer du att få en grundlig genomgång av alla uppgifter med förklaringar som är lätta att förstå, även om du inte har arbetat med nätverk tidigare. Tanken är att du ska kunna förklara vad du gjort och varför, inte bara rabbla kommandon.

## Vad är OSPF egentligen?

OSPF står för Open Shortest Path First, och är ett protokoll som hjälper routrar att kommunicera med varandra för att hitta de bästa vägarna att skicka data genom ett nätverk. Tänk dig ett vägnät där olika GPS-enheter pratar med varandra för att alltid veta vilka vägar som är snabbast och öppna. Om en väg stängs, kan alla GPS-enheter snabbt uppdatera sig och välja en alternativ rutt.

OSPF är särskilt smart eftersom den inte bara räknar hur många "hopp" (routrar) ett paket måste gå genom, utan den tar också hänsyn till bandbredden på varje länk. En kortare väg med långsam anslutning kan vara sämre än en längre väg med snabb anslutning. OSPF använder Dijkstras algoritm (samma som många GPS-system använder) för att beräkna den kortaste vägen baserat på en kostnadsfunktion.

I denna labb har vi tre routrar: **HQ** (huvudkontoret), **Branch1** (filial 1) och **Branch2** (filial 2). Alla routrar är uppkopplade i vad vi kallar en "partial mesh-topologi", vilket betyder att inte alla routrar är direktanslutna till varandra, men det finns flera vägar mellan dem. Alla routrar ingår i OSPF Area 0, som kallas "backbone area" och är hjärtat i ett OSPF-nätverk.

---

## Task 9: Konfigurera OSPF på Branch2

### Steget-för-steg genomgång

När vi konfigurerar OSPF på Branch2 måste vi först förstå vilka nätverk som routern är direkt ansluten till. Detta är viktigt eftersom vi bara kan annonsera (berätta om) nätverk som routern faktiskt känner till. 

#### Identifiera direktanslutna nätverk

Den första frågan du kan få är: **Vilka direktanslutna nätverk finns i Branch2:s routingtabell?**

Svaret är att Branch2 har tre nätverk:
- **172.20.48.0/21** - Detta är det lokala LAN-nätverket där Branch2:s datorer är anslutna
- **172.20.56.4/30** - Detta är WAN-länken (långdistansförbindelsen) mellan Branch2 och HQ
- **172.20.56.8/30** - Detta är WAN-länken mellan Branch2 och Branch1

För att verifiera detta kan du använda kommandot `show ip route connected` på routern. Detta visar alla nätverk som routern har direkta fysiska anslutningar till.

#### Aktivera OSPF och annonsera nätverk

Nu kommer själva konfigurationen. Du behöver aktivera OSPF-processen och tala om för routern vilka nätverk den ska annonsera till sina grannar. Här är kommandona:

```cisco
Branch2(config)# router ospf 1
Branch2(config-router)# network 172.20.48.0 0.0.7.255 area 0
Branch2(config-router)# network 172.20.56.4 0.0.0.3 area 0
Branch2(config-router)# network 172.20.56.8 0.0.0.3 area 0
```

Låt oss bryta ner vad som händer här. Det första kommandot `router ospf 1` startar OSPF-processen med process-ID 1. Detta nummer är lokalt för routern och behöver inte matcha andra routrars OSPF process-ID, men det är god praxis att använda samma nummer överallt för konsekvens.

De följande tre kommandona använder `network`-satsen för att tala om vilka nätverk som ska ingå i OSPF. Men vad är det för konstiga nummer efter nätverksadressen? Det kallas wildcard mask, och det är lite annorlunda än den vanliga subnätmasken du kanske är van vid.

**Förstå Wildcard Masks:**

En wildcard mask är som en inverterad subnätmask. Istället för att säga "dessa bitar måste matcha", säger den "jag bryr mig inte om dessa bitar". 
- En 0 i wildcard masken betyder "detta måste matcha exakt"
- En 1 betyder "det spelar ingen roll vad denna bit är"

För nätverket 172.20.48.0/21:
- Subnätmasken är 255.255.248.0
- Wildcard masken blir då 0.0.7.255

För att förstå varför, tänk på det tredje oktetten: 248 i binärt är 11111000. När vi inverterar detta får vi 00000111, vilket är 7 i decimal. Fjärde oktetten är 0, vilket inverterat blir 11111111 = 255.

För de små /30 nätverken (som bara har 4 IP-adresser) blir wildcard masken 0.0.0.3, eftersom 252 inverterat blir 3.

Alla dessa nätverk placeras i **area 0**. I OSPF är area 0 speciell – det är ryggradsarean som alla andra områden måste ansluta till. I denna labb använder vi bara area 0 eftersom nätverket är relativt litet.

#### Konfigurera Passive Interface

Nästa viktiga steg är att konfigurera så kallade "passive interfaces". Frågan du kan få är: **Finns det några interface på Branch2 som inte behöver skicka ut OSPF-uppdateringar?**

Svaret är ja – **FastEthernet0/0**, vilket är gränssnittet som vetter mot det lokala nätverket där vanliga datorer sitter. 

Varför ska vi inte skicka OSPF-meddelanden här? Jo, det finns inga andra routrar på detta nätverksegment, bara slutanvändares datorer. Dessa datorer kan inte och behöver inte ta emot OSPF hello-paket. Genom att göra interfacet "passivt" gör vi två saker:
1. Vi slutar skicka ut onödiga OSPF-paket som slösar bandbredd och CPU
2. Vi minskar säkerhetsrisken genom att inte exponera vår routing-protokoll information

Kommandot för att göra detta är:
```cisco
Branch2(config-router)# passive-interface fa0/0
```

Det smarta med passive interface är att nätverket fortfarande annonseras i OSPF (andra routrar får veta att detta nätverk existerar), men inga OSPF hello-paket skickas ut på interfacet. Det är som att säga "jag berättar om detta nätverk, men jag letar inte efter OSPF-grannar här".

---

## Task 10: Verifiera konfigurationerna

Nu när vi har konfigurerat OSPF på alla tre routrar, är det dags att verifiera att allt fungerar som det ska. Detta är en kritisk del eftersom det visar att du förstår vad som händer i nätverket, inte bara hur man skriver kommandon.

### Testa uppkoppling mellan datorer

#### Kan PC1 pinga PC2?

Svaret är **ja**, och här är varför det är intressant. PC1 sitter på Branch1:s LAN-nätverk (172.20.32.0/20) och PC2 sitter på HQ:s LAN-nätverk (172.20.0.0/19). Innan OSPF var konfigurerat skulle dessa datorer inte kunna kommunicera eftersom routrarna inte visste om varandras nätverk. Men nu när OSPF är aktiverat har routrarna utbytt information om sina nätverk, och Branch1 vet hur man når HQ:s nätverk och vice versa.

När du pingar från PC1 till PC2 händer följande:
1. PC1 skickar paketet till sin default gateway (Branch1 routern)
2. Branch1 tittar i sin routingtabell och ser en OSPF-route till 172.20.0.0/19 via 172.20.56.1 (HQ)
3. Branch1 vidarebefordrar paketet till HQ
4. HQ levererar paketet till PC2 på sitt lokala nätverk

#### Kan PC1 pinga PC3?

Även detta är **ja**, och detta är ett ännu bättre exempel på OSPF:s styrka. PC3 sitter på Branch2:s nätverk, och efter att vi konfigurerade OSPF på Branch2 kan nu alla tre routrar kommunicera med varandra. 

Det intressanta här är att det finns flera möjliga vägar från PC1 till PC3:
- Direktvägen: Branch1 → Branch2
- Alternativa vägen: Branch1 → HQ → Branch2

OSPF väljer automatiskt den bästa vägen baserat på kostnad (bandbredd).

### Analysera routingtabeller

Routingtabellen är hjärtat i varje router – den berättar för routern vart olika paket ska skickas. Låt oss titta på var och en av våra routrar.

#### Branch1:s routingtabell

När du tittar på Branch1:s routingtabell ser du flera OSPF-routes markerade med **O**:

- **O 172.20.0.0/19 via 172.20.56.1** betyder att Branch1 lärt sig om HQ:s LAN-nätverk via OSPF, och nästa hopp för att nå det nätverket är 172.20.56.1 (HQ:s interface)

- **O 172.20.48.0/21 via 172.20.56.10** visar att Branch2:s LAN-nätverk nås via den direkta länken till Branch2

- **O 172.20.56.4/30 via 172.20.56.1 och 172.20.56.10** är särskilt intressant. Detta är WAN-länken mellan HQ och Branch2, och Branch1 har lärt sig om den via två olika vägar. Detta kallas Equal Cost Multi-Path (ECMP) och innebär att OSPF kan lastbalansera trafik över båda vägarna.

- **O*E2 0.0.0.0/0 via 172.20.56.1** är en specialroute. Asterisken (*) betyder att detta är en kandidat för "gateway of last resort" (standardgateway). E2 betyder "External Type 2", vilket indikerar att denna route har importerats från utanför OSPF (i detta fall representerar den Internet-anslutningen).

**Gateway of last resort** för Branch1 är 172.20.56.1. Detta betyder att om Branch1 får ett paket till en destination som den inte har en specifik route för (t.ex. en webbplats på Internet), så skickas paketet till HQ som har Internet-anslutningen.

#### HQ:s routingtabell

HQ är lite speciell eftersom den är Internet-gatewayen för hela företaget. Dess OSPF-routes inkluderar:

- **O 172.20.32.0/20 via 172.20.56.2** - Branch1:s LAN
- **O 172.20.48.0/21 via 172.20.56.6** - Branch2:s LAN  
- **O 172.20.56.8/30 via 172.20.56.2 och 172.20.56.6** - Länken mellan Branch1 och Branch2, som HQ kan nå via båda brancher

HQ:s gateway of last resort är speciell: **0.0.0.0 is directly connected, Loopback1**. Detta betyder att HQ själv är ansluten till Internet (representerat av Loopback1). Det är HQ som skapar den default route som distribueras till de andra routrarna.

#### Branch2:s routingtabell

Branch2:s routingtabell liknar Branch1:s:

- **O 172.20.0.0/19 via 172.20.56.5** - HQ:s LAN
- **O 172.20.32.0/20 via 172.20.56.9** - Branch1:s LAN
- **O 172.20.56.0/30 via 172.20.56.5 och 172.20.56.9** - Länken mellan HQ och Branch1
- **O*E2 0.0.0.0/0 via 172.20.56.5** - Default route som pekar mot HQ

Branch2:s gateway of last resort är 172.20.56.5 (HQ), precis som Branch1. Båda filialkontoren använder huvudkontoret som sin gateway till Internet.

---

## Task 11: Reflektion och förståelse

Detta sista steget handlar om att verkligen förstå hur trafiken flödar genom nätverket och varför OSPF väljer de vägar den gör.

### Spåra vägen från PC1 till PC3

Om du använder `tracert` (eller `traceroute`) från PC1 till PC3, ser du följande hopp:

1. **172.20.32.1** - Detta är Branch1 routern (PC1:s default gateway)
2. **172.20.56.10** - Detta är Branch2 routern
3. **172.20.55.254** - Detta är PC3

Detta visar att paketet tar den direkta vägen från Branch1 till Branch2. Men varför?

### Varför den direkta vägen?

OSPF använder inte bara antalet hopp som RIP (Routing Information Protocol) gör. Istället använder OSPF en kostnadsfunktion baserad på bandbredd. Formeln är:

**Kostnad = 10^8 / Bandbredd i bps**

Den direkta länken mellan Branch1 och Branch2 har en viss kostnad, låt oss säga den är 100 (om det är en 100 Mbps-länk). Den alternativa vägen genom HQ skulle ha en kostnad på 200 (två länkar à 100 vardera). OSPF väljer alltid vägen med lägst totalkostnad.

Detta är tre hopp (inklusive destination), vilket är det minsta möjliga. Alternativa vägen skulle vara:
1. PC1 → Branch1
2. Branch1 → HQ
3. HQ → Branch2
4. Branch2 → PC3

Det skulle vara fyra hopp, vilket är mer än nödvändigt.

### Vad händer om en länk går ner?

Detta är en viktig fråga för muntlig examination. Om länken mellan Branch1 och Branch2 skulle gå ner, vad händer då?

OSPF upptäcker detta snabbt (inom sekunder) eftersom routrarna kontinuerligt skickar hello-paket till sina grannar. När Branch1 slutar få svar från Branch2 på den direkta länken, markeras grannskapet som nere. OSPF kör då om sin SPF-algoritm och hittar den alternativa vägen genom HQ. Trafiken börjar automatiskt gå:

PC1 → Branch1 → HQ → Branch2 → PC3

Denna process kallas **konvergens**, och OSPF är känd för att ha snabb konvergens jämfört med äldre protokoll som RIP.

---

## Viktiga koncept att kunna förklara muntligt

### 1. Vad är skillnaden mellan direkt anslutna nätverk och OSPF-lärd routes?

Direkt anslutna nätverk (markerade med **C** i routingtabellen) är nätverk där routern har ett fysiskt interface. Routern vet om dessa nätverk "själv". OSPF-lärda routes (markerade med **O**) är nätverk som routern har fått veta om av andra routrar genom OSPF-protokollet.

### 2. Varför använder vi passive interface?

Vi använder passive interface på LAN-segment där det inte finns några andra routrar. Detta sparar resurser (CPU och bandbredd) och förbättrar säkerheten genom att inte exponera routing-information till slutanvändare. Viktigt att komma ihåg är att nätverket fortfarande annonseras – vi slutar bara lyssna efter och skicka OSPF hello-paket.

### 3. Vad betyder "area 0"?

Area 0 är backbone-arean i OSPF. I större nätverk kan man ha flera areas (area 1, area 2, etc.) för att göra routing mer skalbart. Alla icke-backbone areas måste ansluta till area 0. I vår labb använder vi bara area 0 eftersom nätverket är litet.

### 4. Varför ser vi ibland två vägar i routingtabellen?

Detta kallas Equal Cost Multi-Path (ECMP). Om OSPF hittar två eller flera vägar med exakt samma kostnad till samma destination, kommer routern att använda båda för lastbalansering. I vår labb ser vi detta för vissa WAN-länkar som kan nås via två olika vägar med samma totalkostnad.

### 5. Vad är skillnaden mellan O och O*E2?

**O** är en vanlig intra-area OSPF-route (en route inom samma area). **O*E2** är en extern route som har importerats in i OSPF från någon annanstans. Asterisken (*) betyder att den är kandidat för default route. E2 betyder External Type 2, vilket innebär att kostnaden för denna route inte ökar när den propageras genom OSPF-nätverket (till skillnad från E1 som adderar intern OSPF-kostnad).

---

## Verifieringskommandon du bör känna till

När du förklarar hur du verifierade din konfiguration, kan du referera till dessa kommandon:

**`show ip route`** - Visar hela routingtabellen. Här ser du alla routes markerade med bokstavskoder (C för connected, O för OSPF, etc.)

**`show ip route ospf`** - Visar bara OSPF-lärda routes, vilket gör det lättare att se vad OSPF har bidragit med.

**`show ip ospf neighbor`** - Visar alla OSPF-grannar. Detta är viktigt för att verifiera att OSPF-adjacencies har bildats korrekt. Du bör se status FULL för varje granne, vilket betyder att routrarna har utbytt all routing-information.

**`show ip ospf interface`** - Visar detaljer om varje interface som kör OSPF, inklusive om det är passive eller inte, area det tillhör, och kostnad.

**`show ip protocols`** - Visar vilka routing-protokoll som är aktiva och vilka nätverk de annonserar.

**`ping [destination]`** - Testar uppkoppling till en destination. Ett lyckat ping betyder att det finns en fungerande route i båda riktningarna (fram och tillbaka).

**`tracert [destination]`** - Visar hela vägen ett paket tar från källa till destination, hopp för hopp. Mycket användbart för att förstå path selection.

---

## Sammanfattning för muntlig redovisning

När du ska redovisa denna labb muntligt, tänk på att strukturera ditt svar ungefär så här:

**Inledning:** Förklara kortfattat vad OSPF är och varför vi använder det. Nämn att det är ett link-state protokoll som använder Dijkstras algoritm och kostnad baserat på bandbredd.

**Din konfiguration:** Beskriv hur du konfigurerade OSPF på Branch2. Förklara varför du valde de specifika network-satserna (vilka nätverk som skulle annonseras) och varför du satte FastEthernet0/0 som passive interface.

**Verifiering:** Gå igenom hur du verifierade att konfigurationen fungerade. Nämn ping-tester mellan PC:ar och hur du analyserade routingtabellerna på alla tre routrar.

**Reflektion:** Diskutera hur OSPF valde optimal path mellan Branch1 och Branch2 (direkt länk istället för via HQ) och vad som skulle hända om den länken gick ner.

**OSPF:s fördelar:** Avsluta med att nämna några av OSPF:s styrkor jämfört med enklare protokoll som RIP: snabb konvergens, skalbarhet, bättre path selection baserat på bandbredd istället för bara hopp, och support för hierarkisk design med areas.

---

## Vanliga frågor vid muntlig examination

**F: Vad händer om du glömmer att konfigurera passive-interface på fa0/0?**

S: Routern skulle skicka OSPF hello-paket ut på LAN-segmentet, vilket slösar bandbredd och CPU-resurser. Slutanvändare skulle ta emot dessa paket men kunde inte svara på dem, vilket skapar onödig trafik. Det är inte katastrofalt, men det är dålig praxis.

**F: Kan du förklara hur wildcard-masken 0.0.7.255 fungerar för ett /21-nätverk?**

S: En /21-subnätmask är 255.255.248.0. För att få wildcard-masken inverterar vi varje bit. Det tredje oktetten är 248 i decimal, vilket är 11111000 i binärt. När vi inverterar får vi 00000111, vilket är 7. Fjärde oktetten är 0, vilket blir 255 när det inverteras. Därför blir wildcard-masken 0.0.7.255.

**F: Varför har både Branch1 och Branch2 sina default routes pekande mot HQ?**

S: HQ är Internet-gatewayen för hela företaget. Den har en fysisk anslutning till Internet (representerad av Loopback1 i labben). HQ skapar och distribuerar en default route till båda branch-routrarna så att all Internet-trafik går via HQ. Detta är en vanlig design där filialer inte har egna Internet-anslutningar utan tunnlar all extern trafik via huvudkontoret.

**F: Vad är "process ID" i kommandot router ospf 1?**

S: Process ID (i detta fall 1) är ett lokalt nummer för routern som används för att identifiera OSPF-processen. Det behöver inte matcha mellan olika routrar – två routrar kan ha olika process ID och fortfarande bilda OSPF-adjacency. Däremot måste de vara i samma area och ha matchande autentisering och timers. Det är dock god praxis att använda samma process ID överallt för konsekvens.

**F: Hur vet routrarna vilken väg som är snabbast?**

S: OSPF beräknar kostnad för varje länk baserat på formeln Cost = 10^8 / bandbredd. En 100 Mbps-länk får kostnad 1, en 10 Mbps-länk får kostnad 10, osv. OSPF summerar sedan kostnaden för hela vägen till varje destination och väljer vägen med lägst totalkostnad. Om två vägar har samma kostnad används båda för lastbalansering.

---

**Lycka till med din redovisning!** 🎓

Kom ihåg att det viktigaste inte är att rabbla kommandon utantill, utan att visa att du förstår **varför** du gör det du gör och **vad** som händer i nätverket. Om du kan förklara koncepten med egna ord och svara på följdfrågor, visar det verklig förståelse.
