package claire.util.test.math;

import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.math.SInt;
import claire.util.math.UInt;
import claire.util.standards.IInteger;
import claire.util.test.Test;

public class TestMath {

	public static final void testMath() throws Exception
	{
		Log.info.println("Testing unsigned integers....");
		Log.info.println("Testing UInt");
		testIntegerUnsigned(new UInt(128));
		Log.info.println("Testing SInt...");
		testIntegerUnsigned(new SInt(64));
		Log.info.println("Testing Signed Integers...");
		Log.info.println("Testing SInt...");
		testIntegerSigned(new SInt(64));
	}
	
	public static final void testIntegerUnsigned(IInteger<?> i1) throws Exception
	{
		IInteger<?> i2 = i1.createDeepClone();
		IInteger<?> i3 = i1.createDeepClone();
		IInteger<?> i4;
		Log.info.println("Testing against known values...");
		Log.info.println("Testing Addition...");
		i1.setTo("1487815647197611695910312681741273570332356717154798949898498305086387315423300999654757561928633305897036801");
		i2.setTo("5199541521253729102164309190462367181246761624113388644838628182961470617343478994110640944042460839936");
		i3.setTo("1487820846739132949639414846050464032699537963916423063287143143714570276893918343133751672569577348357876737");
		i4 = i1.add(i2);
		if(!i4.toCString().equals(i3.toCString())) {
			System.out.println(i1.toCString());
			System.out.println(i3.toCString());
			Test.reportError("Addition failed for known values.");
			if(i4.isEqualTo(i3))
				Test.reportError("Equality test failed.");
		} else {
			if(i4.doesNotEqual(i3))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing Subtraction...");
		i3.setTo("1487810447656090442181210517432083107965175470393174836509853466458204353952683656175763451287689263436196865");
		i4 = i1.subtract(i2);
		if(!i4.toCString().equals(i3.toCString())) {
			Test.reportError("Subtraction failed for known values.");
			if(i4.isEqualTo(i3))
				Test.reportError("Equality test failed.");
		} else {
			if(i4.doesNotEqual(i3))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing Multiplication...");
		i3.setTo("7735959233574971433270357297502913692339563524604504900196442839167286467726934846064202475697408998139958518402586583122263902060288841708890402656180759233219113533074739807596586680684418722312748503962484736");
		i4 = i1.multiply(i2);
		if(!i4.toCString().equals(i3.toCString())) {
			Test.reportError("Multiplication failed for known values.");
			if(i4.isEqualTo(i3))
				Test.reportError("Equality test failed.");
		} else {
			if(i4.doesNotEqual(i3))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing Division...");
		i3.setTo("286143");
		i4 = i1.divide(i2);
		if(!i4.toCString().equals(i3.toCString())) {
			Test.reportError("Division failed for known values.");
			if(i4.isEqualTo(i3))
				Test.reportError("Equality test failed.");
		} else {
			if(i4.doesNotEqual(i3))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing Modulus...");
		i3.setTo("3237681505889429710757054800437988864605746121582898438720929243228564785889842956430277491433775229953");
		i4 = i1.modulo(i2);
		if(!i4.toCString().equals(i3.toCString())) {
			Test.reportError("Modulus failed for known values.");
			System.out.println(i1.toCString());
			System.out.println(i2.toCString());
			System.out.println(i3.toCString());
			System.out.println(i4.toCString());
			int i = 1;
			if(i == 1)
				throw new java.lang.AbstractMethodError();
			if(i4.isEqualTo(i3))
				Test.reportError("Equality test failed.");
		} else {
			if(i4.doesNotEqual(i3))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing Exponentiation...");
		i1.setTo("7357033235671715479894989849830508638731542330");
		i2.setTo("13");
		i3.setTo("184984497361696780343299354933244379595308053588723922580317037126852658437002479304382032646031775977354791762621301151541709271448349826668857860703567670223463230390245692765989264375339700966211556541002256349307599846913804638262870435611966386466551838485912306979170652884282274967049013023370077155615435141092101162385746144660955186102531915533617421050684001943755589309831872379717058752090619625555501312692090096488861471379267062573499804143701733974879185059183953389104247285471128902961178273091182939434954978433347071621055804328001566836195780424265255359289643130000000000000");
		i4 = i1.exponent(i2);
		if(!i4.toCString().equals(i3.toCString())) {
			Test.reportError("Exponentiation failed for known values.");
			if(i4.isEqualTo(i3))
				Test.reportError("Equality test failed.");
		} else {
			if(i4.doesNotEqual(i3))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing comparison operators...");
		i2 = i1.createDeepClone();
		i2.increment();
		if(!i2.isGreaterThan(i1))
			Test.reportError("Greater then test failed.");
		if(!i2.isGreaterOrEqualTo(i1))
			Test.reportError("Greater then or equal to test failed.");
		if(i2.isLesserThan(i1))
			Test.reportError("Less then test failed.");
		if(i2.isLesserOrEqualTo(i1))
			Test.reportError("Less then or equal to test failed.");
		i2.decrement();
		if(i2.isGreaterThan(i1))
			Test.reportError("Greater then test failed.");
		if(!i2.isGreaterOrEqualTo(i1))
			Test.reportError("Greater then or equal to test failed.");
		if(i2.isLesserThan(i1))
			Test.reportError("Less then test failed.");
		if(!i2.isLesserOrEqualTo(i1))
			Test.reportError("Less then or equal to test failed.");
		i2.decrement();
		if(i2.isGreaterThan(i1))
			Test.reportError("Greater then test failed.");
		if(i2.isGreaterOrEqualTo(i1))
			Test.reportError("Greater then or equal to test failed.");
		if(!i2.isLesserThan(i1))
			Test.reportError("Less then test failed.");
		if(!i2.isLesserOrEqualTo(i1))
			Test.reportError("Less then or equal to test failed.");
		Log.info.println("Testing addition/subtraction contracts...");
		RandUtils.fillArr(i2.getArr());
		i2.p_modulo(i1);
		i3 = i1.createDeepClone();
		i3.p_subtract(i2);
		i3.p_add(i2);
		if(!i3.toCString().equals(i1.toCString())) {
			Test.reportError("Addition followed by subtraction did not result in the same value.");
			System.out.println(i1.toCString());
			System.out.println(i2.toCString());
			System.out.println(i3.toCString());
			if(i3.isEqualTo(i1))
				Test.reportError("Equality test failed.");
		} else {
			if(i3.doesNotEqual(i1))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing multiplication/division contracts...");
		i3 = i1.createDeepClone();
		i3.p_multiply(i2);
		i3.p_divide(i2);
		if(!i3.toCString().equals(i1.toCString())) {
			Test.reportError("Multiplication followed by division did not result in the same value.");
			if(i3.isEqualTo(i1))
				Test.reportError("Equality test failed.");
		} else {
			if(i3.doesNotEqual(i1))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing division contracts...");
		i2.zero();
		RandUtils.fillArr(i2.getArr(), 0, 8);	
		i3.zero();
		RandUtils.fillArr(i3.getArr(), 0, 2);	
		i4 = i2.divide(i3);
		i1 = i2.modulo(i3);
		IInteger<?> i5 = i4.createDeepClone();
		i5.p_multiply(i3);
		i5.p_add(i1);
		if(!i5.toCString().equals(i2.toCString())) {
			Test.reportError("Quotient * Divisor + Remainder did not result in the same value.");
			/*
			 * Don't remove, this is always acting up despite working in practice
			 */
			Log.info.println("Dividend: " + i2.toCString().toString());
			Log.info.println("Divisor: " + i3.toCString().toString());
			Log.info.println("Quotient: " + i4.toCString().toString());
			Log.info.println("Remainder: " + i1.toCString().toString());
			Log.info.println("Result: " + i5.toCString().toString());
			if(i5.isEqualTo(i2))
				Test.reportError("Equality test failed.");
		} else {
			if(i5.doesNotEqual(i2))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing squaring...");
		i1.zero();
		RandUtils.fillArr(i1.getArr(), 0, 16);
		i2 = i1.createDeepClone();
		i2.p_multiply(i1);
		i1.p_square();
		if(!i1.toCString().equals(i2.toCString())) {
			Test.reportError("Squaring and multiplying by copy does not result in the same value.");
			if(i5.isEqualTo(i2))
				Test.reportError("Equality test failed.");
		} else {
			if(i1.doesNotEqual(i2))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing large modulus...");
		i1.zero();
		RandUtils.fillArr(i1.getArr(), 0, 8);
		i2 = i1.createDeepClone();
		i3.zero();
		RandUtils.fillArr(i3.getArr(), 0, 16);
		i1.p_modulo(i3);
		if(!i1.toCString().equals(i2.toCString())) {
			Test.reportError("Mod with a larger modulus actually had an effect.");
			if(i2.isEqualTo(i1))
				Test.reportError("Equality test failed.");
		} else {
			if(i1.doesNotEqual(i2))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing return operations...");
		i1.zero();
		RandUtils.fillArr(i1.getArr(), 0, 16);	
		i2.zero();
		RandUtils.fillArr(i2.getArr(), 0, 8);	
		Log.info.println("Testing Addition...");
		i3 = i1.createDeepClone();
		i4 = i3.add(i2);
		i3.p_add(i2);
		if(!i3.isEqualTo(i4))
			Test.reportError("Addition does not have matching operations.");
		Log.info.println("Testing Subtraction...");
		i3 = i1.createDeepClone();
		i4 = i3.subtract(i2);
		i3.p_subtract(i2);
		if(!i3.isEqualTo(i4))
			Test.reportError("Subtraction does not have matching operations.");
		Log.info.println("Testing Multiplication...");
		i3 = i1.createDeepClone();
		i4 = i3.multiply(i2);
		i3.p_multiply(i2);
		if(!i3.isEqualTo(i4))
			Test.reportError("Multiplication does not have matching operations.");
		Log.info.println("Testing Division...");
		i3 = i1.createDeepClone();
		i4 = i3.divide(i2);
		i3.p_divide(i2);
		if(!i3.isEqualTo(i4))
			Test.reportError("Division does not have matching operations.");
		Log.info.println("Testing Modulus...");
		i3 = i1.createDeepClone();
		i4 = i3.modulo(i2);
		i3.p_modulo(i2);
		if(!i3.isEqualTo(i4))
			Test.reportError("Modulus does not have matching operations.");		
	}
	
	public static final void testIntegerSigned(IInteger<?> i1)
	{
		IInteger<?> i2 = i1.createDeepClone();
		IInteger<?> i3 = i1.createDeepClone();
		Log.info.println("Testing negation...");
		RandUtils.fillArr(i1.getArr(), 0, 16);
		RandUtils.fillArr(i2.getArr(), 0, 32);
		i2.setNegative(true);
		if(!i2.isNegative()) {
			Test.reportError("Critical: Sign inversion failed.");
			return;
		}
		Log.info.println("Testing negation by addition");
		i3.setTo(i1);
		i1.p_add(i2);
		if(!i1.isNegative()) 
			Test.reportError("Addition of a larger negative value did not result in a negative value");
		i1.p_subtract(i2);
		if(!i1.toCString().equals(i3.toCString())) {
			Test.reportError("Addition and subtraction of a larger negative value did not result in the same value.");
			if(i1.isEqualTo(i3))
				Test.reportError("Equality test failed.");
		} else {
			if(i1.doesNotEqual(i3))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing (de)negation by subtraction");
		RandUtils.fillArr(i1.getArr(), 0, 16);
		RandUtils.fillArr(i2.getArr(), 0, 32);
		i1.setNegative(true);
		i2.setNegative(true);
		i3.setTo(i1);
		i1.p_subtract(i2);
		if(i1.isNegative())
			Test.reportError("Subtraction of a larger negative value did not result in a positive value.");
		i1.p_add(i2);
		if(!i1.toCString().equals(i3.toCString())) {
			Test.reportError("Subtraction and addition of a larger negative value did not result in the same value.");
			if(i1.isEqualTo(i3))
				Test.reportError("Equality test failed.");
		} else {
			if(i1.doesNotEqual(i3))
				Test.reportError("Inequality test failed.");
		}
		Log.info.println("Testing negation by multiplication");
		RandUtils.fillArr(i1.getArr(), 0, 10);
		RandUtils.fillArr(i2.getArr(), 0, 10);
		i1.setNegative(true);
		i2.setNegative(false);
		i2.p_multiply(i1);
		if(!i2.isNegative())
			Test.reportError("Multiplying by a negative value gave a positive one");
		i1.setNegative(true);
		i2.setNegative(true);
		i2.p_multiply(i1);
		if(i2.isNegative())
			Test.reportError("Multiplying two negative values gave a negative one");
		Log.info.println("Testing negation by division");
		i1.setNegative(true);
		i2.setNegative(false);
		i2.p_divide(i1);
		if(!i2.isNegative())
			Test.reportError("Dividing by a negative value gave a positive one");
		i1.setNegative(true);
		i2.setNegative(true);
		i2.p_divide(i1);
		if(i2.isNegative())
			Test.reportError("Dividing two negative values gave a negative one");
		
	}
	
}
