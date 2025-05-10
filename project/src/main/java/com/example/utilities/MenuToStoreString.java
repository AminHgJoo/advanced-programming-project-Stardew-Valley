package com.example.utilities;

import com.example.models.enums.types.storeProductTypes.JojaMartProducts;
import com.example.views.Menu;
import com.example.views.gameViews.shops.*;

public class MenuToStoreString {
    public static String convertToString(Menu menu){
        if(menu instanceof BlacksmithShopMenu){
            return "Blacksmith";
        }else if(menu instanceof CarpenterShopMenu){
            return "Carpenter's Shop";
        }else if(menu instanceof FishShopMenu){
            return "Fish Shop";
        }else if(menu instanceof JojaMartShopMenu){
            return "Joja Mart";
        }else if(menu instanceof MarnieRanchMenu){
            return "Marnie's Shop";
        }else if(menu instanceof PierreGeneralStoreMenu){
            return "Pierre's General Store";
        }else if(menu instanceof TheStardropSaloonMenu){
            return "The Stardrop Saloon";
        }
        return null;
    }
}
