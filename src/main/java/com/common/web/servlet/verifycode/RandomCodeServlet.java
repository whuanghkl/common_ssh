package com.common.web.servlet.verifycode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.io.hw.awt.color.CustomColor;
import com.string.widget.util.ValueWidget;
/***
 * 生成验证码
 * <br>需要在web.xml中配置servlet才能访问
 * <br>
 * <code><servlet>
		<servlet-name>imageVerifyCode</servlet-name>
		<servlet-class>com.common.web.servlet.verifycode.RandomCodeServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>imageVerifyCode</servlet-name>
		<url-pattern>/imageVerifyCode</url-pattern>
	</servlet-mapping></code>
 * @author huangwei
 * @since 20140424
 */
public class RandomCodeServlet extends HttpServlet {

	private static final long serialVersionUID = 2040832365570046619L;
	private int width = 60, height = 20;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String picFormat=request.getParameter("picFormat");
		// 在内存中创建图象
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = img.getGraphics();
		// 设定背景色
		g.setColor(Color.WHITE);

		g.fillRect(0, 0, width, height);

		// 生成随机类
		Random r = new Random();

		// 设定字体
		Font f = new Font("Times New Roman", Font.PLAIN, 18);
		g.setFont(f);

		// 画边框
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);

//		g.setColor(Color.GRAY);
		

		// 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到
		for (int i = 0; i < 160; i++) {
			g.setColor(CustomColor.getLightColor());
			int x = r.nextInt(width);
			int y = r.nextInt(height);
			int x1 = r.nextInt(12);
			int y1 = r.nextInt(12);
			g.drawLine(x, y, x + x1, y + y1);
		}

		StringBuffer sb = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		// 取随机产生的认证码(4位数字)
		for (int i = 0; i < 4; i++) {
			String strRand = String.valueOf(r.nextInt(10));

			red = r.nextInt(110);
			green = r.nextInt(50);
			blue = r.nextInt(50);

			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.setColor(new Color(red, green, blue));

			// 将认证码显示到图象中
			g.drawString(strRand, 13 * i + 6, 16);
			sb.append(strRand);
		}

		HttpSession session = request.getSession();
		session.setAttribute("randomCode", sb.toString());

		// 设置页面不缓存
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		if(ValueWidget.isNullOrEmpty(picFormat)){
			picFormat="jpeg";
		}
		response.setContentType("image/"+picFormat);
//		System.out.println("verify code picFormat:"+picFormat);
		
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(img, picFormat, sos);
		sos.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}


}
