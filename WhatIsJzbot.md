> _Most of this stuff has moved to **JZBot's new project**, located [here](http://jzbot.googlecode.com)._

For information on the JZBot run by javawizard (the official JZBot), see http://marlenjackson.opengroove.org.

JZBot is an IRC bot. It has functionality for sending a message to a channel when a user joins that channel, sending a pm to users when a person joins a channel, reporting the weather, and storing factoids. Factoids are set in a somewhat-unconventional manner for IRC bots. In infobot, you might do something like this:

~testfact is Hello there.

Then you could do

~testfact

and the bot would say "Hello there."  JZBot does this slightly differently. You instead do this:

~factoid create testfact Hello there.

For some technical documentation, see http://docs.google.com/Doc?id=dgm4m9gz_1d7jg3s85.

Factoids currently can only be created by an op or a superop. In the future, I'll add commands for allowing channel ops to set the bot to allow factoids to be created by any member of the channel.

Bot ops are not the same as IRC ops. Ops are set by superops when a bot is invited to a channel. Ops can add and remove other ops. Superops are in change of the entire robot, and there will typically be very few of them. For example, jcp's JZBot (which uses the nicknames jzbot and Marlen\_Jackson on irc.freenode.net) has only two superops, jcp and Anthony\_Luth.

JZBot authenticates users (ops and superops) by hostname. It's therefore recommended to get a hostmask, so that you can administer the bot from any location.

JZBot also supports writing commands in the logo programming language. You do this by running "~proc create PROCNAME". JZBot then sends you back a pastebin url, which contains a stub logo proc. You edit this and save your changes as a new paste, then run "~proc save http://pastebin.com/...", specifying the url of the new paste. This saves the procedure. Procedures can currently only be edited by superops and people granted access to the procedure, which is basically the person that created the procedure.

JZBot is written by javawizard2539 (aka jcp on irc.freenode.net).