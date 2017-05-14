package andoop.android.amstory.net.story.bean;

import java.util.List;

import andoop.android.amstory.net.tag.bean.StoryTag;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by wsy on 17/5/14.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class StoryWithTagVo extends SentenceVo {
    List<StoryTag> tagList;
}
