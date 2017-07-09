package xyz.mrsky.mr_love;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import xyz.mrsky.mr_love.command.LoveCommand;
import xyz.mrsky.mr_safe.*;

import java.util.List;

/**
 * Created by WE·JianGe on 2017/2/16.
 */
public class Mr_Love extends PluginBase implements LoveAPI{
    private static Mr_Love Instance;
    static Mr_Love getInstance() {
        return Instance;
    }
    public void onEnable() {
        Instance = this;
        if (checkMr_Safe()){
            this.getLogger().info("插件已检测到Mr_Safe的存在，正在强制开启Mr_Safe提供目录功能");
            this.getLogger().info("强制开启成功，路径为"+SafeAPI.getInstance().GetPlayerPath("玩家名"));
        }else{
            this.getLogger().info("插件没有检测到Mr_Safe插件，正在开启自建目录功能");
            this.getLogger().info("开启成功，路径为"+this.getDataFolder()+"/players/玩家名.yml");
        }
        this.getServer().getCommandMap().register("love", new LoveCommand());
        this.getLogger().info("插件正在加载，作者Mr_sky");
        this.getServer().getPluginManager().registerEvents(new EventListener(),this);
    }
    /*是否允许同性恋*/
    @Override
    public boolean getAllowGay(){
        return this.getConfig().get("是否允许同性结婚").equals("true");
    }

    /*自动检测是否可以使用Mr_Safe提供的目录*/
    @Override
    public boolean checkMr_Safe(){
        return this.getServer().getPluginManager().getPlugins().containsKey("Mr_Safe");
    }
    @Override
    public Config getPlayerConfig(String playername){
        if (checkMr_Safe()){
            return new Config(SafeAPI.getInstance().GetPlayerPath(playername),Config.YAML);
        }else{
            return new Config(this.getDataFolder()+"/players/"+playername.toLowerCase()+".yml",Config.YAML);
        }
    }
    @Override
    public boolean SetPlayerInfo(String playername, String type, Object info){
        if (checkMr_Safe()){
            Config pc = new Config(SafeAPI.getInstance().GetPlayerPath(playername),Config.YAML);
            pc.set(type,info);
            pc.save();
            return pc.get(type).equals(info);
        }else{
            Config pc = new Config(this.getDataFolder()+"/players/"+playername.toLowerCase()+".yml",Config.YAML);
            pc.set(type,info);
            pc.save();
            return pc.get(type).equals(info);
        }
    }
    @Override
    public String getPlayerLeaveMessage(String playername){
        return getPlayerConfig(playername).getString("留言");
    }
    /*检测是否设置性别接口*/
    @Override
    public String CheckPlayerSetSex(String playername){
        return getPlayerConfig(playername).getString("性别");
    }
    /*检测玩家是否结婚接口*/
    @Override
    public boolean CheckPlayerMarry(String playername){
        return getPlayerConfig(playername).get("结婚状态").equals("已结婚");
    }
    /*查看结婚伴侣接口*/
    @Override
    public String GetPlayerMate(String playername){
        if (getPlayerConfig(playername).get("伴侣名字") == "null"){
            return "无伴侣";
        }else{
            return getPlayerConfig(playername).get("伴侣名字").toString();
        }
    }
    /*检测伴侣是否在线*/
    @Override
    public boolean GetPlayerMateStatus(String playername){
        return this.getServer().getPlayer(GetPlayerMate(playername)).isOnline();
    }
    /**/
}
