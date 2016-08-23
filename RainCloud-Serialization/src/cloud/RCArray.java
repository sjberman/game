package cloud;

public class RCArray extends RCBase {

	public static final byte CONTAINER_TYPE = ContainerType.ARRAY;
	public byte type;
	public int count;
	public byte[] data;

	private short[] shortData;
	private char[] charData;
	private int[] intData;
	private long[] longData;
	private float[] floatData;
	private double[] doubleData;
	private boolean[] booleanData;

	private RCArray() {
		size += 1 + 1 + 4;
	}

	private void updateSize() {
		size += getDataSize();
	}

	public int getBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = SerializationUtils.writeBytes(dest, pointer, nameLength);
		pointer = SerializationUtils.writeBytes(dest, pointer, name);
		pointer = SerializationUtils.writeBytes(dest, pointer, size);
		pointer = SerializationUtils.writeBytes(dest, pointer, type);
		pointer = SerializationUtils.writeBytes(dest, pointer, count);

		switch (type) {
		case Type.BYTE:
			pointer = SerializationUtils.writeBytes(dest, pointer, data);
			break;
		case Type.SHORT:
			pointer = SerializationUtils.writeBytes(dest, pointer, shortData);
			break;
		case Type.CHAR:
			pointer = SerializationUtils.writeBytes(dest, pointer, charData);
			break;
		case Type.INTEGER:
			pointer = SerializationUtils.writeBytes(dest, pointer, intData);
			break;
		case Type.LONG:
			pointer = SerializationUtils.writeBytes(dest, pointer, longData);
			break;
		case Type.FLOAT:
			pointer = SerializationUtils.writeBytes(dest, pointer, floatData);
			break;
		case Type.DOUBLE:
			pointer = SerializationUtils.writeBytes(dest, pointer, doubleData);
			break;
		case Type.BOOLEAN:
			pointer = SerializationUtils.writeBytes(dest, pointer, booleanData);
			break;
		}
		return pointer;
	}

	public int getSize() {
		return size;
	}

	public int getDataSize() {
		switch (type) {
		case Type.BYTE:
			return data.length * Type.getSize(Type.BYTE);
		case Type.SHORT:
			return shortData.length * Type.getSize(Type.SHORT);
		case Type.CHAR:
			return charData.length * Type.getSize(Type.CHAR);
		case Type.INTEGER:
			return intData.length * Type.getSize(Type.INTEGER);
		case Type.LONG:
			return longData.length * Type.getSize(Type.LONG);
		case Type.FLOAT:
			return floatData.length * Type.getSize(Type.FLOAT);
		case Type.DOUBLE:
			return doubleData.length * Type.getSize(Type.DOUBLE);
		case Type.BOOLEAN:
			return booleanData.length * Type.getSize(Type.BOOLEAN);
		}
		return 0;
	}

	public static RCArray Byte(String name, byte[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.BYTE;
		array.count = data.length;
		array.data = data;
		array.updateSize();
		return array;
	}

	public static RCArray Short(String name, short[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.SHORT;
		array.count = data.length;
		array.shortData = data;
		array.updateSize();
		return array;
	}

	public static RCArray Char(String name, char[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.CHAR;
		array.count = data.length;
		array.charData = data;
		array.updateSize();
		return array;
	}

	public static RCArray Integer(String name, int[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.INTEGER;
		array.count = data.length;
		array.intData = data;
		array.updateSize();
		return array;
	}

	public static RCArray Long(String name, long[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.LONG;
		array.count = data.length;
		array.longData = data;
		array.updateSize();
		return array;
	}

	public static RCArray Float(String name, float[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.FLOAT;
		array.count = data.length;
		array.floatData = data;
		array.updateSize();
		return array;
	}

	public static RCArray Double(String name, double[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.DOUBLE;
		array.count = data.length;
		array.doubleData = data;
		array.updateSize();
		return array;
	}

	public static RCArray Boolean(String name, boolean[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.BOOLEAN;
		array.count = data.length;
		array.booleanData = data;
		array.updateSize();
		return array;
	}

	public static RCArray Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert (containerType == CONTAINER_TYPE);

		RCArray result = new RCArray();
		result.nameLength = SerializationUtils.readShort(data, pointer);
		pointer += 2;
		result.name = SerializationUtils.readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;

		result.size = SerializationUtils.readInt(data, pointer);
		pointer += 4;

		result.type = data[pointer++];

		result.count = SerializationUtils.readInt(data, pointer);
		pointer += 4;

		switch (result.type) {
		case Type.BYTE:
			result.data = new byte[result.count];
			SerializationUtils.readBytes(data, pointer, result.data);
			break;
		case Type.SHORT:
			result.shortData = new short[result.count];
			SerializationUtils.readShorts(data, pointer, result.shortData);
			break;
		case Type.CHAR:
			result.charData = new char[result.count];
			SerializationUtils.readChars(data, pointer, result.charData);
			break;
		case Type.INTEGER:
			result.intData = new int[result.count];
			SerializationUtils.readInts(data, pointer, result.intData);
			break;
		case Type.LONG:
			result.longData = new long[result.count];
			SerializationUtils.readLongs(data, pointer, result.longData);
			break;
		case Type.FLOAT:
			result.floatData = new float[result.count];
			SerializationUtils.readFloats(data, pointer, result.floatData);
			break;
		case Type.DOUBLE:
			result.doubleData = new double[result.count];
			SerializationUtils.readDoubles(data, pointer, result.doubleData);
			break;
		case Type.BOOLEAN:
			result.booleanData = new boolean[result.count];
			SerializationUtils.readBooleans(data, pointer, result.booleanData);
			break;
		}

		pointer += result.count * Type.getSize(result.type);

		return result;
	}

}
