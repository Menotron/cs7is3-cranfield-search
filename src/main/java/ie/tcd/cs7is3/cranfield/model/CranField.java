package ie.tcd.cs7is3.cranfield.model;

public enum CranField {

    I(".I"), T(".T"), A(".A"), B(".B"), W(".W");

    private String field;

    CranField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static CranField fromName(String name) {
        for (CranField f : values()) {
            if (f.getField().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }
}
