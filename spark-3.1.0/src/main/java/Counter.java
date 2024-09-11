
/**
 * Description:
 * Author:dengww
 * Date:2024/8/5
 */

public class Counter {
    private String tagName;
    private Integer count;

    public Counter(String tagName, Integer count) {
        this.tagName = tagName;
        this.count = count;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
