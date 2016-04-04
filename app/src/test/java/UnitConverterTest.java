import junit.framework.TestCase;

import org.epstudios.epmobile.UnitConverter;

public class UnitConverterTest extends TestCase {

	public UnitConverterTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testWeightConversion() {
		assertEquals(2.2, UnitConverter.kgsToLbs(1), 0.1);
		assertEquals(101.41, UnitConverter.kgsToLbs(46), 0.1);
		assertEquals(0, UnitConverter.kgsToLbs(0), 0.1);
		assertEquals(91, UnitConverter.lbsToKgs(200.61), 0.01);
		assertEquals(100, UnitConverter.lbsToKgs(220.46), 0.01);
	}

	public void testDistanceConversion() {
		assertEquals(28.8976, UnitConverter.cmsToIns(73.4), 0.001);
		assertEquals(0, UnitConverter.cmsToIns(0), 0.001);
		assertEquals(0.34252, UnitConverter.cmsToIns(0.87), 0.001);
		assertEquals(415.70866, UnitConverter.cmsToIns(1055.9), 0.001);
	}

}
