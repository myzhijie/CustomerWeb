package com.jingsky.customer.util.base;

import java.lang.reflect.Array;
import java.util.Arrays;

public abstract class Objects {

	private static final int INITIAL_HASH = 7;
	private static final int MULTIPLIER = 31;

	private static final String EMPTY_STRING = "";
	private static final String NULL_STRING = "null";
	private static final String ARRAY_START = "{";
	private static final String ARRAY_END = "}";
	private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
	private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

	/**
	 * Return whether the given throwable is a checked exception: that is, neither a RuntimeException nor an Error.
	 *
	 * @param ex
	 *            the throwable to check
	 * @return whether the throwable is a checked exception
	 * @see Exception
	 * @see RuntimeException
	 * @see Error
	 */
	public static boolean isCheckedException(Throwable ex) {
		return !(ex instanceof RuntimeException || ex instanceof Error);
	}

	/**
	 * Check whether the given exception is compatible with the exceptions declared in a throws clause.
	 *
	 * @param ex
	 *            the exception to checked
	 * @param declaredExceptions
	 *            the exceptions declared in the throws clause
	 * @return whether the given exception is compatible
	 */
	public static boolean isCompatibleWithThrowsClause(Throwable ex, Class<?>[] declaredExceptions) {
		if (!isCheckedException(ex)) {
			return true;
		}
		if (declaredExceptions != null) {
			int i = 0;
			while (i < declaredExceptions.length) {
				if (declaredExceptions[i].isAssignableFrom(ex.getClass())) {
					return true;
				}
				i++;
			}
		}
		return false;
	}

	/**
	 * Determine whether the given object is an array: either an Object array or a primitive array.
	 *
	 * @param obj
	 *            the object to check
	 */
	public static boolean isArray(Object obj) {
		return (obj != null && obj.getClass().isArray());
	}

