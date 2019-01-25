package reader.loveyou.zjy.b1b.p.loveyoureader.entity;

/**
 Created by 张建宇 on 2019/1/23. */
public class PageInfo {
    private String title;
    private int position;
    private float rate;
    private String content;

    public PageInfo() {
    }

    public PageInfo(String title, int position, float rate, String content) {
        this.title = title;
        this.position = position;
        this.rate = rate;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
