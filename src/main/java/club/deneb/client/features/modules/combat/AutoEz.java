package club.deneb.client.features.modules.combat;

import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.utils.EntityUtil;
import club.deneb.client.value.ModeValue;
import club.deneb.client.value.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.RandomUtils;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by KillRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Module.Info(name = "AutoEz",category = Category.COMBAT)
public class AutoEz extends Module {

    Value<Boolean> clientName = setting("ClientName", false);
    Value<Boolean> antiKick = setting("AntiSpam", false);
    ModeValue<String> ezModeSetting = setting("EzMode","Toxic",listOf("Toxic","Custom"));
    Value<Integer> timeoutTicks = setting("TimeoutTicks", 20, 0, 100);

    public static String ezMsg = "default ez msg";

    private ConcurrentHashMap<String, Integer> targetedPlayers = null;
    private boolean Announce, Announce2;

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event){
        if (mc.player == null || this.isDisabled()) {
            return;
        }

        EntityLivingBase entity;
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<>();
        }
        if ((entity = (EntityLivingBase) event.getEntity()) == null) {
            return;
        }
        if (!EntityUtil.isPlayer(entity)) {
            return;
        }

        EntityLivingBase player = entity;
        if (player.getHealth() > 0.0f) {
            return;
        }

        String name = player.getName();
        if (this.shouldAnnounce(name)) {
            Announce = true;
        }

        if (Announce && !Announce2) {
            this.doAnnounce(name);
            Announce = false;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send packetSent){
        if (mc.player == null || this.isDisabled()) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<>();
        }
        if (!(packetSent.packet instanceof CPacketUseEntity)) {
            return;
        }
        CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)packetSent.packet;
        if (!cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
            return;
        }
        Entity targetEntity = cPacketUseEntity.getEntityFromWorld(mc.world);
        if (!EntityUtil.isPlayer(targetEntity)) {
            return;
        }
        this.addTargetedPlayer(targetEntity.getName());
    }



    @Override
    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap<>();
    }

    @Override
    public void onDisable() {
        this.targetedPlayers = null;
    }

    @Override
    public void onTick() {
        if (this.isDisabled() || mc.player == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<>();
        }
        for (Entity entity : mc.world.getLoadedEntityList()) {
            String name2;
            EntityPlayer player;
            if (entity instanceof EntityPlayer) {
                if ((player = (EntityPlayer) entity).getHealth() > 0.0f || !this.shouldAnnounce(name2 = player.getName())) {
                    continue;
                }
                if (!Announce2) {
                    this.doAnnounce(name2);
                    Announce2 = true;
                } else {
                    Announce2 = false;
                }
                break;
            }
        }
        this.targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.targetedPlayers.remove(name);
            } else {
                this.targetedPlayers.put(name, timeout - 1);
            }
        });
    }

    private boolean shouldAnnounce(String name) {
        return this.targetedPlayers.containsKey(name);
    }


    private void doAnnounce(String name) {
        String messageSanitized;
        this.targetedPlayers.remove(name);
        StringBuilder message = new StringBuilder();
        if (ezModeSetting.toggled("Custom")) {
            message.append(ezMsg);
        } else if (ezModeSetting.toggled("Toxic")){
            message.append("Ezzzz ");
        } else {
            message.append("Good fight ");
        }
        message.append(name);
        message.append("!");
        if (this.clientName.getValue()) {
            message.append(" ");
            message.append("Deneb");
            message.append(" owns you and all!");
        }

        if (antiKick.getValue()){
            message.append(" [").append(randomString(2)).append("]");
        }

        if ((messageSanitized = message.toString().replaceAll("\u00a7", "")).length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }
        mc.player.connection.sendPacket(new CPacketChatMessage(messageSanitized));
    }

    public void addTargetedPlayer(String name) {
        if (Objects.equals(name, mc.player.getName())) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<>();
        }
        this.targetedPlayers.put(name, this.timeoutTicks.getValue());
    }

    public ConcurrentHashMap<String, Integer> getTargetedPlayer(){
        return targetedPlayers;
    }

    private final char[] chars = {
            'c','0','1','2','3','4','5','6','7','8',
            '9','a','b','d','e','f','g','h','i','j',
            'k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z','A','B','C','D',
            'E','F','G','H','I','J','K','L','M','N',
            'O','P','Q','R','S','T','U','V','W','X',
            'Y','Z'};

    public String randomString(int length) {
        char[] c = new char[length];

        for (int i = 0; i < length; i++)
            c[i] = chars[RandomUtils.nextInt(0, chars.length)];

        return new String(c);
    }


}
