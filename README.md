# Java-Go
A Springboot application for the game Go.

# Description
This is a Springboot application which runs a server-side Go engine. This project was started out of the desire to write a software which enforced the rules of go.
A goal was to eventually extend this to be a multiplayer platform where users could play each other in real-time, but this has since been put on hold.
Currently, the application exposes an API which allows users to create Go games and advance the states of these games. 
The endpoints are as follows: 

## `GET /api/v1/game`

Accepts: Nothing

Returns: 
All existing GoGame objects

## `POST /api/v1/game`

Accepts: Nothing

Returns: A new GoGame object

## `PUT /api/v1/game`

Accepts: A GameMoveRequest object with the following payload format

```json
{
  "gameId": "SomeGameId",
  "gameMove": {
    "moveType": "PLACE",
    "player": "BLACK",
    "position": {
      "x": 18,
      "y": 18
    }
  }
}
```
- gameId: String representing the gameId of the GoGame object to be updated
- moveType: String which can take values either "PLACE" or "PASS"
- player: String which can take values either "BLACK" or "WHITE"
- x: Integer corresponding to x coordinate of piece to be placed
- y: Integer corresponding to y coordinate of piece to be placed

Returns: A GameMoveResponse object with the following payload format

```json
{
  "gameId": "SomeGameId",
  "gameMoveResponseType": "SUCCESSFUL",
  "details": null
}
```
- gameId: String representing the gameId of the GoGame object to be updated
- gameMoveResponseType: String which can take te values either "SUCCESSFUL" or "FAILED"
- details: String containing an error message if the move request failed and null otherwise

## `DELETE /api/v1/game/{gameId}`

Accepts: Nothing

Returns: String containing either success message or failure message if the gameId did not exist

# Limitations and Reflections
It import to reflect on the limitations of this project and identify what needs to be implemented 
in order for this application to be useful.

This application suffers from a usability perspective. There is currently no front-end client built for it because HTTP requests is not 
an ideal way for a multiplayer platform game to be implemented. With the current implmementation, users are forced to compete against one another on the same client because
each HTTP request for a game state modification is provided a single response. This means that real-time competition from different clients is not possible without some complex request forwarding.
In order to improve this, the API layer would need to be replaced by a websocket layer which services incoming client connections and transmits 
the progress of the game to both players at the same time. 

In addition to implementing the websocket message mappings, we would also need to an external subscription/publisher system and implement a match-making system.
The subscription/publisher system required in order to keep the application stateless, which in turn allows the application to scale horizontally by duplicating server instances and placing them behind a load balancer. The subscriber/publisher system needs to be external from the server because a server can only talk to a client which is currently connected to it. In a horizontally scaled system, two clients playing the same game may be connected to different servers.  

Finally, the scoring algorithm needs to be implemented. This is not a trivial task, as the Japanese scoring system has a non-trivial implementation for board states which are not in completed state (All territory has been fully encapsulated by one of the two players).
Scoring is mandatory to determining the winner. If two players are competiting, it is a requirement for the application to produce a winner. Implementing this algorithm is a matter of research and testing.

Overall, this project began as proof of concept for building a Go engine but scope eventually increased to a multiplayer game platform. I currently do not have time to keep up with this scope creep so will be putting the project on hold for now. Thank you for reading :)
