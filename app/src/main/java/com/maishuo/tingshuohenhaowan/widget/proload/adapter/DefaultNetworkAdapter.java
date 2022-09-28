package com.maishuo.tingshuohenhaowan.widget.proload.adapter;

public class DefaultNetworkAdapter implements INetworkAdapter {
    @Override
    public boolean canPreLoadIfNotWifi() {
        return false;
    }
}
