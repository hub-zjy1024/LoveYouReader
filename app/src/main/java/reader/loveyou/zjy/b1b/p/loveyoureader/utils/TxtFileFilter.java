package reader.loveyou.zjy.b1b.p.loveyoureader.utils;

import java.io.File;

/**
 Created by 张建宇 on 2019/1/22. */
public class TxtFileFilter implements IFileFilter {
    /**
     字节
     */
    private long size = -1;
    public TxtFileFilter() {
        this(-1);
    }
    public TxtFileFilter(int size) {
        this.size = size;
    }

    public boolean filter(File f) {
        if (!f.getName().endsWith(".txt")) {
            return false;
        }
        if (size > 0) {
            if (size > f.length()) {
                return false;
            }
        }
        return true;
    }
}
