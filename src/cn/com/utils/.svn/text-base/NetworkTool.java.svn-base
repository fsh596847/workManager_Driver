package cn.com.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class NetworkTool
{
	// 根据url指定的json文件获得内容字符串
	public static String getContent(String url) throws Exception
	{
		// StringBuilder sb = new StringBuilder();

		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams, 30000);
		HttpResponse response = client.execute(new HttpGet(url));
		String strResult = EntityUtils.toString(response.getEntity(), "Unicode");
		return strResult;
		// HttpEntity entity = response.getEntity();
		// if (entity != null)
		// {
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(entity.getContent(), "UTF-8"), 8192);
		//
		// String line = null;
		// while ((line = reader.readLine()) != null)
		// {
		// sb.append(line);
		// }
		// reader.close();
		// }
		// return sb.toString();
	}
}
