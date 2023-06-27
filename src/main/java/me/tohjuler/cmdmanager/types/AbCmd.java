/*
 * MIT License
 *
 * Copyright (c) 2023 Tohjuler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.tohjuler.cmdmanager.types;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbCmd extends Command implements TabCompleter, CommandExecutor {

    protected boolean playerOnly = false;
    protected String playerOnlyMsg = "&cOnly players can use this command!";

    protected List<AbSubCmd> subCmds = new ArrayList<>();

    public AbCmd(String name, String description, String usage, String permission, String... aliases) {
        super(name, description, usage, Arrays.asList(aliases));
        setPermission(permission);
    }

    protected void send(CommandSender sender, String... msg) {
        for (String s : msg)
            if (!s.equals("NULL"))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

    /**
     * Sends the help message for this command to the sender
     * Overrides this method to change the help message
     *
     * @param sender The sender to send the help message to
     */
    public void sendHelp(CommandSender sender) {
        send(sender,
                "&8- &e" + getName() + " &8-",
                (description != null && !description.equals("") ? "&7" + description : "NULL"),
                (getUsage() != null && !getUsage().equals("") ? "&7Usage: " + getUsage() : "NULL"),
                (getAliases().size() > 0 ? "&7Aliases: &e" + String.join(", ", getAliases()) : "NULL"),
                ""
        );
        if (subCmds.size() > 0)
            send(sender, subCmds.stream().map(subCmd -> " &f/&e" + getName() + " " + subCmd.getName() + " &8- &7" + subCmd.getDescription()).toArray(String[]::new));
    }

    /**
     * Adds a sub command to this command
     *
     * @param subCmds The sub commands to add
     */
    public void addSubCmd(AbSubCmd... subCmds) {
        this.subCmds.addAll(Arrays.asList(subCmds));
    }

    @Override
    public boolean execute(CommandSender commandSender, String command, String[] arg) {
        if (getPermission() != null) {
            if (!commandSender.hasPermission(getPermission())) {
                if (getPermissionMessage() == null) {
                    commandSender.sendMessage(ChatColor.RED + "no permit!");
                } else {
                    commandSender.sendMessage(getPermissionMessage());
                }
                return false;
            }
        }
        onCommand(commandSender, this, command, arg);
        return true;
    }
}
