package com.nilhcem.xebia.essentials.core.data.importer;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "xebia")
public class XmlData {

    @Path("categories")
    @ElementList(inline = true)
    List<XmlCategory> mCategories;

    @Path("cards")
    @ElementList(inline = true)
    List<XmlCard> mCards;

    public List<XmlCard> getCards() {
        return mCards;
    }

    public List<XmlCategory> getCategories() {
        return mCategories;
    }
}
