import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;
import com.gdut.dongjun.util.StringCommonUtil;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * Created by AcceptedBoy on 2016/9/18.
 */
public class TestC {

    @Test
    public void testOne() {
        String data = "68 0C 0C 68 F3 01 00 03 01 03 01 01 00 04 00 01 0F 16".replace(" ", "");
        String resu = new HighVoltageDeviceCommandUtil().confirmChangeAffair(data.substring(10, 14));
        System.out.println(resu);
    }

    @Test
    public void testTwo() {

        String data = "686816121612";
        test(data.toCharArray());
    }

    public void test(char[] data) {
        while(data.length != 0) {
            System.out.println(String.copyValueOf(data));
            int index = StringCommonUtil.getFirstIndexOfEndTag(data, "16");
            if(index != -1) {
                //handleIdenCode(ctx, ArrayUtils.subarray(data, 0, index));
                data = ArrayUtils.subarray(data, index, data.length);
            } else {
                break;
            }
        }
    }

}
