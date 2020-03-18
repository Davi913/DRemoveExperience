package com.davizin.removeexperience;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveEXP extends JavaPlugin {

    private List<EntityType> removedEntities = new ArrayList<>();

    @Override
    public void onEnable() {
        loadMobs();
        registerListeners();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void cancelExperience(EntityDeathEvent event) {
                if (removedEntities.contains(event.getEntity().getType())) event.setDroppedExp(0);
            }
        }, this);
    }

    private void loadMobs() {
        if (!new File("config.yml").exists()) saveDefaultConfig();
        if (!getConfig().contains("RemovedExperience")) getConfig().set("RemovedExperience", Arrays.asList("64", "99"));
        getConfig().getStringList("RemovedExperience").forEach(line -> {
            int id;
            try {
                id = Integer.parseInt(line);
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[DRemoveExperience] Não foi possível carregar o ID '" + line + "'");
                return;
            }
            EntityType entity = EntityType.fromId(id);
            if (entity == null) {
                Bukkit.getConsoleSender().sendMessage("[DRemoveExperience] Não foi possível carregar o ID '" + line + "'");
                return;
            }
            removedEntities.add(entity);
        });
    }
}
