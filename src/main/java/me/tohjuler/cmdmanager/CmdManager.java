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

package me.tohjuler.cmdmanager;

import me.tohjuler.cmdmanager.annotations.SubCmd;
import me.tohjuler.cmdmanager.events.CmdEvent;
import me.tohjuler.cmdmanager.handlers.CmdCall;
import me.tohjuler.cmdmanager.types.AbCmd;
import me.tohjuler.cmdmanager.types.AbSubCmd;
import me.tohjuler.cmdmanager.handlers.Cmd;
import me.tohjuler.cmdmanager.handlers.SubCmdCall;
import me.tohjuler.cmdmanager.utils.AnnotationUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CmdManager {

    private final JavaPlugin pl;

    private final List<AbCmd> cmds = new ArrayList<>();

    private CommandMap commandMap;

    public CmdManager(JavaPlugin pl) {
        this.pl = pl;

        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (CommandMap) f.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register commands in a folder
     * If you have your command in src\main\java\me\tohjuler\cmdmanager\cmds
     * you would call registerFolder("cmds")
     * As cmdmanager is the package of the main class
     *
     * @param folderName The folder to register, relative to the package of the main class
     */
    public void registerFolder(String folderName) {
        for (Class<? extends Cmd> clazz : new Reflections(pl.getClass().getPackage().getName()+"."+folderName).getSubTypesOf(Cmd.class)) {
            try {
                register(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Register commands
     *
     * @param cmds The commands to register
     */
    public void register(AbCmd... cmds) {
        for (AbCmd cmd : cmds) {
            register(cmd);
        }
    }

    /**
     * Register a command
     *
     * @param cmd The command to register
     */
    public void register(AbCmd cmd) {
        cmds.add(cmd);

        if (cmd instanceof Cmd) {
            List<Method> methods = AnnotationUtils.getMethodsWithAnnotation(cmd.getClass(), SubCmd.class);

            List<AbSubCmd> subCmds = new ArrayList<>();
            for (Method method : methods) {
                SubCmd annotation = method.getAnnotation(SubCmd.class);

                SubCmdCall subCmd = new SubCmdCall(
                        annotation.name(),
                        annotation.description(),
                        annotation.usage(),
                        annotation.permission(),
                        method,
                        cmd,
                        annotation.aliases()
                );
                subCmd.setPermissionMessage(annotation.permissionMessage());
                subCmds.add(subCmd);
            }
            cmd.addSubCmd(subCmds.toArray(new AbSubCmd[0]));
        }

        if (commandMap == null) throw new NullPointerException("CommandMap is null");
        commandMap.register(pl.getName(), cmd);
    }

    /**
     * Register a command with a consumer
     * This is a shortcut for creating a simple command
     *
     * @param name The name of the command
     * @param description The description of the command
     * @param usage The usage of the command
     * @param permission The permission of the command
     * @param call The consumer to call when the command is executed
     * @param aliases The aliases of the command
     */
    public void cmd(String name, String description, String usage, String permission, Consumer<CmdEvent> call, String... aliases) {
        register(new CmdCall(name, description, usage, permission, call, aliases));
    }

}
