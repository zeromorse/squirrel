package io.github.zeromorse.fsm.context.config.cache;

import java.util.Objects;

/**
 * from、to、event 三元组
 */
public class FteTriple {
    private String from;
    private String to;
    private String event;

    public FteTriple(String from, String to, String event) {
        this.from = from;
        this.to = to;
        this.event = event;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FteTriple fteTriple = (FteTriple) o;
        return from.equals(fteTriple.from) && to.equals(fteTriple.to) && event.equals(fteTriple.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, event);
    }

    @Override
    public String toString() {
        return from + "|" + to + "|" + event;
    }
}
