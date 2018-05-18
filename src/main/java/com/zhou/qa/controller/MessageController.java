package com.zhou.qa.controller;

import com.zhou.qa.model.Message;
import com.zhou.qa.model.User;
import com.zhou.qa.model.UserHolder;
import com.zhou.qa.model.ViewObject;
import com.zhou.qa.service.MessageService;
import com.zhou.qa.service.UserService;
import com.zhou.qa.util.QaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouxinyu1cp on 2018/5/17.
 */

// 站内信相关的控制器，包括发送站内信等
@Controller
public class MessageController
{
    public static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    UserHolder userHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    // 发送私信
    // 通过js函数来发送私信，是一个POST请求，返回一个json字符串给前端更新页面
    // 发送成功返回状态码0，跳转到 /msg/list
    // 具体看 resources/static/scripts/main/component/popupMsg.js
    @RequestMapping(value = "/msg/addMessage", method = {RequestMethod.POST})
    @ResponseBody
    public String sendMessage(@RequestParam("toName") String toName,
                              @RequestParam("content") String content)
    {
        try
        {
            // 未登录，返回999，跳转到登录页面
            if(userHolder.getUser() == null)
            {
                return QaUtil.getResponseJsonString(999);
            }

            User toUser = userService.getUserByName(toName);
            if(toUser == null)
            {
                return QaUtil.getResponseJsonString(1, "接收方用户不存在");
            }

            Message message = new Message();
            message.setFromId(userHolder.getUser().getId());
            message.setToId(toUser.getId());
            message.setCreatedDate(new Date());
            message.setContent(content);
            message.setHasRead(0);

            messageService.addMessage(message);
        }
        catch (Exception e)
        {
            logger.error("发送私信错误：" + e.getMessage());
            return QaUtil.getResponseJsonString(2, "服务器内部错误");
        }

        return QaUtil.getResponseJsonString(0);
    }

    // 返回某个会话id的所有私信内容
    // 请求信息在 letter.html 中
    @RequestMapping(value = "/msg/detail", method = {RequestMethod.GET})
    public String getConversationMessageDetail(@RequestParam("conversationId") String conversationId,
                                               Model model)
    {
        try
        {
            List<Message> messageDetails = messageService.getMessageDetailsByConversationId(conversationId, 0, 10);

            List<ViewObject> vos = new ArrayList<ViewObject>();
            for(Message message : messageDetails)
            {
                ViewObject vo = new ViewObject();
                vo.set("user", userService.getUserById(message.getFromId()));
                vo.set("message", message);

                vos.add(vo);
            }

            model.addAttribute("messages", vos);
        }
        catch (Exception e)
        {
            logger.error("获取私信详细信息出错：" + e.getMessage());
        }

        return "letterDetail";
    }

    // 返回与该用户关联的，收、发方是该用户的所有私信列表
    // 请求 URI 是 /msg/list
    @RequestMapping(value = "/msg/list", method = {RequestMethod.GET})
    public String getMessageList(Model model)
    {
        try
        {
            int curUserId = userHolder.getUser().getId();

            // 能发送私信列表请求，用户一定已经登录
            List<Message> messagesList = messageService.getMessageListByUserId(curUserId, 0, 10);

            List<ViewObject> vos = new ArrayList<>();
            for(Message msg : messagesList)
            {
                ViewObject vo = new ViewObject();

                vo.set("conversation", msg);

                int otherUserId = (msg.getFromId() == curUserId ? msg.getToId() : msg.getFromId());
                User otherUser = userService.getUserById(otherUserId);
                vo.set("user", otherUser);

                int unRead = messageService.getConversationUnreadCount(curUserId, msg.getConversationId());
                vo.set("unread", unRead);

                vos.add(vo);
            }

            model.addAttribute("conversations", vos);
        }
        catch (Exception e)
        {
            logger.error("获取用户私信列表失败：" + e.getMessage());
        }

        return "letter";
    }
}














