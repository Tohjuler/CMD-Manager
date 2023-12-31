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

import me.tohjuler.cmdmanager.types.AbSubCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.List;

public class SubCmdCall extends AbSubCmd {

    private final Method call;
    private final Object parent;
    public SubCmdCall(String name, String description, String usage, String permission, Method call, Object parent, String... aliases) {
        super(name, description, usage, permission, aliases);
        this.call = call;
        this.parent = parent;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (call == null) throw new NullPointerException("Call is null");
        try {
            call.invoke(parent, sender, cmd, label, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
