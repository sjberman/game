package cloud;

public class RCString extends RCBase {

	public static final byte CONTAINER_TYPE = ContainerType.STRING;
	public int count;
	private char[] characters;

	private RCString() {
		size += 1 + 4;
	}

	public String getString() {
		return new String(characters);
	}

	private void updateSize() {
		size += getDataSize();
	}

	public int getBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = SerializationUtils.writeBytes(dest, pointer, nameLength);
		pointer = SerializationUtils.writeBytes(dest, pointer, name);
		pointer = SerializationUtils.writeBytes(dest, pointer, size);
		pointer = SerializationUtils.writeBytes(dest, pointer, count);
		pointer = SerializationUtils.writeBytes(dest, pointer, characters);
		return pointer;
	}

	public int getSize() {
		return size;
	}

	public int getDataSize() {
		return characters.length * Type.getSize(Type.CHAR);
	}

	public static RCString Create(String name, String data) {
		RCString string = new RCString();
		string.setName(name);
		string.count = data.length();
		string.characters = data.toCharArray();
		string.updateSize();
		return string;
	}

	public static RCString Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert (containerType == CONTAINER_TYPE);

		RCString result = new RCString();
		result.nameLength = SerializationUtils.readShort(data, pointer);
		pointer += 2;
		result.name = SerializationUtils.readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;

		result.size = SerializationUtils.readInt(data, pointer);
		pointer += 4;

		result.count = SerializationUtils.readInt(data, pointer);
		pointer += 4;

		result.characters = new char[result.count];
		SerializationUtils.readChars(data, pointer, result.characters);

		pointer += result.count * Type.getSize(Type.CHAR);
		return result;
	}

}
