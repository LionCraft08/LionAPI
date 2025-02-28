package de.lioncraft.lionapi.guimanagement.lioninventories;

import de.lioncraft.lionapi.LionAPI;
import de.lioncraft.lionapi.guimanagement.Interaction.Button;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LionInv {
    private List<Inventory> inventory = new ArrayList<>();
    private List<ItemStack> items = new ArrayList<>();
    private boolean autoSort, allowScrolling;


    public boolean isAutoSort() {
        return autoSort;
    }

    /**If true, the Inventory will automatically sort its content in a nice way to look better.
     * This may change the order of the Items.
     * @param autoSort WEther to apply auto sorting.
     */
    public void setAutoSort(boolean autoSort) {
        this.autoSort = autoSort;
    }

    public boolean isAllowScrolling() {
        return allowScrolling;
    }
    public void Update(){

    }
    /**WARNING!!! Changing this setting to false may delete Buttons from the Inventory!
     * @param allowScrolling Whether to allow multiple Pages, wich can be switched
     */
    public void setAllowScrolling(boolean allowScrolling) {
        this.allowScrolling = allowScrolling;
    }
    private static List<Integer> getSorting(int amountOfItems, boolean multipleInventories){
        List<Integer> list = new ArrayList<>();
        switch (amountOfItems){
            case 1 -> list.add(22);
            case 2 -> add(list, 21, 23);
            case 3 -> add(list, 20, 22, 24);
            case 4 -> add(list,19, 21, 23, 25);
            case 5 -> add(list, 11, 15, 21, 23, 31);
            case 6 -> add(list, 11, 13, 15, 21, 23, 31);
            case 7 -> add(list, 10, 12, 14, 16, 20, 22, 24);
            case 8 -> add(list, 10, 12, 14 ,16, 28, 30, 32, 34);
            case 9 -> add(list, 10, 12, 14, 16, 20, 22, 24, 30, 32);
            case 10 -> add(list, 10, 12, 14, 16, 20, 22, 24, 30, 32, 40);
            case 11 -> add(list, 10, 12, 14, 16, 20, 22, 24, 28, 30, 32, 34);
            case 12 -> add(list, 10, 12, 14, 16, 19, 21, 23, 25, 28, 30, 32, 34);
            case 13 -> add(list, 10, 12, 14, 16, 20, 22, 24, 28, 30, 32, 34, 38, 42);
            case 14 -> add(list, 10, 12, 14, 16, 20, 22, 24, 28, 30, 32, 34, 38, 40, 42);
            case 15 -> add(list, 10, 12, 13, 14, 16, 19, 21, 22, 23, 25, 28, 30, 31, 32, 34);
            case 16 -> add(list, 10, 12, 14, 16, 19, 21, 23, 25, 28, 30, 32, 34, 37, 39, 41, 43);
            case 17 -> add(list, 11, 12, 13, 14, 15, 19, 22, 25, 28, 30, 32, 34, 38, 39, 40, 41, 42);
            case 18 -> add(list, 11, 12, 13, 14, 15, 19, 21, 23, 25, 28, 30, 32, 34, 38, 39, 40, 41, 42);
            case 19 -> add(list, 11, 12, 13, 14, 15, 19, 20, 22, 24, 25, 28, 30, 32, 34, 38, 39, 40, 41, 42);
            case 20 -> add(list, 10, 11, 12, 13, 14, 15, 16, 20, 22, 24, 28, 29, 30, 31, 32, 33, 34, 38, 40, 42);
            case 22 -> add(list, 10, 11, 12, 13, 14, 15, 16, 19, 21, 23, 25, 28, 29, 30, 31, 32, 33, 34, 37, 39, 41, 43);
            case 24 -> add(list, 10, 11, 12, 14, 15, 16, 19, 20, 21, 23, 24, 25, 28, 29, 30, 32, 33, 34, 37, 38, 39, 41, 42, 43);
            case 26 -> add(list, 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 38, 39, 40, 41, 42);
        }
        if (list.isEmpty())
            if (amountOfItems > 0) {
                if (amountOfItems <= 28) {
                    int number = 10;
                    for (int i = 0; i < amountOfItems; i++) {
                        list.add(number);
                        number++;
                        if ((number + 2) % 9 == 0) {
                            number += 3;
                        }
                    }
                } else if (amountOfItems <= 45 && !multipleInventories){
                    for(int i = 0; i<amountOfItems;i++){
                        list.add(i);
                    }
                } else if (multipleInventories) {
                    int amountOfInvs = (int) Math.ceil(amountOfItems%28);
                    while (amountOfInvs > 0){
                        int number = 10;
                        for (int i = 0; i < amountOfItems; i++) {
                            list.add(number);
                            number++;
                            if ((number + 2) % 9 == 0) {
                                number += 3;
                            }
                        }
                        amountOfInvs--;
                    }
                }else{
                    for(int i = 0;i<45;i++){
                        list.add(i);
                    }
                    LionAPI.getPlugin().getComponentLogger().warn(Component.text("Used too many Items ("+amountOfItems+") for a single Inventory!"));
                }
            }
        return list;
    }
    private static void add(List<Integer> list, Integer... numbers){
        list.addAll(Arrays.asList(numbers));
    }
}
