package edition.attribute;

import edition.EditionData;

final public class EditionDataAttribute extends Attribute {

    private EditionData editionData;

    public EditionDataAttribute(EditionData.Key key) {
        super(key);
        this.editionData = null;
    }

    public EditionDataAttribute(EditionData.Key key, EditionData editionData) {
        super(key);
        this.editionData = editionData;
    }

    public EditionData getData() {
        return editionData;
    }

    public void setData(EditionData data) {
        this.editionData = data;
    }

    public boolean hasTypeSelect() {
        return editionData.hasTypeSelect();
    }
}
