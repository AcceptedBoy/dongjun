import com.gdut.dongjun.util.StringCommonUtil;
import org.junit.Test;

/**
 * Created by AcceptedBoy on 2016/9/18.
 */
public class TestC {

    @Test
    public void testOne() {
        String data = "680c0c68f4ac0064010701ac00000014cd16";
        System.out.println(data.substring(14, 16));
        while(data.length() != 0) {
            int index = StringCommonUtil.getFirstIndexOfEndTag(data, "16");
            if(index != -1) {
                String dataInfo = data.substring(0, index);
                System.out.println(index);
                System.out.println(data.length());
                data = data.substring(index, data.length());
            }
        }
    }


}
