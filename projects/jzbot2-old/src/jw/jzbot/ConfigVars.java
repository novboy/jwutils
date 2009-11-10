package jw.jzbot;

import java.nio.charset.Charset;

import jw.jzbot.storage.MapEntry;
import jw.jzbot.utils.Pastebin;
import jw.jzbot.utils.Pastebin.Duration;

import net.sf.opengroove.common.utils.StringUtils;


public enum ConfigVars
{
    delay(
            "1000",
            "This config variable sets how often marlen can send messages, in milliseconds. "
                    + "For example, if this is 1000 (the default), then marlen will send "
                    + "no more than 1 "
                    + "message per second, and will buffer messages so that they aren't sent more "
                    + "often than that.")
    {
        public void set(String value)
        {
            int i = Integer.parseInt(value);
            JZBot.bot.setMessageDelay(i);
            super.set(value);
        }
    },
    evalengine("jeval", "")
    {
        public String getDescription()
        {
            return "See \"~help functions eval\" for information. When {{eval}} is "
                    + "used without an engine specified, whatever engine is set in this "
                    + "config variable is the engine that will be used. For example, if "
                    + "this config variable is \"jeval\", then running {{eval||5+3}} "
                    + "will function the same as {{eval||jeval||5+3}}.";
        }
        
    },
    charset(Charset.defaultCharset().name(), "")
    {
        public String getDescription()
        {
            return "This config variable is the charset used to read and write characters "
                    + "from and to the IRC server. Available charsets are, separated "
                    + "by spaces: http://pastebin.com/"
                    + Pastebin.createPost("jzbot", StringUtils.delimited(Charset
                            .availableCharsets().keySet().toArray(new String[0]), "   "),
                            Duration.DAY, null);
        }
        
        public void set(String value)
        {
            JZBot.setCurrentCharset(value);
            super.set(value);
        }
    },
    keys("", "A pipe-separated list of hashes (as obtained from the {{hash}} function). "
            + "If a user runs \"~op key <text>\", and then has of <text> is equal to " + ""), notfound(
            "one of the hashes in this list, the user will be made a superop. Note that "
                    + "keys cannot contain spaces.",
            "This config variable is the name of a factoid to run when users "
                    + "send a message that isn't recognized. If this is blank, then the text \""
                    + "Huh? (pm \"help\" for more info)\" will be sent instead."), primary(
            "",
            "This config variable is the name of a channel that error messages "
                    + "will be sent to when there's not a logical channel to send them to. For"
                    + " example, if the global _onready factoid has a syntax error, the message will "
                    + "be sent to the channel specified in this config variable. Leaving this empty "
                    + "will cause such errors to simply be ignored."), chanops(
            "0",
            "This config variable specifies whether channel operators are treated as "
                    + "bot ops. 1 means they are, 0 means they are not. Channel operators at "
                    + "the bot's primary channel are also treated as bot superops.")
    {
        public void set(String value)
        {
            if (!(value.equals("0") || value.equals("1")))
                throw new ResponseException(
                        "Invalid value; must be 0 or 1, see \"~config chanops\" for help");
            super.set(value);
        }
    },
    openstatus("1", "This config variable specifies whether everyone can run \"~status\". "
            + "If this is 1, then everyone can. If this is 0, only superops can.")
    {
        public void set(String value)
        {
            if (!(value.equals("0") || value.equals("1")))
                throw new ResponseException(
                        "Invalid value; must be 0 or 1, see \"~config openstatus\" for help");
            super.set(value);
        }
    },
    servicemsg("0", "If this is 1, the bot will authenticate to NickServ by messaging "
            + "it directly. If this is 0, the bot will authenticate to NickServ by using "
            + "the IRC \"NICKSERV\" command.")
    {
        public void set(String value)
        {
            if (!(value.equals("0") || value.equals("1")))
                throw new ResponseException(
                        "Invalid value; must be 0 or 1, see \"~config servicemsg\" for help");
            super.set(value);
        }
    },
    logsize("0", "This config variable is the maximum size, in bytes, of the logs to "
            + "keep for each channel on a per-channel basis. Use the {{logs}} function "
            + "to actually read these logs. 0 disables logging. This doesn't take effect "
            + "until the bot is reconnected."), nolog("",
            "This config variable is a pipe-separated list of channels that should "
                    + "not be logged, even if the logsize variable is set to a "
                    + "non-zero value. This doesn't take effect until the bot "
                    + "is reconnected."), modes("",
            "This config variable is a list of user modes that the bot "
                    + "should set on itself whenever it connects. \"+\" and \"-\" "
                    + "characters should not be included."), helpinpm(
            "0",
            "If this is 0 (the default), then ~help can be run in channels. "
                    + "If this is 1, then help can only be run in a pm to prevent needless "
                    + "spamming of channels.")
    {
        public void set(String value)
        {
            if (!(value.equals("0") || value.equals("1")))
                throw new ResponseException(
                        "Invalid value; must be 0 or 1, see \"~config helpinpm\" for help");
            super.set(value);
        }
    };
    private String defaultValue;
    private String description;
    
    private ConfigVars(String defaultValue, String description)
    {
        this.defaultValue = defaultValue;
        this.description = description;
    }
    
    public String get()
    {
        MapEntry entry = JZBot.storage.getConfigVar(name());
        if (entry == null)
            return defaultValue;
        return entry.getValue();
    }
    
    public void set(String value)
    {
        MapEntry entry = JZBot.storage.getConfigVar(name());
        if (entry == null)
        {
            entry = JZBot.storage.createMapEntry();
            JZBot.storage.getConfigVars().add(entry);
            entry.setKey(name());
        }
        entry.setValue(value);
    }
    
    public String getDescription()
    {
        return description;
    }
}