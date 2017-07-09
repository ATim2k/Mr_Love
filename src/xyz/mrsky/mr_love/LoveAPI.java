package xyz.mrsky.mr_love;

import cn.nukkit.utils.Config;

/**
 * Created by JianGe on 2017/7/8.
 */
public interface LoveAPI {
    static Mr_Love getInstance(){return Mr_Love.getInstance();}
    /*是否允许同性恋*/
    boolean getAllowGay();
    /*自动检测是否可以使用Mr_Safe提供的目录*/
    boolean checkMr_Safe();
    Config getPlayerConfig(String playername);
    boolean SetPlayerInfo(String playername, String type, Object info);
    String getPlayerLeaveMessage(String playername);
    /*检测是否设置性别接口*/
    String CheckPlayerSetSex(String playername);
    /*检测玩家是否结婚接口*/
    boolean CheckPlayerMarry(String playername);
    /*查看结婚伴侣接口*/
    String GetPlayerMate(String playername);
    /*检测伴侣是否在线*/
    boolean GetPlayerMateStatus(String playername);
}
