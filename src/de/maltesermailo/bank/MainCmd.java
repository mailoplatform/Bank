package de.maltesermailo.bank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainCmd implements CommandExecutor {

	private BankSource src;
	private BankPlugin main;
	private Player p;
	public MainCmd(BankPlugin main, BankSource src) {
		this.main = main;
		this.src = src;
	}
	private String err_not_online = "§cDieser Spieler war noch nie online!";
	private String err_not_int = "§cDu musst eine Zahl angeben";
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (cs instanceof Player) {
				p = (Player) cs;
				try {
					p.sendMessage("§7Dein Konto: §6"+src.getMoney(p)+"€");
				} catch (IOException e) {
					p.sendMessage("§cDein Konto konnte nicht ausgelesen werden, bitte wende dich an einen Administrator");
				}
			} else {
				cs.sendMessage("Ich kann keine Konsolen leiden :)");
			}
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("help")) {
				cs.sendMessage("§6--- /money pay <NAME> <AMOUNT> ---");
				cs.sendMessage("§6--- /money help ---");
				cs.sendMessage("§6--- /money set <NAME> <AMOUNT>---");
				cs.sendMessage("§6--- /money remove <NAME> <AMOUNT>---");
				cs.sendMessage("§6--- /money sell ---");
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("pay")) {
				if (cs instanceof Player) {
					Player p = (Player) cs;
					Player dest = Bukkit.getPlayer(String.valueOf(args[1]));
					try {
						src.pay(p, dest, Integer.valueOf(String.valueOf(args[2])));
						p.sendMessage("§6Du hast §7" + dest.getName() + " " + args[2] + "€§6 gegeben");
					} catch (NumberFormatException e) {
						cs.sendMessage(err_not_int);
					} catch (IOException e) {
						cs.sendMessage(err_not_online);
					}
				}
			} else if (args[0].equalsIgnoreCase("sell")) {
				if (cs == null) {
					return true;
				}
				Player p = (Player) cs;
				ItemStack item = new ItemStack(Material.getMaterial(args[1]), Integer.valueOf(String.valueOf(args[2])));
				if (p.getInventory().contains(item)) {
					if (p.getItemInHand().getType() == Material.DIRT || p.getItemInHand().getType() == Material.SEEDS) {
						cs.sendMessage("§cDu kannst unwertige Gegenstände nicht verkaufen");
						return true;
					}
					p.getItemInHand().setAmount(0);
					try {
						src.setMoney(p, src.getMoney(p) + 32);
					} catch (IOException e) {
						cs.sendMessage("Fehler!");
					}
				}
			} else if (args[0].equalsIgnoreCase("set")) {
				if(cs.hasPermission("bank.admin")) {
					Player p = Bukkit.getPlayer(String.valueOf(args[1]));
					try {
						src.setMoney(p, Integer.valueOf(String.valueOf(args[2])));
						cs.sendMessage("§6Du hast §7" + p.getName() + " " + args[2] + "€ §6 gegeben");
					} catch (NumberFormatException e) {
						cs.sendMessage(err_not_int);
					} catch (IOException e) {
						cs.sendMessage(err_not_online);
					}
				}
			} else if (args[0].equalsIgnoreCase("remove")) {
				if (cs.hasPermission("bank.admin")) {
					Player p = Bukkit.getPlayer(String.valueOf(args[1]));
					try {
						src.remove(p, Integer.valueOf(String.valueOf(args[2])));
						cs.sendMessage("§6Du hast §7" + p.getName() + " " + args[2] + "€ §6 abgezogen");
					} catch (NumberFormatException e) {
						cs.sendMessage(err_not_int);
					} catch (IOException e) {
						cs.sendMessage(err_not_online);
					}
				}
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("delplayer")) {
				Player p = Bukkit.getPlayer(args[1]);
				try {
					src.delPlayer(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}

}
