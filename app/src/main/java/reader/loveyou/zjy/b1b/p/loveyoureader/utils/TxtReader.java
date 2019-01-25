package reader.loveyou.zjy.b1b.p.loveyoureader.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import reader.loveyou.zjy.b1b.p.loveyoureader.entity.PageInfo;

/**
 Created by 张建宇 on 2019/1/23. */
public class TxtReader {
    private String filePath;

    public TxtReader(String filePath) {
        this.filePath = filePath;
    }

    public interface ReadListenter {
        void readAt(int position, String beforeTxt);

        void postPrePages(List<PageInfo> pages);
    }

    public String read(String filepath) {
        return read(filepath, null);
    }

    public String read(ReadListenter listenter) {
        return read(filePath, listenter);
    }

    public List<PageInfo> read2(String filepath, ReadListenter listenter) {
        List<PageInfo> mPages = new ArrayList<>();

        BufferedReader breader = null;
        try {
            File mFile = new File(filepath);
            String charSet = "GBK";
            //            String charSet = "utf-8";
            InputStreamReader mReader = new InputStreamReader(new FileInputStream(filepath), charSet);
            //             mReader = new InputStreamReader(new FileInputStream(filepath), "utf-8");
            breader = new BufferedReader(mReader);
            StringBuilder sb = new StringBuilder();
            String s = null;
            int limit = 100 * (1024 * 2);//50kb
            int totalChars = 0;
            long time = System.currentTimeMillis();
            Log.e("zjy", "TxtReader->read(): startRead==");
            String reg = "(\\x{7b2c})(\\s*)([\\x{4e00}\\x{4e8c}\\x{4e09}\\x{56db}\\x{4e94}\\x{516d}\\x{4e03}\\x{516b}\\x{4e5d" +
                    "}\\x{5341}\\x{767e}\\x{5343}0-9]+)(\\s*)([\\x{7ae0}\\x{8282}]+)";
            Pattern pattern = Pattern.compile(reg);
            boolean preReadOk = false;
            int len = 0;
            // 忽略大小写的写法
            // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            StringBuilder singlePageBuilder = new StringBuilder();
            PageInfo pageInfo = null;
            while ((s = breader.readLine()) != null) {
                if (listenter != null) {
                    if (s.length() < 30) {
                        Matcher matcher = pattern.matcher(s);
                        // 字符串是否与正则表达式相匹配
                        boolean rs = matcher.find();
                        if (rs) {
                            if (pageInfo != null) {
                                pageInfo.setContent(singlePageBuilder.toString());
                                mPages.add(pageInfo);
                                singlePageBuilder = new StringBuilder();
                            }
                            pageInfo = new PageInfo();
                            pageInfo.setTitle(s);
//                            zhangjie
//                            Log.e("zjy", "TxtReader->read():zhangjie  ==" + s);
                            if (!preReadOk) {
                                if (totalChars > limit) {
                                    Log.e("zjy", "TxtReader->read(): UserTime0==" + (System.currentTimeMillis() - time) / 1000f);
                                    listenter.readAt(sb.toString().getBytes(charSet).length / (int) mFile.length(), sb.toString
                                            ());
                                    List<PageInfo> minfo = new ArrayList<>();
                                    minfo.addAll(mPages);
                                    listenter.postPrePages(minfo);
                                    preReadOk = true;
                                }
                            }
                        }
                    }
                }
                if (pageInfo != null) {
                    singlePageBuilder.append(s);
                    singlePageBuilder.append("\n");
                }
                if (preReadOk) {
//                    Log.e("zjy", "TxtReader->read(): UserTime==" + (System.currentTimeMillis() - time) / 1000f + "\tmaxTitle="
//                            + len);
//                    break;
                }
                totalChars += s.length();
//                sb.append(s);
//                sb.append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (breader != null) {
                try {
                    breader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("zjy", "TxtReader->read(): ReadFinished==");
        return mPages;
    }
    public String read(String filepath, ReadListenter listenter) {
        BufferedReader breader = null;
        try {
            File mFile = new File(filepath);
            String charSet = "GBK";
            //            String charSet = "utf-8";
            InputStreamReader mReader = new InputStreamReader(new FileInputStream(filepath), charSet);
            //             mReader = new InputStreamReader(new FileInputStream(filepath), "utf-8");
            breader = new BufferedReader(mReader);
            StringBuilder sb = new StringBuilder();
            String s = null;
            int limit = 100 * (1024 * 2);//50kb
            int totalChars = 0;
            long time = System.currentTimeMillis();
            Log.e("zjy", "TxtReader->read(): startRead==");
            String reg = "(\\x{7b2c})(\\s*)([\\x{4e00}\\x{4e8c}\\x{4e09}\\x{56db}\\x{4e94}\\x{516d}\\x{4e03}\\x{516b}\\x{4e5d" +
                    "}\\x{5341}\\x{767e}\\x{5343}0-9]+)(\\s*)([\\x{7ae0}\\x{8282}]+)";
            Pattern pattern = Pattern.compile(reg);
            boolean preReadOk = false;

            int len = 0;
            // 忽略大小写的写法
            // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            while ((s = breader.readLine()) != null) {
                if (listenter != null) {
                    if (!preReadOk) {
                        if (s.length() < 30) {
                            Matcher matcher = pattern.matcher(s);
                            // 字符串是否与正则表达式相匹配
                            boolean rs = matcher.find();
                            if (rs) {
                                Log.e("zjy", "TxtReader->read():zhangjie  ==" + s);
                                if (totalChars > limit) {
                                    Log.e("zjy", "TxtReader->read(): UserTime==" + (System.currentTimeMillis() - time) / 1000f);
                                    listenter.readAt(sb.toString().getBytes(charSet).length / (int) mFile.length(), sb.toString
                                            ());
                                    preReadOk = true;
                                }
                            }
                        }
                    }
                }
                if (totalChars > limit) {
                    Log.e("zjy", "TxtReader->read(): UserTime==" + (System.currentTimeMillis() - time) / 1000f + "\tmaxTitle="
                            + len);
                    break;
                }
                totalChars += s.length();
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
        } finally {
            if (breader != null) {
                try {
                    breader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("zjy", "TxtReader->read(): ReadFinished==");
        if (listenter != null) {
        }
        return "";
    }
}
