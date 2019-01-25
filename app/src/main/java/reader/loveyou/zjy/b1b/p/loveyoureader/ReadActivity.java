package reader.loveyou.zjy.b1b.p.loveyoureader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import reader.loveyou.zjy.b1b.p.loveyoureader.contact.BookReadContact;
import reader.loveyou.zjy.b1b.p.loveyoureader.entity.PageInfo;

public class ReadActivity extends AppCompatActivity implements BookReadContact.CView {

    private PagerAdapter mAdapter;

    private List<PageInfo> preCachePages;
    List<View> daa;
    private ViewPager vp;
    int pageStrCounts = 300;
    String txtFilePath ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        TextView tvTitile = findViewById(R.id.activity_read_tv_title);
         txtFilePath = getIntent().getStringExtra("filePath");
        if (!new File(txtFilePath).exists()) {
            showAlertDg("当前文件不存在或已被删除");
        }
        vp = findViewById(R.id.activity_read_vp);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == vp.getAdapter().getCount() - 1) {
//                    pagesToViewPager(moveSharedPreferencesFrom());
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        preCachePages = new ArrayList<>();
        daa = new ArrayList<>();
        mAdapter = new ReadPagerAdapter(daa);
        vp.setAdapter(mAdapter);
        BookReadContact.Presenter mPresenter = new BookReadContact.Presenter(this, this);
                mPresenter.divideTxtToPages(txtFilePath);
    }


    @Override
    public void update(List<File> files) {

    }

    @Override
    public void waitFinish(String msg) {

    }

    @Override
    public void readBefore(String content) {

    }

    @Override
    public void preReadedPages(List<PageInfo> mpages) {
        pagesToViewPager(mpages, 0);
    }

    public void pagesToViewPager(List<PageInfo> mpages, int position) {
        List<View> views = new ArrayList<>();
        PageInfo pageInfo = mpages.get(position);
        String content = pageInfo.getContent();
        int length = content.length();
        int index = 0;
        double totalPages = Math.ceil(length / 1f / pageStrCounts);
        int mCounts = (int) totalPages;
        int poiIndex = 1;
        while (index < length) {
            String nContent = "";
            int endIndex = index + pageStrCounts;
            if (endIndex> length) {
                endIndex = length;
            }
            nContent = content.substring(index, endIndex);
            View mView = LayoutInflater.from(this).inflate(R.layout.item_read_page, null);
            TextView tvTitle = mView.findViewById(R.id.item_read_title);
            TextView tvPosition = mView.findViewById(R.id.item_read_position);
            tvPosition.setText(poiIndex + "/" + mCounts);
            if (index == 0) {
                tvTitle.setText(txtFilePath.substring(txtFilePath.lastIndexOf("/") + 1));
            }else{
                tvTitle.setText(pageInfo.getTitle());
            }
            TextView txtContent = mView.findViewById(R.id.item_read_content);
            txtContent.setText(nContent);
            views.add(mView);
            index = endIndex;
            poiIndex++;
        }
        daa.addAll(views);
        mAdapter.notifyDataSetChanged();
    }

    public void readFinish(List<PageInfo> mdatas) {
        preCachePages.clear();
        Log.e("zjy", "ReadActivity->readFinish(): mdatasSize==" + mdatas.size());
        Log.e("zjy", "ReadActivity->readFinish(): lastTitle==" + mdatas.get(mdatas.size() - 1).getTitle());
        for (int i = preCachePages.size(); i < mdatas.size(); i++) {
            preCachePages.add(mdatas.get(i));
        }
    }


    public static class ReadPagerAdapter extends PagerAdapter {

        private List<View> viewList;

        public ReadPagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(viewList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = viewList.get(position);
            if (view.getParent() != null) {
                container.removeView(view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    public void showAlertDg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(msg);
        AlertDialog mDg = builder.create();
        mDg.show();
    }
}
