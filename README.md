# Knight's Move Game 🎮

A chess-inspired educational game. Built with Java and JavaFX.

## 🎯 Overview

Knight's Move is an educational game that combines chess mechanics with quiz elements. Players control a knight piece on a 8x8 chessboard, collecting points by visiting squares while avoiding the king and queen pieces.

## ✨ Features

### Game Mechanics
- Dynamic 8x8 chessboard with cyclic movement (pieces can wrap around edges)
- Multiple game pieces with unique behaviors:
  - Knight: Player-controlled piece with chess-like movement
  - Queen: Opponent that moves diagonally or straight
  - King: Opponent that moves one square at a time
- Special squares with unique effects:
  - Random Jump Squares: Teleports knight to random location
  - Forgetting Squares: Erases last three moves
  - Blocked Squares: Inaccessible areas (Level 4)

### Educational Elements
- Multiple-choice questions with 3 difficulty levels:
  - Easy (1 point)
  - Medium (2 points)
  - Hard (3 points)
- Question management system
- Points system with rewards and penalties

### Game Progression
- 4 unique levels with increasing difficulty
- Each level introduces new mechanics and challenges
- Score tracking and high score system
- Trophy system for high achievers (200+ points)

## 🎮 Gameplay

### Levels
1. Basic knight and queen movements
2. Enhanced knight movement (2 straight + 1 diagonal)
3. King moves automatically and his speed increases every 10 seconds
4. Addition of blocked squares

### Scoring
- Points for visiting new squares
- Points for correctly answering questions
- Penalties for revisiting squares
- Minimum 15 points required to advance (reduced to 2 points, for now)

### Win Conditions
- Complete all levels
- Accumulate points (Trophy at 200+ points)

### Loss Conditions
- Collision with king/queen
- Failure to reach 15 points in a level
- Time expiration

## 🛠 Technical Details

### Built With
- Java
- JavaFX
- JSON for question storage
- MVC architecture
- Design Patterns

### Features
- User profile system
- Question management interface
- Game history tracking
- Score persistence

## 📝 Development

This project was developed as part of the Software Engineering and Software Quality Assurance course at the University of Haifa.

## 🚀 Getting Started

### Prerequisites
- Java 17
- JavaFX 20 (included)

### Running the Application

The application can be run using the provided `run.sh` script in the target directory.

**Windows Users:**
- Navigate to the target directory and double-click the `run.sh` file
- Or run it from command prompt with `run.sh`

**Unix/Linux/MacOS Users:**
```bash
# Make the script executable (if it's not already runnable)
chmod +x run.sh

# Run the application
./run.sh
```
## 🎮 Gameplay Demo

Watch a brief demonstration of the game:
<video src="https://raw.githubusercontent.com/mlk500/Knight-Move-Game/master/gameplay-video/knights-move-vid.mov" controls="controls" style="max-width: 730px;">
</video>