	/**
	 * Determine whether the given array is empty: i.e. <code>null</code> or of zero length.
	 *
	 * @param array
	 *            the array to check
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * Check whether the given array contains the given element.
	 *
	 * @param array
	 *            the array to check (may be <code>null</code>, in which case the return value will always be <code>false</code>)
	 * @param element
	 *            the element to check for
	 * @return whether the element has been found in the given array
	 */
	public static boolean containsElement(Object[] array, Object element) {
		if (array == null) {
			return false;
		}
		for (Object arrayEle : array) {
			if (nullSafeEquals(arrayEle, element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given array of enum constants contains a constant with the given name, ignoring case when determining a match.
	 *
	 * @param enumValues
	 *            the enum values to check, typically the product of a call to MyEnum.values()
	 * @param constant
	 *            the constant name to find (must not be null or empty string)
	 * @return whether the constant has been found in the given array
	 */
	public static boolean containsConstant(Enum<?>[] enumValues, String constant) {
		return containsConstant(enumValues, constant, false);
	}

	/**
	 * Check whether the given array of enum constants contains a constant with the given name.
	 *
	 * @param enumValues
	 *            the enum values to check, typically the product of a call to MyEnum.values()
	 * @param constant
	 *            the constant name to find (must not be null or empty string)
	 * @param caseSensitive
	 *            whether case is significant in determining a match
	 * @return whether the constant has been found in the given array
	 */
	public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
		for (Enum<?> candidate : enumValues) {
			if (caseSensitive ? candidate.toString().equals(constant) : candidate.toString().equalsIgnoreCase(constant)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Case insensitive alternative to {@link Enum#valueOf(Class, String)}.
	 *
	 * @param <E>
	 *            the concrete Enum type
	 * @param enumValues
	 *            the array of all Enum constants in question, usually per Enum.values()
	 * @param constant
	 *            the constant to get the enum value of
	 * @throws IllegalArgumentException
	 *             if the given constant is not found in the given array of enum values. Use {@link #containsConstant(Enum[], String)} as a guard to
	 *             avoid this exception.
	 */
	public static <E extends Enum<?>> E caseInsensitiveValueOf(E[] enumValues, String constant) {
		for (E candidate : enumValues) {
			if (candidate.toString().equalsIgnoreCase(constant)) {
				return candidate;
			}
		}
		throw new IllegalArgumentException(String.format("constant [%s] does not exist in enum type %s", constant, enumValues.getClass()
				.getComponentType().getName()));
	}

	/**
	 * Append the given object to the given array, returning a new array consisting of the input array contents plus the given object.
	 *
	 * @param array
	 *            the array to append to (can be <code>null</code>)
	 * @param obj
	 *            the object to append
	 * @return the new array (of the same component type; never <code>null</code>)
	 */
	public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
		Class<?> compType = Object.class;
		if (array != null) {
			compType = array.getClass().getComponentType();
		} else if (obj != null) {
			compType = obj.getClass();
		}
		int newArrLength = (array != null ? array.length + 1 : 1);
		@SuppressWarnings("unchecked")
		A[] newArr = (A[]) Array.newInstance(compType, newArrLength);
		if (array != null) {
			System.arraycopy(array, 0, newArr, 0, array.length);
		}
		newArr[newArr.length - 1] = obj;
		return newArr;
	}

	/**
	 * Convert the given array (which may be a primitive array) to an object array (if necessary of primitive wrapper objects).
	 * <p>
	 * A <code>null</code> source value will be converted to an empty Object array.
	 *
	 * @param source
	 *            the (potentially primitive) array
	 * @return the corresponding object array (never <code>null</code>)
	 * @throws IllegalArgumentException
	 *             if the parameter is not an array
	 */
	public static Object[] toObjectArray(Object source) {
		if (source instanceof Object[]) {
			return (Object[]) source;
		}
		if (source == null) {
			return new Object[0];
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}
		int length = Array.getLength(source);
		if (length == 0) {
			return new Object[0];
		}
		Class<?> wrapperType = Array.get(source, 0).getClass();
		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(source, i);
		}
		return newArray;
	}

	// ---------------------------------------------------------------------
	// Convenience methods for content-based equality/hash-code handling
	// ---------------------------------------------------------------------

	/**
	 * Determine if the given objects are equal, returning <code>true</code> if both are <code>null</code> or <code>false</code> if only one is
	 * <code>null</code>.
	 * <p>
	 * Compares arrays with <code>Arrays.equals</code>, performing an equality check based on the array elements rather than the array reference.
	 *
	 * @param o1
	 *            first Object to compare
	 * @param o2
	 *            second Object to compare
	 * @return whether the given objects are equal
	 * @see Arrays#equals
	 */
	public static boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		}
		if (o1.getClass().isArray() && o2.getClass().isArray()) {
			if (o1 instanceof Object[] && o2 instanceof Object[]) {
				return Arrays.equals((Object[]) o1, (Object[]) o2);
			}
			if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
				return Arrays.equals((boolean[]) o1, (boolean[]) o2);
			}
			if (o1 instanceof byte[] && o2 instanceof byte[]) {
				return Arrays.equals((byte[]) o1, (byte[]) o2);
			}
			if (o1 instanceof char[] && o2 instanceof char[]) {
				return Arrays.equals((char[]) o1, (char[]) o2);
			}
			if (o1 instanceof double[] && o2 instanceof double[]) {
				return Arrays.equals((double[]) o1, (double[]) o2);
			}
			if (o1 instanceof float[] && o2 instanceof float[]) {
				return Arrays.equals((float[]) o1, (float[]) o2);
			}
			if (o1 instanceof int[] && o2 instanceof int[]) {
				return Arrays.equals((int[]) o1, (int[]) o2);
			}
			if (o1 instanceof long[] && o2 instanceof long[]) {
				return Arrays.equals((long[]) o1, (long[]) o2);
			}
			if (o1 instanceof short[] && o2 instanceof short[]) {
				return Arrays.equals((short[]) o1, (short[]) o2);
			}
		}
		return false;
	}

	/**
	 * Return as hash code for the given object; typically the value of <code>{@link Object#hashCode()}</code>. If the object is an array, this method
	 * will delegate to any of the <code>nullSafeHashCode</code> methods for arrays in this class. If the object is <code>null</code>, this method
	 * returns 0.
	 *
	 * @see #nullSafeHashCode(Object[])
	 * @see #nullSafeHashCode(boolean[])
	 * @see #nullSafeHashCode(byte[])
	 * @see #nullSafeHashCode(char[])
	 * @see #nullSafeHashCode(double[])
	 * @see #nullSafeHashCode(float[])
	 * @see #nullSafeHashCode(int[])
	 * @see #nullSafeHashCode(long[])
	 * @see #nullSafeHashCode(short[])
	 */
	public static int nullSafeHashCode(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj.getClass().isArray()) {
			if (obj instanceof Object[]) {
				return nullSafeHashCode((Object[]) obj);
			}
			if (obj instanceof boolean[]) {
				return nullSafeHashCode((boolean[]) obj);
			}
			if (obj instanceof byte[]) {
				return nullSafeHashCode((byte[]) obj);
			}
			if (obj instanceof char[]) {
				return nullSafeHashCode((char[]) obj);
			}
			if (obj instanceof double[]) {
				return nullSafeHashCode((double[]) obj);
			}
			if (obj instanceof float[]) {
				return nullSafeHashCode((float[]) obj);
			}
			if (obj instanceof int[]) {
				return nullSafeHashCode((int[]) obj);
			}
			if (obj instanceof long[]) {
				return nullSafeHashCode((long[]) obj);
			}
			if (obj instanceof short[]) {
				return nullSafeHashCode((short[]) obj);
			}
		}
		return obj.hashCode();
	}

	/**
	 * Return a hash code based on the contents of the specified array. If <code>array</code> is <code>null</code>, this method returns 0.
	 */
	public static int nullSafeHashCode(Object[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + nullSafeHashCode(array[i]);
		}
		return hash;
	}

	/**
	 * Return a hash code based on the contents of the specified array. If <code>array</code> is <code>null</code>, this method returns 0.
	 */
	public static int nullSafeHashCode(boolean[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + hashCode(array[i]);
		}
		return hash;
	}

	/**
	 * Return a hash code based on the contents of the specified array. If <code>array</code> is <code>null</code>, this method returns 0.
	 */
	public static int nullSafeHashCode(byte[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + array[i];
		}
		return hash;
	}

	/**
	 * Return a hash code based on the contents of the specified array. If <code>array</code> is <code>null</code>, this method returns 0.
	 */
	public static int nullSafeHashCode(char[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + array[i];
		}
		return hash;
	}

	/**
	 * Return a hash code based on the contents of the specified array. If <code>array</code> is <code>null</code>, this method returns 0.
	 */
	public static int nullSafeHashCode(double[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + hashCode(array[i]);
		}
		return hash;
	}

	/**
	 * Return a hash code based on the contents of the specified array. If <code>array</code> is <code>null</code>, this method returns 0.
	 */
	public static int nullSafeHashCode(float[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + hashCode(array[i]);
		}
		return hash;
	}

	/**
	 * Return a hash code based on the contents of the specified array. If <code>array</code> is <code>null</code>, this method returns 0.
	 */
	public static int nullSafeHashCode(int[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + array[i];
		}
		return hash;
	}

	/**
	 * Return a hash code based on the contents of the specified array. If <code>array</code> is <code>null</code>, this method returns 0.
	 */
	public static int nullSafeHashCode(long[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + hashCode(array[i]);
		}
		return hash;
	}

	/**
	 * Return a hash code based on the contents of the specified array. If <code>array</code> is <code>null</code>, this method returns 0.
	 */
	public static int nullSafeHashCode(short[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + array[i];
		}
		return hash;
	}

	/**
	 * Return the same value as <code>{@link Boolean#hashCode()}</code>.
	 *
	 * @see Boolean#hashCode()
	 */
	public static int hashCode(boolean bool) {
		return bool ? 1231 : 1237;
	}

	/**
	 * Return the same value as <code>{@link Double#hashCode()}</code>.
	 *
	 * @see Double#hashCode()
	 */
	public static int hashCode(double dbl) {
		long bits = Double.doubleToLongBits(dbl);
		return hashCode(bits);
	}

	/**
	 * Return the same value as <code>{@link Float#hashCode()}</code>.
	 *
	 * @see Float#hashCode()
	 */
	public static int hashCode(float flt) {
		return Float.floatToIntBits(flt);
	}

	/**
	 * Return the same value as <code>{@link Long#hashCode()}</code>.
	 *
	 * @see Long#hashCode()
	 */
	public static int hashCode(long lng) {
		return (int) (lng ^ (lng >>> 32));
	}

	// ---------------------------------------------------------------------
	// Convenience methods for toString output
	// ---------------------------------------------------------------------

	/**
	 * Return a String representation of an object's overall identity.
	 *
	 * @param obj
	 *            the object (may be <code>null</code>)
	 * @return the object's identity as String representation, or an empty String if the object was <code>null</code>
	 */
	public static String identityToString(Object obj) {
		if (obj == null) {
			return EMPTY_STRING;
		}
		return obj.getClass().getName() + "@" + getIdentityHexString(obj);
	}

	/**
	 * Return a hex String form of an object's identity hash code.
	 *
	 * @param obj
	 *            the object
	 * @return the object's identity code in hex notation
	 */
	public static String getIdentityHexString(Object obj) {
		return Integer.toHexString(System.identityHashCode(obj));
	}

	/**
	 * Return a content-based String representation if <code>obj</code> is not <code>null</code>; otherwise returns an empty String.
	 * <p>
	 * Differs from {@link #nullSafeToString(Object)} in that it returns an empty String rather than "null" for a <code>null</code> value.
	 *
	 * @param obj
	 *            the object to build a display String for
	 * @return a display String representation of <code>obj</code>
	 * @see #nullSafeToString(Object)
	 */
	public static String getDisplayString(Object obj) {
		if (obj == null) {
			return EMPTY_STRING;
		}
		return nullSafeToString(obj);
	}

	/**
	 * Determine the class name for the given object.
	 * <p>
	 * Returns <code>"null"</code> if <code>obj</code> is <code>null</code>.
	 *
	 * @param obj
	 *            the object to introspect (may be <code>null</code>)
	 * @return the corresponding class name
	 */
	public static String nullSafeClassName(Object obj) {
		return (obj != null ? obj.getClass().getName() : NULL_STRING);
	}

	/**
	 * Return a String representation of the specified Object.
	 * <p>
	 * Builds a String representation of the contents in case of an array. Returns <code>"null"</code> if <code>obj</code> is <code>null</code>.
	 *
	 * @param obj
	 *            the object to build a String representation for
	 * @return a String representation of <code>obj</code>
	 */
	public static String nullSafeToString(Object obj) {
		if (obj == null) {
			return NULL_STRING;
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		if (obj instanceof Object[]) {
			return nullSafeToString((Object[]) obj);
		}
		if (obj instanceof boolean[]) {
			return nullSafeToString((boolean[]) obj);
		}
		if (obj instanceof byte[]) {
			return nullSafeToString((byte[]) obj);
		}
		if (obj instanceof char[]) {
			return nullSafeToString((char[]) obj);
		}
		if (obj instanceof double[]) {
			return nullSafeToString((double[]) obj);
		}
		if (obj instanceof float[]) {
			return nullSafeToString((float[]) obj);
		}
		if (obj instanceof int[]) {
			return nullSafeToString((int[]) obj);
		}
		if (obj instanceof long[]) {
			return nullSafeToString((long[]) obj);
		}
		if (obj instanceof short[]) {
			return nullSafeToString((short[]) obj);
		}
		String str = obj.toString();
		return (str != null ? str : EMPTY_STRING);
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>
	 * The String representation consists of a list of the array's elements, enclosed in curly braces (<code>"{}"</code> ). Adjacent elements are
	 * separated by the characters <code>", "</code> (a comma followed by a space). Returns <code>"null"</code> if <code>array</code> is
	 * <code>null</code>.
	 *
	 * @param array
	 *            the array to build a String representation for
	 * @return a String representation of <code>array</code>
	 */
	public static String nullSafeToString(Object[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			} else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(String.valueOf(array[i]));
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>
	 * The String representation consists of a list of the array's elements, enclosed in curly braces (<code>"{}"</code> ). Adjacent elements are
	 * separated by the characters <code>", "</code> (a comma followed by a space). Returns <code>"null"</code> if <code>array</code> is
	 * <code>null</code>.
	 *
	 * @param array
	 *            the array to build a String representation for
	 * @return a String representation of <code>array</code>
	 */
	public static String nullSafeToString(boolean[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			} else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}

			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>
	 * The String representation consists of a list of the array's elements, enclosed in curly braces (<code>"{}"</code> ). Adjacent elements are
	 * separated by the characters <code>", "</code> (a comma followed by a space). Returns <code>"null"</code> if <code>array</code> is
	 * <code>null</code>.
	 *
	 * @param array
	 *            the array to build a String representation for
	 * @return a String representation of <code>array</code>
	 */
	public static String nullSafeToString(byte[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			} else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>
	 * The String representation consists of a list of the array's elements, enclosed in curly braces (<code>"{}"</code> ). Adjacent elements are
	 * separated by the characters <code>", "</code> (a comma followed by a space). Returns <code>"null"</code> if <code>array</code> is
	 * <code>null</code>.
	 *
	 * @param array
	 *            the array to build a String representation for
	 * @return a String representation of <code>array</code>
	 */
	public static String nullSafeToString(char[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			} else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append("'").append(array[i]).append("'");
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>
	 * The String representation consists of a list of the array's elements, enclosed in curly braces (<code>"{}"</code> ). Adjacent elements are
	 * separated by the characters <code>", "</code> (a comma followed by a space). Returns <code>"null"</code> if <code>array</code> is
	 * <code>null</code>.
	 *
	 * @param array
	 *            the array to build a String representation for
	 * @return a String representation of <code>array</code>
	 */
	public static String nullSafeToString(double[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			} else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}

			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>
	 * The String representation consists of a list of the array's elements, enclosed in curly braces (<code>"{}"</code> ). Adjacent elements are
	 * separated by the characters <code>", "</code> (a comma followed by a space). Returns <code>"null"</code> if <code>array</code> is
	 * <code>null</code>.
	 *
	 * @param array
	 *            the array to build a String representation for
	 * @return a String representation of <code>array</code>
	 */
	public static String nullSafeToString(float[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			} else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}

			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>
	 * The String representation consists of a list of the array's elements, enclosed in curly braces (<code>"{}"</code> ). Adjacent elements are
	 * separated by the characters <code>", "</code> (a comma followed by a space). Returns <code>"null"</code> if <code>array</code> is
	 * <code>null</code>.
	 *
	 * @param array
	 *            the array to build a String representation for
	 * @return a String representation of <code>array</code>
	 */
	public static String nullSafeToString(int[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			} else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>
	 * The String representation consists of a list of the array's elements, enclosed in curly braces (<code>"{}"</code> ). Adjacent elements are
	 * separated by the characters <code>", "</code> (a comma followed by a space). Returns <code>"null"</code> if <code>array</code> is
	 * <code>null</code>.
	 *
	 * @param array
	 *            the array to build a String representation for
	 * @return a String representation of <code>array</code>
	 */
	public static String nullSafeToString(long[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			} else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>
	 * The String representation consists of a list of the array's elements, enclosed in curly braces (<code>"{}"</code> ). Adjacent elements are
	 * separated by the characters <code>", "</code> (a comma followed by a space). Returns <code>"null"</code> if <code>array</code> is
	 * <code>null</code>.
	 *
	 * @param array
	 *            the array to build a String representation for
	 * @return a String representation of <code>array</code>
	 */
	public static String nullSafeToString(short[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			} else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * String -> int，如果转换不成功则返回默认值 d
	 */
	public final static int strToInt(String s, int d) {
		int returnVal;
		try {
			returnVal = Integer.parseInt(safeTrimString(s));
		} catch (Exception e) {
			returnVal = d;
		}
		return returnVal;
	}

	/**
	 * String -> int，如果转换不成功则返回默认值 d
	 */
	public final static Integer strToInt(String s) {
		Integer returnVal = null;
		try {
			returnVal = Integer.parseInt(safeTrimString(s));
		} catch (Exception e) {
		}
		return returnVal;
	}

	/**
	 * String -> int，如果转换不成功则返回 0
	 */
	public final static int strToInt_0(String s) {
		return strToInt(s, 0);
	}

	/**
	 * String -> Short，如果转换不成功则返回 null
	 */
	public final static Short strToShort(String s) {
		Short returnVal;
		try {
			returnVal = Short.decode(safeTrimString(s));
		} catch (Exception e) {
			returnVal = null;
		}
		return returnVal;
	}

	/**
	 * String -> short，如果转换不成功则返回默认值 d
	 */
	public final static short strToShort(String s, short d) {
		short returnVal;
		try {
			returnVal = Short.parseShort(safeTrimString(s));
		} catch (Exception e) {
			returnVal = d;
		}
		return returnVal;
	}

	/**
	 * String -> short，如果转换不成功则返回 0
	 */
	public final static short strToShort_0(String s) {
		return strToShort(s, (short) 0);
	}

	/**
	 * String -> Long，如果转换不成功则返回 null
	 */
	public final static Long strToLong(String s) {
		Long returnVal;
		try {
			returnVal = Long.decode(safeTrimString(s));
		} catch (Exception e) {
			returnVal = null;
		}
		return returnVal;
	}

	/**
	 * String -> long，如果转换不成功则返回默认值 d
	 */
	public final static long strToLong(String s, long d) {
		long returnVal;
		try {
			returnVal = Long.parseLong(safeTrimString(s));
		} catch (Exception e) {
			returnVal = d;
		}
		return returnVal;
	}

	/**
	 * String -> long，如果转换不成功则返回 0
	 */
	public final static long strToLong_0(String s) {
		return strToLong(s, 0L);
	}

	/**
	 * String -> Float，如果转换不成功则返回 null
	 */
	public final static Float strToFloat(String s) {
		Float returnVal;
		try {
			returnVal = Float.valueOf(safeTrimString(s));
		} catch (Exception e) {
			returnVal = null;
		}
		return returnVal;
	}

	/**
	 * String -> float，如果转换不成功则返回默认值 d
	 */
	public final static float strToFloat(String s, float d) {
		float returnVal;
		try {
			returnVal = Float.parseFloat(safeTrimString(s));
		} catch (Exception e) {
			returnVal = d;
		}
		return returnVal;
	}

	/**
	 * String -> float，如果转换不成功则返回 0
	 */
	public final static float strToFloat_0(String s) {
		return strToFloat(s, 0F);
	}

	/**
	 * String -> Double，如果转换不成功则返回 null
	 */
	public final static Double strToDouble(String s) {
		Double returnVal;
		try {
			returnVal = Double.valueOf(safeTrimString(s));
		} catch (Exception e) {
			returnVal = null;
		}
		return returnVal;
	}

	/**
	 * String -> double，如果转换不成功则返回默认值 d
	 */
	public final static double strToDouble(String s, double d) {
		double returnVal;
		try {
			returnVal = Double.parseDouble(safeTrimString(s));
		} catch (Exception e) {
			returnVal = d;
		}
		return returnVal;
	}

	/**
	 * String -> double，如果转换不成功则返回 0.0
	 */
	public final static double strToDouble_0(String s) {
		return strToDouble(s, 0D);
	}

	/**
	 * String -> Byte，如果转换不成功则返回 null
	 */
	public final static Byte strToByte(String s) {
		Byte returnVal;
		try {
			returnVal = Byte.decode(safeTrimString(s));
		} catch (Exception e) {
			returnVal = null;
		}
		return returnVal;
	}

	/**
	 * String -> byte，如果转换不成功则返回默认值 d
	 */
	public final static byte strToByte(String s, byte d) {
		byte returnVal;
		try {
			returnVal = Byte.parseByte(safeTrimString(s));
		} catch (Exception e) {
			returnVal = d;
		}
		return returnVal;
	}

	/**
	 * String -> byte，如果转换不成功则返回 0
	 */
	public final static byte strToByte_0(String s) {
		return strToByte(s, (byte) 0);
	}

	/**
	 * String -> Character，如果转换不成功则返回 null
	 */
	public final static Character strToChar(String s) {
		Character returnVal;
		try {
			returnVal = safeTrimString(s).charAt(0);
		} catch (Exception e) {
			returnVal = null;
		}
		return returnVal;
	}

	/**
	 * 把参数 str 转换为安全字符串并执行去除前后空格：如果 str = null，则把它转换为空字符串
	 */
	public final static String safeTrimString(String str) {
		return safeString(str).trim();
	}

	/**
	 * 把参数 str 转换为安全字符串：如果 str = null，则把它转换为空字符串
	 */
	public final static String safeString(String str) {
		if (str == null)
			str = "";
		return str;
	}

	/**
	 * String -> char，如果转换不成功则返回默认值 d
	 */
	public final static char strToChar(String s, char d) {
		char returnVal;
		try {
			returnVal = safeTrimString(s).charAt(0);
		} catch (Exception e) {
			returnVal = d;
		}
		return returnVal;
	}

	/**
	 * String -> char，如果转换不成功则返回 0
	 */
	public final static char strToChar_0(String s) {
		return strToChar(s, Character.MIN_VALUE);
	}

	/**
	 * String -> Boolean，如果转换不成功则返回 null
	 */
	public final static Boolean strToBoolean(String s) {
		return Boolean.valueOf(safeTrimString(s));
	}

	/**
	 * String -> boolean，如果转换不成功则返回默认值 d
	 */
	public final static boolean strToBoolean(String s, boolean d) {
		s = safeTrimString(s);
		if (s.equalsIgnoreCase("true"))
			return true;
		else if (s.equalsIgnoreCase("false"))
			return false;
		return d;
	}

	/**
	 * String -> boolean，如果转换不成功则返回 0
	 */
	public final static boolean strToBoolean_False(String s) {
		return strToBoolean(s, false);
	}

}
