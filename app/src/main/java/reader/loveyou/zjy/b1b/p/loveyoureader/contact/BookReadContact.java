package reader.loveyou.zjy.b1b.p.loveyoureader.contact;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import reader.loveyou.zjy.b1b.p.loveyoureader.entity.PageInfo;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.TaskExecutor;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.TxtReader;

/**
 Created by 张建宇 on 2019/1/23. */
public class BookReadContact {


    public interface CView {
        void update(List<File> files);

        void waitFinish(String msg);

        void readBefore(String content);

        void preReadedPages(List<PageInfo> mpages);

        void readFinish(List<PageInfo> mdatas);

    }

    public static class DataProvider {
        private Context mContext;

        public DataProvider(Context mContext) {
            this.mContext = mContext;
        }

        public interface SearchListener {
            void transForm(int size);
        }
    }

    public static class Presenter {
        private BookReadContact.CView CView;
        private BookReadContact.DataProvider provider;

        private android.os.Handler mhandelr;
        private Context mContext;
        private TaskExecutor mExecutor = TaskExecutor.getExcutor();

        public Presenter(BookReadContact.CView CView, Context mContext) {
            this.CView = CView;
            this.mContext = mContext;
            mhandelr = new android.os.Handler();
            this.provider = new BookReadContact.DataProvider(mContext);
        }

        public void divideTxtToPages(final String txtFilePath) {
            List<View> daa = new ArrayList<>();
            final TxtReader reader = new TxtReader(txtFilePath);

            Runnable run = new Runnable() {
                @Override
                public void run() {
                    long time1 = System.currentTimeMillis();
                    final List<PageInfo> mdatas = reader.read2(txtFilePath, new TxtReader.ReadListenter() {


                        @Override
                        public void readAt(int position, String beforeTxt) {
                            CView.readBefore(beforeTxt);
                        }

                        @Override
                        public void postPrePages(final List<PageInfo> pages) {
                            mhandelr.post(new Runnable() {
                                @Override
                                public void run() {
                                    CView.preReadedPages(pages);
                                }
                            });
                        }
                    });
                    mhandelr.post(new Runnable() {
                        @Override
                        public void run() {
                            CView.readFinish(mdatas);
                        }
                    });
                    Log.e("zjy", "BookReadContact->run(): TotalTimeRead==" + txtFilePath + "\ttime=" + (System
                            .currentTimeMillis() - time1) / 1000f);
                }
            };
            mExecutor.executeTask(run);
        }
    }
}
