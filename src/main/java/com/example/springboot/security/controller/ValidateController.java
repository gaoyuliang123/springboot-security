package com.example.springboot.security.controller;

import com.example.springboot.security.common.Constant;
import com.example.springboot.security.validate.code.ImageCode;
import com.example.springboot.security.validate.smscode.SmsCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @decription: 验证码
 * @author: admin
 * @date: 2019-08-14 22:22
 */
@Controller
@Slf4j
public class ValidateController {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 创建图片验证码
     * @param request
     * @param response
     */
    @GetMapping("/code/image")
    public void getImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = createImageCode();
        ImageCode imageCodeRedis = new ImageCode(null, imageCode.getCode(), imageCode.getExpireTime());
        sessionStrategy.setAttribute(new ServletWebRequest(request), Constant.SESSION_KEY_IMAGE_CODE, imageCodeRedis);
        ImageIO.write(imageCode.getImage(), "jpg", response.getOutputStream());
    }

    @GetMapping("/code/sms")
    public void getSmsCode(HttpServletRequest request, HttpServletResponse response, String mobile) {
        SmsCode smsCode = createSMSCode();
        sessionStrategy.setAttribute(new ServletWebRequest(request), Constant.SESSION_KEY_SMS_CODE + mobile, smsCode);
        // 发送验证码
        log.info("手机号={}已发送验证码={}有效时间为60秒", mobile, smsCode.getCode());
    }

    /**
     * 生成图片验证码
     * @return
     */
    private ImageCode createImageCode() {
        // TODO 做成可配置的
        int width = 100; // 验证码图片宽度
        int height = 36; // 验证码图片长度
        int length = 4; // 验证码位数
        int expireIn = 60; // 验证码有效时间 60s

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        return new ImageCode(image, sRand.toString(), expireIn);
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 生成短信验证码
     * @return
     */
    public SmsCode createSMSCode() {
        // TODO 数字位数及失效时间做成可配置的
        return new SmsCode(RandomStringUtils.randomNumeric(6), 60);
    }
}
