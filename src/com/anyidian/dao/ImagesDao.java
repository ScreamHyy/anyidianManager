package com.anyidian.dao;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.web.multipart.MultipartFile;

import com.anyidian.model.Constans;

public class ImagesDao extends JdbcDaoSupport {

	private String sql;

	/**
	 * 保存图片到本地
	 * @param request
	 * @param file
	 * @param imagesList
	 * @param mallId 
	 * @return
	 */
	public String saveFile(HttpServletRequest request, MultipartFile file, List<String> imagesList, String mallId) {
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				String fileName = file.getOriginalFilename();
				String stroreName = System.currentTimeMillis() + ""
						+ fileName.substring(fileName.lastIndexOf("."));
				String imageUrl = Constans.MY_IMAGEURL + stroreName;

				String filePath = request.getSession().getServletContext().getRealPath("/")
						+ "resources/images/" + stroreName;
				File saveDir = new File(filePath);
				if (!saveDir.getParentFile().exists()) {
					saveDir.getParentFile().mkdirs();
				}
				// 转存文件
				file.transferTo(saveDir);
				//boolean success = insertImages(mallId, imageUrl);
				return imageUrl;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * 添加图片
	 * @param mallId
	 * @param imageUrl
	 * @return
	 */
	public boolean insertImages(String mallId, String imageUrl) {
		sql = "insert into images(imageId,image) values(?,?)";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{mallId, imageUrl});
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除图片
	 * @param imageId
	 * @return
	 */
	public boolean deleteImages(String imageId) {
		sql = "delete from images where imageId in("+imageId+")";
		int resultInt = getJdbcTemplate().update(sql, new Object[]{}); 
		if (resultInt > 0) {
			return true;
		}
		return false;
	}

}
