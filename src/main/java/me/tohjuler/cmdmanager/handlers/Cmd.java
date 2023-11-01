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

package me.tohjuler.cmdmanager.handlers;

import me.tohjuler.cmdmanager.types.AbCmd;
import me.tohjuler.cmdmanager.types.AbSubCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cmd extends AbCmd {

    public Cmd(String name, String description, String usage, String permission, String... aliases) {
        super(name, description, usage, permission, aliases);
    }

    /**
     * Override this method to execute the main command
     * This method is only called if no sub command is found
     *
     * @param sender The sender of the command
     * @param cmd The command that was executed
     * @param label The label of the command
     * @param args The arguments of the command
     * @return Whether the command was executed successfully
     *
     */
    public boolean executeMain(CommandSender sender, Command cmd, String label, String[] args) { return false; }

    /**
     * Override this method to handle the onCommand event
     *
     * @param sender The sender of the command
     * @param cmd The command that was executed
     * @param label The label of the command
     * @param args The arguments of the command
     * @return Whether the command was executed successfully
     *
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (playerOnly && !(sender instanceof Player)) {
            send(sender, playerOnlyMsg);
            return true;
        }

        if (args.length > 0)
            for (AbSubCmd subCmd : subCmds)
                if (subCmd.getName().equalsIgnoreCase(args[0]) || subCmd.getAliases().contains(args[0])) {
                    if (subCmd.getPermission() != null && !sender.hasPermission(subCmd.getPermission())) {
                        sender.sendMessage(subCmd.getPermissionMessage());
                        return true;
                    }
                    subCmd.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
        if (!executeMain(sender, cmd, label, args))
            sendHelp(sender);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) return new ArrayList<>();

        String firstArg = args.length > 0 ? args[0] : "";

        if (firstArg.isEmpty()) return filter(new ArrayList<>(Arrays.asList(subCmds.stream().map(AbSubCmd::getName).toArray(String[]::new))), firstArg);

        List<AbSubCmd> subCmdsList = Arrays.asList(subCmds.stream().filter(s -> s.getName().equalsIgnoreCase(firstArg) || s.getAliases().contains(firstArg)).toArray(AbSubCmd[]::new));
        if (subCmdsList.isEmpty()) return filter(new ArrayList<>(Arrays.asList(subCmds.stream().map(AbSubCmd::getName).toArray(String[]::new))), firstArg);
        AbSubCmd subCmd = subCmdsList.get(0);
        if (subCmd != null && args.length > 1)
            return subCmd.onTabComplete(sender, alias, Arrays.copyOfRange(args, 1, args.length));

        return filter(new ArrayList<>(Arrays.asList(subCmds.stream().map(AbSubCmd::getName).toArray(String[]::new))), firstArg);
    }
}
