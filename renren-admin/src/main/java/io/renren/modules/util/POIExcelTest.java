package io.renren.modules.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class POIExcelTest {
    @Test
    public void POIExcel() throws Exception {
        String pathname = "C:\\Users\\asus\\Desktop\\uk.eu_browse_tree_mappings._TTH_.xls";
        POIExcelUtil.insetCategory(pathname,"GB");
    }
}
