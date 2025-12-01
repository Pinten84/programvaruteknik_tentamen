# Boomerang Game Rules - Complete Analysis

**Document Purpose:** Complete reference for all three Boomerang game variants to support software architecture design and implementation.

**Last Updated:** 2025-11-27

---

## Table of Contents
1. [Core Game Mechanics (Shared)](#core-game-mechanics-shared)
2. [Boomerang Australia](#boomerang-australia)
3. [Boomerang Europe](#boomerang-europe)
4. [Boomerang USA](#boomerang-usa)
5. [Key Design Differences](#key-design-differences)
6. [Implementation Considerations](#implementation-considerations)

---

## Core Game Mechanics (Shared)

### Basic Setup
- **Players:** 2-4 players
- **Deck:** 28 cards total
- **Rounds:** 4 rounds per game
- **Cards per Round:** 7 cards dealt to each player

### Game Flow (Each Round)
1. **Deal:** Shuffle and deal 7 cards to each player
2. **Select Throw Card:** Each player secretly selects one card (not revealed until round ends)
3. **Draft Phase:** 6 iterations of:
   - Select one card from hand
   - Place face-up (visible to others)
   - Pass remaining cards to next player
4. **Catch Card:** The final remaining card is passed and becomes the "Catch" card
5. **Scoring:** Calculate points across multiple categories
6. **Next Round:** Repeat steps 1-5 for rounds 2-4

### Card Structure
Each card contains:
- **Name:** Tourist site/location name
- **Letter/ID:** Unique identifier (A-Z, *, -)
- **Number:** Used for Throw & Catch scoring (1-7)
- **Region:** Geographic area
- **Category Icons:** 2-3 symbols for scoring (varies by game edition)

---

## Boomerang Australia 🦘

### Game-Specific Rules

#### Card Passing Direction
- **Drafting (rounds 1-6):** Pass to NEXT player (clockwise/left)
- **Final card (Catch):** Pass to PREVIOUS player (counter-clockwise/right)

#### Regions (7 regions, 4 sites each)
- Western Australia: A-D
- Northern Territory: E-H
- Queensland: I-L
- South Australia: M-P
- New South Wales: Q-T
- Victoria: U-X
- Tasmania: Y, Z, *, -

### Scoring Categories

#### 1. Throw & Catch
**Formula:** `|Throw number - Catch number| = points`

**Example:**
- Throw = 4, Catch = 1 → Score = 3
- Throw = 1, Catch = 4 → Score = 3

#### 2. Tourist Sites
**Base Scoring:**
- 1 point per NEW site visited this round
- Sites visited in previous rounds don't score again
- Sites remain marked for region completion

**Region Bonus:**
- First player to complete a region (all 4 sites): **+3 points**
- If multiple players complete same region in same round: **all get +3 points**

#### 3. Collections
**Items & Values:**
- Leaves = 1
- Wildflowers = 2
- Shells = 3
- Souvenirs = 5

**Scoring Rule:**
- Sum all collection values for the round
- If sum is 1-7: **DOUBLE the sum**
- If sum is >7: **Score as-is (no doubling)**

**Examples:**
- 3 Leaves + 1 Shells = 3+3 = 6 → Score 12 (doubled)
- 2 Wildflowers + 1 Souvenirs = 4+5 = 9 → Score 9 (not doubled)

#### 4. Animals
**Scoring:** Only PAIRS count!

**Values per Pair:**
- Kangaroos = 3
- Emus = 4
- Wombats = 5
- Koalas = 7
- Platypuses = 9

**Examples:**
- 2 Kangaroos → 3 points (not 6!)
- 1 Kangaroo → 0 points
- 4 Emus → 8 points (two pairs)

#### 5. Activities
**Available Activities:**
- Indigenous Culture
- Bushwalking
- Swimming
- Sightseeing

**Scoring Table:**
| Cards | 1 | 2 | 3 | 4 | 5 | 6 |
|-------|---|---|---|---|---|---|
| Points| 0 | 2 | 4 | 7 | 10| 15|

**Restrictions:**
- May score **ONE activity per round**
- May score **each activity type only ONCE per game**
- Once scored, that activity cannot be scored again in future rounds

### Game End
**Winner Determination:**
1. Highest total score wins
2. **Tiebreaker:** Most Throw & Catch points

---

## Boomerang Europe 🗼

### Game-Specific Rules

#### Card Passing Direction
- Same as Australia (next player, then previous for final card)

#### Regions
- Multiple regions separated by color
- 2-5pt bonus per region
- Regions contain multiple countries

### Scoring Categories

#### 1. Throw & Catch
**Same as Australia:** `|Throw number - Catch number| = points`

#### 2. Tourist Sites
**Base Scoring:**
- 1 point per NEW COUNTRY visited (not individual sites!)
- Countries visited in previous rounds don't score again

**Region Bonus:**
- Each region has a 2-5pt bonus
- First to complete region gets bonus
- Multiple players completing same round: all get bonus

**Passport Bonus (UNIQUE TO EUROPE):**
- Visit at least ONE country in EVERY region
- **Scoring:**
  - 1st player: 7 points
  - 2nd player: 3 points  
  - 3rd player: 1 point (only in 3-4 player games)
- If multiple players finish simultaneously: all get highest available score
- Can only be scored once per game

#### 3. Cuisine
**Items & Values:**
- Beer = 1
- Cheese = 2
- Wine = 3
- Spirits = 5

**Scoring Rule:**
- Sum all cuisine values for the round
- If sum is 1-7: **Score the sum as-is**
- If sum is 8+: **Score HALF (rounded up)**

**Examples:**
- 3 Beer + 1 Cheese = 3+1+1+2 = 5 → Score 5
- 2 Wine + 1 Spirits = 3+3+5 = 11 → Score 6 (11/2 rounded up)

#### 4. Transport
**Scoring:** Only PAIRS count!

**Values per Pair:**
- Cars = 3
- Trains = 4
- Bicycles = 5
- Boats = 7
- Planes = 9

**Same pairing rules as Australia Animals**

#### 5. National Treasures (UNIQUE MECHANIC!)
**Available Treasures:**
- Art
- Architecture
- Music
- Natural Wonders

**Scoring Rule:**
- Choose ONE treasure type to score per round
- Must have at least ONE matching icon yourself
- Count matching icons on ALL OTHER PLAYERS' cards
- Score: **4/3/2 points** per match (in 2/3/4 player games)
- Do NOT count your own cards
- Can score each treasure type only ONCE per game

**Example (4-player game):**
- You have 1 Art icon
- Other players show: 0 Art, 4 Art, 8 Art icons
- You score: (0+4+8) × 2 = 24 points

### Game End
**Winner Determination:**
1. Highest total score wins
2. **Tiebreaker 1:** Passport bonus points
3. **Tiebreaker 2:** Throw & Catch points

---

## Boomerang USA 🗽

### Game-Specific Rules

#### Card Passing Direction
- Same as Australia/Europe (next player, then previous for final card)

#### Regions
- US regions separated by color
- 4 towns per region
- 3pt bonus per region

### Scoring Categories

#### 1. Throw & Catch (DIFFERENT FORMULA!)
**Formula:** 
- If `Catch number ≥ Throw number`: Score = **Throw number**
- If `Catch number < Throw number`: Score = **0**

**Examples:**
- Throw = 4, Catch = 5 → Score 4
- Throw = 4, Catch = 4 → Score 4
- Throw = 3, Catch = 2 → Score 0

#### 2. Tourist Sites
**Base Scoring:**
- 1 point per NEW TOWN visited
- Towns visited in previous rounds don't score again

**Region Bonus:**
- Complete all 4 towns in a region: **+3 points**
- First to complete gets bonus
- Multiple players completing same round: all get bonus

**West-East Connection Bonus (UNIQUE TO USA):**
- Create continuous path from west coast to east coast
- Path formed by visiting connected towns (pathways shown on map)
- **Scoring:**
  - 1st player: 7 points
  - 2nd player: 3 points
  - Others: 1 point (in 3-4 player games)
- Can only be scored once per game
- West and east coast cities marked in blue on map

#### 3. Americana (INCREMENTAL RULE!)
**Items & Values:**
- Mail Boxes = 1
- Baseball Caps = 2
- Football Jerseys = 3
- US Flags = 5

**Scoring Rule (UNIQUE MECHANIC):**
- Sum all Americana values for the round
- **Must score AT LEAST as much as previous round, or score 0!**
- If you scored 0 last round, you only need ≥1 this round

**Examples:**
- Round 1: 10 points
- Round 2: Must score ≥10 or get 0
- If Round 2 = 7 points → Score 0
- Round 3: Now you only need ≥1 to score

#### 4. Animals
**Scoring:** Only PAIRS count!

**Values per Pair:**
- Trout = 3
- Cattle = 4
- Grizzly Bears = 5
- Bald Eagles = 7
- Bigfoot = 9

**Same pairing rules as Australia**

#### 5. Activities
**Same as Australia:**
- Sightseeing, Sporting Events, Hiking, Dining
- Same scoring table (0, 2, 4, 7, 10, 15)
- One per round, each type once per game

### Game End
**Winner Determination:**
1. Highest total score wins
2. **Tiebreaker 1:** West-East connection points
3. **Tiebreaker 2:** Throw & Catch points

---

## Key Design Differences

### Comparison Matrix

| Feature | Australia | Europe | USA |
|---------|-----------|--------|-----|
| **Throw & Catch** | Absolute difference | Absolute difference | Conditional (≥ or 0) |
| **Location Scoring** | Individual sites | Countries | Towns |
| **Regions** | 7 regions, 4 sites | Variable regions | Variable regions |
| **Region Bonus** | 3pt | 2-5pt | 3pt |
| **Special Bonus** | None | Passport (all regions) | West-East connection |
| **Category 3** | Collections (doubling) | Cuisine (halving) | Americana (incremental) |
| **Category 4** | Animals (pairs) | Transport (pairs) | Animals (pairs) |
| **Category 5** | Activities (optional) | National Treasures (look at others) | Activities (optional) |
| **Tiebreaker** | Throw & Catch | Passport → T&C | West-East → T&C |

### Scoring Mechanic Patterns

#### Pattern 1: Sum with Threshold Modifier
- **Australia Collections:** Double if ≤7
- **Europe Cuisine:** Halve if ≥8
- **USA Americana:** Must maintain/increase

#### Pattern 2: Pair Scoring
- Animals (Australia, USA)
- Transport (Europe)
- Always: pairs only, specific values

#### Pattern 3: Frequency-Based Optional
- Activities (Australia, USA)
- One per round, each type once per game
- Scoring table based on count

#### Pattern 4: External Observation
- National Treasures (Europe only)
- Scores based on OTHER players' cards

---

## Implementation Considerations

### Extensibility Requirements

#### Future Modifications (From Exam Requirements)
1. **Direction Variant:** Alternate passing direction each round (left/right)
2. **New Game Modes:** Europe and USA editions with new mechanics
3. **New Card Types:** Support for new symbols and attributes
4. **New Scoring Mechanisms:** Support for diverse scoring rules

### Design Patterns to Consider

#### Strategy Pattern
- Different scoring strategies per category
- Different Throw & Catch calculations
- Different collection/pair scoring rules

#### Factory Pattern
- Card creation for different editions
- Scoring category creation

#### Observer Pattern
- National Treasures needs to observe other players' cards
- Score updates and notifications

#### Template Method
- Common game flow with variant-specific implementations

### SOLID Principles Application

#### Single Responsibility
- Separate classes for: Card, Player, Scoring, Game Logic, Network
- Each scoring category = separate class

#### Open/Closed
- Easy to add new game editions without modifying existing code
- New scoring categories via interface implementation

#### Liskov Substitution
- All game editions should be interchangeable
- All scoring categories follow same contract

#### Interface Segregation
- Separate interfaces for different scoring types
- Don't force all scorers to implement unused methods

#### Dependency Inversion
- Depend on abstractions (interfaces) not concrete implementations
- Game logic depends on scoring interfaces, not specific scorers

### Testability Requirements

#### Unit Test Coverage Needed
1. Card dealing (7 cards per player)
2. Throw card selection and hiding
3. Card passing mechanics (direction changes)
4. Each scoring category independently
5. Region completion detection
6. Activity "used once" tracking
7. Round and game end detection
8. Winner determination and tiebreakers
9. Network communication (if applicable)

#### Edge Cases to Test
- 2 players vs 4 players
- Simultaneous region completion
- All players tie for special bonuses
- USA Americana: maintaining streak
- Europe National Treasures: counting others
- Activities: attempting to score twice
- Invalid card selections

### Modifiability Hotspots

#### Network Architecture
- Current implementation mixes server/client/game logic
- Should be separated for flexibility

#### Card Data
- Currently hardcoded in constructor
- Should be data-driven (CSV, JSON, database)

#### Scoring Logic
- Currently embedded in Player class
- Should be extracted to separate scorer classes

#### Game State Management
- Mixed with player logic
- Should have dedicated game state manager

---

## Requirements Mapping

### Requirement Coverage (Rules 1-12)

| Req | Description | Australia | Europe | USA |
|-----|-------------|-----------|--------|-----|
| 1 | 2-4 players | ✓ | ✓ | ✓ |
| 2 | 28 cards | ✓ | ✓ | ✓ |
| 3 | Shuffle deck | ✓ | ✓ | ✓ |
| 4 | Deal 7 cards | ✓ | ✓ | ✓ |
| 5 | Select Throw (hidden) | ✓ | ✓ | ✓ |
| 6 | Pass to next player | ✓ | ✓ | ✓ |
| 7 | Show selected cards | ✓ | ✓ | ✓ |
| 8 | 6 draft iterations | ✓ | ✓ | ✓ |
| 9 | Last card to previous | ✓ | ✓ | ✓ |
| 10a | Throw & Catch scoring | ✓ | ✓ | ✓ (modified) |
| 10b | Sites + Region bonus | ✓ | ✓ (countries) | ✓ (towns) |
| 10c | Collection scoring | ✓ | ✓ (cuisine) | ✓ (americana) |
| 10d | Animal pair scoring | ✓ | ✓ (transport) | ✓ |
| 10e | Activity scoring | ✓ | ✓ (treasures) | ✓ |
| 11 | Next round setup | ✓ | ✓ | ✓ |
| 12 | 4 rounds, winner | ✓ | ✓ | ✓ |
| 13 | Direction variant | Future | Future | Future |
| 14 | Multi-edition support | - | Extension | Extension |

---

## Card Data Reference

### Australia Cards (28 total)
See `cards.csv` for complete card data.

**Format:** Name, Letter, Region, Number, Collection, Animal, Activity

**Example:**
- The Bungle Bungles, A, Western Australia, 1, Leaves, -, Indigenous Culture
- The Pinnacles, B, Western Australia, 1, -, Kangaroos, Sightseeing

### Europe Cards
Not provided in current dataset - would follow similar structure with:
- Countries instead of sites
- Cuisine icons
- Transport icons
- National Treasure icons

### USA Cards  
Not provided in current dataset - would follow similar structure with:
- Towns instead of sites
- Americana icons
- Animal icons
- Activity icons
- Path connections on map

---

## Notes for Implementation

### Current Code Issues (BoomerangAustralia.java)
1. Single monolithic file with server, client, and game logic
2. No clear separation of concerns
3. Hardcoded card data
4. Player class handles too many responsibilities
5. Scoring logic embedded in Player.roundScore()
6. Network communication tightly coupled
7. No unit tests
8. Limited error handling
9. Magic numbers throughout
10. No interfaces or abstractions

### Recommended Refactoring Steps
1. Extract Card as separate class with factory
2. Create scoring interfaces and implementations
3. Separate Game, Round, and Player logic
4. Extract network layer
5. Create game state manager
6. Add comprehensive unit tests
7. Document all public APIs
8. Add proper error handling
9. Use dependency injection
10. Apply design patterns appropriately

---

**End of Document**
