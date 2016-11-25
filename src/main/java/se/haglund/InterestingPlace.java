package se.haglund;

/**
 * Class describing an interesting place
 * currently only holds name and vicinity
 */
public class InterestingPlace {
    private String name;
    private String vicinity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterestingPlace that = (InterestingPlace) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return vicinity != null ? vicinity.equals(that.vicinity) : that.vicinity == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (vicinity != null ? vicinity.hashCode() : 0);
        return result;
    }
}
