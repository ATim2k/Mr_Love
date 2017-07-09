package xyz.mrsky.mr_love.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import xyz.mrsky.mr_love.LoveAPI;

import java.util.HashMap;
import java.util.List;

/**
 * Created by JianGe on 2017/7/8.
 */
public class LoveCommand extends Command{
    public LoveCommand(){
        super("love","Mr_Love主命令");
        this.commandParameters.clear();
        this.commandParameters.put("xb",new CommandParameter[]{
                new CommandParameter("xb",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("男/女","填男或者女",false),
                new CommandParameter("解释: 设定性别",null,true),
        });
        this.commandParameters.put("qh",new CommandParameter[]{
                new CommandParameter("qh",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("玩家名","填你想要求婚的玩家",false),
                new CommandParameter("解释: 对某一个玩家求婚",null,true),
        });
        this.commandParameters.put("ty",new CommandParameter[]{
                new CommandParameter("ty",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("玩家名","同意某一个玩家的结婚请求",false),
                new CommandParameter("解释: 同意某一个结婚请求",null,true),
        });
        this.commandParameters.put("jj",new CommandParameter[]{
                new CommandParameter("jj",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("玩家名","拒绝某一个玩家的结婚请求",false),
                new CommandParameter("解释: 拒绝某一个结婚请求",null,true)
        });
        this.commandParameters.put("lh",new CommandParameter[]{
                new CommandParameter("lh",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("解释: 与伴侣离婚",null,true),
        });
        this.commandParameters.put("qhlb",new CommandParameter[]{
                new CommandParameter("qhlb",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("解释: 查看你的追随者列表",null,true)
        });
        this.commandParameters.put("tp",new CommandParameter[]{
                new CommandParameter("tp",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("解释: 传送至你伴侣的身边",null,true)
        });
        this.commandParameters.put("sl",new CommandParameter[]{
                new CommandParameter("sl",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("私聊内容",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("解释: 私聊你的伴侣",null,true)
        });
        this.commandParameters.put("ly",new CommandParameter[]{
                new CommandParameter("ly",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("留言内容",CommandParameter.ARG_TYPE_STRING,false),
                new CommandParameter("解释: 给你的伴侣留言",null,true)
        });

    }
    public boolean execute(CommandSender sender,String label,String[] args){
        if (args[0].equals("help")){
            sender.sendMessage("=================[Mr_Love]指令帮助=================");
            sender.sendMessage("/love xb <男/女> 设定性别");
            sender.sendMessage("/love qh <玩家> 求婚玩家");
            sender.sendMessage("/love ty <玩家> 同意玩家的结婚请求");
            sender.sendMessage("/love lh 与你最爱的人离婚");
            sender.sendMessage("/love qhlb 查看你的追随者列表");
            sender.sendMessage("/love tp 传送至你最爱的人身边");
            sender.sendMessage("/love sl <私聊内容> 私聊伴侣的指令");
            sender.sendMessage("/love ly <留言内容> 给你的伴侣留言");
            sender.sendMessage("=================[Mr_Love]指令帮助=================");
            return true;
        }
        if (label.equals("love")){
            if (args[0].equals("xb")){
                if (args.length < 2){
                    sender.sendMessage("[Mr_Love]请输入/love sb <男/女> 以设定性别");
                    return false;
                }
                if (!LoveAPI.getInstance().CheckPlayerSetSex(sender.getName()).equals("null")){
                    sender.sendMessage("[Mr_Love]你已经设定过性别了，不能再次设定!");
                    return false;
                }
                if (args[1].equals("男")){
                    LoveAPI.getInstance().SetPlayerInfo(sender.getName(),"性别","男");
                    sender.sendMessage("[Mr_Love]成功设定了你的性别为: 男");
                    return true;
                }
                if (args[1].equals("女")){
                    LoveAPI.getInstance().SetPlayerInfo(sender.getName(),"性别","女");
                    sender.sendMessage("[Mr_Love]成功设定了你的性别为: 女");
                    return true;
                }
            }
            if (args[0].equals("qh")) {
                if (args.length < 2) {
                    sender.sendMessage("[Mr_Love]请使用/love qh <玩家> 以求婚玩家");
                    return false;
                }
                if (LoveAPI.getInstance().CheckPlayerSetSex(args[1]).equals("null")) {
                    sender.sendMessage("[Mr_Love]你所求婚的对象没有设定性别，无法向其求婚!");
                    return false;
                }
                if (!LoveAPI.getInstance().getAllowGay()&&LoveAPI.getInstance().CheckPlayerSetSex(args[1]).equals(LoveAPI.getInstance().CheckPlayerSetSex(sender.getName()))) {
                    sender.sendMessage("[Mr_Love]你所求婚的玩家性别为 男 ，当前服务器不允许同性恋");
                    return false;
                }
                if (LoveAPI.getInstance().CheckPlayerMarry(sender.getName())){
                    sender.sendMessage("[Mr_Love]你已经结婚了，还想怎么样");
                    return false;
                }
                if (LoveAPI.getInstance().getServer().getPlayer(args[1]) == null){
                    sender.sendMessage("[Mr_Love]求婚对象不在线");
                    return false;
                }
                if (!LoveAPI.getInstance().getPlayerConfig(args[1]).exists("求婚者")) {
                    LoveAPI.getInstance().SetPlayerInfo(args[1], "求婚者", null);
                }
                List dk = LoveAPI.getInstance().getPlayerConfig(args[1]).getStringList("求婚者");
                dk.add(sender.getName().toLowerCase());
                LoveAPI.getInstance().SetPlayerInfo(args[1],"求婚者",dk);
                sender.sendMessage("[Mr_Love]请求已发送，请等待对方回复");
                LoveAPI.getInstance().getServer().getPlayer(args[1]).sendMessage("[Mr_Love]玩家["+sender.getName()+"]" +
                        "向你求婚了，答应请输入/love ty <对你求婚的玩家名>");
                return true;
            }
            if (args[0].equals("ty")){
                if (args.length < 2){
                    sender.sendMessage("[Mr_Love]请使用/love ty <玩家> 以同意玩家的结婚请求");
                    return false;
                }
                if (LoveAPI.getInstance().CheckPlayerSetSex(sender.getName()).equals("null")){
                    sender.sendMessage("[Mr_Love]请先使用/love xb <男/女> 设定性别后再使用此命令");
                    return false;
                }
                if (LoveAPI.getInstance().CheckPlayerMarry(sender.getName())){
                    sender.sendMessage("[Mr_Love]你已经结婚了，还想怎么样");
                    return false;
                }
                List bk = LoveAPI.getInstance().getPlayerConfig(sender.getName()).getStringList("求婚者");
                if (bk.contains(args[1].toLowerCase())){
                    LoveAPI.getInstance().SetPlayerInfo(sender.getName(),"结婚状态","true");
                    LoveAPI.getInstance().SetPlayerInfo(sender.getName(),"伴侣名字",args[1]);
                    LoveAPI.getInstance().SetPlayerInfo(args[1],"结婚状态","true");
                    LoveAPI.getInstance().SetPlayerInfo(args[1],"伴侣名字",sender.getName());
                    sender.sendMessage("[Mr_Love]已同意该请求!");
                    sender.sendMessage("[Mr_Love]恭喜你们成功结成一对!");
                    LoveAPI.getInstance().getServer().getPlayer("[Mr_Love]恭喜你，对方同意了你的结婚请求!");
                    LoveAPI.getInstance().getServer().broadcastMessage("*------------------------喜讯------------------------*");
                    LoveAPI.getInstance().getServer().broadcastMessage("恭喜玩家["+sender.getName()+"]和玩家["+args[1]+"]结婚了!");
                    LoveAPI.getInstance().getServer().broadcastMessage("*-------------------祝你们新婚快乐-------------------*");
                    return true;
                }else{
                    sender.sendMessage("[Mr_Love]对方不在求婚列表仲，请使用/love qhlb 以查看求婚列表");
                    return false;
                }
            }
            if (args[0].equals("jj")){
                if (args.length < 2){
                    sender.sendMessage("[Mr_Love]请使用/love jj <玩家> 以拒绝玩家的结婚请求!");
                    return false;
                }
                if (LoveAPI.getInstance().CheckPlayerSetSex(sender.getName()).equals("null")){
                    sender.sendMessage("[Mr_Love]请先使用/love xb <男/女> 设定性别后再使用此命令");
                    return false;
                }
                if (LoveAPI.getInstance().CheckPlayerMarry(sender.getName())){
                    sender.sendMessage("[Mr_Love]你已经结婚了，还想怎么样");
                    return false;
                }
                List fk = LoveAPI.getInstance().getPlayerConfig(sender.getName()).getStringList("求婚者");
                if (fk.contains(args[1])){
                    fk.remove(args[1]);
                    LoveAPI.getInstance().SetPlayerInfo(sender.getName(),"求婚者",fk);
                    sender.sendMessage("[Mr_Love]已拒绝该请求!");
                    LoveAPI.getInstance().getServer().getPlayer(args[1]).sendMessage("[Mr_Love]对方拒绝了你的求婚请求!");
                    return true;
                }else{
                    sender.sendMessage("[Mr_Love]对方并不在求婚列表中，请使用/love qhlb 查看求婚列表");
                    return false;
                }
            }
            if (args[0].equals("qhlb")){
                if (!LoveAPI.getInstance().getPlayerConfig(sender.getName()).exists("求婚者")){
                    sender.sendMessage("还没有玩家向你求婚!");
                    return false;
                }else{
                    sender.sendMessage("你的求婚者有:"+String.valueOf(LoveAPI.getInstance().getPlayerConfig(sender.getName()).getStringList("求婚者")));
                    return true;
                }
            }
            if (args[0].equals("lh")){
                HashMap<String,String> lhlh = new HashMap<>();
                HashMap<String,String> lhlh2 = new HashMap<>();
                /*
                * lhlh  [sender,sender]
                * lhlh2 [loveplayer,loveplayer]
                * if lhlh get sender = sender and lhlh2 get love = love , sender.getplayermate = love{
                *   离婚
                * }
                * */
                if (lhlh2.containsKey(sender.getName())){
                    lhlh2.put(sender.getName(),sender.getName());
                    sender.sendMessage("如需离婚，请再输入一遍此指令");
                    return true;
                }
                if (lhlh.get(LoveAPI.getInstance().GetPlayerMate(sender.getName())).equals(LoveAPI.getInstance().GetPlayerMate(sender.getName()))&&lhlh2.get(sender.getName()).equals(sender.getName())){
                    LoveAPI.getInstance().SetPlayerInfo(sender.getName(),"结婚状态","false");
                    LoveAPI.getInstance().SetPlayerInfo(LoveAPI.getInstance().GetPlayerMate(sender.getName()),"结婚状态","false");
                    LoveAPI.getInstance().SetPlayerInfo(sender.getName(),"伴侣名字",null);
                    LoveAPI.getInstance().SetPlayerInfo(LoveAPI.getInstance().GetPlayerMate(sender.getName()),"伴侣名字",null);
                    sender.sendMessage("离婚完成，你现在是条单身狗了");
                    LoveAPI.getInstance().getServer().getPlayer(LoveAPI.getInstance().GetPlayerMate(sender.getName())).sendMessage("离婚完成，你现在是条单身狗了");
                    return true;
                }
                if (!lhlh.containsKey(sender.getName())) {
                    lhlh.put(sender.getName(),null);
                    sender.sendMessage("已启用离婚程序，如需离婚请再输入一遍此指令");
                    return true;
                }
                if (lhlh.containsKey(sender.getName())){
                    lhlh.put(sender.getName(),sender.getName());
                    lhlh2.put(LoveAPI.getInstance().GetPlayerMate(sender.getName()),null);
                    LoveAPI.getInstance().getServer().getPlayer(LoveAPI.getInstance().GetPlayerMate(sender.getName())).sendMessage("你的伴侣开启了离婚程序，如同意请输入/love lh");
                    sender.sendMessage("你已经确定离婚了，等待伴侣的回应");
                    return true;
                }
            }
            if (args[0].equals("tp")){
                if (LoveAPI.getInstance().CheckPlayerSetSex(sender.getName()).equals("null")){
                    sender.sendMessage("[Mr_Love]请先使用/love xb <男/女> 设定性别后再使用此命令");
                    return false;
                }
                if (!LoveAPI.getInstance().CheckPlayerMarry(sender.getName())){
                    sender.sendMessage("[Mr_Love]你都没结婚，想怎么样");
                    return false;
                }
                if (LoveAPI.getInstance().getServer().getPlayer(LoveAPI.getInstance().GetPlayerMate(sender.getName())) == null){
                    sender.sendMessage("你的伴侣离线了，无法传送!");
                    return false;
                }else{
                    LoveAPI.getInstance().getServer().getPlayer(sender.getName()).teleport(LoveAPI.getInstance().getServer().getPlayer(LoveAPI.getInstance().GetPlayerMate(sender.getName())).getLocation());
                    sender.sendMessage("传送完毕");
                    LoveAPI.getInstance().getServer().getPlayer(LoveAPI.getInstance().GetPlayerMate(sender.getName())).sendMessage("你的伴侣传送到了你的身边");
                    return true;
                }
            }
            if (args[0].equals("sl")){
                if (args.length < 2){
                    sender.sendMessage("[Mr_Love]请使用/love sl <私聊内容> 以私聊你的伴侣");
                    return false;
                }
                if (LoveAPI.getInstance().CheckPlayerSetSex(sender.getName()).equals("null")){
                    sender.sendMessage("[Mr_Love]请先使用/love xb <男/女> 设定性别后再使用此命令");
                    return false;
                }
                if (!LoveAPI.getInstance().CheckPlayerMarry(sender.getName())){
                    sender.sendMessage("[Mr_Love]你都没结婚，想怎么样");
                    return false;
                }
                if (LoveAPI.getInstance().getServer().getPlayer(LoveAPI.getInstance().GetPlayerMate(sender.getName())) == null){
                    sender.sendMessage("你的伴侣离线，无法私聊!");
                    sender.sendMessage("你可以使用/love ly <留言内容> 以留言给你的伴侣");
                    return false;
                }else{
                    LoveAPI.getInstance().getServer().dispatchCommand(sender,"tell "+LoveAPI.getInstance().GetPlayerMate(sender.getName())+" "+args[1]);
                    return true;
                }
            }
            if (args[0].equals("ly")){
                if (args.length < 2){
                    sender.sendMessage("请使用/love ly <留言内容> 以留言给你的伴侣");
                    return false;
                }
                if (LoveAPI.getInstance().CheckPlayerSetSex(sender.getName()).equals("null")){
                    sender.sendMessage("[Mr_Love]请先使用/love xb <男/女> 设定性别后再使用此命令");
                    return false;
                }
                if (!LoveAPI.getInstance().CheckPlayerMarry(sender.getName())){
                    sender.sendMessage("[Mr_Love]你都没结婚，想怎么样");
                    return false;
                }
                if (LoveAPI.getInstance().getServer().getPlayer(LoveAPI.getInstance().GetPlayerMate(sender.getName()))==null){
                    LoveAPI.getInstance().SetPlayerInfo(LoveAPI.getInstance().GetPlayerMate(sender.getName()),"留言",args[1]);
                    return true;
                }else{
                    sender.sendMessage("你的情侣在线，无法留言!");
                    sender.sendMessage("你可以使用/love sl <私聊内容> 以私聊你的伴侣");
                    return false;
                }
            }
        }
        return true;
    }
}
