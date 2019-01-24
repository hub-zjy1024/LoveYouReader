package reader.loveyou.zjy.b1b.p.loveyoureader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import reader.loveyou.zjy.b1b.p.loveyoureader.adapter.MainBookAdapter;
import reader.loveyou.zjy.b1b.p.loveyoureader.entity.Book;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.PermissionCheckerActivity;
import reader.loveyou.zjy.b1b.p.loveyoureader.utils.SettingDataUtil;

public class MainActivity extends PermissionCheckerActivity {

    private Context mContext;
    private List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        GridView mShujiaGrid = findViewById(R.id.main_shujia_grid);
        Button btnAdd = findViewById(R.id.main_shujia_btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermitted(Manifest.permission.WRITE_EXTERNAL_STORAGE) && isPermitted(Manifest
                        .permission.READ_EXTERNAL_STORAGE)) {
                    addNewBook();
                } else {
                    custReqPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission
                            .WRITE_EXTERNAL_STORAGE}, new AllowListner() {
                        @Override
                        public void permissionResult(String[] permissions, boolean[] isAllow) {
                            if (isAllow[0] && isAllow[1]) {
                                addNewBook();
                            } else {
                                Toast.makeText(mContext, "请授予权限，否则无法添加书到书架", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        mBooks = new ArrayList<>();
        mShujiaGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoRead(mBooks.get(position).filePath);
            }
        });
        initDataList();
        MainBookAdapter mainBookAdapter = new MainBookAdapter(this, mBooks, R.layout.item_main_book);
        mShujiaGrid.setAdapter(mainBookAdapter);
       // gotoRead("/storage/emulated/0/ucdownloads/人道至尊.txt");
        gotoRead("/storage/emulated/0/ucdownloads/13434.txt");
    }

    void initDataList() {
        List<Book> books = SettingDataUtil.readBooks(mContext);
        mBooks.addAll(books);
    }

    void gotoRead(String filePath) {
        Intent mIntent = new Intent(this, ReadActivity.class);
        mIntent.putExtra("filePath", filePath);
        startActivity(mIntent);
    }

    public void addNewBook() {
        Intent mIntent = new Intent(this, SearchBookActivity.class);
        startActivity(mIntent);
    }
}
