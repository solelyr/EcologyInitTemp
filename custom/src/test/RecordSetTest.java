package test;

import org.junit.Test;
import weaver.conn.RecordSet;

import java.util.HashMap;
import java.util.Map;

public class RecordSetTest extends BaseTest {

    @Test
    public void select() {
        RecordSet rs  = new RecordSet();
        String sql = "select id,lastname from hrmresource where id = (select max(id) from hrmresource)";
        rs.executeQuery(sql);
        while (rs.next()) {
            Map<String,Object> map = new HashMap<>();
            for(String col : rs.getColumnName() ){
                map.put(col,rs.getString(col));
            }
            System.out.println("查询到的人员信息："+map);
        }
    }
}
