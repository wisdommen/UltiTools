package com.ultikits.ultitools.commands;

import com.alibaba.fastjson.JSON;
import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.annotations.ioc.Autowired;
import com.ultikits.annotations.ioc.Consumer;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.entity.UserInfoEntity;
import com.ultikits.ultitools.dao.UserInfoDAO;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@Consumer
@CmdExecutor(function = "testCmd", permission = "", description = "test", alias = "t")
public class TestCommands extends AbstractPlayerCommandExecutor {
    @Autowired
    public UserInfoDAO userInfoDAO;

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        UserInfoEntity userInfoEntityById = userInfoDAO.getUserInfoById(player.getUniqueId().toString());
        if (userInfoEntityById == null){
            UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                    .uuid(player.getUniqueId().toString())
                    .username(player.getName())
                    .password("AAAAA")
                    .banned(false)
                    .whitelisted(false)
                    .build();
            userInfoDAO.addUserInfo(userInfoEntity);
            player.sendMessage("New User Added!");
            return true;
        }
        System.out.println(JSON.toJSONString(userInfoEntityById));
        return true;
    }
}
