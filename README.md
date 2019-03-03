![alt text](https://cdn.discordapp.com/attachments/551684392087584768/551689978606780427/hero.png)

BigBrother 1.0
By: Sean Valley (jakeob22)
For: Spigot 1.13.2

This plugin is designed to log every broken/placed block into a MySQL database. Operators and/or people with permission are then able to check the history of changes for any location, player, or time. They can also rollback any unwanted block changes. It is meant to have servers allow their players complete freedom over where they build without having to designate locations for each player or use a town management plugin to protect players builds. The plugin is still in development and I am open to all suggestions for features or bug fixes! Just email me at valleydsean@gmail.com!

Permissions:

&nbsp;&nbsp;&nbsp;&nbsp;'bb.use' - Allows players to use Big Brother commands, use the log tool, and issue rollbacks

Commands:

  &nbsp;&nbsp;&nbsp;&nbsp;/bb help - Shows the help menu
  
  &nbsp;&nbsp;&nbsp;&nbsp;/bb log - Gives player a log tool
  
  &nbsp;&nbsp;&nbsp;&nbsp;/bb undo - Undoes most recent rollback
  
  &nbsp;&nbsp;&nbsp;&nbsp;/bb rollback - Rolls back specified changes
  
  &nbsp;&nbsp;&nbsp;&nbsp;/bb history - Shows block modification history
  
  &nbsp;&nbsp;&nbsp;&nbsp;/bb page [#] - Shows different pages of history results
  
  
How to use Rollback and History commands:

&nbsp;&nbsp;These commands take special arguments to find exactly what you're looking for. You must specify at least one of the arguments to get results but you can use a combination of 2 or even all 3. History and Rollback take the same argument and can be used interchangably.
  
  &nbsp;&nbsp;&nbsp;&nbsp;p:<playername> - Will give you the history of a specified player
  
  &nbsp;&nbsp;&nbsp;&nbsp;r:<radius> - Will give you the history of blocks within a certain radius
  
  &nbsp;&nbsp;&nbsp;&nbsp;t:<time> - Will give you the history of blocks within a certain time
  
  &nbsp;&nbsp;Examples:
  
  &nbsp;&nbsp;&nbsp;&nbsp;/bb history p:jakeob22
  
  &nbsp;&nbsp;&nbsp;&nbsp;This will show all of the blocks ever changed by the player "jakeob22"
  
  &nbsp;&nbsp;&nbsp;&nbsp;/bb history p:jakeob22 r:100 t:4d3h2m1s
  
  &nbsp;&nbsp;&nbsp;&nbsp;This will show all of the blocks changed by the player "jakeob22" within 100 blocks after 4 days, 3 hours, 2 mins, 1 sec ago
  
  &nbsp;&nbsp;&nbsp;&nbsp;/bb rollback t:1h
  
  &nbsp;&nbsp;&nbsp;&nbsp;This will rollback blocks within after an hour ago
