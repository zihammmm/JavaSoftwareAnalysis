public class PTACP {

    public static void main(String[] args) {
        Number n = new One();
        int x = n.get();
    }
}

interface Number {
    int get();
}

class Zero implements Number {
    public int get() {
        return 0;
    }
}

class One implements Number {
    public int get() {
        return 1;
    }
}

class Two implements Number {
    public int get() {
        return 2;
    }
}
