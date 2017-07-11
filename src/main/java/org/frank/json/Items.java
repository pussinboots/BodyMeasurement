package org.frank.json;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(fluent = true)
@NoArgsConstructor
public class Items<T> {
    private Collection<T> items;
    private int size = 0;
    private int total = 0;
    private int page;

    public Items(Collection<T> items) {
        this.items = (items == null)? Collections.EMPTY_LIST: items;
        this.size = this.items.size();
    }

    public List<T> itemsAsList() {
        return new ArrayList<>(items());
    }
}
