Attrition - CS408 Project at Purdue
===================================

The project my team created in CS408 at Purdue in the Spring of 2014. The project aimed to create a tower
defence game that was a little bit different from other TD games in that we added a controllable character
along side of the towers.

The game itself is written in Java, and the game was built upon the framework found at gametutorial.net.

There were 4 members on the team, with nobody really owning any specific parts.

Building/Running the Project
============================

In order to build the project, you must have the Java Development Kit (JDK) installed. The Java Runtime
Environment (JRE) will not do. You can build the project into a JAR file by running the "Make.bat" script
in the source directory. If you would just like to compile it, run the command "javac *.java".

In order to run the project, you have to do one of 3 things. If you built the project using the Make
script, you can just run the JAR file by double clicking it. If you built the project into source files,
you'll just run the command "java Window". However, the easiest way to run it would be to execute the
"Run.bat" script in the source directory.

Gameplay Details
================

Attrition is a single­player video game of the tower­defense genre. The objective of the game is to
survive and destroy the waves of enemy invasions until a game victory status is reached.

Difficulty selection: There are 3 difficulties to choose from, with higher levels starting with less
player health, less starting resources, and stronger enemies.

Map: The map is a set on a bridge in the cosmos, and is laid out like any other TD game.

Health: The player has a set amount of health based on the difficulty. If it reaches 0, the game ends as
a loss. You lose health by allowing enemies to reach the end of the path.

Resources: This is the game's currency, and is what you use to buy and upgrade towers. The starting
amount is based on the chosen difficulty. You gain more resources by killing enemies.

Towers: Like any other TD games, the towers form the main weapons. You place towers by left clicking on
a space that is not on the path. If you left click on an existing tower, you can then move it by using
the arrow keys. Towers can be upgraded by middle clicking on them, assuming you have enough resources.
There was supposed to be several kinds of towers, but we ran out of time.

Enemies: The enemies are like any other TD game. They spawn at a common point, and attempt to reach the
end of the path. They are organized into waves, and there are several kinds of enemies. At the end of
each wave, you'll be granted bonus resources based on the difficulty. You win the game by clearing all
the waves.

Character: As stated, you also control a player character. It spawns at the end of the path, is
controlled with the WASD keys, and will fire in the direction it's facing by pressing the space bar. The
character is confined to the bounds of the path.
