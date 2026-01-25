package de.lioncraft.lionapi.actions;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ActionSet implements ConfigurationSerializable {
    private HashMap<LionActions, Boolean> actions;
    private HashMap<LionAdvancedActions, String> advancedActions;
    private UUID uuid = UUID.randomUUID();
    private ActionInventory inventory;

    public ActionSet(Map<LionActions, Boolean> actions, Map<LionAdvancedActions, String> advancedActions, boolean goBackToChallengeController) {
        this.actions = new HashMap<>(actions);
        this.advancedActions = new HashMap<>(advancedActions);
        ActionManager.registerActionSet(this);
        inventory = new ActionInventory(this, goBackToChallengeController);
    }

    public UUID getUuid() {
        return uuid;
    }

    public ActionInventory getInventory() {
        return inventory;
    }

    public void addAction(LionActions action, boolean value) {
        actions.put(action, value);
    }
    public void addAdvancedAction(LionAdvancedActions action, String value) {
        advancedActions.put(action, value);
    }
    public void removeAction(LionActions action) {
        actions.remove(action);
    }
    public void removeAdvancedAction(LionAdvancedActions action) {
        advancedActions.remove(action);
    }
    public Boolean getActionValue(LionActions action) {
        return actions.get(action);
    }
    public String getActionValue(LionAdvancedActions action) {
        return advancedActions.get(action);
    }
    public void executeActions() {
        for (LionActions action : actions.keySet()) {
            action.onValueChange(getActionValue(action));
        }
        for (LionAdvancedActions action : advancedActions.keySet()) {
            action.onValueChange(getActionValue(action));
        }
    }

    public Set<LionActions> getPresentLionActions() {
        return actions.keySet();
    }

    public Set<LionAdvancedActions> getPresentLionAdvancedActions() {
        return advancedActions.keySet();
    }

    public ActionSet(Map<String, Object> map) {
        actions = new HashMap<>();
        advancedActions = new HashMap<>();
        HashMap<String, Object> actionSet = (HashMap<String, Object>) map.get("booleanActions");
        HashMap<String, Object> advancedActionSet = (HashMap<String, Object>) map.get("stringActions");
        uuid = UUID.fromString((String) map.get("uuid"));
        for (Map.Entry<String, Object> entry : actionSet.entrySet()) {
            actions.put(LionActions.valueOf(entry.getKey()), Boolean.parseBoolean(entry.getValue().toString()));
        }
        for (Map.Entry<String, Object> entry : advancedActionSet.entrySet()) {
            advancedActions.put(LionAdvancedActions.valueOf(entry.getKey()), (String) entry.getValue());
        }
        ActionManager.registerActionSet(this);
        inventory = new ActionInventory(this, true);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> actionSet = new HashMap<>();
        HashMap<String, Object> advancedActionSet = new HashMap<>();
        for (LionActions action : actions.keySet()) {
            actionSet.put(action.toString(), actions.get(action));
        }
        for (LionAdvancedActions action : advancedActions.keySet()) {
            advancedActionSet.put(action.toString(), advancedActions.get(action));
        }
        return new HashMap<>(Map.of(
                "booleanActions", actionSet,
                "stringActions", advancedActionSet,
                "uuid", uuid.toString()
        ));
    }


}
