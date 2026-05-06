package test;

import org.junit.Before;
import weaver.general.GCONST;
import java.io.File;

public class BaseTest {

	@Before
	public void before() {
		GCONST.setServerName("ecology");
		String fileSeparator = File.separator;
		GCONST.setRootPath(System.getProperty("user.dir")+fileSeparator+"web"+fileSeparator);
	}

}
