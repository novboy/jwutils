package org.opengroove.jzbot.plugins;

import java.net.URI;
import java.util.ArrayList;

public class CommandInvocationContext
{
    /**
     * Messages which are to be sent to the source of the command invocation.
     * These are the replies to the command. If a command is called in single
     * capture mode, then only one message can be inserted here, or the call
     * will throw an exception when it returns.
     */
    private ArrayList<Message> toSourceMessages = new ArrayList<Message>();
    /**
     * Messages which are "other" messages for this command. These are messages
     * that are not considered a reply to the command, and are typically not
     * sent to the message sender. For example, when "~roulette show" is run,
     * the roulette command adds the
     * "so-and-so has seen which chamber is loaded" message as a to-source
     * message, and "chamber X is loaded" as an other message directly to the
     * user.
     * 
     * Capture mode cannot usually capture these messages. Instead, they are
     * added to the parent command invocation context's other message list when
     * a subcommand returns. In contrast, to-source messages can be captured.
     */
    private ArrayList<TargetedMessage> otherMessages = new ArrayList<TargetedMessage>();
    /**
     * True if this context is finished, false if it is not. A context passed
     * into a command will never be finished; contexts are only finished by
     * jzbot itself. Most methods throw IllegalStateExceptions if they are
     * called when the context has been finished.<br/>
     * <br/>
     * 
     * A context enters into the finished state when the finish method is called
     * on it. This method essentially forwards all messages to the parent
     * context if this context is not in capture mode.
     */
    private boolean finished;
    /**
     * The parent command invocation context. If this invocation context is not
     * in capture mode, then calling finish() will cause to-source messages and
     * other messages to be added to the parent context. If this invocation
     * context is in capture mode, then calling finish() will cause only other
     * messages to be added to the parent context.
     */
    private CommandInvocationContext parent;
    /**
     * True if this context is in capture mode. In capture mode, to-source
     * messages are not sent to the parent context after the command runs, but
     * are instead captured and made available to the command invoker. Other
     * messages are still sent to the parent.
     */
    private boolean captureMode;
    
    /**
     * Creates a command invocation context that is a child of the specified
     * context.
     * 
     * @param parent
     *            The parent context
     * @param captureMode
     *            True if this context is to be created in {@link #captureMode},
     *            false if it is to be created in normal mode
     */
    public CommandInvocationContext(CommandInvocationContext parent, boolean captureMode)
    {
        this.parent = parent;
        this.captureMode = captureMode;
    }
    
    /**
     * Creates a context without a parent. This is what is called by JZBot for
     * commands that are called as a result of someone directly sending the
     * command in a message. Contexts without a parent are never created in
     * capture mode.
     */
    public CommandInvocationContext()
    {
        this.parent = null;// redundant
        captureMode = false;// also redundant
    }
    
    public void sendToSource(boolean action, String message)
    {
        sendToSource(new Message(action, message));
    }
    
    public void sendToSource(String message)
    {
        sendToSource(false, message);
    }
    
    public void sendToSource(Message message)
    {
        
    }
    
    public void sendOther(URI target, boolean action, String message)
    {
        
    }
}
