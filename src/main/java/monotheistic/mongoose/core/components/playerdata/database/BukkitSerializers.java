package monotheistic.mongoose.core.components.playerdata.database;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static java.lang.Class.forName;
import static java.lang.String.format;
import static org.bukkit.Bukkit.getServer;

class BukkitSerializers {
   private static final Class<?> CLASS_ITEM;
   private static final Constructor<?> CONSTRUCTOR_ITEM;
   private static final Method METHOD_ITEM_SAVE;

   private static final Constructor<?> CONSTRUCTOR_NBT;
   private static final Method METHOD_NBT_SAVE;
   private static final Method METHOD_NBT_LOAD;

   static {
       try {
           final String version = getServer().getClass().getName().split("\\.")[3];
           final String nms = "net.minecraft.server.%s.%s";
           final String bukkit = "org.bukkit.craftbukkit.%s.%s";
           final Class<?> nbt = forName(format(nms, version, "NBTTagCompound"));

           CLASS_ITEM = forName(format(nms, version, "ItemStack"));
           CONSTRUCTOR_NBT = nbt.getConstructor();
           CONSTRUCTOR_NBT.setAccessible(true);
           CONSTRUCTOR_ITEM = CLASS_ITEM.getConstructor(nbt);
           CONSTRUCTOR_ITEM.setAccessible(true);
           METHOD_ITEM_SAVE = CLASS_ITEM.getDeclaredMethod("save", nbt);
           METHOD_ITEM_SAVE.setAccessible(true);
           Method save = null, load = null;
           for (Method method : forName(format(nms, version, "NBTCompressedStreamTools")).getDeclaredMethods()) {
               final Class<?>[] params = method.getParameterTypes();
               if (params.length == 1 && params[0] == DataInputStream.class)
                   load = method;
               else if (params.length == 2 && params[1] == DataOutput.class)
                   save = method;
           }
           if ((METHOD_NBT_LOAD = load) == null)
               throw new IllegalStateException("Could not find a load method in tools.");
           if ((METHOD_NBT_SAVE = save) == null)
               throw new IllegalStateException("Could not find a save method in tools.");
           METHOD_NBT_LOAD.setAccessible(true);
           METHOD_NBT_SAVE.setAccessible(true);
       } catch (Exception e) {
           throw new IllegalStateException("Could not initialize reflection!", e);
       }
   }

   public static void registerSerializer(Kryo kryo) {
       kryo.register(CLASS_ITEM, new Serializer<Object>() {
           @Override
           public void write(Kryo kryo, Output output, Object item) {
               try {
                   METHOD_NBT_SAVE.invoke(null,
                           METHOD_ITEM_SAVE.invoke(item, CONSTRUCTOR_NBT.newInstance()),
                           new DataOutputStream(output)
                   );
               } catch (Exception e) {
                   throw new IllegalStateException("Failed to serialize NBT for an ItemStack.", e);
               }
           }

           @Override
           public Object read(Kryo kryo, Input input, Class<Object> type) {
               try {
                   return CONSTRUCTOR_ITEM.newInstance(
                           METHOD_NBT_LOAD.invoke(null, new DataInputStream(input))
                   );
               } catch (Exception e) {
                   throw new IllegalStateException("Failed to serialize NBT for an ItemStack.", e);
               }
           }
       });
   }
}