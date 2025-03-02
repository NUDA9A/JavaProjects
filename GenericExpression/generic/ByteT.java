package expression.generic;


public class ByteT implements GenericUtils<Byte> {
    @Override
    public Byte add(Byte a, Byte b) {
        return (byte) (a + b);
    }

    @Override
    public Byte parse(String s) throws Exception {
        return Byte.parseByte(s);
    }

    @Override
    public Byte negate(Byte a) {
        return (byte) -a;
    }

    @Override
    public Byte divide(Byte a, Byte b) {
        return (byte) (a / b);
    }

    @Override
    public Byte multiply(Byte a, Byte b) {
        return (byte) (a * b);
    }

    @Override
    public Byte subtract(Byte a, Byte b) {
        return (byte) (a - b);
    }

    @Override
    public Byte getValue(int value) {
        return (byte) value;
    }
}
