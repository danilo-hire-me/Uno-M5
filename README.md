# Uno Game For SYSC 3110 Project Milestone 5 (Bonus)

### Team

<table>
  <tr>
    <td>
      <img src="https://github.com/qydan.png" width="100px" alt=""/><br />
      <b>Aydan Eng</b><br />
      <a href="https://github.com/qydan">GitHub</a>
    </td>
    <td >
      <img src="https://github.com/AjanzzSkool.png" width="100px" alt=""/><br />
      <b>Ajan Balaganesh</b><br />
      <a href="https://github.com/AjanzzSkool">GitHub</a>
    </td>
<td>
      <img src="https://github.com/AwsAli05.png" width="100px" alt=""/><br />
      <b>Aws Ali</b><br />
      <a href="https://github.com/AwsAli05">GitHub</a>
    </td>
<td>
      <img src="https://github.com/danilo-hire-me.png" width="100px" alt=""/><br />
      <b>Danilo Bukvic</b><br />
      <a href="https://github.com/danilo-hire-me">GitHub</a>
    </td>
  </tr>
</table>

Uno Flip Game created for the class project for the Carleton University SYSC3110 Fall 2025.

## Contributions

**Group Members:**
* Ajan Balaganesh
* Danilo Bukvic
* Aydan Eng
* Aws Ali

### Individual Breakdown (Core Project)

* **Ajan Balaganesh:**
    * Contributed to the core `UnoModel` logic, specifically managing turn progression and state updates.
    * Assisted in implementing the `UnoView` interface and integrating it with the main frame logic.
    * Wrote unit tests for `UnoCardTest` to verify card matching logic.

* **Danilo Bukvic:**
    * Implemented the **Uno Flip** mechanics, including the `isDark` state toggling and the dual-sided `UnoCard` logic.
    * Developed the `UnoController` to handle user input and bridge the Model and View.
    * Refined the Javadoc documentation across the project.

* **Aydan Eng:**
    * Implemented the **AI Player** logic in `UnoModel`, specifically the strategy for prioritizing action cards.
    * Created the `UnoAiTest` suite to verify computer opponent behavior.
    * Worked on the GUI layout in `UnoFrame` to ensure correct updates during game loops.

* **Aws Ali:**
    * Designed the `UnoEvent` class to ensure safe data transfer between Model and View.
    * Implemented the specific card effects (Draw 5, Skip Everyone, Wild Draw Color) in the Model.
    * Wrote `UnoControllerTest` and `UnoModelTest` to ensure robust error handling.

## Deliverables and Roadmap

### DONE: Milestone 5 (Bonus- Version 5.0)
- **Feature 1 - Images:** Added background images and button icons.
- Visual update for flipping between Light vs. Dark mode.
- UI scaling and font improvements.

### DONE: Milestone 4 (Version 4.0)
- Replay Functionality (Rounds & Scoring logic)
- Undo / Redo Functionality
- Serialization (Save / Load Game)
- GUI Menu Bar Integration
- JUnit Tests for new features

### DONE: Milestone 3 (Version 3.0)
- Flip Card implemented
- Draw Five
- Skip Everyone
- Wild Draw Colour
- Updated Scoring System
- AI player system implemented
- JUNIT test for Uno Flip
- JUNIT test for AI behaviour

### DONE: Milestone 2 (Version 2.0)
- GUI Implementation
- Unit test for model components, currently missing areas of game logic
- Changes to documentation

### DONE: Milestone 1 (Version 1.0)
- readme file
- code (source + executable in a jar file)
- UML Diagram
- Documentation (UML Diagram, detailed description of design decisions, user manuals, javadoc documentation)

## Changelog

### Milestone 5 (Bonus Feature)

#### Feature Selected: Feature 1 - Images
**Gameplay Explanation:**
The game now features dynamic background images that change depending on whether the game is in "Light" or "Dark" mode. This enhances the visual feedback for the Uno Flip mechanic, making it immediately obvious which side is active. Additionally, the "Draw" and "Next Player" buttons now include graphical icons for better UI clarity.

