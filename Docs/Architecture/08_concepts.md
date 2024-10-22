This document lists the concepts applied to ScibeVTTRPG.

# Security

## In game

### Checks if you are eligible for ,aking http requests

- http requests are sent to the api endpoint that is named by the roomname
- User identity is *not* part of the request
- The user's http session on the server has a mapping *roomname to Participant object*
- It is checked, if there is a Participation object with the roomname as the key stored in the session
- If yes, it is checked if the Game object of the *room* also lists the same Participant object
- If yes, the request is processed further, with user identity and participant role taken from the Participation object
- If a check fails, the request is declined
