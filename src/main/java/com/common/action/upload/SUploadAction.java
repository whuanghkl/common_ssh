package com.common.action.upload;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.common.util.WebServletUtil;
import com.opensymphony.xwork2.ActionSupport;
/***
 * 下载、上传的action 父类.
 * 
 * @author huangwei
 *
 */
public class SUploadAction extends ActionSupport {
	private static final long serialVersionUID = 636627961411821222L;

	/***
	 * 获取上传文件的真正路径.
	 * 
	 * @param uploadFolderName
	 * @param newFileName
	 * @return
	 */
	public final File getUploadedFilePath(String uploadFolderName,
			String newFileName) {
		ServletContext sContext = ServletActionContext.getServletContext();

		String projectName = ServletActionContext.getRequest().getContextPath();// value:/shop_goods
		String realpath = WebServletUtil.getUploadedPath(uploadFolderName,
				projectName, sContext);

		// D:\xxx\eclipse\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\shop_goods\images
		// D:\xxx\eclipse\workspace\shop_goods\ upload

		// D:\apache-tomcat-6.0.18\webapps\struts2_upload\images
		// System.out.println("realpath: "+realpath);
		if (newFileName == null) {
			return new File(realpath);
		} else {
			File savefile = new File(new File(realpath), newFileName);
			if (!savefile.getParentFile().exists()) {
				savefile.getParentFile().mkdirs();
			}
			return savefile;
		}
		
	}
	/***
	 * 获取上传的目录.
	 * 
	 * @param uploadFolderName
	 * @return
	 */
	public final File getRealUploadedFolder(String uploadFolderName){
		return getUploadedFilePath(uploadFolderName, null);
	}

}
