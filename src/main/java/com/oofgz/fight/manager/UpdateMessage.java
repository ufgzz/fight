package com.oofgz.fight.manager;

import java.io.Serializable;

public class UpdateMessage implements Serializable {

    private Object key;
    private Object value;
    private Type type;

    private UpdateMessage() {
    }

    public UpdateMessage(Object key, Object value, Type type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public UpdateMessage(Object key, Type type) {
        this.key = key;
        this.type = type;
    }

    public UpdateMessage(Type type) {
        this.type = type;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    enum Type {
        PUT, PUTIFABSENT,REMOVE, CLEAN;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UpdateMessage{");
        sb.append("\"key\":")
                .append(key);
        sb.append(",\"value\":")
                .append(value);
        sb.append(",\"type\":")
                .append(type);

        sb.append('}');
        return sb.toString();
    }
}
