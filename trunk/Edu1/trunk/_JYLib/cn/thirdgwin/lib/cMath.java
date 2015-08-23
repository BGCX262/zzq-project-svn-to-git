package cn.thirdgwin.lib;

public class cMath {

	/// Fixed point value for number 1.
	final static int 	s_math_F_1		= 1 << DevConfig.math_fixedPointBase;
	/// Fixed point value for number 0.5.
	final static int 	s_math_F_05		= (s_math_F_1>>1);


	/// Random number generator.
	static java.util.Random s_math_random;


	/// x coordinate of orthogonal projection of point on a segment using Math_distPointLine
	static int 			s_Math_distPointLineX;
	/// y coordinate of orthogonal projection of point on a segment using Math_distPointLine
	static int 			s_Math_distPointLineY;


	/// x coordinate of intersection point of 2 segment using Math_segmentIntersect
	static int 			s_Math_intersectX;
	/// y coordinate of intersection point of 2 segment using Math_segmentIntersect
	static int 			s_Math_intersectY;
	/// return value of Math_segmentIntersect if there is no intersection
	static final int 	MATH_SEGMENTINTERSECT_DONT_INTERSECT 	= 0;
	/// return value of Math_segmentIntersect if there is no or full intersection because both segment are colinear
	static final int 	MATH_SEGMENTINTERSECT_COLLINEAR 		= -1;
	/// return value of Math_segmentIntersect if there is an intersection
	static final int 	MATH_SEGMENTINTERSECT_DO_INTERSECT 		= 1;


	private static int 	Math_quickSortIndices_nbItemPerValue;
	private static int 	Math_quickSortIndices_itemNb;
	private static int[]Math_quickSortIndices_result;
	private static int[]Math_quickSortIndices_data;

	/// x coordinate of bezier interpolated value through Math_Bezier2D or Math_Bezier3D
	static int 			s_math_bezierX;
	/// y coordinate of bezier interpolated value through Math_Bezier2D or Math_Bezier3D
	static int 			s_math_bezierY;
	/// z coordinate of bezier interpolated value through Math_Bezier3D
	static int 			s_math_bezierZ;

	/// cosine table
	private static int 	s_math_cosTable[];
	/// sqrt table
	private static int 	s_math_sqrtTable[];
	/// atan table
	///!note only if zJYLibConfig.useAtanTable is set to true
	private static int 	s_math_aTanTable[];


	/// angle fixed point multiplier eg one degree in fixed point base.(not equal to Math_DegreeToFixedPointAngle(1))
	final static int Math_AngleMUL = 1 << DevConfig.math_angleFixedPointBase;
	/// 90 in fixed point
	final static int Math_Angle90 = Math_DegreeToFixedPointAngle(90);
	/// 180 in fixed point
	final static int Math_Angle180 = Math_DegreeToFixedPointAngle(180);
	/// 270 in fixed point
	final static int Math_Angle270 = Math_DegreeToFixedPointAngle(270);
	/// 360 in fixed point
	final static int Math_Angle360 = Math_DegreeToFixedPointAngle(360);


	//--------------------------------------------------------------------------------------------------------------------
	/// Math initialisation, by reading specified table from a package.
	///!param packName Name of the pack witch contains math table.
	///!param cos Index of cosine table in the pack. Use -1 if no table but all call to Math_Cos & Math_Sin will fail.
	///!param sqrt Index of sqrt table in the pack. Use -1 if no table but all call to Math_sqrt will fail.
	///!returns True if everything was loaded as requested.
	//--------------------------------------------------------------------------------------------------------------------

	public static void Math_Init (int[] costable,int[] sqrttable){
		s_math_cosTable = costable;
		s_math_sqrtTable = sqrttable;
		
	}
	public static void Math_Init (String packName, int cos, int sqrt) throws Exception
	{


		if(!(packName != null))Utils.DBG_PrintStackTrace(false, "Math_Init.packName is null");;

		int i;

		cPack.Pack_Open(packName);

		// read cos table
		if (cos >= 0)
		{
			s_math_cosTable = (int[]) cPack.Pack_ReadArray(cos);
		}
		else
		{
			s_math_cosTable = null;
		}

		// read sqrt table
		if (sqrt >= 0)
		{
			s_math_sqrtTable = (int[]) cPack.Pack_ReadArray(sqrt);
		}
		else
		{
			s_math_sqrtTable = null;
		}

		cPack.Pack_Close();
	}

