package ie.tcd.cs7is3.cranfield.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class Query {
    public String queryid = "";
    public String query = "";

    public Query(){}

    public Query(String queryid, String query) {
        this.queryid = queryid;
        this.query = query;
    }
}
