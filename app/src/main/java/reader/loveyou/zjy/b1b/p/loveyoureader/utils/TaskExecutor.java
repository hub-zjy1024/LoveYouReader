package reader.loveyou.zjy.b1b.p.loveyoureader.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 Created by 张建宇 on 2019/1/22. */
public class TaskExecutor {
    static class Temp {
        static TaskExecutor task = new TaskExecutor();
    }

    private static Executor mEngin;
    private TaskExecutor(){
        mEngin = Executors.newCachedThreadPool();
    }

    public static TaskExecutor getExcutor() {
        return Temp.task;
    }

    public void executeTask(Runnable task) {
        mEngin.execute(task);
    }
}
