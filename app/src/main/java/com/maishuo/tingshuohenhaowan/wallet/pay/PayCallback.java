package com.maishuo.tingshuohenhaowan.wallet.pay;

public interface PayCallback {

    void onSuccess(String message);

    void onFailed(String message);

}
