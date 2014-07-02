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

Attrition is a single­player video game of the tower­defense genre. The objective of the game is to
survive and destroy the waves of enemy invasions until a game victory status is reached.

Difficulty selection:
		Three different difficulty options are displayed for the player to
		choose from: Easy, Medium, and Hard. Higher difficulty levels entail less player
		health, less starting resources, and stronger enemies.
Map:
		The gameplay is set on a futuristic map of the cosmos, with a clearly defined
		path leading to the player’s home base (the moon) which the enemies will travel
		toward.
Health:
		The player begins with a set amount of health depending on the difficulty
		selected (Easy: 100, Medium: 75, Hard: 50), and this count must be maintained at a
		level above zero to continue playing the game. Upon reaching zero, the game ends
		and a ‘Game Over’ screen is displayed which states: “You have died. Click
		anywhere to continue.” The player will lose health each time an enemy survives long
		enough to reach the home base at the end of the map’s path. The exact amount of
		health lost is equal to the remaining health of this enemy.
Resources:
		This statistic can be considered the game’s currency and is used by
		the player to purchase towers that are then placed on the map. The player will
		receive a certain number of resources at the beginning of the game based on the
		difficulty level selected (Easy: 300, Medium: 250, Hard: 200), and will accrue
		additional resources for each enemy that the player kills. The exact amount of
		resources gained when killing an enemy is equal to the initial health of that enemy.
Towers:
		These are the main weapons that the player can utilize to vanquish
		enemies, thereby protecting his home base and obtaining more resources. Towers
		may be placed on any tile within the map that is not a part of the path or the home
		base by left­clicking with the mouse on that tile. When enemies walk within the
		general vicinity of a tower, it will begin firing damaging lasers at that enemy and will
		continue until the enemy either is killed or walks out of range. Once created, towers
		can be moved by left­clicking on them in order to select them, and then navigating
		them around the map with the arrow keys. They can also be upgraded (for a fee in
		resources) by middle­clicking them. Once upgraded, towers will do more damage to
		enemies.
Enemies:
		The main antagonist of this game is the collective group of enemies
		which attempt to invade the player’s home base. These enemies will perform
		organized assaults in the form of waves of attack: where a group of enemies will
		spawn within a short span of time and begin walking toward the home base.
		Different enemies may have different speeds or amounts of hit points. At the end of
		each wave, as a reward for clearing that wave, a bonus amount of resource points
		is given to the player based on the difficulty (Easy: 50, Medium: 35, Hard: 25) The
		game is won once every wave of the enemy forces is defeated, upon which a
		‘Victory’ screen will be displayed.
Character:
		The character is an individual entity in the game that spawns within the
		home base and can be navigated with the WASD keys (‘W’ moves the character
		up, ‘A’ moves the character to the left, etc.) The movement of this character is
		restricted to within the bounds of the path and the home base. The character can
		also attack by pressing the spacebar which will fire a projectile in the direction that it
		last moved. An enemy that comes into contact with this projectile will be damaged.
		Once fired, there is a two second cool­down before another projectile can be fired.
