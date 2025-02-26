package com.rb.follow.service;

import com.rb.follow.client.UserClient;
import com.rb.follow.dto.FollowEvent;
import com.rb.follow.dto.TypeOfAction;
import com.rb.follow.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired private UserClient userClient;

    @Value("${messaging.exchange}")
    private String EXCHANGE_NAME;
    @Value("${messaging.routing-key}")
    private String ROUTING_KEY;
    @Value("${mailing.subject-text}")
    private String subject;
    @Value("${mailing.content.following-text}")
    private String followingContent;
    @Value("${mailing.content.un-following-text}")
    private String unfollowingContent;

    public void publishFollowEvent(String loggedInUser, String username, TypeOfAction action){
        ResponseEntity<User> userResponseEntity = userClient.searchUser(username);
        if(userResponseEntity.getStatusCode()== HttpStatus.OK){
            String email = userResponseEntity.getBody().getEmail();
            FollowEvent followEvent = new FollowEvent();
            followEvent.setToMail(email);
            followEvent.setSubject(subject);
            if(action==TypeOfAction.FOLLOW)followEvent.setContent(loggedInUser+" "+followingContent);
            else if (action == TypeOfAction.UNFOLLOW) followEvent.setContent(loggedInUser+" "+unfollowingContent);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING_KEY,followEvent);
            log.info("Message sent to rabbit MQ");
        }
    }

}
