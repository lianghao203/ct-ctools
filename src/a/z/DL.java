package a.z;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DL extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		if (atct.jar != null) {
			atct.jar.check(context, intent);
		}
	}
}
