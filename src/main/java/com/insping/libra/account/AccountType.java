package com.insping.libra.account;

public enum AccountType {
    COMMON((byte) 0), EMAIL((byte) 1), PHONENUMBER((byte) 2);


    private byte value;

    private AccountType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public static AccountType searchType(byte type) {
        for (AccountType temp : AccountType.values()) {
            if (temp.getValue() == type)
                return temp;
        }
        return null;
    }

}
