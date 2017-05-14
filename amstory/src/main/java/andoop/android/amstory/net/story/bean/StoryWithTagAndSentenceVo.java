package andoop.android.amstory.net.story.bean;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by wsy on 17/5/14.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class StoryWithTagAndSentenceVo extends StoryWithTagVo {
    List<SentenceVo> sentenceList;
}
