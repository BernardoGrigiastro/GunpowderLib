package jackyy.gunpowderlib.handler;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import jackyy.gunpowderlib.client.CapeBufferDownload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientEventsHandler {

    private static Minecraft mc = FMLClientHandler.instance().getClient();
    private static final String DEV_CAPE = "https://jackyytv.github.io/imgs/mc_mods/exchangers/capes/cape_dev.png";

    private Map<String, CapeBufferDownload> buffer = new HashMap<>();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (mc.world != null && mc.player != null && !mc.isGamePaused()) {
                for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                    if (entityPlayer instanceof AbstractClientPlayer) {
                        AbstractClientPlayer player = (AbstractClientPlayer) entityPlayer;
                        if (player.getUniqueID().equals(UUID.fromString("38de3769-70fa-441c-89e8-67280f3068a0"))) {
                            CapeBufferDownload download = buffer.get(player.getName());
                            if (download == null) {
                                download = new CapeBufferDownload(player.getName(), DEV_CAPE);
                                buffer.put(player.getName(), download);
                                download.start();
                            } else {
                                if (!download.downloaded) {
                                    continue;
                                }
                                setCape(player, download.getResourceLocation());
                            }
                        }
                    }
                }
            }
        }
    }

    private static void setCape(AbstractClientPlayer player, ResourceLocation cape) {
        NetworkPlayerInfo info = player.getPlayerInfo();
        if (info != null) {
            info.playerTextures.put(MinecraftProfileTexture.Type.CAPE, cape);
        }
    }

}
