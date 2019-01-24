package reader.loveyou.zjy.b1b.p.loveyoureader.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 Created by 张建宇 on 2019/1/23. */
public class TxtReader {
    private String filePath;

    public TxtReader(String filePath) {
        this.filePath = filePath;
    }

    public interface ReadListenter {
        void readAt(int position, String beforeTxt);
    }

    public String read(String filepath) {
        return read(filepath, null);
    }

    public String read(ReadListenter listenter) {
        return read(filePath, listenter);
    }

    public String read(String filepath, ReadListenter listenter) {
        try {
            File mFile = new File(filepath);
            InputStreamReader mReader = new InputStreamReader(new FileInputStream(filepath), "GBK");
//             mReader = new InputStreamReader(new FileInputStream(filepath), "utf-8");
            BufferedReader breader = new BufferedReader(mReader);
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = breader.readLine()) != null) {
                String reg = "(\\\\\\x{7b2c})(\\\\s*)([\\\\x{4e00}\\\\x{4e8c}\\\\x{4e09}\\\\x{56db}\\\\x{4e94}\\\\x{516d}\\\\x{4e03}\\\\x{516b}\\\\x" +
                        "{4e5d}\\\\x{5341}\\\\x{767e}\\\\x{5343}0-9]+)(\\\\s*)([\\\\x{7ae0}\\\\x{8282}]+)";
                if (s.length() > 10) {
                    Log.e("zjy", "TxtReader->read():before paragraph ==" + s.substring(0, 10));
                }
                if (listenter != null) {
                    int rate = sb.toString().getBytes().length;

                    Pattern pattern = Pattern.compile(reg);
                    // 忽略大小写的写法
                    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(s);
                    // 字符串是否与正则表达式相匹配
                    boolean rs = matcher.matches();
                    if (rs) {
                        Log.e("zjy", "TxtReader->read():before paragraph ==" + s);
                        if (rate > 1024 * 1024) {
                            listenter.readAt(sb.toString().getBytes().length / (int) mFile.length(), sb.toString());
                            break;
                        }
                    }
                }
                sb.append(s);
                sb.append("\n");
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
