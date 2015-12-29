package me.xgh69.pvptoggle.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PvpToggleDebugEvent extends Event implements Cancellable
{
	private static final HandlerList handlerList = new HandlerList();
	private boolean cancel = false;
	private String permission = "pvptoggle.admin";
	private String message;
	
	public PvpToggleDebugEvent(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String s)
	{
		message = s;
	}
	
	public void setPermission(String s)
	{
		permission = s;
	}
	
	public String getPermission()
	{
		return permission;
	}
	
	@Override
	public boolean isCancelled()
	{
		return cancel;
	}

	@Override
	public void setCancelled(boolean b)
	{
		cancel = b;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return handlerList;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlerList;
	}
}
