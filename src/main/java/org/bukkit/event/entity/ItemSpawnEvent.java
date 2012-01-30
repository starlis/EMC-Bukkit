package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an item is spawned into a world
 */
@SuppressWarnings("serial")
public class ItemSpawnEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Location location;
    private boolean canceled;

    public ItemSpawnEvent(final Entity spawnee, final Location loc) {
        super(spawnee);
        this.location = loc;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the location at which the item is spawning.
     *
     * @return The location at which the item is spawning
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
