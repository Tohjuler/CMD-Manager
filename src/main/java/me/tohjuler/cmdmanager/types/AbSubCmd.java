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

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public abstract class AbSubCmd {

    @Getter
    protected String name;
    @Getter
    protected String description;
    @Getter
    protected List<String> aliases;
    @Getter
    protected String usage;
    @Getter
    protected String permission;
    @Getter @Setter
    protected String permissionMessage = "§cYou do not have permission to use this command!";

    public AbSubCmd(String name, String description, String usage, String permission, String... aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
        this.aliases = Arrays.asList(aliases);
    }

    public abstract void onCommand(CommandSender sender, Command cmd, String label, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String alias, String[] args);

    protected List<String> filter(List<String> l, String query) {
        return Arrays.asList(l.stream().filter(s -> s.toLowerCase().startsWith(query.toLowerCase())).toArray(String[]::new));
    }
}
