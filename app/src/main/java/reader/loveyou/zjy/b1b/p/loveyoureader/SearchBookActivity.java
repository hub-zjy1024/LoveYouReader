package reader.loveyou.zjy.b1b.p.loveyoureader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import reader.loveyou.zjy.b1b.p.loveyoureader.contact.BookSearchContact;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.SettingDataUtil;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.TxtFileFilter;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.adapter.CommonAdapter;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.adapter.ViewHolder;

public class SearchBookActivity extends AppCompatActivity implements BookSearchContact.CView {
    private BookSearchContact.Presenter mPresenter;
    private ProgressDialog mPd;
    private MAdapter mAdapter;
    private List<Entity> mData;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        final File sdFile = Environment.getExternalStorageDirectory();
        mPd = new ProgressDialog(this);
        mPresenter = new BookSearchContact.Presenter(this, this);
        // mPresenter.search(sdFile, new TxtFileFilter());
        ListView searchLv = findViewById(R.id.activity_searchbook_searchlist);

        final Button btnAddSelected = findViewById(R.id.activity_searchbook_add_selected);
        Button btnSelectAll = findViewById(R.id.activity_searchbook_selectall);
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                File tempFile = new File(sdFile, "/ucdownloads");
                //                File tempFile = sdFile;
                //                mPresenter.search(tempFile, new TxtFileFilter(10 * 1024));
                File tempFile = sdFile;
                mPresenter.search(tempFile, new TxtFileFilter(0));
                for (int i = 0; i < mData.size(); i++) {
                    Entity temp = mData.get(i);
                    temp.isSeleted = true;
                }
            }
        });
        btnAddSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<File> mBooks = new ArrayList<>();
                for (int i = 0; i < mData.size(); i++) {
                    //                    Book bk = new Book();
                    //                    bk.filePath = mData.get(i).mFile.getAbsolutePath();
                    //                    mBooks.add(bk);
                    Entity temp = mData.get(i);
                    if (temp.isSeleted) {
                        mBooks.add(temp.mFile);
                    }
                }
                File[] arrayBooks = new File[mBooks.size()];
                mBooks.toArray(arrayBooks);
                SettingDataUtil.saveBooks(SearchBookActivity.this, arrayBooks);
            }
        });

        mData = new ArrayList<>();
        mAdapter = new MAdapter(this, mData, R.layout.item_searchbook_lv, mPresenter, new MAdapter.CheckChangeListener() {
            @Override
            public void changed(int count) {
                btnAddSelected.setText("导入书架(" +
                        "" + count +
                        ")");
            }
        });
        searchLv.setAdapter(mAdapter);

        searchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entity nItem = mData.get(position);
                nItem.isSeleted = !nItem.isSeleted;
                //                CheckBox viewById = view.findViewById(R.id.item_sb_select);
                //                viewById.setChecked(nItem.isSeleted);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public static class Entity {
        File mFile;
        long fileSize;
        Date lastModify;
        boolean isSeleted;
    }

    public static class MAdapter extends CommonAdapter<Entity> {

        private BookSearchContact.Presenter mPresenter;

        private int selectCounts = 0;
        private CheckChangeListener cListener;

        interface CheckChangeListener {
            public void changed(int count);
        }

        public MAdapter(Context context, List<Entity> mDatas, int itemLayoutId, BookSearchContact.Presenter mPresenter,
                        CheckChangeListener cListener) {
            super(context, mDatas, itemLayoutId);
            this.mPresenter = mPresenter;
            this.cListener = cListener;
        }

        public MAdapter(Context context, List<Entity> mDatas, int itemLayoutId, BookSearchContact.Presenter mPresenter) {
            super(context, mDatas, itemLayoutId);
            this.mPresenter = mPresenter;
        }

        public MAdapter(Context context, List<Entity> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final Entity item) {
            helper.setText(R.id.item_sb_filename, item.mFile.getName());
            String fileSize = "";
            if (item.fileSize > 1024 * 1024 * 5) {
                Log.e("zjy", "SearchBookActivity->convert(): FilePath==" + item.mFile.getAbsolutePath());
                fileSize = String.format("%.2f", item.fileSize / 1024d / 1024) + "MB";
            } else if (item.fileSize > 1024 * 1024) {
                fileSize = String.format("%.2f", item.fileSize / 1024d / 1024) + "MB";
            } else if (item.fileSize > 1024) {
                fileSize = String.format("%.2f", item.fileSize / 1024d) + "K";
            } else {
                fileSize = "<1K";
            }
            helper.setText(R.id.item_sb_filesize, fileSize);
            helper.setText(R.id.item_sb_parent_dir, ".../" + item.mFile.getParent().substring(item.mFile.getParent()
                    .lastIndexOf("/") + 1));
            View viewStat = helper.getView(R.id.item_sb_stat);


            CheckBox viewSelect = helper.getView(R.id.item_sb_select);
            viewSelect.setOnCheckedChangeListener(null);

            if (mPresenter.checkFileImported(item.mFile)) {
                helper.setText(R.id.item_sb_stat, item.mFile.getName());
                viewStat.setVisibility(View.VISIBLE);
                viewSelect.setVisibility(View.GONE);
            } else {
                helper.setText(R.id.item_sb_stat, "");
                viewStat.setVisibility(View.GONE);
                viewSelect.setVisibility(View.VISIBLE);
            }
            boolean isSeleted = item.isSeleted;
            if (isSeleted) {
                viewSelect.setChecked(true);
            } else {
                viewSelect.setChecked(false);
            }

            viewSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.isSeleted = isChecked;
                    if (isChecked) {
                        selectCounts++;
                    } else {
                        selectCounts--;
                    }
                    if (cListener != null) {
                        cListener.changed(selectCounts);
                    }
                }
            });
        }
    }

    @Override
    public void update(List<File> files) {
        for (File f : files) {
            Entity entity = new Entity();
            entity.mFile = f;
            entity.fileSize = f.length();
            entity.lastModify = new Date(f.lastModified());
            entity.isSeleted = false;
            mData.add(entity);
        }
        mAdapter.notifyDataSetChanged();
        if (mPd != null) {
            mPd.cancel();
        }
    }

    @Override
    public void waitFinish(String msg) {
        mPd.setMessage(msg);
        mPd.show();
    }
}
