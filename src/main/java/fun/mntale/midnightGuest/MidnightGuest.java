package fun.mntale.midnightGuest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Sound;

public final class MidnightGuest extends JavaPlugin implements Listener {

    private static final String VERIFY_URL = "https://mntale.fun/wiki/general/how-to-verify";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private boolean isGuest(Player player) {
        return !player.hasPermission("visitor.bypass");
    }

    private boolean isGuest(Entity entity) {
        return entity instanceof Player && isGuest((Player) entity);
    }

    private void notifyGuest(Player player) {
        Component msg = Component.text()
            .content("คุณไม่สามารถทำอะไรได้จนกว่าจะเชื่อมบัณชีที่ ")
            .color(NamedTextColor.RED)
            .append(
                Component.text(VERIFY_URL)
                    .color(NamedTextColor.AQUA)
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.openUrl(VERIFY_URL))
            )
            .build();
        player.sendMessage(msg);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.4f, 1.2f);
    }

    // === BLOCK INTERACTIONS ===
    @EventHandler(priority = EventPriority.HIGHEST) public void onBreak(BlockBreakEvent e) {
        if (isGuest(e.getPlayer())) {
            e.setCancelled(true);
            notifyGuest(e.getPlayer());
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST) public void onPlace(BlockPlaceEvent e) {
        if (isGuest(e.getPlayer())) {
            e.setCancelled(true);
            notifyGuest(e.getPlayer());
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST) public void onInteract(PlayerInteractEvent e) {
        if (isGuest(e.getPlayer())) {
            e.setCancelled(true);
            notifyGuest(e.getPlayer());
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST) public void onUseEntity(PlayerInteractEntityEvent e) {
        if (isGuest(e.getPlayer())) {
            e.setCancelled(true);
            notifyGuest(e.getPlayer());
        }
    }

    // === INVENTORY & ITEMS ===
    @EventHandler(priority = EventPriority.HIGHEST) public void onDrop(PlayerDropItemEvent e) { if (isGuest(e.getPlayer())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onPickup(PlayerAttemptPickupItemEvent e) { if (isGuest(e.getPlayer())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onConsume(PlayerItemConsumeEvent e) { if (isGuest(e.getPlayer())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onEditBook(PlayerEditBookEvent e) { if (isGuest(e.getPlayer())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onCraft(CraftItemEvent e) { if (isGuest(e.getWhoClicked())) e.setCancelled(true); }

    // === ENTITY & MOB INTERACTIONS ===
    @EventHandler(priority = EventPriority.HIGHEST) public void onEntityDamage(EntityDamageByEntityEvent e) { if (isGuest(e.getDamager())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onEntityTarget(EntityTargetLivingEntityEvent e) { if (isGuest(e.getTarget())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onShear(PlayerShearEntityEvent e) { if (isGuest(e.getPlayer())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onBucketEmpty(PlayerBucketEmptyEvent e) { if (isGuest(e.getPlayer())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onBucketFill(PlayerBucketFillEvent e) { if (isGuest(e.getPlayer())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onFish(PlayerFishEvent e) { if (isGuest(e.getPlayer())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onHangingPlace(HangingPlaceEvent e) {
        assert e.getPlayer() != null;
        if (isGuest(e.getPlayer())) e.setCancelled(true); }
    @EventHandler(priority = EventPriority.HIGHEST) public void onHangingBreak(HangingBreakByEntityEvent e) { if (isGuest(e.getRemover())) e.setCancelled(true); }

    // === IMMORTALITY ===
    @EventHandler(priority = EventPriority.HIGHEST) public void onDamage(EntityDamageEvent e) {
        if (isGuest(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST) public void onHunger(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player player && isGuest(player)) {
            e.setCancelled(true);
            player.setFoodLevel(20);
            player.setSaturation(20);
        }
    }
}
