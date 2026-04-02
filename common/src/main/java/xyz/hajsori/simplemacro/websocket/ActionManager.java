package xyz.hajsori.simplemacro.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.gui.VoiceChatScreen;
import de.maxhenkel.voicechat.gui.VoiceChatSettingsScreen;
import de.maxhenkel.voicechat.gui.group.GroupScreen;
import de.maxhenkel.voicechat.gui.group.JoinGroupScreen;
import de.maxhenkel.voicechat.gui.volume.AdjustVolumesScreen;
import de.maxhenkel.voicechat.voice.client.*;
import de.maxhenkel.voicechat.voice.common.ClientGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.java_websocket.WebSocket;
import xyz.hajsori.simplemacro.Constants;
import xyz.hajsori.simplemacro.config.ConsumeKeys;


public class ActionManager {
    public ActionManager(String message, WebSocket ws) {
        ClientVoicechat client = ClientManager.getClient();
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerStateManager playerStates = ClientManager.getPlayerStateManager();
        JsonObject data = new Gson().fromJson(message, JsonObject.class);
        String action = data.get("action").getAsString();

        switch (action) {
            case "toggle" -> {
                String target = data.get("target").getAsString();
                switch (target) {
                    case "microphone":
                        boolean muted = !playerStates.isMuted();
                        playerStates.setMuted(muted);
                        ws.send("{\"isMuted\":" + muted + "}");
                        break;
                    case "voiceChat":
                        boolean disabled = !playerStates.isDisabled();
                        playerStates.setDisabled(disabled);
                        ws.send("{\"isDisabled\":" + disabled + "}");
                        break;
                    case "icons":
                        boolean hidden = !VoicechatClient.CLIENT_CONFIG.hideIcons.get();
                        VoicechatClient.CLIENT_CONFIG.hideIcons.set(hidden);
                        ws.send("{\"isHidden\":" + hidden + "}");
                        break;
                    case "recording":
                        if (client != null) {
                            client.toggleRecording();
                            ws.send("{\"isRecording\":" + (client.getRecorder() != null) + "}");
                        }
                        break;
                }
            }
            case "setScreen" -> {
                String target = data.get("target").getAsString();
                switch (target) {
                    case "adjustVolumes":
                        minecraft.execute(() -> minecraft.setScreen(new AdjustVolumesScreen()));
                        break;
                    case "groupManagement":
                        if (client != null && client.getConnection() != null && client.getConnection().getData().groupsEnabled()) {
                            ClientGroup group = playerStates.getGroup();
                            minecraft.execute(() -> {
                                if (group != null) {
                                    minecraft.setScreen(new GroupScreen(group));
                                } else {
                                    minecraft.setScreen(new JoinGroupScreen());
                                }
                            });
                        } else {
                            LocalPlayer player = minecraft.player;
                            if (player != null) {
                                player.sendOverlayMessage(Component.translatable("message.voicechat.groups_disabled"));
                            }
                        }
                        break;
                    case "voiceChat":
                        minecraft.execute(() -> minecraft.setScreen(new VoiceChatScreen()));
                        break;
                    case "settings":
                        minecraft.execute(() -> minecraft.setScreen(new VoiceChatSettingsScreen()));
                        break;
                }
            }
            case "activate" -> {
                String target = data.get("target").getAsString();
                switch (target) {
                    case "pushToTalk":
                        ConsumeKeys.pttKeyDown = true;
                        break;
                    case "whisper":
                        ConsumeKeys.whisperKeyDown = true;
                        break;
                }
            }
            case "deactivate" -> {
                String target = data.get("target").getAsString();
                switch (target) {
                    case "pushToTalk":
                        ConsumeKeys.pttKeyDown = false;
                        break;
                    case "whisper":
                        ConsumeKeys.whisperKeyDown = false;
                        break;
                }
            }
            case "getData" -> {
                JsonArray targets = data.get("targets").getAsJsonArray();
                JsonObject answer = new JsonObject();

                for (JsonElement targetElem : targets) {
                    String target = targetElem.getAsString();
                    switch (target) {
                        case "isMuted":
                            answer.addProperty("isMuted", playerStates.isMuted());
                            break;
                        case "isDisabled":
                            answer.addProperty("iisDisabled", playerStates.isDisabled());
                            break;
                        case "isHidden":
                            answer.addProperty("isHidden", VoicechatClient.CLIENT_CONFIG.hideIcons.get());
                            break;
                        case "isRecording":
                            if (client != null) {
                                answer.addProperty("isRecording", (client.getRecorder() != null));
                            }
                            break;
                    }
                }

                ws.send(answer.toString());
            }
        }

        Constants.LOGGER.info(String.valueOf(ConsumeKeys.pttKeyDown));
    }
}
