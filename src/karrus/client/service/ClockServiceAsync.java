package karrus.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClockServiceAsync
{
    void start(AsyncCallback<Void> anAsyncCallback);
}
