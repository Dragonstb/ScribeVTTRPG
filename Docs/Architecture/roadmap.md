# Joining games

### Participant browsing to a *session*

- just has to enter the URL of the *room* (like www.example.com/game/whatever-room-name-the-gm-set).
- gets a join screen (if URL is correct) that asks for the person's name and, only if set, the password of the *session*.
- hitting the "join" button forwards the *participant* to a "please wait for GM" screen.
- *participant*'s name is added to the list of waiting players in the GM's main menu
- entering an invalid *room* name or a nonexisting *room* name redirects the failed *participant* to a page stating that
  the room is either "404 not found" or "410 gone".

#### Browsing a room without password protection

As a joining participant, who is browsing to a room and has not entered yet, I just want to enter the URL of the room.
This should initialize the process of joining.

**Priority class:** in progress

**DoD:**
- when entering a valid URL of a room
  - A welcome page is shown to me where I can enter my name.
  - For the name, a minimum length, a maximum length, and a set of allowed characters is defined.
  - The welcome page has a submit button which can only be pressed when the name fullfills the criteria mentioned above.
  - The welcome page displays the constraints for the name in an accessible way.
  - Clicking the submit button sends a request containing the name. This request initiates the joining process. I am
    forwarded to a wait page (see other user story).
- when browsing to an invalid room URL
  - I get serve a webpage which states that the requested room does not exists or has been closed again already.

#### Waiting for being let in into a room

As a joining participant, who is browsing to a room and has not entered yet but submitted my name, I want to see that I
successfully made my request to enter the room and am now waiting for the request to be processed. So I know that
pressing that submit button on the welcome page (see related user story) has the desired effect.

**Priority class:** in progress

**DoD:**
- A waiting page is displayed to me. This page greets me, mentions the room name somewhere. The page states that the
room creator has been informed about my request and that I should wait.
- The page has a button for cancelling the process of joining from my side.
- Hitting this cancel button sents the cancel request to the server and forwards me to a page without interactive elements
  that confirms my cancellation.

#### Rebrowsing to / reloading a game page when already in the room

As a participant who has already joined a *room*, I want to see the game page immediately when reloading the page or
browsing to it again. I don't want to go through all this joining again.

**Priority class:** imminent

**DoD:**
- I get the game page served immediately, without all this joining stuff.





### GM decides on waiting *paticipants*

- in his/her main menu of the game screen, GM has a list of potential *participants* that have requested to join the *room*.
- for each waiting potential *participant*, GM can choose one of three options: let join as *player*, let join as
  *spectator*, decline
- if accepted, the *participant* is automatically forwarded to the game screen in her/his assigned role. All handouts
  are "hidden"
- if declined, the failed *participant* is forwarded to a "sorry" page

#### Being notified by requests for joining

As a room creator, I want the requestion person to appear in my requests-for-joining-list. From there, I cn decide how to
deal with them

**Priority class:** imminent

**DoD:**
- When a new request comes in, the name the potential participant has entered appears in my requests-for-joining-list in
  the main menu of my game page.
- For each name in the list, I have a submenu with options "let in as player", "let in as spectator", and "decline
  request" (see other user story what happens when choosing one of them).
- If a user cancels her/his request for joing from user side before I made a decision, the name disappears from my list
  and I am informed.

#### Deciding on requests for joining

As a room creator, I either want to let a new *participant* into the room as either *player* or as *spectator*, or
decline the request and don't let the person into the room. This way, I can control who enters and in which role.

**Priority class:** imminient

**DoD:**
- My decission is linked to the correct user who made a request for joining
- If I let the new *participant* in, and the join request has not been canceled by the user so far
  - This new *participant* is added to the running game.
  - The new *participant's* browser is notified. The user automatically becomes forwarded to the game screen.
- If I decline the request for joining, and the join request has not been canceled by the user so far
  - The user's browser is notified. The user automatically becomes forwarded to a rejection screen without interactive
    elements.
- If the user has canceled his/her request for joining by himself/herself in the meantime:
  - I am informed about this.

# Handouts

### rearrange handouts

- buttons "up" and "down" can move a handout item up and down within its container.
- *players* can always rearrange the handouts they can see.

### add handout item

- a button "add" at the end of each containr allows for adding a new handout item to the container.
- GM can always add handout items in any container.
- items added by GM can be seen only by the GM himself/herself.
- items added by GM can only be edited by the GM
- *players* can add handout items to any container they can see.
- an item added by a *player* can only be seen and edited by the *player* herself/himself and by the GM.
- any item added is also added to the GM's general material database, and it is added as content of the current campaign.

### Modifying read/write rights

- for each handout item, the GM has an option menu for setting the access rights for each *player* individually, and for
  the group of *spectators*.
- for each player, the menu contains a line with the name and a button group of three radio buttons, corresponding to
  "hidden", "visible", and "editable". Exactly one of these three radio buttons is activated at any time.
- a last line is for the *spectators*, with only the two radio buttons "hidden" and "visible"

# Dice

### Make your own dice

### *Participant* self-colorizes the dice she/he rolls

### *Participant* self-configures the dice he/she rolls when hitting the "roll" button

### *Participant* self-configures the boni/mali added to rolls

### Live display of all open rolls in all browsers

### Engine writes results of each open roll to the public chat

### Open rolls and hidden rolls

### Rolls shared only to a subset of the *participants*

# Playing together

### Someone else than the creator of the *room* is the GM

**Priority class:** something for the far future

- The *room* creator is not assigned as GM automatically anylonger
- The *room* creator may choose to join the *room* as *player* or *spectator*
- The *room* creator remains responsible for letting potential *participants* into the *room*
- a joining *participant* may be assigned as GM

### Multiple GMs

**Priority class:** something for the far future

- a joining *participant* may be assigned as GM

# Technical stuff

- Adding an alive flag for an instance of game. Currently, join requests may be made during the creation of the game. If
  the room creator's websocket connection has yet not been established by then, the prospect participant will wait for
  ever: the room creator never got the signal. Game may be set alive once the instance has been created, teh room
  creator is in the room, and the room creators UI and WebSocket connection is ready. Requests from others shall be
  rejected as long as the game is not alive. Access to alive flag is synced.
- Extending alive flag by a timestamp of last action. A sheduled task may check all rooms regularly for this time stamp
  and auto-close wasted rooms after settign the alive flag to false again.
- Transition to Java 23
- Update dependencies in general
- Redesign the way how nested handout pieces within containers are addressed by their ID. Currently, the ID of the
  parents and the piece's ID are simply concatenated. But now think of a container "jan" with a child "edoe", alongside
  a second container "jane" with a child "doe". Addressing the child "janedoe" would not be unique. So we need a
  separator character between the IDs of the generations. Thsi character would become illegal for IDs
- User management and user login
- Have you heard of this logging thingy? :)
- Attach to a code analysis platform like Sonarqube
- Attach to an accessibility check system like pa11y


# Explanations

## Priority categories

Rough categorization when features are tackled, ordered from "now" to the "in the far, far future".

1. in progress
2. imminent
3. soon
4. near future
5. somewhat later
6. something for the far future