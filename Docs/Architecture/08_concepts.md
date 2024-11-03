This document lists the concepts applied to ScibeVTTRPG.

# Persistence

# Security

## In game

### Checks if you are eligible for making http requests

- http requests are sent to the api endpoint that is named by the roomname
- User identity is *not* part of the request
- The user's http session on the server has a mapping *roomname to Participant object*
- It is checked, if there is a Participation object with the roomname as the key stored in the session
- If yes, it is checked if the Game object of the *room* also lists the same Participant object
- If yes, the request is processed further, with user identity and participant role taken from the Participation object
- If a check fails, the request is declined

## User groups

### Anonymous User

- can see all openly accessible webpages
- can join games without loggin in, and even without having an account
- /!\ WARNING: Users may freely edit texts that belong to *registered users* and are persisted.

### Registered User

- can everything do an *registered user* can do
- can access login-proteced webpages for creating and editing game materials, creating campaigns, assigning game
  materials to camapigns, and open game rooms for *game sessions*
- Game materials are owned by the *registered user* who created them. Outside games, the materials cannot be seen or
  edited by any other, no matter of which user group. Within games, a *registered user* may grant other *participants*
  the right to use, see and even edit certain owned game materials.
- Can permanently delete the own account if not also an *admin*, rendering a log in impossible
  and deleting all game materials owned by the deleted user

### Admins

- is also a *registered user*
- Can access web pages where all *registered users* are listed
- Can set the membership to the group *Admin* for any *registered user*. Constraint: At least one *Admin* must exists
- Can temporarly block accounts of *registered users* that are not also *admins*, rendering a lock in impossible until
  a certain date
- Can permanently delete accounts of any *registered users* that are not also *admins*, rendering a log in impossible
  and deleting all game materials owned by the deleted user
