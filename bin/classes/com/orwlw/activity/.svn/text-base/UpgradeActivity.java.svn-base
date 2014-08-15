package com.orwlw.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.com.utils.Utils;

import com.orwlw.WorkManager_Driver.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpgradeActivity extends Activity implements OnClickListener {
	public ProgressDialog pBar;
	public ProgressDialog pd; // 进度条对话框 　　
	private Button updatenow;
	private Button updatelater;
	private TextView updatecontentTextView;
	private TextView updatetimeTextView;
	private TextView versonnameTextView;

	private LinearLayout upgradelayout;

	public String newVerName = "";
	public String newUpdateShow = "";
	public String updatetime = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.upgrade);

			upgradelayout = (LinearLayout) findViewById(R.id.upgradelayout);
			updatenow = (Button) findViewById(R.id.button1);
			updatelater = (Button) findViewById(R.id.button2);
			updatenow.setOnClickListener(this);
			updatelater.setOnClickListener(this);

			updatecontentTextView = (TextView) findViewById(R.id.soft_upgradecontent);
			updatetimeTextView = (TextView) findViewById(R.id.soft_upgradedate);
			versonnameTextView = (TextView) findViewById(R.id.versonname);

			Intent intent = getIntent();
			if (intent.getStringExtra("newVerName") != null
					&& intent.getStringExtra("newUpdateShow") != null) {
				newVerName = intent.getStringExtra("newVerName");
				newUpdateShow = intent.getStringExtra("newUpdateShow");
				updatetime = intent.getStringExtra("updatetime");
			}
			versonnameTextView.setText("版本：" + newVerName);
			updatecontentTextView.setText(newUpdateShow);
			updatetimeTextView.setText("版本发布日期：" + updatetime
					+ "\n升级后会覆盖本地数据，如果有未提交的照片请点击稍后升级！");

		} catch (Exception e) {
			Log.i("错误：", e.getMessage() + "");
		}

	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	public void onClick(View v) {
		if (v == this.updatenow) {
			try {
				pBar = new ProgressDialog(UpgradeActivity.this);
				pBar.setTitle("下载提示");
				pBar.setMessage("下载中..");
				pBar.setCancelable(false);
				pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 进度条
				if (android.os.Environment.MEDIA_MOUNTED
						.equals(android.os.Environment
								.getExternalStorageState())) {
					downFile(Utils
							.getUpgradeURL(Utils.APP, Utils.UPDATE_SAVENAME));
					// upgradelayout.setVisibility(4);
					// upgradelayout.getBackground().setAlpha(0);//
				} else {
					Toast toast = Toast.makeText(UpgradeActivity.this,
							"无有效的SD卡,请插入SD卡后升级", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			} catch (Exception e) {
				Toast toast = Toast.makeText(UpgradeActivity.this,
						e.getMessage(), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		}
		if (v == this.updatelater) {

			finish();
		}

	}

	// 下载url
	void downFile(final String url) {
		pBar.show();
		new Thread() {
			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();

				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {

					response = client.execute(get);
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						long length = entity.getContentLength();
						InputStream is = entity.getContent();// 通过HttpEntiy.getContent得到一个输入流
						pBar.setMax((int) length);
						FileOutputStream fileOutputStream = null;
						if (is != null) {

							File file = new File(Environment
									.getExternalStorageDirectory()
									.getAbsolutePath(), Utils.UPDATE_SAVENAME);

							if (file.exists()) {
								if (file.delete()) {
									file.createNewFile();
								}

							} else {
								file.createNewFile();
							}
							fileOutputStream = new FileOutputStream(file);

							byte[] buf = new byte[1024000];
							int ch = -1;
							int count = 0;
							while ((ch = is.read(buf)) != -1) {
								fileOutputStream.write(buf, 0, ch);
								count += ch;
								pBar.setProgress(count);
								if (length > 0) {
								}
							}

						}
						fileOutputStream.flush();
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}

						pBar.cancel();
						update();
					} else {
						pBar.cancel();
					}

				} catch (ClientProtocolException e) {
					pBar.cancel();
					e.printStackTrace();
				} catch (IOException e) {
					pBar.dismiss();
					e.printStackTrace();
				}

			}

		}.start();

	}

	void update() {

		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// intent.setDataAndType(Uri.fromFile(new
		// File(Environment.getExternalStorageDirectory().getAbsolutePath(),
		// Config.UPDATE_SAVENAME)), "application/vnd.android.package-archive");
		// startActivity(intent);

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* 调用getMIMEType()来取得MimeType */

		/* 设置intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory().getAbsolutePath(),
				Utils.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
		finish();

	}
}
