package ie.tcd.cs7is3.cranfield.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class CranDocument {

    public String docid = "";
    public String title = "";
    public String author = "";
    public String biblio = "";
    public String words = "";


    public CranDocument(String docid, String title, String author, String biblio, String words) {
        this.docid = docid;
        this.title = title;
        this.author = author;
        this.biblio = biblio;
        this.words = words;
    }

    public CranDocument() {
    }

}
