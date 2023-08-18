# Battleship Game
Battleship Game is an assessment for AA bank.

## Assumption
1. We have 2 players. Player1 and Player2.
2. Player 1 is the first one to join the game.
3. There is no need to persist the state of each object.
4. Simplicity is more important than other things.
5. There is no need to inform opponent about his turn.

## Introduction
The problem and its solution are explained.

### Points need to take into account:
* API design and documentation
* Readability and comprehensibility of the code (Clean code)
* Security and performance
* Testing your solution (e.g Unit testing)
* Conscious design/technical decisions
* Consistent and proper use of programming paradigms (such as object-oriented programming, functional programming)

### Game's Rule
Battleship is a classic board game where two players compete against each other to sink each other's fleet of ships. Here are the simple rules of Batleship:
Each player has their own grid board. The grid typically consists of 10x10 cells, labeled by numbers and leters (e.g., A1, B2, C3, etc.).
* Each player arranges their fleet of ships on their own grid board without showing their placement to the opponent.
* The fleet usually consists of the following ships, each occupying a certain number of consecutive cells horizontally or vertically:
1. Aircraft Carrier (5 cells)
2. Battleship (4 cells)
3. Cruiser (3 cells)
4. Submarine (3 cells)
5. Destroyer (2 cells)
* The ships can be placed either horizontally or vertically, but not diagonally. They should not overlap or touch each other. Below is an example of a accepted placed fleet.
 ![touch-ship](https://github.com/ma-sharifi/movieapi/assets/8404721/4f3f08b3-8355-451b-9c7b-36c93eecd5bb)


### Game Play
1. Players take turns to try and guess the location of their opponent's ships by calling out a cell on the opponent's grid (e.g., A1, B2, etc.)
2. The opponent responds with "Hit" if the guess hits a ship or "Miss" if the guess does not hit any ship. The hits and misses are marked on the player's own grid
3. If a ship is hit but not entirely sunk, the opponent simply says "Hit" without revealing which ship was hit. If a ship is hit and all its cells are sunk, the opponent declares "Sunk" and the player marks the ship as sunk on their own grid.
4. Players keep track of their guesses and the hits or misses
5. The game continues until one player has successfully sunk all their opponent's ships
6. The player who sinks all the opponent's ships first is declared the winner.

## Solution
Created a standalone java application that allows **users** to play together this game.
Used Java 17, Spring Boot 3, and Maven to implement this assignment. You can run the project with the following bash.
```shell
mvn spring-boot:run
````

Implemented GamePlayService for handling gameplay. I defined the following method for it.
1. createNewGame: Use for creating game. It will return a UUID as game id. After player creates the game, it must share the gameId with another player. 
2. joinGame: Players agreed on a game id. Users must join the game then they can play. only 2 players (player1 and player2) can join the game. This method creates players and assigns their opponent;
3. placeFleet: It places ships on board based on 3 fields of shipDto.
4. fire: The player guesses the location of the opponent's ship, then shoots it by presenting a label like A1. If the player guessed the correct ship coordinate, it will get Hit otherwise it will get Miss.

### How to test 
You can test this project 4 different ways:
1. Swagger. `http://localhost:8080/swagger-ui/index.html`
2. Run test `mvn test`
3. Postman. There is a Postman file of this project in this path located in `/documentation/Battleship.postman_collection.json`. After getting gameId, You must provide it as a value for "game-id" variable in the variables tab of Battleship collection then press the **SAVE** button. 
4. CLI. Commands provided with HTTPie.

### Objects

#### Game ID
It represents a battleship game.

#### Cell
Our board is composed of matrix of cells. It has the following fields.
* Character shipId: It holds the first character of ship as ship id
* boolean shot: If this cell shot, This indicates that the fire did not hit the ship, I take it as Miss.

#### Board
Board is a matrix of cell. We address each cell with a label such as A1, B6.
* A is an abbreviation of Aircraft Carrier.
* B is an abbreviation of Battleship and so on.
  ![ship-on-board](https://github.com/ma-sharifi/movieapi/assets/8404721/774bc0a2-beaf-4bfd-9cdc-08ecc9cd40d3)

#### Label
A label is a Coordinate of matrix cells represented by and string as a row and a number as a column.
A1 means cell in row 0 and column 0. 
* In the above picture, the Coordinate of ships is as follows:
* Aircraft Carrier is A1. Means A1 is the starting point of this ship.
* Battleship is C3. Means C3 is the starting point of this ship.
* Cruiser is I1
* Submarine is E5
* Destroyer is F3
* Note: As you know, when we have the start coordinate of a ship, direction, and length of it, we can find other coordinates of it.

#### Coordinate:
In which location our ship is placed, and if it shot or not?
* int row, column: The hold Coordinate of the ship on board
* boolean hit: If ship shot, I take it as hit;
* Cell C6 is shot, because there is a ship there you can see it as H stands for Hit
* Cell B7 is shot, because there is no ship there you can see it as M stands for Miss
![board1](https://github.com/ma-sharifi/movieapi/assets/8404721/cf62f5c1-6930-4e4a-9daf-178f0f59e1cc)

#### Ship
It has the parent of all ships. The ship holds the information about the ship.
* direction: Represent the direction of the ship on board.
* shipType: Represent the type of ship and **create an instance of ship based on our type of ship**.
* coordinates: List of coordinates that represent where the ship is located on the board.
* isShot: I used this list to consider the status of one ship Coordinate. If opponent fired A1, it means ship on this coordination shot.
* isSunk: If all Coordinate list shots, it means this ship sunk. In the following picture, if you call this method, you will get true as result.
![board-ship-sunk](https://github.com/ma-sharifi/movieapi/assets/8404721/94d8c925-44cd-48a1-8d38-d9e27118e189)

#### Fleet
A fleet is a group of ships.
If all ships in the fleet sunk, it means the opponent is won.

#### LabelDto
It's a simple record Dto with one field to hold label of fire location.
```json
{"label": "A1"}
```

#### ShipDto
Is a record; it means it is immutable. It presents a ship just by 3 values of direction, type, and start Coordinate of it. 
Since we know the length and direction of the ship, we can calculate the other coordinates of a ship on board.
* String start: is a label such as A1 that represented a start coordinated of a ship.
```json
{
  "type": "AIRCRAFT_CARRIER",
  "direction": "HORIZONTAL",
  "start": "A1"
}
```

#### ShipType
Represent the type of ship and **create an instance of ship based on our type of ship**.
* Note the first character of type represented the type of ship on board.

#### Player
The player is the owner of his fleet and his board.
It holds the following fields:
* id: Player1 has id 1 and player 2 has id 2. I can easily find the opponent of player by this formula (playerId % 2) +1
* fleet: Each player has his own fleet.
* opponent: Each player knows who is his opponent.
* board: Each player has his own field that his ships need to be placed on.
* isFleetSunk: If all ships of the fleet are Sunk, it means this fleet has sunk as a result the opponent is won.
* nextAction: Used for controlling the flow of gameplay. It means placeFleet can't call before joinGame
  In the following picture, if you call this method, you will get true.

![board-fleet-on-board](https://github.com/ma-sharifi/movieapi/assets/8404721/75eff5c7-adb2-4558-a414-7662efcd77a8)

####  Cache<String, BattleshipGame> 
For the sake of multi-player can use different games, I save the game after creation into this cache.
The key is game id and value is the game already created by player1 or player2.
Player1 and player2 that have this game id can play this game.

### Cross-Cutting Concern
I used aspects to control some concerns.
For the sake of simplicity and readability, I extracted concerns from the methods and created an annotation for them to assign this concern to the relevant method.

#### PlayerTurnControllerNeeded
It handles the player's turn, and after successful action, it will change the turn.

#### GameIdNeeded
If a player does not present the game, id they can't play the game. A game will be represented by a game id. 
This aspect handles this part.
Because most of the methods need this validation, for the sake of simplicity, maintainability and SOLID principal (Single responsibility), I put it in aspect.

#### FlowControllerNeeded
It controls the flow of the gameplay. The player can't call fire before joining the game. 
The player just can fire his opponent after placing his fleet on board.

### For the following reasons, I added the version of dependencies in pom
1. *Reproducibility:* With specific versions defined, you ensure that anyone who works on the project or uses it can recreate the exact environment you intended, minimizing the chances of compatibility issues.
2. *Stability:* Dependency versions can change over time, and newer versions might introduce breaking changes. By pinning the versions, you prevent unexpected updates that could lead to bugs or compatibility problems.
3. *Security:* Known vulnerabilities might exist in certain versions of dependencies. Explicitly specifying versions enables you to quickly identify and address potential security issues.
4. *Maintenance:* Explicit version declarations make it easier to identify and update dependencies when necessary. This aids in keeping the project up to date and secure.

## Exception
Defined many of them to handle exceptional situations. Tried to explain why this exception happened with a useful message to how to solve this message.

### Example for ViolateOverlapException 
If we placed our fleed like the following code, "A1" is the start point of a ship:
```java
  LinkedList<ShipDto> fleet= new LinkedList<>();
  fleet.add(new ShipDto(ShipType.DESTROYER, Direction.HORIZONTAL, "A1"));  
  fleet.add(new ShipDto(ShipType.CRUISER, Direction.HORIZONTAL, "A1"));   
  fleet.add(new ShipDto(ShipType.SUBMARINE, Direction.HORIZONTAL, "A3"));  
  fleet.add(new ShipDto(ShipType.BATTLESHIP, Direction.HORIZONTAL, "C1"));
  fleet.add(new ShipDto(ShipType.AIRCRAFT_CARRIER, Direction.HORIZONTAL, "I1"));
```
For better understanding, I showed them on the picture as below:
![overlap-touch](https://github.com/ma-sharifi/movieapi/assets/8404721/e5288466-f73f-4685-9270-d87878d8e560)
The error message you will be faced is as follows:  
```
Ships overlapped violated! 3 coordinates overlapped!  
Ship 'DESTROYER' is HORIZONTAL ,overlapped coordinates found: [A1, A2]  
Ship 'CRUISER' is HORIZONTAL ,overlapped coordinates found: [A1, A2, A3]  
Ship 'SUBMARINE' is HORIZONTAL ,overlapped coordinates found: [A3]  
Check your fleet for overlapped ships
```

## API
APIs implemented are correlated with GameplayServices. It implemented the same method as the service.
All responses and request bodies are provided by JSON in order to client read and provide them uniformly. Results will be show in different fields.  
A player will extract from Basic authentication and will pass it to the service.   
I used POST because all these methods have to change the state of the game.  
* Note: For running bashes, you should use HTTPie.  

You can find the sequence diagram of fire method as follows.  
![Sequence Diagram](https://github.com/ma-sharifi/Battleship/assets/8404721/ee3ad9e1-e4db-47bb-b555-1f6d0b58e849)


### ErrorMessageDto
For response, we can use Zalando problems that implemented rfc7807 `Problem Details for HTTP APIs`. But for the sake of simplicity, I used a simple Dto.
* error_code: is a low-level error code. As a third party, you can override it and use it for internal custom messaging. On the other hand, you can change the message of error code 1 to every message you want.
* If you are using this API as a client app, you can show this message to your customer.
* Based on the API call, you will get a different field as a result.
* If the response is success, you will not get this object.

#### ErrorCode
ErrorCode is an enum that holds error codes, and which http status should return based on this error code.
If an unhandled error occurs it will return HTTP status 500.

### API explanation
>Root of our API is:  `/v1/battleship`

If you face an error, the response will be as follows, for example:
```bash
   http post :8080/v1/battleship
```
* If you don't provide an Authorization header, you will face the following response.
  HTTP HEADERS:
```
  HTTP/1.1 401
  Connection: keep-alive
  Content-Length: 70
  Content-Type: application/json
  Date: Thu, 17 Aug 2023 11:49:04 GMT
  Keep-Alive: timeout=60
```
Body:
```json
{
  "error_code": 15,
  "message": "Missing or invalid Authorization header!"
}
```

1.  The First step of game play is creating a game.
* **POST**`/`
  Request:
```bash
http POST :8080/v1/battleship 'Authorization:Basic cGxheWVyMTpwYXNzd29yZA=='  
```
The response will be:
```json
{
    "game_id": "CNG-24d11904-89e3-41a1-93f6-8f3b407d7085"
}
```

2. The second step is joining to the game:
* **POST**`/{game-id}/join`
  For joining player1 you must run the following command:
```bash
http POST :8080/v1/battleship/CNG-24d11904-89e3-41a1-93f6-8f3b407d7085/join 'Authorization:Basic cGxheWVyMTpwYXNzd29yZA=='
```
And for joining player 2 you must run the following command:
```bash
http POST :8080/v1/battleship/CNG-24d11904-89e3-41a1-93f6-8f3b407d7085/join 'Authorization:Basic cGxheWVyMjpwYXNzd29yZA=='
```

After successful join response header will be as follows, and the body is empty:
```
HTTP/1.1 200 
Connection: keep-alive
Content-Length: 0
Date: Thu, 17 Aug 2023 12:37:29 GMT
Keep-Alive: timeout=60
```

3. The third step is placing the fleet, request will be an array of shipDto.
* **POST**`/{game-id}/place`
*  start: is the first coordinate of the ship. Since we have the length of the ship from its Type, and direction of that, we can calculate the other coordinates of a ship if we know the start coordinate of a ship.
   For example below, our ship will be place on board from A1 ,A2 ,A3 because the length of CRUISER is 3, its direction is HORIZONTAL, and start point is A1.
* You should provide 5 different types of ships.
* Player1 request is as follows:
```bash
http POST :8080/v1/battleship/CNG-24d11904-89e3-41a1-93f6-8f3b407d7085/place 'Authorization:Basic cGxheWVyMTpwYXNzd29yZA=='  <<<'[{"type":"AIRCRAFT_CARRIER","direction":"HORIZONTAL","start":"A1"},{"type":"DESTROYER","direction":"HORIZONTAL","start":"B1"},{"type":"CRUISER","direction":"HORIZONTAL","start":"C1"},{"type":"BATTLESHIP","direction":"HORIZONTAL","start":"D1"},{"type":"SUBMARINE","direction":"HORIZONTAL","start":"E1"}]'
```
Player2 request:
```bash
http POST :8080/v1/battleship/CNG-24d11904-89e3-41a1-93f6-8f3b407d7085/place 'Authorization:Basic cGxheWVyMjpwYXNzd29yZA=='  <<<'[{"type":"AIRCRAFT_CARRIER","direction":"HORIZONTAL","start":"A1"},{"type":"DESTROYER","direction":"HORIZONTAL","start":"B1"},{"type":"CRUISER","direction":"HORIZONTAL","start":"C1"},{"type":"BATTLESHIP","direction":"HORIZONTAL","start":"D1"},{"type":"SUBMARINE","direction":"HORIZONTAL","start":"E1"}]'
```
The request body is like below:
```json
[
  {
    "type": "AIRCRAFT_CARRIER",
    "direction": "HORIZONTAL",
    "start": "A1"
  },
  {
    "type": "DESTROYER",
    "direction": "HORIZONTAL",
    "start": "B1"
  },
  {
    "type": "CRUISER",
    "direction": "HORIZONTAL",
    "start": "C1"
  },
  {
    "type": "BATTLESHIP",
    "direction": "HORIZONTAL",
    "start": "D1"
  },
  {
    "type": "SUBMARINE",
    "direction": "HORIZONTAL",
    "start": "E1"
  }
]
```
Response will be like follows without body:
```
HTTP/1.1 200 
Connection: keep-alive
Content-Length: 0
Date: Thu, 17 Aug 2023 12:45:20 GMT
Keep-Alive: timeout=60
```
3. The forth step is shooting the opponent's ship.
* **POST**`/{game-id}/fire`
* Request is a simple Application/JSON body such as : {"label":"A10"}
* Response has a 'result' field, it holds the result of fire. As gameplay implies, it can be Hit, Miss, Sunk, and Winner is: Player1/2:   
  Request Body:
```json
{"label": "A1"}
```

```bash
http POST http://localhost:8080/v1/battleship/CNG-24d11904-89e3-41a1-93f6-8f3b407d7085/fire 'Authorization:Basic cGxheWVyMjpwYXNzd29yZA==' <<< '{"label": "A1"}'\
 Content-Type:'application/json'
```

Http header response:
```
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/json
Date: Thu, 17 Aug 2023 13:37:13 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked
```
Response body will be:
```json 
   {
   "result": "Hit"
   }
``` 

For Errors, header is as follows:
```
HTTP/1.1 400
Connection: close
Content-Type: application/json
Date: Thu, 17 Aug 2023 12:49:06 GMT
Transfer-Encoding: chunked
```
Body
```json
{
"error_code": 16,
"message": "Expected method call is: fire,and real method called is: placeFleet"
}
```

## Swagger
You also can find the swagger ui in the following link:
>http://localhost:8080/swagger-ui/index.html

## Security
For the sake of simplicity, I did not use Spring security for security, I just used a simple filter for it.
AuthenticationFilter filter, extract the player id and add it to the request;
If a user does not provide an authorization header he will get the following error.
* Note: for the purpose of test:
>username is player1 or player2, and password is password

```json
{
    "message": "Missing or invalid Authorization header!",
    "error_code": 22
}
```

### Test Coverage
* 100% Class
* 97% Method
* 97% Line

### SonarLint
All issues are solved.

## How to improve
* Use Redis instead of Caffeine.
* Use a database for saving data.
* Design and implement a beautiful UI for it
* Use Keycloak or Spring security for security instead of a simple filter I implemented.
* Use a way to notify opponent about gameID and his turn, like push notification, SMS, Kafka or Redis,...
