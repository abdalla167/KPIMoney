package com.kpi.money.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
public class CheckVpn {
    public CheckVpn() {
    }
    public static boolean  checkVpn(Context context)
    {
        ConnectivityManager m_ConnectivityManager ;

        m_ConnectivityManager= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        List<NetworkInfo> connectedNetworks    = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= 21)
        {
            Network[] networks = m_ConnectivityManager.getAllNetworks();

            for (Network n : networks)
            {
                NetworkInfo ni = m_ConnectivityManager.getNetworkInfo(n);
                if (ni.isConnectedOrConnecting())
                {
                    connectedNetworks.add(ni);
                }
            }
        }
        else
        {
            NetworkInfo[] nis = m_ConnectivityManager.getAllNetworkInfo();
            for (NetworkInfo ni : nis)
            {
                if (ni.isConnectedOrConnecting())
                {
                    connectedNetworks.add(ni);
                }
            }
        }
        boolean bHasVPN = false;
        if (Build.VERSION.SDK_INT >= 21)
        {
            for (NetworkInfo ni : connectedNetworks)
            {
                bHasVPN |= (ni.getType() == ConnectivityManager.TYPE_VPN);
            }
        }
    return bHasVPN;
    }

}
