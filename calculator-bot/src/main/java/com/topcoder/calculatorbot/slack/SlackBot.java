package com.topcoder.calculatorbot.slack;

import com.topcoder.calculatorbot.service.Calculator;
import me.ramswaroop.jbot.core.common.Controller;
import me.ramswaroop.jbot.core.common.EventType;
import me.ramswaroop.jbot.core.common.JBot;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.WebSocketSession;

import java.util.regex.Matcher;

/**
 * A simple Slack Bot. You can create multiple bots by just
 * extending {@link Bot} class like this one. Though it is
 * recommended to create only bot per jbot instance.
 *
 * @author ramswaroop
 * @version 1.0.0, 05/06/2016
 */
@JBot
@Profile("slack")
public class SlackBot extends Bot {

    private static final Logger logger = LoggerFactory.getLogger(SlackBot.class);

    @Autowired
    private Calculator calculator;

    /**
     * Slack token from application.properties file. You can get your slack token
     * next <a href="https://my.slack.com/services/new/bot">creating a new bot</a>.
     */
    @Value("${slackBotToken}")
    private String slackToken;

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    @Controller(events = EventType.DIRECT_MESSAGE)
    public void onReceiveDM(WebSocketSession session, Event event) {
        logger.info("Equation received: " + event.getText());
        String text = event.getText();
        String result = calculator.calculate(text).toPlainString();
        reply(session, event, new Message(result));
    }

}