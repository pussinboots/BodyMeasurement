package org.frank.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@XmlRootElement
@Data
@Accessors(fluent = true)
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class JSONResponse<T> {

    @lombok.Data
    @Accessors(fluent = true)
    @NoArgsConstructor
    @JsonInclude(NON_NULL)
    public static class Data<T> {
        private Integer size, total, page;
        private Collection<T> items;
        private T item;

        public List<T> asList() {
            return new ArrayList<>(items);
        }
    }

    @lombok.Data
    @Accessors(fluent = true)
    @NoArgsConstructor
    @JsonInclude(NON_NULL)
    public static class Paging {
        private String next, previous, current;
    }

    @lombok.Data
    @Accessors(fluent = true)
    @NoArgsConstructor
    public static class Meta {
        private Date requestedAt;
        private String responseGUID;
    }

    private Data<T> data;
    private Paging paging;
    private Meta meta;

    public JSONResponse() {
        this.meta = new Meta().requestedAt(Calendar.getInstance().getTime())
                            .responseGUID(UUID.randomUUID().toString());
    }

    public JSONResponse<T> items(List<T> items) {
        items = (items == null)? Collections.EMPTY_LIST: items;
        this.data(new Data().items(items).size(items.size())/*.total(total)*/);
        if (items.size() > 0) this.paging(new Paging());
        return this;
    }

    public JSONResponse<T> item(T item) {
        this.data(new Data().item(item));
        return this;
    }
}
