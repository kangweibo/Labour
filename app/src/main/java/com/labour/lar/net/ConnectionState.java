package com.labour.lar.net;

import android.content.Context;
import android.widget.Toast;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;

public class ConnectionState implements ConnectionClassManager.ConnectionClassStateChangeListener {
    private Context context;

    public ConnectionState(Context context){
        this.context = context;
    }

    @Override
    public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
        if(bandwidthState.equals(ConnectionQuality.UNKNOWN)){
            Toast.makeText(context,"网络有问题",Toast.LENGTH_SHORT).show();
        } else if(bandwidthState.equals(ConnectionQuality.POOR)){
            Toast.makeText(context,"网络较差",Toast.LENGTH_SHORT).show();
        } else if(bandwidthState.equals(ConnectionQuality.MODERATE)){
            Toast.makeText(context,"网络还行",Toast.LENGTH_SHORT).show();
        } else if(bandwidthState.equals(ConnectionQuality.GOOD)){
            Toast.makeText(context,"网络很好",Toast.LENGTH_SHORT).show();
        } else if(bandwidthState.equals(ConnectionQuality.EXCELLENT)){
            Toast.makeText(context,"网络超级好",Toast.LENGTH_SHORT).show();
        }
    }

    public void register(){
        ConnectionClassManager.getInstance().register(this);
    }

    public void remove(){
        ConnectionClassManager.getInstance().remove(this);
    }

    public void startSampling(){
        DeviceBandwidthSampler.getInstance().startSampling();
    }

    public void stopSampling(){
        DeviceBandwidthSampler.getInstance().stopSampling();
    }

    public void getCurrentBandwidthQuality(){
        ConnectionQuality cq = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
        onBandwidthStateChange(cq);
    }
}
