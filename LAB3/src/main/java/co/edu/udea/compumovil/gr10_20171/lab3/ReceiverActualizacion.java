package co.edu.udea.compumovil.gr10_20171.lab3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by CJMO on 4/04/2017.
 */

public class ReceiverActualizacion extends BroadcastReceiver {

    public ReceiverActualizacion(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "time is up!!!!.",
                Toast.LENGTH_LONG).show();
        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
