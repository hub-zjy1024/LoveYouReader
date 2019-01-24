package reader.loveyou.zjy.b1b.p.loveyoureader.contact;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import reader.loveyou.zjy.b1b.p.loveyoureader.utils.IFileFilter;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.SettingDataUtil;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.TaskExecutor;

/**
 Created by 张建宇 on 2019/1/22. */
public class BookSearchContact {
    public interface CView {
        void update(List<File> files);

        void waitFinish(String msg);

    }

    public static class DataProvider {
        private Context mContext;

        public DataProvider(Context mContext) {
            this.mContext = mContext;
        }

        public interface SearchListener {
            void transForm(int size);
        }
        public void addBooks(List<File> file) {

        }
        public boolean checkFileImported(File f) {
            return SettingDataUtil.checkFileImported(mContext, f);
        }

        public List<File> traverseFolder1(File file, IFileFilter mFilter) {
            return traverseFolder1(file, mFilter, null);
        }

        public List<File> traverseFolder1(File file, IFileFilter mFilter, SearchListener listener) {
            List<File> mList = new ArrayList<>();
            int fileNum = 0, folderNum = 0;
            if (file.exists()) {
                LinkedList<File> dirList = new LinkedList<File>();
                File[] files = file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        dirList.add(file2);
                        folderNum++;
                    } else if (mFilter.filter(file2)) {
                        fileNum++;
                        mList.add(file2);
                    }
                }
                File temp_file;
                while (!dirList.isEmpty()) {
                    temp_file = dirList.removeFirst();
                    int leftSize = dirList.size();
                    if (listener != null) {
                        listener.transForm(leftSize);
                    }
                    files = temp_file.listFiles();
                    for (File file2 : files) {
                        if (file2.isDirectory()) {
                            dirList.add(file2);
                            folderNum++;
                        } else if (mFilter.filter(file2)) {
                            fileNum++;
                            mList.add(file2);
                        }
                    }
                }
            } else {
                Toast.makeText(mContext, "sd卡不存在", Toast.LENGTH_SHORT).show();
            }
            Log.e("zjy", "SearchBookActivity->traverseFolder1(): ==" + "文件夹共有:" + folderNum + ",txt文件共有:" + fileNum);
            return mList;
        }
    }

    public static class Presenter {
        private CView CView;
        private DataProvider provider;

        private android.os.Handler mhandelr;
        private Context mContext;
        private TaskExecutor mExecutor = TaskExecutor.getExcutor();

        public Presenter(BookSearchContact.CView CView, Context mContext) {
            this.CView = CView;
            this.mContext = mContext;
            mhandelr = new android.os.Handler();
            this.provider = new DataProvider(mContext);
        }

        public boolean checkFileImported(File f) {
            return provider.checkFileImported(f);
        }

        public void addBooks(List<File> file) {

        }

        public void search(final File file, final IFileFilter mFilter) {
            CView.waitFinish("正在查找可以阅读的txt文件");
            mExecutor.executeTask(new Runnable() {
                @Override
                public void run() {
                    final List<File> txtFiles = provider.traverseFolder1(file, mFilter, new DataProvider.SearchListener() {
                        @Override
                        public void transForm(final int size) {
                            mhandelr.post(new Runnable() {
                                @Override
                                public void run() {
                                    CView.waitFinish("剩余待扫描的文件夹个数为：" + size);
                                }
                            });
                        }
                    });
                    mhandelr.post(new Runnable() {
                        @Override
                        public void run() {
                            CView.update(txtFiles);
                        }
                    });
                }
            });
        }
    }
}
