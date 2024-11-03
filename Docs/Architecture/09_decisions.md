Fundamental decisions and why they have been made.

# Server to Client Communication without client requests

For some features (dice, chats, and others) the server actively sends data to the clients. Just responding a request
from a client fails here.

## Options at hand

### Server-sent Events (SSE)

- Backend code very similar to request-responding REST endpoints
- Only unidirectional data flow from server to client
- Limited connections per client (six when not using HTTP2)

### WebSockets

- Needs slightly more backend coding
- Bidirectional communication between Server and Client
- Widely supported by browsers

### WebSocketStream

- Similar to WebSockets, but Promise based
- Can handle backpressure
- Currently (Nov 2024) not supported by all browsers

### WebTransport

- Bidirectional communication between Server and Client
- Currently (Nov 2024) not supported by all browsers

## Expected situatons

- Participants may use any browser => wide browser support required
- Data transmission not permanent, just every now and then (intervals typically order of a seconds or longer) => no
  backpressure expected

## Decisions

### Waiting page

SSEs

- Client expects just one single answer: can the user join the game or not
- The only request the user may make to the server, aborting the joining process, can easily be sent to a standard
  REST endpoint

### Game page

WebSockets

- Data flows forth and back between user and sever