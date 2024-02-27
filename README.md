# Ice Field Game

A complex Java application that features advanced use of OOP concepts, thorough and rigorous UML modelling, software design, and UI features.

## Scope of the project
The software is a game program that was developed for the Software Project Laboratory (BMEVIIIAB02) at Budapest University of Technology and Economics, which is a semester long, project focused lab.

## Documentation
The project includes well-detailed and extensive documentation - see the Requirements/Analysis model and Test framework description in the docs folder, in addition to project-wide comments. Due to their large scope, the documentation has been translated to English from Hungarian via DeepL, so documents might contain grammatical and contextual errors. Comments were not translated, by I believe the self-commenting nature of the code makes up for this. 

## Overview 
The main part of the game is occupied by the game board, which contains an arrangement of ice blocks. You can also find an information window here, which describes what you can know about a particular ice block or player. The user can use the mouse and keyboard to control the actions of characters and retrieve information about a particular ice block.

![Game preview](https://github.com/hainbalazs/bme-java-icefield/blob/main/media/game-example-2.png?raw=true)

## Technical stack
Required skills for the project: Java, OOP concepts, Design patterns, UI development, UML modelling, Project management tools (Kanban Board), Team collaboration.

## Rules of the game
The game has two types of players, Eskimos and Arctic explorers. Eskimos start the game with 5 lives, Arctic explorers with 4. Neither player can exceed this during the game. There can be 3-10 players per game (to keep the game enjoyable, but in principle it could be any number). 
Players move in rounds. Each player is given a number, the players take turns according to this order, which remains their ID until the end of the game. A player's turn ends when he as spent all his action points or no longer wishes to act. 
 
### The ice field
 
The ice field is made up of polygonal ice shelves. Two fields are adjacent if they have sides in common. There are stable and unstable ice sheets; stable ice sheets can have any number of people on them at the same time, while unstable ice sheets can have only a limited number of people on them. All ice tiles on which the players are standing at the start of the game are stable. 
If there are more players on the ice board than there can be, all the players on the board will fall into the water. This value varies per ice board, the value of an ice board does not change during the game. 
The  ice  sheets  are  covered  with  snow,  layers  0-5,  where  0  indicates  a  snow-free  field.  All players can see how much snow is on which field. There can also be a hole on an ice sheet, which if a player steps on it, the player's turn ends immediately. The hole is not visible if the field has at least 1 layer of snow.  A maximum of 1 object can be frozen in a block of ice. These can be: shovel, rope, wetsuit, food, gun, flare, cartridge. Pistol, flare, cartridge (key items): exactly one of each item can be found on the ice field. No objects are allowed on ice slabs with holes. Objects can be seen if there is 0 layer of snow on the ice sheet. These objects can be dug out under certain conditions.

### The course of a round
Each player can do 4 units of work in a round. One of the following actions can be performed for 1 action point: 
- snow clearance 
- movement to an adjacent ice sheet 
- digging up an object 
- rescue teammates 
- iglu construction 
- soil research 
- Assembling the flare 
- eating food 
Snow clearing: any operator can clear snow. Any player can clear snow from the field on which that player is standing. Only the player who is standing on the player's position can clear 1 layer of snow. Digging up an object: an object in a field may be dug up by the player standing on that field if there is no snow or igloos on that field. In this case, the object is placed in the bag of that player. 
A character may carry any number of objects, even several identical ones. 
Rescuing teammates: players with a rope can rescue a teammate who has fallen into the water in the adjacent field if they have a rope. In this case, the drowned player will be moved to the rescuer's field. 
Building an igloo: Eskimos can build an igloo on the ice slabs if there is at least 1 layer of snow on the slabs, after building the igloo all snow will disappear from the field. The igloo remains on the ice until it is removed or the game ends. An Igloo can only be built by an Eskimo on the field it is standing on. 
Soil exploration: a polar explorer can map the field he is standing on, or any adjacent field. This will reveal how many players the field being examined can support and whether it has any holes. 
Eating food: Any character with food in their bag may eat one, increasing their life points by one. 
Assembling the flare: it is possible when all the players are standing on the same field and have found all 3 key pieces. At this point the game ends. 
 
At the end of each round there are 2 options, either a blizzard comes or it doesn't. If it does, then any player not on a field with igloos on it loses 1 life point.Each field has a percentage chance of one or more layers of snow falling, in no case more than 5 snow on any one field. 

If there has been a snowstorm, a counter is started from a randomly drawn positive integer value. This is decremented by 1 at the end of each round. If this value reaches 0, there is a blizzard and the counter restarts as described above. This value is known to the players. There can be a maximum of 1 blizzard per round. 
 
Death of a character: a character can die in one of three ways 
- loses his last life point in a blizzard 
- fell into a hole and nobody pulled him out until the beginning of his next round, and he had no wetsuit 
- fell into the water while standing on an unstable ice floe, and the actor who stepped on 
it exceeded the ice floe's load-bearing capacity. In this case, the player dies when all the players on land have already made at least one move, i.e. all of them have had a chance to rescue him. 
Whichever way a character dies, the game ends immediately.

## Authorship

The project was developed by a team of 5 students including Bernát Ágó, Márton Bankó, Csaba Juhász, Tamás Lukács and me.
The total hours of work involved in the project were 478 out of which I worked 103 hours on the project on my own.