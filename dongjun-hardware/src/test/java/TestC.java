import com.gdut.dongjun.util.HighVoltageDeviceCommandUtil;
import com.gdut.dongjun.util.StringCommonUtil;
import org.junit.Test;

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


}
