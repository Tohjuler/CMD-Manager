<h1 align="center">Welcome to Cmd-Manager</h1>
<p align="center">
  <img src="https://img.shields.io/github/license/Tohjuler/cmd-manager?color=blue&style=flat-square"  alt="license"/>
</p>

> This lib was made for simple creation of commands in minecraft.

## Author

* **Tohjuler**
* **Discord:** tohjuler
* **Website:** https://tohjuler.dk
* **Email:** [tobias@tohjuler.dk](mailto:tobias@tohjuler.dk)

## Documentation

### Maven
```xml
<repositories>
    <repository>
        <id>tohjuler-repository-releases</id>
        <name>Tohjuler's Repository</name>
        <url>https://repo.tohjuler.dk/releases</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>me.tohjuler.cmdmanager</groupId>
        <artifactId>CmdManager</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

### Init
You need to init the CmdManager in your onEnable method.
```java
public class Main() extends JavaPlugin {
    private CmdManager cmdManager;
    
    @Override
    public void onEnable() {
        // Init the cmd Manager
        cmdManager = new CmdManager(this);
    }   
}
```

### Creating a command
You can create a command in 2 ways, with a class or with a lambda.
#### Class
```java
public class TestCommand extends Cmd {

    public TestCommand() {
        // Name, description, usage, permission, aliases
        super("test", "Just a test cmd", "/test", "test");
    }

    // Use the annotation to create a subcmd
    @SubCmd(name = "test", description = "Just a test subcmd", usage = "/test test")
    public boolean onSubCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Test, this is a subcmd");
        return true;
    }
}
```
#### Lambda
```java
public class Main() extends JavaPlugin {
    private CmdManager cmdManager;
    
    @Override
    public void onEnable() {
        // Init the cmd Manager
        cmdManager = new CmdManager(this);
        
        // Name, description, usage, permission, callback, aliases
        cmdManager.cmd("test", "A description of a test command", "/test", null, e -> {
            e.getSender().sendMessage("§c§lTest");
        });
    }   
}
```

### Registering a command
You can register a command manually or a hole folder with commands.

You don't need to register the commands in the plugin.yml, the lib will do it for you.
```java
public class Main() extends JavaPlugin {
    private CmdManager cmdManager;
    
    @Override
    public void onEnable() {
        // Init the cmd Manager
        cmdManager = new CmdManager(this);
        
        // Register the command manually
        cmdManager.register(new TestCommand());
        
        // Register a hole folder
        // The method takes a folder name from the main folder of the plugin
        // The main folder is where your main class is
        cmdManager.registerFolder("commands");
    }   
}
```

### Cooldown 
The lib has a cooldown utils to make it easy to add cooldowns to commands.

```java
import me.tohjuler.cmdmanager.utils.Cooldown;

import java.util.concurrent.TimeUnit;

public class Main() extends JavaPlugin {
    private CmdManager cmdManager;

    @Override
    public void onEnable() {
        // Init the cmd Manager
        cmdManager = new CmdManager(this);

        // Create a new cooldown object
        Cooldown cooldown = new Cooldown();

        // Name, description, usage, permission, callback, aliases
        cmdManager.cmd("test", "A description of a test command", "/test", null, e -> {
            // Check if the sender has a cooldown
            if (cooldown.hasCooldown(e.getSender()))
                // Send a message to the sender with the remaining time
                e.getSender().sendMessage("§cYou have to wait " + cooldown.getRemainingTime(e.getSender(), TimeUnit.MINUTES) + " minutes");
            else {
                // Set a cooldown for the sender
                cooldown.setCooldown(e.getSender(), 5, TimeUnit.MINUTES);
                e.getSender().sendMessage("§c§lTest");
            }
        });
    }
}
```

## 📝 License

Copyright © 2023 [Tohjuler](https://github.com/Tohjuler).<br />
This project is [MIT](https://github.com/Tohjuler/cmd-manager/blob/master/LICENSE) licensed.
