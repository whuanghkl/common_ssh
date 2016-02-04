package com.common.web.servlet.upload;

import java.io.File;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.common.util.WebServletUtil;

public abstract class SUploadServlet extends HttpServlet{
	private static final long serialVersionUID = -4970957022658370016L;

	/***
	 * 
	 * @param request
	 * @param uploadFolderName
	 * @return
	 */
	protected final String getUploadPath(HttpServletRequest request,String uploadFolderName){
		//project name 
		String projectName = request.getContextPath();// value:/shop_goods
		String uploadPath =WebServletUtil.getUploadedPath(uploadFolderName, projectName, this.getServletContext());
		return uploadPath;
	}
	/***
	 * 
	 * @param request
	 * @param uploadFolderName  :eg.WEB-INF/download
	 * @param newFileName
	 * @return
	 */
	protected final File getUploadedFilePath(HttpServletRequest request,String uploadFolderName, String newFileName){
		String realpath=getUploadPath(request, uploadFolderName);
		File savefile = new File(new File(realpath), newFileName);
		//如果父目录不存在，则创建父目录
		if (!savefile.getParentFile().exists()) {
			savefile.getParentFile().mkdirs();
		}
		return savefile;
	}
}
