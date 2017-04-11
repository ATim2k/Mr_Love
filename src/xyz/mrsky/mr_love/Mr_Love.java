package xyz.mrsky.mr_love;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import xyz.mrsky.mr_core.Mr_CORE;

import java.util.List;

/**
 * Created by WE·JianGe on 2017/2/16.
 */
public class Mr_Love extends PluginBase implements Listener{
    private static Mr_Love plugin;
    public static Mr_Love getPlugin() {
        return plugin;
    }
    public void onEnable(){
        plugin = this;
        update update = new update();
        update.updateCheck();
        if (this.getServer().getPluginManager().getPlugins().containsKey("Mr_CORE")){
            this.getLogger().info(TextFormat.BLUE+"Mr_CORE插件检测: "+TextFormat.GREEN+"通过"+TextFormat.WHITE);
            this.getLogger().info(TextFormat.YELLOW+"插件正在开启，作者Mr_sky，贴吧ID贱哥啊哈哈");
            this.getServer().getPluginManager().registerEvents(this,this);
        }else{
            this.getLogger().info(TextFormat.BLUE+"Mr_CORE插件检测: "+TextFormat.RED+"不通过"+TextFormat.WHITE);
            this.getLogger().warning("请使用Mr_CORE插件以提供更好的插件支持！");
            this.getServer().getPluginManager().disablePlugin(this.getServer().getPluginManager().getPlugin("Mr_Love"));
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(event.getPlayer().getName().toLowerCase());
        if (!playerConfig.exists("伴侣名字")){
            playerConfig.set("伴侣名字",null);
            playerConfig.set("结婚状态","未结婚");
            playerConfig.save();
        }
        if (CheckPlayerLoving(event.getPlayer().getName())){
            Config playerLovingConfig = Mr_CORE.getPlugin().getPlayerConfig(GetPlayerLoves(event.getPlayer().getName()).toLowerCase());
            if (playerLovingConfig.exists("留言")){
                event.getPlayer().sendMessage("你的伴侣["+GetPlayerLoves(event.getPlayer().getName())+"]给你发送了留言\n"+playerLovingConfig.get("留言"));
                playerLovingConfig.remove("留言");
                playerLovingConfig.save();
            }
        }
    }
    public boolean onCommand(CommandSender sender, Command command, String zhiling, String[] liebiao) {
        if (zhiling.equals("love")){
            if (liebiao.length < 1){
                sender.sendMessage("请输入/love help 以查看帮助");
                return false;
            }
            if (liebiao[0].equals("help")){
                sender.sendMessage("=================[Mr_Love]指令帮助=================");
                sender.sendMessage("/love xb <男/女> 设定性别");
                sender.sendMessage("/love qh <玩家> 求婚玩家");
                sender.sendMessage("/love ty <玩家> 同意玩家的结婚请求");
                sender.sendMessage("/love lh 与你最爱的人离婚"+TextFormat.RED+"[请勿随意使用此命令！否则后果自负！]"+TextFormat.WHITE);
                sender.sendMessage("/love qhlb 查看你的追随者列表");
                sender.sendMessage("/love tp 传送至你最爱的人身边");
                sender.sendMessage("/love sl <私聊内容> 私聊伴侣的指令");
                sender.sendMessage("/love ly <留言内容> 给你的伴侣留言");
                sender.sendMessage("=================[Mr_Love]指令帮助=================");
                return true;
            }
            /*设定性别*/
            if (liebiao[0].equals("xb")){
                if (liebiao.length < 2){
                    sender.sendMessage("请输入/love xb <男/女> 以设定性别");
                    return false;
                }
                if (liebiao[1].equals("男")){
                    Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(sender.getName());
                    if (playerConfig.get("性别") == null){
                        playerConfig.set("性别","男");
                        sender.sendMessage("成功设定你的性别为男");
                        playerConfig.set("结婚状态","未结婚");
                        playerConfig.save();
                        return true;
                    }else{
                        sender.sendMessage("你已经设定过性别，不能再次设定!");
                        return false;
                    }
                }
                if (liebiao[1].equals("女")){
                    Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(sender.getName());
                    if (playerConfig.get("性别") == null){
                        playerConfig.set("性别","女");
                        sender.sendMessage("成功设定你的性别为女");
                        playerConfig.set("结婚状态","未结婚");
                        playerConfig.save();
                        return true;
                    }else{
                        sender.sendMessage("你已经设定过性别，不能再次设定!");
                        return false;
                    }
                }
            }
            /*求婚*/
            if (liebiao[0].equals("qh")){
                /*检测是否有liebiao[1]*/
                if (liebiao.length < 2){
                    sender.sendMessage("请使用/love qh <玩家> 以求婚其他玩家");
                    return false;
                }else{
                    Config beiQiuHunConfig = Mr_CORE.getPlugin().getPlayerConfig(liebiao[1]);
                    /*检测是否设定了性别*/
                    if (!CheckPlayerSetSex(sender.getName())){
                        sender.sendMessage("请先使用/love xb <男/女> 设定性别再使用求婚命令!");
                        return false;
                    }
                    if (!CheckPlayerSetSex(liebiao[1])){
                        sender.sendMessage("你所求婚的对象没有设定性别，无法向其求婚!");
                        return false;
                    }
                    if (CheckPlayerLoving(sender.getName())){
                        sender.sendMessage("你已经结婚了还求婚个毛线!");
                        return false;
                    }
                    /*检测求婚对象是否存在*/
                    if (this.getServer().getPlayer(liebiao[1]) == null){
                        sender.sendMessage("求婚对象不在线");
                        return false;
                    }else{
                        /*检测求婚列表是否存在*/
                        if (!beiQiuHunConfig.exists("求婚者")){
                            beiQiuHunConfig.set("求婚者",null);
                            beiQiuHunConfig.save();
                            List qiuHunLieBiao = beiQiuHunConfig.getStringList("求婚者");
                            qiuHunLieBiao.add(sender.getName());
                            beiQiuHunConfig.set("求婚者",qiuHunLieBiao);
                            beiQiuHunConfig.save();
                        }else{
                            List qiuHunLieBiao = beiQiuHunConfig.getStringList("求婚者");
                            qiuHunLieBiao.add(sender.getName());
                            beiQiuHunConfig.set("求婚者",qiuHunLieBiao);
                            beiQiuHunConfig.save();
                        }
                        sender.sendMessage("请求已发送，等待对方回复中");
                        this.getServer().getPlayer(liebiao[1]).sendMessage("玩家["+sender.getName()+"]向你求婚了，答应请输入/love ty <求婚玩家名字>");
                        return true;
                    }
                }
            }
            /*被求婚同意*/
            if (liebiao[0].equals("ty")){
                if (liebiao.length < 2){
                    sender.sendMessage("请使用/love ty <玩家> 以同意玩家的结婚请求");
                    return false;
                }else{
                    if (!CheckPlayerSetSex(sender.getName())){
                        sender.sendMessage("请先使用/love xb <男/女> 设定性别再使用同意命令!");
                        return false;
                    }else{
                        if (CheckPlayerLoving(sender.getName())){
                            sender.sendMessage("你已经结婚了还同意个毛线!");
                            return false;
                        }else{
                            Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(sender.getName());
                            Config beiQiuHunConfig = Mr_CORE.getPlugin().getPlayerConfig(liebiao[1]);
                            List qiuhunlist = playerConfig.getStringList("求婚者");
                            if (qiuhunlist.contains(liebiao[1])){
                                playerConfig.set("结婚状态","已结婚");
                                playerConfig.set("伴侣名字",liebiao[1]);
                                beiQiuHunConfig.set("结婚状态","已结婚");
                                beiQiuHunConfig.set("伴侣名字",sender.getName());
                                playerConfig.save();
                                beiQiuHunConfig.save();
                                sender.sendMessage("已同意该请求!");
                                this.getServer().getPlayer(liebiao[1]).sendMessage("对方接受了你的结婚请求，快去拥抱对方吧!");
                                this.getServer().broadcastMessage("恭喜玩家["+sender.getName()+"]和玩家["+liebiao[1]+"]结婚了，祝你们新婚快乐!");
                                return true;
                                }else{
                                sender.sendMessage("对方并不在求婚列表中，请使用/love qhlb 查看求婚列表");
                                return false;
                                }
                            }
                        }
                    }
                }
                if (liebiao[0].equals("jj")){
                    if (liebiao.length < 2){
                        sender.sendMessage("请使用/love jj <玩家> 以拒绝玩家的结婚请求");
                        return false;
                    }else{
                        if (!CheckPlayerSetSex(sender.getName())){
                            sender.sendMessage("请先使用/love xb <男/女> 设定性别再使用拒绝命令!");
                            return false;
                        }else {
                            if (CheckPlayerLoving(sender.getName())) {
                                sender.sendMessage("你已经结婚了还拒绝个毛线!");
                                return false;
                            } else {
                                Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(sender.getName());
                                Config beiQiuHunConfig = Mr_CORE.getPlugin().getPlayerConfig(liebiao[1]);
                                List qiuhunlist = playerConfig.getStringList("求婚者");
                                if (qiuhunlist.contains(liebiao[1])){
                                    playerConfig.getStringList("求婚者").remove(liebiao[1]);
                                    playerConfig.save();
                                    sender.sendMessage("已拒绝该请求!");
                                    this.getServer().getPlayer(liebiao[1]).sendMessage("对方拒绝了你的结婚请求!");
                                    return true;
                                }else{
                                    sender.sendMessage("对方并不在求婚列表中，请使用/love qhlb 查看求婚列表");
                                    return false;
                                }
                            }
                        } 
                    }
                }
            }
            /*求婚列表*/
            if (liebiao[0].equals("qhlb")){
                Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(sender.getName());
                if (playerConfig.getStringList("求婚者") == null){
                    sender.sendMessage("还没有玩家向你求婚!");
                    return false;
                }else{
                    sender.sendMessage("你的求婚者有:" + String.valueOf(playerConfig.getStringList("求婚者")));
                    return true;
                }
            }
            /*离婚*/
            if (liebiao[0].equals("lh")){
                if (!CheckPlayerSetSex(sender.getName())){
                    sender.sendMessage("请先使用/love xb <男/女> 设定性别再使用离婚命令!");
                    return false;
                }else{
                    if (!CheckPlayerLoving(sender.getName())){
                        sender.sendMessage("你都没结婚离个毛线!");
                        return false;
                    }else{
                        Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(sender.getName());
                        Config beiQiuHunConfig = Mr_CORE.getPlugin().getPlayerConfig(GetPlayerLoves(sender.getName()));
                        this.getServer().getPlayer(GetPlayerLoves(sender.getName())).sendMessage("你的伴侣"+sender.getName()+"和你离婚了");
                        playerConfig.set("结婚状态","未结婚");
                        playerConfig.set("伴侣名字",null);
                        beiQiuHunConfig.set("结婚状态","未结婚");
                        beiQiuHunConfig.set("伴侣名字",null);
                        playerConfig.save();
                        beiQiuHunConfig.save();
                        sender.sendMessage("离婚成功，你现在是条开心的单身狗!");
                    }
                }
            }
            /*情侣TP*/
            if (liebiao[0].equals("tp")){
                if (!CheckPlayerSetSex(sender.getName())){
                    sender.sendMessage("请先使用/love xb <男/女> 设定性别再使用传送命令!");
                    return false;
                }else{
                    if (!CheckPlayerLoving(sender.getName())){
                        sender.sendMessage("你都没结婚传送个毛线!");
                        return false;
                    }else{
                        if (this.getServer().getPlayer(GetPlayerLoves(sender.getName())) == null){
                            sender.sendMessage("你的伴侣离线，无法传送!");
                            return false;
                        }else{
                            this.getServer().getPlayer(sender.getName()).teleport(this.getServer().getPlayer(GetPlayerLoves(sender.getName())).getPosition());
                            sender.sendMessage("传送完毕");
                            this.getServer().getPlayer(GetPlayerLoves(sender.getName())).sendMessage("你的伴侣传送到了你的身边!");
                            return true;
                        }
                    }
                }
            }
            /*情侣私聊*/
            if (liebiao[0].equals("sl")){
                if (liebiao.length < 2){
                    sender.sendMessage("请使用/love sl <私聊内容> 以私聊你的伴侣");
                }
                if (!CheckPlayerSetSex(sender.getName())){
                    sender.sendMessage("请先使用/love xb <男/女> 设定性别再使用情侣私聊命令!");
                    return false;
                }else{
                    if (!CheckPlayerLoving(sender.getName())){
                        sender.sendMessage("你都没结婚私聊个毛线!");
                        return false;
                    }else {
                        if (this.getServer().getPlayer(GetPlayerLoves(sender.getName())) == null) {
                            sender.sendMessage("你的伴侣离线，无法私聊!");
                            sender.sendMessage("你可以使用/love ly <留言内容> 以留言给你的伴侣");
                            return false;
                        }else{
                            this.getServer().dispatchCommand(sender,"tell "+GetPlayerLoves(sender.getName())+" "+liebiao[1]);
                            return true;
                        }
                    }
                }
            }
            /*情侣留言*/
            if (liebiao[0].equals("ly")){
                ;if (liebiao.length < 2){
                    sender.sendMessage("请使用/love ly <留言内容> 以留言给你的伴侣");
                }
                if (!CheckPlayerSetSex(sender.getName())){
                    sender.sendMessage("请先使用/love xb <男/女> 设定性别再使用情侣私聊命令!");
                    return false;
                }else{
                    if (!CheckPlayerLoving(sender.getName())){
                        sender.sendMessage("你都没结婚私聊个毛线!");
                        return false;
                    }else {
                        if (CheckPlayerInServer(this.getServer().getPlayer(sender.getName())).equals(GetPlayerLoves(sender.getName()))){
                            this.getServer().getPlayer(GetPlayerLoves(sender.getName())).sendMessage("你的伴侣["+sender.getName()+"]给你发送了留言\n"+liebiao[1]);
                            return true;
                        }else{
                            Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(sender.getName().toLowerCase());
                            playerConfig.set("留言",liebiao[1]);
                            playerConfig.save();
                            return true;
                        }
                    }
                }
            }
        return true;
    }
    public String CheckPlayerInServer(Player player){
        if (this.getServer().getOnlinePlayers().containsValue(player)){
            return player.getName();
        }else{
            return null;
        }
    }
    public Object getPlayerJoinAndSendMessage(String playername){
        Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(playername.toLowerCase());
        return playerConfig.get("留言");
    }
    /*检测是否设置性别接口*/
    public boolean CheckPlayerSetSex(String playername){
        Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(playername.toLowerCase());
        if (playerConfig.exists("性别")){
            return true;
        }else{
            return false;
        }
    }
    /*检测玩家是否结婚接口*/
    public boolean CheckPlayerLoving(String playername){
        Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(playername.toLowerCase());
        if (playerConfig.get("结婚状态").equals("已结婚")){
            return true;
        }else{
            return false;
        }
    }
    /*查看结婚伴侣接口*/
    public String GetPlayerLoves(String playername){
        Config playerConfig = Mr_CORE.getPlugin().getPlayerConfig(playername.toLowerCase());
        if (playerConfig.get("伴侣名字") == null){
            return "无伴侣";
        }else{
            return playerConfig.get("伴侣名字").toString();
        }
    }
}
