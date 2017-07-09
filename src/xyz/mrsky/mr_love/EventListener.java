package xyz.mrsky.mr_love;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.Config;
import xyz.mrsky.mr_safe.SafeAPI;

/**
 * Created by JianGe on 2017/7/8.
 */
public class EventListener implements Listener{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!LoveAPI.getInstance().getPlayerConfig(event.getPlayer().getName()).exists("伴侣名字")) {
            LoveAPI.getInstance().SetPlayerInfo(event.getPlayer().getName(), "伴侣名字", "null");
            LoveAPI.getInstance().SetPlayerInfo(event.getPlayer().getName(), "结婚状态", "false");
        }
        if (LoveAPI.getInstance().CheckPlayerMarry(event.getPlayer().getName())) {
            Config playerLovingConfig = LoveAPI.getInstance().getPlayerConfig(event.getPlayer().getName());
            if (playerLovingConfig.exists("留言")) {
                event.getPlayer().sendMessage("你的伴侣[" + LoveAPI.getInstance().GetPlayerMate(event.getPlayer().getName()) + "]给你发送了留言\n" + playerLovingConfig.get("留言"));
                playerLovingConfig.remove("留言");
                playerLovingConfig.save();
            }
        }
    }
}
