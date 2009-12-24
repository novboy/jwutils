package jw.jzbot.fact.functions;

import jw.jzbot.JZBot;
import jw.jzbot.fact.ArgumentList;
import jw.jzbot.fact.FactContext;
import jw.jzbot.fact.Function;
import jw.jzbot.fact.Sink;

public class TempjoinFunction extends Function
{
    
    @Override
    public void evaluate(Sink sink, ArgumentList arguments, FactContext context)
    {
        context.incrementMessageCount();
        JZBot.bot.joinChannel(arguments.resolveString(0));
    }
    
    @Override
    public String getHelp(String topic)
    {
        return "Syntax: {{tempjoin||<channel>}} -- Causes the bot to join the specified "
                + "channel, but without setting this channel to auto-join or creating "
                + "a factoid database for the channel.";
    }
    
}