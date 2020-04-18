package ie.tcd.cs7is3.cranfield.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class QueryModel {
    public String queryid = "";
    public String query = "";

    public QueryModel(){}

    public QueryModel(String queryid, String query) {
        this.queryid = queryid;
        this.query = query;
    }
}
