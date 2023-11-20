Fundamental decisions and why they have been made.

# Code structure

## DefaultPartcipants in package game reference the lists of handouts in package handouts directly

### Situational backgound

- Participants can only see only a subset of all handouts around.
- The subset of accessible handouts can be different from participant to participant.
- Participants may have the right to edit a handout they can see.
- The handouts a participant can see are read on http GET requests. The data is collected and sent to the browser for
 rendering.

### What we did

- Each Participant references a list of handouts she/he can see.
- The reading access to a certain handout is already defined by this list: handout is in the list (i.e. access granted)
 or it is not (i.e. access denied).

Pros:
- Faster read/write access to handouts due to direct reference.

Cons:
- The modularity of the package "handouts" is somewhat pierced. The service offered by "handouts" is mainly exposed via
the interface "HandoutManager". Now, the lists of handouts also become part of the public API.

### Alternative

- Adding a field to AbstractHandoutPiece for recording who can access the handout.
- Adding methods to interface "HandoutManager" for reading from and writing to handouts. Identifier for participants
would be arguments of these methods.

Pros:
- Clearer structure of the code.

Cons:
- More method calls which likely would cause a drop in the performance.

### Decision

Pro performance. If, in the end, it should turn out that the access is performant enough on an average machine, this
decision might be reviewed.