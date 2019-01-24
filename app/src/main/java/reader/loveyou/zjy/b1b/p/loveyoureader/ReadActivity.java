package reader.loveyou.zjy.b1b.p.loveyoureader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import reader.loveyou.zjy.b1b.p.loveyoureader.contact.BookReadContact;

public class ReadActivity extends AppCompatActivity implements BookReadContact.CView {

    private PagerAdapter mAdapter;
    List<View> daa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        TextView tvTitile = findViewById(R.id.activity_read_tv_title);
        String txtFilePath = getIntent().getStringExtra("filePath");
        if (!new File(txtFilePath).exists()) {
            showAlertDg("当前文件不存在或已被删除");
        }
        final ViewPager vp = findViewById(R.id.activity_read_vp);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == vp.getAdapter().getCount() - 1) {
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        daa = new ArrayList<>();
        mAdapter = new ReadPagerAdapter(daa);
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
    public void readFinish() {

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
    }

    public void showAlertDg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(msg);
        AlertDialog mDg = builder.create();
        mDg.show();
    }
}
