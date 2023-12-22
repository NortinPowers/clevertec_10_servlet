package by.clevertec.proxy.repository.util;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Page<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int totalElements;
}
