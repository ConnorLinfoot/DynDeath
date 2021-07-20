package dev.linfoot.dyndeath;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class DeathListener implements Listener, Runnable {
    private final DynDeath dynDeath;
    private final List<DeathSnapshot> deathSnapshots = new ArrayList<>();
    private MarkerSet set;

    public DeathListener(DynDeath dynDeath) {
        this.dynDeath = dynDeath;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        DynmapAPI dynmap = dynDeath.getDynmap();
        if (dynmap == null) {
            // Shouldn't happen since we wouldn't be registered but make sure
            return;
        }

        if (set == null) {
            set = dynmap.getMarkerAPI().createMarkerSet("linfoot.dyndeath", "DynDeath", dynmap.getMarkerAPI().getMarkerIcons(), false);
        }

        // Unique key for the marker
        String uniqueKey = "death." + event.getEntity().getUniqueId() + "." + System.currentTimeMillis();
        Location location = event.getEntity().getLocation();

        Marker marker = set.createMarker(uniqueKey, event.getDeathMessage(), location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), dynmap.getMarkerAPI().getMarkerIcon("skull"), false);
        deathSnapshots.add(new DeathSnapshot(event.getEntity().getUniqueId(), System.currentTimeMillis(), location, marker));
    }

    @Override
    public void run() {
        if (deathSnapshots.isEmpty()) {
            return;
        }

        long now = System.currentTimeMillis();

        deathSnapshots.removeIf(deathSnapshot -> {
            if (deathSnapshot.getTimestamp() + TimeUnit.MINUTES.toMillis(dynDeath.getDuration()) < now) {
                deathSnapshot.getMarker().deleteMarker();
                return true;
            }
            return false;
        });
    }
}
