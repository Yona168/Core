# Core
[![](https://jitpack.io/v/Yona168/Core.svg)](https://jitpack.io/#Yona168/Core)


First off, credit's due where credit is due. This core makes use of:
1: Avelyn core, for events. -> https://gitlab.com/Avelyn/Architecture
2: Kryo, for object serialziation. ->https://github.com/EsotericSoftware/kryo


## Features:
1. Strong abstraction-This core provides default implementations for things, but also allows for you to make something fitting the design. For example, the Database class allows for many different implementations-one of them being the file database.

2. Easy to use!

3. Many, many things that are usually time consuming, including but not limited to: Databases, Commands, XP System and Economy.

## Referencing
Look here for a guide: https://jitpack.io/#Yona168/Core

## How do I use it?

### Main class changes:
Instead of extending JavaPlugin, extend CorePlugin. Like so:

```java 
public class Main extends CorePlugin{
@Override
public void onEnable(){
//If you wish to use commands, or any feature that outputs strings, see the Commands section for what to add here.
}

}
```
### PlayerData
#### Creating Player Data
In this core, it is possible to create player data-a set of data that can be saved specifically to each player. Before we go about how to store that, let's create a player data class:
```java
public class MyPlayerData extends PlayerData{
private int myVal;

public MyPlayerData(int val){
this.myVal=val;
}

public int getMyVal(){
return myVal
}

}
```
That's more or less it. The only property the abstract class PlayerData has is that it is PlayerData. There are no fields in it, and the constructor is default. Therefore, we do not need to call super() since java does that for us.

I have also included some modifiers to implement into player data in order to easily create systems. Since it is possible to implement multiple modifiers per class, the modifiers are interfaces and therefore a lot of the implementation has to be done by you. No worries, lets look at the XPModifier, and how that would work for example.
```java
public class XPPlayerThing extends PlayerData implements XPPlayer {
    private int currentXp;
    private int level;
    private int xpNeeded;

    public XPPlayerThing() {//You may want to add constructor args in order to modify the values at construction. No need though.

    }

    @Override
    public int currentXp() {// returns the amount of xp the player has. NOT the total-this is the amount between 0 and the xp needed to level up that the player is at
        return currentXp;
    }

    @Override
    public void setCurrentXp(int amt) {//setter for currentxp
        this.currentXp = amt;
    }

    @Override
    public int currentLevel() {//getter for current level
        return this.level;
    }

    @Override
    public void setCurrentLevel(int amt) {//setter for current level
        this.level = amt;
    }

    @Override
    public int levelUpAmountIncrease() {/*Amount of xp the total amount increases by every time. If you want this to be modifiable, 
    pass a config through the constructor and make this value a field*/
        return 20;
    }

    @Override
    public int threshold() {//Getter for amount of xp needed to level up
        return xpNeeded;
    }

    @Override
    public void setNextThreshold() {//Ups the amount of xp needed to level up by the amount
        this.xpNeeded += levelUpAmountIncrease();
    }

    @Override
    public void setPreviousThreshold() {//Decreases the amount o xp needed to level up as defined by previousThreshold() in XPPlayer
        this.xpNeeded = previousThreshold();
    }
}
```

#### Storing Player Data
Once you have your player data created, you can use the Database class to store it. As mentioned before, Database is abstract, so feel free to create your own implementation. For now, we will use the provided FileDatabase as an example, as well as the PlayerData implementation we just created. Do this preferably in the main class, preferably in onEnable:

```java 
public class Main extends CorePlugin{
private Database database;
@Override
public void onEnable(){
this.database=super.setupDb(new FileDatabase(this, ()->new XPPlayerThing());
}

}
```
The setupDb method takes two parameters: The instance of the main class, and a supplier which gives the database the ability to create new XPPlayerThings if needed. After doing this for the first time, a config file will generate in the plugin folder allowing for flexibility with backups.

If you wish to create your own implementation of Database, this is how you would do that:
```java
public class MyDatabase extends Database {

    public MyDatabase(JavaPlugin main, Supplier<PlayerData> function) {
        super(main, function);

    }


    @Override
    public Optional<PlayerData> fromStorage(Player player) {
//Return an Optional.of the player data from the large database-Optional.empty() if not found.
    }

    @Override
    public PlayerData write(Player player, PlayerData data) {
//write a player into the Database with the corresponding PlayerData
    }

    @Override
    public boolean isRegistered(UUID uuid) {
      //Check if the Player with the UUID is registered in the large database
    }
}

```
And that's all! Simple, huh?

#### PlayerDataModifier
PlayerDataModifier is an interface that player data modifiers, like XPPlayer, extend. If you wish to create your own modifier, go ahead. Simply extend PlayerDataModifier and add methods.
##### Transactions
For the two PlayerDataModifiers I created, I created Transactions for them. A Transaction takes two players, makes sure at least one of them isn't null, grabs their player data and adds/removes things. For example, here is the XPTransaction class:

```java
public class XPTransaction extends Transaction {


    public XPTransaction(Player giver, Player taker, int amt, Database database) {
        super(giver, taker, amt, database);/*Calling super constructor with the giver (the player who will lose xp), the taker
(the player gaining), the amt being transferred, and the database that will get the playerdata.*/
    }

    @Override
    public boolean execute() {
//Making sure both players have playerdata, if they are not null
        final PlayerData giver = super.getGiver() == null ? null : getDatabase().fromCache(super.getGiver()).orElseThrow(() -> new RuntimeException("Player data not found in cache!"));
        final PlayerData taker = super.getTaker() == null ? null : getDatabase().fromCache(super.getTaker()).orElseThrow(() -> new RuntimeException("Player data not found in cache!"));
        XPPlayer ecoGiver;
        XPPlayer ecoTaker;
//Making sure that the playerdatas returned are XPPlayers, or are null
        if (!((giver == null || giver instanceof XPPlayer) && (taker == null || taker instanceof XPPlayer))) {
            throw new IllegalArgumentException("Non-XPlayers cannot be used in a XP Transaction!");
        } else {
            ecoGiver = (XPPlayer) giver;
            ecoTaker = (XPPlayer) taker;
        }
//Making sure at least one player is not null. One null player is ideal for things like the server giving xp.
        if (allAreNull()) {
            throw new IllegalArgumentException("Both objects implementing EcoPlayer cannot be null!");
        } if (ecoGiver != null) {//Doing the transaction
            ecoGiver.takeXp((int) super.getAmt());
            super.getGiver().sendMessage(PluginStrings.tag() + ChatColor.GREEN + " Transaction complete.");
        }  if (taker != null) {
            ecoTaker.giveXp((int) super.getAmt());
            super.getTaker().sendMessage(PluginStrings.tag() + ChatColor.GREEN + " Transaction complete.");
        }
        return true;
    }


}
```
Note-This concept has not been developed fully. A better system will most likely be implemented soon.

### The Configuration and ConfigurationHandler Feature
I have simplified the way to make config files. As long as the file is created in your project folder/resources folder, doing the following will create if it it doesnt exist and return the package of the configuration and file bundled together. For this example, I'll use a theoretical file called options.yml:
```java
final Configuration options=new Configuration("options.yml");
```
The Configuration class has the following methods:
Method | Description
------------ | -------------
void save()|saves the configuration to the file
void reload()|reloads the configuration to the last time save() was called
FileConfiguration configuration()|returns the FileConfiguration
Path file()|returns the file as a Path

I have also made a simple way to handle these configurations-the ConfigurationHandler. The ConfigurationHandler has a Map<T, Configuration>, where T can be whatever you want. Let's say I had a ConfigurationEnum enum and wanted to use that as the key. I would do:
```java
final ConfigurationHandler<ConfigurationEnum> handler=new ConfigurationHandler();
```
It has the following methods:
add(T key, Configuration config)->Adds a Configuration to the map
saveAll()->saves all the Configurations in the map
reloadAll()->reloads all the configurations in the map
Note: It will be much more efficient to not use map calls, but this is a generic way that I implemented. Feel free to not use it.

### Commands
I have made it extremely simple to register commands in the form of subcommands. First, this must be done in your onEnable. For this example, I will have a theoretical plugin called AutoFurniture, whose color scheme is brown and red

```java
public class Main extends CorePlugin{
private Database database;
public void onEnable(){
this.database=super.setupDb(new FileDatabase(this, ()->new XPPlayerThing());
PluginStrings.setup(this, ChatColor.RED, ChatColor.BROWN, "AFurniture", "af");
}

}
```
This tells the plugin that from now on:
1. Our color scheme is red and brown
2. The tag name (to be displayed in brackets with our color scheme) is AFurniture
3. The main command name is /af
**MAKE SURE THE MAIN COMMAND NAME IS REGISTERED AS A COMMAND IN YOUR PLUGIN.YML**

Once you have that set up, we can go onto implement subcommands. A subcommand is a command that gets executed after your main command as an argument. For example "/af help" would pull up the help screen for my plugin. In order to do so, we need to create some implementation of AbstractCommandManager to manage the subcommands. I have already created a default implementation: CommandManager, which is what we'll be using for this example. Feel free to make your own-the only thing left to implement with it is how it finds subcommands. For example, the default command manager checks if the player has the permission pluginname.commands.commandname authomatically, so you may want to change that. Once you have a command manager, you can add subcommands to it. I have created a Help subcommand, which works with all subcommands, so we'll use that as an example.

```java
public class Main extends CorePlugin{
private Database database;
private AbstractCommandManager cmdManager;
public void onEnable(){
super.setup();
this.database=super.setupDb(new FileDatabase(this, ()->new XPPlayerThing());
PluginStrings.setup(this, ChatColor.RED, ChatColor.BROWN, "AFurniture", "af");
this.cmdManager=new CommandManager(this);
cmdManager.add(Help::new);
getCommand("af").setExecutor(cmdManager);
}

}
```
The reason we used a method reference right there is because the Help subcommand requires the command manager to be passed to the constructor. AbstractCommandManager has two implementations of .add: add(SubCommand subCommand) and add(Function<AbstractCommandManager, SubCommand>). We simply used the second.

The .add method returns the AbstractCommandManager executing it, so you can theoretically do:
```java
cmdManager.add(new CommandOne()).add(CommandTwo::new).add(new CommandThree());
```
Once a command has been added to the manager, as long as the manager isn't garbage collected, it will work.

#### The PluginStrings Class
The PluginStrings class, which we briefly discussed above, is simply a box that holds all of the strings the plugin, especially the commands bit, will use. These are its methods:

Method | Description
------------ | -------------
void setup(JavaPlugin plugin, ChatColor colorOne, ChatColor colorTwo, String tagTitle, String label)|Described above
String tag()|returns the colored tag constructed with the tagTitle specified in setup
String mainCmdLabel(boolean withSlash)|returns the command label as specified by label in setup, with or without a slash preceding it depending on the argument
ChatColor mainColor()|returns the main color as specified by  colorOne in setup
ChatColor secondaryColor|returns the secondary color as specified by ColorTwo in setup
String invalidSyntax|returns the message players get when they incorrectly execute a command
String noPerms|returns the message players get when they try to execute a command they do not have permission to execute

#### Creating a SubCommand
First, let's take a look at the SubCommand class:
```java
public abstract class SubCommand {
    private final String name;
    private final String desription;
    private final String[] aliases;
    private final String requiredPermissions;
    private final String usage;
    private final String invalidSyntax;

    public SubCommand(String name, String desription, String usage, String[] aliases) {
        this.desription=desription;
        this.name=name;
        this.aliases=aliases;
        requiredPermissions = PluginStrings.mainCmdLabel(false) + ".commands." + name();
        this.usage = PluginStrings.mainCmdLabel(true) + " "+usage;
        invalidSyntax = PluginStrings.invalidSyntax() + usage();
    }

    public SubCommand(String name, String desription, String usage) {
        this(name, desription, usage, null);
    }

    protected abstract boolean onCommand(CommandSender sender, String[] args);

    public String name() {
        return name;
    }

    public String description() {
        return desription;
    }

    public Optional<String[]> aliases() {
        return aliases == null ? Optional.empty() : Optional.of(aliases);
    }

    public String usage() {
        return usage;
    }

    public String requiredPermissions() {
        return requiredPermissions;
    }

    public String invalidSyntax() {
        return invalidSyntax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCommand that = (SubCommand) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(desription, that.desription) &&
                Arrays.equals(aliases, that.aliases) &&
                Objects.equals(requiredPermissions, that.requiredPermissions) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(invalidSyntax, that.invalidSyntax);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, desription, requiredPermissions, usage, invalidSyntax);
        result = 31 * result + Arrays.hashCode(aliases);
        return result;
    }
}
```
As you can see, its fairly simple. We must simply give it the Strings it wants, and implement the functionality. Let's look at the Help SubCommand I made as an example:
```java
public class Help extends SubCommand {
    final AbstractCommandManager commandManager;

    public Help(AbstractCommandManager commandManager) {
        super("help", "Displays commands and their descriptions", "help [page]", null); /*Calling super constructor. Arguments in order:
1. Our players are going to type /af help, so our title has to be "help"
2. The help command needs a page, so the usage is "help [page]"
3. the help command has no aliases, so I passed in null for aliases.*/

        this.commandManager = commandManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(invalidSyntax()); /*Sending the player the invalid syntax message. Note that you should use the one specified in SubCommand, not PluginStrings*/
            return false;
        }
        int num = MiscUtils.parseInt(args[0]).orElse(0);//MiscUtils is just a utils class I made. We'll look at it later.

        if (num <= 0) {
            sender.sendMessage(invalidSyntax());
            return false;
        }
        final ChatColor first = PluginStrings.mainColor();
        final ChatColor second = PluginStrings.secondaryColor();
        final StringBuilder sending = new StringBuilder(second + "---" + PluginStrings.tag() + first + "Page: " + num + second + "---");
        commandManager.getCommands().stream().skip((num-1) * 5).limit(5).map(command ->
                "\n"+first + command.name() + ChatColor.WHITE + ": " + second + command.description()).forEach(sending::append);
        sender.sendMessage(sending.toString());
        return true;
    }


}
```
And its that easy! All you have to do is add your subcommand to your command manager and you're good to go!

### MyGUI
I made a simple GUI class in order to make them easier to deal with. Here's how it works:

```java
        final ItemStack adventureStick=new ItemStack(Material.STICK);
        final ItemMeta meta=adventureStick.getItemMeta();
        meta.setDisplayName("Adventure Stick!");
        adventureStick.setItemMeta(meta);
       final GUI gui=new MyGUI("Name of GUI", 9).set(3, adventureStick, inventoryClickEvent -> {
            inventoryClickEvent.getWhoClicked().sendMessage("You clicked "+inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName()
            +", the stick of adventure!");
            inventoryClickEvent.getWhoClicked().setGameMode(GameMode.ADVENTURE);
        });
gui.open(Bukkit.getPlayer("Yona168"));
```

In this snippet, you can see we can easily create a GUI by using the .set method. .set takes the slot (3), the itemstack (the stick), and a consumer of InventoryClickEvent to execute when the stick is clicked. .set returns the MyGUI, so you can call it similarly to how we called .add with the abstract command manager to add multiple. The MyGUI class also has a constructor that takes a Map<ItemStack, Consumer<InventoryClickEvent>> as a third parameter. If this is used, it will execute similarly to calling .set(nextOpenSlot, key, value) for each of them.

The MyGUI class also has a pattern feature, which builds the inventory with the specified pattern. For example:
```java
 gui.withPattern(new ItemStack(Material.EMERALD), MyGUI.PatternType.BORDER);
```
Will fill the sides of the GUI with emeralds as a border. **USING THIS FEATURE WILL OVERRIDE ANY EXISTING ITEMSTACKS IN THOSE SLOTS. USE THIS BEFORE SETTING ANY ITEMS**

Method | Description
------------ | -------------
MyGUI set(int slot, ItemStack item, Consumer<InventoryClickEvent>)|adds an item to the gui as described above. Returns itself.
void open(Player player)|opens the gui to the player
MyGUI withPattern(MyGUI.PatternType)|Fills the slots of the inventory according to the pattern specified. Returns itself.

### Random Utilities
I made a bunch of random utilities to make my life easier. Feel free to use them

**FileUtils**
Method | Description
------------ | -------------
static Stream<Path> list(Path path)|Tries and catches Files.list(Path path)
static FileConfiguration save(File file, FileConfiguration configuration)|Tries and catches FileConfiguration#save on the File
static FileConfiguration loadConfiguration(File file)|returns a new FileConfiguration for the specified file
static FileConfiguration reload(File file, FileConfiguration configuration)|loads the configuration to the file and saves it
static void createDirectory(Path directory)|Tries and catches Files.createDirectory on directory

**SchedulerUtils**
Just runs methods from Bukkit.getScheduler()

**MiscUtils**
Method | Description
------------ | -------------
static Optional<Integer> parseInt(String string)| tries/catches Integer#parseInt on the String and returns an Optional.empty() if fails

Any Bugs/Issues/Questions? Let me know, and I'll get to them as soon as I can.
Enjoy!
Yona168






