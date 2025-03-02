package expression.generic;


public class IntegerT implements GenericUtils<Integer> {
    private final boolean overflowMode;
    @Override
    public Integer add(Integer a, Integer b) {
        if (overflowMode) {
            if (a > 0 && b > 0) {
                if (Integer.MAX_VALUE - b < a) {
                    throw new OverflowException();
                }
            } else if (a < 0 && b < 0) {
                if (Integer.MIN_VALUE - b > a) {
                    throw new OverflowException();
                }
            }
        }

        return a + b;
    }

    public IntegerT(boolean overflowMode) {
        this.overflowMode = overflowMode;
    }

    @Override
    public Integer parse(String s) throws ConstantOverflowException {
        int num;
        try {
            num = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            if (overflowMode) {
                if (s.charAt(0) == '-') {
                    throw new ConstantOverflowException(1);
                }
                throw new ConstantOverflowException(2);
            } else {
                long n = Long.parseLong(s);
                num = (int) n;
            }
        }

        return num;
    }

    @Override
    public Integer negate(Integer a) {
        if (overflowMode) {
            if (a == Integer.MIN_VALUE) {
                throw new OverflowException();
            }
        }

        return -a;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        if (b == 0) {
            throw new DivisionByZeroException();
        }
        if (overflowMode) {
            if (a == Integer.MIN_VALUE && b == -1) {
                throw new OverflowException();
            }
        }

        return a / b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        if (overflowMode) {
            if (a > 0 && b > 0) {
                if (Integer.MAX_VALUE / b < a) {
                    throw new OverflowException();
                }
            } else if (a < 0 && b < 0) {
                if (Integer.MAX_VALUE / b > a) {
                    throw new OverflowException();
                }
            } else if (a > 0) {
                if (Integer.MIN_VALUE / a > b) {
                    throw new OverflowException();
                }
            } else {
                if (Integer.MIN_VALUE / b > a) {
                    throw new OverflowException();
                }
            }
        }

        return a * b;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        if (overflowMode) {
            if (a > 0 && b < 0) {
                if (Integer.MAX_VALUE + b < a) {
                    throw new OverflowException();
                }
            } else if (a < 0 && b > 0) {
                if (Integer.MIN_VALUE + b > a) {
                    throw new OverflowException();
                }
            } else if (a == 0 && b == Integer.MIN_VALUE) {
                throw new OverflowException();
            }
        }

        return a - b;
    }

    @Override
    public Integer getValue(int value) {
        return value;
    }
}
