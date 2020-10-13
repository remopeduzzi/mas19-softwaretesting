package ch.hsr.testing.unittest.weekenddiscount;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("ch.hsr.testing.unittest.weekenddiscount")
@IncludeTags({"negative", "fast"}) 
public class FastNegativeTestSuite {
}
