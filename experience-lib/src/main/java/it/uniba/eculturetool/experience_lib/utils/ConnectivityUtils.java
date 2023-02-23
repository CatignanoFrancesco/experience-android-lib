package it.uniba.eculturetool.experience_lib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtils {
    private ConnectivityUtils() {}

    /**
     * Metodo che verifica se è presente la connessione ad Internet.
     * @param context contesto dell'app android.
     * @return true se la connessione è presente, false altrimenti.
     */
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isAvailable() && ni.isConnected();
    }
}
