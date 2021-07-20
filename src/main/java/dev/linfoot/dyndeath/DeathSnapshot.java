package dev.linfoot.dyndeath;

import org.bukkit.Location;
import org.dynmap.markers.Marker;

import java.util.UUID;

class DeathSnapshot {
    private final UUID player;
    private final long timestamp;
    private final Location location;
    private final Marker marker;

    DeathSnapshot(UUID player, long timestamp, Location location, Marker marker) {
        this.player = player;
        this.timestamp = timestamp;
        this.location = location;
        this.marker = marker;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Marker getMarker() {
        return marker;
    }
}
