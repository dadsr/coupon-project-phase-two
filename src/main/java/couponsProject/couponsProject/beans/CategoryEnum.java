package couponsProject.couponsProject.beans;

public enum CategoryEnum {
    ELECTRONICS
    ,FASHION
    ,FOOD
    ,TRAVEL;

    public static CategoryEnum fromId(int id) {
        for (CategoryEnum category:values() ) {
            if (category.ordinal() == id) {
                return category;
            }
        }
        return null;
    }
}
