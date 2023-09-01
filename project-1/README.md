# My Specification

## Class
### 1-ShadowDance
#### variables:
1. `flag`: judges the stage of the running game for choosing the proper displayed interface.
2. `notes`: the first parameter in csv file. It judges `Lane` or `Note`.
3. `actions`: the second parameter in csv file. For note, it judges `Normal` or `Hold`. For Lane, it judges direction.
4. `noteX`: the third parameter in csv file. the frame when the note appears
5. `coordinate`: the coordinate of each note

### 2-Lane
A abstract of `4` Lanes, involving Right, Left, Down and Up.
#### variables
