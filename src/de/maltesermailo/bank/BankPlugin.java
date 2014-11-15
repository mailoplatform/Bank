package de.maltesermailo.bank;

import org.bukkit.plugin.java.JavaPlugin;

public class BankPlugin extends JavaPlugin {

	public void onEnable() {
		this.getCommand("money").setExecutor(new MainCmd(this, new BankSource()));
		this.getConfig().addDefault("FirstJoinMoney", 0);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.getServer().getPluginManager().registerEvents(new PluginListener(new BankSource(), this), this);
	}
}
