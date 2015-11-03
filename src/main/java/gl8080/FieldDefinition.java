package gl8080;

import gl8080.FieldDefinition.Builder;

public class FieldDefinition {

    private FieldPosition position;
    private String name;

    private FieldDefinition() {
    }

    public FieldDefinition(String name, FieldPosition position) {
        this.name = name;
        this.position = position;
    }

    public FieldPosition getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public static Builder newDefinition() {
        return new Builder();
    }
    
    public static class Builder {
        
        private FieldDefinition def;
        
        private Builder() {
            this.def = new FieldDefinition();
        }
        
        public Builder name(String name) {
            this.def.name = name;
            return this;
        }

        public Builder position(int left) {
            this.def.position = new FieldPosition(left);
            return this;
        }

        public FieldDefinition build() {
            return this.def;
        }

        public Builder position(int left, int length) {
            this.def.position = new FieldPosition(left, length);
            return this;
        }
    }
    
}
