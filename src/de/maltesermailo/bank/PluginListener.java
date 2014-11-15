package de.maltesermailo.bank;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

public class PluginListener implements Listener {

	private BankSource src;
	private BankPlugin plugin;
	public PluginListener(BankSource src, BankPlugin plugin) {
		this.src = src;
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		try {
			src.openFileOfPlayer(e.getPlayer());
			File f = new File("plugins/Bank/playerdata/" + e.getPlayer().getName() + ".yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
			cfg.set("money", this.plugin.getConfig().get("FirstJoinMoney"));
			cfg.save(f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRightclickSign(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
				BlockState bs = e.getClickedBlock().getState();
				if (bs instanceof Sign) {
					Sign s = (Sign) bs;
					if (s.getLine(0).equalsIgnoreCase("[SHOWBANK]") && s.getLine(1).equalsIgnoreCase("Rightclick")) {
						if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
							s.setLine(0, e.getPlayer().getName());
							s.setLine(1, "Money:");
							try {
								s.setLine(2, "" + src.getMoney(e.getPlayer()) + "€");
							} catch (IndexOutOfBoundsException e1) {
								s.setLine(2, "Fehler");
							} catch (IOException e1) {
								s.setLine(2, "Fehler");
							}
							s.setLine(3, "Rightclick");
							s.update();
						}
					} else if (s.getLine(0).equalsIgnoreCase(e.getPlayer().getName()) && s.getLine(3).equalsIgnoreCase("Rightclick")) {
						if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
							s.setLine(0, "[SHOWBANK]");
							s.setLine(1, "Rightclick");
							s.setLine(2, "");
							s.setLine(3, "");
							s.update();
						}
					} else if (s.getLine(0).equalsIgnoreCase("[BUY]")) {
						if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.SIGN) {
							//Funktioniert nicht!
						}
					}
				}
			} else {
				return;
			}
		}
	}
}
