package com.iphayao.linebot;

import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.regex.*;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@LineMessageHandler
public class LineBotController {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    @EventMapping //พิมอะไรมาตอบคำเดิม
    public void handleTextMessage(MessageEvent<TextMessageContent> event) {
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }//พิมอะไรมาตอบคำเดิม------------------------

    @EventMapping //ส่งสติกเกอร์ ส่งกลับเป็นสติกเกอร์
    public void handleStickerMessage(MessageEvent<StickerMessageContent> event) {
        log.info(event.toString());
        StickerMessageContent message = event.getMessage();
        reply(event.getReplyToken(), new StickerMessage(
                message.getPackageId(), message.getStickerId()
        ));
    }//ส่งสติกเกอร์ ส่งกลับเป็นสติกเกอร์------------------------

    @EventMapping // ส่งโลเคชั้น บนแผนที่ กลับเป็นโลเคชั้น
    public void handleLocationMessage(MessageEvent<LocationMessageContent> event) {
        log.info(event.toString());
        LocationMessageContent message = event.getMessage();
        reply(event.getReplyToken(), new LocationMessage(
                (message.getTitle() == null) ? "Location replied" : message.getTitle(),
                message.getAddress(),
                message.getLatitude(),
                message.getLongitude()
        ));
    }// ส่งโลเคชั้น บนแผนที่ กลับเป็นโลเคชั้น------------------------

    @EventMapping // รับส่งรูปภาพ
    public void handleImageMessage(MessageEvent<ImageMessageContent> event) {
        log.info(event.toString());
        ImageMessageContent content = event.getMessage();
        String replyToken = event.getReplyToken();

        try {
            MessageContentResponse response = lineMessagingClient.getMessageContent(
                    content.getId()).get();
            DownloadedContent jpg = saveContent("jpg", response);
            DownloadedContent previewImage = createTempFile("jpg");

            system("convert", "-resize", "240x",
                    jpg.path.toString(),
                    previewImage.path.toString());

            reply(replyToken, new ImageMessage(jpg.getUri(), previewImage.getUri()));

        } catch (InterruptedException | ExecutionException e) {
            reply(replyToken, new TextMessage("Cannot get image: " + content));
            throw new RuntimeException(e);
        }

    }

    private void system(String... args) {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try {
            Process start = processBuilder.start();
            int i = start.waitFor();
            log.info("result: {} => {}", Arrays.toString(args), i);
        } catch (InterruptedException e) {
            log.info("Interrupted", e);
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static DownloadedContent saveContent(String ext,
                                                 MessageContentResponse response) {
        log.info("Content-type: {}", response);
        DownloadedContent tempFile = createTempFile(ext);
        try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
            ByteStreams.copy(response.getStream(), outputStream);
            log.info("Save {}: {}", ext, tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static DownloadedContent createTempFile(String ext) {
        String fileName = LocalDateTime.now() + "-"
                + UUID.randomUUID().toString()
                + "." + ext;
        Path tempFile = Application.downloadedContentDir.resolve(fileName);
        tempFile.toFile().deleteOnExit();
        return new DownloadedContent(tempFile, createUri("/downloaded/" + tempFile.getFileName()));
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).toUriString();
    }

    @Value
    public static class DownloadedContent {
        Path path;
        String uri;
    }// รับส่งรูปภาพ------------------------

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) {
        String text = content.getText();
        boolean hasText = text.contains("@N;");
        boolean hasText1 = text.contains("เรื่อง");
        boolean hasText2 = text.contains("ต่อ");
        boolean hasText3 = text.contains("@END");
        boolean hasText4 = text.contains("Profile");
        String userId = event.getSource().getUserId();

        if(hasText == true){
            if(userId != null) {
                lineMessagingClient.getProfile(userId)
                        .whenComplete((profile, throwable) -> {
                            if (throwable != null) {
                                this.replyText(replyToken, throwable.getMessage());
                                return;
                            }
                            this.reply(replyToken, Arrays.asList(
                                    new TextMessage("เป็นโปรแกรมรับเรื่องอัตโนมัติ"),
                                    new TextMessage("กรุณาแจ้งเรื่องพิม เรื่อง")
                            ));
                        });
            }
        }else if (hasText1 == true || hasText2 == true){
            if(userId != null) {
                lineMessagingClient.getProfile(userId)
                        .whenComplete((profile, throwable) -> {
                            if (throwable != null) {
                                this.replyText(replyToken, throwable.getMessage());
                                return;
                            }
                            this.reply(replyToken, Arrays.asList(
                                    new TextMessage("ถ้ายังไม่จบพิม ต่อ"),
                                    new TextMessage("ถ้าจบแล้วพิม @END")
                            ));
                        });
            }
        }else if (hasText3 == true){
            if(userId != null) {
                lineMessagingClient.getProfile(userId)
                        .whenComplete((profile, throwable) -> {
                            if (throwable != null) {
                                this.replyText(replyToken, throwable.getMessage());
                                return;
                            }
                            this.reply(replyToken, Arrays.asList(
                                    new TextMessage("ระบบได้ทำการบัญทึกข้อมูลแล้ว"),
                                    new TextMessage("เลขยืนยัน E1024402")
                            ));
                        });
            }
        }else if (hasText4 == true){
            if(userId != null) {
                lineMessagingClient.getProfile(userId)
                        .whenComplete((profile, throwable) -> {
                            if(throwable != null) {
                                this.replyText(replyToken, throwable.getMessage());
                                return;
                            }
                            this.reply(replyToken, Arrays.asList(
                                    new TextMessage("Display name: " +
                                            profile.getDisplayName()),
                                    new TextMessage("Status message: " +
                                            profile.getStatusMessage()),
                                    new TextMessage("User ID: " +
                                            profile.getUserId())
                            ));
                        });
            }
        }
        else{
            this.reply(replyToken, Arrays.asList(
                    new TextMessage("กรุณาแจ้งผู้รับผิดชอบตัวอย่าง @N")
            ));
        }
    }

    private void handleTextContent1(String replyToken, Event event,
                                   TextMessageContent content) {
        String text = content.getText();

        log.info("Got text message from %s : %s", replyToken, text);

        switch (text) {
            case "Profile": {
                String userId = event.getSource().getUserId();
                if(userId != null) {
                    lineMessagingClient.getProfile(userId)
                            .whenComplete((profile, throwable) -> {
                                if(throwable != null) {
                                    this.replyText(replyToken, throwable.getMessage());
                                    return;
                                }
                                this.reply(replyToken, Arrays.asList(
                                        new TextMessage("Display name: " +
                                                        profile.getDisplayName()),
                                        new TextMessage("Status message: " +
                                                        profile.getStatusMessage()),
                                        new TextMessage("User ID: " +
                                                        profile.getUserId())
                                ));
                            });
                }
                break;
            }
            default:
                log.info("Return echo message %s : %s", replyToken, text);
                this.replyText(replyToken, text);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken is not empty");
        }

        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "...";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse response = lineMessagingClient.replyMessage(
                    new ReplyMessage(replyToken, messages)
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}