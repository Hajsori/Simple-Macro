package xyz.hajsori.simplemacro;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.gui.VoiceChatScreen;
import de.maxhenkel.voicechat.gui.VoiceChatSettingsScreen;
import de.maxhenkel.voicechat.gui.group.GroupScreen;
import de.maxhenkel.voicechat.gui.group.JoinGroupScreen;
import de.maxhenkel.voicechat.gui.volume.AdjustVolumesScreen;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.common.ClientGroup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.java_websocket.WebSocket;

public class ActionManager {
    public ActionManager(String action, WebSocket ws) {
        ClientVoicechat client = ClientManager.getClient();
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientPlayerStateManager playerStates = ClientManager.getPlayerStateManager();

        switch (action) {
            case "toggleMicrophone":
                boolean muted = !playerStates.isMuted();
                playerStates.setMuted(muted);
                ws.send("{\"isMuted\":" + muted + "}");
                break;
            case "toggleVoicechat":
                boolean disabled = !playerStates.isDisabled();
                playerStates.setDisabled(disabled);
                ws.send("{\"isDisabled\":" + disabled + "}");
                break;
            case "toggleIcons":
                boolean hidden = !VoicechatClient.CLIENT_CONFIG.hideIcons.get();
                VoicechatClient.CLIENT_CONFIG.hideIcons.set(hidden);
                ws.send("{\"isHidden\":" + hidden + "}");
                break;
            case "toggleRecording":
                if (client != null) {
                    client.toggleRecording();
                }
                break;
            case "adjustVolumes":
                minecraft.setScreen(new AdjustVolumesScreen());
                break;
            case "groupManagement":
                if (client != null && client.getConnection() != null && client.getConnection().getData().groupsEnabled()) {
                    ClientGroup group = playerStates.getGroup();
                    if (group != null) {
                        minecraft.setScreen(new GroupScreen(group));
                    } else {
                        minecraft.setScreen(new JoinGroupScreen());
                    }
                } else {
                    ClientPlayerEntity player = minecraft.player;
                    if (player != null) {
                        player.sendMessage(Text.translatable("message.voicechat.groups_disabled"), true);
                    }
                }
                break;
            case "voicechatMenu":
                minecraft.setScreen(new VoiceChatScreen());
                break;
            case "settingsMenu":
                minecraft.setScreen(new VoiceChatSettingsScreen());
        }
    }
}
