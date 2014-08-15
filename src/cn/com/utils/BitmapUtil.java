package cn.com.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;

public class BitmapUtil {

	private static final String path="/sdcard/img";
	
	/**
	 * 保存bitmap到本地
	 * @param bitmap
	 * @param path
	 * @return
	 */
	/*public static boolean saveBitmap(Bitmap bitmap, String path) {
		try {
			File file = new File(path);
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			return b;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}*/
	
	/**
	 * 
	 * @param bitmap
	 * @param bitmapName
	 * @return
	 */
	public static String saveBitmap(Bitmap bitmap,String bitmapName) {
		try {
			File file = new File(path);
			
			if (!file.exists()) {
				file.mkdirs();
				System.out.println("sdcard/img文件夹建立");
			}
			
			String imgName=bitmapName.substring(bitmapName.lastIndexOf("/"));
			String imgName1=imgName.substring(0, imgName.lastIndexOf("."));
			String imgNameEnd=imgName1+getCharacterAndNumber();
			
			File imageFile = new File(file, imgNameEnd + ".png");
			boolean a=imageFile.createNewFile(); 
			if(a){
				System.out.println("sdcard/img"+imgNameEnd + ".png"+"文件夹建立");
			}
			FileOutputStream fos = new FileOutputStream(imageFile);
			boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			
			
			return path+imgNameEnd+".png";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取本地时间用于本地图片的命名
	 * @return
	 */
	public static String getCharacterAndNumber() { 
		
		String rel=""; 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss"); 
		Date curDate = new Date(System.currentTimeMillis()); 
		rel = formatter.format(curDate); 
		return rel; 
		
	}
	
	
	public static byte[] getImage(String path) {
		URL url;
		byte[] b = null;
		try {
			url = new URL(path); // 设置URL
			HttpURLConnection con;
			con = (HttpURLConnection) url.openConnection(); // 打开连接

			con.setRequestMethod("GET"); // 设置请求方法
			// 设置连接超时时间为5s
			con.setConnectTimeout(5000);
			InputStream in = con.getInputStream(); // 取得字节输入流
			b = readInputStream(in);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return b; // 返回byte数组

	}

	public static byte[] readInputStream(InputStream in) throws Exception {
		int len = 0;
		byte buf[] = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len); // 把数据写入内存
		}
		out.close(); // 关闭内存输出流
		return out.toByteArray(); // 把内存输出流转换成byte数组
	}
	
	/**
	 * 删除本地文件
	 * @param file
	 */
	public static void deleteFile(File file){
		if(file.exists()){
			if(file.isFile()){//如果是个文件夹
				file.delete();
			}else if(file.isDirectory()){//如果是个文件夹
				File[] files=file.listFiles();
				for(int i=0;i<files.length;i++){
					deleteFile(files[i]);
				}
				file.delete();
			}
		}
	}

	
}
