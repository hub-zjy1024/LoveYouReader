package reader.loveyou.zjy.b1b.p.loveyoureader.adapter;

import android.content.Context;

import java.util.List;

import reader.loveyou.zjy.b1b.p.loveyoureader.R;
import reader.loveyou.zjy.b1b.p.loveyoureader.entity.Book;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.adapter.CommonAdapter;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.adapter.ViewHolder;

/**
 Created by 张建宇 on 2019/1/22. */
public class MainBookAdapter extends CommonAdapter<Book> {
    public MainBookAdapter(Context context, List<Book> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Book item) {
        helper.setText(R.id.item_main_book_name1, item.filePath.substring(item.filePath.lastIndexOf("/") + 1));
        helper.setText(R.id.item_main_book_name2, item.filePath.substring(item.filePath.lastIndexOf("/") + 1));
    }
}
