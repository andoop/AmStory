package andoop.android.amstory.net.tag.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xmc1993 on 2017/5/12.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryTag {
    private Integer id;
    private Integer parentId;//父标签，一级标的parentId默认为0
    private String content;//标签的名字
    private Date createTime;
    private Date updateTime;
    private int valid;//用于软删除
}