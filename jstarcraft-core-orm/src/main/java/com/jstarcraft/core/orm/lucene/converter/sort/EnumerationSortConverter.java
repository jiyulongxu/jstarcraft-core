package com.jstarcraft.core.orm.lucene.converter.sort;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.util.BytesRef;

import com.jstarcraft.core.orm.lucene.annotation.SearchSort;
import com.jstarcraft.core.orm.lucene.converter.SearchContext;
import com.jstarcraft.core.orm.lucene.converter.SortConverter;

/**
 * 枚举排序转换器
 * 
 * @author Birdy
 *
 */
public class EnumerationSortConverter implements SortConverter {

    @Override
    public Iterable<IndexableField> convert(SearchContext context, String path, Field field, SearchSort annotation, Type type, Object data) {
        Collection<IndexableField> indexables = new LinkedList<>();
        indexables.add(new SortedDocValuesField(path, new BytesRef(data.toString())));
        return indexables;
    }

}