	public static void Math_Init_CosArr(){
		
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Free all math arrays.
	//--------------------------------------------------------------------------------------------------------------------
	static void Math_Quit ()
	{
		s_math_cosTable 	= null;
		s_math_sqrtTable 	= null;
		s_math_random 		= null;
		s_math_aTanTable	= null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Adjust a fixed point in base 8 to this fixed point base.
	///!param a Value to adjsut.
	///!return The value ajusted.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPointAdjust (int a)
	{
		if (DevConfig.math_fixedPointBase >DevConfig.math_angleFixedPointBase)
		{
			return a << (DevConfig.math_fixedPointBase - DevConfig.math_angleFixedPointBase);
		}
		else if (DevConfig.math_fixedPointBase <DevConfig.math_angleFixedPointBase)
		{
			return a >> (DevConfig.math_angleFixedPointBase - DevConfig.math_fixedPointBase);
		}
		else
		{
			return a;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Convert a fixed point value to an int value.
	///!param a Value to convert to fixed point.
	///!return Fixed point value of a.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_IntToFixedPoint (int a)
	{
		return a << DevConfig.math_fixedPointBase;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Convert a fixed point value to an int value.
	///!param a Fixed point value to convert to int.
	///!return Int value of a.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPointToInt (int a)
	{
		return ((a + (s_math_F_1 >> 1)) >> DevConfig.math_fixedPointBase);
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Divide by 10 a number, with result in fixed point. Faster than classical division.
	///!param a Number to divide by 10.
	///!return Fixed point value of the number divided by 10.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_Div10 (int a)
	{
		return ((a * 6554 ) >> (16 - DevConfig.math_fixedPointBase));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// return log base 2
	///!param a value to calculate log base 2
	///!return log base 2 of value
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_Log2 (int a)
	{
		if(!(a>=0))Utils.DBG_PrintStackTrace(false, "Math_Log2.value is negative");;
		int r = 0;
		while ((a >> r) > 1)
		{
			r++;
		}
		return r;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Test 2 numbers to see if they are both of the same sign.
	///!param a,b Numbers to compare.
	///!return True if both numbers are positive.
	///!return True if both numbers are negative.
	///!return False Otherwise.
	//--------------------------------------------------------------------------------------------------------------------
	final static boolean Math_SameSign (int a, int b)
	{
		return ((a ^ b) >= 0);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Test 2 numbers to see if they are both of the same sign.
	///!param a,b Numbers to compare.
	///!return True if both numbers are positive.
	///!return True if both numbers are negative.
	///!return False Otherwise.
	//--------------------------------------------------------------------------------------------------------------------
	final static boolean Math_SameSign (long a, long b)
	{
		return ((a ^ b) >= 0);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Compute the determinant of 2 2Dvector.
	///!param x1,y1 X & Y value of the first vector.
	///!param x2,y2 X & Y value of the second vector.
	///!return The determinant of the 2 vectors.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_Det (int x1, int y1, int x2, int y2)
	{
		return ((x1 * y2) - (y1 * x2));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Compute the dot product of 2 2Dvector.
	///!param x1,y1 X & Y value of the first vector.
	///!param x2,y2 X & Y value of the second vector.
	///!return The dot product of the 2 vectors.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_DotProduct (int x1, int y1, int x2, int y2)
	{
		return ((x1 * x2) + (y1 * y2));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Compute pow of the normal of a vector.
	///!param x1,y1 X & Y value of the vector.
	///!return The pow of the normal of the vector.
	//--------------------------------------------------------------------------------------------------------------------
	public final static int Math_NormPow (int x1, int y1)
	{
		return x1*x1 + y1*y1;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Norm following newton law approximation.
	/// Sqrt(n) is provided by iteration of xk = (xk + n / xk) / 2.
	/// The more iteration (k++), the better is the approximation.
	///!param x,y X and Y coordinate of vector to get the norm from.
	///!param iter Number of iteration to perform, more iteration gives a better result
	///             however more iteration are more cpu intensive.
	///!returns norm of vector (x,y).
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_Norm (int x, int y, int iter)
	{
		int R = (x * x + y * y);
		int x_0 = 1;
		for (int i=0; i<iter; i++)
		{
			x_0 = (x_0 + R / x_0) >> 1;
		}
		return x_0;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// set math random seed
	///!param seed seed to use for random number generator.
	//--------------------------------------------------------------------------------------------------------------------
	public final static void Math_RandSetSeed(long seed)
	{
		// init math random number generator
	    if (s_math_random == null)
	        s_math_random = new java.util.Random(seed);
	    else
	        s_math_random.setSeed(seed);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Create a random int inside the interval [a, b[.
	///!param a,b Interval for the random number to generate.
	///!return A number between [a, b[.
	///!note if a=b then return value is a (or b)
	//--------------------------------------------------------------------------------------------------------------------
	public static int Math_Rand (int a, int b)
	{
		if(!(s_math_random != null))Utils.DBG_PrintStackTrace(false, "Math_Rand.GLLib mut be initialised prior to using this function");;
		if(!(a <= b))Utils.DBG_PrintStackTrace(false, "Math_Rand.a must be <= b");;
		int rnd;
		if (b != a)
		{
			rnd = s_math_random.nextInt();
			if (rnd < 0)
			{
				rnd *= -1;
			}
			return (a+(rnd%(b-a)));
		}
		return b;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Create a random number.
	///!return a random int.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_Rand ()
	{
		if(!(s_math_random != null))Utils.DBG_PrintStackTrace(false, "Math_Rand.GLLib mut be initialised prior to using this function");;
		return s_math_random.nextInt();
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Convert a degree angle into a fixed point angle.
	///!param a an angle in degrees.
	///!return angle in fixed point.
	//--------------------------------------------------------------------------------------------------------------------
	public final static int Math_DegreeToFixedPointAngle (int a)
	{
		//return ((a << zJYLibConfig.math_angleFixedPointBase) / 360);
		return (a * Math_AngleMUL / 360);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Convert a fixed point angle into a degree angle.
	///!param a an angle in fixed point.
	///!return angle in degrees.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPointAngleToDegree (int a)
	{
		return ((a * 360) >> DevConfig.math_angleFixedPointBase);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Sinus.
	///!param a Angle in fixed point 0=0 (1<<math_angleFixedPointBase)=360.
	///!return Sinus value of the angle.
	//--------------------------------------------------------------------------------------------------------------------
	public final static int Math_Sin (int a)
	{
		if(!(s_math_cosTable!=null))Utils.DBG_PrintStackTrace(false, "!!ERROR!! Math_Sin.s_math_cosTable is null, call Math_Init first");;
		return Math_Cos(Math_Angle90 - a);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Cosinus.
	///!param angle - angle in fixed point 0=0 (1<<math_angleFixedPointBase)=360.
	///!return Cosinus value of the angle in fixed Point
	//--------------------------------------------------------------------------------------------------------------------
	public static int Math_Cos (int angle)
	{
		if(!(s_math_cosTable!=null))Utils.DBG_PrintStackTrace(false, "!!ERROR!! Math_Cos.s_math_cosTable is null, call Math_Init first");;

		if(angle < 0)
			angle *= -1;

		angle &= (Math_Angle360-1);
		if (angle <= Math_Angle90) //0-90
		{
			return s_math_cosTable[angle];
		}
		else if (angle < Math_Angle180)	// 91-179
		{
			angle = Math_Angle180-angle;
			return -s_math_cosTable[angle];
		}
		else if (angle <= Math_Angle270)	// 180-269
		{
			angle = angle - Math_Angle180;
			return -s_math_cosTable[angle];
		}
		else //270 - 360
		{
			angle = Math_Angle360-angle;
			return s_math_cosTable[angle];
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Tangent
	///!param  angle -  Angle in fixed point 0=0 256=360
	///!return Tangent value of the angle.
	//--------------------------------------------------------------------------------------------------------------------
	public static int Math_Tan (int angle)
	{
		if(!(s_math_cosTable!=null))Utils.DBG_PrintStackTrace(false, "!!ERROR!! Math_Tan.s_math_cosTable is null, call Math_Init first");;

		int tan = Math_Cos(angle);
		if (tan == 0)
		{
			return 0x7FFFFFFF;
		}
		return Math_IntToFixedPoint(Math_Sin(angle)) / tan;
	}

	/*
	//--------------------------------------------------------------------------------------------------------------------
	/// utility function for Arctangent
	//--------------------------------------------------------------------------------------------------------------------
	private static int Math_AtanUtility (int x, int y)
	{
		return ((x << zJYLibConfig.math_angleFixedPointBase)*1000) / (y*6283);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Arctangent.
	/// Fastest method (max 2 of imprecision).
	///!param dx,dy Slope of the angle.
	///!return Arctangent of the slope.
	///!note This is not the real atan func as found in common device, it will return an angle in [0, 1] and not in [0, PI/2].
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_Atan (int dx, int dy)
	{
		if (dx > 0)  							// right
		{
			if (dy > 0) 							// up
			{
				if (dy <= dx)
					return Math_AtanUtility(dy, dx);
				else
					return Math_Angle90 - Math_AtanUtility(dx, dy);
			}
			else if (dy == 0)  					// horizontal
			{
				return 0;
			}
			else 								// down
			{
				dy *= -1;
				if (dy <= dx)
					return Math_Angle360 - Math_AtanUtility(dy, dx);
				else
					return Math_Angle270 + Math_AtanUtility(dx, dy);
			}
		}
		else if (dx == 0) 					// vert
		{
			if (dy > 0)  							// up
			{
				return Math_Angle90;
			}
			else if (dy == 0)  					// horizontal
			{
				return 0;
			}
			else  								// down
			{
				return Math_Angle270;
			}
		}
		else 								// left
		{
			dx *= -1;
			if (dy > 0)  							// up
			{
				if (dy <= dx)
					return Math_Angle180 - Math_AtanUtility(dy, dx);
				else
					return Math_Angle90 + Math_AtanUtility(dx, dy);
			}
			else if (dy == 0)  					// horizontal
			{
				return Math_Angle180;
			}
			else 								// down
			{
				dy *= -1;
				if (dy <= dx)
					return Math_Angle180 + Math_AtanUtility(dy, dx);
				else
					return Math_Angle270 - Math_AtanUtility(dx, dy);
			}
		}
	}
	*/
	/*
	 ---- old aTan function, keep it until validation of new one
	*/
	//--------------------------------------------------------------------------------------------------------------------
	/// Arctangent function.
	///!param st,end - quadrant of the trigonometric circle where the angle is to be found
	///!param slope - Slope of the angle.
	///!return Arctangent of the slope.
	//--------------------------------------------------------------------------------------------------------------------
	// static int Math_Atan (int st, int end, int slope)
	// {
		// // formula in radian
		// // (|x|<=1)	atan(x) = x/(1+ 0.28*x^2)
		// // (|x| >=1)	atan(x) = pi/2 - x/(x^2 + 0.28) or  pi/2 - atan(1/x)
		// if (slope <= Math_AngleMUL )
		// {
			// return st + (Math_AngleMUL*slope) / (Math_AngleMUL*6 + (6*71*slope*slope)/(256*Math_AngleMUL));
		// }
		// else
		// {
			// return end - slope * Math_AngleMUL / ((6*71*Math_AngleMUL)/256+(slope*slope*6)/Math_AngleMUL);
		// }
	// }

	//--------------------------------------------------------------------------------------------------------------------
	/// Arctangent.
	/// Fastest method but not very precise
	///!param dx,dy Slope of the angle.
	///!return Arctangent of the slope.
	///!note This is not the real atan func as found in common device, it will return an angle in [0, 1] and not in [0, PI/2].
	//--------------------------------------------------------------------------------------------------------------------
	// static int Math_Atan (int dx, int dy)
	// {
		// if (dx > 0)  							// right
		// {
			// if (dy > 0) 							// up
			// {
				// return Math_Atan(0, Math_Angle90, (dy*Math_AngleMUL)/dx);
			// }
			// else if (dy == 0)  					// horizontal
			// {
				// return 0;
			// }
			// else 								// down
			// {
				// return Math_Angle360 - Math_Atan(0, Math_Angle90, -(dy*Math_AngleMUL)/dx);
			// }
		// }
		// else if (dx == 0) 					// vert
		// {
			// if (dy > 0)  							// up
			// {
				// return Math_Angle90;
			// }
			// else if (dy == 0)  					// horizontal
			// {
				// return 0;
			// }
			// else  								// down
			// {
				// return Math_Angle270;
			// }

		// } else 								// left
		// {
			// if (dy > 0)  							// up
			// {
				// return Math_Angle180-Math_Atan(0, Math_Angle90, -(dy*Math_AngleMUL)/dx);
			// }
			// else if (dy == 0)  					// horizontal
			// {
				// return Math_Angle180;
			// } else 								// down
			// {
				// return Math_Atan(Math_Angle180, Math_Angle270, (dy*Math_AngleMUL)/dx);
			// }
		// }
	// }

	/*---- save
	static private int Math_Atan(int st, int end, int tang)
	{
		for (int i=st; i<end; i++)
		{
			//if ((s_math_tanTable[i]<= tang) && (tang < s_math_tanTable[i+1]))
			if ((Math_Tan(i)<= tang) && (tang < Math_Tan(i+1)))
			{
				return i;
			}
		}
		if ((st == Math_Angle90)||(end == Math_Angle90))
			return Math_Angle90;
		else if ((st == Math_Angle270)||(end == Math_Angle270))
			return Math_Angle270;
		return 0;
	}
	static int Math_Atan (int dx, int dy)
	{
		if (dx > 0) 	// right
		{
			if (dy > 0)  							// up
			{
				return Math_Atan(0, Math_Angle90, (dy*s_math_F_1)/dx);
			}
			else if (dy == 0) 					// horizontal
			{
				return 0;
			}
			else 								// down
			{
				return Math_Atan(Math_Angle270, Math_Angle360, (dy*s_math_F_1)/dx);
			}

		}
		else if (dx == 0) 					// vert
		{
			if (dy > 0)  							// up
			{
				return Math_Angle90;
			}
			else if (dy == 0)  					// horizontal
			{
				return 0;
			}
			else 								// down
			{
				return Math_Angle270;
			}

		}
		else								// left
		{
			if (dy > 0)  							// up
			{
				return Math_Atan(Math_Angle90, Math_Angle180, (dy*s_math_F_1)/dx);
			}
			else if (dy == 0) 					// horizontal
			{
				return Math_Angle180;
			}
			else								// down
			{
				return Math_Atan(Math_Angle180, Math_Angle270, (dy*s_math_F_1)/dx);
			}
		}
	}
	----*/

	//--------------------------------------------------------------------------------------------------------------------
	/// Arctangent utility function, find angle by dichotomy between angle range
	///!param st,end range of angle
	///!return Arctangent of the slope.
	//--------------------------------------------------------------------------------------------------------------------
	static private int Math_AtanSlow(int st, int end, int tang)
	{
		for (int i=st; i<end; i++)
		{
			if ((Math_Tan(i)<= tang) && (tang < Math_Tan(i+1)))
			{
				return i;
			}
		}
		if ((st == Math_Angle90)||(end == Math_Angle90))
			return Math_Angle90;
		else if ((st == Math_Angle270)||(end == Math_Angle270))
			return Math_Angle270;
		return 0;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Arctangent, very slow but accurate method, it find angle by dichotomy
	///!param dx,dy Slope of the angle.
	///!return Arctangent of the slope.
	///!note This is not the real atan func as found in common device, it will return an angle in [0, 1] and not in [0, PI/2].
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_AtanSlow (int dx, int dy)
	{
		if (dx > 0) 	// right
		{
			if (dy > 0)  							// up
			{
				return Math_AtanSlow(0, Math_Angle90, (dy*s_math_F_1)/dx);
			}
			else if (dy == 0) 					// horizontal
			{
				return 0;
			}
			else 								// down
			{
				return Math_AtanSlow(Math_Angle270, Math_Angle360, (dy*s_math_F_1)/dx);
			}

		}
		else if (dx == 0) 					// vert
		{
			if (dy > 0)  							// up
			{
				return Math_Angle90;
			}
			else if (dy == 0)  					// horizontal
			{
				return 0;
			}
			else 								// down
			{
				return Math_Angle270;
			}

		}
		else								// left
		{
			if (dy > 0)  							// up
			{
				return Math_AtanSlow(Math_Angle90, Math_Angle180, (dy*s_math_F_1)/dx);
			}
			else if (dy == 0) 					// horizontal
			{
				return Math_Angle180;
			}
			else								// down
			{
				return Math_AtanSlow(Math_Angle180, Math_Angle270, (dy*s_math_F_1)/dx);
			}
		}
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Arctangent
	///!note this function will create a cache to speed up calculation if zJYLibConfig.math_AtanUseCacheTable is set to true, but will also consume up to ((1<<math_fixedPointBase)+1)*4 byte
	///!param dx,dy Slope of the angle.
	///!return Arctangent of the slope.
	///!note This is not the real atan func as found in common device, it will return an angle in [0, 1] and not in [0, PI/2].
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_Atan (int dx, int dy)
	{
		if(DevConfig.math_AtanUseCacheTable)
		{
			if (s_math_aTanTable == null)
			{
				s_math_aTanTable = new int[s_math_F_1+1];
				for (int i=0; i<s_math_F_1+1; i++)
				{
					// tanTable[i] = Math_Tan(i);
					s_math_aTanTable[i] = Math_AtanSlow(s_math_F_1, i);
				}
			}

			if (dx == 0)
			{
				if (dy > 0)
					return Math_Angle90;
				else if (dy == 0)
					return 0;
				else
					return Math_Angle270;
			}
			int idx;
			if (dx > 0) 	// right
			{
				if (dy >= 0)
				{
					if (dx >= dy)
					{
						idx = dy * s_math_F_1 / dx;
						return s_math_aTanTable[idx];
					}
					else
					{
						idx = dx * s_math_F_1 / dy;
						return Math_Angle90 - s_math_aTanTable[idx];
					}
				}
				else
				{
					dy *= -1;
					if (dx >= dy)
					{
						idx = dy * s_math_F_1 / dx;
						return Math_Angle360 - s_math_aTanTable[idx];
					}
					else
					{
						idx = dx * s_math_F_1 / dy;
						return Math_Angle270 + s_math_aTanTable[idx];
					}
				}
			}
			else								// left
			{
				dx *= -1;
				if (dy >= 0)
				{
					if (dx >= dy)
					{
						idx = dy * s_math_F_1 / dx;
						return Math_Angle180 - s_math_aTanTable[idx];
					}
					else
					{
						idx = dx * s_math_F_1 / dy;
						return Math_Angle90 + s_math_aTanTable[idx];
					}
				}
				else
				{
					dy *= -1;
					if (dx >= dy)
					{
						idx = dy * s_math_F_1 / dx;
						return Math_Angle180 + s_math_aTanTable[idx];
					}
					else
					{
						idx = dx * s_math_F_1 / dy;
						return Math_Angle270 - s_math_aTanTable[idx];
					}
				}
			}
		}
		else
		{
			return Math_AtanSlow(dx, dy);
		}
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Square Root.
	///!param x Number to get square root of.
	///!return Square root of x.
	///!note Integer Square Root function.
	/// Contributors include Arne Steinarson for the basic approximation idea, Dann
	/// Corbit and Mathew Hendry for the first cut at the algorithm, Lawrence Kirby
	/// for the rearrangement, improvments and range optimization, Paul Hsieh
	/// for the round-then-adjust idea, and Tim Tyler, for the Java port.
	//--------------------------------------------------------------------------------------------------------------------
	public static int Math_Sqrt (int x)
	{
		if (true)
		{
			Utils.DBG_PrintStackTrace(s_math_sqrtTable!=null, "!!ERROR!! Math_sqrt.s_math_sqrtTable is null, call Math_Init first");
		}
		if (null!=s_math_sqrtTable)
			{
				return (int) Math.sqrt(x);
			}
		if (x >= 0x10000)
		{
			if (x >= 0x1000000)
			{
				if (x >= 0x10000000)
				{
					if (x >= 0x40000000)
					{
						return (s_math_sqrtTable[x >> 24] << 8);
					}
					else
					{
						return (s_math_sqrtTable[x >> 22] << 7);
					}
				}
				else if (x >= 0x4000000)
				{
					return (s_math_sqrtTable[x >> 20] << 6);
				}
				else
				{
					return (s_math_sqrtTable[x >> 18] << 5);
				}
			}
			else if (x >= 0x100000)
			{
				if (x >= 0x400000)
				{
					return (s_math_sqrtTable[x >> 16] << 4);
				}
				else
				{
					return (s_math_sqrtTable[x >> 14] << 3);
				}
			}
			else if (x >= 0x40000)
			{
				return (s_math_sqrtTable[x >> 12] << 2);
			}
			else
			{
				return (s_math_sqrtTable[x >> 10] << 1);
			}
		}
		else if (x >= 0x100)
		{
			if (x >= 0x1000)
			{
				if (x >= 0x4000)
				{
					return (s_math_sqrtTable[x >> 8]);
				}
				else
				{
					return (s_math_sqrtTable[x >> 6] >> 1);
				}
			}
			else if (x >= 0x400)
			{
				return (s_math_sqrtTable[x >> 4] >> 2);
			}
			else
			{
				return (s_math_sqrtTable[x >> 2] >> 3);
			}
		}
		else
		{
			if (x >=0)
			{
				return s_math_sqrtTable[x] >> 4;
			}
			return 0;
			//return -1; // negative argument...
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Square Root slow.
	///!param val Number to get square root of.
	///!return Square root of val.
	///!note long version is way slower than int function
	//--------------------------------------------------------------------------------------------------------------------
	public static int Math_Sqrt (long val)
	{
		long temp, g = 0, b = 0x8000, bshft = 15;

		do
		{
			temp = ((( g << 1 ) + b ) << bshft-- );
			if( val >= temp )
			{
				g += b;
				val -= temp;
			}
		}
		while (( b >>= 1 ) > 0 );

		return (int)g;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Square Root for Fixed Point.
	///!param val Number to get square root of.
	///!param precisionLoop Number of time to go through the algo to get good presicion in the result. Good value are [10-30] 15 is usualy good.
	///!return Square root of val in Fixed Point.
	///!note Iterative process, can be slow, use with caution.
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_Sqrt_FixedPoint(int val, int precisionLoop)
	{
		int t, q, b, r;

		r = val;
		b = 0x40000000 >> (16 - DevConfig.math_fixedPointBase);
		q = 0;

		while (b >= 256 && precisionLoop-- > 0)
		{
			t = q + b;
			if (r >= t)
			{
				r = r - t;
				q = t + b;
			}
			r <<= 1;
			b >>= 1;
		}

		q >>= 8;

		return( q );
	}


	/**
	/// Tell if 2 axis aligned rectangle intersect
	///!param (Ax0,Ay0)(Ax1,Ay1) top left and bottom right coordinate of first rectangle
	///!param (Bx0,By0)(Bx1,By1) top left and bottom right coordinate of second rectangle
	///!return true if intersect, false if not
	**/
	public static boolean Math_Rect2PointXYIntersect (int Ax0, int Ay0, int Ax1, int Ay1, int Bx0, int By0, int Bx1, int By1)
	{
		if(!(Ax0 <= Ax1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. Ax0 is bigger than Ax1");;
		if(!(Ay0 <= Ay1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. Ay0 is bigger than Ay1");;
		if(!(Bx0 <= Bx1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. Bx0 is bigger than Bx1");;
		if(!(By0 <= By1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. By0 is bigger than By1");;

		if (Ax1 < Bx0)	return false;
		if (Ax0 > Bx1)	return false;
		if (Ay1 < By0)	return false;
		if (Ay0 > By1)	return false;
		return true;
	}

	/**
	/// Tell if 2 axis aligned rectangle intersect
	///!param (Ax0,Ay0,Aw0,Ah0) top left and bottom right coordinate of first rectangle
	///!param (Bx0,By0,Bw0,Bh0) top left and bottom right coordinate of second rectangle
	///!return true if intersect, false if not
	**/
	public static boolean Math_Rect2PointXYWHIntersect (int Ax0, int Ay0, int Aw0, int Ah0, int Bx0, int By0, int Bw0, int Bh0)
	{
		int Ax1 = Ax0 + Aw0;
		int Ay1 = Ay0 + Ah0;
		int Bx1 = Bx0 + Bw0;
		int By1 = By0 + Bh0;
		
		if(!(Ax0 <= Ax1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. Ax0 is bigger than Ax1");;
		if(!(Ay0 <= Ay1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. Ay0 is bigger than Ay1");;
		if(!(Bx0 <= Bx1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. Bx0 is bigger than Bx1");;
		if(!(By0 <= By1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. By0 is bigger than By1");;

		if (Ax1 < Bx0)	return false;
		if (Ax0 > Bx1)	return false;
		if (Ay1 < By0)	return false;
		if (Ay0 > By1)	return false;
		return true;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Tell if 2 axis aligned rectangle intersect
	///!param rectA top left and bottom right coordinates of first rectangle
	///!param rectB top left and bottom right coordinate of second rectangle
	///!return true if intersect, false if not
	//--------------------------------------------------------------------------------------------------------------------
	public static boolean Math_RectIntersect (int[] rectA, int[] rectB)
	{
		if(!(rectA[0] <= rectA[2]))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. rectA[0] is bigger than rectA[2]");;
		if(!(rectA[1] <= rectA[3]))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. rectA[1] is bigger than rectA[3]");;
		if(!(rectB[0] <= rectB[2]))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. rectB[0] is bigger than rectB[2]");;
		if(!(rectB[1] <= rectB[3]))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. rectB[1] is bigger than rectB[3]");;

		if (rectA[2] < rectB[0])     return false;
		if (rectA[0] > rectB[2])     return false;
		if (rectA[3] < rectB[1])     return false;
		if (rectA[1] > rectB[3])     return false;
		return true;
	}

	public static boolean Math_RectIntersect (int Ax0, int Ay0, int Ax1, int Ay1, int Bx0, int By0, int Bx1, int By1)
	{
		if(!(Ax0 <= Ax1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. Ax0 is bigger than Ax1");;
		if(!(Ay0 <= Ay1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. Ay0 is bigger than Ay1");;
		if(!(Bx0 <= Bx1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. Bx0 is bigger than Bx1");;
		if(!(By0 <= By1))Utils.DBG_PrintStackTrace(false, "Math_RectIntersect. By0 is bigger than By1");;

		if (Ax1 < Bx0)	return false;
		if (Ax0 > Bx1)	return false;
		if (Ay1 < By0)	return false;
		if (Ay0 > By1)	return false;
		return true;
	}


	/**
	 * point x,y, rect,x,y,w,h
	 * @param x
	 * @param y
	 * @param rect
	 * @return
	 */
	public static boolean Math_PointInRect(int x, int y, int[] rect) {
		return Math_Rect2PointXYWHIntersect(x,y,1,1,rect[0],rect[1],rect[2],rect[3]);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Tell if 2 segment intersect themselves.
	///!param (x1,y1)(x2,y2) Coordinate of first segment.
	///!param (x3,y3)(x4,y4) Coordinate of second segment.
	///!return \li MATH_SEGMENTINTERSECT_DO_INTERSECT if intersect.
	/// 		\li MATH_SEGMENTINTERSECT_DONT_INTERSECT if no intersection.
	/// 		\li MATH_SEGMENTINTERSECT_COLLINEAR if two segment are colinear (full or no intersection).
	///!note Coordinates of intersection point are saved in (s_Math_intersectX, s_Math_intersectY).
	///!warning	This function can generate some Overflows if large value are used. Be carefull when
	/// 			using this code with FixedPoint values, the boundaries can change with the value of
	///				zJYLibConfig.math_fixedPointBase.
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_SegmentIntersect (int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
	{
		long Ax, Bx, Cx, Ay, By, Cy;
		int x1lo, x1hi, y1lo, y1hi;

		Ax = x2 - x1;
		Bx = x3 - x4;

		// X bound box tes
		if (Ax < 0)
		{
	  		x1lo = x2; x1hi = x1;
	  	}
	  	else
	  	{
	  		x1hi = x2; x1lo = x1;
	  	}

		if (Bx > 0)
		{
	  		if ((x1hi < x4) || (x3 < x1lo))
	  		{
	  			return MATH_SEGMENTINTERSECT_DONT_INTERSECT;
	  		}
	  	}
	  	else
	  	{
	  		if ((x1hi < x3) || (x4 < x1lo))
	  		{
	  			return MATH_SEGMENTINTERSECT_DONT_INTERSECT;
		  	}
	  	}

		Ay = y2 - y1;
		By = y3 - y4;

		// Y bound box test
		if (Ay < 0)
		{
	  		y1lo = y2; y1hi = y1;
	  	}
	  	else
	  	{
	  		y1hi = y2; y1lo = y1;
	  	}

		if (By > 0)
		{
	  		if((y1hi < y4) || (y3 < y1lo))
	  		{
	  			return MATH_SEGMENTINTERSECT_DONT_INTERSECT;
	  		}
	  	}
	  	else
	  	{
	  		if ((y1hi < y3) || (y4 < y1lo))
	  		{
	  			return MATH_SEGMENTINTERSECT_DONT_INTERSECT;
		  	}
	  	}

		Cx = x1 - x3;
		Cy = y1 - y3;

		long d = (By * Cx) - (Bx * Cy);	// alpha numerator
		long f = (Ay * Bx) - (Ax * By);	// both denominator

		if (f > 0) 			// alpha tests
	  	{
	  		if ((d < 0) || (d > f))
	  		{
	  			return MATH_SEGMENTINTERSECT_DONT_INTERSECT;
	  		}
	  	}
	  	else
	  	{
	  		if ((d > 0) || (d < f))
	  		{
	  			return MATH_SEGMENTINTERSECT_DONT_INTERSECT;
	  		}
	  	}

		long e = (Ax * Cy) - (Ay * Cx);	// beta numerator

		if (f > 0) 			// beta tests
		{
			if ((e < 0) || (e > f))
			{
				return MATH_SEGMENTINTERSECT_DONT_INTERSECT;
			}
		}
		else
		{
			if ((e > 0) || (e < f))
			{
				return MATH_SEGMENTINTERSECT_DONT_INTERSECT;
			}
		}

		//compute intersection coordinates
		if (f == 0)
		{
			return MATH_SEGMENTINTERSECT_COLLINEAR;
		}

		long num, offset;
		num = d * Ax;									// numerator
		offset 	= Math_SameSign(num,f) ? (f>>1) : (-f>>1);	// round direction

		s_Math_intersectX = (int)(x1 + (num+offset) / f);			// intersection x

		num 	= d * Ay;
		offset 	= Math_SameSign(num,f) ? (f>>1) : (-f>>1);

		s_Math_intersectY = (int)(y1 + (num+offset) / f);			// intersection y

		return MATH_SEGMENTINTERSECT_DO_INTERSECT;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Computes the distance from a point to a line.
	/// Calculates the coordinates of the orthogonal projection of the point onto the line as well.
	///
	///!param (x0,y0)(x1,y1) 2 Coordinates on the line (not in fixed point!)
	///!param (x2,y2) Coordinates of the point (not in fixed point!)
	///!return Distance from point to line (orthogonal projection) in fixed point.
	///!note Coordinates of projected point are saved in (s_Math_distPointLineX, s_Math_distPointLineY).\n
	///       These are absolute coordinates and they are in fixed point.
	//--------------------------------------------------------------------------------------------------------------------
	public static int Math_DistPointLine (int x0, int y0, int x1, int y1, int x2, int y2)
	{
		int v0x 	= (x1 - x0);
		int v0y 	= (y1 - y0);
		//int l0 		= Math_sqrt( Math_NormPow(v0x, v0y) );
		int angle0	= Math_Atan(v0x, v0y);

		int v1x 	= (x2 - x0);
		int v1y 	= (y2 - y0);
		int l1 		= Math_Sqrt( Math_NormPow(v1x, v1y) );
		int angle1	= Math_Atan(v1x, v1y);

		int angle01 = Math.abs( angle0 - angle1 );
		int dopp 	= l1 * Math_Sin(angle01);
		int dadj 	= l1 * Math_Cos(angle01);

		s_Math_distPointLineX = Math_FixedPoint_Add(Math_IntToFixedPoint(x0), Math_FixedPoint_Multiply(dadj, Math_Cos(angle0)));
		s_Math_distPointLineY = Math_FixedPoint_Add(Math_IntToFixedPoint(y0), Math_FixedPoint_Multiply(dadj, Math_Sin(angle0)));

		return dopp;
//			//if the angle is obtuse between PA and AB is obtuse then the closest vertex must be A
//			int dotA = (x2 - x0)*(x1 - x0) + (y2 - y0)*(y1 - y0);
//			if (dotA <= 0) return Math_sqrt( Math_NormPow((x0-x2), (y0-y2)) );
	//
//			//if the angle is obtuse between PB and AB is obtuse then the closest vertex must be B
//			double dotB = (x2 - x1)*(x0 - x1) + (y2 - y1)*(y0 - y1);
//			if (dotB <= 0) return Math_sqrt( Math_NormPow((x1-x2), (y1-y2)) );
	//
//			//calculate the point along AB that is the closest to P
//			math_distPointSegmentX = x0 + (int)(((x1 - x0) * dotA)/(dotA + dotB));
//			math_distPointSegmentY = y0 + (int)(((y1 - y0) * dotA)/(dotA + dotB));
	//
//			//calculate the distance P-Point
//			return Math_NormPow((math_distPointSegmentX-x2), (math_distPointSegmentY-y2));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Quicksort an array of integer.
	///!param array Array of data to sort.
	//--------------------------------------------------------------------------------------------------------------------
	final static void Math_QuickSort (int array[])
	{
		Math_Q_Sort(array, 0, array.length - 1);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Utility function for quicksort.
	///!param array Array to sort.
	///!param left Starting position of sorting.
	///!param right Ending position of sorting.
	//--------------------------------------------------------------------------------------------------------------------
	private static void Math_Q_Sort (int array[], int left, int right)
	{
		int pivot, l_hold, r_hold;

		l_hold = left;
		r_hold = right;
		pivot = array[left];

		while (left < right)
		{

			while ((array[right] >= pivot) && (left < right))
			{
				right--;
			}

			if (left != right)
			{
				array[left] = array[right];
				left++;
			}

			while ((array[left] <= pivot) && (left < right))
			{
				left++;
			}

			if (left != right)
			{
				array[right] = array[left];
				right--;
			}
		}
		array[left] = pivot;
		pivot = left;
		left = l_hold;
		right = r_hold;

		if (left < pivot)
		{
			Math_Q_Sort(array, left, pivot-1);
		}

		if (right > pivot)
		{
			Math_Q_Sort(array, pivot+1, right);
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Get an array of indice corresponding to the sorting of an array of data.
	/// Example: data={12, 3, 8} return {1, 2, 0}  so that  data[1] <= data[2] <= data[0].
	///!param data Array of data to scale.
	///!return Array of indices, so that data[indices] are sorted.
	//--------------------------------------------------------------------------------------------------------------------
	public final static int[] Math_QuickSortIndices (int data[])
	{
		return Math_QuickSortIndices(data, 1, 0);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Get an array of indice corresponding to the sorting of an array of data.
	/// Example: data={12, 3, 8} return {1, 2, 0}  so that  data[1] <= data[2] <= data[0].
	///!param data Array of data to scale.
	///!param nbItemPerValue If data is an array of values, how many item are needed per value.
	///!param itemNb If data is an array of values, which item shoudl be considered for sorting.
	///!return Array of indices, so that data[indices] are sorted.
	//--------------------------------------------------------------------------------------------------------------------
	static int[] Math_QuickSortIndices (int data[], int nbItemPerValue, int itemNb)
	{
		Math_quickSortIndices_nbItemPerValue = nbItemPerValue;
		Math_quickSortIndices_itemNb = itemNb;
		Math_quickSortIndices_data = data;

		int size = Math_quickSortIndices_data.length;
		if(!DevConfig.JY_USE_SIMPLE_QUICK_SEARCH) {
			size /= Math_quickSortIndices_nbItemPerValue;
		}
//		int size = Math_quickSortIndices_data.length / Math_quickSortIndices_nbItemPerValue;

		// create result array, and init indices
		if ((Math_quickSortIndices_result != null) && (Math_quickSortIndices_result.length != size))
		{
			Math_quickSortIndices_result = null;
//			Gc(); Guijun: Stupid Behavior, just remove it.
		}

		if (Math_quickSortIndices_result == null)
		{
			Math_quickSortIndices_result = new int[size];
		}

		// init result array
		for (int i=0; i<size; i++)
		{
			Math_quickSortIndices_result[i] = i;
		}

		// sort
		Math_QuickSortIndices(0, size-1);

		return Math_quickSortIndices_result;
	}

	// By Guijun
	static int[] Math_QuickSortIndices (int data[], int nbItemPerValue, int itemNb,int left,int right)
	{
		Math_quickSortIndices_nbItemPerValue = nbItemPerValue;
		Math_quickSortIndices_itemNb = itemNb;
		Math_quickSortIndices_data = data;

		int size = (right-left);
		if(!DevConfig.JY_USE_SIMPLE_QUICK_SEARCH) {
			size /= Math_quickSortIndices_nbItemPerValue;
		}
//		int size = (right-left)  / Math_quickSortIndices_nbItemPerValue;

		// create result array, and init indices
		if ((Math_quickSortIndices_result != null) && (Math_quickSortIndices_result.length != size))
		{
			Math_quickSortIndices_result = null;
		}

		if (Math_quickSortIndices_result == null)
		{
			Math_quickSortIndices_result = new int[size];
		}

		// init result array
		for (int i=size -1 ; i>=0; i--)
		{
			Math_quickSortIndices_result[i] = i;
		}

		// sort
		Math_QuickSortIndices(left, right - 1);

		return Math_quickSortIndices_result;
	}
	//--------------------------------------------------------------------------------------------------------------------
	/// Utility function for Math_QuickSortIndices.
	//--------------------------------------------------------------------------------------------------------------------
	private static void Math_QuickSortIndices(int left, int right)
	{
		int pivot, l_hold, r_hold, pivot_i;

		l_hold = left;
		r_hold = right;

		//pivot = numbers[left];
		pivot_i = Math_quickSortIndices_result[left];
		if(DevConfig.JY_USE_SIMPLE_QUICK_SEARCH) {
			pivot 	= Math_quickSortIndices_data[pivot_i];
		} else {
			pivot 	= Math_quickSortIndices_data[pivot_i*Math_quickSortIndices_nbItemPerValue+Math_quickSortIndices_itemNb];
		}

		while (left < right)
		{
			if(DevConfig.JY_USE_SIMPLE_QUICK_SEARCH) {
				while ((Math_quickSortIndices_data[Math_quickSortIndices_result[right]] >= pivot) && (left < right))
				{
					right--;
				}
			} else {
				while ((Math_quickSortIndices_data[Math_quickSortIndices_result[right]*Math_quickSortIndices_nbItemPerValue+Math_quickSortIndices_itemNb] >= pivot) && (left < right))
				{
					right--;
				}
			}
			

			if (left != right)
			{
				//numbers[left] = numbers[right];
				Math_quickSortIndices_result[left] = Math_quickSortIndices_result[right];
				left++;
			}
			
			if(DevConfig.JY_USE_SIMPLE_QUICK_SEARCH) {
				//while ((numbers[left] <= pivot) && (left < right)) {
				while ((Math_quickSortIndices_data[Math_quickSortIndices_result[left]] <= pivot) && (left < right))
				{
					left++;
				}
			} else {
				//while ((numbers[left] <= pivot) && (left < right)) {
				while ((Math_quickSortIndices_data[Math_quickSortIndices_result[left]*Math_quickSortIndices_nbItemPerValue+Math_quickSortIndices_itemNb] <= pivot) && (left < right))
				{
					left++;
				}
			}		

			if (left != right)
			{
				//numbers[right] = numbers[left];
				Math_quickSortIndices_result[right] = Math_quickSortIndices_result[left];
				right--;
			}
		}

		//numbers[left] 	= pivot;
		Math_quickSortIndices_result[left] 	= pivot_i;

		pivot 				= left;
		left 				= l_hold;
		right 				= r_hold;

		if (left < pivot)
		{
			Math_QuickSortIndices(left, pivot-1);
		}

		if (right > pivot)
		{
			Math_QuickSortIndices(pivot+1, right);
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Bezier interpolation.
	///!return interpolated result based on parameter :)
	//--------------------------------------------------------------------------------------------------------------------
	final private static int Math_BezierUtility (int v1, int v2, int v3, int mum1, int mum12, int mu2)
	{
		return (v1 * mum12 + 2 * v2 * mum1 + v3 * mu2) / (1<<(DevConfig.math_fixedPointBase*2));
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Three control point 2D Bezier interpolation.
	///!param x1,y1 First control point.
	///!param x2,y2 Second control point.
	///!param x3,y3 Third control point.
	///!param interp Interpolation value (ranges from 0 to s_math_F_1).
	///!return Result returned in s_math_bezierX, s_math_bezierY.
	//--------------------------------------------------------------------------------------------------------------------
	static void Math_Bezier2D (int x1, int y1, int x2, int y2, int x3, int y3, int interp)
	{
		int mum1, mum12, mu2;
		mu2 	= interp * interp;
		mum1 	= s_math_F_1 - interp;
		mum12 	= mum1 * mum1;
		mum1 	*= interp;

		s_math_bezierX = Math_BezierUtility(x1, x2, x3, mum1, mum12, mu2);
		s_math_bezierY = Math_BezierUtility(y1, y2, y3, mum1, mum12, mu2);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Three control point 3D Bezier interpolation.
	///!param x1,y1,z1 First control point.
	///!param x2,y2,z2 Second control point.
	///!param x3,y3,z3 Third control point.
	///!param interp Interpolation value (ranges from 0 to s_math_F_1).
	///!return Result returned in s_math_bezierX, s_math_bezierY, s_math_bezierZ.
	//--------------------------------------------------------------------------------------------------------------------
	static void Math_Bezier3D (int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3, int interp)
	{
		int mum1, mum12, mu2;
		mu2 	= interp * interp;
		mum1 	= s_math_F_1 - interp;
		mum12 	= mum1 * mum1;
		mum1 	*= interp;

		s_math_bezierX = Math_BezierUtility(x1, x2, x3, mum1, mum12, mu2);
		s_math_bezierY = Math_BezierUtility(y1, y2, y3, mum1, mum12, mu2);
		s_math_bezierZ = Math_BezierUtility(z1, z2, z3, mum1, mum12, mu2);
	}


	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--- Fixed Point Math functions
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--- Fixed Point Constants and Variables
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------

	// PI in Fixed Point with 29 bits precision readjusted to current GLLib precision
	final static int Math_FixedPoint_PI = 1686629713 >> (29 - DevConfig.math_fixedPointBase);

	// E (Euler's number) in Fixed Point with 29 bits precision readjusted to current GLLib precision
	final static int Math_FixedPoint_E =  1459366444 >> (29 - DevConfig.math_fixedPointBase);

	// Ratio between Radians and Degrees in Fixed point
	private final static int ratioRadiansToDegrees = Math_FixedPoint_Divide(180 << DevConfig.math_fixedPointBase, Math_FixedPoint_PI);

	// Ratio between Degrees and GLLib Angle Fixed Point in Fixed point
	private final static int ratioDegreesToAngleFixedPoint = Math_FixedPoint_Divide(1 << DevConfig.math_angleFixedPointBase, 360);

	static final int 	MATH_INTERSECT_NO_INTERSECT 	= 0;
	static final int 	MATH_INTERSECT_ONE_POINT		= (1 << 1);
	static final int 	MATH_INTERSECT_TWO_POINTS		= (1 << 2);

	static int s_Math_intersectPoints[][] = new int[2][2];


	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--- Basic Fixed Point operations
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Addition for Fixed Point.
	///!param summand1 Number to be added in Fixed Point.
	///!param summand2 Number to be added in Fixed Point.
	///!return Sum of summand1 and summand2 in Fixed Point.
	///!note An assert will indicate if the result overflows Integer.MAX_VALUE.
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_Add(int summand1,  int summand2)
	{
		// Values upcasted to long to avoid Integer overflow
		long result = (long)summand1 + (long)summand2;

		if(!(result <= Integer.MAX_VALUE))Utils.DBG_PrintStackTrace(false, "Math_FixedPoint_Add(): Multiplication Integer Overflow");;

		// Values upcasted to long to avoid Integer overflow
		return (int)result;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Subtraction for Fixed Point.
	///!param minuend Number to be subtracted from in Fixed Point.
	///!param subtrahend Number to be subtracted in Fixed Point.
	///!return Difference of minuend and subtrahend in Fixed Point.
	///!note An assert will indicate if the result overflows Integer.MAX_VALUE.
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_Subtract(int minuend,  int subtrahend)
	{
		// Values upcasted to long to avoid Integer overflow
		long result = (long)minuend - (long)subtrahend;

		if(!(result >= Integer.MIN_VALUE))Utils.DBG_PrintStackTrace(false, "Math_FixedPoint_Add(): Multiplication Integer Overflow");;

		// Values upcasted to long to avoid Integer overflow
		return (int)result;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Multiplication for Fixed Point.
	///!param multiplicand Number to be mutiplied in Fixed Point.
	///!param multiplier Number to be multiplied by in Fixed Point.
	///!return Product of multiplicand and multiplier in Fixed Point.
	///!note An assert will indicate if the result overflows Integer.MAX_VALUE.
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_Multiply(int multiplicand,  int multiplier)
	{
		// Values upcasted to long to avoid Integer overflow
		long result = (((long)multiplicand * (long)multiplier) + (s_math_F_1 >> 1)) >> DevConfig.math_fixedPointBase;

		if(!(result <= Integer.MAX_VALUE))Utils.DBG_PrintStackTrace(false, "Math_FixedPoint_Multiply(): Multiplication Integer Overflow");;

		return (int)result;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Division for Fixed Point.
	///!param dividend Number to be divided in Fixed Point.
	///!param divisor Number to be divided by in Fixed Point.
	///!return Quotient of dividend and divisor in Fixed Point.
	///!note An assert will indicate a division by zero.
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_Divide(int dividend,  int divisor)
	{
		if(!(divisor != 0))Utils.DBG_PrintStackTrace(false, "Math_FixedPoint_Divide(): Division by Zero");;

		//
		// Values upcasted to long to avoid Integer overflow
		return (int)((((long)dividend << DevConfig.math_fixedPointBase << 1) / (divisor)) + 1) >> 1;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Square for Fixed Point.
	///!param value Number to be squared in Fixed Point.
	///!return Squared value in Fixed Point.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPoint_Square(int value)
	{
		return Math_FixedPoint_Multiply(value, value);
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Rounds value to closest whole number in Fixed Point.
	///!param value Value to be rounded in Fixed Point.
	///!return Rounded value in Fixed Point.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPoint_Round(int value)
	{
		// Add 0.5 in fixed point then shift back and forth to set bits after . to 0
		return (((value + (s_math_F_1 >> 1)) >> DevConfig.math_fixedPointBase) << DevConfig.math_fixedPointBase);
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Square Root for Integer Fixed Point values.
	///!param value Number to get square root of.
	///!return Square root of val in Fixed Point.
	///!note Iterative process, precise but can be slow, use with caution.
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_Sqrt(int value)
	{
		// No need to loop for values 1 and 0
		if(value == 0 || value == s_math_F_1)
		{
			return value;
		}

		// Longs used to avoid overflows
		long t, q, b, r;

		//Adjust formulas if zJYLibConfig.math_fixedPointBase is an odd number
		int adj = ((DevConfig.math_fixedPointBase & 1) == 0) ? 0 : 1;

		r = value;
		b = 1 << (30 - adj);
		q = 0;

		while (b >= 256)
		{
			t = q + b;
			if (r >= t)
			{
				r = r - t;
				q = t + b;
			}
			else
			{
			}
			r <<= 1;
			b >>= 1;
		}

		// Shift value back to the correct fixed point base
		q >>= 16 - ((DevConfig.math_fixedPointBase) >> 1) - adj;

		return (int)q;
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Square Root for Long Fixed Point values.
	///!param value Number to get square root of.
	///!return Square root of val in Fixed Point.
	///!note Iterative process, precise but can be slow, use with caution.
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_Sqrt(long value)
	{
		// No need to loop for values 1 and 0
		if(value == 0 || value == s_math_F_1)
		{
			return (int)value;
		}

		//If value fits in an int, use Math_FixedPoint_Sqrt(int value) for better precision
		if(value < Integer.MAX_VALUE)
		{
			return Math_FixedPoint_Sqrt((int)value);
		}

		// Adjust for fixed point base
		int bshft = 30 - DevConfig.math_fixedPointBase;
		int b = 1 << 30;

		int g = 0;
		long val = value;

		do {
			long temp = (long)(g + g + b) << bshft;
			if (val >= temp)
			{
				g += b;
				val -= temp;
			}
			b >>= 1;
		} while (bshft-- >= 0);

		return g;
	}


	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--- Vector operations
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Compute pow of the normal of a vector in Fixed Point.
	///!param x,y X & Y value of the vector in Fixed Point.
	///!return The pow of the normal of the vector in Fixed Point.
	///!note An assert will indicate if the result overflows Integer.MAX_VALUE.
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_NormPow(int x,  int y)
	{
		// Values upcasted to long to avoid Integer overflow
		long result = (((long)x * (long)x) + ((long)y * (long)y) + s_math_F_05) >> DevConfig.math_fixedPointBase;

		if(!(result <= Integer.MAX_VALUE))Utils.DBG_PrintStackTrace(false, "Math_FixedPoint_NormPow(): Multiplication Integer Overflow");;

		return (int)result;
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Nnormal of a vector in Fixed Point.
	///!param x,y X & Y value of the vector in Fixed Point.
	///!returns norm of vector (x,y).
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPoint_Norm(int x, int y)
	{
		// Values upcasted to long to avoid Integer overflow
		return Math_FixedPoint_Sqrt((((long)x * (long)x) + ((long)y * (long)y) + s_math_F_05) >> DevConfig.math_fixedPointBase);
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Compute the determinant of 2 2Dvector in Fixed Point.
	///!param x1,y1 X & Y value of the first vector in Fixed Point.
	///!param x2,y2 X & Y value of the second vector in Fixed Point.
	///!return The determinant of the 2 vectors in Fixed Point.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPoint_Det(int x1,  int y1, int x2, int y2)
	{
		return Math_FixedPoint_Multiply(x1, y2) - Math_FixedPoint_Multiply(x2, y1);
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Compute the dot product of 2 2Dvector in Fixed Point.
	///!param x1,y1 X & Y value of the first vector in Fixed Point.
	///!param x2,y2 X & Y value of the second vector in Fixed Point.
	///!return The dot product of the 2 vectors in Fixed Point.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPoint_DotProduct(int x1,  int y1, int x2, int y2)
	{
		return Math_FixedPoint_Multiply(x1, x2) + Math_FixedPoint_Multiply(y1, y2);
	}

	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--- Trigonometric operations
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	/// Convert an angle from radians to degrees (both fixed point and not, since this is just a ratio)
	///!param angle in radians to be converted into degrees.
	///!return angle in degress. (same precision as input param)
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPoint_RadiansToDegrees(int angle)
	{
		return Math_FixedPoint_Multiply(angle, ratioRadiansToDegrees);
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Convert an angle from degrees to radians (both fixed point and not, since this is just a ratio)
	///!param angle in degrees to be converted into radians.
	///!return angle in radians. (same precision as input param)
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPoint_DegreesToRadians(int angle)
	{
		return Math_FixedPoint_Divide(angle, ratioRadiansToDegrees);
	}

	//--------------------------------------------------------------------------------------------------------------------
	/// Convert an angle from radians to fixed point angle.
	///!param angle in radians to be converted.
	///!return angle in Fixed Point.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPoint_RadiansToAngleFixedPoint(int angle)
	{
		return Math_FixedPoint_Multiply(Math_FixedPoint_Multiply(angle, ratioRadiansToDegrees), ratioDegreesToAngleFixedPoint);
	}


	//--------------------------------------------------------------------------------------------------------------------
	/// Convert an angle from degrees to fixed point angle.
	///!param angle in degrees to be converted.
	///!return angle in Fixed Point.
	//--------------------------------------------------------------------------------------------------------------------
	final static int Math_FixedPoint_DegreesToAngleFixedPoint(int angle)
	{
		return Math_FixedPoint_Multiply(angle, ratioDegreesToAngleFixedPoint);
	}


	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--- Line intersections
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	/// Compute the distance from a point to a segment/line.
	/// Calculate coordinate of orthogonal projection of point on segment as well.
	///!param (in_lineX1,in_lineY1)(in_lineX2,in_lineY2) Coordinate of segment.
	///!param (in_pointX,in_pointY) Coordinate of point.
	///!return Distance from point to segment (orthogonal projection) in fixed point.
	///!note Coordinates of intersection points in fixed Point are saved in (s_Math_distPointLineX, s_Math_distPointLineY).
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_PointLineDistance(int in_lineX1, int in_lineY1, int in_lineX2, int in_lineY2, int in_pointX, int in_pointY)
	{
		int x, y;

		int dx = in_lineX1 - in_lineX2;
		int dy = in_lineY1 - in_lineY2;

		// If line is perfectly vertical
		if(dx == 0)
		{
			x = in_lineX1;
			y = in_pointY;
		}
		// If line is perfectly horizontal
		else if(dy == 0)
		{
			x = in_pointX;
			y = in_lineY1;
		}
		else
		{
			// Find y=mx+b equation of the line
			int m1 = Math_FixedPoint_Divide(dy, dx);
			int b1 = -Math_FixedPoint_Multiply(m1, in_lineX1) + in_lineY1;

			// Find y=mx+b line equation perpendicular to the line and passing by the point
			int m2 = -Math_FixedPoint_Divide(dx, dy);
			int b2 = -Math_FixedPoint_Multiply(m2, in_pointX) + in_pointY;

			// Find intersect point between both lines
			x = Math_FixedPoint_Divide(b1 - b2, m2 - m1);
			y = Math_FixedPoint_Multiply(m1, x) + b1;
		}

		s_Math_distPointLineX = x;
		s_Math_distPointLineY = y;

		return Math_FixedPoint_Norm(x - in_pointX, y - in_pointY);
	}


	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	/// Find intersect points between a line and a circle
	///!param (in_x1, in_y1) first point on the line.
	///!param (in_x2, in_y2) second point on the line.
	///!param (in_circleX, in_circleY) center coordinates of circle.
	///!param (in_radius) radius of the circle.
	///!return MATH_INTERSECT_NO_INTERSECT if no intersect between line and  circle.
	/// 		MATH_INTERSECT_ONE_POINT if only one intersect point between line and circle (Tangeant).
	/// 		MATH_INTERSECT_TWO_POINTS if two intersect ponts between line and  circle.
	///!note	Coordinates of intersection points are saved in the array s_Math_intersectPoints[][]
	///			First index indicates point: 0 = first point and 1 = second point
	///			Second index indicates x or y coordinate: 0 = x coordinate and 1 = y coordinate
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_LineCircleIntersect(int in_x1, int in_y1, int in_x2, int in_y2, int in_circleX, int in_circleY, int in_radius)
	{
		int x1, y1, x2, y2;

		// Translation of line to be in relation to circle at origin
		int sX = in_x1 - in_circleX;
		int sY = in_y1 - in_circleY;
		int eX = in_x2 - in_circleX;
		int eY = in_y2 - in_circleY;

		long dX = eX - sX;
		long dY = eY - sY;

		// Line is perfectly Horizontal
		if(dY == 0)
		{
			// If Y of line not within +/- radius of origin, there is no intersect
			if(eY < -in_radius || eY > in_radius)
			{
				return MATH_INTERSECT_NO_INTERSECT;
			}

			// Place Y in circle equation X^2 + Y^2 = R^2  => X = SQRT(R^2 - Y^2)
			x1 =  Math_FixedPoint_Sqrt(Math_FixedPoint_Square(in_radius) - Math_FixedPoint_Square(eY));
			x2 =  -x1;

			y1 =  eY;
			y2 =  eY;
		}

		// Line is perfectly Vertical
		else if(dX == 0)
		{
			// If X of line not within +/- radius of origin, there is no intersect
			if(eY < -in_radius || eY > in_radius)
			{
				return MATH_INTERSECT_NO_INTERSECT;
			}

			x1 =  eX;
			x2 =  eX;

			// Place X in circle equation X^2 + Y^2 = R^2  => Y = SQRT(R^2 - X^2)
			y1 =  Math_FixedPoint_Sqrt(Math_FixedPoint_Square(in_radius) - Math_FixedPoint_Square(eX));
			y2 =  -y1;
		}
		else
		{
			// Reference
			// http://mathworld.wolfram.com/Circle-LineIntersection.html

			long D = ((long)sX * (long)eY - (long)eX * (long)sY) >> DevConfig.math_fixedPointBase;

			long dR_SQR = (dX*dX + dY*dY) >> DevConfig.math_fixedPointBase;


			// Calculation of the discriminant (radius^2 * dR^2) - D^2
			long discriminant = ((Math_FixedPoint_Square(in_radius) * dR_SQR) - (D * D)) >> DevConfig.math_fixedPointBase;

			if(	discriminant < 0 ||	// Line is Tangent or there is no intersection
				dR_SQR == 0)		// No Slope (would result in divison by zero)
			{
				return MATH_INTERSECT_NO_INTERSECT;
			}


			// Line intersects at one point, indiacting the line is tangeant to the circle
			if(discriminant == 0)
			{
				// Calculate intersection point
				x2 = x1 = (int)(D * dY / dR_SQR);
				y2 = y1 = (int)(D * dX / dR_SQR);
			}

			// Line intersects circle in 2 points
			else
			{
				long sqrtDisc = Math_FixedPoint_Sqrt(discriminant);

				// Calculation of coordinate equation for X components
				long tmp1 = D * dY;
				long tmp2 = ((dY < 0) ?( -1):(1)) * dX * sqrtDisc;

				x1 =  (int)((tmp1 + tmp2) / dR_SQR);
				x2 =  (int)((tmp1 - tmp2) / dR_SQR);

				// Calculation of coordinate equation for Y components
				tmp1 = -D * dX;
				tmp2 = Math.abs(dY) * sqrtDisc;

				y1 =  (int)((tmp1 + tmp2) / dR_SQR);
				y2 =  (int)((tmp1 - tmp2) / dR_SQR);
			}
		}

		// Reposition intersect point in relation to center of circle
		s_Math_intersectPoints[0][0] = x1 + in_circleX;
		s_Math_intersectPoints[0][1] = y1 + in_circleY;
		s_Math_intersectPoints[1][0] = x2 + in_circleX;
		s_Math_intersectPoints[1][1] = y2 + in_circleY;

		return (x1 == x2 && y1 == y2) ? (MATH_INTERSECT_ONE_POINT) : (MATH_INTERSECT_TWO_POINTS);
	}


	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	/// Find intersect points between a line and a rectangle.
	///!param (in_x1, in_y1) first point on the line.
	///!param (in_x2, in_y2) second point on the line.
	///!param (in_rectX, in_rectY) coordinates of top-left corner of rectangle.
	///!param (in_rectW, in_rectH) Width and Height of rectangle.
	///!return MATH_INTERSECT_NO_INTERSECT if no intersect between line and rectangle.
	/// 		MATH_INTERSECT_ONE_POINT if only one intersect point between line and rectangle .
	/// 		MATH_INTERSECT_TWO_POINTS if two intersect points between line and  circle.
	///!note	Coordinates of intersection points are saved in the array s_Math_intersectPoints[][]
	///			First index indicates point: 0 = first point and 1 = second point
	///			Second index indicates x or y coordinate: 0 = x coordinate and 1 = y coordinate
	//--------------------------------------------------------------------------------------------------------------------
	static int Math_FixedPoint_LineRectangleIntersect(int in_x1, int in_y1, int in_x2, int in_y2, int in_rectX, int in_rectY, int in_rectW, int in_rectH)
	{
		int x1, y1, x2, y2;

		// Get coordiantes of rectangle
		int leftX	= in_rectX;
		int rightX	= in_rectX + in_rectW;
		int topY	= in_rectY;
		int bottomY	= in_rectY + in_rectH;

		// Line is perfectly Vertical
		if((in_x1 - in_x2) == 0)
		{
			// If x not within rectangle left and right there are no intersects
			if(in_x2 < leftX || in_x2 > rightX)
			{
				return MATH_INTERSECT_NO_INTERSECT;
			}

			x1 = in_x2;
			y1 = topY;
			x2 = in_x2;
			y2 = bottomY;
		}

		else
		{
			// Calculate slope of line for y = mx + b equation
			int m = Math_FixedPoint_Divide(in_y1 - in_y2, in_x1 - in_x2);

			// Line is perfectly Horizontal
			if(m == 0)
			{
				// If y not within rectangle top and bottem there are no intersects
				if(in_y2 < topY || in_y2 > bottomY)
				{
					return MATH_INTERSECT_NO_INTERSECT;
				}

				x1 = leftX;
				y1 = in_y2;
				x2 = rightX;
				y2 = in_y2;
			}
			else
			{

				// Calculate zero of line for y=mx+b equation
				int b = -Math_FixedPoint_Multiply(m, in_x2) + in_y2;

				// Calculate the y value for the left x limit of the rectangle
				x1 = leftX;
				y1 = Math_FixedPoint_Multiply(m, x1) + b;

				// If y value for x outside of the y limits of zone it means it hit a horizontal side before a vertical one.
				// Calculate the x value for the y limit of the zone
				if(y1 < topY || y1 > bottomY)
				{
					y1 = (y1 < topY) ? topY : bottomY;
					x1 = Math_FixedPoint_Divide(b - y1, -m);

					if(x1 < leftX || x1 > rightX)
					{
						return MATH_INTERSECT_NO_INTERSECT;
					}
				}

				// Calculate the y value for right x limit of the rectangle
				x2 = rightX;
				y2 = Math_FixedPoint_Multiply(m, x2) + b;

				// If y value for x outside of the y limits of zone it means it hit a horizontal side before a vertical one.
				// Calculate the x value for the y limit of the zone
				if(y2 < topY || y2 > bottomY)
				{
					y2 = (y2 < topY) ? topY : bottomY;
					x2 = Math_FixedPoint_Divide(b - y2, -m);

					if(x2 < leftX || x2 > rightX)
					{
						return MATH_INTERSECT_NO_INTERSECT;
					}
				}
			}
		}

		// Reposition intersect point in relation to center of circle
		s_Math_intersectPoints[0][0] = x1;
		s_Math_intersectPoints[0][1] = y1;
		s_Math_intersectPoints[1][0] = x2;
		s_Math_intersectPoints[1][1] = y2;

		return (x1 == x2 && y1 == y2) ? (MATH_INTERSECT_ONE_POINT) : (MATH_INTERSECT_TWO_POINTS);
	}

	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--- Fixed Point Conversions
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	/// Converts a GLLib Fixed Point number to a string in base 10
	///!param value Fixed Point value to be converted.
	///!param precision Number or of decimals to print.
	///!return String representaion of Fixed Point number in base 10
	///
	///!note This function does not take language into account, it is mainly to be used for debugging\n
	///       If you have a need for a function that does take language into account to be used for release please request it (Currently on TODO).
	//--------------------------------------------------------------------------------------------------------------------

	static String ConvertFixedPointToString (int value, int precision)
	{
		if(precision == 0)
		{
			return "" + (value >> DevConfig.math_fixedPointBase);
		}

	    String str = (value < 0) ? "-" : "";
		int tmp;
	    value = ((value)<0 ? -(value) : (value));

		for(int i = -1; i < precision; i++)
		{
			tmp = value >> DevConfig.math_fixedPointBase;
	        str = str + tmp;

	        if (i == -1)
	        {
	            str = str + ".";
	        }

			value -= (tmp * s_math_F_1);
			value *= 10;
		}

		return str;
	}


	/*
	final static double Math_Abs(double a)
	{
		return Math.abs(a);
	}
	final static float Math_Abs(float a)
	{
		return Math.abs(a);
	}
	*/
	public final static int Math_Abs(int a)
	{
		return Math.abs(a);
	}
	public final static long Math_Abs(long a)
	{
		return Math.abs(a);
	}
	/*
	final static double Math_Max(double a, double b)
	{
		return Math.max(a, b);
	}
	final static float Math_Max(float a, float b)
	{
		return Math.max(a, b);
	}
	*/
	final static int Math_Max(int a, int b)
	{
		return Math.max(a, b);
	}
	final static long Math_Max(long a, long b)
	{
		return Math.max(a, b);
	}
	/*
	final static double Math_Min(double a, double b)
	{
		return Math.min(a, b);
	}
	final static float 	Math_Min(float a, float b)
	{
		return Math.min(a, b);
	}
	*/
	final static int Math_Min(int a, int b)
	{
		return Math.min(a, b);
	}
	final static long Math_Min(long a, long b)
	{
		return Math.min(a, b);
	}
	/*
	final static double Math_Cos(double a)
	{
		return Math.cos(a);
	}
	final static double Math_Sin(double a)
	{
		return Math.sin(a);
	}
	final static double Math_Tan(double a)
	{
		return Math.tan(a);
	}

	final static double Math_Sqrt(double a)
	{
		return Math.sqrt(a);
	}


	final static double Math_ToDegrees(double angrad)
	{
		return Math.toDegrees(a);
	}
	final static double Math_ToRadians(double angdeg)
	{
		return Math.toRadians(a);
	}

	final static double Math_Ceil(double a)
	{
		return Math.ceil(a);
	}
	final static double Math_Floor(double a)
	{
		return Math.floor(a);
	}
	*/
	///!}
	// ///////////////////////////////
	// about fixed point

	public static final int F2I(int f) {
		return Math_FixedPointToInt(f);
	}

	public static final int I2F(int i) {
		return Math_IntToFixedPoint(i);
	}

	public static final int F_ADD(int f1, int f2) {
		return Math_FixedPoint_Add(f1, f2);
	}

	public static final int F_SUB(int f_minuend, int f_subtrahend) {
		return Math_FixedPoint_Subtract(f_minuend, f_subtrahend);
	}

	public static final int F_DIV(int f_dividend, int f_divisor) {
		return Math_FixedPoint_Divide(f_dividend, f_divisor);
	}

	public static final int F_MUL(int f1, int f2) {
		return Math_FixedPoint_Multiply(f1, f2);
	}

	public static final int F_ROUND_2_I(int f) {
		return ((f & (s_math_F_1 - 1)) > (s_math_F_05) ? F2I(f) + 1
				: F2I(f));
	}

	// //////////////////////////
	// math

	/**
	 * 传入坐标是F
	 */
	public static int getAngle(int source_x, int source_y, int tar_x,
			int tar_y) {
		return Math_FixedPointAngleToDegree(GU_GetAngle(
				F2I(source_x), F2I(source_y), F2I(tar_x),
				F2I(tar_y)));
	}

	static int GU_GetAngle(int centerX, int centerY, int pointX, int pointY) {
		return GU_GetAngle(centerX, centerY, pointX, pointY, true);
	}

	static int GU_GetAngle(int centerX, int centerY, int pointX, int pointY,
			boolean apply) {
		int objX = pointX - centerX;

		int objY = -(pointY - centerY);//
		if (apply) {
			objX = I2F(objX);
			objY = I2F(objY);// ;
		}
		int angle = Math_Atan(objX, objY); // FixedPointAngle

		return angle;
	}


	public static void UpdatePos(int[] targetXYZ,int[] speed,int[] curXYZ)
	{
		int count = targetXYZ.length;
		int delta;
		int linespeed;
		for(int i = 0;i<count;i++)
		{
			if (targetXYZ[i]<curXYZ[i])
				{
				linespeed = -speed[i];
				}
			else
			{
				linespeed = speed[i];
			}
			curXYZ[i]+=linespeed;
			if (Math_Abs(curXYZ[i] - targetXYZ[i])<speed[i])
				curXYZ[i] = targetXYZ[i];
		}
	}
	
}
