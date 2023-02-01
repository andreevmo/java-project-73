package hexlet.code.app.domain.model;

public enum EStatus {

    NEW("Новый"),
    IN_WORK("В работе"),
    IN_TESTING("На тестировании"),
    CLOSED("Завершен");

    private final String name;

    EStatus(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }
}
