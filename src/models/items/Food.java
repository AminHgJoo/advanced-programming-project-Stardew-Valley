package models.items;

public class Food extends Item {
    public final boolean isEdible;

    public Food(boolean isEdible) {
        this.isEdible = isEdible;
    }

    @Override
    public void useItem() {

    }

    @Override
    public void deleteItem() {

    }

    @Override
    public void dropItem() {

    }
}
