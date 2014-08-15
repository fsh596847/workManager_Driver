package cn.com.utils;

import java.util.LinkedList;
import java.util.List;

import com.orwlw.bean.Localdata;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 完全退出程序解决方案<br>
 * 
 * @author - 2012-2-07 吴鑫 创建文件<br>
 * 
 * @version - 2012-2-07 v1 初始版本<br>
 * 
 * @par 版权所有<br>
 *      北京修远谊和科技发展有限公司<br>
 */
public class ExitApplication extends Application {
	private List<Activity> activityList = new LinkedList<Activity>();
	public static ExitApplication instance;
	private static String personcode;// 人员编码
	private static String customcode;// 客户编码
	private static String commname;// 商品名称

	@Override
	public void onCreate() {
		super.onCreate();

	}

	// 全局人员编码-------------------
	public String getpersoncode() {
		return personcode;
	}

	public void setpersoncode(String s) {
		this.personcode = s;
	}

	// ---------------------------
	// 全局客户编码-----------------------
	public String getcustomcode() {
		return customcode;
	}

	public void setcustomcode(String s) {
		this.customcode = s;
	}

	// -----------------------------------
	// 全局商品名称-----------------------
	public String getcommname() {
		return commname;
	}

	public void setccommname(String s) {
		this.commname = s;
	}

	private ExitApplication() {
	}

	// 单例模式中获取唯一的MyApplication实例
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;

	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		try {
			for (Activity activity : activityList) {
				activity.finish();
			}

			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
