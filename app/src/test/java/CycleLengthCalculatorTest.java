import junit.framework.TestCase;

import org.epstudios.epmobile.CycleLength;

public class CycleLengthCalculatorTest extends TestCase {

	public void testCalculate() {
		assertEquals(1000, CycleLength.calculate(60));
		assertEquals(100, CycleLength.calculate(600));
		// unrounded result is 1090.9, test will fail if truncation instead of
		// rounding occurs
		assertEquals(1091, CycleLength.calculate(55));
		// should round 1132.075 down
		assertEquals(1132, CycleLength.calculate(53));
		// test rounding mode -- it looks like it is by default UP, not
		// HALF_EVEN
		assertEquals(6, Math.round(5.5));
		assertEquals(5, Math.round(4.5));
		assertEquals(4, Math.round(4.499));
		// test errors from integer truncation
		assertEquals(68, CycleLength.calculate(888));
	}
}
