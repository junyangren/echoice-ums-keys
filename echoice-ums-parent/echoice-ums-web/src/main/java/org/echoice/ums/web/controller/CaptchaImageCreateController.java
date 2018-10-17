package org.echoice.ums.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.echoice.ums.captcha.GenerateValidImage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CaptchaImageCreateController {
	public static final String KEY_CAPTCHA = "captcha";
	
    @RequestMapping("/captcha.jpg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        // 设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        // 不缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        try {

            HttpSession session = request.getSession();

            String captcha = GenerateValidImage.getImage(120, 40, 10, response.getOutputStream());
            session.removeAttribute(KEY_CAPTCHA);
            session.setAttribute(KEY_CAPTCHA, captcha);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
