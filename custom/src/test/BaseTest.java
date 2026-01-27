package test;

import org.junit.Before;
import weaver.general.GCONST;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class BaseTest {

	@Before
	public void before() {
		GCONST.setServerName("ecology");
		String fileSeparator = File.separator;
		GCONST.setRootPath(System.getProperty("user.dir")+fileSeparator+"web"+fileSeparator);

		String hostname = "Unknown";
		try
		{
			InetAddress addr= InetAddress.getLocalHost();
			hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
			System.out.println("Hostname can not be resolved");
		}
	}

}
