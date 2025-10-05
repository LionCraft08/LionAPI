package de.lioncraft.lionapi.guimanagement;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ScrollableInterface {
    public static HashMap<Inventory, ScrollableInterface> activeInterfaces;
    private Inventory inventory;
    private HashMap<Integer, ItemStack> content;
    boolean scrollPageWise;
    private int currentPage;
    private ItemStack backButton = null;

    public ScrollableInterface(List<ItemStack> content, @Nullable Component title, Player owner, boolean scrollPageWise, @Nullable ItemStack header) {
        this.content = new HashMap<>();
        currentPage = 0;

        createList(content);

        this.scrollPageWise = scrollPageWise;
        inventory = Bukkit.createInventory(owner, 54, Objects.requireNonNullElseGet(title, () -> Component.text("List")));
        inventory.setContents(Items.blockButtons);
        inventory.setItem(27, Items.closeButton);
        inventory.setItem(8, Items.scrollUpButton);
        inventory.setItem(53, Items.scrollDownButton);
        if(header != null){
            inventory.setItem(9, header);
        }
        activeInterfaces.put(inventory, this);
        updateGUI(0);
    }

    private void createList(List<ItemStack> content){
        this.content.clear();
        int i = 0;
        for(ItemStack is : content){
            i = getNextSlot(i);
            this.content.put(i, is);
        }
    }

    public void addContent(int place, ItemStack item){
        ArrayList<ItemStack> content = new ArrayList<>(getContent().values());
        content.add(place, item);
        setContent(content);
    }

    public void addContent(ItemStack item){
        ArrayList<ItemStack> content = new ArrayList<>(getContent().values());
        content.add(item);
        setContent(content);
    }

    public void removeContent(int place){
        ArrayList<ItemStack> content = new ArrayList<>(getContent().values());
        content.remove(place);
        setContent(content);
    }
    public void removeContent(ItemStack item){
        ArrayList<ItemStack> content = new ArrayList<>(getContent().values());
        content.remove(item);
        setContent(content);
    }

    public void setContent(List<ItemStack> list){
        createList(list);
        validateCurrentPage();
        updateGUI(currentPage);
    }

    private void validateCurrentPage(){
        int storage = currentPage;
        if (scrollPageWise){
            while (!content.containsKey(getNextSlot(currentPage*54))){
                if (currentPage==0) break;
                currentPage-=1;
            }
            if (currentPage != storage){
                updateGUI(currentPage);
            }
        }else{
            while (!content.containsKey(getNextSlot(currentPage*9))){
                if (currentPage==0) break;
                currentPage-=1;
            }
            if (currentPage != storage){
                updateGUI(currentPage);
            }
        }
    }

    public ItemStack getBackButton() {
        return backButton;
    }

    public void setBackButton(ItemStack backButton) {
        this.backButton = backButton;
        inventory.setItem(45, backButton);
    }

    private void updateGUI(int page){
        if(page == 0){
            inventory.setItem(8, Items.blockButton);
        }else{
            inventory.setItem(8, Items.scrollUpButton);
        }
        int slot = 0;
        boolean b = true;
        if(scrollPageWise){
            for(int i = getNextSlot(page*54); i < page*54+53; i = getNextSlot(i)){
                slot = getNextSlot(slot);
                ItemStack is = content.get(i);
                if(is == null){
                    inventory.setItem(53, Items.blockButton);
                    inventory.setItem(slot, Items.blockButton);
                    b = false;
                }else{
                    inventory.setItem(slot, is);
                }
            }
            if (b && !content.containsKey(getNextSlot(page*54+53))) b = false;
        }else{
            for(int i = getNextSlot(page*9); i < page*9+53; i = getNextSlot(i)){
                slot = getNextSlot(slot);
                ItemStack is = content.get(i);
                if(is == null){
                    inventory.setItem(53, Items.blockButton);
                    inventory.setItem(slot, Items.blockButton);
                    b = false;
                }else{
                    inventory.setItem(slot, is);
                }
            }
            if (b && !content.containsKey(getNextSlot(page*9+53))) b = false;
        }
        if(b){
            inventory.setItem(53, Items.scrollDownButton);
        }
    }
    private int getNextSlot(int currentSlot){
        if((currentSlot + 2)%9==0){
            return currentSlot + 3;
        }else if ((currentSlot + 1)%9==0){
            return currentSlot + 2;
        }else{
            return currentSlot + 1;
        }
    }
    public void scroll(boolean forward, boolean toStart){
        if(forward){
            if(toStart){
                if(scrollPageWise){
                    currentPage = (int) (content.size() -1) / 42;
                }else{
                    currentPage = ((int) (content.size() -1) / 7)-4;
                }

            }else{
                currentPage +=1;
            }
        }else{
            if(toStart){
                currentPage = 0;
            }else{
                currentPage -=1;
            }

        }
        updateGUI(currentPage);
    }

    public HashMap<Integer, ItemStack> getContent() {
        return content;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