**Implementation Details:**
* **`UnoFrame.java`:** Modified to include a custom inner class `BackgroundPanel` that overrides `paintComponent` to render the background image.
* **Transparency:** Implemented transparency (`setOpaque(false)`) on child panels (Hand, Stats, Buttons) so the background remains visible.
* **Resources:** Added an `images/` directory containing `bg_light.jpg`, `bg_dark.jpg`, `draw_btn.png`, and `next_btn.png`.
* **Dynamic UI:** Text colors now automatically toggle between Black (Light Mode) and White (Dark Mode) to ensure readability against the backgrounds. Buttons were resized to `50x50` to accommodate the new icons.

**Contributors for Milestone 5:**
* Danilo Bukvic, Ajan Balaganesh, Aydan Eng and Aws Ali- Implemented image loading, background panel logic, and UI asset integration.

### Milestone 4 (Version 4.0)

#### Added
- **Undo/Redo System:** Implemented a history stack system using serialization to snapshot game states, allowing players to undo moves and redo them.
- **Serialization (Save/Load):** Added functionality to save the current game state to a file (`.ser`) and load it back later to resume play.
- **Scoring & Rounds:** Implemented official scoring rules. When a player empties their hand, the round ends, and points are tallied based on opponents' remaining cards. The game continues in rounds until a player reaches 500 points.
- **GUI Menu Bar:** Added a `JMenuBar` to the main frame containing "File" (Save, Load) and "Game" (Undo, Redo) options.
- **JUnit Tests:** Added `UnoMilestone4Test` to verify serialization persistence and undo/redo logic.

#### Changed
- Updated `UnoModel`, `UnoPlayer`, and `UnoCard` to implement `Serializable`.
- Refactored `UnoView` interface to include `handleRoundEnd` for intermediate round summaries.
- Updated `UnoController` to handle `SAVE`, `LOAD`, `UNDO`, and `REDO` commands.

### Milestone 3 (Version 3.0)

#### Added
- Full Uno Flip special card functionality:
    - Flip Card (switch all cards between Light and Dark sides)
    - Draw Five (next player draws 5 cards and loses their turn)
    - Skip Everyone (all opponents skipped, current player goes again)
    - Wild Draw Colour (player selects new dark colour; next player draws until matching colour)
- Dual-sided Light/Dark card system and Flip mechanic for the deck and discard pile
- Updated scoring rules for Uno Flip cards
- AI Player system supporting 2–4 players, with selection between Human or AI
- AI legal-move validation and basic strategy for selecting playable cards
- JUnit tests for:
    - Uno Flip special cards
    - AI player behaviour and strategy
- Updated UML class diagrams including new card types, AIPlayer, and dual-side card logic
- New sequence diagrams:
    - Human player plays a Flip Card
    - AI Player selects and plays a move
- README updates explaining AI strategy, special cards, and milestone-specific features

#### AI Strategy
The AI Player implementation uses a priority-based strategy rather than random selection to provide a competitive experience. When it is the AI's turn, it evaluates its hand against the current top card and active side (Light/Dark):
1.  **Win Condition:** If the AI has one card left and it matches, it plays it immediately.
2.  **Aggression (Action Cards):** The AI prioritizes playing Action cards (Skip, Reverse, Draw Five, Flip, etc.) to disrupt the next player's turn.
3.  **Standard Play:** If no action cards are valid, it plays a matching number or color card.
4.  **Wild Cards:** Wild cards are saved for last resort to ensure the AI always has a playable option if colors change.
5.  **Draw:** If no legal move is available, the AI draws a card.

#### Changed
- Updated internal data structures to support Light/Dark card states and Flip behavior
- Modified game loop and turn-handling logic to incorporate AI players and new special card effects
- Improved deck/discard logic to support flipping between sides
- Updated documentation to match new system architecture and gameplay rules

Uno Flip Rules and Scoring: https://www.unorules.com/uno-flip-rules/

### Milestone 2 (Version 2.0)

#### Added
- Java Event Model to keep track and update game state
- Further error handling of user input
- JUnit testing for new game logic and implementation
- UML Diagram of new MVC implementation
- Sequence Diagram of new MVC implementation

#### Changed
- Refactored game to use MVC (Model - View - Controller) implementation
- Updated readme to reflect new changes and documentation

###
