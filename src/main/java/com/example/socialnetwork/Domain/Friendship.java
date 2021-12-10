package com.example.socialnetwork.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> {

    Long ID1;
    Long ID2;
    LocalDateTime date;

    public Friendship(Long ID1, Long ID2) {
        this.ID1 = ID1;
        this.ID2 = ID2;
        this.date = LocalDateTime.now();
    }

    public Friendship(Long ID1, Long ID2, LocalDateTime date) {
        this.ID1 = ID1;
        this.ID2 = ID2;
        this.date = date;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public Long getID1() {
        return ID1;
    }

    public void setID1(Long ID1) {
        this.ID1 = ID1;
    }

    public Long getID2() {
        return ID2;
    }

    public void setID2(Long ID2) {
        this.ID2 = ID2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(ID1, that.ID1) && Objects.equals(ID2, that.ID2) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ID1, ID2, date);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", ID1=" + ID1 +
                ", ID2=" + ID2 +
                ", date=" + date +
                "}\n";
    }
}
