# KU Tower Defense

A 2D tower defense game developed in Java 21 and JavaFX 21.0.7, without using a game engine.

The project contains both a playable tower-defense game and an in-game map editor. The gameplay is driven by a separate domain layer, while JavaFX handles the scenes, rendering, animation, and player interaction.

## Download

**[Release v1.0.0](https://github.com/comp302-zibzibzibzib/KUTowerDefense/releases/tag/v1.0.0)**

| Platform | Release archive |
|---|---|
| Windows | `KuTowerDefense_Windows-x64.zip` |
| Linux | `KuTowerDefense_Linux-x64.tar.gz` |

The release packages are self-contained application images and include the Java runtime. You do not need to install Java or configure a separate JavaFX SDK to run the game.

## Gameplay

The goal is to defend the castle from incoming enemy waves. Enemies follow paths across the map, and towers placed on available lots automatically target enemies within range and fire projectiles. Defeated enemies reward gold, enemies that reach the end of the path cost the player a life.

### Towers

There are three tower types, each with its own attack and upgrade behavior:

| Tower | Attack | Upgrade |
|---|---|---|
| **Archer Tower** | Fires arrows at enemies in range. | Increases range and doubles its firing rate. |
| **Artillery Tower** | Fires artillery projectiles with area damage. | Increases range and uses the improved artillery projectile. |
| **Mage Tower** | Fires magical projectiles. | Changes to a slowing spell, allowing it to hinder enemy movement. |

Towers select targets based on enemy progress along the path, rather than simply choosing the closest enemy. Towers can be upgraded once, provided the player has enough gold. They can also be sold to recover part of their cost.

### Enemies and waves

Enemy waves are generated during play. A wave is made up of groups, and each group contains a randomly composed set of enemies. The number of groups and enemies scales with the current wave, so later waves become larger. Spawn delays control the pacing between waves, groups, and individual enemies.

The two enemy types have different characteristics:

- **Goblins** are faster and take reduced damage from magic attacks.
- **Knights** are slower, have more health, and take reduced damage from arrows. Knights also speed up when they are close to goblins which is indicated by a flash next to their health bar.

The game tracks the player's gold, remaining lives, and current wave. Killing enemies grants gold, which can be spent on building and upgrading towers. The in-game controls also let you pause, resume, and change the game speed.

### Map editor

The built-in map editor lets you create maps instead of relying only on predefined layouts. Maps are built on a tile grid, with tools for placing and removing:

- Path tiles, including straight segments, turns, and junctions.
- Starting and ending points for enemy routes.
- Empty lots where towers can be built during gameplay.
- Decorative objects such as houses, trees, rocks, wells, and wood.
- The castle, which occupies a 2×2 tile area.

The editor checks map validity before saving. A valid map needs start and end points, at least 4 available lots, and a connected route from a start to an end. The game uses `A* pathfinding` to find routes through the path tiles.

Maps can be saved and loaded between sessions. The project also stores snapshot images for saved maps.

### Game options

The options screen exposes gameplay parameters rather than only presentation settings. Options are grouped into the following categories:

- **Spawning:** wave count, group sizes, enemies per group, enemy composition, and spawn delays.
- **Player:** starting gold and lives.
- **Enemies:** goblin and knight health, speed, and rewards, as well as the chance of a gold bag dropping.
- **Projectiles:** arrow, artillery, and spell damage, plus area-of-effect range.
- **Towers:** each tower's cost, upgrade cost, range, and firing rate.
- **Music:** background music volume.

Changes can be saved, or the options can be reset to their defaults.

## Project structure

The source is organized into a domain layer and a JavaFX UI layer. The domain layer contains the game rules and state, controllers expose the operations and data needed by the UI.

```
KUTowerDefense/
├── Data/
│   ├── Maps/                 # Serialized saved maps
│   ├── Options/              # Default and user game options
│   └── Snapshots/            # Preview images for saved maps
├── src/
│   └── main/
│       ├── java/
│       │   ├── domain/
│       │   │   ├── controller/
│       │   │   ├── entities/
│       │   │   ├── kutowerdefense/
│       │   │   ├── map/
│       │   │   ├── services/
│       │   │   └── tower/
│       │   ├── ui/
│       │   └── module-info.java
│       └── resources/
│           ├── Images/
│           └── soundtrack.mp3
├── pom.xml
└── credits.txt
```

### `domain.controller`

Controllers form the boundary between the UI and the game logic. They provide operations for the main menu, map editor, gameplay, player state, towers, and game options.

- `MapEditorController` exposes map editing operations and tile information.
- `PlayModeController` exposes the active map and play-session controls, including pause, resume, and game speed.
- `TowerController` handles tower construction, upgrades, selling, targeting updates, and projectile state.
- `EntityController` exposes enemy, effect, and gold-bag state for rendering.
- `PlayerController` exposes player values and listeners used to update the HUD.
- `GameOptionsController` maps option names to the corresponding game-option values and constraints.

### `domain.entities`

Contains the runtime entities used during a game:

- `Enemy` defines shared enemy state, movement along a path, health, and damage handling.
- `Goblin` and `Knight` implement their different damage resistances and behavior.
- `Wave` coordinates the spawning of groups.
- `Group` coordinates the timed spawning of individual enemies.
- Factories create enemies, groups, and waves.
- `GoldBag` and `Effect` represent gameplay pickups and visual effects.

### `domain.tower`

Contains the tower and projectile systems:

- `Tower` implements target selection, firing cadence, freezing, and projectile creation.
- `ArcherTower`, `ArtilleryTower`, and `MageTower` define tower-specific upgrades.
- Projectile classes implement projectile movement and impact behavior.
- Factories create towers and the different projectile types.

### `domain.map`

Defines the game's tile-based world:

- `Map` stores the tile grid, path connections, starting and ending points, and towers.
- `Tile`, `PathTile`, `Lot`, and `DecorativeTile` represent the different map cells.
- `MapEditor` applies edits, maintains tile connections, and validates maps.
- Enums such as `TileType`, `PathType`, `TowerType`, and `DecorativeType` describe the available map elements.

### `domain.kutowerdefense`

Holds the central game state and configuration:

- `KUTowerDefense` and `PlayModeManager` manage the application/game session state.
- `Player` stores current gold, lives, and wave progress.
- `GameOptions` stores the configurable gameplay parameters.

### `domain.services`

Contains shared utilities, including A* pathfinding, distance calculations, and serialization of maps and game options. The package also contains test and diagnostic classes.

### `ui`

Contains the JavaFX application and scenes:

- `KuTowerDefenseApp` is the JavaFX application entry point and handles scene transitions.
- `MainMenuScene`, `PlayModeScene`, `MapEditorScene`, and `OptionScene` implement the main menu, game, editor, and options screens.
- `MusicPlayer` handles background music.
- `DynamicPopup` provides popup UI elements.

## Run from source

### Requirements

- JDK 21
- Apache Maven 3.8+

JavaFX dependencies are declared in `pom.xml`. Maven resolves the required JavaFX modules and platform-specific artifacts automatically.

Clone the repository and run the application from the project root:

```bash
git clone https://github.com/comp302-zibzibzibzib/KUTowerDefense.git
cd KUTowerDefense
mvn clean javafx:run
```

**Important:** Run the application with the repository root as its working directory. The game reads and writes maps and options using relative paths under `Data/`. Starting it from another working directory can prevent it from finding saved data or cause files to be written somewhere else.

## Technologies

- Java 21
- JavaFX 21.0.7
- Apache Maven

## Credits

See [`credits.txt`](credits.txt) for asset credits.
