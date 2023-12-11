package com.iamxgw.server.asyncpro;

import com.iamxgw.vo.MyMessage;

public interface ITaskProcessor {
    Runnable execAsyncTask(MyMessage msg);
}
