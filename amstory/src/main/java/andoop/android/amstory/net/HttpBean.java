package andoop.android.amstory.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by wsy on 17/5/13.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HttpBean<T> {
    int status;
    String type;
    T obj;
    String errorMes;
    int count;
}
